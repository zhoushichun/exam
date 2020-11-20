package com.garm.modules.exam.dto.library;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.garm.common.validator.group.AddGroup;
import com.garm.common.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 题库公共属性类
 */
@Data
@ApiModel(value = "子题详情")
public class LibraryChildDTO {
    @ApiModelProperty("题库id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long libraryId;
    /**
     * 父题目ID,没有父题时为0
     */
    @ApiModelProperty("父题目ID,没有父题时为0")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentLibraryId;

    @ApiModelProperty("试题题目")
    @NotBlank(groups = { UpdateGroup.class},message = "请填写试题题目")
    private String libraryName;

    @ApiModelProperty("试题题目，带标签")
    private String libraryNameContent;
    /**
     * 试题类型，1.单选题 2.多选题 3.判断题 4.填空题
     */
    @ApiModelProperty("试题类型，1.单选题 2.多选题 3.判断题 4.填空题 ")
    @NotBlank(groups = { UpdateGroup.class},message = "请选择试题类型")
    private Integer type;

    /**
     * 正确答案,当类型为1单选题时,1--A 2--B 3--C 4--D;当类型为2多选题时,1--A 2--B 3--C 4--D,多个答案之间用逗号,连接;当类型为3判断题时，1---正确，2---错误;当类型为4填空题时，无答案,选项即答案
     */
    @ApiModelProperty(value = "正确答案",notes = "当类型为1单选题时,1--A 2--B 3--C 4--D;当类型为2多选题时,1--A 2--B 3--C 4--D,多个答案之间用逗号,连接;当类型为3判断题时，1---正确，2---错误;当类型为4填空题时，无答案,选项即答案")
    private String answer;
    /**
     * 答题解析
     */
    @ApiModelProperty("答题解析")
    @NotBlank(groups = {UpdateGroup.class},message = "请填写答案解析")
    private String answerAnalysis;

    @ApiModelProperty("用户答案,当类型为1单选题时，答案为1--A 2--B 3--C 4--D,当类型为2多选题时,1--A 2--B 3--C 4--D,多个答案之间用逗号,连接;当类型为3判断题时，1---正确，2---错误;当类型为4填空题时，多个答案用',@#,',即逗号加上@加上#加上逗号连接")
    private String reply;

    @ApiModelProperty("选项")
    @NotEmpty(groups = {UpdateGroup.class},message = "请填写选项")
    private List<LibraryOptionDTO> optionDatas;

}
