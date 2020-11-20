package com.garm.modules.exam.controller;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.garm.common.exception.ResultException;
import com.garm.common.utils.JmBeanUtils;
import com.garm.common.validator.ValidatorUtils;
import com.garm.common.validator.group.AddGroup;
import com.garm.common.validator.group.UpdateGroup;
import com.garm.modules.exam.constants.DeleteConstant;
import com.garm.modules.exam.dto.library.LibraryListDTO;
import com.garm.modules.exam.dto.paper.PaperDTO;
import com.garm.modules.exam.dto.test.official.OfficialTestDTO;
import com.garm.modules.exam.dto.test.official.OfficialTestInfoDTO;
import com.garm.modules.exam.dto.test.official.OfficialTestUserInfoDTO;
import com.garm.modules.exam.entity.*;
import com.garm.modules.exam.service.*;
import com.garm.modules.sys.controller.AbstractController;
import com.garm.modules.sys.entity.DictItemEntity;
import com.garm.modules.sys.entity.SysUserEntity;
import com.garm.modules.sys.service.SysUserService;
import com.garm.modules.sys.service.UserService;
import com.garm.modules.web.entity.UserEntity;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.garm.common.utils.PageUtils;
import com.garm.common.utils.Result;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 官方考试
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-09 17:38:48
 */
@RestController
@Api(tags = "后台-考试管理-发布考试")
@RequestMapping("exam/officialtest")
public class OfficialTestController extends AbstractController {
    @Autowired
    private OfficialTestService officialTestService;

    @Autowired
    private OfficialTestUserService officialTestUserService;

    @Autowired
    private OfficialTestPaperService officialTestPaperService;

    @Autowired
    private PaperService paperService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private UserService userService;

    /**
     * 发布考试
     */
    @PostMapping("/publish")
    @ApiOperation("发布考试")
    @RequiresPermissions("exam:officialtest:publish")
    public Result save(@RequestBody OfficialTestDTO dto){
        ValidatorUtils.validateEntity(dto, AddGroup.class);
        return officialTestService.publish(dto);
    }

