package com.garm.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 试卷管理
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-09 09:14:42
 */
@Data
@TableName("exam_paper")
public class PaperEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 试卷id
	 */
	@TableId(type = IdType.INPUT)
	private Long paperId;
	/**
	 * 试卷名称
	 */
	private String paperName;
	/**
	 * 服务类型id，来自码表
	 */
	private Long serviceType;
	/**
	 * 总分数
	 */
	private BigDecimal totalScore;
	/**
	 * 总题数
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
	 * 是否删除 1.已删除 2.正常
	 */
	private Integer isDel;
	/**
	 * 是否已被考试  1-未被使用过  2-已被使用
	 */
	private Integer isTest;

	private Long createUserId;

}
