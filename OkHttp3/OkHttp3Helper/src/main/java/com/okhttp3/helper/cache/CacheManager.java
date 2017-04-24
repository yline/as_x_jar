package com.okhttp3.helper.cache;

import com.yline.log.LogFileUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

public class CacheManager
{
	private CacheManager()
	{
	}

	public static CacheManager getInstance()
	{
		return CacheManagerHolder.sInstance;
	}

	private static class CacheManagerHolder
	{
		private static CacheManager sInstance = new CacheManager();
	}

	private TextCache textCache;

	public void init(File dir, int maxSize)
	{
		textCache = new TextCache(dir, maxSize);
	}

	/**
	 * 写入缓存数据
	 *
	 * @param response
	 */
	public void write(Response response)
	{
		try
		{
			textCache.put(response);
		} catch (IOException e)
		{
			LogFileUtil.e("", "write Cache Failed", e);
		}
	}

	/**
	 * 获取缓存 结果
	 *
	 * @param request
	 * @return
	 */
	public Response get(Request request)
	{
		return textCache.get(request);
	}
}
