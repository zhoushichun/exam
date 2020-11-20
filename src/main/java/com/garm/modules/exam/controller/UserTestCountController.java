package com.garm.modules.exam.controller;

import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Result;
import com.garm.modules.exam.dto.ServiceOrderDTO;
import com.garm.modules.exam.dto.usertestcount.*;
import com.garm.modules.exam.service.OfficialTestUserService;
import com.garm.modules.exam.service.ServiceOrderService;
import com.garm.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;


/**
 * 考生成绩统计
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-08 10:42:37
 */
@RestController
@Api(tags = "后台-数据分析-考生成绩统计")
@RequestMapping("exam/user/score")
public class UserTestCountController extends AbstractController {
    @Autowired
    private OfficialTestUserService officialTestUserService;

    @GetMapping("/list")
    @ApiOperation("列表")
    @RequiresPermissions("exam:score:user:list")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value="页码",required=true),
            @ApiImplicitParam(name="limit",value="大小",required=true),
            @ApiImplicitParam(name="username",value="用户账号"),
            @ApiImplicitParam(name="nickname",value="用户名")
    })
    public Result<PageUtils<OfficialTestStudentListDTO>> list(@RequestParam @ApiIgnore Map<String, Object> params){
        return officialTestUserService.userScoreAnalyze(params);
    }

    @GetMapping("/info/{userId}")
    @ApiOperation("基本信息")
    @RequiresPermissions("exam:score:user:info")
    @ApiImplicitParam(name="userId",value="用户ID")
    public Result<OfficialTestUserInfoDTO> info(@PathVariable Long userId){
        return Result.ok(officialTestUserService.queryOfficialTestStudentInfo(userId));
    }

    @GetMapping("/info/test")
    @ApiOperation("考试记录")
    @RequiresPermissions("exam:score:user:info")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value="页码",required=true),
            @ApiImplicitParam(name="limit",value="大小",required=true),
            @ApiImplicitParam(name="testName",value="考试名称"),
            @ApiImplicitParam(name="serviceType",value="服务分类ID"),
            @ApiImplicitParam(name="startTime",value="开始时间,yyyy-MM-dd"),
            @ApiImplicitParam(name="endTime",value="结束时间,yyyy-MM-dd"),
            @ApiImplicitParam(name="userId",value="用户ID",required = true)
    })
    public Result<PageUtils<OfficialTestUserHisInfoDTO>> test(@RequestParam @ApiIgnore Map<String, Object> params){
        return Result.ok(officialTestUserService.queryOfficialTestStudentHisList(params));
    }

    @GetMapping("/analyze/{id}")
    @ApiOperation("成绩分析")
    @ApiImplicitParam(name="id",value="记录ID")
    public Result<List<OfficialTestAnalysisInfoDTO>> analyze(@PathVariable Long id){
        return officialTestUserService.queryOfficialTestAnalysis(id);
    }

    @GetMapping("/paper/{id}")
    @ApiOperation("查看问卷")
    @ApiImplicitParam(name="id",value="记录ID")
    public Result<PaperAnswerInfoDTO> paper(@PathVariable Long id){
        return officialTestUserService.queryPaper(id);
    }

}
