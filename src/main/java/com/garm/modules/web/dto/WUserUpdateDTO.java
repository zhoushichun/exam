package com.garm.modules.web.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@ApiModel("修改用户名")
public class WUserUpdateDTO implements Serializable {
    /**
     * 电话号码，登陆账号
     */
    @ApiModelProperty("电话号码、登录账号")
    @NotBlank(message = "请填写电话号码")
    @Size(min = 11,max = 11,message = "电话号码为11个字符")
    @Pattern(regexp = "^1[3456789]\\d{9}$",message = "电话号码格式不正确")
    private String userName;
    /**
     * 验证码
     */
    @ApiModelProperty("验证码")
    @NotBlank(message = "请填写验证码")
    @Size(min = 6,max = 6,message = "请输入6位验证码")
    private String code;
    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String nickname;
}
