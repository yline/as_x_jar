package com.yline.fresco.activity.lib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.yline.base.BaseAppCompatActivity;
import com.yline.fresco.FrescoManager;
import com.yline.fresco.sample.R;
import com.yline.fresco.view.FrescoView;

/**
 * FrescoManger测试工程
 *
 * @author yline 2017/9/25 -- 20:20
 * @version 1.0.0
 */
public class FrescoManagerActivity extends BaseAppCompatActivity {

    private FrescoView frescoView;

    private TextView tvHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco_manager);

        frescoView = (FrescoView) findViewById(R.id.manager_fresco_view);
        tvHint = (TextView) findViewById(R.id.manager_tv_hint);

        FrescoManager.setImageResource(frescoView, R.drawable.image_background);
        tvHint.setText("显示了Drawable目录");
    }

    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, FrescoManagerActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }
}
