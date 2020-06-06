package com.yline.moduletest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yline.framework.router.RouterManager;
import com.yline.test.BaseTestActivity;
import com.yline.utils.LogUtil;

import androidx.annotation.Nullable;

@Route(path = RouterManager.Test.TEST_ACTIVITY)
public class TestAActivity extends BaseTestActivity {
    public static void launch(Context context) {
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, TestAActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private static final int REQUEST_CODE = 1001;

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addTextView("TestAActivity");

        addButton("Activity 跳转携带参数", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouterManager.moduleFunction().launchOneActivity(TestAActivity.this, "yline", 25);
            }
        });

        addButton("StartActivityForResult", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouterManager.moduleFunction().launchTwoActivityForResult(TestAActivity.this, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        LogUtil.v("requestCode = " + requestCode + ", resultCode = " + resultCode + ", data = " + data);
        if (null != data){
            String content = data.getStringExtra("data");
            LogUtil.v("data content = " + content);
        }
    }
}
