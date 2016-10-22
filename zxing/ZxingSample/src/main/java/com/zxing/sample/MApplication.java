package com.zxing.sample;

import android.app.Application;

import com.zxing.lib.activity.ZXingLibrary;

/**
 * bug:视图 有的时候,扫的时候对准的地方与真实对准的地方不一致。
 * @author yline 2016/10/22 --> 23:07
 * @version 1.0.0
 */
public class MApplication extends Application
{

	@Override
	public void onCreate()
	{
		super.onCreate();

		ZXingLibrary.initDisplayOpinion(this);
	}
}
