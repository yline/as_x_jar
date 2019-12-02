package com.baidu.idl.face.platform.utils;

import android.graphics.Rect;

import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.model.FaceExtInfo;
import com.baidu.idl.face.platform.model.FaceStatusEnum;
import com.baidu.idl.face.platform.model.LivenessTypeEnum;

/**
 * 脸部信息处理工具
 *
 * @author yline 2019/11/28 -- 10:40
 */
public class FaceExtInfoUtil {
    /**
     * @param detectRect 界面上的方框
     * @param extInfo    脸信息
     * @return true，人脸太近
     */
    public static boolean isZoomIn(Rect detectRect, FaceExtInfo extInfo) {
        int faceWidth = extInfo.getFaceWidth();
        return faceWidth > detectRect.width();
    }

    /**
     * @param detectRect 界面上的方框
     * @param extInfo    脸信息
     * @return true，人脸太远
     */
    public static boolean isZoomOut(Rect detectRect, FaceExtInfo extInfo) {
        int faceWidth = extInfo.getFaceWidth();
        return faceWidth < (detectRect.width() * 0.4f);
    }

    /**
     * 人脸数是否过高
     *
     * @param detectRect 界面上的方框
     * @param extInfo    脸信息
     * @return true 人脸数太多
     */
    public static boolean isFaceCountMuch(Rect detectRect, FaceExtInfo extInfo) {
        int[] landMarks = extInfo.landmarks;
        if (null == landMarks) {
            return false;
        }
        int faceCount = getLandmarksOutOfDetectCount(landMarks, detectRect);
        return faceCount > 10;
    }

    /**
     * 检查头部角度是否超过阈值
     *
     * @param typeEnum 当前检查的类别
     * @param extInfo  脸部信息
     * @return null 未超过
     */
    public static FaceStatusEnum checkHeadValid(LivenessTypeEnum typeEnum, FaceExtInfo extInfo) {
        if (null == typeEnum || null == extInfo) {
            return null;
        }

        float headPitch = extInfo.getPitch();
        int headPitchValue = FaceSDKManager.getInstance().getFaceSDKConfig().getHeadPitchValue();

        // 建议略微抬头
        if (headPitch > headPitchValue && (typeEnum != LivenessTypeEnum.Liveness_HeadDown)) {
            return FaceStatusEnum.Detect_PitchOutOfDownMaxRange;
        }

        // 建议略微低头
        if (headPitch < -headPitchValue && (typeEnum != LivenessTypeEnum.Liveness_HeadUp)) {
            return FaceStatusEnum.Detect_PitchOutOfUpMaxRange;
        }

        float headYaw = extInfo.getYaw();
        int headYawValue = FaceSDKManager.getInstance().getFaceSDKConfig().getHeadYawValue();

        // 建议略微向右转头
        if (headYaw > headYawValue && (typeEnum != LivenessTypeEnum.Liveness_HeadLeft) && (typeEnum != LivenessTypeEnum.Liveness_HeadLeftOrRight)) {
            return FaceStatusEnum.Detect_PitchOutOfLeftMaxRange;
        }

        // 建议略微向左转头
        if (headYaw < -headYawValue && (typeEnum != LivenessTypeEnum.Liveness_HeadRight) && (typeEnum != LivenessTypeEnum.Liveness_HeadLeftOrRight)) {
            return FaceStatusEnum.Detect_PitchOutOfRightMaxRange;
        }

        return null;
    }

    /**
     * 当前的脸部信息，和，当前的动作是否匹配
     *
     * @param typeEnum 动作类型
     * @param extInfo  脸部信息
     * @return true 匹配
     */
    public static boolean isLivenessMatch(LivenessTypeEnum typeEnum, FaceExtInfo extInfo) {
        switch (typeEnum) {
            case Liveness_Eye:
                return isLiveEye(extInfo);
            case Liveness_Mouth:
                return isLiveMouth(extInfo);
            case Liveness_HeadUp:
                return isLiveHeadUp(extInfo);
            case Liveness_HeadDown:
                return isLiveHeadDown(extInfo);
            case Liveness_HeadLeft:
                return isLiveHeadTurnLeft(extInfo);
            case Liveness_HeadRight:
                return isLiveHeadTurnRight(extInfo);
            case Liveness_HeadLeftOrRight:
                return isLiveHeadTurnLeftOrRight(extInfo);
        }
        return false;
    }

    /**
     * @param extInfo 脸部信息
     * @return true 在运动
     */
    private static boolean isLiveEye(FaceExtInfo extInfo) {
        return (null != extInfo && null != extInfo.getIs_live() && 11 == extInfo.getIs_live().length
                && extInfo.getIs_live()[0] == 1);
    }

    /**
     * @param extInfo 脸部信息
     * @return true 在运动
     */
    private static boolean isLiveMouth(FaceExtInfo extInfo) {
        return (null != extInfo && null != extInfo.getIs_live() && 11 == extInfo.getIs_live().length
                && extInfo.getIs_live()[3] == 1);
    }

