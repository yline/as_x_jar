package com.yline.framework;

import android.app.Application;

import com.yline.application.SDKConfig;
import com.yline.application.SDKManager;
import com.yline.framework.router.RouterManager;
import com.yline.utils.LogUtil;

public class FrameworkManager {
    /**
     * 在Application 的时候进行初始化
     */
    public static void init(Application application) {
        SDKManager.init(application, new SDKConfig());

        long startTime = System.currentTimeMillis();
        RouterManager.init(application);
        LogUtil.v("router cost:" + (System.currentTimeMillis() - startTime));
    }
}
