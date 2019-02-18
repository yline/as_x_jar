package com.yline.jetpack.binding.express.model;

import android.databinding.BaseObservable;

import java.io.Serializable;

public class ExpressModel extends BaseObservable implements Serializable {
    private String realName;
    private String nickName;

    private boolean visible;
    private int age;

    public ExpressModel(String nickName, boolean visible, int age) {
        this.nickName = nickName;
        this.visible = visible;
        this.age = age;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
