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
import android.util.Log;

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

/**
 * Zxing模块，生成、解析图片、日志开关
 *
 * @author yline
 * @times 2018/8/3 -- 9:50
 */
public final class CodeManager {
	private static final boolean DEBUG = true; // 是否debug
	
	private static final int DECODE_DIMENSION = 400; // 默认缩放的较小边大小
	
	private static final String CHARACTER = "UTF-8";
	
	private static final int WHITE = 0xFFFFFFFF;
	
	private static final int BLACK = 0xFF000000;
	
	public static void v(String tag, String content) {
		if (DEBUG) {
			LogUtil.v(tag + ":" + content, LogUtil.LOG_LOCATION_PARENT);
		}
	}
	
	/**
	 * 解析Bitmap (可以识别绝大部分，像素大的bitmap[拍照出来的图片])
	 * 耗时：
	 * 280 * 280 --> decode(70), load+decode(80ms)
	 * 手机照片(4160*3120) --> decode(91ms), load+decode(285)
	 * 是否放入子线程，使用者自身考虑
	 *
	 * @param filePath 图片路径
	 * @return 解析结果
	 */
	public static Result decodeQRCodeBitmap(String filePath) {
		int scale = computeScale(filePath);
		Bitmap bitmap = loadBitmap(filePath, scale);
		return decodeQRCodeBitmap(bitmap);
	}
	
	/**
	 * 加载出Bitmap
	 *
	 * @param sourcePath 图片路径
	 * @param scale      缩放比例
	 * @return Bitmap or null
	 */
	public static Bitmap loadBitmap(String sourcePath, int scale) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = scale;
		
		options.inPreferredConfig = Bitmap.Config.ALPHA_8; // 降低加载的内存大小，测试过，能够识别出来
		return BitmapFactory.decodeFile(sourcePath, options);
	}
	
	/**
	 * 手动计算 加载Bitmap所需的Scale
	 *
	 * @param sourcePath 图片路径
	 * @return scale大小
	 */
	public static int computeScale(String sourcePath) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inSampleSize = 1;
		BitmapFactory.decodeFile(sourcePath, options);
		
		int sourceWidth = options.outWidth;
		int sourceHeight = options.outHeight;
		
		int scale = Math.min(sourceWidth, sourceHeight) / DECODE_DIMENSION;
		return (scale + 1);
	}
	
	/**
	 * 解析Bitmap (若bitmap像素过大，则无法识别)
	 *
	 * @param barcode bitmap
	 * @return 图片信息 or null
	 */
	public static Result decodeQRCodeBitmap(Bitmap barcode) {
		if (null == barcode) {
			return null;
		}
		
		long start = System.currentTimeMillis();
		// 参数配置
		Map<DecodeHintType, Object> hintMap = new EnumMap<>(DecodeHintType.class);
		DecodeThread.attachBarcodeFormat(hintMap);
		hintMap.put(DecodeHintType.CHARACTER_SET, CHARACTER);
		
		MultiFormatReader multiFormatReader = new MultiFormatReader();
		multiFormatReader.setHints(hintMap);
		
		try {
			// 具体解析
			BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BitmapLuminanceSource(barcode)));
			Result rawResult = multiFormatReader.decodeWithState(binaryBitmap);
			v("decodeQRCodeBitmap", "Decode barcode in " + (System.currentTimeMillis() - start) + " ms");
			return rawResult;
		} catch (NotFoundException e) {
			v("decodeQRCodeBitmap", "NotFoundException -> " + android.util.Log.getStackTraceString(e));
			return null;
		} finally {
			multiFormatReader.reset();
		}
	}
	
	/* ------------------------- 生成 二维码 -------------------------- */
	
	/**
	 * 编码成 二维码图片
	 *
	 * @param encodeData 内容
	 * @param dimension  大小
	 * @return bitmap or null
	 */
	public static Bitmap encodeAsQRCodeBitmap(String encodeData, int dimension) {
		return encodeAsQRCodeBitmap(encodeData, dimension, 0);
	}
	
	/**
	 * 编码成 二维码图片
	 *
	 * @param encodeData 内容
	 * @param dimension  大小
	 * @param margin     边距
	 * @return bitmap or null
	 */
	public static Bitmap encodeAsQRCodeBitmap(String encodeData, int dimension, int margin) {
		if (TextUtils.isEmpty(encodeData)) {
			return null;
		}
		
		try {
			BarcodeFormat format = BarcodeFormat.QR_CODE;
			
			Map<EncodeHintType, Object> paramMap = new EnumMap<>(EncodeHintType.class);
			paramMap.put(EncodeHintType.CHARACTER_SET, CHARACTER);
			paramMap.put(EncodeHintType.MARGIN, (margin >= 0) ? margin : 5);
			
			BitMatrix result = new MultiFormatWriter().encode(encodeData, format, dimension, dimension, paramMap);
			return matrixToBitmap(result);
		} catch (IllegalArgumentException | WriterException iae) {
			// Unsupported format
			return null;
		}
	}
	
	/* ------------------------- 生成 一维码 -------------------------- */
	public static Bitmap encodeAsOneCodeBitmap(String encodeData, int widthBitmap, int heightBitmap) {
		return encodeAsOneCodeBitmap(encodeData, widthBitmap, heightBitmap, 0);
	}
	
	/**
	 * 编码成 一维码图片
	 *
	 * @param encodeData   内容（仅仅允许字符串(0-9)）
	 * @param bitmapWidth  宽度
	 * @param bitmapHeight 高度
	 * @param margin       边距
	 * @return bitmap or null
	 */
	public static Bitmap encodeAsOneCodeBitmap(String encodeData, int bitmapWidth, int bitmapHeight, int margin) {
		if (TextUtils.isEmpty(encodeData)) {
			return null;
		}
		
		try {
			BarcodeFormat format = BarcodeFormat.CODE_128;
			
			Map<EncodeHintType, Object> paramMap = new EnumMap<>(EncodeHintType.class);
			paramMap.put(EncodeHintType.CHARACTER_SET, CHARACTER);
			paramMap.put(EncodeHintType.MARGIN, (margin >= 0) ? margin : 5);
			
			BitMatrix result = new MultiFormatWriter().encode(encodeData, format, bitmapWidth, bitmapHeight, paramMap);
			return matrixToBitmap(result);
		} catch (WriterException | IllegalArgumentException e) {
			// Unsupported format
			LogUtil.e("xxx", e);
			return null;
		}
	}
	
	private static Bitmap matrixToBitmap(BitMatrix bitMatrix) {
		int width = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = bitMatrix.get(x, y) ? BLACK : WHITE;
			}
		}
		
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		
		LogUtil.v("width = " + bitmap.getWidth() + ", height = " + bitmap.getHeight());
		
		return bitmap;
	}
}
