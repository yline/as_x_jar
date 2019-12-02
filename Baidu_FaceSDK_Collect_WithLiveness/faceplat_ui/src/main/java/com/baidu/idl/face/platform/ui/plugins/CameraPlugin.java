package com.baidu.idl.face.platform.ui.plugins;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import com.baidu.idl.face.platform.injector.LogManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 相机管理、实现
 * 1，startPreview
 * 2，stopPreview
 *
 * @author yline 2019/11/19 -- 15:04
 */
public class CameraPlugin implements Camera.ErrorCallback, Camera.PreviewCallback {
    private Camera mCamera = null;
    private int mCameraId = 0;
    private Camera.Parameters mCameraParam;

    private int mPreviewDegree = 0; // 预览摄像头角度
    private int mPreviewWidth; // 预览摄像头，图片宽度
    private int mPreviewHeight; // 预览摄像头，图片高度

    private OnCameraCallback mCameraCallback;

    public void setOnCameraCallback(OnCameraCallback cameraCallback) {
        this.mCameraCallback = cameraCallback;
    }

    public interface OnCameraCallback {
        /**
         * Camera初始化完成
         */
        void onStartPreviewFinish(int previewDegree, int previewWidth, int previewHeight);

        /**
         * Camera预览状态，系统会不断调用这个
         */
        void onPreviewFrame(byte[] data, Camera camera, int previewDegree, int previewWidth, int previewHeight);
    }

    @Override
    public void onError(int error, Camera camera) {
        LogManager.e("error = " + error);
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (null != mCameraCallback) {
            mCameraCallback.onPreviewFrame(data, camera, mPreviewDegree, mPreviewWidth, mPreviewHeight);
        }
    }

    public void startPreview(Context context, SurfaceHolder surfaceHolder, int displayWidth, int displayHeight) {
        Camera camera = getCameraInner();

        // 出现了异常 或 没有获取到摄像头
        if (null == camera) {
            return;
        }

        // 设置CameraParam
        if (null == mCameraParam) {
            mCameraParam = camera.getParameters();
        }
        mCameraParam.setPictureFormat(PixelFormat.JPEG);

        int degree = displayOrientation(context, mCameraId);
        camera.setDisplayOrientation(degree); // 设置后无效，camera.setDisplayOrientation方法有效

        mCameraParam.set("rotation", degree); // 预览效果
        mPreviewDegree = degree;

        Point point = getBestPreview(mCameraParam, new Point(displayWidth, displayHeight));
        mPreviewWidth = point.x;
        mPreviewHeight = point.y;

        mCameraParam.setPreviewSize(mPreviewWidth, mPreviewHeight);
        camera.setParameters(mCameraParam);

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
            camera.setErrorCallback(this);
            camera.setPreviewCallback(this);
            camera.startPreview();

            if (null != mCameraCallback) {
                mCameraCallback.onStartPreviewFinish(mPreviewDegree, mPreviewWidth, mPreviewHeight);
            }
        } catch (Throwable tr) {
            LogManager.e(android.util.Log.getStackTraceString(tr));
            releaseCameraInner();
        }
    }

    public void stopPreview() {
        if (null != mCamera) {
            try {
                mCamera.setErrorCallback(null);
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();

                mCameraCallback = null;
            } catch (Throwable tr) {
                tr.printStackTrace();
            } finally {
                releaseCameraInner();
            }
        }
    }

    /**
     * 获取前置摄像头；
     *
     * @return Camera对象
     */
    private Camera getCameraInner() {
        if (null == mCamera) {
            try {
                int numCamera = Camera.getNumberOfCameras();
                if (0 == numCamera) {
                    return null;
                }

                // 获取到当前摄像头的方向，以及对应的信息
                int index = 0;
                while (index < numCamera) {
                    Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                    Camera.getCameraInfo(index, cameraInfo);
                    if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        break;
                    }
                    index++;
                }

                if (index < numCamera) {
                    mCamera = Camera.open(index);
                    mCameraId = index;
                } else {
                    mCamera = Camera.open(0);
                    mCameraId = 0;
                }
            } catch (Throwable tr) {
                LogManager.e(android.util.Log.getStackTraceString(tr));
            }
        }
        return mCamera;
    }

    private void releaseCameraInner() {
        if (null != mCamera) {
            try {
                mCamera.release();
                mCamera = null;
                mCameraParam = null;
            } catch (Throwable tr) {
                tr.printStackTrace();
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private int displayOrientation(Context context, int cameraId) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int rotation = windowManager.getDefaultDisplay().getRotation();
        int degree;
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 0;
                break;
            case Surface.ROTATION_90:
                degree = 90;
                break;
            case Surface.ROTATION_180:
                degree = 180;
                break;
            case Surface.ROTATION_270:
                degree = 270;
                break;
            default:
                degree = 0;
                break;
        }

        int result = (0 - degree + 360) % 360;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraId, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                result = (info.orientation + degree) % 360;
                result = (0 - result + 360) % 360;
            } else {
                result = (info.orientation - degree + 360) % 360;
            }
        }
        return result;
    }

    private static final int MIN_PREVIEW_PIXELS = 640 * 480;
    private static final int MAX_PREVIEW_PIXELS = 1280 * 720;

    /**
     * 获取当前最佳的预览大小
     *
     * @param parameters       参数
     * @param screenResolution 屏幕参数
     * @return 预览参数
     */
    private static Point getBestPreview(Camera.Parameters parameters, Point screenResolution) {
        List<Camera.Size> rawSupportedSizes = parameters.getSupportedPreviewSizes();
        if (rawSupportedSizes == null) {
            Camera.Size defaultSize = parameters.getPreviewSize();
            return new Point(defaultSize.width, defaultSize.height);
        }

        List<Camera.Size> supportedPictureSizes = new ArrayList<Camera.Size>(rawSupportedSizes);
        Collections.sort(supportedPictureSizes, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size a, Camera.Size b) {
                int aPixels = a.height * a.width;
                int bPixels = b.height * b.width;
                if (aPixels == bPixels) {
                    return 0;
                }
                return aPixels > bPixels ? 1 : -1;
            }
        });

        final double screenAspectRatio = (screenResolution.x > screenResolution.y) ?
                ((double) screenResolution.x / (double) screenResolution.y) :
                ((double) screenResolution.y / (double) screenResolution.x);

        Camera.Size selectedSize = null;
        double selectedMinus = -1;
        for (Camera.Size previewSize : supportedPictureSizes) {
            int realSize = previewSize.width * previewSize.height;
            if (realSize >= MIN_PREVIEW_PIXELS && realSize <= MAX_PREVIEW_PIXELS) {
                double aRatio = (previewSize.width > previewSize.height) ?
                        ((double) previewSize.width / (double) previewSize.height) :
                        ((double) previewSize.height / (double) previewSize.width);
                double minus = Math.abs(aRatio - screenAspectRatio);

                boolean selectedFlag = false;
                if ((selectedMinus == -1 && minus <= 0.25f) || (selectedMinus >= minus && minus <= 0.25f)) {
                    selectedFlag = true;
                }

                if (selectedFlag) {
                    selectedMinus = minus;
                    selectedSize = previewSize;
                }
            }
        }

        if (selectedSize != null) {
            return new Point(selectedSize.width, selectedSize.height);
        } else {
            Camera.Size defaultSize = parameters.getPreviewSize();
            return new Point(defaultSize.width, defaultSize.height);
        }
    }


}
