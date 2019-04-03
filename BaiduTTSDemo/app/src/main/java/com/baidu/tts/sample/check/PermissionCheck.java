package com.baidu.tts.sample.check;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.yline.application.SDKManager;

import java.util.ArrayList;

public class PermissionCheck implements CheckInterceptor {

    @Override
    public StringBuilder check(StringBuilder lastLog) {
        final Context context = SDKManager.getApplication();

        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                // Manifest.permission.WRITE_EXTERNAL_STORAGE,
                // Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                // Manifest.permission.CHANGE_WIFI_STATE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(context, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.
            }
        }

        lastLog.append("---------------------检查申请的Android权限--------------------").append("\n");
        if (!toApplyList.isEmpty()) {
            lastLog.append("缺少权限：").append(toApplyList).append("\n");
            lastLog.append("请从AndroidManifest.xml复制相关权限").append("\n");
        } else {
            lastLog.append("通过").append("\n");
        }

        return lastLog;
    }
}
