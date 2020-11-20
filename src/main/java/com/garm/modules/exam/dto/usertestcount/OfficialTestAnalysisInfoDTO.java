package com.garm.modules.exam.dto.usertestcount;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liwt
 *
 * @title OfficialTestAnalysisInfoRespDTO
 * @description
 * @date 2020/5/11 16:57
 */
@Data
@ApiModel("成绩分析")
public class OfficialTestAnalysisInfoDTO implements Serializable {


    /**
     * 题眼名称
     */
    @ApiModelProperty("题眼名称")
    private String eyeTypeName;

    /**
     * 题眼
     */
    @ApiModelProperty("题眼")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long eyeType;

    /**
     * 题数
     */
    @ApiModelProperty("题数")
    private Integer totalNum;

    /**
     * 错误题数
     */
    @ApiModelProperty("错误题数")
    private Integer falseNum;

    /**
     * 正确题数
     */
    @ApiModelProperty("正确题数")
    private Integer trueNum;

    /**
     * 未答题数
     */
    @ApiModelProperty("未答题数")
    private Integer unansweredNum;

    /**
     * 正确率
     */
    @ApiModelProperty("正确率")
    private Double truePercentage;

    /**
     * 错题率
     */
    @ApiModelProperty
    private Double errorPercentage;



}
