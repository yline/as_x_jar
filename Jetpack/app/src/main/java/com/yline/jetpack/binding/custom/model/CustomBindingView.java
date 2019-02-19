package com.yline.jetpack.binding.custom.model;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.yline.jetpack.R;
import com.yline.jetpack.databinding.CustomBindingViewBinding;

/**
 * 自定义View，通过DataBinding实现
 *
 * @author yline 2019/2/19 -- 9:55
 */
public class CustomBindingView extends FrameLayout {
    private CustomModel mCustomModel;

    public CustomBindingView(Context context) {
        this(context, null);
    }

    public CustomBindingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        CustomBindingViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_binding_view, this, true);
        mCustomModel = new CustomModel();
        binding.setValueModel(mCustomModel);
    }

    public void setData(CustomModel customModel) {
        mCustomModel.setName(customModel.getName());
        mCustomModel.setPhone(customModel.getPhone());
        mCustomModel.setAge(customModel.getAge());
        mCustomModel.notifyChange();
    }
}
