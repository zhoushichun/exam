package com.garm.modules.web.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("验证码消息")
public class MessageModel implements Serializable {

    /**
     * 电话号码
     */
    @ApiModelProperty("电话号码")
    private String phone;

    /**
     * 验证码
     */
    @ApiModelProperty("验证码")
    private String code;

    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String type;

}
