

package com.garm.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.garm.common.utils.Constant;
import com.garm.modules.sys.dao.SysMenuDao;
import com.garm.modules.sys.dao.SysUserDao;
import com.garm.modules.sys.dao.SysUserTokenDao;
import com.garm.modules.sys.entity.SysMenuEntity;
import com.garm.modules.sys.entity.SysUserEntity;
import com.garm.modules.sys.entity.SysUserRoleEntity;
import com.garm.modules.sys.entity.SysUserTokenEntity;
import com.garm.modules.sys.service.ShiroService;
import com.garm.modules.sys.service.SysUserRoleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShiroServiceImpl implements ShiroService {
    @Autowired
    private SysMenuDao sysMenuDao;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysUserTokenDao sysUserTokenDao;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public Set<String> getUserPermissions(long userId) {
        List<String> permsList;

        List<SysUserRoleEntity> userRoleEntities = sysUserRoleService.list(Wrappers.<SysUserRoleEntity>lambdaQuery().eq(SysUserRoleEntity::getUserId,userId));

        //系统管理员，拥有最高权限
        if( userRoleEntities.stream().filter(s->s.getRoleId()==Constant.SUPER_ADMIN).findFirst().isPresent()){
            List<SysMenuEntity> menuList = sysMenuDao.selectList(null);
            permsList = new ArrayList<>(menuList.size());
            for(SysMenuEntity menu : menuList){
                permsList.add(menu.getPerms());
            }
        }else{
            permsList = sysUserDao.queryAllPerms(userId);
        }
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for(String perms : permsList){
            if(StringUtils.isBlank(perms)){
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }

    @Override
    public SysUserTokenEntity queryByToken(String token) {
        return sysUserTokenDao.queryByToken(token);
    }

    @Override
    public SysUserEntity queryUser(Long userId) {
        return sysUserDao.selectById(userId);
    }
}
