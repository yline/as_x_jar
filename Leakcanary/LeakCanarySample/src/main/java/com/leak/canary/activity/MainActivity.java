package com.leak.canary.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

import com.leak.canary.R;
import com.yline.base.BaseAppCompatActivity;

/**
 * https://github.com/square/leakcanary.git git地址
 * @author yline 2016/11/11 --> 23:16
 * @version 1.0.0
 */
public class MainActivity extends BaseAppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn_async_task).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				startAsyncTask();
			}
		});
	}

	void startAsyncTask()
	{
		// This async task is an anonymous class and therefore has a hidden reference to the outer
		// class MainActivity. If the activity gets destroyed before the task finishes (e.g. rotation),
		// the activity instance will leak.
		new AsyncTask<Void, Void, Void>()
		{
			@Override
			protected Void doInBackground(Void... params)
			{
				// Do some slow work in background
				SystemClock.sleep(20000);
				return null;
			}
		}.execute();
	}
}
