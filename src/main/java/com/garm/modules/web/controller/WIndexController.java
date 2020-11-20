

package com.garm.modules.web.controller;


import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Result;
import com.garm.modules.exam.constants.DeleteConstant;
import com.garm.modules.exam.constants.DictConstant;
import com.garm.modules.exam.service.OfficialTestService;
import com.garm.modules.exam.service.OfficialTestUserService;
import com.garm.modules.exam.service.SysUserServiceService;
import com.garm.modules.sys.controller.AbstractController;
import com.garm.modules.sys.entity.DictItemEntity;
import com.garm.modules.web.annotation.Login;
import com.garm.modules.web.annotation.LoginUser;
import com.garm.modules.web.dto.index.OfficialTestPageListReqDTO;
import com.garm.modules.web.dto.shop.WUserServiceDTO;
import com.garm.modules.web.dto.shop.WUserServiceFeeDTO;
import com.garm.modules.web.dto.shop.WUserServiceQueryDTO;
import com.garm.modules.web.dto.test.OfficialTestPageListRespDTO;
import com.garm.modules.web.dto.test.ScoreDiagramListReqDTO;
import com.garm.modules.web.dto.test.ScoreDiagramListRespDTO;
import com.garm.modules.web.model.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping
@Api(tags = "前台-首页")
public class WIndexController extends AbstractController {
    @Autowired
    private OfficialTestService officialTestService;

    @Autowired
    private OfficialTestUserService officialTestUserService;

    @Login
    @PostMapping("api/web/official/test/v1/userTestList")
    @ApiOperation("最新考试")
    public Result<PageUtils<OfficialTestPageListRespDTO>> queryOfficialTestList(@LoginUser @ApiIgnore UserDTO user, @RequestBody OfficialTestPageListReqDTO dto) {
        dto.setUserId(user.getUserId());
        dto.setIsDel(DeleteConstant.NOT_DELETE);
        return officialTestService.queryOfficialTestList(dto);
    }

    /**
     * 前端-首页成绩曲线图
     *
     * @param dto
     * @return
     */
    @Login
    @PostMapping("api/web/statistics/list/scoreDiagram")
    @ApiOperation("成绩曲线图统计")
    public Result<PageUtils<ScoreDiagramListRespDTO>> queryScoreDiagramList(@LoginUser @ApiIgnore UserDTO user, @RequestBody ScoreDiagramListReqDTO dto) {
        dto.setUserId(user.getUserId());
        return officialTestUserService.queryScoreDiagramList(dto);
    }


}
