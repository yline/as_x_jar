package com.yline.jetpack.binding.custom.model;

import android.databinding.BaseObservable;

import java.io.Serializable;

public class CustomModel extends BaseObservable implements Serializable {
    private String name;
    private String phone;
    private int age;

    public CustomModel() {
    }

    public CustomModel(String name, String phone, int age) {
        this.name = name;
        this.phone = phone;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
