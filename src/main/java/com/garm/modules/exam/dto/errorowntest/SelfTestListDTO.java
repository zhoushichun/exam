package com.garm.modules.exam.dto.errorowntest;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xq
 * @title SelfTestListRespDTO
 * @description
 * @date 2020/5/8 10:10
 */
@Data
@ApiModel("自测详情统计")
public class SelfTestListDTO implements Serializable {

    /**
     * 自测ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("自测ID")
    private Long ownTestId;

    /**
     * 自测名称
     */
    @ApiModelProperty("自测ID")
    private String ownTestName;
    /**
     * 服务类型名称，来自码表
     */
    @ApiModelProperty("服务类型名称")
    private String serviceTypeName;

    @ApiModelProperty("服务类型名称")
    private Long serviceType;

    /**
     * 自测题数量
     */
    @ApiModelProperty("自测题数量")
    private Integer ownNum;

    /**
     * 考试总时长/分钟
     */
    @ApiModelProperty("考试总时长/分钟")
    private Long testMin;

}
