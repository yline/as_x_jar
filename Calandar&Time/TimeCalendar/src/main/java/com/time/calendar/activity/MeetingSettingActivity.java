package com.time.calendar.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.time.calendar.R;
import com.time.calendar.view.MyCalendarView;

import java.util.ArrayList;
import java.util.List;

public class MeetingSettingActivity extends FragmentActivity implements OnClickListener, OnCheckedChangeListener
{
	public static final int REQ_SELECT_DATE = 0x001;

	private TextView mTvOk;

	private ImageView mImgvBack;

	private Button mBtnSelect;

	private RadioGroup mRgTimeType;

	private MeetingSelectFragment meetingSelectFragment;

	private MeetingNoFliterSelectFragment meetingSelectFragment_noFilter;

	private List<Fragment> mFragments = new ArrayList<Fragment>();

	private boolean mIsOutMeeting;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meeting_settime);

		mIsOutMeeting = getIntent().getBooleanExtra("isOutMeeting", false);
		mTvOk = (TextView) findViewById(R.id.tv_ok);
		mImgvBack = (ImageView) findViewById(R.id.tv_back);
		mBtnSelect = (Button) findViewById(R.id.btn_select);
		mRgTimeType = (RadioGroup) findViewById(R.id.rg_meeting_type);

		mTvOk.setOnClickListener(this);
		mImgvBack.setOnClickListener(this);
		mBtnSelect.setOnClickListener(this);
		mRgTimeType.setOnCheckedChangeListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.tv_back:
				onBackPressed();
				break;
			case R.id.tv_ok:

				break;
			case R.id.btn_select:
				startActivityForResult(new Intent(this, SelectDateActivity.class), REQ_SELECT_DATE);
				break;

			default:
				break;
		}
	}

	private FragmentTransaction getTransaction()
	{
		return getSupportFragmentManager().beginTransaction();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK)
		{
			return;
		}
		// TODO　to del
		ArrayList<Integer> filterTimePosition = new ArrayList<Integer>();
		filterTimePosition.add(1);
		filterTimePosition.add(2);
		filterTimePosition.add(5);
		filterTimePosition.add(6);
		filterTimePosition.add(7);
		filterTimePosition.add(8);
		filterTimePosition.add(10);

		// TODO　to del
		ArrayList<Integer> filterDay = new ArrayList<Integer>();
		filterDay.add(25);
		filterDay.add(26);
		filterDay.add(27);
		filterDay.add(28);
		filterDay.add(29);
		filterDay.add(30);
		filterDay.add(31);

		// TODO to del
		MyCalendarView.CalendarData calendarData = new MyCalendarView.CalendarData();
		calendarData.setYear(2015);
		calendarData.setMonth(3);
		calendarData.setFilterDay(filterDay);

		// TODO 获得服务器筛选数据 此处为假数据
		if (meetingSelectFragment != null)
		{
			getTransaction().remove(meetingSelectFragment).commit();
		}
		meetingSelectFragment = new MeetingSelectFragment();

		getTransaction().add(R.id.fragment_content, meetingSelectFragment).commit();

		meetingSelectFragment.setData(filterTimePosition, calendarData, mIsOutMeeting);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId)
	{
		switch (group.getCheckedRadioButtonId())
		{
			case R.id.rb_inner_meeting:// 智能筛选时间
				if (meetingSelectFragment == null)
				{
					getTransaction().hide(meetingSelectFragment_noFilter).commit();
				}
				else
				{
					getTransaction().hide(meetingSelectFragment_noFilter).commit();
					getTransaction().show(meetingSelectFragment).commit();
				}
				break;
			case R.id.rb_outer_meeting:// 自行选择时间
				if (meetingSelectFragment_noFilter == null)
				{
					meetingSelectFragment_noFilter = new MeetingNoFliterSelectFragment();
					getTransaction().add(R.id.fragment_content, meetingSelectFragment_noFilter).commit();
					if (mIsOutMeeting)
					{
						meetingSelectFragment_noFilter.setMeetingTypeOut();
					}
				}
				getTransaction().show(meetingSelectFragment_noFilter).commit();
				if (meetingSelectFragment != null)
				{
					getTransaction().hide(meetingSelectFragment).commit();
				}
				break;
			default:
				break;
		}
	}
}
