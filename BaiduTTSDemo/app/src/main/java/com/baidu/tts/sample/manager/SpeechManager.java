package com.baidu.tts.sample.manager;

import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.yline.utils.LogUtil;

public class SpeechManager {
    private static boolean mIsDebug = true;

    public static void setIsDebug(boolean isDebug) {
        LoggerProxy.printable(isDebug); // 日志打印在logcat中
        mIsDebug = isDebug;
    }

    public static boolean isDebug() {
        return mIsDebug;
    }

    public static void v(String content) {
        if (mIsDebug) {
            LogUtil.v("Speech:" + content, LogUtil.LOG_LOCATION_PARENT);
        }
    }

    public static void e(String content) {
        if (mIsDebug) {
            LogUtil.e("Speech:" + content, LogUtil.LOG_LOCATION_PARENT);
        }
    }
}
