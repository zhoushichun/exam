

package com.garm.modules.sys.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录表单
 *
 * @author
 */
@Data
@ApiModel(value = "后台登录表单")
public class SysLoginForm {
    @ApiModelProperty("账号")
    private String username;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("验证码")
    private String captcha;
    @ApiModelProperty("获取验证码时前端传的UUID")
    private String uuid;


}
