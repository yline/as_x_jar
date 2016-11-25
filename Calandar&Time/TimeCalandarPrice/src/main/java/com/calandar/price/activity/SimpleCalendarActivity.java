package com.calandar.price.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.calandar.price.R;
import com.calandar.price.utils.DateUtils;
import com.calandar.price.view.CommonCalendarView;

public class SimpleCalendarActivity extends AppCompatActivity
{

	private CommonCalendarView calendarView;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simple_calendar);
		this.calendarView = (CommonCalendarView) findViewById(R.id.calendarView);
		this.calendarView.setMinDate(DateUtils.stringtoDate("1937-01-01", "yyyy-MM-dd"));
		this.calendarView.setMaxDate(DateUtils.stringtoDate("2100-01-22", "yyyy-MM-dd"));
		this.calendarView.init(null);
	}
}
