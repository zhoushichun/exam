package com.garm.modules.exam.dto.testcount;

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
@ApiModel("考试成绩分析-基本信息")
public class OfficialTestInfoDTO implements Serializable {


    /**
     * 考试id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("考试ID")
    private Long officialTestId;


    /**
     * 服务类型名称，来自码表
     */
    @ApiModelProperty("服务类型名称")
    private String serviceTypeName;


    /**
     * 服务类型，来自码表
     */
    @ApiModelProperty("服务类型")
    private Long serviceType;


    /**
     * 考试名称
     */
    @ApiModelProperty("考试名称")
    private String officialTestName;


    /**
     * 考试开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("考试开始时间")
    private Date testStartTime ;

    /**
     * 考试结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("考试结束时间")
    private Date testEndTime;


    /**
     * 总分数
     */
    @ApiModelProperty("总分数")
    private Integer totalScore = 0;

    /**
     * 及格分数
     */
    @ApiModelProperty("及格分数")
    private Integer passScore = 0;

    /**
     * 答题时长
     * 考试所用时间限制，单位：分
     */
    @ApiModelProperty("答题时长，考试所用时间限制，单位：分")
    private Integer stateTime = 0;

}
