package com.yline.router;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.yline.framework.router.RouterManager;
import com.yline.moduletest.TestActivity;
import com.yline.test.BaseTestActivity;
import com.yline.utils.LogUtil;

public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("跳转到测试页面(原生方式)", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestActivity.launch(MainActivity.this);
            }
        });

        addButton("跳转到测试页面(router方式)", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouterManager.moduleTest().launchTestActivity(MainActivity.this);
            }
        });

        addButton("跳转到测试页面(router + callback方式)", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouterManager.moduleTest().launchTestActivity(MainActivity.this, new NavigationCallback() {
                    @Override
                    public void onFound(Postcard postcard) {
                        LogUtil.v("onArrival: 找到了 ");
                    }

                    @Override
                    public void onLost(Postcard postcard) {
                        LogUtil.v("onArrival: 找不到了 ");
                    }

                    @Override
                    public void onArrival(Postcard postcard) {
                        LogUtil.v("onArrival: 跳转完了 ");
                    }

                    @Override
                    public void onInterrupt(Postcard postcard) {
                        LogUtil.v("onArrival: 被拦截了 ");
                    }
                });
            }
        });
    }
}
