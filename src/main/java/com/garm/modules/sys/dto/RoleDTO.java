package com.garm.modules.sys.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色数据
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-07 13:41:50
 */
@Data
@ApiModel(value = "角色数据")
public class RoleDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("角色ID")
	private Long roleId;

	@ApiModelProperty("角色名称")
	private String roleName;

}
