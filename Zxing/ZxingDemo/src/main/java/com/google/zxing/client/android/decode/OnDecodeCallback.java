package com.google.zxing.client.android.decode;

import android.graphics.Bitmap;

import com.google.zxing.Result;

/**
 * 主线程，解析状态的回调
 *
 * @author yline
 * @times 2018/8/1 -- 14:42
 */
public interface OnDecodeCallback {
	/**
	 * 摄像机开始预览时，回调(主线程)
	 */
	void onRestartPreview();
	
	/**
	 * A valid barcode has been found, so give an indication of success and show the results.
	 * 扫描成功回调
	 *
	 * @param rawResult   扫描生成的具体的数据（文字、时间等）
	 * @param barcode     扫描得到的图片
	 * @param scaleFactor 生成的图片的 （宽度/原始资料的宽度）
	 */
	void onHandleDecode(Result rawResult, Bitmap barcode, float scaleFactor);
}
