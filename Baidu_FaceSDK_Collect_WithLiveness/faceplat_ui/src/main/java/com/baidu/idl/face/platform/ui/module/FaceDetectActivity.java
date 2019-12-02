package com.baidu.idl.face.platform.ui.module;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;

import com.baidu.aip.face.stat.Ast;
import com.baidu.idl.face.platform.injector.LogManager;
import com.baidu.idl.face.platform.ui.R;
import com.baidu.idl.face.platform.ui.plugins.CameraPlugin;
import com.baidu.idl.face.platform.ui.utils.FaceStorageUtils;
import com.baidu.idl.face.platform.ui.utils.FaceUIScreenUtil;
import com.baidu.idl.face.platform.ui.view.FaceDetectView;
import com.baidu.idl.face.platform.receiver.FaceVolumeReceiver;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.model.FaceStatusEnum;
import com.baidu.idl.face.platform.OnDetectStrategyCallback;
import com.baidu.idl.face.platform.ui.view.FaceDetectRoundView;
import com.baidu.idl.face.platform.strategy.FaceDetectStrategy;
import com.baidu.idl.face.platform.ui.view.KjtDialog;
import com.baidu.idl.face.platform.utils.FaceBitmapUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 人脸采集接口
 */
public class FaceDetectActivity extends Activity implements FaceVolumeReceiver.VolumeCallback, OnDetectStrategyCallback {
    public static void launch(Context context) {
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, FaceDetectActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    // 显示Size
    private Rect mPreviewRect = new Rect();
    private int mDisplayWidth = 0;
    private int mDisplayHeight = 0;
    // 状态标识
    private boolean mIsEnableSound = true; // 声音标志

    private volatile boolean mIsCompletion = false;
    // 监听系统音量广播
    private FaceVolumeReceiver mVolumeReceiver;

    // 相机管理
    private CameraPlugin mCameraPlugins;

    // View管理
    private FaceDetectView mFaceDetectView;

    // 人脸信息
    private FaceDetectStrategy mDetectStrategy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.faceui_activity_face_detect);

        LogManager.v("----------------------detect start----------------------");

        mCameraPlugins = new CameraPlugin();
        mFaceDetectView = findViewById(R.id.face_detect_view);

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
        super.onStop();
        FaceVolumeReceiver.unRegisterReceiver(this, mVolumeReceiver);
        if (mDetectStrategy != null) {
            mDetectStrategy.reset();
        }
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

        mCameraPlugins.startPreview(this, surfaceHolder, mDisplayWidth, mDisplayHeight);
        mCameraPlugins.setOnCameraCallback(new CameraPlugin.OnCameraCallback() {
            @Override
            public void onStartPreviewFinish(int previewDegree, int previewWidth, int previewHeight) {
                mPreviewRect.set(0, 0, previewHeight, previewWidth);

                if (null == mDetectStrategy) {
                    mDetectStrategy = FaceSDKManager.getInstance().getDetectStrategy();

                    Rect detectRect = FaceDetectRoundView.getPreviewDetectRect(mDisplayWidth, previewHeight, previewWidth);
                    mDetectStrategy.setDetectStrategyConfig(mPreviewRect, detectRect);
                    mDetectStrategy.setOnDetectStrategyCallback(FaceDetectActivity.this);
                }

                mDetectStrategy.setPreviewDegree(previewDegree); // 更新角度
            }

            @Override
            public void onPreviewFrame(byte[] data, Camera camera, int previewDegree, int previewWidth, int previewHeight) {
                if (mIsCompletion) {
                    return;
                }

                if (null != mDetectStrategy) {
                    mDetectStrategy.detectStrategy(data);

                    // todo 保存图片测试 delete
                    saveBytes(data, camera);
                }
            }
        });
    }

    protected void stopPreview() {
        LogManager.v("stop preview");

        mCameraPlugins.stopPreview();
        mFaceDetectView.release();

        if (mDetectStrategy != null) {
            mDetectStrategy = null;
        }
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

            String path = FaceStorageUtils.getPath(FaceDetectActivity.this);
            String savePath = FaceStorageUtils.bytes2Path(path, imageBytes);

            LogManager.v("saveBytes finish, savePath = " + savePath);
            mLastSaveTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onDetecting(FaceStatusEnum status, String message) {
        if (mIsCompletion) {
            return;
        }

        Ast.getInstance().faceHit("detect");
        mFaceDetectView.showDefaultResult(message);
    }

    @Override
    public void onDetectSuccess(FaceStatusEnum status, String message, Bitmap bitmap) {
        if (mIsCompletion) {
            return;
        }

        mIsCompletion = true;
        Ast.getInstance().faceHit("detect");

        mFaceDetectView.showOKResult(message);
        showMessageDialog("人脸图像采集", "采集成功");

        List<Bitmap> bitmapList = new ArrayList<>();
        if (null != bitmap) {
            bitmapList.add(bitmap);
        }
        mFaceDetectView.setImageResultList(bitmapList);
    }

    @Override
    public void onDetectFailed(FaceStatusEnum status, String message) {
        if (mIsCompletion) {
            return;
        }

        mIsCompletion = true;
        Ast.getInstance().faceHit("detect");
        showMessageDialog("人脸图像采集", "采集超时");
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
