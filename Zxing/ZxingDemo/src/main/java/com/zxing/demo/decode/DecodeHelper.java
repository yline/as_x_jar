package com.zxing.demo.decode;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.yline.application.SDKManager;
import com.yline.utils.LogUtil;

import java.io.File;

public class DecodeHelper {
	public static boolean openCamera(Activity activity, File tempFile, int requestCode) {
		if (null == tempFile || !tempFile.exists()) {
			SDKManager.toast("文件不存在");
			return false;
		}
		
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		Uri outputUri = Uri.fromFile(tempFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
		if (null != intent.resolveActivity(activity.getPackageManager())) {
			activity.startActivityForResult(intent, requestCode);
			return true;
		} else {
			LogUtil.e("camera do not exist");
			return false;
		}
	}
	
	public static boolean openAlbum(Activity activity, int requestCode) {
		Intent intent = new Intent();
		
		intent.setAction(Intent.ACTION_PICK);
		intent.setType("image/*");
		if (null != intent.resolveActivity(activity.getPackageManager())) {
			activity.startActivityForResult(intent, requestCode);
			return true;
		} else {
			LogUtil.e("album do not exist");
			return false;
		}
	}
}
