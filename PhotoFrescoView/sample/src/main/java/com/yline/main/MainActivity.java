package com.yline.main;

import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestActivity;

import com.yline.sample.RecyclerViewActivity;
import com.yline.sample.SingleActivity;
import com.yline.sample.ViewPagerActivity;

public class MainActivity extends BaseTestActivity {
    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("Single", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleActivity.launcher(MainActivity.this);
            }
        });

        addButton("ViewPager", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPagerActivity.launcher(MainActivity.this);
            }
        });

        addButton("RecyclerView", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerViewActivity.launcher(MainActivity.this);
            }
        });
    }
}
