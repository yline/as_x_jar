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
        tHandler.obtainMessage(SpeechHandler.INIT).sendToTarget();
    }

    public void release() {
        tHandler.obtainMessage(SpeechHandler.RELEASE).sendToTarget();
    }

    public void speak(@NonNull String text) {
        tHandler.obtainMessage(SpeechHandler.SPEAK, text).sendToTarget();
    }

    public void synthesize(@NonNull String text) {
        tHandler.obtainMessage(SpeechHandler.SYNTHESIZE, text).sendToTarget();
    }

    public void speakBatch(@NonNull Map<String, String> map) {
        tHandler.obtainMessage(SpeechHandler.BATCH_SPEAK, map).sendToTarget();
    }

    public void resume() {
        tHandler.obtainMessage(SpeechHandler.RESUME).sendToTarget();
    }

    public void pause() {
        tHandler.obtainMessage(SpeechHandler.PAUSE).sendToTarget();
    }

    public void stop() {
        tHandler.obtainMessage(SpeechHandler.STOP).sendToTarget();
    }

    public void loadModel(int voiceType) {
        tHandler.obtainMessage(SpeechHandler.LOAD_MODEL, voiceType).sendToTarget();
    }

    public void setSpeechSynthesizerListener(SpeechSynthesizerListener listener) {
        tHandler.setSpeechSynthesizerListener(listener);
    }

    // 真正实现功能的地方，如果通过handler实现，则是在子线程；并且，内部都是阻塞进行的
    private static class SpeechHandler extends Handler {
        private static final int INIT = 1; // 初始化

        private static final int SPEAK = 10; // 合成并播放
        private static final int SYNTHESIZE = 11; // 只合成，不播放
        private static final int BATCH_SPEAK = 12; // 批量播放
        private static final int PAUSE = 13; // 暂停播放
        private static final int RESUME = 14; // 继续播放
        private static final int STOP = 15; // 停止合成引擎。即停止播放，合成，清空内部合成队列。
        private static final int LOAD_MODEL = 16; // 切换离线发音。注意需要添加额外的判断：引擎在合成时该方法不能调用

        private static final int RELEASE = 100; // 释放

        private boolean sInitSuccess;

        private SpeechSynthesizer sSynthesizer;

        private SpeechHandler(Looper looper) {
            super(looper);
            sInitSuccess = false;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            SpeechManager.v("msg.what = " + msg.what);
            try {
                if (!sInitSuccess) {
                    if (msg.what == INIT) {
                        int result = init(SDKManager.getApplication(), TtsMode.MIX);
                        sInitSuccess = (result == SUCCESS);
                    }
                } else {
                    int code = 0;
                    switch (msg.what) {
                        case SPEAK:
                            String textSpeak = (String) msg.obj;
                            code = sSynthesizer.speak(textSpeak);
                            break;
                        case SYNTHESIZE:
                            String textSynth = (String) msg.obj;
                            code = sSynthesizer.synthesize(textSynth);
                            break;
                        case BATCH_SPEAK:
                            if (msg.obj instanceof Map) {
                                Map batchMap = (Map) msg.obj;

                                List<SpeechSynthesizeBag> bagList = new ArrayList<>();
                                SpeechSynthesizeBag bag = new SpeechSynthesizeBag();
                                for (Object text : batchMap.keySet()) {
                                    bag.setText(String.valueOf(text));
                                    Object value = batchMap.get(text);
                                    if (null != value) {
                                        bag.setUtteranceId(String.valueOf(value));
                                    }
                                }
                                code = sSynthesizer.batchSpeak(bagList);
                            }
                            break;
                        case PAUSE:
                            code = sSynthesizer.pause();
                            break;
                        case RESUME:
                            code = sSynthesizer.resume();
                            break;
                        case STOP:
                            code = sSynthesizer.stop();
                            break;
                        case LOAD_MODEL:
                            int voiceType = (int) msg.obj;
                            String textSource = OfflineResourceManager.getTextSource(SDKManager.getApplication());
                            String voiceSource = OfflineResourceManager.getVoiceSource(SDKManager.getApplication(), voiceType);
                            if (!TextUtils.isEmpty(textSource) && !TextUtils.isEmpty(voiceSource)) {
                                code = sSynthesizer.loadModel(textSource, voiceSource);
                            }
                            break;
                        case RELEASE:
                            sSynthesizer.stop();
                            code = sSynthesizer.release();
                            sSynthesizer = null;
                            sInitSuccess = false;

                            releaseLooper();
                            break;
                    }

                    // 异常
                    if (code != SUCCESS) {
                        SpeechManager.e("code = " + code + ", 文档 = " + DOC);
                    }
                }
            } catch (Exception ex) {
                SpeechManager.e("handlerMessage Error, ex = " + android.util.Log.getStackTraceString(ex));
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
