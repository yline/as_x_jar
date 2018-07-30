package com.google.zxing.client.android.encode;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.google.zxing.WriterException;
import com.google.zxing.client.android.FinishListener;
import com.google.zxing.client.android.Intents;
import com.google.zxing.client.android.R;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.yline.application.SDKManager;
import com.yline.base.BaseActivity;
import com.yline.utils.UIScreenUtil;

public final class EncodeActivity extends BaseActivity {
	private static final String TAG = EncodeActivity.class.getSimpleName();
	
	private static final String USE_VCARD_KEY = "USE_VCARD";
	
	private QRCodeEncoder qrCodeEncoder;
	
	public static void launch(Context context) {
		if (null != context) {
			Intent intent = new Intent();
			intent.setClass(context, EncodeActivity.class);
			if (!(context instanceof Activity)) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent);
		}
	}
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		Intent intent = getIntent();
		if (intent == null) {
			finish();
		} else {
			setContentView(R.layout.encode);
			
			initView();
		}
	}
	
	private void initView() {
		int screenWidth = UIScreenUtil.getScreenWidth(SDKManager.getApplication());
		int smallerDimension = screenWidth * 7 / 8;
		
		boolean useVCard = getIntent().getBooleanExtra(USE_VCARD_KEY, false);
		try {
			qrCodeEncoder = new QRCodeEncoder(this, getIntent(), smallerDimension, useVCard);
			Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
			
			if (bitmap == null) {
				Log.w(TAG, "Could not encode barcode");
				showErrorMessage(R.string.msg_encode_contents_failed);
				qrCodeEncoder = null;
				return;
			}
			
			ImageView view = (ImageView) findViewById(R.id.image_view);
			view.setImageBitmap(bitmap);
			
			TextView contents = (TextView) findViewById(R.id.contents_text_view);
			contents.setText(qrCodeEncoder.getDisplayContents());
			
			setTitle(qrCodeEncoder.getTitle());
		} catch (WriterException e) {
			Log.w(TAG, "Could not encode barcode", e);
			showErrorMessage(R.string.msg_encode_contents_failed);
			qrCodeEncoder = null;
		}
	}

	private void showErrorMessage(int message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message);
		builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
		builder.setOnCancelListener(new FinishListener(this));
		builder.show();
	}
}
