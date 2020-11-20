package com.garm.modules.web.dto.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.garm.modules.exam.dto.library.LibraryDetailDTO;
import com.garm.modules.exam.dto.paper.PaperDetailDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 数据
 */
@Data
@ApiModel("考试数据")
public class OfficialTestRespDTO {


    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("试卷ID")
    private Long officialTestId;


    @ApiModelProperty("试卷名称")
    private String officialTestName;

    /**
     * 正确题数
     */
    @ApiModelProperty("正确题数")
    private Integer trueNum;


    /**
     * 考试所用时间，单位：秒
     */
    @ApiModelProperty("考试所用时间，单位：秒")
    private Long stateTime;


    /**
     * 考试详情信息
     */
    @ApiModelProperty("考试详情信息")
    private PaperDetailDTO testDetail;

    /**
     * 错题详细信息
     */
    @ApiModelProperty("错题详细信息")
    private PaperDetailDTO errorDetail;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 考试开始时间
     */
    @ApiModelProperty("考试开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date testStartTime;

    /**
     * 考试结束时间
     */
    @ApiModelProperty("考试结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date testEndTime;

    /**
     * 考试实际开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("用户开始考试时间")
    private Date startTime;

    /**
     * 考试实际结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("用户考试结束时间")
    private Date endTime;

    /**
     * 考试预计结束时间
     */
    @ApiModelProperty("用户考试预计结束时间")
    private Date expectEndTime;

    /**
     * 考试倒计时,单位：秒
     */
    @ApiModelProperty("考试倒计时,单位：秒")
    private Long countdown;

    /**
     * 考题数量
     */
    @ApiModelProperty("考题总数量")
    private Integer totalNum;

    /**
     * 考试时间,单位(分)
     */
    @ApiModelProperty("考试时间,单位(分)")
    private Long time;


    /**
     * 得分
     */
    @ApiModelProperty("用户得分")
    private BigDecimal scores;

}
