package com.okhttp3.helper.interceptor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.yline.log.LogFileUtil;

import java.util.Arrays;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 注意：若无网络，则chain.proceed(request)之后就不会执行
 */
public abstract class BaseInterceptor implements Interceptor
{
	protected void preLog(Chain chain, Request request)
	{
		if (isDebug())
		{
			LogFileUtil.v("debug", String.format("Network request %s on %s%n%s", request.url(), chain.connection(), request.headers()), LogFileUtil.LOG_LOCATION_PARENT);
		}
	}

	protected void postLog(Response response, long milliTime)
	{
		if (isDebug())
		{
			LogFileUtil.v("debug", String.format("Network response %s in %.1fms%n%s", response.request().url(), milliTime / 1e6d, response.headers()), LogFileUtil.LOG_LOCATION_PARENT);
		}
	}

	protected void nullLog(String... strings)
	{
		if (isDebug())
		{
			LogFileUtil.v("debug", Arrays.toString(strings) + "is null", LogFileUtil.LOG_LOCATION_PARENT);
		}
	}

	protected void hintLog(String... strings)
	{
		if (isDebug())
		{
			LogFileUtil.v("debug", Arrays.toString(strings), LogFileUtil.LOG_LOCATION_PARENT);
		}
	}

	protected boolean isNetConnected(Context context)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (null == connectivityManager)
		{
			return false;
		}

		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (null == networkInfo)
		{
			return false;
		}

		int type = networkInfo.getType();
		if (type == -1)
		{
			return false;
		}

		return true;
	}

	protected boolean isDebug()
	{
		return true;
	}
}
