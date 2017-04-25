package com.okhttp3.helper.interceptor;

import com.okhttp3.helper.cache.CacheManager;
import com.yline.application.SDKManager;

import java.io.IOException;

import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;

public class NetThanCacheInterceptor extends BaseInterceptor
{
	@Override
	public Response intercept(Chain chain) throws IOException
	{
		Request request = chain.request();
		preLog(chain, request);

		long time1 = System.nanoTime();
		boolean isNetWorkable = isNetConnected(SDKManager.getApplication());
		hintLog("isNetWorkable = " + isNetWorkable);
		if (isNetWorkable)
		{
			Response response = chain.proceed(request);

			postLog(response, System.nanoTime() - time1);
			CacheManager.getInstance().write(response);
			Response resultResponse = CacheManager.getInstance().get(request);
			return resultResponse;
		}
		else
		{
			Response cacheResponse = CacheManager.getInstance().get(request);
			if (null == cacheResponse)
			{
				nullLog("cacheResponse");
				return new Response.Builder()
						.request(request)
						.protocol(Protocol.HTTP_1_1)
						.code(504)
						.message("Unsatisfiable Request (cache is null)")
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
}
