package com.garm.modules.exam.dto.errorowntest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author xq
 * @title SelfTestUserListRespDTO
 * @description
 * @date 2020/5/7 17:35
 */
@Data
@ApiModel("自测错题列表")
public class SelfTestUserListDTO implements Serializable {

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String nickname;
    /**
     * 用户账号
     */
    @ApiModelProperty("用户账号")
    private String username;
    /**
     * 自测次数
     */
    @ApiModelProperty("自测次数")
    private Integer testNum;

    /**
     * 考试总时长/分钟
     */
    @ApiModelProperty("考试总时长/分钟")
    private BigDecimal testMin;

}
