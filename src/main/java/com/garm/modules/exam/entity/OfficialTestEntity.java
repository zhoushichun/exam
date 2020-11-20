package com.garm.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 官方考试
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-09 13:27:29
 */
@Data
@TableName("exam_official_test")
public class OfficialTestEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 考试id
	 */
	@TableId(type = IdType.INPUT)
	private Long officialTestId;
	/**
	 * 考试名称
	 */
	private String officialTestName;
	/**
	 * 服务类型id
	 */
	private Long serviceType;
	/**
	 * 总分数
	 */
	private BigDecimal totalScore;
	/**
	 * 及格分数
	 */
	private BigDecimal passScore;
	/**
	 * 是否乱序 1-否 2-是
	 */
	private Integer disorderSeq;
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
	 * 考试所用时间限制，单位：分
	 */
	private Long time;
	/**
	 * 逻辑状态1:删除，2:正常
	 */
	private Integer isDel;

}
