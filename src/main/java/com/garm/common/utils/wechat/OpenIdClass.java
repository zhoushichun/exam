package com.garm.common.utils.wechat;

import lombok.Data;

/**
 * 微信用户信息
 * @author 阮克刚
 * @date 2020年4月30日
 */
@Data
public class OpenIdClass {
	private String access_token;
	private String expires_in;
	private String refresh_token;
	private String openid;
	private String scope;
	private String unionid;

	private String errcode;
	private String errmsg;
}
