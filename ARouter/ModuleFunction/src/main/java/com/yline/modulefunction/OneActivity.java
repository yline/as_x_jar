package com.yline.modulefunction;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.yline.framework.router.RouterManager;
import com.yline.test.BaseTestActivity;
import com.yline.utils.LogUtil;

import androidx.core.content.ContextCompat;

@Route(path = RouterManager.Function.ONE_ACTIVITY)
public class OneActivity extends BaseTestActivity {
    @Autowired(name = "name")
    public String name; // 这个还不能是 private类型

    @Autowired(name = "age")
    public int ageOut;

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addTextView("OneActivity");

        final TextView contentTextView = addTextView("");
        contentTextView.setTextColor(ContextCompat.getColor(OneActivity.this, android.R.color.holo_red_light));

        LogUtil.v("-------------------正常赋值------------------");
        LogUtil.v("name = " + name + ", age = " + ageOut);
        name = getIntent().getStringExtra("name");
        ageOut = getIntent().getIntExtra("age", -1);
        LogUtil.v("name = " + name + ", age = " + ageOut);

        LogUtil.v("-------------------注解赋值------------------");
        name = null;
        ageOut = -1;
        LogUtil.v("name = " + name + ", age = " + ageOut);
        ARouter.getInstance().inject(this);
        LogUtil.v("name = " + name + ", age = " + ageOut);

        contentTextView.setText("name = " + name + ", age = " + ageOut);
    }
}
