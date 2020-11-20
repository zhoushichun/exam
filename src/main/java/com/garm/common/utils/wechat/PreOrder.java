package com.garm.common.utils.wechat;

import lombok.Data;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 统一下单
 * @author 阮克刚
 * @date 2020年4月30日
 */
@Data
public class PreOrder {
	/**
	 * 公众账号ID
	 */
	private String appid;
	/**
	 * 商品描述
	 */
	private String body;
	/**
	 * 商户号
	 */
	private String mch_id;
	/**
	 * 随机字符串
	 */
	private String nonce_str;
	/**
	 * 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
	 */
	private String notify_url;
	/**
	 * 商户订单号
	 */
	private String out_trade_no;
	/**
	 * APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
	 */
	private String spbill_create_ip;
	/**
	 * 订单总金额，单位为分
	 */
	private int total_fee;
	/**
	 * 取值如下：JSAPI，NATIVE，APP
	 */
	private String trade_type;
	/**
	 * 签名
	 */
	private String sign;

	/**
	 * 用户openId
	 * @return
	 */
	private String openid;

	public SortedMap<Object, Object> beanToMap(){
		SortedMap<Object, Object> map = new TreeMap<Object, Object>();
		if(this.appid != null){
			map.put("appid",this.appid);
		}
		if(this.body != null){
			map.put("body",this.body);
		}
		if(this.mch_id != null){
			map.put("mch_id",this.mch_id);
		}
		if(this.nonce_str != null){
			map.put("nonce_str",this.nonce_str);
		}
		if(this.notify_url != null){
			map.put("notify_url",this.notify_url);
		}
		if(this.out_trade_no != null){
			map.put("out_trade_no",this.out_trade_no);
		}
		if(this.openid != null){
			map.put("openid",this.openid);
		}
		if(this.spbill_create_ip != null){
			map.put("spbill_create_ip",this.spbill_create_ip);
		}
		if(this.total_fee != 0){
			map.put("total_fee",this.total_fee);
		}
		if(this.trade_type != null){
			map.put("trade_type",this.trade_type);
		}
		if(this.sign != null){
			map.put("sign",this.sign);
		}

		return map;
	}
}
