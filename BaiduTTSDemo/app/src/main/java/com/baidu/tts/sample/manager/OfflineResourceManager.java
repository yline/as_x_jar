package com.baidu.tts.sample.manager;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.yline.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 离线资源管理
 *
 * @author yline 2019/4/2 -- 17:01
 */
public class OfflineResourceManager {
    /**
     * 文字的，资源
     */
    private static final String TEXT_2_VOICE = "baidu_text.dat";
    /**
     * 离线，女生
     */
    private static final String VOICE_WOMAN_7 = "baidu_speech_woman_7_v3.0.0_20170512.dat";
    /**
     * 离线，杜丫丫
     */
    private static final String VOICE_WOMAN_DUYY = "baidu_speech_woman_duyy_v3.0.0_20170516.dat";
    /**
     * 离线，男生
     */
    private static final String VOICE_MAN_15 = "baidu_speech_man_15_v3.0.0_20170505.dat";
    /**
     * 离线，杜逍遥
     */
    private static final String VOICE_MAN_DUXY = "baidu_speech_man_duxy_v3.0.0_20170512.dat";

    public static String getTextSource(Context context) {
        return copyAsset2File(context.getApplicationContext(), TEXT_2_VOICE, false);
    }

    public static String getVoiceSource(Context context) {
        return getVoiceSource(context, 0);
    }

    public static String getVoiceSource(Context context, int type) {
        String assetName = VOICE_WOMAN_7;
        switch (type) {
            case 0:
                assetName = VOICE_WOMAN_7;
                break;
            case 1:
                assetName = VOICE_WOMAN_DUYY;
                break;
            case 2:
                assetName = VOICE_MAN_15;
                break;
            case 3:
                assetName = VOICE_MAN_DUXY;
        }
        return copyAsset2File(context, assetName, false);
    }

    private static String copyAsset2File(Context context, String assetName, boolean isCover) {
        // 创建文案
        String dirPath = getPath(context) + "baiduTTS" + File.separator;
        File assetFile = new File(dirPath, assetName);
        if (!assetFile.exists()) {
            assetFile = FileUtil.create(dirPath, assetName);
        } else {
            if (!isCover) { // 文件已存在，并且不覆盖写入
                return assetFile.getAbsolutePath();
            }
        }

        // copy 资源【目标文件存在，并写入】
        if (null != assetFile) {
            try {
                FileOutputStream outputStream = new FileOutputStream(assetFile);
                InputStream inputStream = context.getAssets().open(assetName);
                byte[] buffer = new byte[2048];
                int size = 0;
                while ((size = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, size);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String sPath;

    public static synchronized String getPath(Context context) {
        if (sPath == null) {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                sPath = Environment.getExternalStorageDirectory().getPath();
            } else {
                sPath = context.getFilesDir().getAbsolutePath();
            }

            if (!TextUtils.isEmpty(sPath)) {
                sPath += (File.separator + ".baidu" + File.separator);
            }
        }
        return sPath;
    }
}
