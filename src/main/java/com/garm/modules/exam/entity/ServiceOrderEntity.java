package com.garm.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 服务订单
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-11 16:39:39
 */
@Data
@TableName("exam_service_order")
public class ServiceOrderEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 订单id
	 */
	@TableId(type = IdType.INPUT)
	private Long orderId;
	/**
	 * 服务类型id，来自码表
	 */
	private Long serviceType;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 充值类型 1-月支付 2-学期支付
	 */
	private Integer type;
	/**
	 * 支付金额
	 */
	private BigDecimal fee;
	/**
	 * 支付方式 1-微信 2-支付宝
	 */
	private Integer payType;
	/**
	 * 订单号（微信和支付包都有订单号）
	 */
	private String orderNo;
	/**
	 * 过期时间
	 */
	private Date expireTime;
	/**
	 * 支付时间
	 */
	private Date payTime;
	/**
	 * 是否支付 1-未支付 2-已支付
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private Date createTime;

}
