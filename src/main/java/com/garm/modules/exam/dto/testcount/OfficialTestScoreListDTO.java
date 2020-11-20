package com.garm.modules.exam.dto.testcount;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xq
 * @title OfficialTestScoreListRespDTO
 * @description
 * @date 2020/5/9 11:39
 */
@Data
@ApiModel("考试成绩统计列表")
public class OfficialTestScoreListDTO implements Serializable {

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
     * 服务类型
     */
    @ApiModelProperty("服务类型")
    private String serviceType;

    /**
     * 考试名称
     */
    @ApiModelProperty("考试名称")
    private String officialTestName;

    /**
     * 参加人数
     */
    @ApiModelProperty("参加人数")
    private Integer totalNum;

    /**
     * 平均分
     */
    @ApiModelProperty("平均分")
    private Double avgScore;

    /**
     * 平均时长/分钟
     */
    @ApiModelProperty("平均时长/分钟")
    private Integer avgMin;

    /**
     * 不及格人数
     */
    @ApiModelProperty("不及格人数")
    private Integer notPassNum;

}
