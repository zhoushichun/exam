

package com.garm.modules.web.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.garm.common.exception.ResultException;
import com.garm.common.utils.CipherUtil;
import com.garm.common.utils.Result;
import com.garm.modules.web.constants.MesasgeConstant;
import com.garm.modules.sys.service.UserService;
import com.garm.modules.web.annotation.Login;
import com.garm.modules.web.annotation.LoginUser;
import com.garm.modules.web.dto.UserCountInfoDTO;
import com.garm.modules.web.dto.WUserForgetDTO;
import com.garm.modules.web.dto.WUserNicknameDTO;
import com.garm.modules.web.dto.WUserUpdateDTO;
import com.garm.modules.web.entity.UserEntity;
import com.garm.modules.web.model.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 前台登录授权
 *
 * @author
 */
@RestController
@RequestMapping
@Api(tags = "前台-用户相关")
public class WUserController {
    @Autowired
    private UserService userService;

    /**
     * 获取用户考试统计信息
     * @return
     */
    @Login
    @PostMapping("/api/web/user/service/dict/total")
    @ApiOperation("获取用户考试统计信息")
    public Result<UserCountInfoDTO> total(@LoginUser @ApiIgnore UserDTO user){
        return userService.getUserTestCount( user);
    }

    @Login
    @PostMapping("/api/v1/user/loginDetail")
    @ApiOperation("获取用户信息")
    public Result<UserDTO> userInfo(@LoginUser @ApiIgnore UserDTO user){
        return Result.ok(user);
    }

    /**
     * 修改密码
     * @param dto
     * @return
     */
    @Login
    @PostMapping("api/web/user/updatePwd")
    @ApiOperation("修改密码")
    public Result updatePwd(@LoginUser @ApiIgnore UserDTO user,@RequestBody WUserForgetDTO dto) {
        if(StringUtils.isBlank(dto.getPassword())){
            return Result.error("请填写密码");
        }
        if(StringUtils.isBlank(dto.getCode())){
            return Result.error("请填写验证码");
        }
        if(userService.checkCode(user.getUsername(),dto.getCode(), MesasgeConstant.UPDATE_PWD)){

            UserEntity sysuser = new UserEntity();
            sysuser.setPassword(dto.getPassword());
            sysuser.setUserId(user.getUserId());
            sysuser.setSalt(CipherUtil.getSalt());
            sysuser.setPassword(CipherUtil.encryptPassword(sysuser.getPassword(), sysuser.getSalt()));
            if (!userService.updateById(sysuser)) {
                throw new ResultException("修改密码失败");
            }
            return Result.ok();
        }
        return Result.error("验证码无效");
    }

    /**
     * 修改账号
     * @param dto
     * @return
     */
    @Login
    @PostMapping("api/web/user/updateUserName")
    @ApiOperation("修改账号")
    public Result updateUserName(@LoginUser @ApiIgnore UserDTO user,@RequestBody WUserUpdateDTO dto) {
        if(userService.checkCode(dto.getUserName(),dto.getCode(), MesasgeConstant.UPDATE)){
            final UserEntity check = userService
                    .getOne(Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getUsername, dto.getUserName()));
            if(check != null){
                return Result.error("账号已存在");
            }
            UserEntity sysuser = new UserEntity();
            sysuser.setUserId(user.getUserId());
            sysuser.setUsername(dto.getUserName());
            sysuser.setMobile(dto.getUserName());
            if (!userService.updateById(sysuser)) {
                throw new ResultException("修改帐号失败");
            }
            return Result.ok();
        }
        return Result.error("验证码无效");
    }

    /**
     * 修改用户名
     * @param dto
     * @return
     */
    @Login
    @PostMapping("api/web/user/updateName")
    @ApiOperation("修改用户名")
    public Result updateName(@LoginUser @ApiIgnore UserDTO user,@RequestBody WUserNicknameDTO dto) {
        UserEntity sysuser = new UserEntity();
        sysuser.setUserId(user.getUserId());
        sysuser.setNickname(dto.getNickname());
        if (!userService.updateById(sysuser)) {
            throw new ResultException("修改用户名失败");
        }
        return Result.ok();
    }

}
