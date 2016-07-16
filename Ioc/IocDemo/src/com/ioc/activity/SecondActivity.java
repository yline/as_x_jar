package com.ioc.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.f21.ioc.R;
import com.ioc.base.BaseFragmentActivity;
import com.lib.ioc.view.annomation.ContentView;

@ContentView(R.layout.activity_second)
public class SecondActivity extends BaseFragmentActivity{
	private FragmentManager fragmentManager = getSupportFragmentManager();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		fragmentManager.beginTransaction().add(R.id.ll_fragment, new SecondFragment()).commit();
	}
}
