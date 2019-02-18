package com.yline.jetpack.binding.express.helper;

import android.view.View;

import com.yline.application.SDKManager;
import com.yline.jetpack.binding.express.model.ExpressModel;
import com.yline.utils.LogUtil;

import java.util.Random;

public class ExpressPresenter {
    public void onClickToast(View view) {
        SDKManager.toast("给你的吐司！！！");
    }

    public void onClickModel(ExpressModel expressModel) {
        int age = new Random().nextInt(40);
        LogUtil.v("age = " + age);

        expressModel.setAge(age);
        expressModel.setNickName("yline-" + age);
        expressModel.setVisible(age % 2 == 0);
        expressModel.notifyChange(); // 通知所有绑定的数据，对应的UI修改
    }
}
