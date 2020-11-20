package com.garm.modules.web.dto.owntest.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.garm.common.utils.PagerModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;

/**
 * @Author liwt
 * @Date 2020/4/28 11:47
 * @Description
 * @Version 1.0.0
 */
@Data
@ApiModel("自测列表参数")
public class WOwnTestQueryDTO extends PagerModel {

    /**
     * 自测名称
     */
    @ApiModelProperty("自测名称")
    private String ownTestName;

    /**
     * 考试结束标识;1，已结束;2,未结束
     */
    @NotNull(message = "考试结束标识不能为空")
    @ApiModelProperty("考试结束标识;1，已结束;2,未结束")
    private Integer isEnd;

    /**
     * 服务分类id
     */
    @ApiModelProperty("服务分类id")
    private Integer serviceId;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间,yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间,yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private String endTime;

}
