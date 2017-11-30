package com.yline.main;

import android.os.Bundle;
import android.view.View;

import com.yline.basicfeatures.BasicFeaturesActivity;
import com.yline.basicfeatures.BasicFeaturesLargeActivity;
import com.yline.basicfeatures.BasicFeaturesRegionActivity;
import com.yline.configuration.ConfigurationActivity;
import com.yline.event.EventHandlingActivity;
import com.yline.event.EventHandlingAdvancedActivity;
import com.yline.extension.ExtensionCircleActivity;
import com.yline.extension.ExtensionFreehandActivity;
import com.yline.extension.ExtensionPinActivity;
import com.yline.test.BaseTestActivity;
import com.yline.viewpager.ViewPagerActivity;

public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        // 基础展示
        addButton("BasicFeaturesActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BasicFeaturesActivity.launcher(MainActivity.this);
            }
        });
        addButton("BasicFeaturesLargeActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BasicFeaturesLargeActivity.launcher(MainActivity.this);
            }
        });
        addButton("BasicFeaturesRegionActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BasicFeaturesRegionActivity.launcher(MainActivity.this);
            }
        });

        // 事件处理
        addButton("EventHandlingActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventHandlingActivity.launcher(MainActivity.this);
            }
        });
        addButton("EventHandlingAdvancedActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventHandlingAdvancedActivity.launcher(MainActivity.this);
            }
        });

        // ViewPager展示
        addButton("ViewPagerActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPagerActivity.launcher(MainActivity.this);
            }
        });

        // 设置 api
        addButton("Configuration", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfigurationActivity.launcher(MainActivity.this);
            }
        });

        // 继承，实现更多
        addButton("ExtensionPinActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExtensionPinActivity.launcher(MainActivity.this);
            }
        });
        addButton("ExtensionCircleActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExtensionCircleActivity.launcher(MainActivity.this);
            }
        });
        addButton("ExtensionFreehandActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExtensionFreehandActivity.launcher(MainActivity.this);
            }
        });
    }
}
