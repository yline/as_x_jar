package com.yline.jetpack.binding.observe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;

import com.yline.base.BaseAppCompatActivity;
import com.yline.jetpack.BR;
import com.yline.jetpack.R;
import com.yline.jetpack.binding.observe.model.ObserveFiledModel;
import com.yline.jetpack.binding.observe.model.ObserveModel;
import com.yline.jetpack.databinding.ActivityObserveBinding;

/**
 * xml -> Java [数据双向绑定]
 * 1，BaseObservable
 * 2，ObservableField 包装一层
 * 3，ObservableArrayList 直接使用
 *
 * @author yline 2019/2/18 -- 16:24
 */
public class ObserveActivity extends BaseAppCompatActivity {
    public static void launch(Context context) {
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, ObserveActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityObserveBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_observe);
        binding.setPresenter(this);
        binding.setValueModel(new ObserveModel("yline", "15812345678", 21));

        binding.setFiledModel(new ObserveFiledModel("15812345678"));
        binding.setDataList(new ObservableArrayList<String>());
    }

    public void onUpdateName(ObserveModel model) {
        model.setName("yline" + System.currentTimeMillis());
        // model.notifyPropertyChanged(BR.valueModel); 这个API，经常性找不到对应的值；因此不好用，少用
        model.notifyChange();
    }

    public void onUpdateAge(ObserveModel model) {
        model.setAge((int) (System.currentTimeMillis() % 100));
        model.notifyChange();
    }

    public void onUpdatePhone(ObserveModel model) {
        model.setPhone("159" + String.valueOf(System.currentTimeMillis()));
        model.notifyChange();
    }

    public void onUpdateModel(ObserveModel model) {
        model.setName("yline" + System.currentTimeMillis());
        model.setPhone("159" + String.valueOf(System.currentTimeMillis()));
        model.setAge((int) (System.currentTimeMillis() % 100));
        model.notifyChange();
    }

    public void onUpdatePhone(ObserveFiledModel model) {
        model.setPhone("159" + String.valueOf(System.currentTimeMillis()));
    }

    public void onUpdateList(ObservableList<String> list) {
        list.clear();
        for (int i = 0; i < 3; i++) {
            list.add(System.currentTimeMillis() % 100 + "-" + i);
        }
    }
}



































