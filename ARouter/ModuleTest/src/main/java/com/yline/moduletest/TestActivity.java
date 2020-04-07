package com.yline.moduletest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.yline.framework.router.RouterConstant;
import com.yline.framework.router.RouterManager;
import com.yline.test.BaseTestActivity;
import com.yline.utils.LogUtil;

@Route(path = RouterConstant.Test.ACTIVITY_TEST)
public class TestActivity extends BaseTestActivity {
    public static void launch(Context context){
    	if (null != context){
    		Intent intent = new Intent();
    		intent.setClass(context, TestActivity.class);
    		if (!(context instanceof Activity)){
    			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		}
    		context.startActivity(intent);
    	}
    }
    
    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addTextView("TestActivity");


    }
}
