package com.garm.modules.web.dto.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.garm.modules.exam.dto.paper.PaperDetailDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 自测数据
 */
@Data
@ApiModel("错题数据")
public class ErrorTestRespDTO {

    /**
     * 考试ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("考试ID")
    private Long testId;

    /**
     * 考试名称
     */
    @ApiModelProperty("考试名称")
    private String testName;

    /**
     * 错误题数
     */
    @ApiModelProperty("错误题数")
    private Integer falseNum;

    /**
     * 错题详细信息
     */
    @ApiModelProperty("错题详细信息")
    private String errorDetail;

    /**
     * 考试实际开始时间
     */
    @ApiModelProperty("考试实际开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 考试实际结束时间
     */
    @ApiModelProperty("考试实际结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    //服务名称类型
    @ApiModelProperty("服务名称类型")
    private Long serviceType;

    //服务名称
    @ApiModelProperty("服务名称")
    private String serviceTypeName;

    /**
     * 文件路径
     */
    @ApiModelProperty("文件路径")
    private String filePath;

}
