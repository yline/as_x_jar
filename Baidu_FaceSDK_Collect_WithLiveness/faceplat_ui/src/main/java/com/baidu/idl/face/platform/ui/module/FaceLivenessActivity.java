package com.baidu.idl.face.platform.ui.module;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;

import com.baidu.aip.face.stat.Ast;
import com.baidu.idl.face.platform.model.LivenessTypeEnum;
import com.baidu.idl.face.platform.ui.R;
import com.baidu.idl.face.platform.ui.plugins.CameraPlugin;
import com.baidu.idl.face.platform.ui.utils.FaceStorageUtils;
import com.baidu.idl.face.platform.ui.utils.FaceUIScreenUtil;
import com.baidu.idl.face.platform.ui.view.FaceDetectView;
import com.baidu.idl.face.platform.receiver.FaceVolumeReceiver;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.injector.LogManager;
import com.baidu.idl.face.platform.model.FaceStatusEnum;
import com.baidu.idl.face.platform.OnLivenessStrategyCallback;
import com.baidu.idl.face.platform.ui.view.FaceDetectRoundView;
import com.baidu.idl.face.platform.strategy.FaceLivenessStrategy;
import com.baidu.idl.face.platform.ui.view.KjtDialog;
import com.baidu.idl.face.platform.utils.FaceBitmapUtils;

import java.util.Map;

/**
 * 活体检测接口
 */
public class FaceLivenessActivity extends Activity implements
        FaceVolumeReceiver.VolumeCallback,
        OnLivenessStrategyCallback {
    public static void launch(Context context) {
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, FaceLivenessActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    // 人脸信息
    private FaceLivenessStrategy mLivenessStrategy;
    // 显示Size
    private Rect mPreviewRect = new Rect();
    private int mDisplayWidth = 0;
    private int mDisplayHeight = 0;

    // 状态标识
    private boolean mIsEnableSound = true;
    private boolean mIsCompletion = false;
    // 监听系统音量广播
    private FaceVolumeReceiver mVolumeReceiver;

    // 相机管理插件
    private CameraPlugin mCameraPlugin;

    // View管理
    private FaceDetectView mFaceDetectView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.faceui_activity_face_liveness);

        LogManager.v("----------------------detect start----------------------");

        mCameraPlugin = new CameraPlugin();
        mFaceDetectView = findViewById(R.id.face_liveness_view);

        mDisplayWidth = FaceUIScreenUtil.getScreenWidth(this);
        mDisplayHeight = FaceUIScreenUtil.getScreenHeight(this);

        mIsEnableSound = FaceVolumeReceiver.isAudioSound(this) && FaceSDKManager.getInstance().isSound();
        mFaceDetectView.updateSoundView(mIsEnableSound);
        mFaceDetectView.setOnSoundClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsEnableSound = !mIsEnableSound;

                FaceSDKManager.getInstance().setIsSound(mIsEnableSound);
                mFaceDetectView.updateSoundView(mIsEnableSound);
            }
        });
        mFaceDetectView.setOnDetectCallback(new FaceDetectView.OnDetectCallback() {
            @Override
            public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                startPreview();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mVolumeReceiver = FaceVolumeReceiver.registerReceiver(this);
        mVolumeReceiver.setOnVolumeVolumeCallback(this);

        mFaceDetectView.setTopTips(R.string.faceui_detect_face_in);
        startPreview();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopPreview();
    }

    @Override
    public void onStop() {
        if (mLivenessStrategy != null) {
            mLivenessStrategy.reset();
        }
        FaceVolumeReceiver.unRegisterReceiver(this, mVolumeReceiver);
        mVolumeReceiver = null;
        super.onStop();
        stopPreview();
    }

    @Override
    public void onVolumeChanged() {
        mIsEnableSound = FaceVolumeReceiver.isAudioSound(this);
        mFaceDetectView.updateSoundView(mIsEnableSound);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogManager.v("--------------------detect end-----------------------");
    }

    protected void startPreview() {
        LogManager.v("start preview");

        SurfaceHolder surfaceHolder = mFaceDetectView.checkSurfaceHolder();

        mCameraPlugin.startPreview(this, surfaceHolder, mDisplayWidth, mDisplayHeight);
        mCameraPlugin.setOnCameraCallback(new CameraPlugin.OnCameraCallback() {
            @Override
            public void onStartPreviewFinish(int previewDegree, int previewWidth, int previewHeight) {
                mPreviewRect.set(0, 0, previewHeight, previewWidth);

                if (null == mLivenessStrategy) {
                    mLivenessStrategy = FaceSDKManager.getInstance().getLivenessStrategy();

                    Rect detectRect = FaceDetectRoundView.getPreviewDetectRect(mDisplayWidth, previewHeight, previewWidth);
                    mLivenessStrategy.setDetectStrategyConfig(mPreviewRect, detectRect);
                    mLivenessStrategy.setLivenessList(FaceSDKManager.getInstance().getLivenessTypeList());
                    mLivenessStrategy.setOnDetectStrategyCallback(FaceLivenessActivity.this);
                }

                mLivenessStrategy.setPreviewDegree(previewDegree); // 更新角度
            }

            @Override
            public void onPreviewFrame(byte[] data, Camera camera, int previewDegree, int previewWidth, int previewHeight) {
                if (mIsCompletion) {
                    return;
                }

                if (null != mLivenessStrategy) {
                    mLivenessStrategy.livenessStrategy(data);

                    // todo 保存图片测试 delete
                    saveBytes(data, camera);
                }
            }
        });
    }

    protected void stopPreview() {
        LogManager.v("stop preview");

        mCameraPlugin.stopPreview();
        mFaceDetectView.release();

        if (mLivenessStrategy != null) {
            mLivenessStrategy = null;
        }
    }

    @Override
    public void onDetecting(FaceStatusEnum status, LivenessTypeEnum typeEnum, String message) {
        if (mIsCompletion) {
            return;
        }

        Ast.getInstance().faceHit("liveness");
        mFaceDetectView.showActionResult(message);
    }

    private long mLastSaveTime;

    /**
     * 保存图片,1s一次
     *
     * @param data 内容
     */
    private void saveBytes(byte[] data, Camera camera) {
        if (System.currentTimeMillis() - mLastSaveTime > 2000) {
            byte[] imageBytes = FaceBitmapUtils.preFrame2RowImage(data, camera);

            String path = FaceStorageUtils.getPath(FaceLivenessActivity.this);
            String savePath = FaceStorageUtils.bytes2Path(path, imageBytes);

            LogManager.v("saveBytes finish, savePath = " + savePath);
            mLastSaveTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onDetectSuccess(FaceStatusEnum status, String message, Map<String, Bitmap> resultMap) {
        if (mIsCompletion) {
            return;
        }

        mIsCompletion = true;
        mFaceDetectView.setImageResultList(resultMap);
        Ast.getInstance().faceHit("liveness");

        showMessageDialog("活体检测", "检测成功");
    }

    @Override
    public void onDetectFailed(FaceStatusEnum status, String message) {
        if (mIsCompletion) {
            return;
        }

        mIsCompletion = true;

        mFaceDetectView.showOKResult(message);
        Ast.getInstance().faceHit("liveness");
        showMessageDialog("活体检测", "采集超时");
    }

    private void showMessageDialog(String title, String message) {
        KjtDialog.showDialog(this, title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // finish();
            }
        });
    }
}
