package com.garm.modules.web.controller;


import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Result;
import com.garm.modules.exam.dto.usertestcount.OfficialTestAnalysisInfoDTO;
import com.garm.modules.exam.service.OfficialTestUserService;
import com.garm.modules.sys.service.DictItemService;
import com.garm.modules.web.annotation.Login;
import com.garm.modules.web.annotation.LoginUser;
import com.garm.modules.web.dto.test.OfficialTestAnalysisRespDTO;
import com.garm.modules.web.dto.test.ScoreDiagramListRespDTO;
import com.garm.modules.web.dto.test.WOfficialTestQueryRequestDTO;
import com.garm.modules.web.model.UserDTO;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@Api(tags = "前台-成绩中心")
@RequestMapping("api/web/official/test")
public class WTestController {

    @Autowired
    private OfficialTestUserService officialTestUserService;

    /**
     * 成绩曲线
     *
     * @param dto
     * @return
     */
    @Login
    @PostMapping("v1/report")
    @ApiOperation("成绩曲线")
    public Result<List<ScoreDiagramListRespDTO>> webApiReport(@LoginUser @ApiIgnore UserDTO user, @RequestBody WOfficialTestQueryRequestDTO dto) {
        dto.setUserId(user.getUserId());
        return officialTestUserService.webApiReport(dto);
    }



    @Login
    @PostMapping("v1/report/page")
    @ApiOperation("成绩列表-分页记录")
    public Result<PageUtils<ScoreDiagramListRespDTO>> webApiReportPage(@LoginUser @ApiIgnore UserDTO user,@RequestBody WOfficialTestQueryRequestDTO dto) {
        dto.setUserId(user.getUserId());
        return officialTestUserService.webApiReportPage(dto);
    }

    /**
     * 成绩分析
     *
     * @return
     */
    @Login
    @ApiImplicitParam(name="officialTestId",value="考试ID",required = true)
    @PostMapping("/v1/query/scoreAnalysis/{officialTestId}")
    @ApiOperation("成绩分析")
    public Result<OfficialTestAnalysisRespDTO> scoreAnalysis(@PathVariable("officialTestId") Long officialTestId, @LoginUser @ApiIgnore UserDTO user) {
        return officialTestUserService.queryWebOfficialTestAnalysis(officialTestId,user.getUserId() );
    }

}
