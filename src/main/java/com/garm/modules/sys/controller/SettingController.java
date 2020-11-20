package com.garm.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.garm.common.exception.ResultException;
import com.garm.common.redis.redislock.annotation.RedisLockAable;
import com.garm.common.utils.FileUtil;
import com.garm.common.utils.JmBeanUtils;
import com.garm.common.utils.Result;
import com.garm.modules.exam.constants.DeleteConstant;
import com.garm.modules.exam.constants.DictConstant;
import com.garm.modules.exam.entity.LibraryEntity;
import com.garm.modules.exam.entity.OfficialTestEntity;
import com.garm.modules.exam.entity.PaperEntity;
import com.garm.modules.exam.service.LibraryService;
import com.garm.modules.exam.service.OfficialTestService;
import com.garm.modules.exam.service.PaperService;
import com.garm.modules.sys.dto.SettingDTO;
import com.garm.modules.sys.entity.DictItemEntity;
import com.garm.modules.sys.form.FreeSettingForm;
import com.garm.modules.sys.service.DictItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * 设置面板
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-07 13:41:50
 */
@RestController
@Api(tags = "后台-系统管理-设置面板")
@RequestMapping("/sys/setting")
public class SettingController {
    @Autowired
    private DictItemService dictItemService;

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private PaperService paperService;

    @Autowired
    private OfficialTestService officialTestService;

    @Autowired
    private FileUtil fileUtil;
    /**
     * 列表
     */
    @PostMapping("/service/list/{dictId}")
    @ApiOperation("服务设置/题眼设置-列表")
    @RequiresPermissions("sys:setting:service:list")
    public Result<List<SettingDTO>> list(@PathVariable Long dictId){

        List<DictItemEntity> entitys = dictItemService.list(Wrappers.<DictItemEntity>lambdaQuery().eq(DictItemEntity::getDictId,dictId).orderByDesc(DictItemEntity::getCreateTime));

        return Result.ok(JmBeanUtils.entityToDtoList(entitys,SettingDTO.class));
    }

    /**
      * 信息
      */
    @PostMapping("/service/info/{dictItemId}")
    @ApiOperation("服务设置/题眼设置-信息")
    @RequiresPermissions("sys:setting:service:info")
    public Result<SettingDTO> info(@PathVariable("dictItemId") Long dictItemId){
        DictItemEntity dict = dictItemService.getById(dictItemId);
        return Result.ok(JmBeanUtils.entityToDto(dict,SettingDTO.class));
    }

