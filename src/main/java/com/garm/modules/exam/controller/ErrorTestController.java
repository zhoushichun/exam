package com.garm.modules.exam.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

import com.garm.modules.exam.dto.errortest.OfficialTestErrorListDTO;
import com.garm.modules.exam.dto.errortest.OfficialTestErrorStatisticsDTO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.garm.modules.exam.entity.SysUserErrorEyeTypeEntity;
import com.garm.modules.exam.service.SysUserErrorEyeTypeService;
import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Result;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;


/**
 * 考试错题统计
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-11 16:39:23
 */
@RestController
@Api(tags = "后台-数据分析-考试错题统计")
@RequestMapping("exam/error/test")
public class ErrorTestController {
    @Autowired
    private SysUserErrorEyeTypeService sysUserErrorEyeTypeService;

    /**
     * 列表
     *
     */
    @GetMapping("/list")
    @ApiOperation("列表")
    @RequiresPermissions("exam:error:test:list")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value="页码",required=true),
            @ApiImplicitParam(name="limit",value="大小",required=true),
            @ApiImplicitParam(name="serviceType",value="服务类型"),
            @ApiImplicitParam(name="officialTestName",value="考试名称")
    })
    public Result<OfficialTestErrorListDTO> list(@RequestParam @ApiIgnore Map<String, Object> params){
        PageUtils<OfficialTestErrorListDTO> page = sysUserErrorEyeTypeService.queryPage(params);
        return Result.ok(page);
    }


    /**
     * 考试错题集统计-错题集统计列表
     *
     * @return
     */
    @GetMapping("/info")
    @ApiOperation("考试错题集")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value="页码",required=true),
            @ApiImplicitParam(name="limit",value="大小",required=true),
            @ApiImplicitParam(name="eyeType",value="题眼类型"),
            @ApiImplicitParam(name="officialTestId",value="考试ID",required = true)
    })
    @RequiresPermissions("exam:error:test:info")
    public Result<PageUtils<OfficialTestErrorStatisticsDTO>> statisticsList(@RequestParam @ApiIgnore Map<String, Object> params) {
        return sysUserErrorEyeTypeService.queryOfficialTestErrorStatistics(params);
    }

}
