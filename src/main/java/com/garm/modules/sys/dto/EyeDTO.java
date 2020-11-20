package com.garm.modules.sys.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 题眼数据
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-07 13:41:50
 */
@Data
@ApiModel(value = "题眼数据")
public class EyeDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("题眼类型ID")
	private Long eyeType;

	@ApiModelProperty("题眼类型名称")
	private String eyeName;

}
