package com.garm.modules.web.dto.index;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.garm.common.utils.PagerModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @Author ldx
 * @Date 2020/4/28 11:47
 * @Description
 * @Version 1.0.0
 */
@Data
public class WOfficialTestQueryRequestDTO extends PagerModel {

    /**
     * 考试名称
     */
    private String officialTestName;

    /**
     * 服务类型id
     */
    private Long serviceId;

    /**
     * 考试状态 1:未考试,2:正在进行中,3:已结束
     */
    private Integer doTest ;

    /**
     * 考试开始时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime gmtTestStart;

    /**
     * 考试结束时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime gmtTestEnd;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 全部/已完成;1,全部;2,已完成
     */
    private Integer isAll;

}
