package com.garm.modules.exam.dto.library;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 试卷添加试题信息
 */
@Data
@ApiModel(value = "试卷添加试题信息")
public class LibraryPaperListDTO extends LibraryBaseDTO{

    /**
     * 子题数量
     */
    @ApiModelProperty("子题数量")
    private String childNum;

    @ApiModelProperty("服务类型名称")
    private String serviceTypeName;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
