package com.garm.modules.exam.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Result;
import com.garm.modules.exam.constants.DeleteConstant;
import com.garm.modules.exam.dto.PaperTowardsDTO;
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

import java.util.List;
import java.util.Map;


/**
 * 考试分析
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-08 10:42:37
 */
@RestController
@Api(tags = "后台-考试管理-考试分析")
@RequestMapping("exam/test/analyze")
public class TestAnalyzeController extends AbstractController {
    @Autowired
    private LibraryService libraryService;
    @Autowired
    private PaperService paperService;
    @Autowired
    private OfficialTestService officialTestService;
    @Autowired
    private OfficialTestUserService officialTestUserService;
    @Autowired
    private OwnTestService ownTestService;
    @Autowired
    private SysUserErrorEyeTypeService sysUserErrorEyeTypeService;
    @Autowired
    private PaperTowardsService paperTowardsService;


    @PostMapping("/all")
    @ApiOperation("考试数据、题眼分析")
    @RequiresPermissions("exam:test:analyze:all")
    public Result<TestAnalyzeDTO> all(){
        TestAnalyzeDTO result = new TestAnalyzeDTO();
        int libraryCount = libraryService.count(Wrappers.<LibraryEntity>lambdaQuery().eq(LibraryEntity::getIsDel, DeleteConstant.NOT_DELETE));
        int paperCount = paperService.count(Wrappers.<PaperEntity>lambdaQuery().eq(PaperEntity::getIsDel, DeleteConstant.NOT_DELETE));
        int testCount = officialTestService.count(Wrappers.<OfficialTestEntity>lambdaQuery().eq(OfficialTestEntity::getIsDel, DeleteConstant.NOT_DELETE));
        int testUserCount = officialTestUserService.count();
        int ownTestCount = ownTestService.count();

        List<TestAnalyzeDTO.EyeAnalyze> testUserEyeAnalyze =  sysUserErrorEyeTypeService.queryAnalyzeInfo(ErrorEyeType.OFFICIAL_TEST.getCode());
        List<TestAnalyzeDTO.EyeAnalyze> ownTestEyeAnalyze =  sysUserErrorEyeTypeService.queryAnalyzeInfo(ErrorEyeType.USER_ERROR_LIB.getCode());

        result.setLibraryCount(libraryCount);
        result.setPaperCount(paperCount);
        result.setTestCount(testCount);
        result.setTestUserCount(testUserCount);
        result.setOwnTestCount(ownTestCount);
        result.setTestUserEyeAnalyze(testUserEyeAnalyze);
        result.setOwnTestEyeAnalyze(ownTestEyeAnalyze);
        return Result.ok(result);
    }


    @GetMapping("/toward")
    @ApiOperation("试卷问题走向")
    @RequiresPermissions("exam:test:analyze:all")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value="页码",required=true),
            @ApiImplicitParam(name="limit",value="大小",required=true),
            @ApiImplicitParam(name="startTime",value="开始时间,yyyy-MM-dd"),
            @ApiImplicitParam(name="endTime",value="结束时间,yyyy-MM-dd")
    })
    public Result<PaperTowardsDTO> toward(@RequestParam @ApiIgnore Map<String, Object> params){
        return Result.ok(paperTowardsService.queryList(params));
    }
}
