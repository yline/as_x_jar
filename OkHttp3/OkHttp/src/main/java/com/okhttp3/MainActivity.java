package com.okhttp3;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.okhttp3.helper.cache.CacheType;
import com.okhttp3.helper.interceptor.NetThanCacheInterceptor;
import com.okhttp3.helper.interceptor.OnCacheResponseCallback;
import com.okhttp3.request.SampleUtil;
import com.yline.log.LogFileUtil;
import com.yline.test.BaseTestActivity;
import com.yline.utils.FileUtil;
import com.yline.utils.IOUtil;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
		addButton("Get 标准Http请求", new View.OnClickListener()
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

		addButton("Get only_network", new View.OnClickListener()
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

		addButton("Get only_cache", new View.OnClickListener()
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

		addButton("Get cache_than_network", new View.OnClickListener()
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

		addButton("Get network_than_cache", new View.OnClickListener()
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

		addButton("Get both_cache_network", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Map<String, String> map = new HashMap<>();
				map.put("name", "yanzhenjie");
				map.put("pwd", "123");

				String httpUrl = "http://120.92.35.211/wanghong/wh/index.php/Api/ApiNews/new_tui";  // method cache
				// String httpUrl = "http://api.nohttp.net/cache";  // method cache
				SampleUtil.doGet(httpUrl, CacheType.CACHE_AND_NETWORK, map, new OnCacheResponseCallback()
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

		addButton("Post network_than_cache", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String httpUrl = "http://120.92.35.211/wanghong/wh/index.php/Api/ApiNews/news";
				SampleUtil.doPost(httpUrl, new Bean(0, 3), new NetThanCacheInterceptor());
			}
		});

		// 失败
		addButton("无损copy一个inputStream", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				try
				{
					// 创建原始数据
					String content = "BoyceZhang!";
					InputStream inputStream = new ByteArrayInputStream(content.getBytes());
					BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);


					// 开始复制读取
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

					int ch;
					int tempLength = 0, pastLength = 0;
					// 先实现一次
					bufferedInputStream.mark(24);
					ch = bufferedInputStream.read();
					outputStream.write(ch);
					bufferedInputStream.reset();

					// 最终 读取两个
					String originalString = IOUtil.toString(bufferedInputStream);
					LogFileUtil.v("originalString = " + originalString);
					/*String readedString = outputStream.toString();
					LogFileUtil.v("originalString = " + originalString + ", readedString = " + readedString);
*/
					/*int ch;
					boolean marked = false;
					while ((ch = bufferedInputStream.read()) != -1)
					{
						//读取一个字符输出一个字符
						LogFileUtil.v((char) ch + "");
						//读到 'e'的时候标记一下
						if (((char) ch == 'e') & !marked)
						{
							bufferedInputStream.mark(content.length());  //先不要理会mark的参数
							marked = true;
						}

						//读到'!'的时候重新回到标记位置开始读
						if ((char) ch == '!' && marked)
						{
							bufferedInputStream.reset();
							marked = false;
						}
					}*/
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	private class Bean
	{
		private int num1;

		private int length;

		public Bean(int num1, int length)
		{
			this.num1 = num1;
			this.length = length;
		}

		public int getNum1()
		{
			return num1;
		}

		public void setNum1(int num1)
		{
			this.num1 = num1;
		}

		public int getLength()
		{
			return length;
		}

		public void setLength(int length)
		{
			this.length = length;
		}
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
