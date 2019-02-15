package com.yline.jetpack.binding.manager;

import android.view.View;

import com.yline.application.SDKManager;
import com.yline.jetpack.binding.model.UserInfoModel;
import com.yline.utils.LogUtil;

import java.util.Random;

public class BindingEventManager {
    public void onClickToast(View view) {
        SDKManager.toast("给你的吐司！！！");
    }

    public void onClickModel(UserInfoModel userInfoModel) {
        int age = new Random().nextInt(40);
        LogUtil.v("age = " + age);

        userInfoModel.setAge(age);
        userInfoModel.setName("yline-" + age);
        userInfoModel.setVisible(age % 2 == 0);
        userInfoModel.notifyChange(); // 通知所有绑定的数据，对应的UI修改
    }
}
