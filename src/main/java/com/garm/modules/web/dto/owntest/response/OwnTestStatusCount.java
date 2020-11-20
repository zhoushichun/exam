package com.garm.modules.web.dto.owntest.response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liwt
 * @version v1
 * @file OwnTestStatusCount
 * @description 考试状态统计
 * @createTime 2020/5/22 10:32
 * @copyright 极梦科技
 */
@Data
@Api("自测统计数据")
public class OwnTestStatusCount implements Serializable {

    /**
     * 自测中
     */
    @ApiModelProperty("自测中")
    private Integer testingNum;

    /**
     * 自测记录
     */
    @ApiModelProperty("自测记录")
    private Integer endNum;


    public OwnTestStatusCount(Integer testingNum, Integer endNum) {
        this.testingNum = testingNum;
        this.endNum = endNum;
    }

    public OwnTestStatusCount() {}
}
