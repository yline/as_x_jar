package com.ioc.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.f21.ioc.R;
import com.ioc.base.BaseApplication;
import com.ioc.base.BaseFragment;
import com.lib.ioc.view.annomation.ContentView;
import com.lib.ioc.view.annomation.Event;
import com.lib.ioc.view.annomation.ViewInject;

@ContentView(R.layout.fragment_second)
public class SecondFragment extends BaseFragment{

	@ViewInject(R.id.tv_second)
	private TextView mTvSecond;
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		mTvSecond.setText("second After");
	}
	
	@Event(R.id.btn_second)
	private void onClick(View v){
		BaseApplication.finishAllActivity();
	}
}













