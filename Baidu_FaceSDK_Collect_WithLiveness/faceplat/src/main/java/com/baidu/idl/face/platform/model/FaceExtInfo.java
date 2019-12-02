package com.baidu.idl.face.platform.model;

import android.graphics.Rect;

import com.baidu.idl.facesdk.FaceInfo;

/**
 * 人脸数据对象
 */
public class FaceExtInfo {
    private int mWidth;
    private int mAngle;
    private int mCenter_y;
    private int mCenter_x;
    private float mConf;
    public int[] landmarks;
    private int face_id;
    private float[] headPose; // { 低头仰头角度、侧脸、平面内旋转
    private int[] is_live;

    public FaceExtInfo() {
    }

    public FaceExtInfo(FaceInfo info) {
        this.mWidth = info.mWidth;
        this.mAngle = info.mAngle;
        this.mCenter_y = info.mCenter_y;
        this.mCenter_x = info.mCenter_x;
        this.mConf = info.mConf;
        this.landmarks = info.landmarks;
        this.face_id = info.face_id;
        this.headPose = info.headPose;
        this.is_live = info.is_live;
    }

    public int getFaceId() {
        return face_id;
    }

    public int[] getIs_live() {
        return is_live;
    }

    // 人脸区域
    public Rect getFaceRect() {
        Rect rect = new Rect(mCenter_x - mWidth / 2, mCenter_y - mWidth / 2,
                mWidth, mWidth);
        return rect;
    }

    // 人脸宽度
    public int getFaceWidth() {
        return mWidth;
    }

    /**
     * @return 低头仰头角度
     */
    public float getPitch() {
        try {
            return headPose[0];
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * @return 侧脸
     */
    public float getYaw() {
        try {
            return headPose[1];
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * @return 平面内旋转
     */
    public float getRoll() {
        try {
            return headPose[2];
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * @return 置信度
     */
    public float getConfidence() {
        return mConf;
    }

    /**
     * @return 取得活体状态
     */
    public int[] getLiveInfo() {
        return is_live;
    }

}
