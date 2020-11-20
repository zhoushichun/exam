

package com.garm.modules.sys.service.impl;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.garm.common.exception.ResultException;
import com.garm.common.redis.redislock.annotation.RedisLockAable;
import com.garm.common.utils.*;
import com.garm.common.utils.wechat.MD5Util;
import com.garm.common.validator.ValidatorUtils;
import com.garm.config.PwdConfig;
import com.garm.modules.exam.constants.DeleteConstant;
import com.garm.modules.exam.constants.DictConstant;
import com.garm.modules.exam.constants.UserType;
import com.garm.modules.exam.dao.UserTokenDao;
import com.garm.modules.exam.dto.library.FileUploadFailDTO;
import com.garm.modules.exam.dto.test.official.OfficialTestUserInfoDTO;
import com.garm.modules.exam.dto.user.FileUserFailInfoRespDTO;
import com.garm.modules.exam.dto.user.UserServiceUpdateDTO;
import com.garm.modules.exam.entity.SysUserServiceEntity;
import com.garm.modules.exam.enums.ReadUserFileDataType;
import com.garm.modules.exam.service.SysUserServiceService;
import com.garm.modules.sys.dto.UserServiceDTO;
import com.garm.modules.sys.entity.DictItemEntity;
import com.garm.modules.sys.service.DictItemService;
import com.garm.modules.sys.service.UserService;
import com.garm.modules.web.constants.PayTypeConstant;
import com.garm.modules.web.dao.UserDao;
import com.garm.modules.web.dto.login.LoginDTO;
import com.garm.modules.web.dto.UserCountInfoDTO;
import com.garm.modules.web.dto.login.RegisterDTO;
import com.garm.modules.web.entity.UserEntity;
import com.garm.modules.web.entity.UserTokenEntity;
import com.garm.modules.web.model.MessageModel;
import com.garm.modules.web.model.UserDTO;
import com.garm.modules.web.response.UserLoginResponseDTO;
import com.garm.modules.web.service.IMessageService;
import com.garm.modules.web.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Service("userService")
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

	//12小时后过期
	private final static int EXPIRE = 3600 * 12;

	@Autowired
	private DictItemService dictItemService;
	@Autowired
	private SysUserServiceService sysUserServiceService;
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private UserTokenDao userTokenDao;
	@Autowired
	private IMessageService messageService;
	@Autowired
	private UserService userService;

	@Autowired
	private PwdConfig pwdConfig;


	@Override
	public Result login(LoginDTO dto) {
		final UserEntity user = baseMapper
				.selectOne(Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getUsername, dto.getUsername()).eq(UserEntity::getIsDel,DeleteConstant.NOT_DELETE));

		if (null == user) {
			return Result.error("账号不存在");
		}
		if (!CipherUtil.verifyPassword(dto.getPassword(), user.getPassword(), user.getSalt())) {
			return Result.error("用户名或密码错误");
		}

		String token = jwtUtils.generateToken(user.getUserId());

		// 登录成功修改token状态
		UserTokenEntity userToken = userTokenDao
				.selectOne(Wrappers.<UserTokenEntity>lambdaQuery().eq(UserTokenEntity::getUserId, user.getUserId()));

		//过期时间
		Date expireTime = new Date(jwtUtils.getExpire());

		if (org.springframework.util.StringUtils.isEmpty(userToken)) {
			userToken = new UserTokenEntity();
			userToken.setUserId(user.getUserId());
			userToken.setExpireTime(expireTime);
			userToken.setUpdateTime(new Date());
			userToken.setToken(token);
			userTokenDao.insert(userToken);
		} else {
			userToken.setExpireTime(expireTime);
			userToken.setUpdateTime(new Date());
			userToken.setToken(token);
			userTokenDao.update(userToken,
					Wrappers.<UserTokenEntity>lambdaQuery().eq(UserTokenEntity::getUserId, userToken.getUserId()));
		}
		UserLoginResponseDTO responseDTO = new UserLoginResponseDTO();
		responseDTO.setToken(token);
		return Result.ok(responseDTO);
	}

	@Override
	public Result getTestUserInfo(Map<String, Object> params) {
		String serviceType = (String)params.get("serviceType");
		String username = (String)params.get("userName");
		String userIds = (String)params.get("userIds");
		String nickname = (String)params.get("nickname");
		String userType = (String)params.get("userType");

		String[] ids = StringUtils.isBlank(userIds)?null:userIds.split(",");
		if(StringUtils.isBlank((String)params.get("page"))){
			List<OfficialTestUserInfoDTO> result = baseMapper.listByServiceByServiceType(serviceType,username,ids, nickname, userType,null);
			if(null==result||result.size()==0){
				return Result.error("暂无服务用户数据");
			}

			result.stream().forEach(e->{
				String services = null;
				String serviceIds = null;
				String serviceTypes = null;
				Map<String,String> map = null;
				if(e.getUserType() != null && e.getUserType().intValue() == 2){
					map = baseMapper.queryServicePayingStrByUserId(e.getUserId());
				}else if(e.getUserType() != null && e.getUserType().intValue() == 3){
					map = baseMapper.queryServiceOverStrByUserId(e.getUserId());
				}
				if(map != null){
					services = map.get("services");
					serviceIds = map.get("serviceIds");
					serviceTypes = map.get("serviceTypes");
				}
				e.setServices(StringUtils.isBlank(services)?"无":services);
				e.setServiceIds(serviceIds);
				e.setServiceTypes(serviceTypes);
			});
			return Result.ok(result);
		}else{
			Long page = Long.valueOf((String)params.get("page"));
			Long limit = Long.valueOf((String)params.get("limit"));
			PageUtils<OfficialTestUserInfoDTO> result = new PageUtils<OfficialTestUserInfoDTO>(baseMapper.listByServiceByServiceType(new Page(page,limit),serviceType,username,ids, nickname, userType,null));
			if(null==result||result.getList().size()==0){
				return Result.error("暂无服务用户数据");
			}
			result.getList().stream().forEach(e->{
				String services = null;
				String serviceIds = null;
				String serviceTypes = null;
				Map<String,String> map = null;
				if(e.getUserType() != null && e.getUserType().intValue() == 2){
					map = baseMapper.queryServicePayingStrByUserId(e.getUserId());
				}else if(e.getUserType() != null && e.getUserType().intValue() == 3){
					map = baseMapper.queryServiceOverStrByUserId(e.getUserId());
				}
				if(map != null){
					services = map.get("services");
					serviceIds = map.get("serviceIds");
					serviceTypes = map.get("serviceTypes");
				}
				e.setServices(StringUtils.isBlank(services)?"无":services);
				e.setServiceIds(serviceIds);
				e.setServiceTypes(serviceTypes);
			});
			return Result.ok(result);
		}

	}

	@Override
	public List<OfficialTestUserInfoDTO> getTestUserInfo(List<Long> userId) {

		return baseMapper.listByServiceByServiceType(null,null,null, null, null,userId);
	}

	@Override
	public List<Map<String, Object>> export() {
		List<UserEntity> list = baseMapper.selectList(Wrappers.<UserEntity>lambdaQuery()
				.eq(UserEntity::getIsDel,DeleteConstant.NOT_DELETE)
				.orderByDesc(UserEntity::getCreateTime)
		);
		List<Map<String, Object>> data = list.stream().map(e->{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("nickname",e.getNickname());
			map.put("username",e.getUsername());
			switch (e.getUserType()){
				case UserType.COMMON_USER:
					map.put("serviceType","普通用户");
					break;
				case UserType.PAYING_USER:
					map.put("serviceType","付费用户");
					break;
				case UserType.OVER_USER:
					map.put("serviceType","流失用户");
					break;
				default:
					map.put("serviceType","");

			}

			if(e.getUserType() != null && e.getUserType().intValue() == 2){
				map.put("services",baseMapper.queryServicePayingStrByUserId(e.getUserId()));
			}else if(e.getUserType() != null && e.getUserType().intValue() == 3){
				map.put("services",baseMapper.queryServiceOverStrByUserId(e.getUserId()));
			}else {
				map.put("services", "无");
			}
			return map;
		}).collect(Collectors.toList());

		return data;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@RedisLockAable(root = "sys::upload::", key = "#{file.originalFilename}")
	public Result<FileUploadFailDTO> upload(MultipartFile file) {
		if (file != null ) {
			//以原来的名称命名,覆盖掉旧的
			String fileName = ExcelUtil.getFileName(file.getOriginalFilename());

			if (ExcelUtil.isExcel2003(fileName)||ExcelUtil.isExcel2007(fileName)) {

				//成功数量
				AtomicInteger successNum = new AtomicInteger(0);
				//失败数量
				AtomicInteger failNum = new AtomicInteger(0);

				List<FileUserFailInfoRespDTO> failDatas = new ArrayList<FileUserFailInfoRespDTO>();

				try(InputStream in = file.getInputStream()){
					//读取文件流数据
					List<List<String>> results = ExcelUtil.readOne(in,ExcelUtil.isExcel2003(fileName));


					List<SysUserServiceEntity> saveData = new ArrayList<>();
					AtomicInteger rowNum = new AtomicInteger(1);

					results.stream().forEach(m->{
						FileUserFailInfoRespDTO dto = new FileUserFailInfoRespDTO();
						SysUserServiceEntity sysUserService = new SysUserServiceEntity();

						String nickName = m.get(ReadUserFileDataType.NICK_NAME.getCode());
						String mobile = m.get(ReadUserFileDataType.MOBILE.getCode());
						String serviceTypeName = m.get(ReadUserFileDataType.SERVICE_TYPE_NAME.getCode());
						String serviceStartTime = m.get(ReadUserFileDataType.SERVICE_START_TIME.getCode());
						String serviceEndTime = m.get(ReadUserFileDataType.SERVICE_END_TIME.getCode());
						String type = null;
						if(m.size()-1==ReadUserFileDataType.TYPE.getCode()){
							type = m.get(ReadUserFileDataType.TYPE.getCode());
						}


						dto.setNickName(nickName);
						dto.setRowNum(rowNum.intValue());
						if(StringUtils.isBlank(serviceStartTime)&&StringUtils.isBlank(serviceEndTime)){

						}else if(StringUtils.isBlank(serviceStartTime)){
							failNum.getAndIncrement();
							dto.setReason("服务开始时间为空");
							failDatas.add(dto);
							rowNum.getAndIncrement();
							dto.setRowNum(rowNum.intValue());
							return ;
						}else if(StringUtils.isBlank(serviceEndTime)){
							failNum.getAndIncrement();
							dto.setReason("服务结束时间为空");
							failDatas.add(dto);
							rowNum.getAndIncrement();
							dto.setRowNum(rowNum.intValue());
							return ;
						}
						if(org.springframework.util.StringUtils.isEmpty(mobile)){
							failNum.getAndIncrement();
							dto.setReason("用户电话号码不能为空");
							failDatas.add(dto);
							rowNum.getAndIncrement();
							dto.setRowNum(rowNum.intValue());
							return ;
						}
						if(org.springframework.util.StringUtils.isEmpty(type)){
							failNum.getAndIncrement();
							dto.setReason("支付方式不能为空");
							failDatas.add(dto);
							rowNum.getAndIncrement();
							dto.setRowNum(rowNum.intValue());
							return ;
						}else if("月支付".equals(type)){
							sysUserService.setType(1);
						}else if("学期付".equals(type)){
							sysUserService.setType(2);
						}else{
							failNum.getAndIncrement();
							dto.setReason("支付方式暂时只支持月支付或学期付");
							failDatas.add(dto);
							rowNum.getAndIncrement();
							dto.setRowNum(rowNum.intValue());
							return ;
						}

						DictItemEntity dictItem = dictItemService.getOne(Wrappers.<DictItemEntity>lambdaQuery().eq(DictItemEntity::getDictId, DictConstant.SERVICE_TYPE).eq(DictItemEntity::getItemName,serviceTypeName));

						if(org.springframework.util.StringUtils.isEmpty(serviceTypeName)){
							failNum.getAndIncrement();
							dto.setReason("服务类型不能为空");
							failDatas.add(dto);
							rowNum.getAndIncrement();
							dto.setRowNum(rowNum.intValue());

							return ;
						}else if(null == dictItem ) {
							failNum.getAndIncrement();
							dto.setReason("服务类型不存在");
							failDatas.add(dto);
							rowNum.getAndIncrement();
							dto.setRowNum(rowNum.intValue());
							return ;
						}

						UserEntity user = baseMapper.selectOne(Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getMobile,mobile));

						if(null == user){
							user= new UserEntity();
							user.setMobile(mobile);
							user.setUserId(IdUtil.genId());
							user.setUsername(mobile);
							user.setNickname(nickName);
							user.setSalt(CipherUtil.getSalt());
							user.setPassword(CipherUtil.encryptPassword(MD5Util.MD5Encode(pwdConfig.getDefaultPassword(),null), user.getSalt()));
							user.setCreateTime(new Date());
							user.setIsDel(DeleteConstant.NOT_DELETE);
							try{
								ValidatorUtils.validateEntity(user);
							}catch (ResultException ex){
								failNum.getAndIncrement();
								dto.setReason(ex.getMsg());
								failDatas.add(dto);
								rowNum.getAndIncrement();
								dto.setRowNum(rowNum.intValue());
								return ;
							}
							userService.save(user);
						}
						sysUserService.setUserId(user.getUserId());
						sysUserService.setCreateTime(new Date());
						sysUserService.setServiceType(dictItem.getDictItemId());
						try{
							sysUserService.setCreateTime(DateUtils.stringToDate(serviceStartTime,DateUtils.DATE_PATTERN));
							sysUserService.setExpireTime(DateUtils.stringToDate(serviceEndTime, DateUtils.DATE_PATTERN));
						}catch (Exception e){
							failNum.getAndIncrement();
							dto.setReason("服务时间格式有误");
							failDatas.add(dto);
							rowNum.getAndIncrement();
							dto.setRowNum(rowNum.intValue());
							return ;
						}

						sysUserService.setStatus(2);
						sysUserService.setUpdateTime(new Date());
						sysUserService.setOrderId(0L);
						if(StringUtils.isNotBlank(serviceEndTime)){
							if(sysUserService.getExpireTime().getTime()>=new Date().getTime()){
								user.setUserType(UserType.PAYING_USER);
							}else{
								user.setUserType(UserType.OVER_USER);
							}
						}else{
							user.setUserType(UserType.COMMON_USER);
						}

						userService.updateById(user);

						sysUserServiceService.remove(Wrappers.<SysUserServiceEntity>lambdaQuery().eq(SysUserServiceEntity::getUserId,user.getUserId())
								.eq(SysUserServiceEntity::getStatus,2)
								.eq(SysUserServiceEntity::getServiceType,sysUserService.getServiceType())
						);

						saveData.add(sysUserService);
						successNum.getAndIncrement();
						rowNum.getAndIncrement();
					});
					sysUserServiceService.saveBatch(saveData);

				}catch (ResultException ae){
					log.info(ae.getMessage());
					throw ae;
				}catch (Exception excelException){
					excelException.printStackTrace();
					log.info(excelException.getMessage());
					throw new ResultException("文件数据上传失败!");
				}

				FileUploadFailDTO map = new FileUploadFailDTO();
				map.setSuccessNum(successNum.intValue());
				map.setFailNum(failNum.intValue());
				map.setFailDatas(failDatas);
				return Result.ok(map);
			}else{
				throw new ResultException("请选择xls或xlsx文件");
			}
		}
		return Result.error("文件不存在");
	}

	@Override
	public Result<UserServiceDTO> queryUserService(Long userId) {
		List<SysUserServiceEntity> list = sysUserServiceService.list(Wrappers.<SysUserServiceEntity>lambdaQuery()
				.eq(SysUserServiceEntity::getUserId,userId)
				.eq(SysUserServiceEntity::getStatus, 2));

		List<DictItemEntity> items = dictItemService.list();
		List<UserServiceDTO> result = list.stream().map(e->{
			UserServiceDTO responseDTO = new UserServiceDTO();
			BeanUtils.copyProperties(e,responseDTO);
			int count = (int)items.stream().filter(m->m.getDictItemId().equals(e.getServiceType())).count();
			if(count !=0){
				responseDTO.setServiceTypeName( items.stream().filter(m->m.getDictItemId().equals(e.getServiceType())).findFirst().get().getItemName());
			}
			return responseDTO;
		}).collect(Collectors.toList());
		return Result.ok(result);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@RedisLockAable(root = "sys::update::", key = "#{dto.type}")
	public Result updateUserService(UserServiceUpdateDTO dto) {
		String[] str = null;
		if(dto.getType().intValue() == 1){
			//批量修改用户服务
			if(StringUtils.isBlank(dto.getUserIds())){
				return Result.error("请选择批量修改的用户");
			}
			str = dto.getUserIds().split(",");
			System.out.println(dto.getUserIds().indexOf(","));
		}else{
			//单个修改用户服务
			str = new String[]{dto.getUserIds()};
		}

		try {
			Arrays.stream(str).forEach(e->{
				if(StringUtils.isNotBlank(e)){
					Long userId = Long.parseLong(e);
					Integer count = sysUserServiceService.count(Wrappers.<SysUserServiceEntity>lambdaQuery()
							.eq(SysUserServiceEntity::getStatus,2)
							.eq(SysUserServiceEntity::getUserId,e)
					);
					if(count>1&&dto.getType()==1){
						throw new ResultException("某条数据包含多个服务,无法进行批量调整");
					}

					//单个修改用户服务
					SysUserServiceEntity sysUserService = new SysUserServiceEntity();
					sysUserService.setServiceId(IdUtil.genId());
					sysUserService.setUserId(userId);
					sysUserService.setUpdateTime(new Date());
					sysUserService.setCreateTime(new Date());
					sysUserService.setOrderId(0L);
					sysUserService.setType(1);
					if(StringUtils.isNotBlank(dto.getTimeStr())){
						sysUserService.setExpireTime(DateUtils.stringToDate(dto.getTimeStr(),DateUtils.DATE_PATTERN));
						if(sysUserService.getExpireTime().getTime()>=new Date().getTime()){
							sysUserService.setStatus(2);
						}else{
							sysUserService.setStatus(1);
						}
					}else{
						sysUserService.setStatus(1);
					}
					if(dto.getServiceType() != null){
						sysUserService.setServiceType(dto.getServiceType());
					}
					sysUserServiceService.remove(Wrappers.<SysUserServiceEntity>lambdaQuery().eq(SysUserServiceEntity::getUserId,userId).eq(SysUserServiceEntity::getStatus,2));
					sysUserServiceService.save(sysUserService);


					UserEntity user = userService.getById(userId);
					if(DateUtils.stringToDate(dto.getTimeStr(),DateUtils.DATE_PATTERN).getTime()>=new Date().getTime()){
						user.setUserType(UserType.PAYING_USER);
					}else{
						user.setUserType(UserType.OVER_USER);
					}
					userService.updateById(user);
				}
			});
			return Result.ok();
		}catch(ResultException ex){
			log.error("调整服务失败：异常：--->" +ex.getMsg());
			return Result.error(ex.getMsg());
		}catch (Exception e){
			log.error("调整服务失败：异常：--->" +e.getMessage());
			return Result.error("调整服务失败");
		}
	}

	@Override
	public Result<UserCountInfoDTO> getUserTestCount(UserDTO user) {
		return Result.ok(baseMapper.selcetTestTotal(user.getUserId().toString()));
	}


	@Override
	public PageUtils<UserEntity> queryPage(Map<String, Object> params) {
		String nickname = (String)params.get("nickname");
		String username = (String)params.get("username");
		String userType = (String)params.get("userType");
		IPage<UserEntity> page = this.page(
				new Query<UserEntity>().getPage(params),
				Wrappers.<UserEntity>lambdaQuery()
						.like(!StringUtils.isBlank(nickname),UserEntity::getNickname,nickname)
						.like(!StringUtils.isBlank(username),UserEntity::getUsername,username)
						.eq(!StringUtils.isBlank(userType),UserEntity::getUserType,userType)
						.orderByDesc(UserEntity::getCreateTime)
		);

		return new PageUtils(page);
	}

	@Override
	public boolean checkCode(String phone,String code,String type){
		MessageModel model = new MessageModel();
		model.setPhone(phone);
		model.setType(type);
		model.setCode(code);
		int result = messageService.checkCode(model);
		if(result == 2){
			return false;
		}else if(result == 0){
			return true;
		}else{
			throw new ResultException("验证码错误");
		}
	}
}
