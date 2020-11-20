package com.garm.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 自测考试
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-11 16:40:56
 */
@Data
@TableName("exam_own_test")
public class OwnTestEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自测ID
	 */
	@TableId(type = IdType.INPUT)
	private Long ownTestId;
	/**
	 * 自测名称
	 */
	private String ownTestName;
	/**
	 * 自测时间(分钟)
	 */
	private Integer time;
	/**
	 * 服务类型id，来自码表
	 */
	private Long serviceType;
	/**
	 * 试题总数
	 */
	private Integer totalNum;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改时间
	 */
	private Date updateTime;
	/**
	 * 考试开始时间
	 */
	private Date testStartTime;
	/**
	 * 考试结束时间
	 */
	private Date testEndTime;
	/**
	 * 用户ID
	 */
	private Long userId;
	/**
	 * 正确题数
	 */
	private Integer trueNum;
	/**
	 * 错误题数
	 */
	private Integer falseNum;
	/**
	 * 考试所用时间，单位：秒
	 */
	private Long stateTime;
	/**
	 * 考试详情信息
	 */
	private String testDetail;
	/**
	 * 错题详细信息
	 */
	private String errorDetail;
	/**
	 * 考试实际开始时间
	 */
	private Date startTime;
	/**
	 * 考试实际结束时间
	 */
	private Date endTime;
	/**
	 * 考试预计结束时间
	 */
	private Date expectEndTime;

}
