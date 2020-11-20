package com.garm.common.utils.wechat;

import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * 微信支付 - 签名类
 * @author 阮克刚
 * @date 2020年4月30日
 */
public class Sign {
	/**
	 * 微信支付签名算法sign
	 *
	 * @param characterEncoding
	 * @param parameters
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String createSign(String characterEncoding,
			SortedMap<Object, Object> parameters, String key) {
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k)
					&& !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + key);

		String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding)
				.toUpperCase();
		return sign;
	}

}
