package com.garm.modules.exam.dto.library;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

@Data
@ApiModel(value = "文件上传失败信息")
public class FileUploadFailDTO<T> {


    /**
     * 成功数量
     */
    @ApiModelProperty("成功数量")
    private Integer successNum;

    /**
     * 失败数量
     */
    @ApiModelProperty("失败数量")
    private Integer failNum;

    /**
     * 失败数据信息
     */
    @ApiModelProperty("失败数据信息")
    private List<T> failDatas;


}
