package com.picasso.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.picasso.R;
import com.yline.base.BaseAppCompatActivity;

public class MainActivity extends BaseAppCompatActivity
{
	private ImageView ivShow;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ivShow = (ImageView) findViewById(R.id.iv_show);

		findViewById(R.id.btn_base).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ListActivity.actionStart(MainActivity.this);
			}
		});
	}
}
