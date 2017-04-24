package com.okhttp3.helper.interceptor;

import okhttp3.Response;

public interface OnCacheResponseCallback
{
	/**
	 * 返回缓存，若缓存为空，则cacheResponse == null
	 *
	 * @param cacheResponse
	 */
	void onCacheResponse(Response cacheResponse);
}
