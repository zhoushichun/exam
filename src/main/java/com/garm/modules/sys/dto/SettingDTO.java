package com.garm.modules.sys.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统变量统一返回数据格式
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-07 13:41:50
 */
@Data
@ApiModel(value = "系统变量统一返回数据")
public class SettingDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * id
	 */
	@ApiModelProperty("主键id")
	private Long dictItemId;
	/**
	 * 类型ID
	 */
	@ApiModelProperty("类型id,1-服务设置；2-题眼设置；3-费用设置；4-答题时长")
	private Long dictId;
	/**
	 * 名称
	 */
	@ApiModelProperty("名称")
	private String itemName;
	/**
	 * 状态 1:禁用，2:正常
	 */
	@ApiModelProperty("状态 1:禁用，2:正常")
	private Integer status;
	/**
	 * 备注
	 */
	@ApiModelProperty("备注")
	private String remarks;
	/**
	 * 键值
	 */
	@ApiModelProperty("键值")
	private String value;

}
