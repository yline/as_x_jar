package com.yline.framework.router.impl;

import android.content.Context;

import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.yline.framework.router.RouterConstant;

public class TestImpl {
    public void launchTestActivity(Context context) {
        final String target = RouterConstant.Test.ACTIVITY_TEST;
        ARouter.getInstance().build(target).navigation(context);
    }

    public void launchTestActivity(Context context, NavigationCallback callback) {
        final String target = RouterConstant.Test.ACTIVITY_TEST;
        ARouter.getInstance().build(target).navigation(context, callback);
    }


}
