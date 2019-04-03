package com.baidu.tts.sample.manager;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.baidu.tts.aop.tts.TtsError;
import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechSynthesizeBag;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.yline.application.SDKManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 语音合成管理类
 *
 * @author yline 2019/3/26 -- 19:00
 */
public class SpeechSynthesizerManager {
    private static final String DOC = "http://ai.baidu.com/docs#/TTS-API/top";
    public static final int ERROR = -1;
    public static final int SUCCESS = 0;

    private static final int SPEECH_INIT = 1;
    private static final int SPEECH_RELEASE = 2;

    private static final String APP_ID = "11005757";
    private static final String APP_KEY = "Ovcz19MGzIKoDDb3IsFFncG1";
    private static final String SECRET_KEY = "e72ebb6d43387fc7f85205ca7e6706e2";

    private SpeechHandler tHandler;

    public static SpeechSynthesizerManager getInstance() {
        return new SpeechSynthesizerManager();
    }

    private SpeechSynthesizerManager() {
        HandlerThread handlerThread = new HandlerThread("SpeechSynthesizer");
        handlerThread.start();
        tHandler = new SpeechHandler(handlerThread.getLooper());
    }

    public void init() {
        tHandler.obtainMessage(SPEECH_INIT).sendToTarget();
    }

    public void release() {
        tHandler.obtainMessage(SPEECH_RELEASE).sendToTarget();
    }

    public void speak(@NonNull String text) {
        int result = tHandler.speak(text);
        if (SUCCESS != result) {
            SpeechManager.e("code = " + result + ", 文档：" + DOC);
        }
    }

    public void synthesize(@NonNull String text) {
        int result = tHandler.synthesize(text);
        if (SUCCESS != result) {
            SpeechManager.e("code = " + result + ", 文档：" + DOC);
        }
    }

    public void speakBatch(@NonNull Map<String, String> map) {
        int result = tHandler.batchSpeak(map);
        if (SUCCESS != result) {
            SpeechManager.e("code = " + result + ", 文档：" + DOC);
        }
    }

    public void resume() {
        int result = tHandler.resume();
        if (SUCCESS != result) {
            SpeechManager.e("code = " + result + ", 文档：" + DOC);
        }
    }

    public void pause() {
        int result = tHandler.pause();
        if (SUCCESS != result) {
            SpeechManager.e("code = " + result + ", 文档：" + DOC);
        }
    }

    public void stop() {
        int result = tHandler.stop();
        if (SUCCESS != result) {
            SpeechManager.e("code = " + result + ", 文档：" + DOC);
        }
    }

    public void loadModel(Context context, int voiceType) {
        int result = tHandler.loadModel(context, voiceType);
        if (SUCCESS != result) {
            SpeechManager.e("code = " + result + ", 文档：" + DOC);
        }
    }

    public void setSpeechSynthesizerListener(SpeechSynthesizerListener listener) {
        tHandler.setSpeechSynthesizerListener(listener);
    }

    // 真正实现功能的地方，如果通过handler实现，则是在子线程
    private static class SpeechHandler extends Handler {
        private SpeechSynthesizer sSynthesizer;

        private SpeechHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SPEECH_INIT:
                    init(SDKManager.getApplication(), TtsMode.MIX);
                    break;
                case SPEECH_RELEASE:
                    sSynthesizer.stop();
                    sSynthesizer.release();
                    sSynthesizer = null;

