package com.google.zxing.client.android;

import android.content.Context;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.camera.CameraManager;
import com.google.zxing.client.android.decode.CaptureActivityHandler;
import com.google.zxing.client.android.decode.OnDecodeCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.view.ViewfinderResultPointCallback;
import com.google.zxing.client.android.view.ViewfinderView;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;
import com.google.zxing.client.result.ResultParser;
import com.yline.base.BaseActivity;
import com.zxing.demo.capture.AmbientLightHelper;
import com.zxing.demo.capture.BeepHelper;
import com.zxing.demo.capture.CaptureResultView;
import com.zxing.demo.manager.DBManager;
import com.zxing.demo.manager.LogManager;

import java.io.IOException;
import java.util.Map;

/**
 * This activity opens the camera and does the actual scanning on a background thread. It draws a
 * viewfinder to help the user place the barcode correctly, shows feedback as the image processing
 * is happening, and then overlays the results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CaptureActivity extends BaseActivity implements SurfaceHolder.Callback, OnDecodeCallback {
	public static void launch(Context context) {
		if (null != context) {
			Intent intent = new Intent();
			intent.setClass(context, CaptureActivity.class);
			if (!(context instanceof Activity)) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent);
		}
	}
	
	private static final String TAG = CaptureActivity.class.getSimpleName();
	
	private static final long BULK_MODE_SCAN_DELAY_MS = 1000L;
	
	private SurfaceView mSurfaceView;
	private ViewfinderView mViewfinderView;
	private CaptureResultView mCaptureResultView;
	private TextView mStatusView;
	
	private BeepHelper mBeepHelper;
	private AmbientLightHelper mAmbientLightHelper;
	
	private CaptureActivityHandler handler;
	
	private Result lastResult;
	
	private boolean hasSurface;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_capture);
		
		mSurfaceView = findViewById(R.id.capture_surface);
		mViewfinderView = findViewById(R.id.capture_viewfinder_view);
		mCaptureResultView = findViewById(R.id.capture_result_view);
		mStatusView = findViewById(R.id.capture_status_view);
		
		hasSurface = false;
		mBeepHelper = new BeepHelper(this);
		mAmbientLightHelper = new AmbientLightHelper();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		handler = null;
		lastResult = null;
		
		if (DBManager.getInstance().getOrientation()) {
			setRequestedOrientation(getCurrentOrientation(this));
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		}
		
		resetStatusView();
		
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
	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) {
			Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// do nothing
	}
	
	@Override
	protected void onPause() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				if (lastResult != null) {
					restartPreviewAfterDelay(0L);
					return true;
				}
				break;
			case KeyEvent.KEYCODE_FOCUS:
			case KeyEvent.KEYCODE_CAMERA:
				// Handle these events so they don't launch the Camera app
				return true;
			// Use volume up/down to turn on light
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				CameraManager.getInstance().setTorch(false);
				return true;
			case KeyEvent.KEYCODE_VOLUME_UP:
				CameraManager.getInstance().setTorch(true);
				return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onRestartPreview() {
		mViewfinderView.drawViewfinder();
	}
	
	@Override
	public void onHandleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
		LogManager.printResult(rawResult);
		
		lastResult = rawResult;
		
		ParsedResult parsedResult = ResultParser.parseResult(rawResult);
		boolean fromLiveScan = (barcode != null);
		if (fromLiveScan) {
			DBManager.getInstance().addHistoryItem(rawResult, parsedResult);
			// Then not from history, so beep/vibrate and we have an image to draw on
			mBeepHelper.playBeepSoundAndVibrate();
			drawResultPoints(barcode, scaleFactor, rawResult);
		}
		
		if (fromLiveScan && DBManager.getInstance().getBulkMode()) {
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_bulk_mode_scanned) + " (" + rawResult.getText() + ')', Toast.LENGTH_SHORT).show();
			// Wait a moment or else it will scan the same barcode continuously about 3 times
			restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);
		} else {
			handleDecodeInternally(rawResult, parsedResult, barcode);
		}
	}
	
	/**
	 * Superimpose a line for 1D or dots for 2D to highlight the key features of the barcode.
	 *
	 * @param barcode     A bitmap of the captured image.
	 * @param scaleFactor amount by which thumbnail was scaled
	 * @param rawResult   The decoded results which contains the points to draw.
	 */
	private void drawResultPoints(Bitmap barcode, float scaleFactor, Result rawResult) {
		ResultPoint[] points = rawResult.getResultPoints();
		if (points != null && points.length > 0) {
			Canvas canvas = new Canvas(barcode);
			Paint paint = new Paint();
			paint.setColor(getResources().getColor(R.color.result_points));
			if (points.length == 2) {
				paint.setStrokeWidth(4.0f);
				drawLine(canvas, paint, points[0], points[1], scaleFactor);
			} else if (points.length == 4 && (rawResult.getBarcodeFormat() == BarcodeFormat.UPC_A || rawResult.getBarcodeFormat() == BarcodeFormat.EAN_13)) {
				// Hacky special case -- draw two lines, for the barcode and metadata
				drawLine(canvas, paint, points[0], points[1], scaleFactor);
				drawLine(canvas, paint, points[2], points[3], scaleFactor);
			} else {
				paint.setStrokeWidth(10.0f);
				for (ResultPoint point : points) {
					if (point != null) {
						canvas.drawPoint(scaleFactor * point.getX(), scaleFactor * point.getY(), paint);
					}
				}
			}
		}
	}
	
	private static void drawLine(Canvas canvas, Paint paint, ResultPoint a, ResultPoint b, float scaleFactor) {
		if (a != null && b != null) {
			canvas.drawLine(scaleFactor * a.getX(), scaleFactor * a.getY(), scaleFactor * b.getX(), scaleFactor * b.getY(), paint);
		}
	}
	
	// Put up our own UI for how to handle the decoded contents.
	private void handleDecodeInternally(Result rawResult, ParsedResult parsedResult, Bitmap barcode) {
		mStatusView.setVisibility(View.GONE);
		mViewfinderView.setVisibility(View.GONE);
		
		BarcodeFormat format = rawResult.getBarcodeFormat();
		ParsedResultType resultType = parsedResult.getType();
		Map<ResultMetadataType, Object> metadata = rawResult.getResultMetadata();
		
		String encodeContent = parsedResult.getDisplayResult();
		
		mCaptureResultView.setVisibility(View.VISIBLE);
		mCaptureResultView.setData(barcode, format, resultType, rawResult.getTimestamp(), metadata, encodeContent);
	}
	
	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (CameraManager.getInstance().isOpen()) {
			Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
			return;
		}
		try {
			CameraManager.getInstance().openDriver(surfaceHolder);
			// Creating the handler starts the preview, which can also throw a RuntimeException.
			if (handler == null) {
				handler = new CaptureActivityHandler(this, new ViewfinderResultPointCallback(mViewfinderView));
			}
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			displayFrameworkBugMessageAndExit();
		} catch (RuntimeException e) {
			// Barcode Scanner has seen crashes in the wild of this variety:
			// java.?lang.?RuntimeException: Fail to connect to camera service
			Log.w(TAG, "Unexpected error initializing camera", e);
			displayFrameworkBugMessageAndExit();
		}
	}
	
	private void displayFrameworkBugMessageAndExit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage(getString(R.string.msg_camera_framework_bug));
		builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				finish();
			}
		});
		builder.show();
	}
	
	public void restartPreviewAfterDelay(long delayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
		}
		resetStatusView();
	}
	
	private void resetStatusView() {
		mCaptureResultView.setVisibility(View.GONE);
		mStatusView.setText(R.string.msg_default_status);
		mStatusView.setVisibility(View.VISIBLE);
		mViewfinderView.setVisibility(View.VISIBLE);
		lastResult = null;
	}
	
	private static int getCurrentOrientation(Activity activity) {
		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
		if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			switch (rotation) {
				case Surface.ROTATION_0:
				case Surface.ROTATION_90:
					return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
				default:
					return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
			}
		} else {
			switch (rotation) {
				case Surface.ROTATION_0:
				case Surface.ROTATION_270:
					return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
				default:
					return ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
			}
		}
	}
}
