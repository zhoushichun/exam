package com.garm.common.utils.wechat;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

import java.io.InputStream;

/**
 * 微信支付 - xml管理类
 * @author 阮克刚
 * @date 2020年4月30日
 */
public class XmlUtil {


	/**
	 *
	 * @Description: xml字符串转换为对象
	 * @param inputXml
	 * @param type
	 * @return
	 * @throws Exception
	 *
	 * @author tiankong
	 * @date 2017年8月31日 下午4:52:13
	 */
	public static Object xml2Object(String inputXml, Class<?> type) throws Exception {
		if (null == inputXml || "".equals(inputXml)) {
			return null;
		}
		XStream xstream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
		xstream.alias("xml", type);
		return xstream.fromXML(inputXml);
	}

	/**
	 *
	 * @Description: 从inputStream中读取对象
	 * @param inputStream
	 * @param type
	 * @return
	 * @throws Exception
	 *
	 * @author leechenxiang
	 * @date 2017年8月31日 下午4:52:29
	 */
	public static Object xml2Object(InputStream inputStream, Class<?> type) throws Exception {
		if (null == inputStream) {
			return null;
		}
		XStream xstream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
		xstream.alias("xml", type);
		return xstream.fromXML(inputStream, type);
	}

	/**
	 *
	 * @Description: 对象转换为xml字符串
	 * @param ro
	 * @param types
	 * @return
	 * @throws Exception
	 *
	 */
	public static String object2Xml(Object ro, Class<?> types) throws Exception {
		if (null == ro) {
			return null;
		}
		XStream xstream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
		xstream.alias("xml", types);
		return xstream.toXML(ro);
	}

}
