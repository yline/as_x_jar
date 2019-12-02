package com.baidu.idl.face.platform.strategy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.config.FaceFlatConfig;
import com.baidu.idl.face.platform.manager.FaceDecodeManager;
import com.baidu.idl.face.platform.model.FaceStatusEnum;
import com.baidu.idl.face.platform.OnDetectStrategyCallback;
import com.baidu.idl.face.platform.model.FaceExtInfo;
import com.baidu.idl.face.platform.injector.LogManager;
import com.baidu.idl.face.platform.manager.FaceSoundManager;
import com.baidu.idl.face.platform.utils.FaceBitmapUtils;
import com.baidu.idl.facesdk.FaceTracker;

import java.util.HashMap;

/**
 * 人脸跟踪策略控制类
 */
public final class FaceDetectStrategy {
    private volatile boolean mIsFirstDetect = true; // 是否是首次检测
    private volatile boolean mIsDetecting; // 是否正在检测中

    private volatile long mDetectStartTime = 0; // 检测开始时间
    private final long DETECT_MAX_TIME; // 单次检测的最大时间

    private volatile long mDetectNoFaceTime = 0; // 检测结果为无脸，的上次时间
    private final long DETECT_NO_MAX_TIME; // 单次检测无脸，的最大时间

    private volatile boolean mIsCompletion = false; // 是否已经完成了检测任务；防止多次成功回调给UI层

    private FaceDecodeManager mFaceDecoder;

    private Context mContext;
    private Rect mPreviewRect;
    private Rect mDetectRect;

    private HashMap<String, String> mBase64ImageMap = new HashMap<>();
    private HashMap<FaceStatusEnum, String> mTipsMap = new HashMap<>();
    private OnDetectStrategyCallback mOnDetectStrategyCallback;

    public FaceDetectStrategy(Context context, FaceTracker tracker) {
        mContext = context;

        mFaceDecoder = new FaceDecodeManager(tracker);

        // 配置信息
        DETECT_MAX_TIME = FaceFlatConfig.getDetectMaxTime();
        DETECT_NO_MAX_TIME = FaceFlatConfig.getDetectNoFaceMaxTime();
    }

    public void setDetectStrategyConfig(Rect previewRect, Rect detectRect) {
        mPreviewRect = previewRect;
        mDetectRect = detectRect;
    }

    public void setOnDetectStrategyCallback(OnDetectStrategyCallback callback) {
        this.mOnDetectStrategyCallback = callback;
    }

    public void setPreviewDegree(int degree) {
        LogManager.v("camera degree = " + degree);

        if (mFaceDecoder != null) {
            mFaceDecoder.setPreviewDegree(degree);
        }
    }

    public void detectStrategy(byte[] imageData) {
        // 首次检测，做一些准备工作
        if (mIsFirstDetect) {
            mIsFirstDetect = false;
            mIsDetecting = true;

            mDetectStartTime = System.currentTimeMillis();
            FaceSoundManager.getInstance().play(mContext, FaceStatusEnum.Detect_FacePointOut);

            LogManager.v("首次检测，mDetectStartTime = " + mDetectStartTime);
            return;
        }

        // 结束了检测
        if (!mIsDetecting) {
            LogManager.e("结束检测，mIsDetecting = false");
            return;
        }

        // 单次最大检测时间是否起效、是否超过最大单次检测时间
        if (DETECT_MAX_TIME != 0 && System.currentTimeMillis() - mDetectStartTime > DETECT_MAX_TIME) {
            mIsDetecting = false;
            processUICallback(FaceStatusEnum.Detect_Timeout, null);

            LogManager.e("结束检测，Error_Timeout");
            return;
        }

        // 检测
        mFaceDecoder.detect(mDetectRect, imageData, mPreviewRect.height(), mPreviewRect.width(), new FaceDecodeManager.OnDecodeResultCallback() {
            @Override
            public void onDetectFace(final FaceExtInfo extInfo, int[] argbByte, final FaceStatusEnum statusEnum) {
                mDetectNoFaceTime = 0;
                mIsDetecting = false;

                FaceSoundManager.getInstance().play(mContext, FaceStatusEnum.Detect_OK);
                processUICallback(FaceStatusEnum.Detect_OK, FaceBitmapUtils.createBitmap(argbByte, mPreviewRect));
            }

            @Override
            public void onErrorWithFace(FaceExtInfo extInfo, FaceStatusEnum statusEnum) {
                FaceSoundManager.getInstance().play(mContext, statusEnum);
                processUICallback(statusEnum, null);
            }

            @Override
            public void onError(final FaceStatusEnum statusEnum) {
                if (statusEnum == FaceStatusEnum.Detect_NoFace && DETECT_NO_MAX_TIME != 0) {
                    if (mDetectNoFaceTime == 0) {
                        mDetectNoFaceTime = System.currentTimeMillis();
                    } else if (System.currentTimeMillis() - mDetectNoFaceTime > DETECT_NO_MAX_TIME) {
                        processUICallback(FaceStatusEnum.Detect_Timeout, null);
                        mIsDetecting = false;
                        return;
                    }
                } else {
                    mDetectNoFaceTime = 0;
                }

                FaceSoundManager.getInstance().play(mContext, FaceStatusEnum.Detect_NoFace);
                processUICallback(FaceStatusEnum.Detect_NoFace, null);
            }
        });
    }

    private void processUICallback(FaceStatusEnum status, Bitmap bitmap) {
        if (mIsCompletion) {
            return;
        }

        String statusText = getStatusTextResId(status);
        if (status == FaceStatusEnum.Detect_OK) {
            mIsCompletion = true;
            LogManager.v("完成检测流程, status = " + status);

            if (null != mOnDetectStrategyCallback) {
                mOnDetectStrategyCallback.onDetectSuccess(status, statusText, bitmap);
            }
            return;
        }

        if (status == FaceStatusEnum.Detect_Timeout) {
            mIsCompletion = true;
            LogManager.v("完成检测流程, status = " + status);

            if (null != mOnDetectStrategyCallback) {
                mOnDetectStrategyCallback.onDetectFailed(status, statusText);
            }
            return;
        }

        if (null != mOnDetectStrategyCallback) {
            mOnDetectStrategyCallback.onDetecting(status, statusText);
        }
    }

    private String getStatusTextResId(FaceStatusEnum status) {
        int resId = FaceSDKManager.getInstance().getTipsId(status);
        if (resId > 0) {
            return mContext.getString(resId);
        }
        return null;
    }

    public void reset() {
        LogManager.v("reset!!!");
        mIsFirstDetect = true;
        mIsCompletion = false;

        if (mFaceDecoder != null) {
            mFaceDecoder.reset();
        }
        FaceSoundManager.getInstance().release();
    }
}
