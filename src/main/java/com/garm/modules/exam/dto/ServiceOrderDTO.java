package com.garm.modules.exam.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 财务管理
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-11 16:39:39
 */
@Data
@ApiModel(value = "财务管理")
public class ServiceOrderDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 用户id
	 */
	@ApiModelProperty("用户id")
	private Long userId;
	/**
	 * 用户名
	 */
	@ApiModelProperty("用户名")
	private String nickname;
	/**
	 * 用户账号
	 */
	@ApiModelProperty("用户账号")
	private String userName;

	@ApiModelProperty("购买的服务类型id")
	private Long serviceType;

	@ApiModelProperty("购买的服务类型名称")
	private String serviceTypeName;
	/**
	 * 充值类型 1-月支付 2-学期支付
	 */
	@ApiModelProperty("充值类型 1-月支付 2-学期支付")
	private Integer type;
	/**
	 * 支付金额
	 */
	@ApiModelProperty("支付金额")
	private BigDecimal fee;
	/**
	 * 支付方式 1-微信 2-支付宝
	 */
	@ApiModelProperty("支付方式 1-微信 2-支付宝")
	private Integer payType;
	/**
	 * 支付时间
	 */
	@ApiModelProperty("支付时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date payTime;

}
