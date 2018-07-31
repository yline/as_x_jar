package com.google.zxing.client.android.encode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import android.graphics.Bitmap;
import android.text.TextUtils;
import java.util.EnumMap;
import java.util.Map;

public final class EncoderManager {
	private static final int WHITE = 0xFFFFFFFF;
	
	private static final int BLACK = 0xFF000000;
	
	/**
	 * 编码成 二维码图片
	 *
	 * @param encodeData 内容
	 * @param dimension  大小
	 * @return bitmap
	 */
	public static Bitmap encodeAsQRCodeBitmap(String encodeData, int dimension) {
		if (TextUtils.isEmpty(encodeData)) {
			return null;
		}
		
		try {
			BarcodeFormat format = BarcodeFormat.QR_CODE;
			
			Map<EncodeHintType, Object> paramMap = new EnumMap<>(EncodeHintType.class);
			paramMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			
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
