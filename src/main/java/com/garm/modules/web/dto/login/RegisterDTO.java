

package com.garm.modules.web.dto.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 注册表单
 *
 * @author
 */
@Data
@ApiModel(value = "注册表单")
public class RegisterDTO {
    @ApiModelProperty(value = "电话号码")
    @NotBlank(message = "请填写电话号码")
    @Pattern(regexp = "^1[3456789]\\d{9}$",message = "请输入正确的手机号码")
    private String userName;

    @ApiModelProperty(value = "密码")
    @NotBlank(message="密码不能为空")
    @Size(min = 6,max = 32,message = "密码长度为6-32个字符")
    private String password;

    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "请填写用户名")
    @Size(min = 3,max = 50,message = "用户名为3-50个字符")
    private String nickname;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "请填写验证码")
    @Size(min = 6,max = 6,message = "请输入6位验证码")
    private String code;

}
