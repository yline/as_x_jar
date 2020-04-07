package com.yline.modulefunction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yline.framework.router.RouterManager;
import com.yline.test.BaseTestActivity;

@Route(path = RouterManager.Function.TWO_ACTIVITY)
public class TwoActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addTextView("TwoActivity");

        addButton("点击成功结束", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("data", "success");
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });
    }
}
