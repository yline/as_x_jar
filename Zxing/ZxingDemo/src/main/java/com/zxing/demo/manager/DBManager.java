package com.zxing.demo.manager;

import android.text.TextUtils;

import com.google.zxing.Result;
import com.google.zxing.client.android.result.ResultHandler;
import com.yline.application.SDKManager;
import com.yline.utils.LogUtil;
import com.yline.utils.SPUtil;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
	public static final int HISTORY_MAX_NUM = 5;
	
	private static DBManager dbManager;
	
	private DBManager() {
	}
	
	public static DBManager getInstance() {
		if (null == dbManager) {
			synchronized (DBManager.class) {
				if (null == dbManager) {
					dbManager = new DBManager();
				}
			}
		}
		return dbManager;
	}
	
	public boolean getAutoFocus() {
		return (boolean) SPUtil.get(SDKManager.getApplication(), Key.KEY_AUTO_FOCUS, true);
	}
	
	public boolean getInvertScan() {
		return (boolean) SPUtil.get(SDKManager.getApplication(), Key.KEY_INVERT_SCAN, false);
	}
	
	public boolean getBulkMode() {
		return (boolean) SPUtil.get(SDKManager.getApplication(), Key.KEY_BULK_MODE, false);
	}
	
	public boolean getOrientation() {
		return (boolean) SPUtil.get(SDKManager.getApplication(), Key.KEY_DISABLE_AUTO_ORIENTATION, true);
	}
	
	public boolean getDisableContinuousFocus() {
		return (boolean) SPUtil.get(SDKManager.getApplication(), Key.KEY_DISABLE_CONTINUOUS_FOCUS, true);
	}
	
	public boolean getDisableExposure() {
		return (boolean) SPUtil.get(SDKManager.getApplication(), Key.KEY_DISABLE_EXPOSURE, true);
	}
	
	public boolean getDisableMetering() {
		return (boolean) SPUtil.get(SDKManager.getApplication(), Key.KEY_DISABLE_METERING, true);
	}
	
	public boolean getDisableBarcodeSceneMode() {
		return (boolean) SPUtil.get(SDKManager.getApplication(), Key.KEY_DISABLE_BARCODE_SCENE_MODE, true);
	}
	
	public void addHistoryItem(Result result, ResultHandler resultHandler) {
		String historyItem = LogManager.buildHistoryItem(result, resultHandler);
		int historyNum = (int) SPUtil.get(SDKManager.getApplication(), Key.KEY_HISTORY_NUM, 0);
		historyNum = historyNum % HISTORY_MAX_NUM;
		
		SPUtil.put(SDKManager.getApplication(), (Key.KEY_HISTORY_VALUE + historyNum), historyItem);
		
		historyNum += 1;
		SPUtil.put(SDKManager.getApplication(), Key.KEY_HISTORY_NUM, historyNum);
	}
	
	public String getHistoryItem(int index) {
		return (String) SPUtil.get(SDKManager.getApplication(), (Key.KEY_HISTORY_VALUE + index), "");
	}
	
	private static class Key {
		// 扫描设置
		private static final String KEY_AUTO_FOCUS = "auto_focus"; // 自动对焦
		
		private static final String KEY_INVERT_SCAN = "invert_scan"; // 扫描黑色背景上的白色条码。仅适用于部分设备。
		
		private static final String KEY_BULK_MODE = "bulk_mode"; // 连续扫描并保存多个条码
		
		private static final String KEY_DISABLE_AUTO_ORIENTATION = "orientation"; // 不自动旋转
		
		// 设备适配
		private static final String KEY_DISABLE_CONTINUOUS_FOCUS = "disable_continuous_focus"; // 使用标准对焦模式(不持续对焦)
		
		private static final String KEY_DISABLE_EXPOSURE = "disable_exposure"; // 不曝光
		
		private static final String KEY_DISABLE_METERING = "disable_metering"; // 不使用距离测量
		
		private static final String KEY_DISABLE_BARCODE_SCENE_MODE = "disable_barcode_scene_mode"; // 不进行条形码场景匹配
		
		// 保存五条历史记录
		private static final String KEY_HISTORY_NUM = "history_num"; // 保存储存的数量
		
		private static final String KEY_HISTORY_VALUE = ""; // 保存历史数据 的值
	}
}
