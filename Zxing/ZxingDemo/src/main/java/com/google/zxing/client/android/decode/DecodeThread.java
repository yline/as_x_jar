package com.google.zxing.client.android.decode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.client.android.helper.CodeManager;

import android.os.Handler;
import android.os.Looper;

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * 负责设置解析配置，和，实现具体解析的Handler
 *
 * @author yline
 * @times 2018/8/1 -- 14:35
 */
final class DecodeThread extends Thread {
	public static final String BARCODE_BITMAP = "barcode_bitmap";
	
	public static final String BARCODE_SCALED_FACTOR = "barcode_scaled_factor";
	
	private final Map<DecodeHintType, Object> hints; // 配置信息
	
	private Handler mDecodeHandler; // 属于子线程的，具体解析的Handler
	
	private final Handler mMainHandler; // 属于UI线程，回调信息
	private final CountDownLatch handlerInitLatch; // 计数器达到0，锁wait方法释放
	
	DecodeThread(Handler mainHandler, ResultPointCallback resultPointCallback) {
		this.mMainHandler = mainHandler;
		
		handlerInitLatch = new CountDownLatch(1);
		hints = buildDecodeHintMap(resultPointCallback);
	}
	
	Handler getmDecodeHandler() {
		try {
			handlerInitLatch.await();
		} catch (InterruptedException ie) {
			// continue?
		}
		return mDecodeHandler;
	}
	
	@Override
	public void run() {
		Looper.prepare();
		mDecodeHandler = new DecodeHandler(hints, mMainHandler);
		handlerInitLatch.countDown();
		Looper.loop();
	}
	
	/**
	 * 管理 支持的 设置
	 *
	 * @param resultPointCallback 扫描过程中，识别中回调
	 * @return 设置内容
	 */
	private static Map<DecodeHintType, Object> buildDecodeHintMap(ResultPointCallback resultPointCallback) {
		Map<DecodeHintType, Object> hintMap = new EnumMap<>(DecodeHintType.class);
		CodeManager.attachBarcodeFormat(hintMap);
		
		hintMap.put(DecodeHintType.CHARACTER_SET, "UTF-8");
		hintMap.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, resultPointCallback);
		return hintMap;
	}
}
