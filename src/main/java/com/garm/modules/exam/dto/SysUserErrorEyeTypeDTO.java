package com.garm.modules.exam.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户考试错题题眼信息
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-11 16:39:23
 */
@Data
@ApiModel(value = "sys_user_error_eye_type")
public class SysUserErrorEyeTypeDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty("主键id")
private Long id;
	/**
	 * 答案id
	 */
	@ApiModelProperty("答案id")
private Long answerId;
	/**
	 * 考试id
	 */
	@ApiModelProperty("考试id")
private Long testId;
	/**
	 * 用户id（分析考试是没有）
	 */
	@ApiModelProperty("用户id（分析考试是没有）")
private Long userId;
	/**
	 * 题眼类型id，来自码表
	 */
	@ApiModelProperty("题眼类型id，来自码表")
private Long eyeType;
	/**
	 * 总题数
	 */
	@ApiModelProperty("总题数")
private Integer totalNum;
	/**
	 * 正确题数
	 */
	@ApiModelProperty("正确题数")
private Integer trueNum;
	/**
	 * 未答题数
	 */
	@ApiModelProperty("未答题数")
private Integer unansweredNum;
	/**
	 * 错误题数
	 */
	@ApiModelProperty("错误题数")
private Integer falseNum;
	/**
	 * 错题百分比
	 */
	@ApiModelProperty("错题百分比")
private Double errorPercentage;
	/**
	 * 类型 1-考试错题统计  2-用户错题统计
	 */
	@ApiModelProperty("类型 1-考试错题统计  2-用户错题统计")
private Integer type;
	/**
	 * 创建时间
	 */
	@ApiModelProperty("创建时间")
private Date createTime;

}
