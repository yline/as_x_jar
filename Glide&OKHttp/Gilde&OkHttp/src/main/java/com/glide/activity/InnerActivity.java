package com.glide.activity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.yline.log.LogFileUtil;
import com.yline.test.BaseTestActivity;
import com.yline.utils.FileSizeUtil;

public class InnerActivity extends BaseTestActivity
{
	@Override
	protected void testStart(final Bundle savedInstanceState)
	{
		addButton("使用Glide", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				SampleSingleActivity.actionStart(InnerActivity.this);
			}
		});

		addButton("查看缓存大小", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				long size = FileSizeUtil.getDirSize(InnerActivity.this.getExternalFilesDir("GlidePicture"));
				LogFileUtil.v("DirSize = " + size);
			}
		});

		addButton("清除内存缓存", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Glide.get(InnerActivity.this).clearMemory(); // 清理内存缓存  只能在UI主线程中进行
			}
		});

		addButton("清除磁盘缓存", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						Glide.get(InnerActivity.this).clearDiskCache();
					}
				}).start();
			}
		});
	}
}
