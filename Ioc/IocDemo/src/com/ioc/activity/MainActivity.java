package com.ioc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.f21.ioc.R;
import com.ioc.base.BaseActivity;
import com.lib.ioc.view.annomation.ContentView;
import com.lib.ioc.view.annomation.Event;
import com.lib.ioc.view.annomation.ViewInject;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
	
	@ViewInject(R.id.tv_main)
    private TextView mTvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mTvMain.setText("Main After");
    }
    
    @Event(R.id.btn_main)
    private void onClick(View v){
    	Intent intent = new Intent();
    	intent.setClass(this, SecondActivity.class);
    	this.startActivity(intent);
    }
    
}
