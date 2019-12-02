package com.baidu.idl.face.platform.manager;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;

import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.injector.LogManager;
import com.baidu.idl.face.platform.model.FaceExtInfo;
import com.baidu.idl.face.platform.model.FaceStatusEnum;
import com.baidu.idl.face.platform.utils.FaceExtInfoUtil;
import com.baidu.idl.face.platform.utils.FaceUnitUtil;
import com.baidu.idl.facesdk.FaceInfo;
import com.baidu.idl.facesdk.FaceSDK;
import com.baidu.idl.facesdk.FaceTracker;
import com.baidu.idl.facesdk.FaceVerifyData;

import java.util.ArrayList;
import java.util.List;

/**
 * 人脸跟踪,活体检测功能类
 */
public class FaceDecodeManager {
    // 人脸检测功能接口
    private FaceTracker mFaceTracker;
    private long mDetectStartTime;

    // 视角
    private int mDegree = 90;

    private int[] mArgbData = null; // 检测图像
    private int mImageWidth = 0; // 图像宽度
    private int mImageHeight = 0; // 图像高度

    public FaceDecodeManager(FaceTracker tracker) {
        mFaceTracker = tracker;
        if (mFaceTracker != null) {
            mFaceTracker.clearTrackedFaces();
        }
    }

    public void detect(Rect detectRect, byte[] imageData, int imageWidth, int imageHeight, OnDecodeResultCallback callback) {
        mDetectStartTime = System.currentTimeMillis();

        // 参数异常
        if (null == detectRect || null == imageData || imageWidth <= 0 || imageHeight <= 0) {
            LogManager.e("decode 参数异常，detectRect = " + detectRect + ", imageData = " + (null == imageData ? null : imageData.length) +
                    ", imageWidth = " + imageWidth + ", imageHeight = " + imageHeight);
            return;
        }

        // 还未初始化
        if (!FaceSDKManager.getInstance().isLicenseSuccess()) {
            LogManager.e("sdk 还未初始化");
            return;
        }

        detectInner(detectRect, imageData, imageWidth, imageHeight, callback);
    }

    private void detectInner(Rect detectRect, byte[] imageData, int imageWidth, int imageHeight, OnDecodeResultCallback callback) {
        if (null == mArgbData || imageWidth != mImageWidth || imageHeight != mImageHeight) {
            mArgbData = new int[imageWidth * imageHeight];
            mImageWidth = imageWidth;
            mImageHeight = imageHeight;
        }

        // 获取二维数据
        FaceSDK.getARGBFromYUVimg(imageData, mArgbData, imageWidth, imageHeight, 360 - mDegree, 1);
        // 识别人脸信息
        FaceTracker.ErrCode errorCode = mFaceTracker.faceVerification(mArgbData, imageWidth, imageHeight, FaceSDK.ImgType.ARGB, FaceTracker.ActionType.RECOGNIZE);

        // 识别到的人脸信息
        FaceInfo[] faceArray = mFaceTracker.get_TrackedFaceInfo();

        FaceStatusEnum faceStatusEnum = FaceUnitUtil.trackerErrorCode2FaceStatusEnum(errorCode);
        FaceExtInfo[] faceExtInfoArray = FaceUnitUtil.faceInfo2FaceExtInfo(faceArray);

        LogManager.v("detect, errorCode = " + errorCode + ", faceInfo size = " + (null != faceArray ? faceArray.length : -1)
                + ", cost = " + (System.currentTimeMillis() - mDetectStartTime) + "--------------------------------");

        // 分发信息
        dispatchCallback(detectRect, faceStatusEnum, faceExtInfoArray, mArgbData, callback);
    }

