package com.garm.modules.web.dto.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 修改密码接收类
 */
@Data
@ApiModel("获取短信验证码")
public class MessageDTO implements Serializable {

    /**
     * 电话号码，登陆账号
     */
    @NotBlank(message = "请填写电话号码")
    @Size(min = 11,max = 11,message = "电话号码为11个字符")
    @Pattern(regexp = "^1[3456789]\\d{9}$",message = "请输入正确的手机号码")
    @ApiModelProperty("电话号码，登陆账号")
    private String phone;


    @NotBlank(message = "请填写短信发送类型")
    @ApiModelProperty("发送类型  forget-忘记密码，regist-注册,update-修改账号,update_pwd-修改密码")
    private String type;

}
