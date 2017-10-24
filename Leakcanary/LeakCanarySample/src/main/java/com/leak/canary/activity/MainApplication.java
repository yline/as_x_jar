package com.leak.canary.activity;

import com.squareup.leakcanary.LeakCanary;
import com.yline.application.BaseApplication;

/**
 * @author yline 2017/10/24 -- 16:07
 * @version 1.0.0
 */
public class MainApplication extends BaseApplication
{
	public static final String TAG = "LeakCanarySample";

	@Override
	public void onCreate()
	{
		super.onCreate();
		LeakCanary.install(this);
	}
}
