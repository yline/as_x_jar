package com.google.zxing.client.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.Result;
import com.google.zxing.client.android.camera.CameraManager;
import com.google.zxing.client.android.decode.CaptureActivityHandler;
import com.google.zxing.client.android.decode.OnDecodeCallback;
import com.google.zxing.client.android.helper.CodeManager;
import com.google.zxing.client.android.view.ViewfinderResultPointCallback;
import com.google.zxing.client.android.view.ViewfinderView;
import com.google.zxing.client.android.helper.AmbientLightHelper;
import com.google.zxing.client.android.helper.BeepHelper;

import java.io.IOException;

/**
 * 二维码扫描提供使用的 Fragment
 *
 * @author yline
 * @times 2018/8/2 -- 14:48
 */
public class CaptureFragment extends Fragment implements SurfaceHolder.Callback, OnDecodeCallback {
	private static final String TAG = "CaptureFragment";
	
	private SurfaceView mSurfaceView;
	private ViewfinderView mViewfinderView;
	private CaptureActivityHandler mMainHandler;
	
	private BeepHelper mBeepHelper;
	private AmbientLightHelper mAmbientLightHelper;
	
	private boolean hasSurface;
	private OnResultCallback onResultCallback;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_capture, container, false);
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		mSurfaceView = view.findViewById(R.id.fragment_capture_surface);
		mViewfinderView = view.findViewById(R.id.fragment_capture_viewfinder);
		
		mBeepHelper = new BeepHelper(view.getContext());
		mAmbientLightHelper = new AmbientLightHelper();
		
		hasSurface = false;
	}
	
	private boolean isLight = (AmbientLightHelper.getLightMode() == AmbientLightHelper.LightMode.ON);
	
	public void switchAmbientLight() {
		if (isLight){
			CameraManager.getInstance().setTorch(false);
			isLight = false;
		}else {
			CameraManager.getInstance().setTorch(true);
			isLight = true;
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		mMainHandler = null;
		
		mBeepHelper.updatePrefs();
		mAmbientLightHelper.start();
		
		SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
		if (hasSurface) {
			// The activity was paused but not stopped, so the surface still exists. Therefore
			// surfaceCreated() won't be called, so init the camera here.
			initCamera(surfaceHolder);
		} else {
			// Install the callback and wait for surfaceCreated() to init the camera.
			surfaceHolder.addCallback(this);
		}
	}
	
	@Override
	public void onPause() {
		if (mMainHandler != null) {
			mMainHandler.quitSynchronously();
			mMainHandler = null;
		}
		mAmbientLightHelper.stop();
		mBeepHelper.close();
		CameraManager.getInstance().closeDriver();
		if (!hasSurface) {
			SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
			surfaceHolder.removeCallback(this);
		}
		super.onPause();
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) {
			CodeManager.v(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// do nothing
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}
	
	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (CameraManager.getInstance().isOpen()) {
			CodeManager.v(TAG, "initCamera() while already open -- late SurfaceView callback?");
			return;
		}
		try {
			CameraManager.getInstance().openDriver(surfaceHolder);
			// Creating the mMainHandler starts the preview, which can also throw a RuntimeException.
			if (mMainHandler == null) {
				mMainHandler = new CaptureActivityHandler(this, new ViewfinderResultPointCallback(mViewfinderView));
			}
		} catch (IOException ioe) {
			CodeManager.v(TAG, "");
			displayFrameworkBugMessageAndExit();
		} catch (RuntimeException e) {
			// Barcode Scanner has seen crashes in the wild of this variety:
			// java.?lang.?RuntimeException: Fail to connect to camera service
			CodeManager.v(TAG, "Unexpected error initializing camera");
			displayFrameworkBugMessageAndExit();
		}
	}
	
	@Override
	public void onRestartPreview() {
		mViewfinderView.drawViewfinder();
	}
	
	@Override
	public void onHandleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
		if (null != barcode) {
			mBeepHelper.playBeepSoundAndVibrate();
		}
		
		if (null != onResultCallback) {
			onResultCallback.onDecodeResult(rawResult, barcode, scaleFactor);
		}
	}
	
	private void displayFrameworkBugMessageAndExit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage(getString(R.string.msg_camera_framework_bug));
		builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finishActivity();
			}
		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				finishActivity();
			}
		});
		builder.show();
	}
	
	private void finishActivity() {
		Activity activity = getActivity();
		if (null != activity && !activity.isFinishing()) {
			activity.finish();
		}
	}
	
	public void restartPreviewAfterDelay(long delayMS) {
		if (null != mMainHandler) {
			mMainHandler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
		}
	}
	
	public void setViewFinderVisibility(int visibility) {
		if (null != mViewfinderView) {
			mViewfinderView.setVisibility(visibility);
		}
	}
	
	public void setOnResultCallback(OnResultCallback callback) {
		this.onResultCallback = callback;
	}
	
	public interface OnResultCallback {
		/**
		 * A valid barcode has been found, so give an indication of success and show the results.
		 * 扫描成功回调
		 *
		 * @param rawResult   扫描生成的具体的数据（文字、时间等）
		 * @param barcode     扫描得到的图片
		 * @param scaleFactor 生成的图片的 （宽度/原始资料的宽度）
		 */
		void onDecodeResult(Result rawResult, Bitmap barcode, float scaleFactor);
	}
}
