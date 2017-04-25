package com.okhttp3.request;

import com.google.gson.Gson;
import com.okhttp3.helper.cache.CacheManager;
import com.okhttp3.helper.cache.CacheType;
import com.okhttp3.helper.interceptor.CacheAndNetInterceptor;
import com.okhttp3.helper.interceptor.CacheThanNetInterceptor;
import com.okhttp3.helper.interceptor.NetThanCacheInterceptor;
import com.okhttp3.helper.interceptor.OnCacheResponseCallback;
import com.okhttp3.helper.interceptor.OnlyCacheInterceptor;
import com.okhttp3.helper.interceptor.OnlyNetInterceptor;
import com.yline.application.SDKManager;
import com.yline.log.LogFileUtil;
import com.yline.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 暂时做个网络请求
 *
 * @author yline 2017/4/21 -- 12:54
 * @version 1.0.0
 */
public class SampleUtil
{
	private static final int DEFAULT_CACHE_SIZE = 512 * 1024 * 1024;

	public static void doGet(String httpUrl, CacheType cacheType, Map<String, String> map)
	{
		doGet(httpUrl, cacheType, map, null);
	}

	public static void doGet(String httpUrl, CacheType cacheType, Map<String, String> map, OnCacheResponseCallback callback)
	{
		if (cacheType == CacheType.ONLY_NET)
		{
			doGet(httpUrl, map, new OnlyNetInterceptor());
		}
		else if (cacheType == CacheType.ONLY_CACHE)
		{
			doGet(httpUrl, map, new OnlyCacheInterceptor());
		}
		else if (cacheType == CacheType.CACHE_THAN_NET)
		{
			doGet(httpUrl, map, new CacheThanNetInterceptor());
		}
		else if (cacheType == CacheType.NET_THAN_CACHE)
		{
			doGet(httpUrl, map, new NetThanCacheInterceptor());
		}
		else if (cacheType == CacheType.CACHE_AND_NETWORK)
		{
			doGet(httpUrl, map, new CacheAndNetInterceptor(callback));
		}
		else
		{
			LogFileUtil.v("cacheType is null and httpUrl = " + httpUrl);
			doGet(httpUrl, map, null);
		}
	}

	private static void doGet(String httpUrl, Map<String, String> map, Interceptor interceptor)
	{
		// HttpClient
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		// 设置缓存
		File cacheDir = FileUtil.getFileExternalCacheDir(SDKManager.getApplication());
		final Cache cache = new Cache(cacheDir, DEFAULT_CACHE_SIZE);
		builder.cache(cache);
		// 设置超时
		builder.connectTimeout(10, TimeUnit.SECONDS)
				.readTimeout(10, TimeUnit.SECONDS)
				.writeTimeout(10, TimeUnit.SECONDS);
		if (null != interceptor)
		{
			builder.addInterceptor(interceptor);
		}
		OkHttpClient okHttpClient = builder.build();

		// Request
		Request.Builder requestBuilder = new Request.Builder();
		if (null != map)
		{
			StringBuffer buffer = new StringBuffer(httpUrl);
			if (null != map)
			{
				buffer.append("?");
				for (String key : map.keySet())
				{
					buffer.append(key);
					buffer.append("=");
					buffer.append(map.get(key));
					buffer.append("&");
				}
				buffer.deleteCharAt(buffer.length() - 1);
			}
			httpUrl = buffer.toString();
		}
		requestBuilder.url(httpUrl);

		CacheControl.Builder cacheBuilder = new CacheControl.Builder();
		// cacheBuilder.onlyIfCached(); // 表明不进行网络请求，且缓存不存在或者过期，一定会返回503错误
		cacheBuilder.maxAge(2, TimeUnit.SECONDS);
		requestBuilder.cacheControl(cacheBuilder.build());
		Request request = requestBuilder.build();

		// 请求
		okHttpClient.newCall(request).enqueue(new Callback()
		{
			@Override
			public void onFailure(Call call, IOException e)
			{
				LogFileUtil.e("", "onFailure", e);
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException
			{
				logResponse(response);
			}
		});
	}

	public static void doPost(String httpUrl, Object param, Interceptor interceptor)
	{
		// HttpClient
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		// 设置缓存
		File cacheDir = FileUtil.getFileExternalCacheDir(SDKManager.getApplication());
		final Cache cache = new Cache(cacheDir, DEFAULT_CACHE_SIZE);
		builder.cache(cache);
		// 设置超时
		builder.connectTimeout(10, TimeUnit.SECONDS)
				.readTimeout(10, TimeUnit.SECONDS)
				.writeTimeout(10, TimeUnit.SECONDS);
		if (null != interceptor)
		{
			builder.addInterceptor(interceptor);
		}
		OkHttpClient okHttpClient = builder.build();

		// Request
		Request.Builder requestBuilder = new Request.Builder();
		requestBuilder.post(RequestBody.create(CacheManager.DEFAULT_MEDIA_TYPE, new Gson().toJson(param)));
		requestBuilder.url(httpUrl);

		CacheControl.Builder cacheBuilder = new CacheControl.Builder();
		// cacheBuilder.onlyIfCached(); // 表明不进行网络请求，且缓存不存在或者过期，一定会返回503错误
		cacheBuilder.maxAge(2, TimeUnit.SECONDS);
		requestBuilder.cacheControl(cacheBuilder.build());
		Request request = requestBuilder.build();
		
		// 请求
		okHttpClient.newCall(request).enqueue(new Callback()
		{
			@Override
			public void onFailure(Call call, IOException e)
			{
				LogFileUtil.e("", "onFailure", e);
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException
			{
				logResponse(response);
			}
		});
	}

	public static void logResponse(Response response)
	{
		LogFileUtil.v("response.code() = " + response.code());

		try
		{
			LogFileUtil.v("onResponse cacheResponse = " + response.cacheResponse().body().string());
		} catch (Exception e)
		{
			LogFileUtil.e("onResponse", "cacheResponse is null");
		}

		try
		{
			LogFileUtil.v("onResponse body = " + response.body().string());
		} catch (Exception e)
		{
			LogFileUtil.e("onResponse", "body is null");
		}

		try
		{
			LogFileUtil.v("onResponse message = " + response.message());
		} catch (Exception e)
		{
			LogFileUtil.e("onResponse", "message is null");
		}

		try
		{
			LogFileUtil.v("onResponse headers = " + response.headers().toString());
		} catch (Exception e)
		{
			LogFileUtil.e("onResponse", "headers is null");
		}
	}
}
