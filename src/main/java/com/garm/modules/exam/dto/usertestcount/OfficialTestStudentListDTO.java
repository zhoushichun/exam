package com.garm.modules.exam.dto.usertestcount;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liwt
 * @title OfficialTestStudentListRespDTO
 * @description
 * @date 2020/5/11 10:39
 */
@Data
@ApiModel("考生成绩分析数据")
public class OfficialTestStudentListDTO implements Serializable {

    /**
     * 考生ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("考生ID")
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
     * 考试次数
     */
    @ApiModelProperty("考试次数")
    private Integer totalNum;

    /**
     * 考试总时长/分钟
     */
    @ApiModelProperty("考试总时长/分钟")
    private Long avgTime;



}
