package com.baidu.idl.face.example;

import com.baidu.idl.face.platform.ui.module.FaceDetectActivity;
import com.baidu.idl.face.platform.ui.module.FaceLivenessActivity;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.ui.module.FaceSettingsActivity;
import com.yline.application.SDKManager;
import com.yline.log.LogFileUtil;
import com.yline.test.BaseTestActivity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseTestActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化是否成功
        checkLicenseState(System.currentTimeMillis());
    }

    private void checkLicenseState(final long startTime) {
        boolean success = FaceSDKManager.getInstance().isLicenseSuccess();
        if (success) {
            LogFileUtil.v("license init success, cost time = " + (System.currentTimeMillis() - startTime));
        } else {
            LogFileUtil.e("license init waiting, cost time = " + (System.currentTimeMillis() - startTime));
            SDKManager.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkLicenseState(startTime);
                }
            }, 1000);
        }
    }

    @Override
    protected void onDestroy() {
        FaceSDKManager.getInstance().release();
        super.onDestroy();
    }

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("图片查看", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageActivity.launch(MainActivity.this);
            }
        });

        addButton("设置", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FaceSettingsActivity.launch(MainActivity.this);
            }
        });

        addButton("活体检测", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FaceLivenessActivity.launch(MainActivity.this);
            }
        });

        addButton("人脸图像采集", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FaceDetectActivity.launch(MainActivity.this);
            }
        });
    }

    protected String[] initRequestPermission() {
        return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    }
}
