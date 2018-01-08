package com.bugly;

import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;
import com.yline.application.SDKManager;

import java.util.Map;

/**
 * Bugly 日志统计，的使用
 * Jcenter中的地址：https://jcenter.bintray.com/com/tencent/bugly/
 *
 * @author yline 2018/1/8 -- 11:25
 * @version 1.0.0
 */
public class BuglyConfig {
    private static final String APP_ID = "79ea17e2ee";
    public static final boolean isDebug = true;

    /**
     * Application 中同步进行 设置
     *
     * @param context 全局的环境变量
     */
    public static void initConfig(Context context) {
        CrashReport.UserStrategy userStrategy = new CrashReport.UserStrategy(context);
//        userStrategy.setAppVersion("1.0.0"); // App版本号，会覆盖AndroidManifest的默认配置和自定义配置
//        userStrategy.setAppPackageName("com.bugly"); // App版本号，会覆盖AndroidManifest的默认配置
//        userStrategy.setAppReportDelay(10_000); // 默认启动10s后，同步数据
        userStrategy.setCrashHandleCallback(new CrashReport.CrashHandleCallback() {
            @Override
            public synchronized Map<String, String> onCrashHandleStart(int i, String s, String s1, String s2) {
                // TODO 添加CrashHandler 数据
                return super.onCrashHandleStart(i, s, s1, s2);
            }

            @Override
            public synchronized byte[] onCrashHandleStart2GetExtraDatas(int i, String s, String s1, String s2) {
                // TODO 额外数据设置
                return super.onCrashHandleStart2GetExtraDatas(i, s, s1, s2);
            }
        });

        CrashReport.initCrashReport(context, APP_ID, isDebug, userStrategy);
    }

    /* ---------------------------------- 设置 其它参数 ------------------------------- */

    /**
     * 对应不同页面，设置不同的tag; 即可知道bug 触发的界面
     *
     * @param tag 页面的标记
     */
    public static void setUserSceneTag(int tag) {
        CrashReport.setUserSceneTag(SDKManager.getApplication(), tag);
    }

    /**
     * 例如：设置设备信息、用户的id；进行用户的独立定位
     *
     * @param key   key限长50字节，必须匹配正则：[a-zA-Z[0-9]]+
     * @param value value限长200字节，过长截断
     */
    public static void setUserData(String key, String value) {
        CrashReport.putUserData(SDKManager.getApplication(), key, value);
    }

    /**
     * 区别是否是，开发者设备，方便区分日志来源
     *
     * @param isDevelopmentDevice 是否是开发设备
     */
    public static void setIsDevelopmentDevice(boolean isDevelopmentDevice) {
        CrashReport.setIsDevelopmentDevice(SDKManager.getApplication(), isDevelopmentDevice);
    }
}

















