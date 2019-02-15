package com.yline.jetpack;

import com.yline.application.BaseApplication;
import com.yline.view.fresco.common.FrescoConfig;

public class MainApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        FrescoConfig.initConfig(this, true);
    }
}
