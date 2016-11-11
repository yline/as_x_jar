package com.leak.canary.activity;

import android.os.StrictMode;

import com.squareup.leakcanary.LeakCanary;
import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.GINGERBREAD;

/**
 * https://github.com/square/leakcanary.git git地址
 * @author yline 2016/11/11 --> 23:16
 * @version 1.0.0
 */
public class MainApplication extends BaseApplication
{
	public static final String TAG = "LeakCanarySample";

	@Override
	public void onCreate()
	{
		super.onCreate();
		if (LeakCanary.isInAnalyzerProcess(this))
		{
			return;
		}
		enabledStrictMode();
		LeakCanary.install(this);
	}

	private void enabledStrictMode()
	{
		if (SDK_INT >= GINGERBREAD)
		{
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //
					.detectAll() //
					.penaltyLog() //
					.penaltyDeath() //
					.build());
		}
	}

	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath(TAG);
		return sdkConfig;
	}
}
