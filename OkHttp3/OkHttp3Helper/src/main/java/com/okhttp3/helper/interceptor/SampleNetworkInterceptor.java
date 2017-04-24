package com.okhttp3.helper.interceptor;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

public class SampleNetworkInterceptor extends BaseInterceptor
{
	@Override
	public Response intercept(Chain chain) throws IOException
	{
		Request request = chain.request();
		preLog(chain, request);

		long time1 = System.nanoTime();
		Response response = chain.proceed(request);

		postLog(response, System.nanoTime() - time1);

		return response;
	}
}
