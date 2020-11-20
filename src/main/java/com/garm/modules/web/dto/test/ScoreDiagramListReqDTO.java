package com.garm.modules.web.dto.test;

import com.garm.common.utils.PagerModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xq
 * @title ScoreDiagramListReqDTO
 * @description
 * @date 2020/4/30 14:24
 */
@Data
@ApiModel("成绩曲线参数")
public class ScoreDiagramListReqDTO extends PagerModel implements Serializable {

    /**
     * 考试用户id
     */
    @ApiModelProperty(hidden = true)
    private Long userId;

    /**
     * 服务类型id 来自于数据字典
     */
    @ApiModelProperty("服务类型ID")
    private Long serviceType;

    /**
     * 是否已考试 1-否 2-是
     */
    @ApiModelProperty("是否已考试 1-否 2-是")
    private Integer doTest;


}
