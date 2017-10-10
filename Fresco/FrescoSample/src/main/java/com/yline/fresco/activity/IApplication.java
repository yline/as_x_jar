package com.yline.fresco.activity;

import com.yline.application.BaseApplication;
import com.yline.fresco.common.FrescoConfig;

public class IApplication extends BaseApplication {

    public static final String BenDiUrl = "http://img.benditoutiao.com/material/20170914/081cf6c0-98e8-11e7-aecc-4fb4862aa761.png@!news-list-single-pic";

    @Override
    public void onCreate() {
        super.onCreate();

        FrescoConfig.initConfig(this, true);
    }
}
