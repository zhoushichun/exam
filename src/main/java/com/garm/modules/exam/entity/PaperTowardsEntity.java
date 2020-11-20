package com.garm.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 成绩分析
 * 
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-11 16:39:53
 */
@Data
@TableName("exam_paper_towards")
public class PaperTowardsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 走向id
	 */
	@TableId
	private Long towardsId;
	/**
	 * 新增试题总数
	 */
	private Integer questionsCount;
	/**
	 * 考试试题总数
	 */
	private Integer testQuestionsCount;
	/**
	 * 自测试题总数
	 */
	private Integer seltQuestionsCount;
	/**
	 * 试卷总数
	 */
	private Integer paperCount;
	/**
	 * 考试总数
	 */
	private Integer testCount;
	/**
	 * 时间
	 */
	private Date time;
	/**
	 * 时间
	 */
	private Date createTime;

}
