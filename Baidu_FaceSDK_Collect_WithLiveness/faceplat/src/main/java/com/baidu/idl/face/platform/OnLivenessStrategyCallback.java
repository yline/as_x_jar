package com.baidu.idl.face.platform;

import android.graphics.Bitmap;

import com.baidu.idl.face.platform.model.FaceStatusEnum;
import com.baidu.idl.face.platform.model.LivenessTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 活体检测回调接口
 */
public interface OnLivenessStrategyCallback {
    /**
     * 检测成功
     */
    void onDetectSuccess(FaceStatusEnum status, String message, Map<String, Bitmap> resultMap);

    /**
     * 检测中 检测状态和动态状态，两者一个为空，另一个肯定不为null
     *
     * @param status   检测状态，可能为null
     * @param typeEnum 动态状态，可能为null
     */
    void onDetecting(FaceStatusEnum status, LivenessTypeEnum typeEnum, String message);

    /**
     * 检测失败
     */
    void onDetectFailed(FaceStatusEnum status, String message);
}
