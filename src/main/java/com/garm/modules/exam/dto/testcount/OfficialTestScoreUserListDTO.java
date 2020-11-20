package com.garm.modules.exam.dto.testcount;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xq
 * @title OfficialTestScoreUserListRespDTO
 * @description
 * @date 2020/5/9 14:08
 */
@Data
@ApiModel("考试称及统计-参考人员信息")
public class OfficialTestScoreUserListDTO implements Serializable {

    /**
     * id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("记录ID")
    private Long id;
    /**
     * 用户id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 用户昵称
     */
    @ApiModelProperty("用户名")
    private String nickname;

    /**
     * 用户登录账号
     */
    @ApiModelProperty("用户账号")
    private String userName;


    /**
     * 试卷名称
     */
    @ApiModelProperty("试卷名称")
    private String paperName;

    /**
     * 分数
     */
    @ApiModelProperty("分数")
    private Integer scores;

    /**
     * 是否及格 1-未及格 2-及格
     */
    @ApiModelProperty("是否及格 1-未及格 2-及格")
    private Integer isPass;

    /**
     * 考试所用时间，单位：分钟
     */
    @ApiModelProperty("考试所用时间，单位：分钟")
    private Integer testMin;
}
