package com.garm.common.utils.message;


import com.alibaba.fastjson.JSONObject;
import com.garm.common.utils.JMessage;

/**
 * 短信提供类
 * @className: MessageProvider
 * @description:TODO
 * @author: ruankegang
 * @date: 2019年12月27日 下午4:48:13
 */
public interface MessageProvider {

	/**
	 * 发送验证码
	 * @title: sendSMS
	 * @description:TODO
	 * @author: ruankegang
	 * @param message
	 * @return
	 * @date：2019年12月27日下午6:43:33
	 */
	JSONObject sendSMS(JMessage message);
}
