package com.garm.modules.exam.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.garm.common.utils.Result;

import com.garm.modules.exam.constants.UserType;
import com.garm.modules.exam.dao.OfficialTestUserDao;
import com.garm.modules.exam.dao.OwnTestDao;

import com.garm.modules.sys.service.SysUserService;
import com.garm.modules.web.dao.UserDao;
import com.garm.modules.web.dto.shop.UserTestQueryDTO;
import com.garm.modules.web.dto.shop.WUserServiceDTO;
import com.garm.modules.web.dto.shop.WUserServiceQueryDTO;
import com.garm.modules.web.entity.UserEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Query;

import com.garm.modules.exam.dao.SysUserServiceDao;
import com.garm.modules.exam.entity.SysUserServiceEntity;
import com.garm.modules.exam.service.SysUserServiceService;
import org.springframework.util.CollectionUtils;


@Service("sysUserServiceService")
public class SysUserServiceServiceImpl extends ServiceImpl<SysUserServiceDao, SysUserServiceEntity> implements SysUserServiceService {

    @Autowired
    private OfficialTestUserDao officialTestUserDao;

    @Autowired
    private OwnTestDao ownTestDao;

    @Autowired
    private UserDao userDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String serviceType = (String)params.get("serviceType");
        String username = (String)params.get("username");
        IPage<SysUserServiceEntity> page = this.page(
                new Query<SysUserServiceEntity>().getPage(params),
                Wrappers.<SysUserServiceEntity>lambdaQuery()
                .eq(StringUtils.isNotBlank(serviceType),SysUserServiceEntity::getServiceType,serviceType)
                .orderByDesc(SysUserServiceEntity::getCreateTime)
        );

        return new PageUtils(page);
    }

    @Override
    public Result<List<WUserServiceDTO>> queryList(WUserServiceQueryDTO dto) {

        List<WUserServiceDTO> list = baseMapper.queryListByConditions(new Page(dto.getCurrentPage(),dto.getPageSize()),dto);
        List<WUserServiceDTO> collect = list.stream().map(e -> {
            if("1".equalsIgnoreCase(e.getStatus())){
                e.setStatus("已过期");
            }else{
                e.setStatus("未过期");
            }
            if("1".equalsIgnoreCase(e.getTypeName())){
                e.setTypeName("月付");
            }else{
                e.setTypeName("学期付");
            }
            UserTestQueryDTO userTestAnswerQueryDTO = new UserTestQueryDTO();
            //用户id
            userTestAnswerQueryDTO.setUserId(e.getUserId());
            //开始时间
            userTestAnswerQueryDTO.setStartTime(e.getBuyTime());
            //结束时间
            userTestAnswerQueryDTO.setEndTime(e.getMaturityTime());
            userTestAnswerQueryDTO.setServiceType(e.getServiceType());
            int officeCount = officialTestUserDao.queryOfficeCount(userTestAnswerQueryDTO);
            int ownCount = ownTestDao.queryOwnCount(userTestAnswerQueryDTO);
            e.setOfficeCount(officeCount);
            e.setOwnCount(ownCount);
            return e;
        }).collect(Collectors.toList());
        return Result.ok(collect);
    }

    @Override
    public void updateServiceType() {
        List<SysUserServiceEntity> list = baseMapper.queryServiceByTime();
        if(!CollectionUtils.isEmpty(list)){
            list.stream().forEach(e->{
                //1.修改用户服务状态为失效
                baseMapper.updateServiceTypeById(e.getServiceId());
                //2.修改用户服务类型为流失用户
                List<SysUserServiceEntity> SysUserServices = baseMapper.selectList(Wrappers.<SysUserServiceEntity>lambdaQuery()
                        .eq(SysUserServiceEntity::getUserId,e.getUserId())
                        .eq(SysUserServiceEntity::getStatus, 2));
                if(CollectionUtils.isEmpty(SysUserServices)){
                    //已经没有正在进行的服务了，需要修改账号为 流失用户
                    UserEntity user = new UserEntity();
                    user.setUserId(e.getUserId());
                    user.setUserType(UserType.OVER_USER);
                    userDao.updateById(user);
                }
            });

        }
    }


}
