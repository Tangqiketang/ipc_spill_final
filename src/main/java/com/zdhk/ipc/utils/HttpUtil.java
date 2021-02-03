package com.zdhk.ipc.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.util.Map;

/**
 * 
 */
@Slf4j
public class HttpUtil {
	
	/** 默认编码方式 **/
	public static final String DEFAULT_CHARSET = "UTF-8";
	/** 连接超时时间，由bean factory设置，缺省为8秒钟 */
	public static final int DEFAULT_CONNECTION_TIMEOUT = 10000;
	public static final int CONNECTION_REQUEST_TIMEOUT = 10000;
	
	/** 回应超时时间, 由bean factory设置，缺省为30秒钟 */
	public static final int DEFAULT_SO_TIMEOUT = 10000;
	/** 闲置连接超时时间, 由bean factory设置，缺省为60秒钟 */
	public static final int DEFAULT_IDLE_CONN_TIMEOUT = 60000;
	/** 默认最大连接时间 **/
	public static final int DEFAULT_MAX_CONN_PER_HOST = 30;
	/** 默认最大连接数 **/
	public static final int DEFAULT_MAX_TOTAL_CONN = 80;
	/** 默认等待HttpConnectionManager返回连接超时（只有在达到最大连接数时起作用）：1秒 */
	public static final long DEFAULT_HTTP_CONNECTION_MANAGER_TIMEOUT = 3 * 1000;

	/** HTTP GET method */
	public static final String REQUEST_METHOD_GET = "GET";

	/** HTTP POST method */
	public static final String REQUEST_METHOD_POST = "POST";
	
	/**
	 * httpClient的get请求方式2
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String doGet(String url, String charset) throws Exception {
		/*
		 * 使用 GetMethod 来访问一个 URL 对应的网页,实现步骤: 1:生成一个 HttpClinet 对象并设置相应的参数。
		 * 2:生成一个 GetMethod 对象并设置响应的参数。 3:用 HttpClinet 生成的对象来执行 GetMethod 生成的Get
		 * 方法。 4:处理响应状态码。 5:若响应正常，处理 HTTP 响应内容。 6:释放连接。
		 */
		/* 1 生成 HttpClinet 对象并设置参数 */
		HttpClient httpClient = new HttpClient();
		// 设置 Http 连接超时为5秒
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		/* 2 生成 GetMethod 对象并设置参数 */
		GetMethod getMethod = new GetMethod(url);
		// 设置 get 请求超时为 5 秒
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		// 设置请求重试处理，用的是默认的重试处理：请求三次
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		String response = "";
		/* 3 执行 HTTP GET 请求 */
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			/* 4 判断访问的状态码 */
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("请求出错: " + getMethod.getStatusLine());
			}
			/* 5 处理 HTTP 响应内容 */
			// HTTP响应头部信息，这里简单打印
			//Header[] headers = getMethod.getResponseHeaders();
			//for (Header h : headers)
				//System.out.println(h.getName() + "------------ " + h.getValue());
			// 读取 HTTP 响应内容，这里简单打印网页内容
			byte[] responseBody = getMethod.getResponseBody();// 读取为字节数组
			response = new String(responseBody, charset);
			// 读取为 InputStream，在网页内容数据量大时候推荐使用
			// InputStream response = getMethod.getResponseBodyAsStream();
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			e.printStackTrace();
		} catch (IOException e) {
			// 发生网络异常
			e.printStackTrace();
		} finally {
			/* 6 .释放连接 */
			getMethod.releaseConnection();
		}
		return response;
	}


	/**
	 * post请求
	 * @param url
	 * @param req
	 * @param header
	 * @return
	 */
	public static String doPost(String url, String req,Map<String,String> header) {
		
		HttpPost post = new HttpPost(url);
		for (Map.Entry<String, String> entry : header.entrySet()) { 
			post.addHeader(entry.getKey(),entry.getValue());
		}
		String response = null;
		try {
			StringEntity s = new StringEntity(req, "UTF-8");
			s.setContentEncoding("UTF-8");
			s.setContentType("application/json");// 发送json数据需要设置contentType
			post.setEntity(s);
			CloseableHttpClient client = HttpClients.createDefault();
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
					.setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT)
					.setSocketTimeout(DEFAULT_SO_TIMEOUT).build();
			post.setConfig(requestConfig);
			HttpResponse res = client.execute(post);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(res.getEntity());// 返回json格式：
				response = result;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return response;
	}

	public static String doPost(String url, String json) {

		HttpPost post = new HttpPost(url);
		String response = null;
		try {
			StringEntity s = new StringEntity(json.toString(), "UTF-8");
			s.setContentEncoding("UTF-8");
			s.setContentType("application/json");// 发送json数据需要设置contentType
			post.setEntity(s);
			CloseableHttpClient client = HttpClients.createDefault();
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
					.setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT)
					.setSocketTimeout(DEFAULT_SO_TIMEOUT).build();
			post.setConfig(requestConfig);
			HttpResponse res = client.execute(post);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(res.getEntity());// 返回json格式：
				response = result;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return response;
	}


	/**
	 * 异步post请求
	 * @param url
	 * @param req
	 * @param header
	 * @return
	 */
	public static String doPostAsync(String url, String req,Map<String,String> header) {

		HttpPost post = new HttpPost(url);
		for (Map.Entry<String, String> entry : header.entrySet()) { 
			post.addHeader(entry.getKey(),entry.getValue());
		}
		String response = null;
		try {
			StringEntity s = new StringEntity(req, "UTF-8");
			s.setContentEncoding("UTF-8");
			s.setContentType("application/json");// 发送json数据需要设置contentType
			post.setEntity(s);
			CloseableHttpClient client = HttpClients.createDefault();
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
					.setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT)
					.setSocketTimeout(DEFAULT_SO_TIMEOUT).build();
			post.setConfig(requestConfig);
			HttpResponse res = client.execute(post);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(res.getEntity());// 返回json格式：
				response = result;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return response;
	}


	/**
	 * Https方式调用
	 *
	 * @param requestUrl    请求地址
	 * @param data   请求参数
	 * @param header 请求头
	 * @return java.lang.String
	 */
	public static String doPostJsonSSL(String requestUrl, String data, Map<String, String> header) {
		String result = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT);
			httpUrlConn.setReadTimeout(3000);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(REQUEST_METHOD_POST);

			if ("GET".equalsIgnoreCase(REQUEST_METHOD_POST))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != data) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(data.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			result = buffer.toString();
		} catch (ConnectException ce) {
			log.info("ssl.request. server connection timed out.");
		} catch (Exception e) {
        	 /*StackTraceElement[] bbb = e.getStackTrace();
     		int len = bbb.length>10 ? 10 : bbb.length;
     		for (int i=0; i<len; i++) {
     			log.info(bbb[i].toString());
     		}*/
			log.info("Http.reqeust.exception");
		}
		return result;
	}
}
