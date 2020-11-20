package com.garm.modules.web.dto.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.garm.modules.exam.dto.usertestcount.OfficialTestAnalysisInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author liwt
 *
 * @title OfficialTestAnalysisRespDTO
 * @description
 * @date 2020/5/11 16:57
 */
@Data
@ApiModel("前台成绩分析")
public class OfficialTestAnalysisRespDTO implements Serializable {

    /**
     * 服务分类
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("服务分类")
    private Long serviceType;

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
     * 题眼数据集合
     */
    @ApiModelProperty("题眼数据集合")
    private List<OfficialTestAnalysisInfoDTO> datas;

}
