package com.garm.modules.exam.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Result;
import com.garm.modules.exam.constants.DeleteConstant;
import com.garm.modules.exam.dto.PaperTowardsDTO;
import com.garm.modules.exam.dto.ServiceOrderDTO;
import com.garm.modules.exam.dto.analyze.TestAnalyzeDTO;
import com.garm.modules.exam.entity.*;
import com.garm.modules.exam.enums.ErrorEyeType;
import com.garm.modules.exam.service.*;
import com.garm.modules.sys.controller.AbstractController;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 财务管理
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-08 10:42:37
 */
@RestController
@Api(tags = "后台-财务管理")
@RequestMapping("exam/finance")
public class FinanceController extends AbstractController {
    @Autowired
    private ServiceOrderService serviceOrderService;

    @GetMapping("/list")
    @ApiOperation("列表")
    @RequiresPermissions("exam:finance:list")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value="页码",required=true),
            @ApiImplicitParam(name="limit",value="大小",required=true),
            @ApiImplicitParam(name="startTime",value="开始时间,yyyy-MM-dd"),
            @ApiImplicitParam(name="endTime",value="结束时间,yyyy-MM-dd")
    })
    public Result<ServiceOrderDTO> list(@RequestParam @ApiIgnore Map<String, Object> params){
        return Result.ok(serviceOrderService.queryPage(params));
    }
}
