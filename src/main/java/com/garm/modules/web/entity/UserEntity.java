

package com.garm.modules.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;


/**
 * 用户
 *
 * @author
 */
@Data
@TableName("tb_user")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@TableId(type = IdType.INPUT)
	private Long userId;
	/**
	 * 用户账号
	 */
	private String username;

	/**
	 * 用户名
	 */
	@NotBlank(message = "请填写用户名")
	@Size(min = 3,max = 50,message = "用户名为3-50个字符")
	private String nickname;

	/**
	 * 用户类型：1.普通用户 2.付费用户 3.流失用户
	 */
	private Integer userType;
	/**
	 * 手机号
	 */
	@NotBlank(message = "请填写电话号码")
	@Pattern(regexp = "^1[3456789]\\d{9}$",message = "请输入正确的手机号码")
	private String mobile;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 盐
	 */
	private String salt;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 逻辑删除;1-已删除，2-正常
	 */
	private Integer isDel;

}
