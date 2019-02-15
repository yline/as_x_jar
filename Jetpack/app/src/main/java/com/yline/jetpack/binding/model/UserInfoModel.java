package com.yline.jetpack.binding.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

import java.io.Serializable;

public class UserInfoModel extends BaseObservable implements Serializable {
    private String name;
    private int age;
    private boolean visible;

    @Bindable
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        notifyPropertyChanged(BR.visible); // 加上 @Bindable，clean后，出现
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
