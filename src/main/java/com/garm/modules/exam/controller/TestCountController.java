package com.garm.modules.exam.controller;

import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Result;
import com.garm.modules.exam.dto.testcount.OfficialTestInfoDTO;
import com.garm.modules.exam.dto.testcount.OfficialTestScoreListDTO;
import com.garm.modules.exam.dto.testcount.OfficialTestScoreUserListDTO;
import com.garm.modules.exam.dto.usertestcount.*;
import com.garm.modules.exam.service.OfficialTestUserService;
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
 * 考试成绩统计
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-08 10:42:37
 */
@RestController
@Api(tags = "后台-数据分析-考试成绩统计")
@RequestMapping("exam/test/score")
public class TestCountController extends AbstractController {
    @Autowired
    private OfficialTestUserService officialTestUserService;

    @GetMapping("/list")
    @ApiOperation("列表")
    @RequiresPermissions("exam:test:score:list")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value="页码",required=true),
            @ApiImplicitParam(name="limit",value="大小",required=true),
            @ApiImplicitParam(name="serviceType",value="服务类型"),
            @ApiImplicitParam(name="testName",value="考试名称")
    })
    public Result<PageUtils<OfficialTestScoreListDTO>> list(@RequestParam @ApiIgnore Map<String, Object> params){
        return Result.ok(officialTestUserService.queryOfficialTestScoreList(params));
    }

    @GetMapping("/info/{officialTestId}")
    @ApiOperation("基本信息")
    @RequiresPermissions("exam:test:score:info")
    @ApiImplicitParam(name="officialTestId",value="考试ID")
    public Result<OfficialTestInfoDTO> info(@PathVariable Long officialTestId){
        return officialTestUserService.testInfo(officialTestId);
    }

    @GetMapping("/info/test")
    @ApiOperation("参考人员信息")
    @RequiresPermissions("exam:score:user:info")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value="页码",required=true),
            @ApiImplicitParam(name="limit",value="大小",required=true),
            @ApiImplicitParam(name="nickname",value="用户名称"),
            @ApiImplicitParam(name="isPass",value="是否及格 1-未及格 2-及格"),
            @ApiImplicitParam(name="username",value="用户账号"),
            @ApiImplicitParam(name="officialTestId",value="考试ID",required = true)
    })
    public Result<PageUtils<OfficialTestScoreUserListDTO>> test(@RequestParam @ApiIgnore Map<String, Object> params){
        return Result.ok(officialTestUserService.queryOfficialTestScoreUserList(params));
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
