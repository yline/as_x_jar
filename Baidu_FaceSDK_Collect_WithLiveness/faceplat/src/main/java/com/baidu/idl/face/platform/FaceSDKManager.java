package com.baidu.idl.face.platform;

import android.annotation.SuppressLint;
import android.content.Context;

import com.baidu.aip.face.stat.Ast;
import com.baidu.idl.face.platform.config.FaceFlatConfig;
import com.baidu.idl.face.platform.config.FaceSDKConfig;
import com.baidu.idl.face.platform.injector.DefaultSDKInjector;
import com.baidu.idl.face.platform.injector.IFaceSDKInjector;
import com.baidu.idl.face.platform.model.FaceStatusEnum;
import com.baidu.idl.face.platform.model.LivenessTypeEnum;
import com.baidu.idl.face.platform.strategy.FaceDetectStrategy;
import com.baidu.idl.face.platform.strategy.FaceLivenessStrategy;
import com.baidu.idl.facesdk.FaceSDK;
import com.baidu.idl.facesdk.FaceTracker;

import java.util.List;

/**
 * FaceSDK功能接口
 */
public class FaceSDKManager {
    @SuppressLint("StaticFieldLeak")
    private static FaceSDKManager mInstance = null;

    private Context mContext;
    private FaceTracker mFaceTracker;
    private IFaceSDKInjector mInjector;
    private FaceSDKConfig mSDKConfig;

    private FaceSDKManager() {
    }

    public static FaceSDKManager getInstance() {
        if (mInstance == null) {
            synchronized (FaceSDKManager.class) {
                if (mInstance == null) {
                    mInstance = new FaceSDKManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化方法
     *
     * @param context  上下文
     * @param injector 依赖上层的方法
     */
    public void init(Context context, String licenseId, String licenseFileName, IFaceSDKInjector injector) {
        mContext = context.getApplicationContext();
        mInjector = injector;

        FaceSDK.initLicense(context.getApplicationContext(), licenseId, licenseFileName, true);
        Ast.getInstance().init(context.getApplicationContext(), "3.3.0.0", "facenormal");
    }

    /**
     * 离线采集
     *
     * @return 策略类
     */
    public FaceDetectStrategy getDetectStrategy() {
        return new FaceDetectStrategy(mContext, getFaceTracker(mContext));
    }

    /**
     * 活体检测
     *
     * @return 策略类
     */
    public FaceLivenessStrategy getLivenessStrategy() {
        return new FaceLivenessStrategy(mContext, getFaceTracker(mContext));
    }

    /**
     * 证书初始化是否成功
     *
     * @return true 成功
     */
    public boolean isLicenseSuccess() {
        return FaceSDK.getAuthorityStatus() == 0;
    }

    /**
     * 释放资源
     */
    public void release() {
        synchronized (FaceSDKManager.class) {
            Ast.getInstance().immediatelyUpload();
            mFaceTracker = null;
            mContext = null;
            mSDKConfig = null;
        }
    }

    /**
     * 不可能为空，人为保证
     *
     * @return 上层实现的方法
     */
    public IFaceSDKInjector getInjector() {
        if (null == mInjector) {
            mInjector = new DefaultSDKInjector();
        }
        return mInjector;
    }

    private FaceTracker getFaceTracker(Context context) {
        if (null == mFaceTracker) {
            mFaceTracker = new FaceTracker(context);
            setFaceSDKConfig(new FaceSDKConfig());
        }
        return mFaceTracker;
    }

    public void setFaceSDKConfig(FaceSDKConfig sdkConfig) {
        if (null == sdkConfig || null == mFaceTracker) {
            return;
        }
        mSDKConfig = sdkConfig;
        FaceSDKConfig.resetTracker(mFaceTracker, mSDKConfig);
    }

    public FaceSDKConfig getFaceSDKConfig() {
        if (null == mSDKConfig) {
            mSDKConfig = new FaceSDKConfig();
        }
        return mSDKConfig;
    }

    /* -------------------------------- 转接的api ----------------------------------- */

    /**
     * 获取SDK当前版本
     *
     * @return SDK版本号
     */
    public String getVersion() {
        return FaceFlatConfig.getVersion();
    }

    public void putSoundId(Object status, int soundId) {
        FaceFlatConfig.putSoundId(status, soundId);
    }

    public int getSoundId(Object status) {
        return FaceFlatConfig.getSoundId(status);
    }

    public void putTipsId(Object status, int tipsId) {
        FaceFlatConfig.putTipsId(status, tipsId);
    }

    public int getTipsId(Object status) {
        return FaceFlatConfig.getTipsId(status);
    }

    public boolean isSound() {
        return FaceFlatConfig.isSound();
    }

    public void setIsSound(boolean sound) {
        FaceFlatConfig.setIsSound(sound);
    }

    public boolean isIsLivenessTypeRandom() {
        return FaceFlatConfig.isIsLivenessTypeRandom();
    }

    public void setIsLivenessTypeRandom(boolean isLivenessTypeRandom) {
        FaceFlatConfig.setIsLivenessTypeRandom(isLivenessTypeRandom);
    }

    public List<LivenessTypeEnum> getLivenessTypeList() {
        return FaceFlatConfig.getLivenessTypeList();
    }

    public void setLivenessTypeList(List<LivenessTypeEnum> typeList) {
        FaceFlatConfig.setLivenessTypeList(typeList);
    }
}
