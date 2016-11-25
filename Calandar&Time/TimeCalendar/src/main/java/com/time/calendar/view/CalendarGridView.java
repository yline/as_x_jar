package com.time.calendar.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarGridView extends GridView
{
	private int mItemWidth;

	private int mItemHeight;

	private CalendarAdapter mAdapter;

	public CalendarGridView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setNumColumns(7);
		setSelector(new ColorDrawable(Color.TRANSPARENT));

		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;

		MyCalendarView.CalendarData data = new MyCalendarView.CalendarData();
		data.setYear(year);
		data.setMonth(month);

		mAdapter = new CalendarAdapter(context, data);
		setAdapter(mAdapter);

		// 获取item的宽高
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		mItemWidth = displayMetrics.widthPixels / 7;
		mItemHeight = dp2px(51);
	}

	public int dp2px(int dp)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getContext().getResources().getDisplayMetrics());
	}

	public void resetData(MyCalendarView.CalendarData data)
	{
		mAdapter.resetData(data);
	}

	// 重写onMeasure以全部展开GridView
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		switch (ev.getAction())
		{
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				getParent().requestDisallowInterceptTouchEvent(true);
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				getParent().requestDisallowInterceptTouchEvent(false);
				break;
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * @Description : 通过坐标点获取位置
	 * @auth : sid'pc
	 */
	private int getPosition(int x, int y)
	{
		y = y - getTop();
		int position_x = x / mItemWidth;
		int position_y = y / mItemHeight;
		int positon = position_y * 7 + position_x;
		return positon;
	}

	int position_start = 0;

	int position_move = -1;

	int position_flag = -1;

	ArrayList<Integer> mMoveDay;

	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		switch (ev.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				position_start = getPosition((int) ev.getX(), (int) ev.getY());
				setDaySelected(position_start, position_start);
				break;
			case MotionEvent.ACTION_MOVE:
				if (position_move == -1)
				{
					position_move = getPosition((int) ev.getX(), (int) ev.getY());
					return super.onTouchEvent(ev);
				}
				position_flag = getPosition((int) ev.getX(), (int) ev.getY());
				if (position_move != position_flag)
				{
					position_move = position_flag;
					setDaySelected(position_start, position_move);
				}
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				mAdapter.ChangeMoveDayToSelected();
				mMoveDay.clear();
				break;
			default:
				break;
		}
		return true;
	}

	/**
	 * @param moveCount
	 * @Description : 将滑动选中的起止点传入，并更新UI
	 * @auth : sid'pc
	 */
	private void setDaySelected(int position_start, int position_move)
	{
		mMoveDay = mAdapter.getMoveDay();
		mMoveDay.clear();
		int start = Math.min(position_start, position_move);
		int end = Math.max(position_start, position_move);
		for (int i = start; i <= end; i++)
		{
			mMoveDay.add(i);
		}
		mAdapter.notifyDataSetChanged();
	}

	public MyCalendarView.CalendarData getCalendarData()
	{
		return mAdapter.getCalendarData();
	}
}
