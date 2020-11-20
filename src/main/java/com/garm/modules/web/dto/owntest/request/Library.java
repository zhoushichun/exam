package com.garm.modules.web.dto.owntest.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.util.List;

/**
 * 题库管理
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-08 10:42:37
 */
//@ApiModel(value = "试题保存详情")
public class Library {

	@ApiModelProperty(value= "题库id")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long libraryId;

	@ApiModelProperty(value= "试题类型，1.单选题 2.多选题 3.判断题 4.填空题 5.阅读理解")
	private Integer type;


	@ApiModelProperty(value= "用户回答的答案,当类型为1单选题时，答案为1--A 2--B 3--C 4--D,当类型为2多选题时,1--A 2--B 3--C 4--D,多个答案之间用逗号,连接;当类型为3判断题时，1---正确，2---错误;当类型为4填空题时，多个答案用',@#,',即英文状态的逗号加上@加上#加上逗号连接")
	private String reply;


	@ApiModelProperty(value= "当为阅读理解时的子题数据集合")
	private List<LibraryChild> childDatas;

	/**
	 * 标记，是否已填写
	 */
	@ApiModelProperty(value= "标记，是否已填写,false为未填写，true为已填写")
	private boolean mark = false;

	public Long getLibraryId() {
		return libraryId;
	}

	public void setLibraryId(Long libraryId) {
		this.libraryId = libraryId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public List<LibraryChild> getChildDatas() {
		return childDatas;
	}

	public void setChildDatas(List<LibraryChild> childDatas) {
		this.childDatas = childDatas;
	}

	public boolean isMark() {
		return mark;
	}

	public void setMark(boolean mark) {
		this.mark = mark;
	}
}
