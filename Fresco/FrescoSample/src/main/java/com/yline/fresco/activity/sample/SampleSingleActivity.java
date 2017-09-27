package com.yline.fresco.activity.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yline.base.BaseAppCompatActivity;
import com.yline.fresco.sample.R;
import com.yline.fresco.view.FrescoView;

public class SampleSingleActivity extends BaseAppCompatActivity {
    private static final String TAG = "xxx-";

    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, SampleSingleActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_single);

        FrescoView frescoView = (FrescoView) findViewById(R.id.fresco_view_single);
    }
}
