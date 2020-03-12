package com.ioc.xutils3.sample.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.ioc.xutils3.lib.com.ioc.xutils3.lib.view.annomation.ContentView;
import com.ioc.xutils3.sample.R;
import com.ioc.xutils3.sample.base.IocFragmentActivity;

@ContentView(R.layout.activity_second)
public class SecondActivity extends IocFragmentActivity {
    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentManager.beginTransaction().add(R.id.ll_fragment, new SecondFragment()).commit();
    }
}