    /**
     * @param extInfo 脸部信息
     * @return true 在运动
     */
    private static boolean isLiveHeadTurnLeft(FaceExtInfo extInfo) {
        return (null != extInfo && null != extInfo.getIs_live() && 11 == extInfo.getIs_live().length
                && extInfo.getIs_live()[5] == 1);
    }

    /**
     * @param extInfo 脸部信息
     * @return true 在运动
     */
    private static boolean isLiveHeadTurnRight(FaceExtInfo extInfo) {
        return (null != extInfo && null != extInfo.getIs_live() && 11 == extInfo.getIs_live().length
                && extInfo.getIs_live()[6] == 1);
    }

    /**
     * @param extInfo 脸部信息
     * @return true 在运动
     */
    private static boolean isLiveHeadTurnLeftOrRight(FaceExtInfo extInfo) {
        return (null != extInfo && null != extInfo.getIs_live() && 11 == extInfo.getIs_live().length
                && (extInfo.getIs_live()[5] == 1 || extInfo.getIs_live()[6] == 1));
    }

    /**
     * @param extInfo 脸部信息
     * @return true 在运动
     */
    private static boolean isLiveHeadUp(FaceExtInfo extInfo) {
        return (null != extInfo && null != extInfo.getIs_live() && 11 == extInfo.getIs_live().length
                && extInfo.getIs_live()[8] == 1);
    }

    /**
     * @param extInfo 脸部信息
     * @return true 在运动
     */
    private static boolean isLiveHeadDown(FaceExtInfo extInfo) {
        return (null != extInfo && null != extInfo.getIs_live() && 11 == extInfo.getIs_live().length
                && extInfo.getIs_live()[9] == 1);
    }

    /**
     * @param extInfo 脸部信息
     * @return true 在运动
     */
    public static int getLeftEyeState(FaceExtInfo extInfo) {
        return (null != extInfo && null != extInfo.getIs_live() && 11 == extInfo.getIs_live().length)
                ? extInfo.getIs_live()[1] : 0;
    }

    /**
     * @param extInfo 脸部信息
     * @return true 在运动
     */
    public static int getRightEyeState(FaceExtInfo extInfo) {
        return (null != extInfo && null != extInfo.getIs_live() && 11 == extInfo.getIs_live().length)
                ? extInfo.getIs_live()[2] : 0;
    }

    /**
     * @param extInfo 脸部信息
     * @return true 在运动
     */
    public static int getMouthState(FaceExtInfo extInfo) {
        return (null != extInfo && null != extInfo.getIs_live() && 11 == extInfo.getIs_live().length)
                ? extInfo.getIs_live()[4] : 0;
    }

    // 取得人脸在跟踪框外的关键点数量
    private final static int nComponents = 9;
    private final static int[] comp1 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    private final static int[] comp2 = {13, 14, 15, 16, 17, 18, 19, 20, 13, 21};
    private final static int[] comp3 = {22, 23, 24, 25, 26, 27, 28, 29, 22};
    private final static int[] comp4 = {30, 31, 32, 33, 34, 35, 36, 37, 30, 38};
    private final static int[] comp5 = {39, 40, 41, 42, 43, 44, 45, 46, 39};
    private final static int[] comp6 = {47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 47};
    private final static int[] comp7 = {51, 57, 52};
    private final static int[] comp8 = {58, 59, 60, 61, 62, 63, 64, 65, 58};
    private final static int[] comp9 = {58, 66, 67, 68, 62, 69, 70, 71, 58};
    private final static int[] nPoints = {13, 10, 9, 10, 9, 11, 3, 9, 9};

    private static int getLandmarksOutOfDetectCount(int[] landmarks, Rect detectRect) {
        float ratioX = 1;
        float ratioY = 1;
        int outCount = 0;
        if (landmarks.length == 144) {
            int[][] idx = {comp1, comp2, comp3, comp4, comp5, comp6, comp7, comp8, comp9};
            float[] positionArr = new float[4];
            for (int i = 0; i < nComponents; ++i) {
                for (int j = 0; j < nPoints[i] - 1; ++j) {
                    positionArr[0] = landmarks[idx[i][j] << 1];
                    positionArr[1] = landmarks[1 + (idx[i][j] << 1)];
                    positionArr[2] = landmarks[idx[i][j + 1] << 1];
                    positionArr[3] = landmarks[1 + (idx[i][j + 1] << 1)];

                    if (!detectRect.contains((int) (positionArr[0] * ratioX), (int) (positionArr[1] * ratioY))) {
                        outCount++;
                    }
                    if (!detectRect.contains((int) (positionArr[2] * ratioX), (int) (positionArr[3] * ratioY))) {
                        outCount++;
                    }
                }
            }
        }
        return outCount;
    }
}
