package com.ioc.xutils3.sample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ioc.xutils3.lib.com.ioc.xutils3.lib.view.annomation.ContentView;
import com.ioc.xutils3.lib.com.ioc.xutils3.lib.view.annomation.Event;
import com.ioc.xutils3.lib.com.ioc.xutils3.lib.view.annomation.ViewInject;
import com.ioc.xutils3.sample.R;
import com.ioc.xutils3.sample.base.IocActivity;


@ContentView(R.layout.activity_main)
public class MainActivity extends IocActivity {

    @ViewInject(R.id.tv_main)
    private TextView mTvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTvMain.setText("Main After");
    }

    @Event(R.id.btn_main)
    private void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(this, SecondActivity.class);
        this.startActivity(intent);
    }

}
