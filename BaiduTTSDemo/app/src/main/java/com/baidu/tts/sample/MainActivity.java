package com.baidu.tts.sample;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import com.baidu.tts.sample.synth.SynthActivity;
import com.baidu.tts.sample.synthsave.SaveFileActivity;
import com.yline.test.BaseTestActivity;

/**
 * 首页
 *
 * @author yline 2019/3/26 -- 16:47
 */
public class MainActivity extends BaseTestActivity {

    @Override
    protected String[] initRequestPermission() {
        return new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE
        };
    }

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("离在线语音合成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SynthActivity.launch(MainActivity.this);
            }
        });

        addButton("保存合成后的音频", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveFileActivity.launch(MainActivity.this);
            }
        });
    }
}