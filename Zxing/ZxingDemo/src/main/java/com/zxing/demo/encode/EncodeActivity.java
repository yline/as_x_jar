package com.zxing.demo.encode;

import android.app.Activity;
import android.content.Context;

import com.google.zxing.client.android.FinishListener;
import com.google.zxing.client.android.R;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.client.android.encode.EncoderManager;
import com.yline.application.SDKManager;
import com.yline.base.BaseActivity;
import com.yline.utils.LogUtil;
import com.yline.utils.UIScreenUtil;

public final class EncodeActivity extends BaseActivity {
	private static final String DATA = "encode_data";
	
	public static void launch(Context context, String encodeData) {
		if (null != context) {
			Intent intent = new Intent();
			intent.setClass(context, EncodeActivity.class);
			intent.putExtra(DATA, encodeData);
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
			setContentView(R.layout.activity_encode);
			
			initView();
		}
	}
	
	private void initView() {
		String encodeData = getIntent().getStringExtra(DATA);
		
		int screenWidth = UIScreenUtil.getScreenWidth(SDKManager.getApplication());
		int smallerDimension = screenWidth * 7 / 8;
		
		Bitmap bitmap = EncoderManager.encodeAsQRCodeBitmap(encodeData, smallerDimension);
		
		if (bitmap == null) {
			LogUtil.v("Could not activity_encode barcode");
			showErrorMessage("无法生成条码。");
			return;
		}
		
		ImageView view = (ImageView) findViewById(R.id.encode_image_view);
		view.setImageBitmap(bitmap);
		
		TextView contents = (TextView) findViewById(R.id.encode_contents_text_view);
		contents.setText(encodeData);
		
		setTitle("纯文本");
	}
	
	private void showErrorMessage(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message);
		builder.setPositiveButton("确定", new FinishListener(this));
		builder.setOnCancelListener(new FinishListener(this));
		builder.show();
	}
}
