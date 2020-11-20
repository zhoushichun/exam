package com.garm.modules.exam.dto.errortest;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xq
 * @title OfficialTestErrorStatisticsRespDTO
 * @description
 * @date 2020/5/7 11:18
 */
@Data
@ApiModel("错题统计列表")
public class OfficialTestErrorStatisticsDTO implements Serializable {

    /**
     * 题眼类型id，来自码表
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("题眼类型id")
    private Long eyeType;
    /**
     * 题眼类型名称，来自码表
     */
    @ApiModelProperty("题眼类型名称")
    private String eyeTypeName;
    /**
     * 总题数
     */
    @ApiModelProperty("总题数")
    private Integer totalEyesNum;

    /**
     * 错题百分比
     */
    @ApiModelProperty("错题百分比")
    private Double errorPercentage;

}
