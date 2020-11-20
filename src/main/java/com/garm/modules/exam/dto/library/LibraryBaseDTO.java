package com.garm.modules.exam.dto.library;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.garm.common.validator.group.AddGroup;
import com.garm.common.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 题库管理
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-08 10:42:37
 */
@Data
public class LibraryBaseDTO implements Serializable {

	@ApiModelProperty("题库id")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long libraryId;

	@ApiModelProperty("试题题目")
	@NotBlank(groups = {AddGroup.class,UpdateGroup.class},message = "请填写试题题目")
	private String libraryName;

	@ApiModelProperty("试题题目，带标签")
	@NotBlank(groups = {AddGroup.class,UpdateGroup.class},message = "请填写试题题目")
	private String libraryNameContent;

	@ApiModelProperty("试题类型，1.单选题 2.多选题 3.判断题 4.填空题 5.阅读理解")
	private Integer type;

	@ApiModelProperty("服务类型id")
	@NotNull(groups = {AddGroup.class,UpdateGroup.class},message = "请选择服务类型")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long serviceType;

	@ApiModelProperty("难度类型,1-简单,2-中等,3-困难")
	@NotNull(groups = {AddGroup.class,UpdateGroup.class},message = "请选择难度")
	private Integer difficultyType;

	@ApiModelProperty("使用范围,1-考试,2-自测,3，都有")
	@NotNull(groups = {AddGroup.class,UpdateGroup.class},message = "请选择使用范围")
	private Integer rangeType;


	@ApiModelProperty("题眼类型id")
	@NotNull(groups = {AddGroup.class,UpdateGroup.class},message = "请选择题眼")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long eyeType;


	@ApiModelProperty("音频/视频路径")
	private String url;


}
