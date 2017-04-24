package com.okhttp3.helper.interceptor;

import com.okhttp3.helper.cache.CacheManager;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * 1，不读取缓存
 * 2，缓存数据
 *
 * @author yline 2017/4/21 -- 16:22
 * @version 1.0.0
 */
public class OnlyNetInterceptor extends BaseInterceptor
{
	@Override
	public Response intercept(Chain chain) throws IOException
	{
		Request request = chain.request();
		preLog(chain, request);
		
		long time1 = System.nanoTime();
		Response response = chain.proceed(request);

		postLog(response, System.nanoTime() - time1);
		CacheManager.getInstance().write(response);

		return response;
	}
}
