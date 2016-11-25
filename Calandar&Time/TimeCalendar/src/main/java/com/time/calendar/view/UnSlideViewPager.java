package com.time.calendar.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class UnSlideViewPager extends ViewPager implements Runnable
{
	private boolean mScrollable = false;

	private long mTime;

	public UnSlideViewPager(Context context)
	{
		super(context);
	}

	public UnSlideViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	/**
	 * 设置是否可以横向滑动
	 * @param enable True：允许滑动；False：不允许
	 */
	public void setScrollable(boolean enable)
	{
		mScrollable = enable;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event)
	{
		return mScrollable && super.onInterceptTouchEvent(event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return mScrollable && super.onTouchEvent(event);
	}

	public void stopAnimation()
	{
		removeCallbacks(this);
	}

	@Override
	public void run()
	{
		setCurrentItem(getCurrentItem() + 1, true);
		postDelayed(this, mTime);
	}

}
