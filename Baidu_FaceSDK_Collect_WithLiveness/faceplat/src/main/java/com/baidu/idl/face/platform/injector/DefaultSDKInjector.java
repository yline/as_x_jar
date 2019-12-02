package com.baidu.idl.face.platform.injector;

import android.util.Log;

public class DefaultSDKInjector implements IFaceSDKInjector {
    @Override
    public void log(String content, int level) {
        if (level == 0) {
            Log.v("xxx-face", content);
        } else {
            Log.e("xxx-face", content);
        }
    }
}
