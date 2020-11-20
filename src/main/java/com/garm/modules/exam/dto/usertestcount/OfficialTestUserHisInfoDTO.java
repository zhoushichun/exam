package com.garm.modules.exam.dto.usertestcount;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author liwt
 *
 * @title OfficialTestUserHisInfoRespDTO
 * @description
 * @date 2020/5/11 13:57
 */
@Data
@ApiModel("考试记录列表")
public class OfficialTestUserHisInfoDTO implements Serializable {
    //主键ID
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("记录ID")
    private Long id;

    /**
     * 考生id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("考生id")
    private Long userId;

    /**
     * 考试id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("考试id")
    private Long officialTestId;

    /**
     * 服务分类
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("服务分类")
    private Long serviceType;

    /**
     * 考试名称
     */
    @ApiModelProperty("考试名称")
    private String testName;

    /**
     * 服务类型名称，来自码表
     */
    @ApiModelProperty("服务类型名称")
    private String serviceTypeName;


    /**
     * 考试名称
     */
    @ApiModelProperty("考试名称")
    private String officialTestName;


    /**
     * 考试开始时间
     */
    @ApiModelProperty("考试开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date testStartTime;

    /**
     * 考试结束时间
     */
    @ApiModelProperty("考试结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date testEndTime;


    /**
     * 总分数
     */
    @ApiModelProperty("总分数")
    private Integer totalScore;

    /**
     * 用户得分
     */
    @ApiModelProperty("用户得分")
    private Integer scores;

    /**
     * 是否及格 1-未及格 2-及格
     */
    @ApiModelProperty("是否及格 1-未及格 2-及格")
    private Integer isPass;


}
