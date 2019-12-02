package com.baidu.idl.face.example;

import android.app.Application;

import com.baidu.idl.face.platform.injector.IFaceSDKInjector;
import com.baidu.idl.face.platform.ui.FaceUIManager;
import com.yline.application.SDKConfig;
import com.yline.application.SDKManager;
import com.yline.log.LogFileUtil;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SDKManager.init(this, new SDKConfig());

        FaceUIManager.init(this, new IFaceSDKInjector() {
            @Override
            public void log(String content, int level) {
                if (level == 0) {
                    LogFileUtil.v("face", content, 5);
                } else {
                    LogFileUtil.e("face", content, 5);
                }
            }
        });
    }
}
