

package com.garm.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.garm.common.exception.ResultException;
import com.garm.common.utils.Constant;
import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Query;
import com.garm.modules.sys.dao.SysRoleDao;
import com.garm.modules.sys.entity.SysRoleEntity;
import com.garm.modules.sys.entity.SysUserRoleEntity;
import com.garm.modules.sys.service.SysRoleMenuService;
import com.garm.modules.sys.service.SysRoleService;
import com.garm.modules.sys.service.SysUserRoleService;
import com.garm.modules.sys.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 角色
 *
 * @author
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String roleName = (String)params.get("roleName");
		Long createUserId = (Long)params.get("createUserId");

		IPage<SysRoleEntity> page = this.page(
			new Query<SysRoleEntity>().getPage(params),
			new QueryWrapper<SysRoleEntity>()
				.like(StringUtils.isNotBlank(roleName),"role_name", roleName)
				.eq(createUserId != null,"create_user_id", createUserId)
		);

		return new PageUtils(page);
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(SysRoleEntity role,Long userId) {
        role.setCreateTime(new Date());
        this.save(role);

        //检查权限是否越权
        checkPrems(role,userId);

        //保存角色与菜单关系
        sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysRoleEntity role,Long userId) {
        this.updateById(role);

        //检查权限是否越权
        checkPrems(role,userId);

        //更新角色与菜单关系
        sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] roleIds) {
        //删除角色
        this.removeByIds(Arrays.asList(roleIds));

        //删除角色与菜单关联
        sysRoleMenuService.deleteBatch(roleIds);

        //删除角色与用户关联
        sysUserRoleService.deleteBatch(roleIds);
    }


    @Override
	public List<Long> queryRoleIdList(Long createUserId) {
		return baseMapper.queryRoleIdList(createUserId);
	}

	/**
	 * 检查权限是否越权
	 */
	private void checkPrems(SysRoleEntity role,Long userId){
		//如果不是超级管理员，则需要判断角色的权限是否超过自己的权限
		List<SysUserRoleEntity> userRoleEntities = sysUserRoleService.list(Wrappers.<SysUserRoleEntity>lambdaQuery().eq(SysUserRoleEntity::getUserId,userId));
		//系统管理员，拥有最高权限
		if( userRoleEntities.stream().filter(s->s.getRoleId()==Constant.SUPER_ADMIN).findFirst().isPresent()){
			return ;
		}

		//查询用户所拥有的菜单列表
		List<Long> menuIdList = sysUserService.queryAllMenuId(role.getCreateUserId());

		//判断是否越权
		if(!menuIdList.containsAll(role.getMenuIdList())){
			throw new ResultException("新增角色的权限，已超出你的权限范围");
		}
	}
}
