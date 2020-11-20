package com.garm.modules.exam.dto.ranke;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author liwt
 * @Date 2020/5/11 14:38
 * @Description
 * @Version 1.0.0
 */
@Data
public class RankingDTO implements Serializable {

    @ApiModelProperty("考试次数")
    private Integer testCount;

    @ApiModelProperty("总分")
    private Integer score;

    @ApiModelProperty("用户名")
    private String nickname;

    @ApiModelProperty("用户账号")
    private String userName;

}
