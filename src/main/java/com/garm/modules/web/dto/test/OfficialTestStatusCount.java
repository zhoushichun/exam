package com.garm.modules.web.dto.test;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xq
 * @version v1
 * @file OfficialTestStatusCount
 * @description 考试状态统计
 * @createTime 2020/5/20 10:32
 * @copyright 极梦科技
 */
@Data
@Api("考试状态")
public class OfficialTestStatusCount implements Serializable {

    /**
     * 未开始
     */
    @ApiModelProperty("未开始")
    private Integer examNotStart;

    /**
     * 考试中
     */
    @ApiModelProperty("考试中")
    private Integer eaxmIngNum;

    /**
     * 已结束
     */
    @ApiModelProperty("已结束")
    private Integer examFinished;

    public OfficialTestStatusCount(Integer examNotStart, Integer eaxmIngNum, Integer examFinished) {
        this.examNotStart = examNotStart;
        this.eaxmIngNum = eaxmIngNum;
        this.examFinished = examFinished;
    }

    public OfficialTestStatusCount() {

    }
}
