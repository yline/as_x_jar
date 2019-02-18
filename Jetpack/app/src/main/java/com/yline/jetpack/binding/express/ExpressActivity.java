package com.yline.jetpack.binding.express;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;

import com.yline.base.BaseAppCompatActivity;
import com.yline.jetpack.R;
import com.yline.jetpack.binding.express.helper.ExpressPresenter;
import com.yline.jetpack.binding.express.model.ExpressModel;
import com.yline.jetpack.databinding.ActivityExpressBinding;

import java.util.Arrays;
import java.util.HashMap;

/**
 * xml -> Java的使用 [不支持merge]
 *
 * @author yline 2019/2/18 -- 11:31
 */
public class ExpressActivity extends BaseAppCompatActivity {
    public static void launch(Context context) {
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, ExpressActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityExpressBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_express);

        dataBinding.setStrValue("yline");
        dataBinding.setIntValue(0);
        dataBinding.setValueList(Arrays.asList("list-0", "list-1", "list-2"));

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("0", "map-0");
        hashMap.put("1", "map-1");
        hashMap.put("2", "map-2");
        dataBinding.setValueMap(hashMap);

        dataBinding.setValueModel(new ExpressModel("yline", true, 24));

        dataBinding.setPresenter(new ExpressPresenter());
    }
}
