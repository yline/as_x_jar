package com.zxing.demo.encode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.google.zxing.client.android.R;

import com.yline.base.BaseActivity;

/**
 * 编码输入 界面
 *
 * @author linjiang@kjtpay.com
 * @times 2018/7/31 -- 9:20
 */
public final class EncodeInputActivity extends BaseActivity {
	public static void launch(Context context) {
		if (null != context) {
			Intent intent = new Intent();
			intent.setClass(context, EncodeInputActivity.class);
			if (!(context instanceof Activity)) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent);
		}
	}
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_encode_input);
		
		findViewById(R.id.encode_input_text_view).setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View view, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
					String text = ((TextView) view).getText().toString();
					if (!TextUtils.isEmpty(text)) {
						EncodeActivity.launch(EncodeInputActivity.this, text);
					}
					return true;
				}
				return false;
			}
		});
	}
}
