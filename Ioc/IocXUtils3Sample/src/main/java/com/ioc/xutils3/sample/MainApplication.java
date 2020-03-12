package com.ioc.xutils3.sample;

import android.os.Message;

import com.ioc.xutils3.sample.base.x;
import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

/**
 * 继承之后,自己调用方法开启对应的功能
 *
 * @author YLine 2016/8/18 --> 23:56
 * @version 1.0.0
 */
public class MainApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化
        x.init(this);
    }
}


















