package com.garm.common.utils.wechat;

import com.garm.common.utils.SRedisUtil;
import com.google.gson.Gson;

import javax.net.ssl.*;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 *
 * @Title: MD5Util.java
 * @Description: 微信H5支付工具类
 *
 * @author ruankegang
 * @date 2020年4月30日
 * @version V1.0
 */
public class MobileUtil {

	/**
	 * 获取用户openID
	 * @param app_id 微信公众号appid
	 * @param app_secret 微信公众号密钥
	 * @param code 前端传入随机码
	 * @return
	 */
	public static String getOpenId(String app_id,String app_secret,String code){
		if (code != null) {
			String url = "https://api.weixin.qq.com/sns/oauth2/access_token?"
					+ "appid="+ app_id
					+ "&secret="+ app_secret + "&code="
					+code + "&grant_type=authorization_code";
			String returnData = getReturnData(url);
			System.out.println(returnData);

			Gson gson = new Gson();
			OpenIdClass openIdClass = gson.fromJson(returnData,
					OpenIdClass.class);
			if (openIdClass.getOpenid() != null) {
				SRedisUtil.set(code,openIdClass.getOpenid(),60*60L);
				return openIdClass.getOpenid();
			}
		}
		return "**************";
	}
	public static String getReturnData(String urlString) {
		String res = "";
		try {
			URL url = new URL(urlString);
			java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url
					.openConnection();
			SSLSocketFactory oldSocketFactory = null;
			HostnameVerifier oldHostnameVerifier = null;
			boolean useHttps = urlString.startsWith("https");
			if (useHttps) {
				HttpsURLConnection https = (HttpsURLConnection) conn;
				// 方式一，相信所有
				oldSocketFactory = trustAllHosts(https);
				// 方式二，覆盖默认验证方法
				oldHostnameVerifier = https.getHostnameVerifier();
				// 方式三，不校验
				https.setHostnameVerifier(DO_NOT_VERIFY);
			}
			conn.connect();
			java.io.BufferedReader in = new java.io.BufferedReader(
					new java.io.InputStreamReader(conn.getInputStream(),
							"UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				res += line;
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 设置不验证主机
	 */
	private static final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

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
