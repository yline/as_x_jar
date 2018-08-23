package com.zxing.demo.decode;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.Result;
import com.google.zxing.client.android.R;
import com.google.zxing.client.android.helper.CodeManager;
import com.yline.base.BaseActivity;
import com.yline.utils.FileUtil;
import com.yline.utils.LogUtil;
import com.yline.utils.PermissionUtil;

import java.io.File;

/**
 * 解析本地图片
 *
 * @author yline
 * @times 2018/8/2 -- 16:02
 */
public class DecodeActivity extends BaseActivity {
	private static final int REQUEST_CODE_CAMERA = 1; // 照相机
	private static final int REQUEST_CODE_ALBUM = 2; // 相册
	
	public static void launch(Context context) {
		if (null != context) {
			Intent intent = new Intent();
			intent.setClass(context, DecodeActivity.class);
			if (!(context instanceof Activity)) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent);
		}
	}
	
	private ImageView decodeImageView;
	private TextView decodeTextView;
	private String filePath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_decode);
		
		PermissionUtil.request(this, PermissionUtil.REQUEST_CODE_PERMISSION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
		
		decodeImageView = findViewById(R.id.decode_image);
		decodeTextView = findViewById(R.id.decode_info);
		
		// 打开照相机
		findViewById(R.id.decode_open_camera).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				File tempFile = FileUtil.getFileTop("yline", System.currentTimeMillis() + ".jpg");
				filePath = tempFile.getPath();
				
				LogUtil.v("filePath = " + filePath);
				DecodeHelper.openCamera(DecodeActivity.this, tempFile, REQUEST_CODE_CAMERA);
			}
		});
		
		// 打开相册
		findViewById(R.id.decode_open_album).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DecodeHelper.openAlbum(DecodeActivity.this, REQUEST_CODE_ALBUM);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtil.v("onActivityResult data is null ? -> " + (data == null) + ", requestCode = " + requestCode + ", resultCode = " + resultCode);
		
		if (requestCode == REQUEST_CODE_CAMERA) { // 照相机
			if (resultCode == Activity.RESULT_OK) {
				decodeBitmap(filePath);
			}
		} else if (requestCode == REQUEST_CODE_ALBUM) { // 相册
			if (resultCode == Activity.RESULT_OK && null != data) {
				Uri uri = data.getData();
				LogUtil.v(String.valueOf(uri));
				if (null != uri) {
					decodeBitmap(uri.getPath());
				}
			}
		}
	}
	
	private void decodeBitmap(String filePath) {
		long start = System.currentTimeMillis();
		int scale = CodeManager.computeScale(filePath);
		CodeManager.v("decodeQRCodeBitmap", "scale = " + scale + ", filePath = " + filePath);
		Bitmap bitmap = CodeManager.loadBitmap(filePath, scale);
		
		Result rawResult = CodeManager.decodeQRCodeBitmap(bitmap);
		CodeManager.v("decodeQRCodeBitmap", "load and decode in " + (System.currentTimeMillis() - start) + " ms");
		
		CodeManager.v("decodeQRCodeBitmap", "bitmap = " + bitmap + ", rawResult = " + rawResult);
		if (null != bitmap) {
			decodeImageView.setImageBitmap(bitmap);
		}
		
		if (null != rawResult) {
			LogUtil.v("text = " + rawResult.getText());
			decodeTextView.setText(rawResult.getText());
		}
	}
}
