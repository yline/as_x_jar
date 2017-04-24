package com.okhttp3.helper.interceptor;

import com.okhttp3.helper.cache.CacheManager;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

public class CacheThanNetInterceptor extends BaseInterceptor
{
	@Override
	public Response intercept(Chain chain) throws IOException
	{
		Request request = chain.request();
		preLog(chain, request);

		long time1 = System.nanoTime();
		Response cacheResponse = CacheManager.getInstance().get(request);
		if (null == cacheResponse)
		{
			nullLog("cacheResponse");
			Response response = chain.proceed(request);

			postLog(response, System.nanoTime() - time1);
			CacheManager.getInstance().write(response);
			return response;
		}
		else
		{
			postLog(cacheResponse, System.nanoTime() - time1);
			return cacheResponse;
		}
	}
}
