package com.zxing.demo;

import android.os.Bundle;

import android.view.View;
import com.google.zxing.client.android.CaptureActivity;
import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("CaptureActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CaptureActivity.launch(MainActivity.this);
            }
        });
    }
}
