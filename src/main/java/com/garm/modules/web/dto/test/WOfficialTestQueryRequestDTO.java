package com.garm.modules.web.dto.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.garm.common.utils.PagerModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.ibatis.annotations.Param;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author ldx
 * @Date 2020/4/28 11:47
 * @Description
 * @Version 1.0.0
 */
@Data
@ApiModel("请求参数")
public class WOfficialTestQueryRequestDTO extends PagerModel {

    /**
     * 考试名称
     */
    @ApiModelProperty("考试名称")
    private String officialTestName;

    /**
     * 服务类型id
     */
    @ApiModelProperty("服务类型ID")
    private Long serviceId;

    /**
     * 考试状态 1:未考试,2:正在进行中,3:已结束
     */
    @ApiModelProperty("考试状态 1:未考试,2:正在进行中,3:已结束")
    private Integer doTest ;

    /**
     * 考试开始时间
     */
    @ApiModelProperty("考试开始时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private String gmtTestStart;

    /**
     * 考试结束时间
     */
    @ApiModelProperty("考试结束时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private String gmtTestEnd;

    /**
     * 用户id
     */
    @ApiModelProperty(hidden = true)
    private Long userId;

    /**
     * 全部/已完成;1,全部;2,已完成
     */
    @ApiModelProperty("全部/已完成;1,全部;2,已完成")
    private Integer isAll;

}
