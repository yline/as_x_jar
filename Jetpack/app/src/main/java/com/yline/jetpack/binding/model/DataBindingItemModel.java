package com.yline.jetpack.binding.model;

import java.io.Serializable;

public class DataBindingItemModel implements Serializable {
    private int type;
    private String text;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