                    releaseLooper();
                    break;
            }
        }

        private int init(Context context, TtsMode ttsMode) {
            sSynthesizer = SpeechSynthesizer.getInstance();
            sSynthesizer.setContext(context);

            // 注册应用得到的 APP_ID、APP_KEY、SECRET_KEY
            sSynthesizer.setAppId(APP_ID);
            sSynthesizer.setApiKey(APP_KEY, SECRET_KEY);

            // isMix 离在线融合
            if (TtsMode.MIX.equals(ttsMode)) {
                // 授权检测接口(只是通过AuthInfo进行检验授权是否成功。选择纯在线可以不必调用auth方法。
                AuthInfo authInfo = sSynthesizer.auth(ttsMode);
                if (null == authInfo) { // 失败；鉴权失败
                    SpeechManager.e("authInfo is null");
                } else if (!authInfo.isSuccess()) { // 失败；鉴权失败
                    TtsError ttsError = authInfo.getTtsError();
                    if (null != ttsError) {
                        SpeechManager.e("code = " + ttsError.getDetailCode() + ", msg = " + ttsError.getDetailMessage());
                    } else {
                        SpeechManager.e("authInfo ttsError is null");
                    }
                } else { // 成功；验证通过，离线正式授权文件存在。
                    SpeechManager.v("auth success!!!");
                }
            }

            buildParams(context);

            return sSynthesizer.initTts(ttsMode);
        }

        private void buildParams(Context context) {
            // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
            sSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
            // 设置合成的音量，0-9 ，默认 5
            sSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "9");
            // 设置合成的语速，0-9 ，默认 5
            sSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
            // 设置合成的语调，0-9 ，默认 5
            sSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");

            // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
            // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
            // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
            // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
            // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
            sSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);

            // 外放
            sSynthesizer.setAudioStreamType(AudioManager.MODE_IN_CALL);

            // 离线资源文件， 从assets目录中复制到临时目录，需要在initTTs方法前完成
            String textSource = OfflineResourceManager.getTextSource(context);
            String voiceSource = OfflineResourceManager.getVoiceSource(context);
            if (!TextUtils.isEmpty(textSource) && !TextUtils.isEmpty(voiceSource)) {
                sSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, textSource);
                sSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, voiceSource);
            }
        }

        /**
         * 合成并播放
         *
         * @param text 小于1024 GBK字节，即512个汉字或者字母数字
         * @return -1 if synthesizer is null
         */
        private int speak(String text) {
            if (null != sSynthesizer) {
                return sSynthesizer.speak(text);
            }
            return ERROR;
        }

        /**
         * 只合成不播放
         *
         * @param text 小于1024 GBK字节，即512个汉字或者字母数字
         * @return -1 if synthesizer is null
         */
        private int synthesize(String text) {
            if (null != sSynthesizer) {
                return sSynthesizer.synthesize(text);
            }
            return ERROR;
        }

        /**
         * 批量播放
         *
         * @param map 文案和音量(可以为null)，集合
         * @return error if synthesizer is null and map is null
         */
        private int batchSpeak(Map<String, String> map) {
            if (null != sSynthesizer && null != map) {
                List<SpeechSynthesizeBag> bagList = new ArrayList<>();
                SpeechSynthesizeBag bag = new SpeechSynthesizeBag();
                for (String text : map.keySet()) {
                    bag.setText(text);
                    String value = map.get(text);
                    if (null != value) {
                        bag.setUtteranceId(value);
                    }
                }
                sSynthesizer.batchSpeak(bagList);
            }
            return ERROR;
        }

        /**
         * 暂停播放。仅调用speak后生效
         *
         * @return error if synthesizer is null
         */
        private int pause() {
            if (null != sSynthesizer) {
                return sSynthesizer.pause();
            }
            return ERROR;
        }

        /**
         * 继续播放。仅调用speak后生效，调用pause生效
         *
         * @return error if synthesizer is null
         */
        private int resume() {
            if (null != sSynthesizer) {
                return sSynthesizer.resume();
            }
            return ERROR;
        }

        /**
         * 停止合成引擎。即停止播放，合成，清空内部合成队列。
         *
         * @return error if synthesizer is null
         */
        private int stop() {
            if (null != sSynthesizer) {
                return sSynthesizer.stop();
            }
            return ERROR;
        }

        /**
         * 切换离线发音。注意需要添加额外的判断：引擎在合成时该方法不能调用
         * 注意 只有 TtsMode.MIX 才可以切换离线发音
         */
        private int loadModel(Context context, int voiceType) {
            if (null != sSynthesizer) {
                String textSource = OfflineResourceManager.getTextSource(context);
                String voiceSource = OfflineResourceManager.getVoiceSource(context, voiceType);
                if (!TextUtils.isEmpty(textSource) && !TextUtils.isEmpty(voiceSource)) {
                    return sSynthesizer.loadModel(textSource, voiceSource);
                }
            }
            return ERROR;
        }

        private void setSpeechSynthesizerListener(SpeechSynthesizerListener listener) {
            if (null != sSynthesizer) {
                sSynthesizer.setSpeechSynthesizerListener(listener);
            }
        }

        private void releaseLooper() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                getLooper().quitSafely();
            } else {
                getLooper().quit();
            }
        }
    }
}
