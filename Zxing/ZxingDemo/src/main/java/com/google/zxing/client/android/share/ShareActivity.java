package com.google.zxing.client.android.share;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.android.Contents;
import com.google.zxing.client.android.Intents;
import com.google.zxing.client.android.R;

import com.yline.base.BaseActivity;

public final class ShareActivity extends BaseActivity {
	private void launchSearch(String text) {
		Intent intent = new Intent(Intents.Encode.ACTION);
		intent.addFlags(Intents.FLAG_NEW_DOC);
		intent.putExtra(Intents.Encode.TYPE, Contents.Type.TEXT);
		intent.putExtra(Intents.Encode.DATA, text);
		intent.putExtra(Intents.Encode.FORMAT, BarcodeFormat.QR_CODE.toString());
		startActivity(intent);
	}
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.share);
		
		findViewById(R.id.share_text_view).setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View view, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
					String text = ((TextView) view).getText().toString();
					if (!TextUtils.isEmpty(text)) {
						launchSearch(text);
					}
					return true;
				}
				return false;
			}
		});
	}
}
