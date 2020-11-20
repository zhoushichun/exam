package com.garm.modules.exam.dto.test.official;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.garm.common.validator.group.AddGroup;
import com.garm.common.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 官方考试
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-09 17:38:48
 */
@Data
@ApiModel(value = "考试列表")
public class OfficialTestBaseDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 考试id
	 */
	@ApiModelProperty("考试id")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@NotNull(groups = {UpdateGroup.class},message = "考试ID不能为空")
	private Long officialTestId;
	/**
	 * 服务类型id
	 */
	@ApiModelProperty("服务类型id")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@NotNull(groups = {AddGroup.class, UpdateGroup.class},message = "请选择服务类型")
	private Long serviceType;
	/**
	 * 考试名称
	 */
	@ApiModelProperty("考试名称")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@NotNull(groups = {AddGroup.class, UpdateGroup.class},message = "请填写考试名称")
	private String officialTestName;

	/**
	 * 考试开始时间
	 */
	@ApiModelProperty("考试开始时间,yyyy-MM-dd HH:mm")
	@NotNull(groups = {AddGroup.class, UpdateGroup.class},message = "请选择考试开始时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date testStartTime;
	/**
	 * 考试结束时间
	 */
	@ApiModelProperty("考试结束时间,yyyy-MM-dd HH:mm")
	@NotNull(groups = {AddGroup.class, UpdateGroup.class},message = "请选择考试结束时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date testEndTime;
	/**
	 * 及格分数
	 */
	@ApiModelProperty("及格分数")
	@NotNull(groups = {AddGroup.class, UpdateGroup.class},message = "请输入及格分数")
	@Min(value = 0,message = "及格分数错误")
	@Max(value = 10000,message = "及格分数错误")
	private BigDecimal passScore;
	/**
	 * 总分数
	 */
	@ApiModelProperty("总分数")
	private BigDecimal totalScore;

	/**
	 * 是否乱序 1-否 2-是
	 */
	@ApiModelProperty("是否乱序 1-否 2-是")
	@NotNull(groups = {AddGroup.class},message = "请选择是否乱序")
	@Min(value = 1,message = "参数错误")
	@Max(value = 2,message = "参数错误")
	private Integer disorderSeq;

	/**
	 * 考试所用时间限制，单位：分
	 */
	@ApiModelProperty("考试所用时间限制，单位：分")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@NotNull(groups = {AddGroup.class, UpdateGroup.class},message = "请输入考试时间")
	@Min(value = 0,message = "考试时间错误")
	@Max(value = 10000,message = "考试时间错误")
	private Long time;


}
