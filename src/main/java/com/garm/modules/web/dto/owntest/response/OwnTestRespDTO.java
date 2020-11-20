package com.garm.modules.web.dto.owntest.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.garm.modules.exam.dto.library.LibraryDetailDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 自测数据
 */
@Data
@ApiModel("自测数据")
public class OwnTestRespDTO {


    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("试卷ID")
    private Long ownTestId;


    @ApiModelProperty("试卷名称")
    private String ownTestName;

    /**
     * 正确题数
     */
    @ApiModelProperty("正确题数")
    private Integer trueNum;


    /**
     * 考试所用时间，单位：秒
     */
    @ApiModelProperty("考试所用时间，单位：秒")
    private Long stateTime;


    /**
     * 剩余题数
     */
    @ApiModelProperty("剩余题数")
    private Integer remainderNum;


    /**
     * 考试详情信息
     */
    @ApiModelProperty("考试详情信息")
    private List<LibraryDetailDTO> testDetail;

    /**
     * 错题详细信息
     */
    @ApiModelProperty("错题详细信息")
    private List<LibraryDetailDTO> errorDetail;


    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date gmtCreate;

    /**
     * 考试实际开始时间
     */
    @ApiModelProperty("考试实际开始时间")
    private Date gmtStartTime;

    /**
     * 考试实际结束时间
     */
    @ApiModelProperty("考试实际结束时间")
    private Date gmtEntTime;

    /**
     * 考试预计结束时间
     */
    @ApiModelProperty("考试预计结束时间")
    private Date gmtExpectEndTime;

    /**
     * 考试倒计时,单位：秒
     */
    @ApiModelProperty("考试倒计时,单位：秒")
    private Long countdown;

    /**
     * 考题数量
     */
    @ApiModelProperty("考题数量")
    private Integer totalNum;

    /**
     * 考试时间,单位(分)
     */
    @ApiModelProperty("考试时间,单位(分)")
    private Integer time;

}
