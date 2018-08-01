package com.google.zxing.client.android.helper;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import android.graphics.Bitmap;
import android.text.TextUtils;

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

public final class CodeManager {
	private static final String CHARACTER = "UTF-8";
	private static final int WHITE = 0xFFFFFFFF;
	
	private static final int BLACK = 0xFF000000;
	
	private static Result decodeQRCodeBitmap(Bitmap barcode) {
		if (null == barcode) {
			return null;
		}
		
		// 参数配置
		Map<DecodeHintType, Object> hintMap = new EnumMap<>(DecodeHintType.class);
		attachBarcodeFormat(hintMap);
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
	
	public static Bitmap encodeAsQRCodeBitmap(String encodeData, int dimension) {
		return encodeAsQRCodeBitmap(encodeData, dimension, 0);
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
