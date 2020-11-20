package com.garm.modules.web.dto.shop;

import lombok.Data;

import java.util.Date;

/**
 * 考试答卷查询类
 * @author rkg
 */
@Data
public class UserTestQueryDTO {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 服务类型id
     */
    private Integer serviceType;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

}
