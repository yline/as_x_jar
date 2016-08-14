package com.litepal.instance;

import android.app.Application;

import com.litepal.lib.LitePalApplication;

public class MyApplication extends Application
{

	@Override
	public void onCreate()
	{
		super.onCreate();
		LitePalApplication.initialize(this);
	}
}
