

package com.garm.modules.sys.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.R;
import com.garm.common.annotation.SysLog;
import com.garm.common.utils.Constant;
import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Result;
import com.garm.common.validator.Assert;
import com.garm.common.validator.ValidatorUtils;
import com.garm.common.validator.group.AddGroup;
import com.garm.common.validator.group.UpdateGroup;
import com.garm.config.PwdConfig;
import com.garm.modules.sys.entity.SysUserEntity;
import com.garm.modules.sys.entity.SysUserRoleEntity;
import com.garm.modules.sys.form.PasswordForm;
import com.garm.modules.sys.service.SysUserRoleService;
import com.garm.modules.sys.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 * @author
 */
@RestController
@RequestMapping("/sys/user")
@Api(tags = "后台-系统管理-系统用户")
public class SysUserController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;

	@Autowired
	private PwdConfig pwdConfig;
	/**
	 * 所有用户列表
	 */
	@GetMapping("/list")
	@ApiOperation("所有用户列表")
	@RequiresPermissions("sys:user:list")
	@ApiImplicitParams({
			@ApiImplicitParam(name="page",value="页码",required=true),
			@ApiImplicitParam(name="limit",value="大小",required=true),
			@ApiImplicitParam(name="mobile",value="用户电话"),
			@ApiImplicitParam(name="roleId",value="角色ID"),
			@ApiImplicitParam(name="nickname",value="用户名")
	})
	public Result<Map> list(@RequestParam Map<String, Object> params){
		List<SysUserRoleEntity> userRoleEntities = sysUserRoleService.list(Wrappers.<SysUserRoleEntity>lambdaQuery().eq(SysUserRoleEntity::getUserId,getUserId()));
		//系统管理员，拥有最高权限
		if( !userRoleEntities.stream().filter(s->s.getRoleId()==Constant.SUPER_ADMIN).findFirst().isPresent()){
			params.put("createUserId", getUserId());
		}
		PageUtils page = sysUserService.queryPage(params);
		Map<String,Object> map = new HashMap<>();
		map.put("page", page);
		return Result.ok(map);
	}

	/**
	 * 获取登录的用户信息
	 */
	@GetMapping("/info")
	@ApiOperation("获取登录的用户信息")
	public Result<Map> info(){
		Map<String,Object> map = new HashMap<>();
		map.put("user", getUser());
		return Result.ok(map);
	}

	/**
	 * 修改登录用户密码
	 */
	@SysLog("修改密码")
	@PostMapping("/password")
	@ApiOperation("修改登录用户密码")
	public Result password(@RequestBody PasswordForm form){
		Assert.isBlank(form.getNewPassword(), "新密码不为能空");

		//sha256加密
		String password = new Sha256Hash(form.getPassword(), getUser().getSalt()).toHex();
		//sha256加密
		String newPassword = new Sha256Hash(form.getNewPassword(), getUser().getSalt()).toHex();

		//更新密码
		boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
		if(!flag){
			return Result.error("原密码不正确");
		}

		return Result.ok();
	}

	/**
	 * 用户信息
	 */
	@GetMapping("/info/{userId}")
	@ApiOperation("用户信息")
	@RequiresPermissions("sys:user:info")
	public Result<Map> info(@PathVariable("userId") Long userId){
		SysUserEntity user = sysUserService.getById(userId);

		//获取用户所属的角色列表
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
		user.setRoleIdList(roleIdList);
		Map<String,Object> map = new HashMap<>();
		map.put("user", user);
		return Result.ok(map);
	}

	/**
	 * 保存用户
	 */
	@SysLog("保存用户")
	@PostMapping("/save")
	@ApiOperation("保存用户")
	@RequiresPermissions("sys:user:save")
	public Result save(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, AddGroup.class);

		user.setCreateUserId(getUserId());
		sysUserService.saveUser(user,getUserId());

		return Result.ok();
	}

	/**
	 * 重置密码
	 */
	@SysLog("重置密码")
	@GetMapping("/reset/{userId}")
	@ApiOperation("重置密码")
	@RequiresPermissions("sys:user:reset")
	@ApiImplicitParam(name="userId",value="用户ID",required=true)
	public Result update(@PathVariable("userId") Long userId){
		SysUserEntity user = sysUserService.getById(userId);

		//sha256加密
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setPassword(new Sha256Hash(pwdConfig.getDefaultPassword(), salt).toHex());
		user.setSalt(salt);
		sysUserService.updateById(user);

		return Result.ok();
	}

	/**
	 * 修改用户
	 */
	@SysLog("修改用户")
	@PostMapping("/update")
	@ApiOperation("修改用户")
	@RequiresPermissions("sys:user:update")
	public Result update(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, UpdateGroup.class);

		user.setCreateUserId(getUserId());
		sysUserService.update(user,getUserId());

		return Result.ok();
	}

	/**
	 * 删除用户
	 */
	@SysLog("删除用户")
	@PostMapping("/delete")
	@ApiOperation("删除用户")
	@RequiresPermissions("sys:user:delete")
	public Result delete(@RequestBody Long[] userIds){
		if(ArrayUtils.contains(userIds, 1L)){
			return Result.error("系统管理员不能删除");
		}

		if(ArrayUtils.contains(userIds, getUserId())){
			return Result.error("当前用户不能删除");
		}

		sysUserService.deleteBatch(userIds);

		return Result.ok();
	}
}
