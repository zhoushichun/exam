

package com.garm.modules.sys.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.garm.common.annotation.SysLog;
import com.garm.common.exception.ResultException;
import com.garm.common.utils.Constant;
import com.garm.common.utils.Result;
import com.garm.modules.sys.entity.SysMenuEntity;
import com.garm.modules.sys.entity.SysRoleMenuEntity;
import com.garm.modules.sys.entity.SysUserRoleEntity;
import com.garm.modules.sys.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 系统菜单
 *
 * @author
 */
@RestController
@RequestMapping("/sys/menu")
@Api(tags = "后台-系统管理-系统菜单")
public class SysMenuController extends AbstractController {
	@Autowired
	private SysMenuService sysMenuService;
	@Autowired
	private ShiroService shiroService;


	@Autowired
	private SysUserRoleService sysUserRoleService;

	@Autowired
	private SysRoleMenuService sysRoleMenuService;

	/**
	 * 导航菜单
	 */
	@GetMapping("/nav")
	@ApiOperation("导航菜单")
	public Result<Map> nav(){
		List<SysMenuEntity> menuList = sysMenuService.getUserMenuList(getUserId());
		Set<String> permissions = shiroService.getUserPermissions(getUserId());

		Map<String,Object> map = new HashMap<>();
		map.put("menuList", menuList);
		map.put("permissions", permissions);
		return Result.ok(map);
	}

	/**
	 * 所有菜单列表
	 */
	@GetMapping("/list")
	@ApiOperation("所有菜单列表")
	@RequiresPermissions("sys:menu:list")
	public List<SysMenuEntity> list(){

		List<SysUserRoleEntity> roleEntity = sysUserRoleService.list(Wrappers.<SysUserRoleEntity>lambdaQuery().eq(SysUserRoleEntity::getUserId,getUserId()));
		Set<Long> menuIds = new HashSet<>();
		roleEntity.stream().forEach(s->{
			List<SysRoleMenuEntity> roleMenuEntities= sysRoleMenuService.list(Wrappers.<SysRoleMenuEntity>lambdaQuery().eq(SysRoleMenuEntity::getRoleId, s.getRoleId()));
			roleMenuEntities.forEach(m->{
				menuIds.add(m.getMenuId());
			});
		});
		List<SysMenuEntity> menuList = sysMenuService.list(Wrappers.<SysMenuEntity>lambdaQuery()
				.in(getUserId()!=1,SysMenuEntity::getMenuId,menuIds)
				.orderByAsc(SysMenuEntity::getOrderNum));
		for(SysMenuEntity sysMenuEntity : menuList){
			SysMenuEntity parentMenuEntity = sysMenuService.getById(sysMenuEntity.getParentId());
			if(parentMenuEntity != null){
				sysMenuEntity.setParentName(parentMenuEntity.getName());
			}
		}

		return menuList;
	}

	/**
	 * 选择菜单(添加、修改菜单)
	 */
	@GetMapping("/select")
	@ApiOperation("选择菜单(添加、修改菜单)")
	@RequiresPermissions("sys:menu:select")
	public Result<Map> select(){
		//查询列表数据
		List<SysMenuEntity> menuList = sysMenuService.queryNotButtonList();

		//添加顶级菜单
		SysMenuEntity root = new SysMenuEntity();
		root.setMenuId(0L);
		root.setName("一级菜单");
		root.setParentId(-1L);
		root.setOpen(true);
		menuList.add(root);

		Map<String,Object> map = new HashMap<>();
		map.put("menuList", menuList);
		return Result.ok(map);
	}

	/**
	 * 菜单信息
	 */
	@GetMapping("/info/{menuId}")
	@ApiOperation("菜单信息")
	@RequiresPermissions("sys:menu:info")
	public Result<Map> info(@PathVariable("menuId") Long menuId){
		SysMenuEntity menu = sysMenuService.getById(menuId);
		Map<String,Object> map = new HashMap<>();
		map.put("menu", menu);
		return Result.ok(map);
	}

	/**
	 * 保存
	 */
	@SysLog("保存菜单")
	@PostMapping("/save")
	@ApiOperation("保存菜单")
	@RequiresPermissions("sys:menu:save")
	public Result save(@RequestBody SysMenuEntity menu){
		//数据校验
		verifyForm(menu);

		sysMenuService.save(menu);

		return Result.ok();
	}

	/**
	 * 修改
	 */
	@SysLog("修改菜单")
	@PostMapping("/update")
	@ApiOperation("修改菜单")
	@RequiresPermissions("sys:menu:update")
	public Result update(@RequestBody SysMenuEntity menu){
		//数据校验
		verifyForm(menu);

		sysMenuService.updateById(menu);

		return Result.ok();
	}

	/**
	 * 删除
	 */
	@SysLog("删除菜单")
	@PostMapping("/delete/{menuId}")
	@ApiOperation("删除菜单")
	@RequiresPermissions("sys:menu:delete")
	public Result delete(@PathVariable("menuId") long menuId){
		if(menuId <= 31){
			return Result.error("系统菜单，不能删除");
		}

		//判断是否有子菜单或按钮
		List<SysMenuEntity> menuList = sysMenuService.queryListParentId(menuId);
		if(menuList.size() > 0){
			return Result.error("请先删除子菜单或按钮");
		}

		sysMenuService.delete(menuId);

		return Result.ok();
	}

	/**
	 * 验证参数是否正确
	 */
	private void verifyForm(SysMenuEntity menu){
		if(StringUtils.isBlank(menu.getName())){
			throw new ResultException("菜单名称不能为空");
		}

		if(menu.getParentId() == null){
			throw new ResultException("上级菜单不能为空");
		}

		//菜单
		if(menu.getType() == Constant.MenuType.MENU.getValue()){
			if(StringUtils.isBlank(menu.getUrl())){
				throw new ResultException("菜单URL不能为空");
			}
		}

		//上级菜单类型
		int parentType = Constant.MenuType.CATALOG.getValue();
		if(menu.getParentId() != 0){
			SysMenuEntity parentMenu = sysMenuService.getById(menu.getParentId());
			parentType = parentMenu.getType();
		}

		//目录、菜单
		if(menu.getType() == Constant.MenuType.CATALOG.getValue() ||
				menu.getType() == Constant.MenuType.MENU.getValue()){
			if(parentType != Constant.MenuType.CATALOG.getValue()){
				throw new ResultException("上级菜单只能为目录类型");
			}
			return ;
		}

		//按钮
		if(menu.getType() == Constant.MenuType.BUTTON.getValue()){
			if(parentType != Constant.MenuType.MENU.getValue()){
				throw new ResultException("上级菜单只能为菜单类型");
			}
			return ;
		}
	}
}
