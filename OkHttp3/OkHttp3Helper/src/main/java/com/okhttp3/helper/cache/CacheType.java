package com.okhttp3.helper.cache;

public enum CacheType
{
	/**
	 * 原模原样
	 */
	DEFAULT,

	/**
	 * 只要有缓存，就读取缓存；读取缓存失败504错误
	 */
	ONLY_CACHE,

	/**
	 * 不管有没有缓存，只读取网络；网络失败不考虑
	 */
	ONLY_NET,

	/**
	 * 有缓存，则读取缓存；读取缓存失败，则读取网络
	 * 无缓存，则读取网络；网络错误，则失败
	 */
	CACHE_THAN_NET,

	/**
	 * 有网络，则读取网络；网络失败不考虑
	 * 无网络，则读取缓存；读取缓存失败504错误
	 */
	NET_THAN_CACHE,

	/**
	 * 先读取缓存；有缓存，则返回缓存，无缓存，则返回空
	 * 再读取网络；有网络，则返回网络，无网络，则返回空
	 */
	CACHE_AND_NETWORK;
}
