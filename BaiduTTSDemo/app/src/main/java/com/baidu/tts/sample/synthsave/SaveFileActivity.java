package com.baidu.tts.sample.synthsave;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.sample.manager.SpeechManager;
import com.baidu.tts.sample.synth.SynthActivity;
import com.yline.utils.FileUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 点击合成按钮，保存录音文件
 *
 * @author linjiang@kjtpay.com  2019/3/27 -- 14:00
 */
public class SaveFileActivity extends SynthActivity {
    public static void launch(Context context) {
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, SaveFileActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected SpeechSynthesizerListener providerListener() {
        return new SpeechSynthesizerListener() {
            @Override
            public void onSynthesizeStart(String utteranceId) {
                // 播放开始，每句播放开始都会回调
                SpeechManager.v("准备开始合成，序列号 = " + utteranceId);

                createTtsFile(utteranceId);
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

                writeData(bytes);
            }

            @Override
            public void onSynthesizeFinish(String utteranceId) {
                SpeechManager.v("合成结束回调，序列号 = " + utteranceId);

                close();
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

                close();
            }
        };
    }

    private String dirPath;
    private BufferedOutputStream bufferedOutputStream;

    private void createTtsFile(String utterId) {
        final String fileName = "output-" + utterId + ".pcm";

        if (null == dirPath) {
            FileUtil.createDir(Environment.getExternalStorageDirectory().toString() + File.separator + "baiduTTS");
        }
        File ttsFile = new File(dirPath, fileName);
        SpeechManager.v("try to write audio file to " + ttsFile.getAbsolutePath());

        try {
            if (ttsFile.exists()) {
                ttsFile.delete();
            }

            ttsFile.createNewFile();
            // 创建FileOutputStream对象
            FileOutputStream outputStream = new FileOutputStream(ttsFile);

            // 创建BufferedOutputStream对象
            bufferedOutputStream = new BufferedOutputStream(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeData(byte[] data) {
        try {
            if (null != bufferedOutputStream) {
                bufferedOutputStream.write(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        try {
            if (null != bufferedOutputStream) {
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
                bufferedOutputStream = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
