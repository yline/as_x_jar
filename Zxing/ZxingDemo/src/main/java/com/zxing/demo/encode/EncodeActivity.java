package com.zxing.demo.encode;

import android.app.Activity;
import android.content.Context;

import com.google.zxing.client.android.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.client.android.helper.CodeManager;
import com.yline.application.SDKManager;
import com.yline.base.BaseActivity;
import com.yline.utils.LogUtil;
import com.yline.utils.UIScreenUtil;

public final class EncodeActivity extends BaseActivity {
	private static final String ONECODE_DATA = "onecode_data";
	private static final String QRCODE_DATA = "qrcode_data";
	
	public static void launch(Context context, String oneCodeData, String qrcodeData) {
		if (null != context) {
			Intent intent = new Intent();
			intent.setClass(context, EncodeActivity.class);
			intent.putExtra(ONECODE_DATA, oneCodeData);
			intent.putExtra(QRCODE_DATA, qrcodeData);
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
			
			String onecodeData = intent.getStringExtra(ONECODE_DATA);
			String qrcodeData = intent.getStringExtra(QRCODE_DATA);
			
			initOneCodeView(onecodeData);
			initQrcodeView(qrcodeData);
		}
	}
	
	private void initOneCodeView(String onecodeData) {
		int screenWidth = UIScreenUtil.getScreenWidth(SDKManager.getApplication());
		int smallerDimension = screenWidth * 7 / 8;
		
		Bitmap bitmap = CodeManager.encodeAsOneCodeBitmap(onecodeData, smallerDimension, smallerDimension / 2);
		if (bitmap == null) {
			LogUtil.v("Could not activity_encode barcode, onecodeData = " + onecodeData);
			showErrorMessage("无法生成条形码。");
			return;
		}
		
		ImageView view = findViewById(R.id.encode_onecode_img);
		view.setImageBitmap(bitmap);
		
		TextView contents = findViewById(R.id.encode_onecode_text);
		contents.setText(onecodeData);
		
		setTitle("纯文本");
	}
	
	private void initQrcodeView(String qrcodeData) {
		int screenWidth = UIScreenUtil.getScreenWidth(SDKManager.getApplication());
		int smallerDimension = screenWidth * 7 / 8;
		
		Bitmap bitmap = CodeManager.encodeAsQRCodeBitmap(qrcodeData, smallerDimension);
		if (bitmap == null) {
			LogUtil.v("Could not activity_encode barcode, qrcodeData = " + qrcodeData);
			showErrorMessage("无法生成二维码。");
			return;
		}
		
		ImageView view = findViewById(R.id.encode_qrcode_img);
		view.setImageBitmap(bitmap);
		
		TextView contents = findViewById(R.id.encode_qrcode_text);
		contents.setText(qrcodeData);
		
		setTitle("纯文本");
	}
	
	private void showErrorMessage(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
}
