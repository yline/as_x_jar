package com.yline.fresco.activity;

import android.app.Application;

import com.yline.fresco.common.FrescoConfig;

public class IApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FrescoConfig.initConfig(this, true);
    }
}
