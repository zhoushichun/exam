package com.garm.modules.exam.controller;

import java.util.*;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.garm.common.utils.JmBeanUtils;
import com.garm.common.validator.ValidatorUtils;
import com.garm.modules.exam.constants.DeleteConstant;
import com.garm.modules.exam.dto.library.LibraryPaperListDTO;
import com.garm.modules.exam.dto.paper.PaperDTO;
import com.garm.modules.exam.dto.paper.PaperDetailDTO;
import com.garm.modules.exam.service.LibraryService;
import com.garm.modules.sys.controller.AbstractController;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.garm.modules.exam.entity.PaperEntity;
import com.garm.modules.exam.service.PaperService;
import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Result;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 试卷管理
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-09 09:14:42
 */
@RestController
@Api(tags = "后台-考试管理-试卷管理")
@RequestMapping("exam/paper")
public class PaperController extends AbstractController {
    @Autowired
    private PaperService paperService;

    @Autowired
    private LibraryService libraryService;
    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation("保存")
    @RequiresPermissions("exam:paper:save")
    public Result add(@RequestBody PaperDTO dto) {
        ValidatorUtils.validateEntity(dto);
        dto.setCreateUserId(getUserId());
        return paperService.add(dto);
    }


    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("修改")
    @RequiresPermissions("exam:paper:update")
    public Result update(@RequestBody PaperDTO dto){
        ValidatorUtils.validateEntity(dto);

        return paperService.edit(dto);
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("列表")
    @RequiresPermissions("exam:paper:list")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value="页码",required=true),
            @ApiImplicitParam(name="limit",value="大小",required=true),
            @ApiImplicitParam(name="paperName",value="试卷名称"),
            @ApiImplicitParam(name="serviceType",value="服务类型id")
    })
    public Result<PageUtils<PaperDTO>> list(@RequestParam @ApiIgnore Map<String, Object> params){
        PageUtils<PaperEntity> page = paperService.queryPage(params);
        List<PaperDTO> list = JmBeanUtils.entityToDtoList(page.getList(),PaperDTO.class);

        list.stream().forEach(s->{
            if(getItems().stream().filter(m->m.getDictItemId().equals(s.getServiceType())).findFirst().isPresent()){
                s.setServiceTypeName( getItems().stream().filter(m->m.getDictItemId().equals(s.getServiceType())).findFirst().get().getItemName());
            }
        });
        //服务类型名称和题眼名称
        PageUtils<PaperDTO> result = new PageUtils<>();
        BeanUtils.copyProperties(page,result);
        result.setList(list);
        return Result.successPage(page);
    }


    /**
     * 信息
     */
    @PostMapping("/info/{paperId}")
    @ApiOperation("信息")
    @RequiresPermissions("exam:paper:info")
    public Result<PaperDetailDTO> info(@PathVariable("paperId") Long paperId){
        return Result.ok(paperService.detail(paperId));
    }

    /**
     * 复制
     */
    @GetMapping("/copy/{paperId}")
    @ApiOperation("复制")
    @RequiresPermissions("exam:paper:copy")
    public Result copy(@PathVariable("paperId") Long paperId){
        paperService.copy(paperId);
        return Result.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除")
    @RequiresPermissions("exam:paper:delete")
    public Result delete(@RequestBody Long[] paperIds){
        PaperEntity entity = new PaperEntity();
        entity.setIsDel(DeleteConstant.DELETE);
        paperService.update(entity, Wrappers.<PaperEntity>lambdaQuery().in(PaperEntity::getPaperId,Arrays.asList(paperIds)));
        return Result.ok();
    }

    /**
     * 获取固定试题
     */
    @GetMapping("/fixture")
    @ApiOperation("获取固定试题")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value="页码",required=true),
            @ApiImplicitParam(name="limit",value="大小",required=true),
            @ApiImplicitParam(name="libraryName",value="试题题目"),
            @ApiImplicitParam(name="serviceType",value="服务类型id"),
            @ApiImplicitParam(name="rangeType",value="使用范围,1-考试,2-自测,3，都有"),
            @ApiImplicitParam(name="eyeType",value="题眼类型id"),
            @ApiImplicitParam(name="type",value="试题类型，1.单选题 2.多选题 3.判断题 4.填空题 5.阅读理解"),
            @ApiImplicitParam(name="difficultyType",value="难度类型,1-简单,2-中等,3-困难"),
            @ApiImplicitParam(name="libraryIds",value="已添加的试题ID字符串，多个ID用逗号,连接")
    })
    public Result<PageUtils<LibraryPaperListDTO>> fixture(@RequestParam @ApiIgnore Map<String,Object> params){
        return libraryService.getfixtureLibrarys(params);
    }

    /**
     * 获取随机试题
     */
    @GetMapping("/rand")
    @ApiOperation("获取随机试题")
    @ApiImplicitParams({
            @ApiImplicitParam(name="serviceType",value="服务类型id",required=true),
            @ApiImplicitParam(name="type",value="试题类型，1.单选题 2.多选题 3.判断题 4.填空题 5.阅读理解"),
            @ApiImplicitParam(name="difficultyType",value="难度类型,1-简单,2-中等,3-困难"),
            @ApiImplicitParam(name="rangeType",value="使用范围,1-考试,2-自测,3，都有"),
            @ApiImplicitParam(name="eyeType",value="题眼类型id"),
            @ApiImplicitParam(name="num",value="试题数量",required=true),
            @ApiImplicitParam(name="libraryIds",value="已添加的试题ID数组，多个ID用逗号,连接")
    })
    public Result<List<LibraryPaperListDTO>> rand(@RequestParam @ApiIgnore Map<String,Object> params){
        return libraryService.getRandLibrarys(params);
    }

    /**
     * 导出试卷
     *
     * @return
     */
    @GetMapping("/export/{type}/{paperId}")
    @ApiOperation("导出试卷")
    @ApiImplicitParams({
            @ApiImplicitParam(name="type",value="试卷类型，1.无答案版 2.有答案版",required = true),
            @ApiImplicitParam(name="paperId",value="试卷ID",required = true)
    })
    public void export(@PathVariable("type") String type,@PathVariable("paperId") String paperId,@ApiIgnore HttpServletResponse response) {
        paperService.export(Integer.valueOf(type),Long.parseLong(paperId),response);
    }

}
