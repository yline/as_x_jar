package com.glide.activity;

import android.os.Bundle;
import android.view.View;

import com.glide.R;
import com.yline.base.BaseAppCompatActivity;

public class MainActivity extends BaseAppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn_list).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ListActivity.actionStart(MainActivity.this);
			}
		});
	}
}
