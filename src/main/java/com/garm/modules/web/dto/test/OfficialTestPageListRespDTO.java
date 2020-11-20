package com.garm.modules.web.dto.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xq
 * @title OfficialTestPageListRespDTO
 * @description
 * @date 2020/4/30 11:27
 */
@Data
@ApiModel("前端用户考试列表")
public class OfficialTestPageListRespDTO implements Serializable {

    /**
     * 考试ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("考试ID")
    private Long officialTestId;

    /**
     * 考试名称
     */
    @ApiModelProperty("考试名称")
    private String officialTestName;

    /**
     * 服务类型id，来自码表
     */
    @ApiModelProperty("服务类型id")
    private Long serviceType;

    /**
     * 服务类型名称，来自码表
     */
    @ApiModelProperty("服务类型名称")
    private String serviceTypeName;

    /**
     * 考试所用时间限制，单位：分
     */
    @ApiModelProperty("考试所用时间限制，单位：分")
    private Integer stateTime;

    /**
     * 总分数
     */
    @ApiModelProperty("总分数")
    private Integer totalScore;

    /**
     * 及格分数
     */
    @ApiModelProperty("及格分数")
    private Integer passScore;

    /**
     * 考试开始时间
     */
    @ApiModelProperty("考试开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date gmtTestStart;

    /**
     * 考试结束时间
     */
    @ApiModelProperty("考试结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date gmtTestEnd;


    @ApiModelProperty("图片路径")
    private String filePath;
}
