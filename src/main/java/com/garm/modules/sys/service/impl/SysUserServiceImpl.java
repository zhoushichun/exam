

package com.garm.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.garm.common.exception.ResultException;
import com.garm.common.utils.Constant;
import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Query;
import com.garm.config.PwdConfig;
import com.garm.modules.exam.entity.PaperEntity;
import com.garm.modules.sys.dao.SysUserDao;
import com.garm.modules.sys.entity.SysUserEntity;
import com.garm.modules.sys.entity.SysUserRoleEntity;
import com.garm.modules.sys.service.SysRoleService;
import com.garm.modules.sys.service.SysUserRoleService;
import com.garm.modules.sys.service.SysUserService;
import io.swagger.annotations.ApiImplicitParam;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 系统用户
 *
 * @author
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysRoleService sysRoleService;

	@Autowired
	private PwdConfig pwdConfig;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String nickname = (String)params.get("nickname");
		String mobile = (String)params.get("mobile");
		String roleId = (String)params.get("roleId");
		Long createUserId = (Long)params.get("createUserId");

		Long page  = Long.valueOf((String)params.get("page"));
		Long limit  = Long.valueOf((String)params.get("limit"));

		IPage<SysUserEntity> result = baseMapper.queryList(new Page(page,limit),nickname,mobile,roleId,createUserId);

		return new PageUtils(result);
	}

	@Override
	public List<String> queryAllPerms(Long userId) {
		return baseMapper.queryAllPerms(userId);
	}

	@Override
	public List<Long> queryAllMenuId(Long userId) {
		return baseMapper.queryAllMenuId(userId);
	}

	@Override
	public SysUserEntity queryByUserName(String username) {
		return baseMapper.queryByUserName(username);
	}

	@Override
	@Transactional
	public void saveUser(SysUserEntity user,Long userId) {
		user.setCreateTime(new Date());
		//sha256加密
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setPassword(new Sha256Hash(pwdConfig.getDefaultPassword(), salt).toHex());
		user.setSalt(salt);
		this.save(user);

		//检查角色是否越权
		checkRole(user,userId);

		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}

	@Override
	@Transactional
	public void update(SysUserEntity user,Long userId) {
//		if(StringUtils.isBlank(user.getPassword())){
//			user.setPassword(null);
//		}else{
//			user.setPassword(new Sha256Hash(pwdConfig, user.getSalt()).toHex());
//		}

//		user.setPassword(new Sha256Hash(pwdConfig.getDefaultPassword(), user.getSalt()).toHex());

		this.updateById(user);

		//检查角色是否越权
		checkRole(user,userId);

		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}

	@Override
	public void deleteBatch(Long[] userId) {
		this.removeByIds(Arrays.asList(userId));
	}

	@Override
	public boolean updatePassword(Long userId, String password, String newPassword) {
		SysUserEntity userEntity = new SysUserEntity();
		userEntity.setPassword(newPassword);
		return this.update(userEntity,
				new QueryWrapper<SysUserEntity>().eq("user_id", userId).eq("password", password));
	}

	/**
	 * 检查角色是否越权
	 */
	private void checkRole(SysUserEntity user,Long userId){
		if(user.getRoleIdList() == null || user.getRoleIdList().size() == 0){
			return;
		}
		//如果不是超级管理员，则只查询自己创建的角色列表
		List<SysUserRoleEntity> userRoleEntities = sysUserRoleService.list(Wrappers.<SysUserRoleEntity>lambdaQuery().eq(SysUserRoleEntity::getUserId,userId));
		//系统管理员，拥有最高权限
		if( userRoleEntities.stream().filter(s->s.getRoleId()==Constant.SUPER_ADMIN).findFirst().isPresent()){
			return ;
		}

//		//如果不是超级管理员，则需要判断用户的角色是否自己创建
//		if(user.getCreateUserId() == Constant.SUPER_ADMIN){
//			return ;
//		}

		//查询用户创建的角色列表
		List<Long> roleIdList = sysRoleService.queryRoleIdList(user.getCreateUserId());

		//判断是否越权
		if(!roleIdList.containsAll(user.getRoleIdList())){
			throw new ResultException("新增用户所选角色，不是本人创建");
		}
	}
}
