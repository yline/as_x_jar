package com.google.zxing.client.android.helper;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.decode.DecodeThread;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.yline.utils.LogUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

public final class CodeManager {
	private static final boolean DEBUG = true;
	private static final String CHARACTER = "UTF-8";
	private static final int WHITE = 0xFFFFFFFF;
	
	private static final int BLACK = 0xFF000000;
	
	public static void v(String tag, String content) {
		if (DEBUG) {
			LogUtil.v(tag + ":" + content, LogUtil.LOG_LOCATION_PARENT);
		}
	}
	
	public static Result decodeQRCodeBitmap(String pathName) {
		return decodeQRCodeBitmap(BitmapFactory.decodeFile(pathName));
	}
	
	public static Bitmap encodeAsQRCodeBitmap(String encodeData, int dimension) {
		return encodeAsQRCodeBitmap(encodeData, dimension, 0);
	}
	
	private static Result decodeQRCodeBitmap(Bitmap barcode) {
		if (null == barcode) {
			return null;
		}
		
		// 参数配置
		Map<DecodeHintType, Object> hintMap = new EnumMap<>(DecodeHintType.class);
		DecodeThread.attachBarcodeFormat(hintMap);
		hintMap.put(DecodeHintType.CHARACTER_SET, CHARACTER);
		
		MultiFormatReader multiFormatReader = new MultiFormatReader();
		multiFormatReader.setHints(hintMap);
		
		try {
			// 具体解析
			BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BitmapLuminanceSource(barcode)));
			return multiFormatReader.decodeWithState(binaryBitmap);
		} catch (NotFoundException e) {
			return null;
		} finally {
			multiFormatReader.reset();
		}
	}
	
	/**
	 * 编码成 二维码图片
	 *
	 * @param encodeData 内容
	 * @param dimension  大小
	 * @param margin     边距
	 * @return bitmap
	 */
	private static Bitmap encodeAsQRCodeBitmap(String encodeData, int dimension, int margin) {
		if (TextUtils.isEmpty(encodeData)) {
			return null;
		}
		
		try {
			BarcodeFormat format = BarcodeFormat.QR_CODE;
			
			Map<EncodeHintType, Object> paramMap = new EnumMap<>(EncodeHintType.class);
			paramMap.put(EncodeHintType.CHARACTER_SET, CHARACTER);
			paramMap.put(EncodeHintType.MARGIN, (margin >= 0) ? margin : 5);
			
			BitMatrix result = new MultiFormatWriter().encode(encodeData, format, dimension, dimension, paramMap);
			int width = result.getWidth();
			int height = result.getHeight();
			int[] pixels = new int[width * height];
			for (int y = 0; y < height; y++) {
				int offset = y * width;
				for (int x = 0; x < width; x++) {
					pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
				}
			}
			
			Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			
			return bitmap;
		} catch (IllegalArgumentException | WriterException iae) {
			// Unsupported format
			return null;
		}
	}
}
