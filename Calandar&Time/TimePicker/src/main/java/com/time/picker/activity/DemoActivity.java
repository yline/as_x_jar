package com.time.picker.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.time.picker.R;
import com.time.picker.wheel.SelectTimeView;

public class DemoActivity extends Activity implements SelectTimeView.OnTimeGotListener, View.OnClickListener
{
	private SelectTimeView timeView;

	private TextView tvTime;

	private ImageView im;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		timeView = new SelectTimeView(this);
		timeView.setOnTimeGotListener(this);

		tvTime = (TextView) findViewById(R.id.tv_time);
		tvTime.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		if (v == tvTime)
		{
			timeView.show(tvTime);
			Toast.makeText(this, "tvTime", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void OnTimeGot(String time)
	{
		tvTime.setText(time);
	}
}
