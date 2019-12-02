package com.baidu.idl.face.platform.strategy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.config.FaceFlatConfig;
import com.baidu.idl.face.platform.manager.FaceDecodeManager;
import com.baidu.idl.face.platform.model.FaceStatusEnum;
import com.baidu.idl.face.platform.OnLivenessStrategyCallback;
import com.baidu.idl.face.platform.model.LivenessTypeEnum;
import com.baidu.idl.face.platform.model.FaceExtInfo;
import com.baidu.idl.face.platform.manager.FaceSoundManager;
import com.baidu.idl.face.platform.utils.FaceBitmapUtils;
import com.baidu.idl.face.platform.injector.LogManager;
import com.baidu.idl.facesdk.FaceTracker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 活体检测策略控制类
 */
public final class FaceLivenessStrategy {
    private volatile boolean mIsFirstDetect = true; // 是否是首次检测
    private volatile boolean mIsDetecting; // 是否正在检测中

    private volatile long mDetectStartTime = 0; // 检测开始时间
    private final long DETECT_MAX_TIME; // 单次检测的最大时间

    private volatile long mDetectNoFaceTime = 0; // 检测结果为无脸，的上次时间
    private final long DETECT_NO_MAX_TIME; // 单次检测无脸，的最大时间

    private volatile boolean mIsCompletion = false; // 是否已经完成了检测任务；防止多次成功回调给UI层

    private Context mContext;
    private Rect mPreviewRect;
    private Rect mDetectRect;

    private Map<String, Bitmap> mResultMap = new HashMap<>();

    private OnLivenessStrategyCallback mOnLivenessStrategyCallback;
    private FaceDecodeManager mFaceDecoder;

    private LivenessPlugin mLivenessPlugin;

    public FaceLivenessStrategy(Context context, FaceTracker tracker) {
        mContext = context;

        mFaceDecoder = new FaceDecodeManager(tracker);
        mLivenessPlugin = new LivenessPlugin();

        // 配置信息
        DETECT_MAX_TIME = FaceFlatConfig.getDetectMaxTime();
        DETECT_NO_MAX_TIME = FaceFlatConfig.getDetectNoFaceMaxTime();
    }

    public void setDetectStrategyConfig(Rect previewRect, Rect detectRect) {
        mPreviewRect = previewRect;
        mDetectRect = detectRect;
    }

    public void setLivenessList(List<LivenessTypeEnum> livenessList) {
        mLivenessPlugin.setLivenessList(livenessList);
    }

    public void setOnDetectStrategyCallback(OnLivenessStrategyCallback callback) {
        this.mOnLivenessStrategyCallback = callback;
    }

    public void setPreviewDegree(int degree) {
        LogManager.v("camera degree = " + degree);

        if (mFaceDecoder != null) {
            mFaceDecoder.setPreviewDegree(degree);
        }
    }

    public void livenessStrategy(byte[] imageData) {
        // 首次检测，做一些准备工作
        if (mIsFirstDetect) {
            mIsFirstDetect = false;
            mIsDetecting = true;

            mDetectStartTime = System.currentTimeMillis();
            FaceSoundManager.getInstance().play(mContext, FaceStatusEnum.Detect_FacePointOut);
            processUICallback(FaceStatusEnum.Detect_FacePointOut, null);

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

            LogManager.e("结束检测，mIsDetecting = false");
            return;
        }

        // 检测
        mFaceDecoder.detect(mDetectRect, imageData, mPreviewRect.height(), mPreviewRect.width(), new FaceDecodeManager.OnDecodeResultCallback() {
            @Override
            public void onDetectFace(final FaceExtInfo extInfo, final int[] argbByte, final FaceStatusEnum statusEnum) {
                mDetectNoFaceTime = 0;

                handleFaceInfo(extInfo, argbByte, statusEnum);
            }

            @Override
            public void onErrorWithFace(FaceExtInfo extInfo, FaceStatusEnum statusEnum) {
                mDetectNoFaceTime = 0;

                FaceStatusEnum checkEnum = mLivenessPlugin.checkDetect(extInfo);
                checkEnum = null != checkEnum ? checkEnum : statusEnum;

                FaceSoundManager.getInstance().play(mContext, checkEnum);
                processUICallback(checkEnum, null);
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

    private void handleFaceInfo(FaceExtInfo extInfo, int[] argbByte, FaceStatusEnum statusEnum) {
        // 距离开始时间太短【未超过语音播放时间，避免太快，做一个过滤】
        long restTime = System.currentTimeMillis() - mDetectStartTime;
        if (restTime < 1000) {
            LogManager.e("too fast, restTime = " + restTime);
            return;
        }

        // 处理内容
        LivenessTypeEnum typeEnum = mLivenessPlugin.process(extInfo);
        // 当前状态处理成功
        if (null != typeEnum) {
            // 保存图片
            Bitmap bitmap = FaceBitmapUtils.createBitmap(argbByte, mPreviewRect);
            if (!mResultMap.containsKey(typeEnum.name())) {
                LogManager.v("bitmap is adding");
                mResultMap.put(typeEnum.name(), bitmap);
            } else {
                LogManager.e("bitmap is contained");
            }
        }

        // 判断是否结束
        if (mLivenessPlugin.isLivenessSuccess()) {
            mIsDetecting = false;
            processUICallback(FaceStatusEnum.Detect_OK, null);
            return;
        }

        LivenessTypeEnum currentTypeEnum = mLivenessPlugin.getCurrentLivenessType();
        FaceSoundManager.getInstance().play(mContext, currentTypeEnum);
        processUICallback(null, currentTypeEnum);
    }

    private void processUICallback(FaceStatusEnum statusEnum, LivenessTypeEnum typeEnum) {
        if (mIsCompletion) {
            return;
        }

        String statusText = null != statusEnum ? getStatusTextResId(statusEnum) : getStatusTextResId(typeEnum);
        if (statusEnum == FaceStatusEnum.Detect_OK) {
            mIsDetecting = false;
            mIsCompletion = true;

            LogManager.v("完成检测流程, status = " + statusEnum);
            if (null != mOnLivenessStrategyCallback) {
                mOnLivenessStrategyCallback.onDetectSuccess(statusEnum, statusText, mResultMap);
            }
            return;
        }

        if (statusEnum == FaceStatusEnum.Detect_Timeout) {
            mIsDetecting = false;
            mIsCompletion = true;

            LogManager.v("完成检测流程, status = " + statusEnum);
            if (null != mOnLivenessStrategyCallback) {
                mOnLivenessStrategyCallback.onDetectFailed(statusEnum, statusText);
            }
            return;
        }

        if (null != mOnLivenessStrategyCallback) {
            mOnLivenessStrategyCallback.onDetecting(statusEnum, typeEnum, statusText);
        }
    }

    private String getStatusTextResId(FaceStatusEnum status) {
        int resId = FaceSDKManager.getInstance().getTipsId(status);
        if (resId > 0) {
            return mContext.getString(resId);
        }
        return null;
    }

    private String getStatusTextResId(LivenessTypeEnum typeEnum) {
        int resId = FaceSDKManager.getInstance().getTipsId(typeEnum);
        if (resId > 0) {
            return mContext.getString(resId);
        }
        return null;
    }

    public void reset() {
        LogManager.v("reset!!!");

        mIsFirstDetect = true;

        if (mFaceDecoder != null) {
            mFaceDecoder.reset();
        }

        mLivenessPlugin.reset();
        if (null != mResultMap) {
            mResultMap.clear();
        }
        FaceSoundManager.getInstance().release();
    }
}
