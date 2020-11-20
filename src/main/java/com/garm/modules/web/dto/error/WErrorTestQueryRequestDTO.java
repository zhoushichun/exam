package com.garm.modules.web.dto.error;

import com.garm.common.utils.PagerModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author liwt
 * @Date 2020/5/6 9:30
 * @Description
 * @Version 1.0.0
 */
@Data
@ApiModel("错题查询请求参数")
public class WErrorTestQueryRequestDTO extends PagerModel {

    /**
     * 考试名称
     */
    @ApiModelProperty("考试名称")
    private String testName;

    /**
     * 服务类型
     */
    @ApiModelProperty("服务类型")
    private Integer serviceType;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private String endTime;

    /**
     * 试卷类型 1-官方考试 2-自测考试
     */
    @ApiModelProperty("试卷类型 1-官方考试 2-自测考试")
    @NotNull(message = "请输入考试类型")
    private Integer type = 1;

}
