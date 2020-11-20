

package com.garm.modules.sys.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 密码表单
 *
 * @author
 */
@Data
@ApiModel(value = "密码表单")
public class PasswordForm {
    /**
     * 原密码
     */
    private String password;
    /**
     * 新密码
     */
    private String newPassword;

}
