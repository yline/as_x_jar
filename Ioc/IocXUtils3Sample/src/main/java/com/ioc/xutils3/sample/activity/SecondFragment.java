package com.ioc.xutils3.sample.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.ioc.xutils3.lib.com.ioc.xutils3.lib.view.annomation.ContentView;
import com.ioc.xutils3.lib.com.ioc.xutils3.lib.view.annomation.Event;
import com.ioc.xutils3.lib.com.ioc.xutils3.lib.view.annomation.ViewInject;
import com.ioc.xutils3.sample.R;
import com.ioc.xutils3.sample.base.IocFragment;

@ContentView(R.layout.fragment_second)
public class SecondFragment extends IocFragment
{

	@ViewInject(R.id.tv_second)
	private TextView mTvSecond;
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
		mTvSecond.setText("second After");
	}
	
	@Event(R.id.btn_second)
	private void onClick(View v)
	{
		MainApplication.finishActivity();
	}
}













