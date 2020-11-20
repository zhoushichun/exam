package com.garm.modules.web.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 创建用户的实体类
 * @Author liwt
 * @Date 2020/4/8 11:29
 * @Description
 * @Version 1.0.0
 */
@Data
@ApiModel("注册")
public class UserRegistDTO implements Serializable {


    /**
     * 电话号码，登陆账号
     */
    @NotBlank(message = "请填写电话号码")
    @ApiModelProperty("电话号码")
    @Pattern(regexp = "^1[3456789]\\d{9}$",message = "手机号码不正确")
    private String userName;

    /**
     * 密码
     */
    @NotBlank(message = "请填写密码")
    @ApiModelProperty("密码")
    @Size(min = 6,max = 32,message = "密码长度为6-32个字符")
    private String password;

    /**
     * 昵称
     */
    @NotBlank(message = "请填写用户名")
    @ApiModelProperty("昵称")
    @Size(min = 3,max = 50,message = "用户名为3-50个字符")
    private String nickname;

    /**
     * 验证码
     */
    @NotBlank(message = "请填写验证码")
    @ApiModelProperty("验证码")
    @Size(min = 6,max = 6,message = "请输入6位验证码")
    private String code;

}
