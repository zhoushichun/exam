package com.garm.modules.exam.dto.test.official;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 官方考试用户关联
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-09 17:38:48
 */
@Data
@ApiModel(value = "exam_official_test_user")
public class OfficialTestUserDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty("主键id")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long id;
	/**
	 * 考试id
	 */
	@ApiModelProperty("考试id")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long officialTestId;
	/**
	 * 用户id
	 */
	@ApiModelProperty("用户id")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long userId;
	/**
	 * 是否已考试 1-未考试 2-正在考试,3-已结束
	 */
	@ApiModelProperty("是否已考试 1-未考试 2-正在考试,3-已结束")
	private Integer doTest;
	/**
	 * 正确题数
	 */
	@ApiModelProperty("正确题数")
	private Integer trueNum;
	/**
	 * 错误题数
	 */
	@ApiModelProperty("错误题数")
	private Integer falseNum;
	/**
	 * 分数
	 */
	@ApiModelProperty("分数")
	private BigDecimal scores;
	/**
	 * 考试所用时间，单位：毫秒
	 */
	@ApiModelProperty("考试所用时间，单位：毫秒")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long stateTime;
	/**
	 * 是否及格 1-未及格 2-及格
	 */
	@ApiModelProperty("是否及格 1-未及格 2-及格")
	private Integer isPass;
	/**
	 * 考试详情信息
	 */
	@ApiModelProperty("考试详情信息")
	private String testDetail;
	/**
	 * 错题详细信息
	 */
	@ApiModelProperty("错题详细信息")
	private String errorDetail;
	/**
	 * 考试实际开始时间
	 */
	@ApiModelProperty("考试实际开始时间")
	private Date startTime;
	/**
	 * 考试实际结束时间
	 */
	@ApiModelProperty("考试实际结束时间")
	private Date endTime;
	/**
	 * 考试预计结束时间
	 */
	@ApiModelProperty("考试预计结束时间")
	private Date expectEndTime;
	/**
	 * 创建时间
	 */
	@ApiModelProperty("创建时间")
	private Date createTime;

}
