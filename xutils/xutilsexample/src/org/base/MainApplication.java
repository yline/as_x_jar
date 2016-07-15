package org.base;

import android.app.Application;

import org.xutils.x;

import f21.xutilsexample.BuildConfig;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        
        /** 初始化xutils3 */
        x.Ext.init(this);
        /** 设置debug模式 */
        x.Ext.setDebug(BuildConfig.DEBUG);
    }
}
