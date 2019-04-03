package com.baidu.tts.sample.check;

import android.text.TextUtils;

import com.baidu.tts.client.TtsMode;
import com.baidu.tts.sample.manager.OfflineResourceManager;
import com.baidu.tts.sample.manager.SpeechManager;
import com.yline.application.SDKManager;

import java.util.ArrayList;
import java.util.List;

public class AutoCheck {

    public static void check(final TtsMode ttsMode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = checkInner(ttsMode);
                SpeechManager.v("result:\n" + result);
            }
        }).start();
    }

    private static String checkInner(TtsMode ttsMode) {
        List<CheckInterceptor> checkList = new ArrayList<>();

        checkList.add(new PermissionCheck()); // 检查申请的Android权限
        checkList.add(new JniCheck()); // 检查4个so文件是否存在
        checkList.add(new AppInfoCheck()); // 检查AppId AppKey SecretKey
        checkList.add(new ApplicationIdCheck()); // 检查包名

        if (TtsMode.MIX.equals(ttsMode)) {
            // 检查离线资源
            String textSource = OfflineResourceManager.getTextSource(SDKManager.getApplication());
            String voiceSource = OfflineResourceManager.getVoiceSource(SDKManager.getApplication());
            if (!TextUtils.isEmpty(textSource) && !TextUtils.isEmpty(voiceSource)) {
                checkList.add(new OfflineCheck(textSource, voiceSource));
            } else {
                SpeechManager.e("max tts mode failed, textSource = " + textSource + ", voiceSource = " + voiceSource);
            }
        } else {
            SpeechManager.v("ttsModel = " + ttsMode);
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < checkList.size(); i++) {
            CheckInterceptor interceptor = checkList.get(i);
            stringBuilder = interceptor.check(stringBuilder);
        }
        return stringBuilder.toString();
    }


}
