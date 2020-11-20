package com.garm.modules.web.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author ldx
 * @Date 2020/4/8 11:46
 * @Description
 * @Version 1.0.0
 */
@Data
@ApiModel("登录成功返回模型")
public class UserLoginResponseDTO implements Serializable {

    @ApiModelProperty("token")
    private String token;

}
