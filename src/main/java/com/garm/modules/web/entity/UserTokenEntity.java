package com.garm.modules.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 系统用户Token
 *
 * @author liwt
 * @email liwentao@qq.com
 * @date 2020-07-13 21:21:58
 */
@Data
@TableName("tb_user_token")
public class UserTokenEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId(type = IdType.INPUT)
	private Long userId;
	/**
	 * token
	 */
	private String token;
	/**
	 * 过期时间
	 */
	private Date expireTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;

}
