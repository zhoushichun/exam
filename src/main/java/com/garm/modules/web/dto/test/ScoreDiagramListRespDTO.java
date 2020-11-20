package com.garm.modules.web.dto.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author xq
 * @title ScoreDiagramListRespDTO
 * @description 前端-首页成绩曲线图
 * @date 2020/4/30 14:19
 */
@Data
@ApiModel("成绩曲线数据")
public class ScoreDiagramListRespDTO implements Serializable {


    /**
     * 考试id
     */
    @ApiModelProperty("考试id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long officialTestId;
    /**
     * 考试名称
     */
    @ApiModelProperty("考试名称")
    private String officialTestName;

    /**
     * 分数
     */
    @ApiModelProperty("分数")
    private BigDecimal scores;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date gmtStartTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date gmtEntTime;

    //服务名称
    @ApiModelProperty("服务名称")
    private String serviceTypeName;

    //考试时间(单位:分)
    @ApiModelProperty("考试时间(单位:分)")
    private Integer time;

    //试卷总分
    @ApiModelProperty("试卷总分")
    private Integer totalScores;

    //及格分
    @ApiModelProperty("及格分")
    private Integer passScores;

}
