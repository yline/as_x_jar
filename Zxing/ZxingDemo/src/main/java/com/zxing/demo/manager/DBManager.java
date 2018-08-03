package com.zxing.demo.manager;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.yline.application.SDKManager;
import com.yline.utils.SPUtil;

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
	
	public void addHistoryItem(Result result, ParsedResult parsedResult) {
		String historyItem = LogManager.buildHistoryItem(result, parsedResult);
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
		// 保存五条历史记录
		private static final String KEY_HISTORY_NUM = "history_num"; // 保存储存的数量
		
		private static final String KEY_HISTORY_VALUE = ""; // 保存历史数据 的值
	}
}
