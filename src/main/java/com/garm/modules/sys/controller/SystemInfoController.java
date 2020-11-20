package com.garm.modules.sys.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.garm.common.exception.ResultException;
import com.garm.common.redis.redislock.annotation.RedisLockAable;
import com.garm.common.utils.Constant;
import com.garm.common.utils.FileUtil;
import com.garm.common.utils.JmBeanUtils;
import com.garm.common.utils.Result;
import com.garm.modules.exam.constants.DictConstant;
import com.garm.modules.exam.constants.SystemEnableType;
import com.garm.modules.sys.dto.EyeDTO;
import com.garm.modules.sys.dto.RoleDTO;
import com.garm.modules.sys.dto.ServiceDTO;
import com.garm.modules.sys.dto.SettingDTO;
import com.garm.modules.sys.entity.DictItemEntity;
import com.garm.modules.sys.entity.SysRoleEntity;
import com.garm.modules.sys.entity.SysUserRoleEntity;
import com.garm.modules.sys.form.FreeSettingForm;
import com.garm.modules.sys.service.DictItemService;
import com.garm.modules.sys.service.SysRoleService;
import com.garm.modules.sys.service.SysUserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
@Api(tags = "后台-公共接口-获取所有下拉选数据")
@RequestMapping("/sys/info")
public class SystemInfoController {
    @Autowired
    private DictItemService dictItemService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 服务列表
     */
    @PostMapping("/service")
    @ApiOperation("服务列表")
    public List<ServiceDTO> service(){

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

    /**
     * 角色
     */
    @PostMapping("/role")
    @ApiOperation("角色列表")
    public List<RoleDTO> role(){
        List<SysRoleEntity> roleEntities = sysRoleService.list();

        return JmBeanUtils.entityToDtoList(roleEntities, RoleDTO.class);
    }

}
