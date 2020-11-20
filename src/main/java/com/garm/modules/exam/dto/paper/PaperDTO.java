package com.garm.modules.exam.dto.paper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 试卷管理
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-09 09:14:42
 */
@ApiModel(value = "试卷管理")
public class PaperDTO  extends PaperBaseDTO{

	@ApiModelProperty("服务类型名称")
	private String serviceTypeName;

	/**
	 * 段落集合
	 */
	@ApiModelProperty("段落集合")
	private List<PaperParagraphDTO> paragraphDatas;

	/**
	 * 创建时间
	 */
	@ApiModelProperty("创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@ApiModelProperty("创建用户ID")
	private Long createUserId;
	/**
	 * 创建人
	 */
	@ApiModelProperty("创建人")
	private String createUser;

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

	public String getServiceTypeName() {
		return serviceTypeName;
	}

	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}

	public List<PaperParagraphDTO> getParagraphDatas() {
		return paragraphDatas;
	}

	public void setParagraphDatas(List<PaperParagraphDTO> paragraphDatas) {
		this.paragraphDatas = paragraphDatas;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
}
