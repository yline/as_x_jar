package com.okhttp3;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.okhttp3.helper.cache.CacheType;
import com.okhttp3.helper.interceptor.CacheAndNetInterceptor;
import com.okhttp3.request.SampleUtil;
import com.yline.log.LogFileUtil;
import com.yline.test.BaseTestActivity;
import com.yline.utils.FileUtil;
import com.yline.utils.IOUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;
import okhttp3.internal.cache.DiskLruCache;
import okhttp3.internal.io.FileSystem;
import okio.BufferedSink;
import okio.Okio;

public class MainActivity extends BaseTestActivity
{
	@Override
	protected void testStart(Bundle savedInstanceState)
	{
		addButton("标准Http请求", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Map<String, String> map = new HashMap<>();
				map.put("name", "yanzhenjie");
				map.put("pwd", "123");

				String httpUrl = "http://120.92.35.211/wanghong/wh/index.php/Api/ApiNews/new_tui";  // method cache
				// String httpUrl = "http://api.nohttp.net/cache";  // method cache
				SampleUtil.doGet(httpUrl, CacheType.DEFAULT, map);
			}
		});

		addButton("only_network", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Map<String, String> map = new HashMap<>();
				map.put("name", "yanzhenjie");
				map.put("pwd", "123");

				String httpUrl = "http://120.92.35.211/wanghong/wh/index.php/Api/ApiNews/new_tui";  // method cache
				// String httpUrl = "http://api.nohttp.net/cache";  // method cache
				SampleUtil.doGet(httpUrl, CacheType.ONLY_NET, map);
			}
		});

		addButton("only_cache", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Map<String, String> map = new HashMap<>();
				map.put("name", "yanzhenjie");
				map.put("pwd", "123");

				String httpUrl = "http://120.92.35.211/wanghong/wh/index.php/Api/ApiNews/new_tui";  // method cache
				// String httpUrl = "http://api.nohttp.net/cache";  // method cache
				SampleUtil.doGet(httpUrl, CacheType.ONLY_CACHE, map);
			}
		});

		addButton("cache_than_network", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Map<String, String> map = new HashMap<>();
				map.put("name", "yanzhenjie");
				map.put("pwd", "123");

				String httpUrl = "http://120.92.35.211/wanghong/wh/index.php/Api/ApiNews/new_tui";  // method cache
				// String httpUrl = "http://api.nohttp.net/cache";  // method cache
				SampleUtil.doGet(httpUrl, CacheType.CACHE_THAN_NET, map);
			}
		});

		addButton("network_than_cache", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Map<String, String> map = new HashMap<>();
				map.put("name", "yanzhenjie");
				map.put("pwd", "123");

				String httpUrl = "http://120.92.35.211/wanghong/wh/index.php/Api/ApiNews/new_tui";  // method cache
				// String httpUrl = "http://api.nohttp.net/cache";  // method cache
				SampleUtil.doGet(httpUrl, CacheType.NET_THAN_CACHE, map);
			}
		});

		addButton("both_cache_network", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Map<String, String> map = new HashMap<>();
				map.put("name", "yanzhenjie");
				map.put("pwd", "123");

				String httpUrl = "http://120.92.35.211/wanghong/wh/index.php/Api/ApiNews/new_tui";  // method cache
				// String httpUrl = "http://api.nohttp.net/cache";  // method cache
				SampleUtil.doGet(httpUrl, CacheType.CACHE_AND_NETWORK, map, new CacheAndNetInterceptor.OnCacheResponseCallback()
				{
					@Override
					public void onCacheResponse(Response cacheResponse)
					{
						SampleUtil.logResponse(cacheResponse);
					}
				});
			}
		});

		addButton("写入单个文件", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				try
				{
					File dir = FileUtil.getFileExternalDir(MainActivity.this, "yline20170422");

					DiskLruCache diskLruCache = DiskLruCache.create(FileSystem.SYSTEM, dir, 2001, 1, 50 * 1024 * 1024);

					DiskLruCache.Editor editor = diskLruCache.edit("ylinetext");

					//BufferedSink bufferedSink = Okio.buffer(editor.newSink(TextCache.ENTRY_METADATA));
					BufferedSink bufferedSink = Okio.buffer(editor.newSink(0));

					// bufferedSink.writeInt(7).writeByte('\n');
					bufferedSink.writeDecimalLong(123).writeByte('\n');
					bufferedSink.writeString("ddklsajfdklasjflkdsa", Charset.forName("utf-8"));
					/*editor.commit();

					if (null != editor)
					{
						editor.abortUnlessCommitted();
					}*/
					IOUtil.close(bufferedSink);
					editor.commit();

					/*if (null != editor)
					{
						editor.abort();
					}*/
					/*
					File dir = FileUtil.getFileExternalDir(MainActivity.this, "yline20170422");
					FileUtil.write(FileUtil.create(dir, "teststringda.0"), "123123123");*/
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	private void showMessageDialog(CharSequence title, CharSequence message)
	{
		LogFileUtil.v("dialog content = " + message);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("知道了", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		builder.show();
	}
}
