package com.garm.modules.exam.dto.errortest;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author xq
 * @title OfficialTestErrorStatisticsRespDTO
 * @description
 * @date 2020/5/7 11:18
 */
@Data
@ApiModel("考试错题集统计列表")
public class OfficialTestErrorListDTO implements Serializable {

    /**
     * 考试ID
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
     * 服务类型ID
     */
    @ApiModelProperty("服务类型ID")
    private Long serviceType;

    /**
     * 考试名称
     */
    @ApiModelProperty("考试名称")
    private String officialTestName;

    /**
     * 考试开始时间
     */
    @ApiModelProperty("考试开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date testStartTime;

    /**
     * 考试结束时间
     */
    @ApiModelProperty("考试结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date testEndTime;
}
