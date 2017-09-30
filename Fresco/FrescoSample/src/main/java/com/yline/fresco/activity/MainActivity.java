package com.yline.fresco.activity;

import android.os.Bundle;
import android.view.View;

import com.yline.fresco.activity.lib.LibControllerDynamicActivity;
import com.yline.fresco.activity.lib.LibControllerLowerActivity;
import com.yline.fresco.activity.lib.LibControllerUriActivity;
import com.yline.fresco.activity.lib.LibProcessorUriActivity;
import com.yline.fresco.activity.lib.LibResourceActivity;
import com.yline.fresco.activity.lib.LibUriActivity;
import com.yline.fresco.activity.lib.LibUriParamActivity;
import com.yline.fresco.activity.sample.DebugActivity;
import com.yline.fresco.activity.sample.SampleBitmapActivity;
import com.yline.fresco.activity.sample.SampleCallbackActivity;
import com.yline.fresco.activity.sample.SamplePrefetchActivity;
import com.yline.fresco.activity.sample.SampleProgressiveLoadingActivity;
import com.yline.fresco.activity.sample.SampleRecyclerActivity;
import com.yline.fresco.activity.sample.SampleSingleActivity;
import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("Lib setImageResource", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LibResourceActivity.launcher(MainActivity.this);
            }
        });

        addButton("Lib setImageUri", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LibUriActivity.launcher(MainActivity.this);
            }
        });

        addButton("Lib setImageUri(Param)", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LibUriParamActivity.launcher(MainActivity.this);
            }
        });

        addButton("Lib setImageUri(Controller)", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LibControllerUriActivity.launcher(MainActivity.this);
            }
        });

        addButton("Lib setImageUri(Lower)", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LibControllerLowerActivity.launcher(MainActivity.this);
            }
        });

        addButton("Lib setDynamicUri", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LibControllerDynamicActivity.launcher(MainActivity.this);
            }
        });

        addButton("Lib setProcessorUri", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LibProcessorUriActivity.launcher(MainActivity.this);
            }
        });

        addButton("Sample 预加载", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SamplePrefetchActivity.launcher(MainActivity.this);
            }
        });

        addButton("Sample 渐进式加载", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SampleProgressiveLoadingActivity.launcher(MainActivity.this);
            }
        });

        addButton("Sample Callback 回调", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SampleCallbackActivity.launcher(MainActivity.this);
            }
        });

        addButton("Sample GetBitmap", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SampleBitmapActivity.launcher(MainActivity.this);
            }
        });

        addButton("Sample Single", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SampleSingleActivity.launcher(MainActivity.this);
            }
        });

        addButton("Sample Recycler", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SampleRecyclerActivity.launcher(MainActivity.this);
            }
        });

        // Fresco Bug
        addButton("DebugActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DebugActivity.launcher(MainActivity.this);
            }
        });
    }
}
