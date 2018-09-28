package com.google.zxing.client.android.decode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
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
public final class DecodeThread extends Thread {
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
		//		attachBarcodeFormat(hintMap);
		
		hintMap.put(DecodeHintType.CHARACTER_SET, "UTF-8");
		hintMap.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, resultPointCallback);
		return hintMap;
	}
	
	/**
	 * 管理支持的扫码功能；解码
	 * 对应文件：com.google.zxing.MultiFormatReader
	 *
	 * @param decodeHintMap 将要 设置入 MultiFormatReader 的hints内容
	 */
	public static void attachBarcodeFormat(Map<DecodeHintType, Object> decodeHintMap) {
		Collection<BarcodeFormat> barcodeFormats = EnumSet.noneOf(BarcodeFormat.class);
		
		/*
		 * 一维码  对应  MultiFormatOneDReader
		 * {UPC_A、UPC_E、EAN_13、EAN_8}  // 一般用于超市
		 * {CODE_39} // 应用广
		 * {CODE_93} // 应用广
		 * {CODE_128} // 应用最广
		 * {CODABAR、ITF、RSS_14、RSS_EXPANDED} // 有场景，应用不多
		 * 以上满足一条，即可支持一维码解码
		 */
		barcodeFormats.add(BarcodeFormat.UPC_A);
		barcodeFormats.add(BarcodeFormat.CODE_39);
		barcodeFormats.add(BarcodeFormat.CODE_93);
		barcodeFormats.add(BarcodeFormat.CODE_128);
		
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
