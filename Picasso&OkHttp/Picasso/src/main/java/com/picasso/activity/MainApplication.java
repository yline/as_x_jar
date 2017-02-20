package com.picasso.activity;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

/**
 * @author yline 2017/2/20 --> 13:12
 * @version 1.0.0
 */
public class MainApplication extends BaseApplication
{
	public static final String TAG = "Picasso&OkHttp";
	
	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath(TAG);
		return sdkConfig;
	}
}
