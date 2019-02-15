package com.yline.jetpack.binding;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.yline.base.BaseAppCompatActivity;
import com.yline.jetpack.R;
import com.yline.jetpack.binding.manager.BindingEventManager;
import com.yline.jetpack.binding.model.UserInfoModel;
import com.yline.jetpack.databinding.ActivityBindingBinding;

public class BindingActivity extends BaseAppCompatActivity {
    public static void launch(Context context) {
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, BindingActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private UserInfoModel userInfoModel;
    private BindingEventManager eventManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 绑定数据
        ActivityBindingBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_binding);
        userInfoModel = new UserInfoModel();
        dataBinding.setUserInfo(userInfoModel);

        eventManager = new BindingEventManager();
        dataBinding.setEvent(eventManager);
    }
}
