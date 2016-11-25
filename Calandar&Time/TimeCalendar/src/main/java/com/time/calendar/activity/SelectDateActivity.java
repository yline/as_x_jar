package com.time.calendar.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.time.calendar.R;

public class SelectDateActivity extends FragmentActivity implements
		OnClickListener, OnItemClickListener
{

	private TextView mTvSelect;

	private ImageView mImgvBack;

	private GridView mGvDuration;

	private Animation mAnimZoomIn;

	private DurationAdapter mAdapter;

	private int mSelectHourPosition = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meeting_select_day);
		mImgvBack = (ImageView) findViewById(R.id.tv_back);
		mTvSelect = (TextView) findViewById(R.id.tv_select);
		mGvDuration = (GridView) findViewById(R.id.grid_duration);

		mTvSelect.setOnClickListener(this);
		mImgvBack.setOnClickListener(this);
		mGvDuration.setOnItemClickListener(this);

		mAdapter = new DurationAdapter();
		mGvDuration.setAdapter(mAdapter);

		mAnimZoomIn = AnimationUtils.loadAnimation(this,
				R.anim.chat_button_zoom_in);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.tv_back:
				onBackPressed();
				break;
			case R.id.tv_select:// 筛选
				// TODO 向服务器发送筛选条件，并获得数据返回
				double hour = 0.5 * (mSelectHourPosition + 1);
				String hourStr = String.format("%1.1f", hour);
				setResult(RESULT_OK);
				onBackPressed();
				break;

			default:
				break;
		}
	}

	public class DurationAdapter extends BaseAdapter
	{

		@Override
		public int getCount()
		{
			return 15;
		}

		@Override
		public Object getItem(int position)
		{
			return null;
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			TextView textView;
			if (convertView == null)
			{
				convertView = getLayoutInflater().inflate(
						R.layout.grid_item_duration, null);
				textView = (TextView) convertView.findViewById(R.id.tv_hour);
				convertView.setTag(textView);
			}
			else
			{
				textView = (TextView) convertView.getTag();
			}
			double hour = 0.5 * (position + 1);
			String hourStr = " " + String.format("%1.1f", hour) + "\n"
					+ "小时";
			textView.setText(hourStr);
			if (position == mSelectHourPosition)
			{
				textView.setTextColor(Color.WHITE);
				convertView.setBackgroundResource(R.drawable.bg_date_selected);
				convertView.startAnimation(mAnimZoomIn);
			}
			else
			{
				textView.setTextColor(Color.BLACK);
				convertView.setBackgroundColor(Color.WHITE);
			}
			return convertView;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
	                        long id)
	{
		if (position != mSelectHourPosition)
		{
			mSelectHourPosition = position;
		}
		else
		{
			mSelectHourPosition = -1;
		}
		mAdapter.notifyDataSetChanged();
	}
}
