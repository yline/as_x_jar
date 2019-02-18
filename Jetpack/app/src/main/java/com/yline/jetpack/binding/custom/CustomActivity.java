package com.yline.jetpack.binding.custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.yline.base.BaseAppCompatActivity;
import com.yline.jetpack.R;
import com.yline.jetpack.databinding.ActivityCustomBinding;

/**
 * 自定义属性
 *
 * @author yline 2019/2/18 -- 17:54
 */
public class CustomActivity extends BaseAppCompatActivity {
    public static void launch(Context context) {
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, CustomActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCustomBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_custom);

    }
}
