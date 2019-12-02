package com.baidu.idl.face.platform;

import android.graphics.Bitmap;

import com.baidu.idl.face.platform.model.FaceStatusEnum;

import java.util.HashMap;
import java.util.List;

/**
 * IDetectStrategyCallback
 * 描述:人脸跟踪回调接口
 */
public interface OnDetectStrategyCallback {
    /**
     * 检测成功
     */
    void onDetectSuccess(FaceStatusEnum status, String message, Bitmap bitmap);

    /**
     * 检测中
     */
    void onDetecting(FaceStatusEnum status, String message);

    /**
     * 检测失败
     */
    void onDetectFailed(FaceStatusEnum status, String message);
}
