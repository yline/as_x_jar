package com.butter.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.butter.R;
import com.yline.base.BaseAppCompatActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseAppCompatActivity
{
	@BindView(R.id.btn_one)
	public Button btnOne;

	@BindViews({R.id.tv_one, R.id.tv_two, R.id.tv_three})
	public List<TextView> tvList;

	@OnClick(R.id.btn_one)
	public void showToast()
	{
		MainApplication.toast("this is a on click");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ButterKnife.bind(this);

		btnOne.setText("this is the one");

		tvList.get(0).setText("this is text view one");
		tvList.get(1).setText("this is text view two");
		tvList.get(2).setText("this is text view three");
	}
}
