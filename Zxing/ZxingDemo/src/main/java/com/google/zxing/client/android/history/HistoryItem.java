/*
 * Copyright 2012 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android.history;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.yline.utils.LogUtil;

import java.util.Map;

/**
 * Represents one item in the scan history.
 */
public final class HistoryItem {
	
	private final Result result;
	
	private final String display;
	
	private final String details;
	
	HistoryItem(Result result, String display, String details) {
		this.result = result;
		this.display = display;
		this.details = details;
	}
	
	public Result getResult() {
		return result;
	}
	
	public String getDisplayAndDetails() {
		StringBuilder displayResult = new StringBuilder();
		if (display == null || display.isEmpty()) {
			displayResult.append(result.getText());
		} else {
			displayResult.append(display);
		}
		if (details != null && !details.isEmpty()) {
			displayResult.append(" : ").append(details);
		}
		return displayResult.toString();
	}
	
	public static void printResult(Result rawResult) {
		if (null == rawResult) {
			LogUtil.v("rawResult is null");
			return;
		}
		
		// 识别的内容 {1,蝴蝶眨几次眼睛才学会飞行 2,夜空洒满了星星但几颗会落地}
		StringBuilder stringBuilder = new StringBuilder();
		String text = rawResult.getText();
		stringBuilder.append(text);
		
		// 识别的图片byte，string是乱码{EΉ�N��nyʎXzj�y��yپh��Z�nKɮ�9�#"�ZI�z��kI.k�K�ni��i��K�nXz�)~Kɮ��Y�����}
		byte[] rawBytes = rawResult.getRawBytes();
		stringBuilder.append('\n');
		stringBuilder.append("rawBytes = ");
		stringBuilder.append(new String(rawBytes));
		
		// rawByte的数量 {688}
		int numBits = rawResult.getNumBits();
		stringBuilder.append('\n');
		stringBuilder.append("numBits = ");
		stringBuilder.append(numBits);
		
		// 识别的四个角的坐标 {0point = (235.0,372.5), 1point = (230.5,77.0), 2point = (524.0,76.5), 3point = (505.5,346.5), }
		ResultPoint[] resultPoints = rawResult.getResultPoints();
		stringBuilder.append('\n');
		stringBuilder.append("resultPoints = ");
		for (int i = 0; i < resultPoints.length; i++) {
			stringBuilder.append(i);
			stringBuilder.append("point = ");
			stringBuilder.append(resultPoints[i].toString());
			stringBuilder.append(", ");
		}
		
		// 扫描的码的类型 {QR_CODE}
		BarcodeFormat barcodeFormat = rawResult.getBarcodeFormat();
		stringBuilder.append('\n');
		stringBuilder.append("barcodeFormat = ");
		stringBuilder.append(barcodeFormat.toString());
		
		// {key = BYTE_SEGMENTS, value = [[B@b50c9f6]
		//    key = ERROR_CORRECTION_LEVEL, value = H}
		Map<ResultMetadataType, Object> resultMetadata = rawResult.getResultMetadata();
		stringBuilder.append('\n');
		stringBuilder.append("resultMetadata = ");
		for (ResultMetadataType key : resultMetadata.keySet()) {
			stringBuilder.append("key = ");
			stringBuilder.append(key.toString());
			stringBuilder.append(", value = ");
			stringBuilder.append(resultMetadata.get(key));
			stringBuilder.append('\n');
		}
		
		// 毫秒级{1532934507021}
		long timestamp = rawResult.getTimestamp();
		stringBuilder.append('\n');
		stringBuilder.append("timestamp = ");
		stringBuilder.append(timestamp);
		
		LogUtil.v(stringBuilder.toString());
	}
	
}
