package com.garm.modules.exam.controller;

import com.garm.common.utils.Result;
import com.garm.modules.exam.dto.PaperTowardsDTO;
import com.garm.modules.exam.dto.ranke.RankInfoDTO;
import com.garm.modules.exam.service.OfficialTestUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@Api(tags = "后台-数据分析-活跃排行榜")
@RequestMapping("exam/ranke")
public class RankeController {
    @Autowired
    private OfficialTestUserService officialTestUserService;

    @GetMapping("/list/{date}")
    @ApiOperation("活跃排行榜")
    @RequiresPermissions("exam:ranke:list")
    @ApiImplicitParam(name="date",value="时间,yyyy-MM",required=true)
    public Result<RankInfoDTO> toward(@PathVariable String date){
        return officialTestUserService.ranke(date);
    }
}
