

package com.garm.modules.sys.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.R;
import com.garm.common.annotation.SysLog;
import com.garm.common.exception.ResultException;
import com.garm.common.utils.Constant;
import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Result;
import com.garm.common.validator.ValidatorUtils;
import com.garm.modules.sys.entity.SysRoleEntity;
import com.garm.modules.sys.entity.SysUserRoleEntity;
import com.garm.modules.sys.service.SysRoleMenuService;
import com.garm.modules.sys.service.SysRoleService;
import com.garm.modules.sys.service.SysUserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 * @author
 */
@RestController
@RequestMapping("/sys/role")
@Api(tags = "后台-系统管理-角色管理")
public class SysRoleController extends AbstractController {
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysUserRoleService sysUserRoleService;

	/**
	 * 角色列表
	 */
	@GetMapping("/list")
	@ApiOperation("角色列表")
	@RequiresPermissions("sys:role:list")
	public Result<Map> list(@RequestParam Map<String, Object> params){
		//如果不是超级管理员，则只查询自己创建的角色列表
		List<SysUserRoleEntity> userRoleEntities = sysUserRoleService.list(Wrappers.<SysUserRoleEntity>lambdaQuery().eq(SysUserRoleEntity::getUserId,getUserId()));
		//系统管理员，拥有最高权限
		if( !userRoleEntities.stream().filter(s->s.getRoleId()==Constant.SUPER_ADMIN).findFirst().isPresent()){
			params.put("createUserId", getUserId());
		}

		PageUtils page = sysRoleService.queryPage(params);
		Map<String,Object> map = new HashMap<>();
		map.put("page", page);
		return Result.ok(map);
	}

	/**
	 * 角色列表
	 */
	@GetMapping("/select")
	@ApiOperation("角色列表")
	@RequiresPermissions("sys:role:select")
	public Result<Map> select(){
		Map<String, Object> map = new HashMap<>();

		//如果不是超级管理员，则只查询自己创建的角色列表
		List<SysUserRoleEntity> userRoleEntities = sysUserRoleService.list(Wrappers.<SysUserRoleEntity>lambdaQuery().eq(SysUserRoleEntity::getUserId,getUserId()));
		//系统管理员，拥有最高权限
		if( !userRoleEntities.stream().filter(s->s.getRoleId()==Constant.SUPER_ADMIN).findFirst().isPresent()){
			map.put("create_user_id", getUserId());
		}
		List<SysRoleEntity> list = (List<SysRoleEntity>) sysRoleService.listByMap(map);
		Map<String,Object> result = new HashMap<>();
		result.put("list", list);
		return Result.ok(result);
	}

	/**
	 * 角色信息
	 */
	@GetMapping("/info/{roleId}")
	@ApiOperation("角色信息")
	@RequiresPermissions("sys:role:info")
	public Result<Map> info(@PathVariable("roleId") Long roleId){
		SysRoleEntity role = sysRoleService.getById(roleId);

		//查询角色对应的菜单
		List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
		role.setMenuIdList(menuIdList);
		Map<String,Object> result = new HashMap<>();
		result.put("role", role);
		return Result.ok(result);
	}

	/**
	 * 保存角色
	 */
	@SysLog("保存角色")
	@PostMapping("/save")
	@ApiOperation("保存角色")
	@RequiresPermissions("sys:role:save")
	public Result save(@RequestBody SysRoleEntity role){
		ValidatorUtils.validateEntity(role);

		role.setCreateUserId(getUserId());
		sysRoleService.saveRole(role,getUserId());

		return Result.ok();
	}

	/**
	 * 修改角色
	 */
	@SysLog("修改角色")
	@PostMapping("/update")
	@ApiOperation("修改角色")
	@RequiresPermissions("sys:role:update")
	public Result update(@RequestBody SysRoleEntity role){
		ValidatorUtils.validateEntity(role);

		role.setCreateUserId(getUserId());
		sysRoleService.update(role,getUserId());

		return Result.ok();
	}

	/**
	 * 删除角色
	 */
	@SysLog("删除角色")
	@PostMapping("/delete")
	@ApiOperation("删除角色")
	@RequiresPermissions("sys:role:delete")
	public Result delete(@RequestBody Long[] roleIds){
		if(Arrays.stream(roleIds).filter(s->s==Constant.SUPER_ADMIN).findFirst().isPresent()){
			throw new ResultException("系统管理员无法删除!");
		}
		sysRoleService.deleteBatch(roleIds);

		return Result.ok();
	}
}
