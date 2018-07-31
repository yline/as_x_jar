package com.zxing.demo;

import android.os.Bundle;

import android.view.View;

import com.google.zxing.client.android.CaptureActivity;
import com.yline.test.BaseTestActivity;
import com.zxing.demo.encode.EncodeInputActivity;
import com.zxing.demo.manager.DBManager;

public class MainActivity extends BaseTestActivity {
	
	@Override
	public void testStart(View view, Bundle savedInstanceState) {
		addButton("扫描 - 码", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CaptureActivity.launch(MainActivity.this);
			}
		});
		
		addButton("生成 - 二维码", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EncodeInputActivity.launch(MainActivity.this);
			}
		});
		
		addButton("日志 - 打印最近5次扫码记录", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DBManager.getInstance().buildHistoryItems();
			}
		});
	}
}
