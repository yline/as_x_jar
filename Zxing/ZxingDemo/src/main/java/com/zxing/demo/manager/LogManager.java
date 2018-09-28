package com.zxing.demo.manager;

import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.result.ParsedResult;
import com.yline.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LogManager {
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
		if (null != resultMetadata) {
			stringBuilder.append('\n');
			stringBuilder.append("resultMetadata = ");
			for (ResultMetadataType key : resultMetadata.keySet()) {
				stringBuilder.append("key = ");
				stringBuilder.append(key.toString());
				stringBuilder.append(", value = ");
				stringBuilder.append(resultMetadata.get(key));
				stringBuilder.append('\n');
			}
		}
		
		// 毫秒级{1532934507021}
		long timestamp = rawResult.getTimestamp();
		stringBuilder.append('\n');
		stringBuilder.append("timestamp = ");
		stringBuilder.append(timestamp);
		
		LogUtil.v(stringBuilder.toString());
	}
	
	public static String buildHistoryItem(Result result, ParsedResult parsedResult) {
		StringBuilder stringBuilder = new StringBuilder();
		String text = result.getText();
		stringBuilder.append("text = ");
		stringBuilder.append(text);
		
		String barcodeFormat = result.getBarcodeFormat().toString();
		stringBuilder.append('\n');
		stringBuilder.append("barcodeFormat = ");
		stringBuilder.append(barcodeFormat);
		
		String displayContent = parsedResult.getDisplayResult();
		stringBuilder.append('\n');
		stringBuilder.append("displayContent = ");
		stringBuilder.append(displayContent);
		
		long timestamp = result.getTimestamp();
		stringBuilder.append('\n');
		stringBuilder.append("timestamp = ");
		stringBuilder.append(timestamp);
		
		return stringBuilder.toString();
	}
	
	public static void printHistoryItems() {
		List<String> historyItemList = new ArrayList<>();
		
		String historyItem;
		for (int i = 0; i < DBManager.HISTORY_MAX_NUM; i++) {
			historyItem = DBManager.getInstance().getHistoryItem(i);
			if (!TextUtils.isEmpty(historyItem)) {
				historyItemList.add(historyItem);
			}
		}
		
		if (historyItemList.isEmpty()) {
			LogUtil.v("historyItem is null");
		} else {
			for (String str : historyItemList) {
				try {
					Thread.sleep(1); // 打印太快，可能就混一起了
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				LogUtil.v("historyItem = \n" + str);
			}
		}
	}
}
