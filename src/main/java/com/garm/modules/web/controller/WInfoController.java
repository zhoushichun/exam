package com.garm.modules.web.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.garm.modules.exam.constants.DictConstant;
import com.garm.modules.exam.constants.SystemEnableType;
import com.garm.modules.exam.entity.SysUserServiceEntity;
import com.garm.modules.exam.service.SysUserServiceService;
import com.garm.modules.sys.dto.EyeDTO;
import com.garm.modules.sys.dto.ServiceDTO;
import com.garm.modules.sys.entity.DictItemEntity;
import com.garm.modules.sys.service.DictItemService;
import com.garm.modules.sys.service.SysUserService;
import com.garm.modules.web.annotation.Login;
import com.garm.modules.web.annotation.LoginUser;
import com.garm.modules.web.model.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 所有下拉选数据
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-07 13:41:50
 */
@RestController
@Api(tags = "前台-公共接口-获取所有下拉选数据")
@RequestMapping("api/info")
public class WInfoController {
    @Autowired
    private DictItemService dictItemService;

    @Autowired
    private SysUserServiceService sysUserServiceService;

    /**
     * 获取当前用户的服务列表
     */
    @Login
    @PostMapping("/user/service")
    @ApiOperation("当前用户的服务列表")
    public List<ServiceDTO> userService(@LoginUser @ApiIgnore UserDTO user){
        List<Long> serviceTypes = sysUserServiceService.list(Wrappers.<SysUserServiceEntity>lambdaQuery().eq(SysUserServiceEntity::getUserId,user.getUserId())).stream().map(s->s.getServiceType()).collect(Collectors.toList());
        List<DictItemEntity> entitys = dictItemService.list(Wrappers.<DictItemEntity>lambdaQuery()
                .eq(DictItemEntity::getDictId,DictConstant.SERVICE_TYPE)
                .eq(DictItemEntity::getStatus, SystemEnableType.ENABLE)
                .in(DictItemEntity::getDictItemId,serviceTypes)
                .orderByDesc(DictItemEntity::getCreateTime)
        );
        List<ServiceDTO> result =  entitys.stream().map(s->{
            ServiceDTO dto = new ServiceDTO();
            dto.setServiceName(s.getItemName());
            dto.setServiceType(s.getDictItemId());
            return dto;
        }).collect(Collectors.toList());

        return result;
    }

    /**
     * 服务列表
     */
    @Login
    @PostMapping("/service")
    @ApiOperation("服务列表")
    public List<ServiceDTO> service(@LoginUser @ApiIgnore UserDTO user){

        List<DictItemEntity> entitys = dictItemService.list(Wrappers.<DictItemEntity>lambdaQuery()
                .eq(DictItemEntity::getDictId,DictConstant.SERVICE_TYPE)
                .eq(DictItemEntity::getStatus, SystemEnableType.ENABLE)
                .orderByDesc(DictItemEntity::getCreateTime)
        );
        List<ServiceDTO> result =  entitys.stream().map(s->{
            ServiceDTO dto = new ServiceDTO();
            dto.setServiceName(s.getItemName());
            dto.setServiceType(s.getDictItemId());
            return dto;
        }).collect(Collectors.toList());

        return result;
    }

    /**
      * 题眼列表
      */
    @PostMapping("/eye")
    @ApiOperation("题眼列表")
    public List<EyeDTO> eye(){
        List<DictItemEntity> entitys = dictItemService.list(Wrappers.<DictItemEntity>lambdaQuery()
                .eq(DictItemEntity::getDictId,DictConstant.EYE_TYPE)
                .eq(DictItemEntity::getStatus, SystemEnableType.ENABLE)
                .orderByDesc(DictItemEntity::getCreateTime)
        );
        List<EyeDTO> result =  entitys.stream().map(s->{
            EyeDTO dto = new EyeDTO();
            dto.setEyeName(s.getItemName());
            dto.setEyeType(s.getDictItemId());
            return dto;
        }).collect(Collectors.toList());

        return result;
    }

    /**
     * 答题时长列表
     */
    @PostMapping("/time")
    @ApiOperation("答题时长列表")
    public List<String> time(){
        List<DictItemEntity> entitys = dictItemService.list(Wrappers.<DictItemEntity>lambdaQuery()
                .eq(DictItemEntity::getDictId,DictConstant.TIME_TYPE)
                .eq(DictItemEntity::getStatus, SystemEnableType.ENABLE)
                .orderByDesc(DictItemEntity::getCreateTime)
        );
        List<String> result =  entitys.stream().map(s->s.getItemName()).collect(Collectors.toList());

        return result;
    }

}
