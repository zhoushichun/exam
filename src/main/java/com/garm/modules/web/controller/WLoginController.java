

package com.garm.modules.web.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.garm.common.exception.ResultException;
import com.garm.common.utils.CipherUtil;
import com.garm.common.utils.IdUtil;
import com.garm.common.utils.Result;
import com.garm.common.validator.ValidatorUtils;
import com.garm.modules.exam.constants.DeleteConstant;
import com.garm.modules.web.constants.MesasgeConstant;
import com.garm.modules.sys.service.UserService;
import com.garm.modules.web.dto.*;
import com.garm.modules.web.dto.login.LoginDTO;
import com.garm.modules.web.dto.login.MessageDTO;
import com.garm.modules.web.dto.login.RegisterDTO;
import com.garm.modules.web.entity.UserEntity;
import com.garm.modules.web.response.UserLoginResponseDTO;
import com.garm.modules.web.service.IMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * 前台登录授权
 *
 * @author
 */
@RestController
@RequestMapping("/api")
@Api(tags = "前台-登录相关")
public class WLoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private IMessageService iMessageService;


    @PostMapping("v1/user/login")
    @ApiOperation("登录")
    public Result<UserLoginResponseDTO> login(@RequestBody LoginDTO dto){
        //表单校验
        ValidatorUtils.validateEntity(dto);

        //用户登录
        Result result = userService.login(dto);

        return result;
    }


    @ApiOperation("发送短信验证码")
    @PostMapping("v1/message/send")
    public Result sendMessage(@RequestBody MessageDTO dto) {
        ValidatorUtils.validateEntity(dto);
        return iMessageService.sendMessage(dto);
    }

    @PostMapping("v1/user/regist")
    @ApiOperation("注册")
    public Result<Map> register(@RequestBody RegisterDTO dto){
        //表单校验
        ValidatorUtils.validateEntity(dto);

        if(userService.checkCode(dto.getUserName(),dto.getCode(), MesasgeConstant.REGIST)){
            final UserEntity user = userService
                    .getOne(Wrappers.<UserEntity>lambdaQuery()
                            .eq(UserEntity::getUsername, dto.getUserName()).eq(UserEntity::getIsDel, DeleteConstant.NOT_DELETE));
            if(user != null){
                return Result.error("账号已存在");
            }
            UserEntity sysuser = new UserEntity();
            BeanUtils.copyProperties(dto, sysuser);
            sysuser.setCreateTime(new Date());
            sysuser.setUsername(dto.getUserName());
            sysuser.setMobile(dto.getUserName());
            sysuser.setUserId(IdUtil.genId());
            sysuser.setSalt(CipherUtil.getSalt());
            sysuser.setPassword(CipherUtil.encryptPassword(sysuser.getPassword(), sysuser.getSalt()));
            if (! userService.save(sysuser)) {
                throw new ResultException("注册失败");
            }
            return Result.ok();
        }
        return Result.error("验证码无效");
    }

    /**
     * 忘记密码
     * @param dto
     * @return
     */
    @PostMapping("v1/user/forget")
    @ApiOperation("忘记密码")
    public Result forget(@RequestBody WUserForgetDTO dto) {
        //表单校验
        ValidatorUtils.validateEntity(dto);

        if(userService.checkCode(dto.getUserName(),dto.getCode(), MesasgeConstant.FORGET)){
            final UserEntity user = userService
                    .getOne(Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getUsername, dto.getUserName()).eq(UserEntity::getIsDel,DeleteConstant.NOT_DELETE));
            if(user == null){
                return Result.error("账号不存在");
            }
            UserEntity sysuser = new UserEntity();
            sysuser.setPassword(dto.getPassword());
            sysuser.setUserId(user.getUserId());
            sysuser.setSalt(CipherUtil.getSalt());
            sysuser.setPassword(CipherUtil.encryptPassword(sysuser.getPassword(), sysuser.getSalt()));
            if (!userService.updateById(sysuser)){
                throw new ResultException("修改密码失败");
            }
            return Result.ok();
        }
        return Result.error("验证码无效");
    }


}
