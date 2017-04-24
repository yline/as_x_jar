package com.okhttp3.helper.cache;

import com.yline.log.LogFileUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.cache.DiskLruCache;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.io.FileSystem;
import okio.ByteString;

class TextCache
{
	private static final int VERSION = 201105;

	public static final int ENTRY_METADATA = 0;

	public static final int ENTRY_BODY = 1;

	public static final int ENTRY_COUNT = 2;

	final DiskLruCache diskLruCache;

	public TextCache(File dir, long maxSize)
	{
		this(dir, maxSize, FileSystem.SYSTEM);
	}

	public TextCache(File dir, long maxSize, FileSystem fileSystem)
	{
		this.diskLruCache = DiskLruCache.create(fileSystem, dir, VERSION, ENTRY_COUNT, maxSize);
	}

	/**
	 * 获取key（没有后缀）
	 *
	 * @param url
	 * @return
	 */
	public static String key(HttpUrl url)
	{
		return ByteString.encodeUtf8(url.toString()).md5().hex();
	}

	public Response get(Request request)
	{
		String key = key(request.url());
		DiskLruCache.Snapshot snapshot;
		TextCacheEntry textCacheEntry;
		try
		{
			snapshot = diskLruCache.get(key);
			if (snapshot == null)
			{
				return null;
			}
		} catch (IOException e)
		{
			return null;
		}

		try
		{
			textCacheEntry = new TextCacheEntry(snapshot.getSource(ENTRY_METADATA));
		} catch (IOException e)
		{
			Util.closeQuietly(snapshot);
			return null;
		}

		Response response = textCacheEntry.response(snapshot);

		boolean isMatches = textCacheEntry.matches(request, response);
		if (!isMatches)
		{
			Util.closeQuietly(response.body());
			return null;
		}

		return response;
	}

	public void put(Response response) throws IOException
	{
		String requestMethod = response.request().method();
		// 请求方式 过滤; 暂时不过滤

		if (HttpHeaders.hasVaryAll(response))
		{
			return;
		}

		TextCacheEntry textCacheEntry = new TextCacheEntry(response);
		DiskLruCache.Editor editor = null;

		// 写入第一个文件
		HttpUrl httpUrl = response.request().url();
		try
		{
			editor = diskLruCache.edit(key(httpUrl));
			LogFileUtil.v("editor = " + editor);
			if (null == editor)
			{
				return;
			}
			textCacheEntry.writeTo(editor, response);
		} catch (IOException e)
		{
			abortQuietly(editor);
			return;
		}
	}

	private void abortQuietly(DiskLruCache.Editor editor)
	{
		// Give up because the cache cannot be written.
		try
		{
			if (editor != null)
			{
				editor.abort();
			}
		} catch (IOException ignored)
		{
		}
	}
}
