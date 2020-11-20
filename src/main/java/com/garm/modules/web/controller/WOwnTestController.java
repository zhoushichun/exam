package com.garm.modules.web.controller;

import java.util.List;

import com.garm.common.validator.ValidatorUtils;
import com.garm.modules.exam.dto.library.LibraryDetailDTO;
import com.garm.modules.web.annotation.Login;
import com.garm.modules.web.annotation.LoginUser;
import com.garm.modules.web.dto.owntest.request.WOwnTestQueryDTO;
import com.garm.modules.web.dto.owntest.request.WOwnTestRequestDTO;
import com.garm.modules.web.dto.owntest.request.OwnTestDetailReqDTO;
import com.garm.modules.web.dto.owntest.response.OwnTestDetailRespDTO;
import com.garm.modules.web.dto.owntest.response.OwnTestRespDTO;
import com.garm.modules.web.dto.owntest.response.OwnTestStatusCount;
import com.garm.modules.web.model.UserDTO;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.garm.modules.exam.service.OwnTestService;
import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Result;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 自测考试
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-11 16:40:56
 */
@RestController
@Api(tags = "前台-自测考试")
@RequestMapping("api/web/own/test")
public class WOwnTestController {
    @Autowired
    private OwnTestService ownTestService;


    /**
     * 生成试卷
     */
    @Login
    @PostMapping("v1/generate")
    @ApiOperation("生成试卷")
    public Result<OwnTestDetailRespDTO> generate(@LoginUser @ApiIgnore UserDTO user,  @RequestBody WOwnTestRequestDTO dto) {
        ValidatorUtils.validateEntity(dto);
        return ownTestService.generatePaper(dto,user);
    }

    /**
     * 预览
     *
     * @return
     */
    @Login
    @PostMapping("v1/preview/{ownTestId}")
    @ApiOperation("预览")
    @ApiImplicitParam(name="ownTestId",value="试卷ID",required=true)
    public Result<List<LibraryDetailDTO>> preview(@PathVariable Long ownTestId) {
        return ownTestService.preview(ownTestId);
    }


    /**
     * 开始考试
     *
     * @return
     */
    @Login
    @PostMapping("v1/start/{ownTestId}")
    @ApiOperation("开始考试")
    @ApiImplicitParam(name="ownTestId",value="试卷ID",required=true)
    public Result<OwnTestDetailRespDTO> start(@PathVariable Long ownTestId) {
        return ownTestService.start(ownTestId);
    }


    /**
     * 保存自测考试数据
     *
     * @return
     */
    @Login
    @ApiOperation("保存自测考试数据")
    @PostMapping("v1/save")
    public Result save(@RequestBody OwnTestDetailReqDTO dto) {
        ValidatorUtils.validateEntity(dto);
        return ownTestService.saveData(dto);
    }

    /**
     * 统计状态数量
     *
     * @return
     */
    @Login
    @PostMapping("v1/count")
    @ApiOperation("统计状态数量")
    public Result<OwnTestStatusCount> count(@LoginUser @ApiIgnore UserDTO user) {

        return ownTestService.count(user.getUserId());
    }

    /**
     * 清除
     *
     * @return
     */
    @Login
    @PostMapping("v1/clear/{ownTestId}")
    @ApiOperation("清除")
    @ApiImplicitParam(name="ownTestId",value="试卷ID",required=true)
    public Result clear(@PathVariable Long ownTestId) {
        return ownTestService.clear(ownTestId);
    }

    /**
     * 获取自测数据详情
     *
     * @return
     */
    @Login
    @PostMapping("v1/detail/{ownTestId}")
    @ApiOperation("获取自测数据详情")
    @ApiImplicitParam(name="ownTestId",value="试卷ID",required=true)
    public Result<OwnTestRespDTO> detail(@PathVariable Long ownTestId) {
        return ownTestService.detail(ownTestId);
    }

    /**
     * 获取自测数据列表
     *
     * @return
     */
    @Login
    @PostMapping("v1/list")
    @ApiOperation("获取自测数据列表")
    public Result<PageUtils<OwnTestRespDTO>> list(@LoginUser @ApiIgnore UserDTO user,@Validated @RequestBody WOwnTestQueryDTO dto) {
        ValidatorUtils.validateEntity(dto);
        return ownTestService.list(dto,user.getUserId());
    }
}
