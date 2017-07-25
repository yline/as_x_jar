package org.xutils.sample;

import android.app.Application;

import org.xutils.x;

/**
 * Created by wyouflf on 15/10/28.
 */

/**
 * @author yline 2016/10/5 --> 7:33
 * @version 1.0.0
 */
public class MainApplication extends Application
{
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		x.Ext.init(this);
		x.Ext.setDebug(BuildConfig.DEBUG); // 开启debug会影响性能
	}
}
