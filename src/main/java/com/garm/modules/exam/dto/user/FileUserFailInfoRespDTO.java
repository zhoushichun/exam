package com.garm.modules.exam.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("失败详情")
public class FileUserFailInfoRespDTO {


    /**
     * 失败用户名
     */
    @ApiModelProperty("失败用户名")
    private String nickName;

    /**
     * 行号
     */
    @ApiModelProperty("行号")
    private Integer rowNum;

    /**
     * 失败原因
     */
    @ApiModelProperty("失败原因")
    private String reason;


}
