package com.google.zxing.client.android;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.client.android.camera.CameraManager;
import com.zxing.demo.manager.DBManager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * This thread does all the heavy lifting of decoding the images.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
final class DecodeThread extends Thread {
	public static final String BARCODE_BITMAP = "barcode_bitmap";
	
	public static final String BARCODE_SCALED_FACTOR = "barcode_scaled_factor";
	
	private final Map<DecodeHintType, Object> hints;
	
	private Handler handler;
	
	private final Handler mMainHandler;
	private final CountDownLatch handlerInitLatch;
	
	DecodeThread(ResultPointCallback resultPointCallback, Handler mainHandler) {
		this.mMainHandler = mainHandler;
		
		handlerInitLatch = new CountDownLatch(1);
		hints = buildDecodeHintMap(resultPointCallback);
	}
	
	Handler getHandler() {
		try {
			handlerInitLatch.await();
		} catch (InterruptedException ie) {
			// continue?
		}
		return handler;
	}
	
	@Override
	public void run() {
		Looper.prepare();
		handler = new DecodeHandler(hints, mMainHandler);
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
		attachBarcodeFormat(hintMap);
		
		hintMap.put(DecodeHintType.CHARACTER_SET, "UTF-8");
		hintMap.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, resultPointCallback);
		return hintMap;
	}
	
	/**
	 * 管理支持的扫码功能
	 * 对应文件：com.google.zxing.MultiFormatReader
	 *
	 * @param decodeHintMap 将要 设置入 MultiFormatReader 的hints内容
	 */
	private static void attachBarcodeFormat(Map<DecodeHintType, Object> decodeHintMap) {
		Collection<BarcodeFormat> barcodeFormats = EnumSet.noneOf(BarcodeFormat.class);
		
		/*
		 * 一维码  对应  MultiFormatOneDReader
		 * {UPC_A、UPC_E、EAN_13、EAN_8、CODABAR、CODE_39、CODE_93、CODE_128、ITF、RSS_14、RSS_EXPANDED}
		 * 以上满足一条，即可支持一维码解码
		 */
		barcodeFormats.add(BarcodeFormat.UPC_A);
		
		/* 二维码  对应 QRCodeReader */
		barcodeFormats.add(BarcodeFormat.QR_CODE);
		
		/* 矩阵式二维码  对应  DataMatrixReader */
		barcodeFormats.add(BarcodeFormat.DATA_MATRIX);
		
		/* 高容量二维码  对应  AztecReader */
		// barcodeFormats.add(BarcodeFormat.AZTEC);
		
		/* 堆叠式二维码  对应  PDF417Reader */
		// barcodeFormats.add(BarcodeFormat.PDF_417);
		
		/* 多功能条码  对应  MaxiCodeReader */
		// barcodeFormats.add(BarcodeFormat.MAXICODE);
		
		decodeHintMap.put(DecodeHintType.POSSIBLE_FORMATS, barcodeFormats);
	}
}
