package com.yline.router;

import android.app.Application;

import com.yline.framework.FrameworkManager;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FrameworkManager.init(this);
    }
}
