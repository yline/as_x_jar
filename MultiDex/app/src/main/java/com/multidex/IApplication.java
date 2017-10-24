package com.multidex;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.yline.application.SDKConfig;
import com.yline.application.SDKManager;

/**
 *
 * @author yline 2017/10/24 -- 15:38
 * @version 1.0.0
 */
public class IApplication extends Application{
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SDKManager.init(this, new SDKConfig());
    }
}
