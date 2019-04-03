package com.baidu.tts.sample.check;

import com.yline.application.SDKManager;

public class ApplicationIdCheck implements CheckInterceptor {
    private static final String APP_ID = "11005757";

    @Override
    public StringBuilder check(StringBuilder lastLog) {
        lastLog.append("--------------检查包名------------").append("\n");

        lastLog.append("如果您集成过程中遇见离线合成初始化问题，请检查网页上appId：").append(APP_ID).append("\n");
        lastLog.append("应用是否开通了合成服务，并且网页上的应用填写了Android包名：").append(SDKManager.getApplication().getPackageName()).append("\n");

        return lastLog;
    }
}
