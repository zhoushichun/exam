package com.garm.modules.exam.dto.analyze;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author ldx
 * @Date 2020/5/9 10:20
 * @Description
 * @Version 1.0.0
 */
@Data
@ApiModel("考试分析---考试数据、试题分析")
public class TestAnalyzeDTO implements Serializable {

    //试题总数
    @ApiModelProperty("试题总数")
    private Integer libraryCount;

    //试卷总数
    @ApiModelProperty("试卷总数")
    private Integer paperCount;

    //发布考试总数
    @ApiModelProperty("发布考试总数")
    private Integer testCount;

    //考试总数
    @ApiModelProperty("考试总数")
    private Integer testUserCount;

    //自测总数
    @ApiModelProperty("自测总数")
    private Integer ownTestCount;

    //考试题眼数据
    @ApiModelProperty("考试题眼数据")
    private List<EyeAnalyze> testUserEyeAnalyze;

    //自测题眼数据
    @ApiModelProperty("自测题眼数据")
    private List<EyeAnalyze> ownTestEyeAnalyze;

    @Data
    public static class EyeAnalyze{

        //总数
        @ApiModelProperty("总数")
        private Integer totalNum;

        //题眼名称
        @ApiModelProperty("题眼名称")
        private String eyeTypeName;

    }

}
