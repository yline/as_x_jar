package com.baidu.idl.face.platform.ui;

import android.content.Context;

import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.injector.IFaceSDKInjector;
import com.baidu.idl.face.platform.model.FaceStatusEnum;
import com.baidu.idl.face.platform.model.LivenessTypeEnum;

public class FaceUIManager {
    private static String licenseID = "kjt-facepay-191112-face-android";
    private static String licenseFileName = "idl-license.face-android";

    public static void init(Context context, IFaceSDKInjector injector) {
        FaceSDKManager.getInstance().init(context, licenseID, licenseFileName, injector);
        initializeResId();
    }

    private static void initializeResId() {
        // Sound Res Id
        FaceSDKManager.getInstance().putSoundId(FaceStatusEnum.Detect_NoFace, R.raw.faceui_detect_face_in); // 把脸移入框内
        FaceSDKManager.getInstance().putSoundId(FaceStatusEnum.Detect_FacePointOut, R.raw.faceui_detect_face_in);
        FaceSDKManager.getInstance().putSoundId(FaceStatusEnum.Detect_OK, R.raw.faceui_face_good); // 德玲

        FaceSDKManager.getInstance().putSoundId(LivenessTypeEnum.Liveness_Eye, R.raw.faceui_liveness_eye); // 眨眨眼
        FaceSDKManager.getInstance().putSoundId(LivenessTypeEnum.Liveness_Mouth, R.raw.faceui_liveness_mouth); // 张张嘴
        FaceSDKManager.getInstance().putSoundId(LivenessTypeEnum.Liveness_HeadUp, R.raw.faceui_liveness_head_up); // 缓慢抬头
        FaceSDKManager.getInstance().putSoundId(LivenessTypeEnum.Liveness_HeadDown, R.raw.faceui_liveness_head_down); // 缓慢低头
        FaceSDKManager.getInstance().putSoundId(LivenessTypeEnum.Liveness_HeadLeft, R.raw.faceui_liveness_head_left); // 向左转头
        FaceSDKManager.getInstance().putSoundId(LivenessTypeEnum.Liveness_HeadRight, R.raw.faceui_liveness_head_right); // 向右转头
        FaceSDKManager.getInstance().putSoundId(LivenessTypeEnum.Liveness_HeadLeftOrRight, R.raw.faceui_liveness_head_left_right); // 摇摇头

        // Tips Res Id
        FaceSDKManager.getInstance().putTipsId(FaceStatusEnum.Detect_NoFace, R.string.faceui_detect_no_face);
        FaceSDKManager.getInstance().putTipsId(FaceStatusEnum.Detect_PoorIllumintion, R.string.faceui_detect_low_light);
        FaceSDKManager.getInstance().putTipsId(FaceStatusEnum.Detect_ImageBlured, R.string.faceui_detect_keep);
        FaceSDKManager.getInstance().putTipsId(FaceStatusEnum.Detect_OccLeftEye, R.string.faceui_detect_occ_face);
        FaceSDKManager.getInstance().putTipsId(FaceStatusEnum.Detect_OccRightEye, R.string.faceui_detect_occ_face);
        FaceSDKManager.getInstance().putTipsId(FaceStatusEnum.Detect_OccNose, R.string.faceui_detect_occ_face);
        FaceSDKManager.getInstance().putTipsId(FaceStatusEnum.Detect_OccMouth, R.string.faceui_detect_occ_face);
        FaceSDKManager.getInstance().putTipsId(FaceStatusEnum.Detect_OccLeftContour, R.string.faceui_detect_occ_face);
        FaceSDKManager.getInstance().putTipsId(FaceStatusEnum.Detect_OccRightContour, R.string.faceui_detect_occ_face);
        FaceSDKManager.getInstance().putTipsId(FaceStatusEnum.Detect_OccChin, R.string.faceui_detect_occ_face);
        FaceSDKManager.getInstance().putTipsId(FaceStatusEnum.Detect_PitchOutOfUpMaxRange, R.string.faceui_detect_head_down);
        FaceSDKManager.getInstance().putTipsId(FaceStatusEnum.Detect_PitchOutOfDownMaxRange, R.string.faceui_detect_head_up);
        FaceSDKManager.getInstance().putTipsId(FaceStatusEnum.Detect_PitchOutOfLeftMaxRange, R.string.faceui_detect_head_right);
        FaceSDKManager.getInstance().putTipsId(FaceStatusEnum.Detect_PitchOutOfRightMaxRange, R.string.faceui_detect_head_left);

        FaceSDKManager.getInstance().putTipsId(FaceStatusEnum.Detect_FacePointOut, R.string.faceui_detect_face_in);
        FaceSDKManager.getInstance().putTipsId(FaceStatusEnum.Detect_FaceZoomIn, R.string.faceui_detect_zoom_in);
        FaceSDKManager.getInstance().putTipsId(FaceStatusEnum.Detect_FaceZoomOut, R.string.faceui_detect_zoom_out);
        FaceSDKManager.getInstance().putTipsId(FaceStatusEnum.Detect_OK, R.string.faceui_liveness_good);
        FaceSDKManager.getInstance().putTipsId(FaceStatusEnum.Detect_Timeout, R.string.faceui_detect_timeout);

        FaceSDKManager.getInstance().putTipsId(LivenessTypeEnum.Liveness_Eye, R.string.faceui_liveness_eye);
        FaceSDKManager.getInstance().putTipsId(LivenessTypeEnum.Liveness_Mouth, R.string.faceui_liveness_mouth);
        FaceSDKManager.getInstance().putTipsId(LivenessTypeEnum.Liveness_HeadUp, R.string.faceui_liveness_head_up);
        FaceSDKManager.getInstance().putTipsId(LivenessTypeEnum.Liveness_HeadDown, R.string.faceui_liveness_head_down);
        FaceSDKManager.getInstance().putTipsId(LivenessTypeEnum.Liveness_HeadLeft, R.string.faceui_liveness_head_left);
        FaceSDKManager.getInstance().putTipsId(LivenessTypeEnum.Liveness_HeadRight, R.string.faceui_liveness_head_right);
        FaceSDKManager.getInstance().putTipsId(LivenessTypeEnum.Liveness_HeadLeftOrRight, R.string.faceui_liveness_head_left_right);
    }
}
