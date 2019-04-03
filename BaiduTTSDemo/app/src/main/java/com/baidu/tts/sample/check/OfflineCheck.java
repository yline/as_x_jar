package com.baidu.tts.sample.check;

import android.text.TextUtils;

import com.baidu.tts.client.SpeechSynthesizer;

import java.io.File;

public class OfflineCheck implements CheckInterceptor {
    private String mTextFileName;
    private String mSpeechFileName;

    public OfflineCheck(String textFileName, String speechFileName) {
        mTextFileName = textFileName;
        mSpeechFileName = speechFileName;
    }

    @Override
    public StringBuilder check(StringBuilder lastLog) {
        lastLog.append("----------------检查离线资TEXT文件参数---------------").append("\n");
        String fileKey = SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE;
        checkKey(fileKey, lastLog, "SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE未设置");

        lastLog.append("----------------检查离线资源TEXT文件---------------").append("\n");
        checkFile(mTextFileName, lastLog);

        lastLog.append("----------------检查离线资Speech文件参数---------------").append("\n");
        fileKey = SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE;
        checkKey(fileKey, lastLog, "SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE未设置");

        lastLog.append("----------------检查离线资源Speech文件---------------").append("\n");
        checkFile(mSpeechFileName, lastLog);

        return lastLog;
    }

    private void checkKey(String fileKey, StringBuilder lastLog, String prefix) {
        if (TextUtils.isEmpty(fileKey)) {
            lastLog.append(prefix).append(", 参数中没有设置：").append(fileKey).append("\n");
            lastLog.append("请参照demo在设置").append(fileKey).append("参数").append("\n");
        } else {
            lastLog.append("通过").append("\n");
        }
    }

    private void checkFile(String fileName, StringBuilder lastLog) {
        File file = new File(fileName);
        boolean isSuccess = true;
        if (!file.exists()) {
            lastLog.append("资源文件不存在：").append(fileName).append("\n");
            isSuccess = false;
        } else if (!file.canRead()) {
            lastLog.append("资源文件不可读：").append(fileName).append("\n");
            isSuccess = false;
        }

        if (!isSuccess) {
            lastLog.append("请将demo中src/main/assets目录下同名文件复制到").append(fileName).append("\n");
        } else {
            lastLog.append("通过").append("\n");
        }
    }
}
