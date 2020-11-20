

package com.garm.modules.web.dto.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 前台登录表单
 *
 * @author
 */
@Data
@ApiModel(value = "前台登录表单")
public class LoginDTO {

    @NotBlank(message = "请填写用户账号")
    @Size(min = 3,max = 50,message = "用户账号不能为空")
    @ApiModelProperty("用户账号")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6,max = 32,message = "密码错误")
    @ApiModelProperty("密码")
    private String password;

}
