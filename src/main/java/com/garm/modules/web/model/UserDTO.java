

package com.garm.modules.web.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户
 *
 * @author
 */
@Data
@ApiModel("前台登录用户")
public class UserDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@ApiModelProperty("用户ID")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long userId;
	/**
	 * 用户账号
	 */
	@ApiModelProperty("用户账号")
	private String username;

	/**
	 * 用户名
	 */
	@ApiModelProperty("用户名")
	private String nickname;

	/**
	 * 用户类型：1.普通用户 2.付费用户 3.流失用户
	 */
	@ApiModelProperty("用户类型：1.普通用户 2.付费用户 3.流失用户")
	private Integer userType;
	/**
	 * 手机号
	 */
	@ApiModelProperty("手机号")
	private String mobile;

	/**
	 * 头像地址
	 */
	@ApiModelProperty("头像地址")
	private String imgUrl;

	/**
	 * 最后登录时间
	 */
	@ApiModelProperty("最后登录时间")
	private Date lastLoginTime;

}
