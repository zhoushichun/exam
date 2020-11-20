package com.garm.modules.web.controller;

import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Result;
import com.garm.modules.exam.dto.paper.PaperDetailDTO;
import com.garm.modules.exam.service.OfficialTestService;
import com.garm.modules.exam.service.OfficialTestUserService;
import com.garm.modules.web.annotation.Login;
import com.garm.modules.web.annotation.LoginUser;
import com.garm.modules.web.dto.test.*;
import com.garm.modules.web.model.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author ldx
 * @Date 2020/4/28 11:40
 * @Description
 * @Version 1.0.0
 */
@Api(tags = "前台-考试中心")
@RestController
@RequestMapping("api/web/official/test")
public class WOfficialTestController {

    @Autowired
    private OfficialTestService iExamOfficialTestService;

    /**
     * 考试列表
     *
     * @param dto
     * @return
     */
    @Login
    @ApiOperation("考试列表")
    @PostMapping("v1/list")
    public Result<PageUtils<WOfficialTestResponseDTO>> webApiList(@LoginUser @ApiIgnore UserDTO user, @RequestBody WOfficialTestQueryRequestDTO dto) {
        dto.setUserId(user.getUserId());
        return iExamOfficialTestService.webApiList(dto);
    }

    /**
     * 考试须知
     *
     * @return
     */
    @Login
    @ApiOperation("考试须知")
    @PostMapping("v1/need/{officialTestId}")
    @ApiImplicitParam(name="officialTestId",value="考试ID",required=true)
    public Result<WOfficialTestResponseDTO> webApineed(@LoginUser @ApiIgnore UserDTO user,@PathVariable Long officialTestId) {
        return Result.ok(iExamOfficialTestService.need(officialTestId,user.getUserId()));
    }

    /**
     * 考试状态统计
     *
     * @return
     */
    @Login
    @ApiOperation("考试状态统计")
    @PostMapping("v1/countStatus")
    public Result<OfficialTestStatusCount> webApiCountStatus(@LoginUser @ApiIgnore UserDTO user) {
        return iExamOfficialTestService.countStatus(user.getUserId());
    }

    /**
     * 开始/继续 考试
     *
     * @param officialTestId
     * @return
     */
    @Login
    @ApiOperation("开始/继续 考试")
    @PostMapping("v1/exam/start/{officialTestId}")
    @ApiImplicitParam(name="officialTestId",value="考试ID",required=true)
    public Result<OfficialTestRespDTO> webApiExamStart(@PathVariable Long officialTestId, @LoginUser @ApiIgnore UserDTO user) {
        return iExamOfficialTestService.webApiExamStart(officialTestId, user.getUserId());
    }
    /**
     * N 秒自动保存 答案
     *
     * @param officialTestId
     * @param dto
     * @return
     */
    @Login
    @ApiOperation("N 秒自动保存 答案")
    @PostMapping("v1/exam/timing/save/{officialTestId}/{stateTime}")
    @ApiImplicitParams({
            @ApiImplicitParam(name="officialTestId",value="考试ID",required=true),
            @ApiImplicitParam(name="stateTime",value="已用的时间，单位：秒",required=true)
    })
    public Result webApiTimingSave(@PathVariable Long officialTestId, @PathVariable Long stateTime, @RequestBody OfficiatesTestDTO dto, @LoginUser @ApiIgnore UserDTO user) {
        return iExamOfficialTestService.webApiTimingSave(officialTestId,stateTime,user.getUserId(), dto);
    }

    /**
     * 提交试卷
     *
     * @param officialTestId
     * @param dto
     * @return
     */
    @Login
    @ApiOperation("提交试卷")
    @PostMapping("v1/exam/their/papers/{officialTestId}/{stateTime}")
    @ApiImplicitParams({
            @ApiImplicitParam(name="officialTestId",value="考试ID",required=true),
            @ApiImplicitParam(name="stateTime",value="已用的时间，单位：秒",required=true)
    })
    public Result webApiTheirPapers(@PathVariable Long officialTestId,@PathVariable Long stateTime, @RequestBody OfficiatesTestDTO dto, @LoginUser @ApiIgnore UserDTO user) {
        return iExamOfficialTestService.webApiTheirPapers(officialTestId, dto,user.getUserId(),stateTime);
    }

    /**
     * 查询考试详情信息
     *
     * @param officialTestId
     * @return
     */
    @Login
    @PostMapping("v1/exam/info/{officialTestId}")
    @ApiOperation("试卷详情")
    @ApiImplicitParam(name="officialTestId",value="考试ID",required=true)
    public Result<OfficialTestRespDTO> webApiofficialInfo(@PathVariable Long officialTestId, @LoginUser @ApiIgnore UserDTO user) {
        return iExamOfficialTestService.webApiofficialInfo(officialTestId,user.getUserId());
    }


}
