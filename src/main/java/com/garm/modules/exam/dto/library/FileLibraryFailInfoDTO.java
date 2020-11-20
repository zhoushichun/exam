package com.garm.modules.exam.dto.library;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "失败详情")
public class FileLibraryFailInfoDTO {


    /**
     * 失败题型
     */
    @ApiModelProperty("失败题型")
    private String paperName;

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
