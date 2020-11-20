package com.garm.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户服务VIP信息
 * 
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-11 14:59:42
 */
@Data
@TableName("sys_user_service")
public class SysUserServiceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 服务id
	 */
	@TableId
	private Long serviceId;
	/**
	 * 用户id(一对多)
	 */
	private Long userId;
	/**
	 * 服务类型id，来自码表
	 */
	private Long serviceType;
	/**
	 * 充值类型 1-月支付 2-学期支付
	 */
	private Integer type;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 过期时间
	 */
	private Date expireTime;
	/**
	 * 是否有效 1-无效 2-有效
	 */
	private Integer status;
	/**
	 * 修改时间
	 */
	private Date updateTime;
	/**
	 * 订单id（订单支付成功后，用户变成vip，同时可以获取到订单id）
	 */
	private Long orderId;

}
