package com.google.zxing.client.android.encode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.Contents;
import com.google.zxing.client.android.Intents;
import com.google.zxing.client.android.R;
import com.google.zxing.client.result.AddressBookParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.common.BitMatrix;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

final class QRCodeEncoder {
	private static final int WHITE = 0xFFFFFFFF;
	
	private static final int BLACK = 0xFF000000;
	
	private final Context context;
	
	private String contents;
	
	private String displayContents;
	
	private String title;
	
	private BarcodeFormat format;
	
	private final int dimension;
	
	QRCodeEncoder(Context context, Intent intent, int dimension, boolean useVCard) throws WriterException {
		this.context = context;
		this.dimension = dimension;
		
		String action = intent.getAction();
		if (Intents.Encode.ACTION.equals(action)) {
			encodeContentsFromZXingIntent(intent);
		}
	}
	
	private void encodeContentsFromZXingIntent(Intent intent) {
		format = BarcodeFormat.QR_CODE;
		title = context.getString(R.string.contents_text);
		
		contents = intent.getStringExtra(Intents.Encode.DATA);
		displayContents = contents;
	}
	
	private static List<String> getAllBundleValues(Bundle bundle, String[] keys) {
		List<String> values = new ArrayList<>(keys.length);
		for (String key : keys) {
			Object value = bundle.get(key);
			values.add(value == null ? null : value.toString());
		}
		return values;
	}
	
	public Bitmap encodeAsBitmap() throws WriterException {
		String contentsToEncode = contents;
		if (contentsToEncode == null) {
			return null;
		}
		Map<EncodeHintType, Object> hints = null;
		String encoding = guessAppropriateEncoding(contentsToEncode);
		if (encoding != null) {
			hints = new EnumMap<>(EncodeHintType.class);
			hints.put(EncodeHintType.CHARACTER_SET, encoding);
		}
		
		BitMatrix result;
		try {
			result = new MultiFormatWriter().encode(contentsToEncode, format, dimension, dimension, hints);
		} catch (IllegalArgumentException iae) {
			// Unsupported format
			return null;
		}
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
	}
	
	private static String guessAppropriateEncoding(CharSequence contents) {
		// Very crude at the moment
		for (int i = 0; i < contents.length(); i++) {
			if (contents.charAt(i) > 0xFF) {
				return "UTF-8";
			}
		}
		return null;
	}
	
	String getDisplayContents() {
		return displayContents;
	}
	
	String getTitle() {
		return title;
	}
}
