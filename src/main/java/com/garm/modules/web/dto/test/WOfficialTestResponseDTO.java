package com.garm.modules.web.dto.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author ldx
 * @Date 2020/4/28 17:37
 * @Description
 * @Version 1.0.0
 */
@Data
@ApiModel("列表返回数据")
public class WOfficialTestResponseDTO implements Serializable {

    /**
     * 考试id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("考试id")
    private Long officialTestId;

    /**
     * 考试名称
     */
    @ApiModelProperty("考试名称")
    private String officialTestName;

    /**
     * 服务类型名称，来自码表
     */
    @ApiModelProperty("服务类型名称")
    private String serviceTypeName;

    /**
     * 总分数
     */
    @ApiModelProperty("总分数")
    private Integer totalScore;

    /**
     * 及格分数
     */
    @ApiModelProperty("及格分数")
    private Integer passScore;

    /**
     * 考试所用时间限制，单位：分
     */
    @ApiModelProperty("考试已用时间限制，单位：秒")
    private Integer stateTime;

    /**
     * 考试开始时间
     */
    @ApiModelProperty("考试开始时间,yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date gmtTestStart;

    /**
     * 考试结束时间
     */
    @ApiModelProperty("考试结束时间,yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date gmtTestEnd;

    /**
     * 状态 1:未考试,2:正在考试,3:已结束
     */
    @ApiModelProperty("状态 1:未考试,2:正在考试,3:已结束")
    private Integer doTest;

    /**
     * 文件路径
     */
    @ApiModelProperty("文件路径")
    private String filePath;

    /**
     * 得分
     */
    @ApiModelProperty("用户得分")
    private String scores;

}
