package com.garm.modules.exam.dto.paper;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.garm.common.validator.group.AddGroup;
import com.garm.common.validator.group.UpdateGroup;
import com.garm.modules.exam.dto.library.LibraryDTO;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 试卷试题段落关联
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-09 09:14:42
 */
@ApiModel(value = "试卷试题段落关联")
public class PaperParagraphLibraryDTO extends LibraryDTO  {

	/**
	 * 类型 1.固定试题 2.随机试题
	 */
	@ApiModelProperty("类型 1.固定试题 2.随机试题")
	@NotNull(message = "请选择试题类型")
	private Integer libraryType;
	/**
	 * 排序
	 */
	@ApiModelProperty("排序")
	@NotNull(message = "排序不能为空")
	private Integer seq;
	/**
	 * 试题分数
	 */
	@ApiModelProperty("试题分数")
	@NotNull(message = "试题分数不能为空")
	private BigDecimal score;

	/**
	 * 子题数量
	 */
	@ApiModelProperty("子题数量")
	private String childNum;

	/**
	 * 修改时间
	 */
	@ApiModelProperty("修改时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	@ApiModelProperty("题眼类型名称")
	private String eyeTypeName;

	/**
	 * 标记，是否已填写
	 */
	@ApiModelProperty("标记，是否已填写,false为未填写，true为已填写")
	private boolean mark = false;

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getLibraryType() {
		return libraryType;
	}

	public void setLibraryType(Integer libraryType) {
		this.libraryType = libraryType;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public String getChildNum() {
		return childNum;
	}

	public void setChildNum(String childNum) {
		this.childNum = childNum;
	}

	public boolean isMark() {
		return mark;
	}

	public void setMark(boolean mark) {
		this.mark = mark;
	}

	public String getEyeTypeName() {
		return eyeTypeName;
	}

	public void setEyeTypeName(String eyeTypeName) {
		this.eyeTypeName = eyeTypeName;
	}
}
