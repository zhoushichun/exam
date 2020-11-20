package com.garm.modules.web.controller;

import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Result;
import com.garm.modules.exam.service.OwnTestService;
import com.garm.modules.web.annotation.Login;
import com.garm.modules.web.annotation.LoginUser;
import com.garm.modules.web.dto.error.ErrorTestDetailDTO;
import com.garm.modules.web.dto.error.ErrorTestRespDTO;
import com.garm.modules.web.dto.error.WErrorTestQueryRequestDTO;
import com.garm.modules.web.model.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
;

/**
 * @Author liwt
 * @Date 2020/5/6 15:02
 * @Description
 * @Version 1.0.0
 */
@Api(tags = "前台-错题集")
@RestController
@RequestMapping("api/web/error/test")
public class WErrorTestController {

    @Autowired
    private OwnTestService iOwnTestService;

    /**
     * 获取错题数据
     */
    @Login
    @PostMapping("v1/list")
    @ApiOperation("获取错题数据")
    public Result<PageUtils<ErrorTestRespDTO>> listOwn(@RequestBody WErrorTestQueryRequestDTO dto, @LoginUser @ApiIgnore UserDTO user) {
        return iOwnTestService.listErrorTest(dto,user.getUserId());
    }

    /**
     * 获取错题数据详情
     */
    @Login
    @ApiOperation("获取错题数据详情")
    @PostMapping("v1/detail/{type}/{testId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name="type",value="试卷类型 1-官方考试 2-自测考试",required=true),
            @ApiImplicitParam(name="testId",value="考试ID",required=true),
    })
    public Result<ErrorTestDetailDTO> detailOwn(@PathVariable Integer type, @PathVariable Long testId, @LoginUser @ApiIgnore UserDTO user)  {
        return iOwnTestService.getErrorTestDetail(type,testId,user.getUserId());
    }

}
