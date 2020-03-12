package com.ioc.xutils3.sample.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;

import com.yline.base.BaseFragmentActivity;

/**
 * Activity中注入
 * zc.view().inject(this);
 * zc.view().inject(mHolder,this.getWindow().getDecorView());
 */
@SuppressLint("Registered")
public class IocFragmentActivity extends BaseFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        x.view().inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
