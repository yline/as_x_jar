package com.okhttp3.helper.cache;

import android.text.TextUtils;

import com.yline.log.LogFileUtil;
import com.yline.utils.IOUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.cache.DiskLruCache;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.io.FileSystem;
import okio.Buffer;
import okio.BufferedSink;
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

	public Response get(Request request)
	{
		DiskLruCache.Snapshot snapshot = null;
		TextCacheEntry textCacheEntry = null;
		try
		{
			String key = key(request);
			if (TextUtils.isEmpty(key))
			{
				return null;
			}

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

		Response response = textCacheEntry.response(request.body(), snapshot);

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
		// 过滤头部，不符合vary的字符“*”
		if (HttpHeaders.hasVaryAll(response))
		{
			LogFileUtil.e("TextCache", "hasVaryAll is false");
			return;
		}

		// 过滤 请求方式，仅限get + post(json)
		String key = key(response.request());
		if (TextUtils.isEmpty(key))
		{
			return;
		}

		DiskLruCache.Editor editor = null;
		try
		{
			editor = diskLruCache.edit(key);
			TextCacheEntry textCacheEntry = new TextCacheEntry(response);
			if (null == editor)
			{
				return;
			}
			textCacheEntry.writeTo(editor, response);
		} catch (IOException e)
		{
			abortQuietly(editor);
			LogFileUtil.e("textCache", "put IOException", e);
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

	/**
	 * 过滤请求方式
	 *
	 * @param request
	 * @return
	 * @throws IOException
	 */
	private String key(Request request) throws IOException
	{
		String key = null;
		String method = request.method();
		if (method == "GET")
		{
			key = ByteString.encodeUtf8(request.url().toString()).md5().hex();
			LogFileUtil.v("method = GET, key = " + key + ", url = " + request.url().toString() + ", result = null");
			return key;
		}
		else if (method == "POST" && CacheManager.DEFAULT_MEDIA_TYPE.equals(request.body().contentType()))
		{
			RequestBody requestBody = request.body();
			BufferedSink sink = new Buffer();
			requestBody.writeTo(sink);
			InputStream inputStream = sink.buffer().inputStream();
			String result = IOUtil.toString(inputStream);

			key = ByteString.encodeUtf8(request.url().toString() + result).md5().hex();
			LogFileUtil.v("method = POST, key = " + key + ", url = " + request.url().toString() + ", result = " + result);
			return key;
		}
		else
		{
			LogFileUtil.v("method = " + method + ", key = " + key + ", url = " + request.url().toString() + ", result = null");
		}
		return key;
	}
}
