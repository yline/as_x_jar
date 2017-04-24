package com.okhttp3.helper.interceptor;

import com.okhttp3.helper.cache.CacheManager;

import java.io.IOException;

import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;

/**
 * 1，有缓存，返回缓存
 * 2，无缓存，返回 504 错误
 *
 * @author yline 2017/4/22 -- 16:48
 * @version 1.0.0
 */
public class OnlyCacheInterceptor extends BaseInterceptor
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
			return new Response.Builder()
					.request(request)
					.protocol(Protocol.HTTP_1_1)
					.code(504)
					.message("Unsatisfiable Request (only-if-cached)")
					.body(Util.EMPTY_RESPONSE)
					.sentRequestAtMillis(-1L)
					.receivedResponseAtMillis(System.currentTimeMillis())
					.build();
		}
		else
		{
			postLog(cacheResponse, System.nanoTime() - time1);
			return cacheResponse;
		}
	}
}
