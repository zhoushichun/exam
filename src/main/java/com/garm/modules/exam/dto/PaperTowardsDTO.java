package com.garm.modules.exam.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 成绩分析
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-11 16:39:53
 */
@Data
@ApiModel(value = "试题问卷走向")
public class PaperTowardsDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 走向id
	 */
	@ApiModelProperty("走向id")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long towardsId;
	/**
	 * 新增试题总数
	 */
	@ApiModelProperty("新增试题总数")
	private Integer questionsCount;
	/**
	 * 考试试题总数
	 */
	@ApiModelProperty("考试试题总数")
	private Integer testQuestionsCount;
	/**
	 * 自测试题总数
	 */
	@ApiModelProperty("自测试题总数")
	private Integer seltQuestionsCount;
	/**
	 * 试卷总数
	 */
	@ApiModelProperty("试卷总数")
	private Integer paperCount;
	/**
	 * 考试总数
	 */
	@ApiModelProperty("考试总数")
	private Integer testCount;
	/**
	 * 时间
	 */
	@ApiModelProperty("时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private String createTime;

}
