package com.google.zxing.client.android;

import android.graphics.BitmapFactory;

import com.google.zxing.Result;
import com.google.zxing.client.android.camera.CameraManager;
import com.zxing.demo.manager.LogManager;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * This class handles all the messaging which comprises the state machine for activity_capture.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class CaptureActivityHandler extends Handler {
	private final CaptureActivity activity;
	
	private final DecodeThread decodeThread;
	
	private State state;
	
	private final CameraManager cameraManager;
	
	private enum State {
		PREVIEW, SUCCESS, DONE
	}
	
	CaptureActivityHandler(CaptureActivity activity, CameraManager cameraManager, ViewfinderView viewfinderView) {
		this.activity = activity;
		decodeThread = new DecodeThread(cameraManager, new ViewfinderResultPointCallback(viewfinderView), this);
		decodeThread.start();
		state = State.SUCCESS;
		
		// Start ourselves capturing previews and decoding.
		this.cameraManager = cameraManager;
		cameraManager.startPreview();
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
				LogManager.printResult(rawResult);
				activity.handleDecode(rawResult, barcode, scaleFactor);
				break;
			case R.id.decode_failed:
				// We're decoding as fast as possible, so when one decode fails, start another.
				state = State.PREVIEW;
				cameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
				break;
		}
	}
	
	public void quitSynchronously() {
		state = State.DONE;
		cameraManager.stopPreview();
		Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
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
			cameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
			activity.drawViewfinder();
		}
	}
	
}
