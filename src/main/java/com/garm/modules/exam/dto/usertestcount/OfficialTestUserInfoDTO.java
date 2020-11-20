package com.garm.modules.exam.dto.usertestcount;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author xq
 * @title OfficialTestInfoRespDTO
 * @description
 * @date 2020/5/9 13:57
 */
@Data
@ApiModel("考生成绩分析--基本信息")
public class OfficialTestUserInfoDTO implements Serializable {


    /**
     * 考生id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("用户ID")
    private Long userId;


    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String nickName;


    /**
     * 用户账号
     */
    @ApiModelProperty("用户账号")
    private String userName;


    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @ApiModelProperty("最后登录时间")
    private Date time;

    /**
     * 考试次数
     */
    @ApiModelProperty("考试次数")
    private Integer totalNum;

    /**
     * 及格次数
     */
    @ApiModelProperty("及格次数")
    private Integer totalPassNum;

    /**
     * 通过率
     */
    @ApiModelProperty("通过率")
    private Double passRate;


}
