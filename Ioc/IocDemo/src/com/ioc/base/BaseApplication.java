package com.ioc.base;

import java.util.ArrayList;
import java.util.List;

import com.lib.ioc.utils.LogIoc;

import android.app.Activity;
import android.app.Application;

/**
 * 继承之后,自己调用方法开启对应的功能
 * @author fatenliyer
 * @date 2016-1-2
 */
public class BaseApplication extends Application {
	public static List<Activity> activities = new ArrayList<Activity>();
	
	/** 抛出x.app() + 设置环境 */
	protected static final boolean isInject = true;
	/** 关闭调试,默认开启 */
	private static final boolean isDebug = true;

	@Override
	public void onCreate() {
		super.onCreate();

		if (isInject) {
			x.Ext.init(this);
		}
		x.Ext.setDebug(isDebug);
	}
	
	/**
	 * 添加
	 * @param activity
	 */
	public static void addActivity(Activity activity){
		LogIoc.v("addActivity:" + activity.getClass().getSimpleName());
		activities.add(activity);
	}
	
	/**
	 * 移除
	 * @param activity
	 */
	public static void removeActivity(Activity activity){
		activities.remove(activity);
		LogIoc.v("removeActivity:" + activity.getClass().getSimpleName());
	}
	
	/**
	 * 退出当前Application,应用
	 */
	public static void finishAllActivity(){
		LogIoc.v("finishAllActivity");
		for(Activity activity : activities){
			if (!activity.isFinishing()) {
				activity.finish();
			}
		}
	}
}


















