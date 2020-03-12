package com.ioc.xutils3.sample.base;

import android.os.Message;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

/**
 * 继承之后,自己调用方法开启对应的功能
 *
 * @author YLine 2016/8/18 --> 23:56
 * @version 1.0.0
 */
public class IocApplication extends BaseApplication {
    private static final String TAG = "IocXUtils3Sample";

    /**
     * 抛出x.app() + 设置环境
     */
    protected static final boolean isInject = true;

    /**
     * 关闭调试,默认开启
     */
    private static final boolean isDebug = true;

    @Override
    public void onCreate() {
        super.onCreate();

        if (isInject) {
            x.Ext.init(this);
        }
        x.Ext.setDebug(isDebug);
    }
}


