    /**
     * 分发结果
     */
    private void dispatchCallback(Rect detectRect, FaceStatusEnum statusEnum, FaceExtInfo[] faceArray, int[] argbByte, OnDecodeResultCallback callback) {
        // 成功的直接返回
        if (statusEnum == FaceStatusEnum.Detect_OK) {
            if (null != faceArray && faceArray.length > 0 && null != faceArray[0]) {
                dispatchDetectSuccess(faceArray[0], argbByte, statusEnum, callback);
            } else {
                LogManager.e("sdk 内部状态异常");
                dispatchDetectError(FaceStatusEnum.Detect_NoFace, callback);
            }
            return;
        }

        // 无人脸信息时，直接返回对应的错误
        FaceExtInfo faceExtInfo = (null != faceArray && faceArray.length > 0) ? faceArray[0] : null;
        if (null == faceExtInfo) {
            dispatchDetectError(statusEnum, callback);
            return;
        }

        // 有人脸信息时，部分状态不处理
        switch (statusEnum) {
            // 以下这些状态，直接返回
            case Detect_PitchOutOfUpMaxRange:
            case Detect_PitchOutOfDownMaxRange:
            case Detect_PitchOutOfLeftMaxRange:
            case Detect_PitchOutOfRightMaxRange:
            case Detect_PoorIllumintion:
            case Detect_ImageBlured:
            case Detect_OccLeftEye:
            case Detect_OccRightEye:
            case Detect_OccNose:
            case Detect_OccMouth:
            case Detect_OccLeftContour:
            case Detect_OccRightContour:
            case Detect_OccChin:
                dispatchDetectErrorWithFace(faceExtInfo, statusEnum, callback);
                return;
        }

        // 其它状态，Java层再处理一次
        if (FaceExtInfoUtil.isZoomIn(detectRect, faceExtInfo)) {
            dispatchDetectErrorWithFace(faceExtInfo, FaceStatusEnum.Detect_FaceZoomOut, callback);
            return;
        }

        if (FaceExtInfoUtil.isZoomOut(detectRect, faceExtInfo)) {
            dispatchDetectErrorWithFace(faceExtInfo, FaceStatusEnum.Detect_FaceZoomIn, callback);
            return;
        }

        if (FaceExtInfoUtil.isFaceCountMuch(detectRect, faceExtInfo)) {
            dispatchDetectErrorWithFace(faceExtInfo, FaceStatusEnum.Detect_FacePointOut, callback);
            return;
        }

        dispatchDetectErrorWithFace(faceExtInfo, statusEnum, callback);
    }

    private void dispatchDetectSuccess(final FaceExtInfo extInfo, final int[] argbByte, final FaceStatusEnum statusEnum, final OnDecodeResultCallback callback) {
        if (null != callback) {
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    callback.onDetectFace(extInfo, argbByte, statusEnum);
                }
            });
        }
    }

    private void dispatchDetectErrorWithFace(final FaceExtInfo extInfo, final FaceStatusEnum statusEnum, final OnDecodeResultCallback callback) {
        if (null != callback) {
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    callback.onErrorWithFace(extInfo, statusEnum);
                }
            });
        }
    }

    private void dispatchDetectError(final FaceStatusEnum statusEnum, final OnDecodeResultCallback callback) {
        if (null != callback) {
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    callback.onError(statusEnum);
                }
            });
        }
    }

    public void setPreviewDegree(int degree) {
        if (degree >= 0 && degree <= 360) {
            mDegree = degree;
        }
    }

    /**
     * 这个功能有问题，有时候获取的时候就是没有图片
     * PS：保留这个api，表示还有这个功能，下面是支持的。虽然不靠谱
     *
     * @return 结果图片
     */
    public List<Bitmap> getDetectBitmapList() {
        List<Bitmap> bitmapList = new ArrayList<>();
        FaceVerifyData[] verifyDataArray = mFaceTracker.get_FaceVerifyData(0);
        if (null != verifyDataArray && verifyDataArray.length > 0) {
            for (FaceVerifyData verifyData : verifyDataArray) {
                Bitmap image = Bitmap.createBitmap(verifyData.cols, verifyData.rows, Bitmap.Config.ARGB_8888);
                image.setPixels(verifyData.mRegImg, 0, verifyData.cols, 0, 0,
                        verifyData.cols, verifyData.rows);
                bitmapList.add(image);
            }
        }

        LogManager.v("bitmapList size = " + bitmapList.size());
        return bitmapList;
    }

    public void reset() {
        if (mFaceTracker != null) {
            mFaceTracker.re_collect_reg_imgs();
            mFaceTracker.clearTrackedFaces();
        }
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    private Handler mHandler;

    private Handler getHandler() {
        if (null == mHandler) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }

    public interface OnDecodeResultCallback {
        /**
         * 检测到人脸，并且有人脸信息
         *
         * @param extInfo    人脸信息
         * @param argbByte   当前的人脸img
         * @param statusEnum FaceStatusEnum.Detect_OK
         */
        void onDetectFace(FaceExtInfo extInfo, int[] argbByte, FaceStatusEnum statusEnum);

        /**
         * 检测到了人脸信息
         *
         * @param extInfo    人脸信息
         * @param statusEnum 当前状态
         */
        void onErrorWithFace(FaceExtInfo extInfo, FaceStatusEnum statusEnum);

        /**
         * 未检测到人脸信息
         *
         * @param statusEnum 当前状态
         */
        void onError(FaceStatusEnum statusEnum);
    }
}
