package com.ioc.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Activity中注入
 * zc.view().inject(this);
 * zc.view().inject(mHolder,this.getWindow().getDecorView());
 */
public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		BaseApplication.addActivity(this);
		
		if (BaseApplication.isInject) {
			x.view().inject(this);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		BaseApplication.removeActivity(this);
	}
}
