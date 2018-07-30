package com.zxing.demo;

import com.google.zxing.client.android.camera.FrontLightMode;
import com.yline.application.SDKManager;
import com.yline.utils.SPUtil;

public class DBManager {
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
	
	public boolean getDecode1DProduct() {
		return (boolean) SPUtil.get(SDKManager.getApplication(), Key.KEY_DECODE_1D_PRODUCT, true);
	}
	
	public boolean getDecode1DIndustrial() {
		return (boolean) SPUtil.get(SDKManager.getApplication(), Key.KEY_DECODE_1D_INDUSTRIAL, true);
	}
	
	public boolean getDecodeQR() {
		return (boolean) SPUtil.get(SDKManager.getApplication(), Key.KEY_DECODE_QR, true);
	}
	
	public boolean getDecodeDataMatrix() {
		return (boolean) SPUtil.get(SDKManager.getApplication(), Key.KEY_DECODE_DATA_MATRIX, true);
	}
	
	public boolean getDecodeAztec() {
		return (boolean) SPUtil.get(SDKManager.getApplication(), Key.KEY_DECODE_AZTEC, false);
	}
	
	public boolean getDecodePdf417() {
		return (boolean) SPUtil.get(SDKManager.getApplication(), Key.KEY_DECODE_PDF417, false);
	}
	
	public boolean getPlayBeep() {
		return (boolean) SPUtil.get(SDKManager.getApplication(), Key.KEY_PLAY_BEEP, true);
	}
	
	public boolean getVibrate() {
		return (boolean) SPUtil.get(SDKManager.getApplication(), Key.KEY_VIBRATE, false);
	}
	
	public boolean getCopyToClipboard() {
		return (boolean) SPUtil.get(SDKManager.getApplication(), Key.KEY_COPY_TO_CLIPBOARD, true);
	}
	
	public boolean getAutoOpenWeb() {
		return (boolean) SPUtil.get(SDKManager.getApplication(), Key.KEY_AUTO_OPEN_WEB, false);
	}
	
	public boolean getRememberDuplicates() {
		return (boolean) SPUtil.get(SDKManager.getApplication(), Key.KEY_REMEMBER_DUPLICATES, false);
	}
	
	public boolean getHistory() {
		return (boolean) SPUtil.get(SDKManager.getApplication(), Key.KEY_ENABLE_HISTORY, true);
	}
	
	public boolean getSupplemental() {
		return (boolean) SPUtil.get(SDKManager.getApplication(), Key.KEY_SUPPLEMENTAL, true);
	}
	
	public String getFrontLightMode() {
		return (String) SPUtil.get(SDKManager.getApplication(), Key.KEY_FRONT_LIGHT_MODE, FrontLightMode.OFF.toString());
	}
	
	public void setFrontLightMode(FrontLightMode lightMode) {
		SPUtil.put(SDKManager.getApplication(), Key.KEY_FRONT_LIGHT_MODE, lightMode.toString());
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
	
	public String getCustomProductSearch() {
		return (String) SPUtil.get(SDKManager.getApplication(), Key.KEY_CUSTOM_PRODUCT_SEARCH, null);
	}
	
	public String getSearchCountry() {
		return (String) SPUtil.get(SDKManager.getApplication(), Key.KEY_SEARCH_COUNTRY, "-");
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
	
	/*
	    private static final String KEY_DISABLE_CONTINUOUS_FOCUS = "disable_continuous_focus"; // 使用标准对焦模式(不持续对焦)
		
		private static final String KEY_DISABLE_EXPOSURE = "disable_exposure"; // 不曝光
		
		private static final String KEY_DISABLE_METERING = "disable_metering"; // 不使用距离测量
		
		private static final String KEY_DISABLE_BARCODE_SCENE_MODE = "disable_barcode_scene_mode"; // 不进行条形码场景匹配*/
	
	private static class Key {
		// 条码类型
		private static final String KEY_DECODE_1D_PRODUCT = "decode_1D_product"; // 一维码：商品
		
		private static final String KEY_DECODE_1D_INDUSTRIAL = "decode_1D_industrial"; // 一维码：工业
		
		private static final String KEY_DECODE_QR = "decode_QR"; // 二维码
		
		private static final String KEY_DECODE_DATA_MATRIX = "decode_Data_Matrix"; // Data Matrix
		
		private static final String KEY_DECODE_AZTEC = "decode_Aztec"; // Aztec
		
		private static final String KEY_DECODE_PDF417 = "decode_PDF417"; // PDF417 测试
		
		// 扫描成功
		private static final String KEY_PLAY_BEEP = "play_beep"; // 播放提示音
		
		private static final String KEY_VIBRATE = "vibrate"; // 震动
		
		private static final String KEY_COPY_TO_CLIPBOARD = "copy_to_clipboard"; // 复制到剪贴板
		
		private static final String KEY_AUTO_OPEN_WEB = "auto_open_web"; // 自动打开网页
		
		private static final String KEY_REMEMBER_DUPLICATES = "remember_duplicates"; // 在历史记录中保存重复的记录
		
		private static final String KEY_ENABLE_HISTORY = "history"; // 自动将条码存入历史记录
		
		private static final String KEY_SUPPLEMENTAL = "supplemental"; // 尝试检索关于条码内容的更多信息
		
		// 扫描设置
		private static final String KEY_FRONT_LIGHT_MODE = "front_light_mode"; // 设置闪光灯模式  FrontLightMode
		
		private static final String KEY_AUTO_FOCUS = "auto_focus"; // 自动对焦
		
		private static final String KEY_INVERT_SCAN = "invert_scan"; // 扫描黑色背景上的白色条码。仅适用于部分设备。
		
		private static final String KEY_BULK_MODE = "bulk_mode"; // 连续扫描并保存多个条码
		
		private static final String KEY_DISABLE_AUTO_ORIENTATION = "orientation"; // 不自动旋转
		
		// 搜索设置
		private static final String KEY_CUSTOM_PRODUCT_SEARCH = "custom_product_search"; //  替换：％s=内容，％f=格式，％t=类型 （自定义搜索网址URI）
		
		private static final String KEY_SEARCH_COUNTRY = "search_country"; // 搜索引擎国别
		/* R.array.country_codes */
		
		// 设备适配
		private static final String KEY_DISABLE_CONTINUOUS_FOCUS = "disable_continuous_focus"; // 使用标准对焦模式(不持续对焦)
		
		private static final String KEY_DISABLE_EXPOSURE = "disable_exposure"; // 不曝光
		
		private static final String KEY_DISABLE_METERING = "disable_metering"; // 不使用距离测量
		
		private static final String KEY_DISABLE_BARCODE_SCENE_MODE = "disable_barcode_scene_mode"; // 不进行条形码场景匹配
	}
}
