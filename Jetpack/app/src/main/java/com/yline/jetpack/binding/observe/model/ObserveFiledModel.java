package com.yline.jetpack.binding.observe.model;

import android.databinding.ObservableField;

public class ObserveFiledModel {
    // 自动更新
    public ObservableField<String> mPhone = new ObservableField<>();

    public ObserveFiledModel(String phone) {
        this.mPhone.set(phone);
    }

    public void setPhone(String phone) {
        this.mPhone.set(phone);
    }

    public String getPhone() {
        return mPhone.get();
    }
}
