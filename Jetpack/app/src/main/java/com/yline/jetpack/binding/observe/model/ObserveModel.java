package com.yline.jetpack.binding.observe.model;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;

import java.io.Serializable;

public class ObserveModel extends BaseObservable implements Serializable {
    public ObservableField<String> mPhone = new ObservableField<>(); // 如果同时使用，则无效[不会自主更新]
    private String name;
    private int age;

    public ObserveModel(String name, String phone, int age) {
        this.name = name;
        this.mPhone.set(phone);
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return mPhone.get();
    }

    public void setPhone(String phone) {
        this.mPhone.set(phone);
    }
}
