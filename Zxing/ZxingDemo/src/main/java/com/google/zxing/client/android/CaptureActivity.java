package com.google.zxing.client.android;

import android.content.Context;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.camera.CameraManager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;
import com.google.zxing.client.result.ResultParser;
import com.yline.application.SDKManager;
import com.yline.base.BaseActivity;
import com.yline.utils.SPUtil;
import com.zxing.demo.capture.CaptureResultView;
import com.zxing.demo.manager.DBManager;
import com.zxing.demo.manager.LogManager;

import java.util.Map;

/**
 * This activity opens the camera and does the actual scanning on a background thread. It draws a
 * viewfinder to help the user place the barcode correctly, shows feedback as the image processing
 * is happening, and then overlays the results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CaptureActivity extends BaseActivity implements CaptureFragment.OnResultCallback {
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
	
	private static final long BULK_MODE_SCAN_DELAY_MS = 1000L;
	
	private CaptureResultView mCaptureResultView;
	private TextView mStatusView;
	private Result lastResult;
	
	private CaptureFragment mCaptureFragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_capture);
		
		mCaptureResultView = findViewById(R.id.capture_result_view);
		mStatusView = findViewById(R.id.capture_status_view);
		
		mCaptureFragment = (CaptureFragment) getFragmentManager().findFragmentById(R.id.capture_fragment);
		mCaptureFragment.setOnResultCallback(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		lastResult = null;
		if (getOrientation()) {
			setRequestedOrientation(getCurrentOrientation(this));
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		}
		
		resetStatusView();
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
			case KeyEvent.KEYCODE_VOLUME_DOWN: // 音量下键，关闭灯光
				CameraManager.getInstance().setTorch(false);
				return true;
			case KeyEvent.KEYCODE_VOLUME_UP: // 音量上键，打开灯光
				CameraManager.getInstance().setTorch(true);
				return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onDecodeResult(Result rawResult, Bitmap barcode, float scaleFactor) {
		LogManager.printResult(rawResult);
		
		lastResult = rawResult;
		
		ParsedResult parsedResult = ResultParser.parseResult(rawResult);
		boolean fromLiveScan = (barcode != null);
		if (fromLiveScan) {
			DBManager.getInstance().addHistoryItem(rawResult, parsedResult);
			drawResultPoints(barcode, scaleFactor, rawResult);
		}
		
		if (fromLiveScan && isBulkMode()) {
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_bulk_mode_scanned) + " (" + rawResult.getText() + ')', Toast.LENGTH_SHORT).show();
			// Wait a moment or else it will scan the same barcode continuously about 3 times
			restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);
		} else {
			handleDecodeInternally(rawResult, parsedResult, barcode);
		}
	}
	
	private boolean isBulkMode(){
		return false;
	}
	
	public boolean getOrientation() {
		return true;
	}
	
	// Put up our own UI for how to handle the decoded contents.
	private void handleDecodeInternally(Result rawResult, ParsedResult parsedResult, Bitmap barcode) {
		mStatusView.setVisibility(View.GONE);
		mCaptureFragment.setViewFinderVisibility(View.GONE);
		
		BarcodeFormat format = rawResult.getBarcodeFormat();
		ParsedResultType resultType = parsedResult.getType();
		Map<ResultMetadataType, Object> metadata = rawResult.getResultMetadata();
		
		String encodeContent = parsedResult.getDisplayResult();
		
		mCaptureResultView.setVisibility(View.VISIBLE);
		mCaptureResultView.setData(barcode, format, resultType, rawResult.getTimestamp(), metadata, encodeContent);
	}
	
	public void restartPreviewAfterDelay(long delayMS) {
		mCaptureFragment.restartPreviewAfterDelay(delayMS);
		resetStatusView();
	}
	
	private void resetStatusView() {
		mCaptureResultView.setVisibility(View.GONE);
		mStatusView.setText(R.string.msg_default_status);
		mStatusView.setVisibility(View.VISIBLE);
		
		mCaptureFragment.setViewFinderVisibility(View.VISIBLE);
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
	
	/**
	 * 绘制二维码的 四个点 或一维码的两个点
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
}
