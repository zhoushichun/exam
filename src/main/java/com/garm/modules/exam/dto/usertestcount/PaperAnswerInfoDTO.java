package com.garm.modules.exam.dto.usertestcount;


import com.alibaba.fastjson.JSONArray;
import com.garm.modules.exam.dto.paper.PaperDetailDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("试卷详情")
public class PaperAnswerInfoDTO {

    @ApiModelProperty("服务名称")
    private String serviceTypeName;

    @ApiModelProperty("服务名称ID")
    private Long serviceType;

    @ApiModelProperty("考试名称")
    private String testName;

    @ApiModelProperty("试卷详情")
    private PaperDetailDTO testDetail;

}
