package com.yline.jetpack.binding.custom.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yline.jetpack.R;
import com.yline.jetpack.binding.custom.model.CustomModel;

/**
 * 旧式，自定义View; 通过setData让xml中设置数据
 *
 * @author yline 2019/2/19 -- 9:39
 */
public class CustomView extends FrameLayout {
    private TextView mName;
    private TextView mPhone;
    private TextView mAge;

    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.custom_view, this);

        mName = findViewById(R.id.custom_view_name);
        mPhone = findViewById(R.id.custom_view_phone);
        mAge = findViewById(R.id.custom_view_age);
    }

    // 自动 Setter
    public void setData(CustomModel model) {
        mName.setText(model.getName());
        mPhone.setText(model.getPhone());
        mAge.setText(String.valueOf(model.getAge()));
    }
}
