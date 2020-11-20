package com.garm.modules.exam.dto.library;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 题库管理
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-08 10:42:37
 */
@ApiModel(value = "试题详情")
public class LibraryDetailDTO extends LibraryDTO {

	@ApiModelProperty("题眼类型名称")
	private String eyeTypeName;

	@ApiModelProperty("服务类型名称")
	private String serviceTypeName;

	@ApiModelProperty("创建时间,yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 修改时间
	 */
	@ApiModelProperty("修改时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * 标记，是否已填写
	 */
	@ApiModelProperty("标记，是否已填写,false为未填写，true为已填写")
	private boolean mark = false;

	public String getEyeTypeName() {
		return eyeTypeName;
	}

	public void setEyeTypeName(String eyeTypeName) {
		this.eyeTypeName = eyeTypeName;
	}

	public String getServiceTypeName() {
		return serviceTypeName;
	}

	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public boolean isMark() {
		return mark;
	}

	public void setMark(boolean mark) {
		this.mark = mark;
	}
}
