package com.baidu.idl.face.platform.config;

import com.baidu.idl.facesdk.FaceSDK;
import com.baidu.idl.facesdk.FaceTracker;

/**
 * SDK内部的配置文件
 *
 * @author yline 2019/11/27 -- 17:28
 */
public class FaceSDKConfig {
    public static void resetTracker(FaceTracker tracker, FaceSDKConfig config) {
        if (null == tracker || null == config) {
            return;
        }

        tracker.set_isFineAlign(config.isFineAlign);
        tracker.set_isVerifyLive(config.isVerifyLive);
        tracker.set_DetectMethodType(config.detectMethodType);

        tracker.set_isCheckQuality(config.isCheckFaceQuality);
        tracker.set_notFace_thr(config.notFaceValue);
        tracker.set_min_face_size(config.minFaceSize);
        tracker.set_cropFaceSize(config.cropFaceValue);
        tracker.set_illum_thr(config.brightnessValue);
        tracker.set_blur_thr(config.blurnessValue);
        tracker.set_occlu_thr(config.occlusionValue);
        tracker.set_isVerifyLive(config.isVerifyLive);
        tracker.set_max_reg_img_num(config.maxCropImageNum);
        tracker.set_eulur_angle_thr(
                config.headPitchValue,
                config.headYawValue,
                config.headRollValue
        );

        tracker.set_track_by_detection_interval(config.trackByDetectionInterval);

        FaceSDK.setNumberOfThreads(config.faceDecodeNumberOfThreads);
    }

    // 新增的参数
    private boolean isFineAlign = false;
    private int detectMethodType = 1;
    private int trackByDetectionInterval = 800;

    // 人脸检测参数
    private float brightnessValue = 40f; // 图像光照阀值
    private float blurnessValue = 0.5f; // 图像模糊阀值

    private float occlusionValue = 0.5f; // 图像中人脸遮挡阀值
    private int headPitchValue = 10; // 图像中人脸抬头低头角度阀值
    private int headYawValue = 10; // 图像中人脸左右角度阀值
    private int headRollValue = 10; // 图像中人脸偏头阀值

    private int cropFaceValue = 400; // 裁剪图像中人脸时的大小
    private int minFaceSize = 200; // 图像能被检测出人脸的最小人脸值

    private float notFaceValue = 0.6f; // 图像能被检测出人脸阀值
    private int maxCropImageNum = 1; // 人脸采集图片数量阀值

    private boolean isCheckFaceQuality = true; // 是否进行人脸图片质量检测
    private boolean isVerifyLive = true; // 是否进行活体检测

    private int faceDecodeNumberOfThreads = 2; // 人脸检测时开启的线程数，建议为CPU核数

    public int getHeadPitchValue() {
        return headPitchValue;
    }

    public int getHeadYawValue() {
        return headYawValue;
    }
}