    /**
     * 新增
     */
    @PostMapping("/service/save")
    @ApiOperation("服务设置/题眼设置-新增")
    @RequiresPermissions("sys:setting:service:save")
    @Transactional(rollbackFor = Exception.class)
    @RedisLockAable(root = "sys:setting:service:save", key = "#{dto.dictId}")
    public Result<SettingDTO> save(@RequestBody SettingDTO dto){
        DictItemEntity dictItemEntity = dictItemService.getOne(Wrappers.<DictItemEntity>lambdaQuery().eq(DictItemEntity::getItemName,dto.getItemName()).eq(DictItemEntity::getDictId,dto.getDictId()));
        if(null!=dictItemEntity&&dto.getDictId()==DictConstant.SERVICE_TYPE){
            throw new ResultException("服务名称已存在!");
        }else if(null!=dictItemEntity&&dto.getDictId()==DictConstant.EYE_TYPE){
            throw new ResultException("题眼名称已存在!");
        }
        DictItemEntity entity = new DictItemEntity();
        BeanUtils.copyProperties(dto,entity);
        entity.setCreateTime(new Date());
        dictItemService.save(entity);
        return Result.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("服务设置/题眼设置-修改")
    @RequiresPermissions("sys:setting:update")
    @Transactional(rollbackFor = Exception.class)
    @RedisLockAable(root = "sys:setting:update", key = "#{dto.dictItemId}")
    public Result update(@RequestBody SettingDTO dto){
        DictItemEntity dictItemEntity = dictItemService.getOne(Wrappers.<DictItemEntity>lambdaQuery()
                .eq(DictItemEntity::getItemName,dto.getItemName())
                .eq(DictItemEntity::getDictId,dto.getDictId())
                .ne(DictItemEntity::getDictItemId,dto.getDictItemId())
        );
        if(null!=dictItemEntity&&dto.getDictId()==DictConstant.SERVICE_TYPE){
            throw new ResultException("服务名称已存在!");
        }else if(null!=dictItemEntity&&dto.getDictId()==DictConstant.EYE_TYPE){
            throw new ResultException("题眼名称已存在!");
        }
        DictItemEntity entity =  dictItemService.getOne(Wrappers.<DictItemEntity>lambdaQuery().eq(DictItemEntity::getDictItemId,dto.getDictItemId()));
        if(null!=entity.getValue()&&!entity.getValue().equals(dto.getValue())){
            fileUtil.remove(entity.getValue());
        }
        dictItemService.updateById(JmBeanUtils.entityToDto(dto,DictItemEntity.class));
        return Result.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除")
    @RequiresPermissions("sys:setting:delete")
    @Transactional(rollbackFor = Exception.class)
//    @RedisLockAable(root = "sys:setting:delete", key = "#{dictItemIds}")
    public Result delete(@RequestBody Long[] dictItemIds){
        int libraryCount = libraryService.count(Wrappers.<LibraryEntity>lambdaQuery().eq(LibraryEntity::getIsDel, DeleteConstant.NOT_DELETE)
                .in(LibraryEntity::getServiceType,dictItemIds)
        );
        int libraryCountTwo = libraryService.count(Wrappers.<LibraryEntity>lambdaQuery().eq(LibraryEntity::getIsDel, DeleteConstant.NOT_DELETE)
                .in(LibraryEntity::getEyeType,dictItemIds)
        );
        if(libraryCount!=0||libraryCountTwo!=0){
            throw new ResultException("当前设置已被题库使用，无法进行删除");
        }

        int paperCount = paperService.count(Wrappers.<PaperEntity>lambdaQuery().in(PaperEntity::getServiceType,dictItemIds).eq(PaperEntity::getIsDel, DeleteConstant.NOT_DELETE));
        if(paperCount!=0){
            throw new ResultException("当前设置已被试卷使用，无法进行删除");
        }

        int testCount = officialTestService.count(Wrappers.<OfficialTestEntity>lambdaQuery().in(OfficialTestEntity::getServiceType,dictItemIds).eq(OfficialTestEntity::getIsDel, DeleteConstant.NOT_DELETE));
        if(testCount!=0){
            throw new ResultException("当前设置已被考试使用，无法进行删除");
        }
        dictItemService.removeByIds(Arrays.asList(dictItemIds));
        return Result.ok();
    }


    @PostMapping("/free/list")
    @ApiOperation("费用设置-列表")
    @RequiresPermissions("sys:setting:free:list")
    public Result<List<SettingDTO>> list(){
        List<DictItemEntity> entitys = dictItemService.list(Wrappers.<DictItemEntity>lambdaQuery().eq(DictItemEntity::getDictId,DictConstant.FEE_TYPE));
        return Result.ok(JmBeanUtils.entityToDtoList(entitys,SettingDTO.class));
    }

    @PostMapping("/free/save")
    @ApiOperation("费用设置-保存")
    @Transactional(rollbackFor = Exception.class)
    @RequiresPermissions("sys:setting:free:save")
    public Result freeList(@RequestBody FreeSettingForm form){
        List<DictItemEntity> entitys = dictItemService.list(Wrappers.<DictItemEntity>lambdaQuery().eq(DictItemEntity::getDictId,DictConstant.FEE_TYPE));

        DictItemEntity monthItem = dictItemService.getOne(Wrappers.<DictItemEntity>lambdaQuery().eq(DictItemEntity::getRemarks,"M"));
        monthItem.setValue(form.getMonthPay().toString());

        DictItemEntity semesterItem = dictItemService.getOne(Wrappers.<DictItemEntity>lambdaQuery().eq(DictItemEntity::getRemarks,"S"));
        semesterItem.setValue(form.getSemesterPay().toString());
        if(!dictItemService.updateById(monthItem)){
            throw new ResultException("编辑月费用失败");
        }
        if(!dictItemService.updateById(semesterItem)){
            throw new ResultException("编辑学期费用失败");
        }
        return Result.ok();
    }

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result up(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String filePath = fileUtil.saveFile(file,request);
        if("FAIL".equals(filePath)){
            throw new ResultException("文件上传失败!");
        }
        return Result.ok(filePath);
    }
}
