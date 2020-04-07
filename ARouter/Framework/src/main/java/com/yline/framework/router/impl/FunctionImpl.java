package com.yline.framework.router.impl;

import android.app.Activity;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.yline.framework.router.RouterManager;

public class FunctionImpl {
    public void launchOneActivity(Context context, String name, int age) {
        final String target = RouterManager.Function.ONE_ACTIVITY;
        ARouter.getInstance().build(target)
                .withString("name", name)
                .withInt("age", age)
                .navigation(context);
    }

    public void launchTwoActivityForResult(Activity activity, int requestCode) {
        final String target = RouterManager.Function.TWO_ACTIVITY;
        ARouter.getInstance().build(target).navigation(activity, requestCode);
    }
}
