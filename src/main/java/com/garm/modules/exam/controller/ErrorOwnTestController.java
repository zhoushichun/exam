package com.garm.modules.exam.controller;

import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Result;
import com.garm.modules.exam.dto.errorowntest.SelfTestUserListDTO;
import com.garm.modules.exam.dto.errortest.OfficialTestErrorListDTO;
import com.garm.modules.exam.dto.errortest.OfficialTestErrorStatisticsDTO;
import com.garm.modules.exam.service.SysUserErrorEyeTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;


/**
 * 自测错题统计
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-11 16:39:23
 */
@RestController
@Api(tags = "后台-数据分析-自测错题统计")
@RequestMapping("exam/error/test/own")
public class ErrorOwnTestController {
    @Autowired
    private SysUserErrorEyeTypeService sysUserErrorEyeTypeService;

    /**
     * 列表
     *
     */
    @GetMapping("/list")
    @ApiOperation("列表")
    @RequiresPermissions("exam:error:test:own:list")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value="页码",required=true),
            @ApiImplicitParam(name="limit",value="大小",required=true),
            @ApiImplicitParam(name="username",value="用户账户"),
            @ApiImplicitParam(name="nickname",value="用户名")
    })
    public Result<PageUtils<SelfTestUserListDTO>> list(@RequestParam @ApiIgnore Map<String, Object> params){
        return sysUserErrorEyeTypeService.querySelfTestUserList(params);
    }


    /**
     * 详情
     *
     */
    @GetMapping("/detail")
    @ApiOperation("详情")
    @RequiresPermissions("exam:error:test:own:info")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value="页码",required=true),
            @ApiImplicitParam(name="limit",value="大小",required=true),
            @ApiImplicitParam(name="userId",value="用户ID",required = true),
            @ApiImplicitParam(name="testName",value="自测名称"),
            @ApiImplicitParam(name="serviceType",value="服务类型")
    })
    public Result<PageUtils<SelfTestUserListDTO>> detail(@RequestParam @ApiIgnore Map<String, Object> params){
        return sysUserErrorEyeTypeService.detail(params);
    }


    /**
     * 自测错题集统计-错题集统计列表
     *
     * @return
     */
    @ApiOperation("错题集统计")
    @GetMapping("/info")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value="页码",required=true),
            @ApiImplicitParam(name="limit",value="大小",required=true),
            @ApiImplicitParam(name="eyeType",value="题眼类型"),
            @ApiImplicitParam(name="ownTestId",value="考试ID",required = true)
    })
    @RequiresPermissions("exam:error:test:own:info")
    public Result<PageUtils<OfficialTestErrorStatisticsDTO>> statisticsList(@RequestParam @ApiIgnore Map<String, Object> params) {
        return sysUserErrorEyeTypeService.queryOwnTestErrorStatistics(params);
    }

}