    /**
      * 修改
      */
    @PostMapping("/update")
    @ApiOperation("修改")
    @RequiresPermissions("exam:officialtest:update")
    public Result update(@RequestBody OfficialTestDTO form){
        ValidatorUtils.validateEntity(form, UpdateGroup.class);
		officialTestService.edit(form);

        return Result.ok();
    }
    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("列表")
    @RequiresPermissions("exam:officialtest:list")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value="页码",required=true),
            @ApiImplicitParam(name="limit",value="大小",required=true),
            @ApiImplicitParam(name="serviceType",value="服务类型id",required=true),
            @ApiImplicitParam(name="officialTestName",value="考试名称"),
    })
    public Result<PageUtils<OfficialTestInfoDTO>> list(@RequestParam @ApiIgnore Map<String, Object> params){
        PageUtils page = officialTestService.queryPage(params);
        List<OfficialTestInfoDTO> list = JmBeanUtils.entityToDtoList(page.getList(),OfficialTestInfoDTO.class);

        List<DictItemEntity> entitys = getItems();
        if (null!=list&&list.size()==0)
        list.stream().forEach(s->{
            s.setServiceTypeName( entitys.stream().filter(m->m.getDictItemId().equals(s.getServiceType())).findFirst().get().getItemName());
        });
        //服务类型名称和题眼名称
        PageUtils<OfficialTestInfoDTO> result = new PageUtils<>();
        BeanUtils.copyProperties(page,result);
        result.setList(list);

        return Result.ok(result);
    }
    /**
     * 信息
     */
    @PostMapping("/info/{officialTestId}")
    @ApiOperation("信息")
    @RequiresPermissions("exam:officialtest:info")
    @ApiImplicitParam(name="officialTestId",value="考试ID",required=true)
    public Result<OfficialTestInfoDTO> info(@PathVariable("officialTestId") Long officialTestId){
		OfficialTestEntity officialTest = officialTestService.getById(officialTestId);
        OfficialTestInfoDTO result = JmBeanUtils.entityToDto(officialTest,OfficialTestInfoDTO.class);

        if(getItems().stream().filter(m->m.getDictItemId().equals(result.getServiceType())).findFirst().isPresent()){
            result.setServiceTypeName( getItems().stream().filter(m->m.getDictItemId().equals(result.getServiceType())).findFirst().get().getItemName());
        }

        List<OfficialTestUserEntity> officialTestUserEntities = officialTestUserService.list(Wrappers.<OfficialTestUserEntity>lambdaQuery().eq(OfficialTestUserEntity::getOfficialTestId,officialTestId));
        List<Long> userIds =  officialTestUserEntities.stream().map(OfficialTestUserEntity::getUserId).collect(Collectors.toList());
        List<OfficialTestUserInfoDTO> datas = userService.getTestUserInfo(userIds);
        result.setUserDatas(datas);

        List<OfficialTestPaperEntity> paperEntities = officialTestPaperService.list(Wrappers.<OfficialTestPaperEntity>lambdaQuery().eq(OfficialTestPaperEntity::getOfficialTestId,officialTestId));
        List<Long> paperIds = paperEntities.stream().map(OfficialTestPaperEntity::getPaperId).collect(Collectors.toList());

        List<PaperEntity> page = paperService.listByIds(paperIds);
        List<PaperDTO> list = JmBeanUtils.entityToDtoList(page,PaperDTO.class);

        //服务类型名称和用户名称
        List<SysUserEntity> userEntities = sysUserService.list();
        list.stream().forEach(s->{
            if( getItems().stream().filter(m->m.getDictItemId().equals(s.getServiceType())).findFirst().isPresent()){
                s.setServiceTypeName( getItems().stream().filter(m->m.getDictItemId().equals(s.getServiceType())).findFirst().get().getItemName());
            }
            if(userEntities.stream().filter(m->m.getUserId().equals(s.getCreateUserId())).findFirst().isPresent()){
                s.setCreateUser( userEntities.stream().filter(m->m.getUserId().equals(s.getCreateUserId())).findFirst().get().getUsername());
            }
        });

        result.setPaperDatas(list);
        return Result.ok(result);
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除")
    @RequiresPermissions("exam:officialtest:delete")
    public Result delete(@RequestBody Long[] officialTestIds){
        List<OfficialTestEntity> datas = officialTestService.listByIds(Arrays.asList(officialTestIds));
        datas.stream().forEach(s->{
            if(s.getTestStartTime().getTime()<new Date().getTime()){
                throw new ResultException("考试已开始,无法进行删除");
            }
        });
        OfficialTestEntity officialTest = new OfficialTestEntity();
        officialTest.setIsDel(DeleteConstant.DELETE);
        officialTestService.update(officialTest, Wrappers.<OfficialTestEntity>lambdaQuery().in(OfficialTestEntity::getOfficialTestId,Arrays.asList(officialTestIds)));
        return Result.ok();
    }


    @GetMapping("/list/paper")
    @ApiOperation("试卷列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value="页码",required=true),
            @ApiImplicitParam(name="limit",value="大小",required=true),
            @ApiImplicitParam(name="paperName",value="试卷名称"),
            @ApiImplicitParam(name="serviceType",value="服务类型id"),
            @ApiImplicitParam(name="paperIds",value="已添加的试卷ID数组",dataType = "Long")
    })
    public Result<PageUtils<PaperDTO>> paperList(@RequestParam @ApiIgnore Map<String, Object> params){
        PageUtils<PaperEntity> page = paperService.queryPage(params);
        List<PaperDTO> list = JmBeanUtils.entityToDtoList(page.getList(),PaperDTO.class);
        //服务类型名称和用户名称
        List<SysUserEntity> userEntities = sysUserService.list();
        list.stream().forEach(s->{
            if( getItems().stream().filter(m->m.getDictItemId().equals(s.getServiceType())).findFirst().isPresent()){
                s.setServiceTypeName( getItems().stream().filter(m->m.getDictItemId().equals(s.getServiceType())).findFirst().get().getItemName());
            }
            if(userEntities.stream().filter(m->m.getUserId().equals(s.getCreateUserId())).findFirst().isPresent()){
                s.setCreateUser( userEntities.stream().filter(m->m.getUserId().equals(s.getCreateUserId())).findFirst().get().getUsername());
            }
        });

        PageUtils<PaperDTO> result = new PageUtils();
        result.setList(list);
        result.setCurrPage(page.getCurrPage());
        result.setPageSize(page.getPageSize());
        result.setTotalCount(page.getTotalCount());
        result.setTotalPage(page.getTotalPage());
        System.out.println(result);


        return Result.ok(result);
    }

    @GetMapping("/list/user")
    @ApiOperation("用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value="页码"),
            @ApiImplicitParam(name="limit",value="大小"),
            @ApiImplicitParam(name="serviceType",value="服务类型id"),
            @ApiImplicitParam(name="userName",value="用户账户"),
            @ApiImplicitParam(name="userIds",value="已添加的用户ID数组")
    })
    public Result<PageUtils<OfficialTestUserInfoDTO>> userInfo(@RequestParam @ApiIgnore Map<String, Object> params){

        Result datas = userService.getTestUserInfo(params);
        return datas;
    }

}
