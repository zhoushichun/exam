package com.garm.common.utils.wechat;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 *
 * @Title: HttpUtil.java
 * @Description: HTTP
 *
 * @author ruankegang
 * @date 2020年4月30日
 * @version V1.0
 */
public class HttpUtil {

	/**
	 * POST请求
	 *
	 * @param url
	 * @param outStr
	 * @return
	 */
	public static String doPostStr(String url, String outStr) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			SSLSocketFactory oldSocketFactory = null;
			HostnameVerifier oldHostnameVerifier = null;
			boolean useHttps = url.startsWith("https");
			if (useHttps) {
				HttpsURLConnection https = (HttpsURLConnection) conn;
				// 方式一，相信所有
				oldSocketFactory = trustAllHosts(https);
				// 方式二，覆盖默认验证方法
				oldHostnameVerifier = https.getHostnameVerifier();
				// 方式三，不校验
				https.setHostnameVerifier(DO_NOT_VERIFY);
			}

			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(outStr);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			System.out.println(result);
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 *
	 * @param url 		发送请求的 URL
	 * @param param 	请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		Writer out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();

			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			SSLSocketFactory oldSocketFactory = null;
			HostnameVerifier oldHostnameVerifier = null;
			boolean useHttps = url.startsWith("https");
			if (useHttps) {
				HttpsURLConnection https = (HttpsURLConnection) conn;
				// 方式一，相信所有
				oldSocketFactory = trustAllHosts(https);
				// 方式二，覆盖默认验证方法
				oldHostnameVerifier = https.getHostnameVerifier();
				// 方式三，不校验
				https.setHostnameVerifier(DO_NOT_VERIFY);
			}
			// 获取URLConnection对象对应的输出流
			out = new BufferedWriter(new OutputStreamWriter(
					conn.getOutputStream(),"UTF-8"));
			// 发送请求参数
			out.write(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
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
