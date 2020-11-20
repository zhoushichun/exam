package com.garm.modules.exam.dto.library;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.garm.common.validator.group.AddGroup;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * 试题选项
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-08 10:42:37
 */
@Data
@ApiModel(value = "试题选项")
public class LibraryOptionDTO  {

	/**
	 * 选项内容
	 */
	@ApiModelProperty("选项内容")
	private String optionContent;
	/**
	 * 选项类型，如，单选和多选1--A 2--B 3--C ,判断为1正确,2错误;填空为1,2,3,4
	 */
	@ApiModelProperty("选项类型，如，单选和多选1--A 2--B 3--C ,判断为1正确,2错误;填空为1,2,3,4")
	private String optionType;

}
