package com.baidu.idl.face.platform.utils;

import android.content.Context;
import android.util.TypedValue;

import com.baidu.idl.face.platform.model.FaceExtInfo;
import com.baidu.idl.face.platform.model.FaceStatusEnum;
import com.baidu.idl.facesdk.FaceInfo;
import com.baidu.idl.facesdk.FaceTracker;

/**
 * 单位转换工具类
 *
 * @author yline 2019/11/28 -- 10:20
 */
public class FaceUnitUtil {
    /**
     * 将 FaceTracker 的错误码，转换到，内部定义的扫脸状态
     *
     * @param errCode 错误码
     * @return 扫脸状态
     */
    public static FaceStatusEnum trackerErrorCode2FaceStatusEnum(FaceTracker.ErrCode errCode) {
        switch (errCode) {
            case OK:
                return FaceStatusEnum.Detect_OK;
            case PITCH_OUT_OF_DOWN_MAX_RANGE:
                return FaceStatusEnum.Detect_PitchOutOfDownMaxRange;
            case PITCH_OUT_OF_UP_MAX_RANGE:
                return FaceStatusEnum.Detect_PitchOutOfUpMaxRange;
            case YAW_OUT_OF_LEFT_MAX_RANGE:
                return FaceStatusEnum.Detect_PitchOutOfLeftMaxRange;
            case YAW_OUT_OF_RIGHT_MAX_RANGE:
                return FaceStatusEnum.Detect_PitchOutOfRightMaxRange;
            case POOR_ILLUMINATION:
                return FaceStatusEnum.Detect_PoorIllumintion;
            case NO_FACE_DETECTED:
                return FaceStatusEnum.Detect_NoFace;
            case DATA_NOT_READY:
                return FaceStatusEnum.Detect_DataNotReady;
            case DATA_HIT_ONE:
                return FaceStatusEnum.Detect_DataHitOne;
            case DATA_HIT_LAST:
                return FaceStatusEnum.Detect_DataHitLast;
            case IMG_BLURED:
                return FaceStatusEnum.Detect_ImageBlured;
            case OCCLUSION_LEFT_EYE:
                return FaceStatusEnum.Detect_OccLeftEye;
            case OCCLUSION_RIGHT_EYE:
                return FaceStatusEnum.Detect_OccRightEye;
            case OCCLUSION_NOSE:
                return FaceStatusEnum.Detect_OccNose;
            case OCCLUSION_MOUTH:
                return FaceStatusEnum.Detect_OccMouth;
            case OCCLUSION_LEFT_CONTOUR:
                return FaceStatusEnum.Detect_OccLeftContour;
            case OCCLUSION_RIGHT_CONTOUR:
                return FaceStatusEnum.Detect_OccRightContour;
            case OCCLUSION_CHIN_CONTOUR:
                return FaceStatusEnum.Detect_OccChin;
            case FACE_NOT_COMPLETE:
                return FaceStatusEnum.Detect_FaceNotComplete;
            case UNKNOW_TYPE:
                return FaceStatusEnum.Detect_NoFace;
            default:
                return FaceStatusEnum.Detect_NoFace;
        }
    }

    /**
     * @param faceInfoArray SDK内部的数据结构
     * @return 自定义的数据结构
     */
    public static FaceExtInfo[] faceInfo2FaceExtInfo(FaceInfo[] faceInfoArray) {
        if (null == faceInfoArray || faceInfoArray.length == 0) {
            return null;
        }

        FaceExtInfo[] extInfoArray = new FaceExtInfo[faceInfoArray.length];
        for (int i = 0; i < faceInfoArray.length; i++) {
            extInfoArray[i] = new FaceExtInfo(faceInfoArray[i]);
        }
        return extInfoArray;
    }

    /**
     * dp to px
     *
     * @param context 上下文
     * @param dpValue dp
     * @return px
     */
    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue,
                context.getResources().getDisplayMetrics());
    }
}
