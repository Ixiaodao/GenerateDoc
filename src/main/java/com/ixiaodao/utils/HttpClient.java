package com.ixiaodao.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.ui.Messages;
import com.ixiaodao.model.Interface;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 1
 * @author jinwenbiao
 * @since 2022/3/31 18:10
 */
public class HttpClient {
	public static String sendPost(String url, Interface interfaceVO) {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		HttpPost httpPost = new HttpPost(url);

		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create() ;
		String s = gson.toJson(interfaceVO);
		StringEntity entity = new StringEntity(s, "UTF-8");

		httpPost.setEntity(entity);

		httpPost.setHeader("Content-Type", "application/json;charset=utf8");

		// 响应模型
		CloseableHttpResponse response = null;
		try {
			// 由客户端执行(发送)Post请求
			response = httpClient.execute(httpPost);

			if (response == null) {
				throw new RuntimeException("响应内容为空");
			}
			// 从响应模型中获取响应实体
			HttpEntity responseEntity = response.getEntity();
			if (response.getStatusLine().getStatusCode() ==200 && responseEntity != null ) {
				String msg = EntityUtils.toString(responseEntity);
				if (!"success".equals(msg)) {
					throw new RuntimeException(msg);
				}
				return msg;
			} else {
				throw new RuntimeException("调用返回状态非200");
			}
		} catch (ParseException | IOException e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				// 释放资源
				if (httpClient != null) {
					httpClient.close();
				}
				if (response != null) {
					response.close();
				}
			} catch (IOException ignored) {
			}
		}
	}
}
