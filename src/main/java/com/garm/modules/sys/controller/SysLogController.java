

package com.garm.modules.sys.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Result;
import com.garm.modules.sys.service.SysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Map;


/**
 * 系统日志
 *
 * @author
 */
@Controller
@RequestMapping("/sys/log")
@Api(tags = "后台-系统管理-系统日志")
public class SysLogController {
	@Autowired
	private SysLogService sysLogService;

	/**
	 * 列表
	 */
	@ResponseBody
	@GetMapping("/list")
	@ApiOperation("列表")
	@RequiresPermissions("sys:log:list")
	public Result<Map> list(@RequestParam @ApiIgnore Map<String, Object> params){
		PageUtils page = sysLogService.queryPage(params);
		Map<String,Object> map = new HashMap<>();
		map.put("page", page);
		return Result.ok(map);
	}

}
