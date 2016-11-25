package com.time.calendar.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CalendarFragment extends Fragment
{
	
	private CalendarGridView calendarGridView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		calendarGridView = new CalendarGridView(getActivity(), null);
		return calendarGridView;
	}
	
	public void setData(final MyCalendarView.CalendarData data)
	{
		new Handler().post(new Runnable()
		{
			
			@Override
			public void run()
			{
				calendarGridView.resetData(data);
			}
		});
	}
	
	public MyCalendarView.CalendarData getCalendarData()
	{
		return calendarGridView.getCalendarData();
	}
	
	
	@Override
	public void onPause()
	{
		super.onPause();
		MyCalendarView.addTheMonth(getCalendarData());
	}
}
