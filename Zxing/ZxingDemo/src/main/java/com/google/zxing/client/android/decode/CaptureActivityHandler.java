package com.google.zxing.client.android.decode;

import android.graphics.BitmapFactory;

import com.google.zxing.Result;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.client.android.R;
import com.google.zxing.client.android.camera.CameraManager;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * decode 和 camera 的协调工作；辅助activity的存在
 *
 * @author yline
 * @times 2018/8/1 -- 15:02
 */
public final class CaptureActivityHandler extends Handler {
	private final OnDecodeCallback decodeCallback;
	
	private final DecodeThread decodeThread;
	
	private State state;
	
	public CaptureActivityHandler(OnDecodeCallback decodeCallback, ResultPointCallback resultPointCallback) {
		this.decodeCallback = decodeCallback;
		decodeThread = new DecodeThread(this, resultPointCallback);
		decodeThread.start();
		state = State.SUCCESS;
		
		// Start ourselves capturing previews and decoding.
		CameraManager.getInstance().startPreview();
		restartPreviewAndDecode();
	}
	
	@Override
	public void handleMessage(Message message) {
		switch (message.what) {
			case R.id.restart_preview:
				restartPreviewAndDecode();
				break;
			case R.id.decode_succeeded:
				state = State.SUCCESS;
				Bundle bundle = message.getData();
				Bitmap barcode = null;
				float scaleFactor = 1.0f;
				if (bundle != null) {
					byte[] compressedBitmap = bundle.getByteArray(DecodeThread.BARCODE_BITMAP);
					if (compressedBitmap != null) {
						barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
						// Mutable copy:
						barcode = barcode.copy(Bitmap.Config.ARGB_8888, true);
					}
					scaleFactor = bundle.getFloat(DecodeThread.BARCODE_SCALED_FACTOR);
				}
				
				Result rawResult = (Result) message.obj;
				decodeCallback.onHandleDecode(rawResult, barcode, scaleFactor);
				break;
			case R.id.decode_failed:
				// We're decoding as fast as possible, so when one decode fails, start another.
				state = State.PREVIEW;
				CameraManager.getInstance().requestPreviewFrame(decodeThread.getmDecodeHandler(), R.id.decode);
				break;
		}
	}
	
	public void quitSynchronously() {
		state = State.DONE;
		CameraManager.getInstance().stopPreview();
		Message quit = Message.obtain(decodeThread.getmDecodeHandler(), R.id.quit);
		quit.sendToTarget();
		try {
			// Wait at most half a second; should be enough time, and onPause() will timeout quickly
			decodeThread.join(500L);
		} catch (InterruptedException e) {
			// continue
		}
		
		// Be absolutely sure we don't send any queued up messages
		removeMessages(R.id.decode_succeeded);
		removeMessages(R.id.decode_failed);
	}
	
	private void restartPreviewAndDecode() {
		if (state == State.SUCCESS) {
			state = State.PREVIEW;
			CameraManager.getInstance().requestPreviewFrame(decodeThread.getmDecodeHandler(), R.id.decode);
			decodeCallback.onRestartPreview();
		}
	}
	
	private enum State {
		PREVIEW, SUCCESS, DONE
	}
}
