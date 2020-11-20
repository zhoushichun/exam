package com.garm.modules.exam.dto.library;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.garm.common.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 题库公共属性类
 */
@Data
@ApiModel(value = "题库列表返回属性")
public class LibraryListDTO extends LibraryBaseDTO{


    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("题眼类型名称")
    private String eyeTypeName;

    @ApiModelProperty("服务类型名称")
    private String serviceTypeName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 答题解析
     */
    @ApiModelProperty("答题解析")
    @NotBlank(groups = {AddGroup.class},message = "请填写答案解析")
    private String answerAnalysis;

}
