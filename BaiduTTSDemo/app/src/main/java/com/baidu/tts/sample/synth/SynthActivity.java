package com.baidu.tts.sample.synth;

import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.baidu.tts.sample.check.AutoCheck;
import com.baidu.tts.sample.manager.SpeechManager;
import com.baidu.tts.sample.manager.SpeechSynthesizerManager;
import com.baidu.tts.sample.synth.view.SynthView;
import com.yline.base.BaseAppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

/**
 * 合成demo。含在线和离线，没有纯离线功能。
 * 根据网络状况优先走在线，在线时访问服务器失败后转为离线。
 * <p>
 * 1，测试离线合成功能需要首次联网。
 * 2，纯在线请修改代码里ttsMode为TtsMode.ONLINE， 没有纯离线。
 * 3，本Demo的默认参数设置为wifi情况下在线合成, 其它网络（包括4G）使用离线合成。 在线普通女声发音，离线男声发音.
 * 4，合成可以多次调用，SDK内部有缓存队列，会依次完成。
 *
 * @author yline 2019/3/26 -- 17:02
 */
public class SynthActivity extends BaseAppCompatActivity {
    public static void launch(Context context) {
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, SynthActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private SynthView mSynthView;
    private SpeechSynthesizerManager synthesizerManager;

    private static final String SYNTH_HELP = "1，测试离线合成功能需要首次联网。\n" +
            "2，纯在线请修改代码里ttsMode为TtsMode.ONLINE， 没有纯离线。\n" +
            "3，本Demo的默认参数设置为wifi情况下在线合成, 其它网络（包括4G）使用离线合成。 在线普通女声发音，离线男声发音.\n" +
            "4，合成可以多次调用，SDK内部有缓存队列，会依次完成。";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSynthView = new SynthView(this);
        setContentView(mSynthView);

        initViewClick();

        initialTts(); // 初始化TTS引擎
    }

    private void initViewClick() {
        // 合成并播放
        mSynthView.setOnSpeakClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSynthView.setShowText("");
                String inputText = mSynthView.getInputText();
                if (TextUtils.isEmpty(inputText)) {
                    inputText = "输入内容为空！！！";
                }

                synthesizerManager.speak(inputText);
            }
        });

        // 只合成不播放
        mSynthView.setOnSynthesizeClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSynthView.setShowText("");
                String inputText = mSynthView.getInputText();
                if (TextUtils.isEmpty(inputText)) {
                    inputText = "输入内容为空！！！";
                }

                synthesizerManager.synthesize(inputText);
            }
        });

        //  批量合成并播放
        mSynthView.setOnBatchSpeakClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSynthView.setShowText("");

                ArrayMap<String, String> batchMap = new ArrayMap<>();
                batchMap.put("开始批量播放，", "a0");
                batchMap.put("123456，", "a1");
                batchMap.put("欢迎使用百度语音，，，", "a2");
                batchMap.put("重(chong2)量这个是多音字示例", "a3");
                synthesizerManager.speakBatch(batchMap);
            }
        });

        // 切换离线资源
        mSynthView.setOnLoadModelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SynthActivity.this, android.R.style.Theme_Holo_Light_Dialog);
                builder.setTitle("引擎空闲时切换");

                final int[] typeArray = {0, 1, 2, 3};
                final String[] keyArray = {"离线女声", "离线度丫丫", "离线男声", "离线度逍遥"};
                builder.setItems(keyArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int voiceType = typeArray[which];
                        synthesizerManager.loadModel(SynthActivity.this, voiceType);
                    }
                });
                builder.show();
            }
        });

        // 播放暂停
        mSynthView.setOnPauseClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                synthesizerManager.pause();
            }
        });

        // 播放恢复
        mSynthView.setOnResumeClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                synthesizerManager.resume();
            }
        });

        // 停止合成引擎
        mSynthView.setOnStopClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                synthesizerManager.stop();
            }
        });

        // 帮助按钮
        mSynthView.setOnHelpClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSynthView.setShowText(SYNTH_HELP);
            }
        });
    }

    protected void initialTts() {
        AutoCheck.check(TtsMode.MIX);

        synthesizerManager = SpeechSynthesizerManager.getInstance();
        synthesizerManager.init(); // 推到子线程中，初始化
        synthesizerManager.setSpeechSynthesizerListener(providerListener());
    }

    protected SpeechSynthesizerListener providerListener() {
        return new SpeechSynthesizerListener() {
            @Override
            public void onSynthesizeStart(String utteranceId) {
                // 播放开始，每句播放开始都会回调
                SpeechManager.v("准备开始合成，序列号 = " + utteranceId);
            }

            /**
             * 语音流 16K采样率 16bits编码 单声道 。
             * @param bytes 二进制语音 ，注意可能有空data的情况，可以忽略
             * @param progress 如合成“百度语音问题”这6个字， progress肯定是从0开始，到6结束。 但progress无法和合成到第几个字对应。
             */
            @Override
            public void onSynthesizeDataArrived(String utteranceId, byte[] bytes, int progress) {
                // 语音流 16K采样率 16bits编码 单声道 。
                SpeechManager.v("合成进度回调，序列号 = " + utteranceId + ", progress = " + progress);
            }

            @Override
            public void onSynthesizeFinish(String utteranceId) {
                SpeechManager.v("合成结束回调，序列号 = " + utteranceId);
            }

            @Override
            public void onSpeechStart(String utteranceId) {
                SpeechManager.v("播放开始回调，序列号 = " + utteranceId);
            }

            @Override
            public void onSpeechProgressChanged(String utteranceId, int progress) {
                SpeechManager.v("播放进度回调，序列号 = " + utteranceId + ", progress = " + progress);
            }

            @Override
            public void onSpeechFinish(String utteranceId) {
                SpeechManager.v("播放结束回调，序列号 = " + utteranceId);
            }

            @Override
            public void onError(String utteranceId, SpeechError speechError) {
                SpeechManager.e("错误发生，序列号 = " + utteranceId + ", 错误码：" + speechError.code
                        + ", 错误描述：" + speechError.description);
            }
        };
    }

    @Override
    protected void onDestroy() {
        synthesizerManager.release();
        super.onDestroy();
    }
}
