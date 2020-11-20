package com.garm.modules.exam.dto.library;

import java.util.List;

import com.garm.common.validator.group.AddGroup;
import com.garm.common.validator.group.UpdateGroup;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 题库管理
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-08 10:42:37
 */
@Data
@ApiModel(value = "试题详情")
public class LibraryDTO extends LibraryBaseDTO {

	/**
	 * 正确答案,当类型为1单选题时,1--A 2--B 3--C 4--D;当类型为2多选题时,1--A 2--B 3--C 4--D,多个答案之间用逗号,连接;当类型为3判断题时，1---正确，2---错误;当类型为4填空题时，无答案,选项即答案
	 */
	@ApiModelProperty(value = "正确答案",notes = "当类型为1单选题时,1--A 2--B 3--C 4--D;当类型为2多选题时,1--A 2--B 3--C 4--D,多个答案之间用逗号,连接;当类型为3判断题时，1---正确，2---错误;当类型为4填空题时，无答案,选项即答案")
	private String answer;

	@ApiModelProperty("用户回答的答案,当类型为1单选题时，答案为1--A 2--B 3--C 4--D,当类型为2多选题时,1--A 2--B 3--C 4--D,多个答案之间用逗号,连接;当类型为3判断题时，1---正确，2---错误;当类型为4填空题时，多个答案用',@#,',即英文状态的逗号加上@加上#加上逗号连接")
	private String reply;
	/**
	 * 答题解析
	 */
	@ApiModelProperty("答题解析")
	@NotBlank(groups = {AddGroup.class},message = "请填写答案解析")
	private String answerAnalysis;

	@ApiModelProperty("选项")
	@NotEmpty(groups = {AddGroup.class},message = "选项不能为空")
	private List<LibraryOptionDTO> optionDatas;

	@ApiModelProperty("当为阅读理解时的子题数据集合")
	private List<LibraryChildDTO> childDatas;

}
