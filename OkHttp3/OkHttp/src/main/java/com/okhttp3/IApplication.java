package com.okhttp3;

import com.okhttp3.helper.cache.CacheManager;
import com.yline.application.BaseApplication;
import com.yline.utils.FileUtil;

import java.io.File;

public class IApplication extends BaseApplication
{
	// 每天的大小
	private static final int max_size = 1024 * 1024 * 50;

	@Override
	public void onCreate()
	{
		super.onCreate();

		File topDir = FileUtil.getFileExternalDir(this, "text");
		CacheManager.getInstance().init(topDir, max_size);
	}
}
