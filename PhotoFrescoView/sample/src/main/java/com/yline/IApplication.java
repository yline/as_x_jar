package com.yline;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.yline.application.BaseApplication;

/**
 * @author yline 2017/11/27 -- 16:54
 * @version 1.0.0
 */
public class IApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(getApplicationContext());
    }
}
