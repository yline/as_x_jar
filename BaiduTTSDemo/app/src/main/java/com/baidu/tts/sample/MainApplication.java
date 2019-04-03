package com.baidu.tts.sample;

import com.baidu.tts.sample.manager.SpeechManager;
import com.yline.application.BaseApplication;

public class MainApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        SpeechManager.setIsDebug(true);
    }
}
