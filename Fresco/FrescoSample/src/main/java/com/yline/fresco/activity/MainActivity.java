package com.yline.fresco.activity;

import android.os.Bundle;
import android.view.View;

import com.yline.fresco.activity.lib.FrescoManagerActivity;
import com.yline.fresco.activity.sample.RecyclerActivity;
import com.yline.fresco.activity.sample.SingleActivity;
import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("Single 简单的测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleActivity.launcher(MainActivity.this);
            }
        });

        addButton("Recycler, 多个展示", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerActivity.launcher(MainActivity.this);
            }
        });

        addButton("FrescoManager 案例", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrescoManagerActivity.launcher(MainActivity.this);
            }
        });
    }
}
