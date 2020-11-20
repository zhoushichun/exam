package com.garm.modules.exam.dto.test.official;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author liwt
 * @Date 2020/4/28 13:46
 * @Description
 * @Version 1.0.0
 */
@Data
@ApiModel("考试用户信息")
public class OfficialTestUserInfoDTO implements Serializable {
    /**
     * 用户id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户名")
    private String nickname;
    /**
     * 用户账号
     */
    @ApiModelProperty("用户账号")
    private String userName;

    /**
     * 用户服务类型
     */
    @ApiModelProperty("用户服务类型：1.普通用户 2.付费用户 3.流失用户")
    private Integer userType;
    /**
     * 用户服务类型：1.普通用户 2.付费用户 3.流失用户
     */
    @ApiModelProperty("购买服务")
    private String services;

    /**
     * 用户服务对应类型id字符串，批量调整时会判断这个值，1.不存在多个服务，2.多个被选着用户相同
     */
    @ApiModelProperty("用户服务对应类型id字符串，批量调整时会判断这个值，1.不存在多个服务，2.多个被选着用户相同")
    private String serviceTypes;

    /**
     * 用户服务对应id字符串
     */
    @ApiModelProperty("用户服务对应id字符串")
    private String serviceIds;

}
