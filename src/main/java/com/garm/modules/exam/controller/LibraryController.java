package com.garm.modules.exam.controller;

import java.util.*;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.garm.common.exception.ResultException;
import com.garm.common.utils.FileUtil;
import com.garm.common.utils.JmBeanUtils;
import com.garm.common.validator.ValidatorUtils;
import com.garm.common.validator.group.AddGroup;
import com.garm.common.validator.group.UpdateGroup;
import com.garm.modules.exam.constants.DeleteConstant;
import com.garm.modules.exam.dto.library.FileUploadFailDTO;
import com.garm.modules.exam.dto.library.LibraryDTO;
import com.garm.modules.exam.dto.library.LibraryDetailDTO;
import com.garm.modules.exam.dto.library.LibraryListDTO;
import com.garm.modules.exam.enums.ModelFileType;
import com.garm.modules.sys.controller.AbstractController;
import com.garm.modules.sys.entity.DictItemEntity;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.ibatis.annotations.Update;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.garm.modules.exam.entity.LibraryEntity;
import com.garm.modules.exam.service.LibraryService;
import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Result;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 题库管理
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-08 10:42:37
 */
@RestController
@Api(tags = "后台-考试管理-题库管理")
@RequestMapping("exam/library")
public class LibraryController extends AbstractController {
    @Autowired
    private LibraryService libraryService;

    @Autowired
    private FileUtil fileUtil;



    @PostMapping("/save")
    @ApiOperation("单选/多选/判断/填空保存")
    @RequiresPermissions("exam:library:save")
    public Result save(@RequestBody LibraryDTO dto){
        ValidatorUtils.validateEntity(dto, AddGroup.class);
        libraryService.add(dto);
        return Result.ok();
    }

    @PostMapping("/read/save")
    @ApiOperation("阅读理解保存")
    @RequiresPermissions("exam:library:save")
    public Result readSave(@RequestBody LibraryDTO dto){
        ValidatorUtils.validateEntity(dto, UpdateGroup.class);
        libraryService.addRead(dto);
        return Result.ok();
    }

    /**
      * 修改
      */
    @PostMapping("/update")
    @ApiOperation("单选/多选/判断/填空修改")
    @RequiresPermissions("exam:library:update")
    public Result update(@RequestBody LibraryDTO dto){
        ValidatorUtils.validateEntity(dto, AddGroup.class);
		libraryService.edit(dto);
        return Result.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/read/update")
    @ApiOperation("阅读理解修改")
    @RequiresPermissions("exam:library:update")
    public Result updateRead(@RequestBody LibraryDTO dto){
        ValidatorUtils.validateEntity(dto,UpdateGroup.class);
        libraryService.editRead(dto);
        return Result.ok();
    }


    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value="页码",required=true),
            @ApiImplicitParam(name="limit",value="大小",required=true),
            @ApiImplicitParam(name="serviceType",value="服务类型id",required=true),
            @ApiImplicitParam(name="libraryName",value="试题题目"),
            @ApiImplicitParam(name="type",value="试题类型，1.单选题 2.多选题 3.判断题 4.填空题 5.阅读理解"),
            @ApiImplicitParam(name="eyeType",value="题眼类型id"),
            @ApiImplicitParam(name="difficultyType",value="难度类型"),
            @ApiImplicitParam(name="answerAnalysis",value="试题解析"),
            @ApiImplicitParam(name="rangeType",value="使用范围")
    })
    @RequiresPermissions("exam:library:list")
    public Result<PageUtils<LibraryListDTO>> list(@RequestParam @ApiIgnore Map<String, Object> params){
        PageUtils<LibraryEntity> page = libraryService.queryPage(params);
        List<LibraryListDTO> list = JmBeanUtils.entityToDtoList(page.getList(),LibraryListDTO.class);
        List<DictItemEntity> entitys = getItems();
        list.stream().forEach(s->{
            if(entitys.stream().filter(m->m.getDictItemId().equals(s.getEyeType())).findFirst().isPresent()){
                s.setEyeTypeName( entitys.stream().filter(m->m.getDictItemId().equals(s.getEyeType())).findFirst().get().getItemName());
            }
            if(entitys.stream().filter(m->m.getDictItemId().equals(s.getServiceType())).findFirst().isPresent()){
                s.setServiceTypeName( entitys.stream().filter(m->m.getDictItemId().equals(s.getServiceType())).findFirst().get().getItemName());
            }

        });
        //服务类型名称和题眼名称
        PageUtils<LibraryListDTO> result = new PageUtils<>();
        BeanUtils.copyProperties(page,result);
        result.setList(list);

        return Result.successPage(result);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{libraryId}")
    @ApiOperation("信息")
    @RequiresPermissions("exam:library:info")
    public Result<LibraryDetailDTO> info(@PathVariable("libraryId") Long libraryId){
        LibraryDetailDTO dto = libraryService.detail(libraryId);
        return Result.ok(dto);
    }

    /**
     * 逻辑删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除")
    @RequiresPermissions("exam:library:delete")
    public Result delete(@RequestBody Long[] libraryIds){
        LibraryEntity entity = new LibraryEntity();
        entity.setIsDel(DeleteConstant.DELETE);
        entity.setDelTime(new Date());
		libraryService.update(entity,Wrappers.<LibraryEntity>lambdaQuery().in(LibraryEntity::getLibraryId,Arrays.asList(libraryIds)));
        return Result.ok();
    }

    /**
     * 模板文件下载
     * @return
     */
    @ApiOperation("模板文件下载")
    @GetMapping("model/download")
    public void downloadModel( HttpServletRequest request, HttpServletResponse response){
        fileUtil.downloadModel(ModelFileType.LIBRARY_MODEL.getDesc(),request,response);
    }

   /**
     * 文件上传
     * @param file
     * @return
      */
    @ApiOperation("试题导入")
    @PostMapping("upload")
//    @RequiresPermissions("exam:library:upload")
    @ApiImplicitParams({
            @ApiImplicitParam(name="serviceType",value="服务分类"),
            @ApiImplicitParam(name="file",value="文件流")
    })
    public Result<FileUploadFailDTO> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("serviceType") String serviceType){
        if(null==serviceType){
            throw new ResultException("请选择服务分类");
        }
        return libraryService.uploadFile(file,Long.parseLong(serviceType));
    }

}
