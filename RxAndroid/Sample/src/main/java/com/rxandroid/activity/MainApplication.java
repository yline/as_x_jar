package com.rxandroid.activity;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

/**
 * Created by yline on 2016/11/15.
 */
public class MainApplication extends BaseApplication
{
	public static final String TAG = "RxAndroidSample";

	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath(TAG);
		return sdkConfig;
	}
}
