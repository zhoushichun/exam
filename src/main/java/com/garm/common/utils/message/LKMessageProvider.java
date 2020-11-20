package com.garm.common.utils.message;

import com.alibaba.fastjson.JSONObject;
import com.garm.common.utils.JMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 凌凯短信发送
 * @className: LKMessageProvider
 * @description:TODO
 * @author: ruankegang
 * @date: 2019年12月27日 下午4:57:13
 */

public class LKMessageProvider extends AbstractMessageProvider{

	private static Logger log = LoggerFactory.getLogger(LKMessageProvider.class);

	@Override
	public JSONObject sendSMS(JMessage message) {
		JSONObject obj = new JSONObject();
		try {
			URL url = null;
			String send_content = URLEncoder.encode(
					message.getContent().replaceAll("<br/>", " "), "GBK");// 发送内容
			String str = "https://sdk2.028lk.com/sdk2/BatchSend2.aspx?CorpID="
					+ MessageConstant.id + "&Pwd=" + MessageConstant.pwd + "&Mobile=" + message.getPhone() + "&Content="
					+ send_content + "&Cell=&SendTime=" + "";
			url = new URL(str);

			BufferedReader in = null;
			int inputLine = 0;
			try {
				log.info("开始发送短信手机号码为 ：" + message.getPhone());
				log.info("开始发送短信内容为 ：" + message.getContent());
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				SSLSocketFactory oldSocketFactory = null;
				HostnameVerifier oldHostnameVerifier = null;
				boolean useHttps = str.startsWith("https");
				if (useHttps) {
					HttpsURLConnection https = (HttpsURLConnection) connection;
					// 方式一，相信所有
					oldSocketFactory = trustAllHosts(https);
					// 方式二，覆盖默认验证方法
					oldHostnameVerifier = https.getHostnameVerifier();
					// 方式三，不校验
					https.setHostnameVerifier(DO_NOT_VERIFY);
					in = new BufferedReader(new InputStreamReader(url.openStream()));
					inputLine = new Integer(in.readLine()).intValue();
				}
			} catch (Exception e) {
				inputLine = -2;
			}
			obj.put("code",inputLine);
			switch (inputLine){
				case -1:
					obj.put("msg","账号未注册");
					break;
				case -2:
					obj.put("msg","其他错误");
					break;
				case -3:
					obj.put("msg","帐号或密码错误");
					break;
				case -5:
					obj.put("msg","余额不足，请充值");
					break;
				case -6:
					obj.put("msg","定时发送时间不是有效的时间格式");
					break;
				case -7:
					obj.put("msg","提交信息末尾未加签名，请添加中文的企业签名【 】或内容乱码");
					break;
				case -8:
					obj.put("msg","发送内容需在1到300字之间");
					break;
				case -9:
					obj.put("msg","发送号码为空");
					break;
				case -10:
					obj.put("msg","定时时间不能小于系统当前时间");
					break;
				case -100:
					obj.put("msg","IP黑名单");
					break;
				case -102:
					obj.put("msg","账号黑名单");
					break;
				case -103:
					obj.put("msg","IP未导白");
					break;
				default:
					obj.put("code",0);
					obj.put("msg","发送成功");
					break;
			}


		} catch (MalformedURLException e) {
			obj.put("code",-11);
			obj.put("msg","url编码失败");
		} catch (UnsupportedEncodingException e) {
			obj.put("code",-11);
			obj.put("msg","编码失败");
		}
		return obj;
	}


	/**
	 * 覆盖java默认的证书验证
	 */
	private static final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return new java.security.cert.X509Certificate[]{};
		}

		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}
	}};

	/**
	 * 设置不验证主机
	 */
	private static final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	/**
	 * 信任所有
	 * @param connection
	 * @return
	 */
	private static SSLSocketFactory trustAllHosts(HttpsURLConnection connection) {
		SSLSocketFactory oldFactory = connection.getSSLSocketFactory();
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			SSLSocketFactory newFactory = sc.getSocketFactory();
			connection.setSSLSocketFactory(newFactory);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oldFactory;
	}
}
