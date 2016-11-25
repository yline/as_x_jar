package com.time.calendar.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.time.calendar.R;
import com.time.calendar.activity.MeetingNoFliterSelectFragment;
import com.time.calendar.activity.MeetingSelectFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyCalendarView extends RelativeLayout implements OnClickListener,
		OnPageChangeListener
{
	private Context context;

	private LayoutInflater inflater;

	private TextView mTvMonth;

	private ImageView mImgBack;

	private ImageView mImgForward;

	private ViewPager mViewPager;

	private MypagerAdapter mPagerAdapter;

	private String[] strMonths = new String[12];

	private static int mStartMonth;

	public static List<CalendarData> sTotalSelectDay;

	public MyCalendarView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.view_calendar, this);
		sTotalSelectDay = new ArrayList<CalendarData>();

		mTvMonth = (TextView) findViewById(R.id.tv_month);
		mImgBack = (ImageView) findViewById(R.id.img_back);
		mImgForward = (ImageView) findViewById(R.id.img_forward);
		mViewPager = (ViewPager) findViewById(R.id.view_pager);

		mImgBack.setOnClickListener(this);
		mImgForward.setOnClickListener(this);
		expandViewTouchDelegate(mImgBack, 100, 100, 100, 500);
		expandViewTouchDelegate(mImgForward, 100, 100, 500, 100);

		initView();
	}
	
	public static void expandViewTouchDelegate(final View view, final int top,
	                                           final int bottom, final int left, final int right)
	{

		((View) view.getParent()).post(new Runnable()
		{
			@Override
			public void run()
			{
				Rect bounds = new Rect();
				view.setEnabled(true);
				view.getHitRect(bounds);

				bounds.top -= top;
				bounds.bottom += bottom;
				bounds.left -= left;
				bounds.right += right;

				TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

				if (View.class.isInstance(view.getParent()))
				{
					((View) view.getParent()).setTouchDelegate(touchDelegate);
				}
			}
		});
	}

	private void initView()
	{
		// 寰楀埌鏈堜唤鐨凷tring鍊�
		strMonths[0] = context.getString(R.string.jan);
		strMonths[1] = context.getString(R.string.feb);
		strMonths[2] = context.getString(R.string.mar);
		strMonths[3] = context.getString(R.string.apr);
		strMonths[4] = context.getString(R.string.may);
		strMonths[5] = context.getString(R.string.jun);
		strMonths[6] = context.getString(R.string.jul);
		strMonths[7] = context.getString(R.string.aug);
		strMonths[8] = context.getString(R.string.sep);
		strMonths[9] = context.getString(R.string.oct);
		strMonths[10] = context.getString(R.string.nov);
		strMonths[11] = context.getString(R.string.dec);

		// 璁剧疆adapter鐨凢ragmentManager
		FragmentManager childFragmentManager = null;
		try
		{
			List<Fragment> fragments = ((FragmentActivity) context)
					.getSupportFragmentManager().getFragments();
			for (Fragment fragment : fragments)
			{
				if (fragment instanceof MeetingSelectFragment
						|| fragment instanceof MeetingNoFliterSelectFragment)
				{
					childFragmentManager = fragment.getChildFragmentManager();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (childFragmentManager != null)
		{
			mPagerAdapter = new MypagerAdapter(childFragmentManager);
		}
		else
		{
			FragmentManager fragmentManager = ((FragmentActivity) context)
					.getSupportFragmentManager();
			mPagerAdapter = new MypagerAdapter(fragmentManager);
		}

		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(5000);
		mViewPager.setOnPageChangeListener(this);

		// 杩欎釜鏄缃畍iewPager鍒囨崲杩囧害鏃堕棿鐨勭被
		ViewPagerScroller scroller = new ViewPagerScroller(context);
		scroller.initViewPagerScroll(mViewPager);

		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		mTvMonth.setText(strMonths[month - 1]);
	}

	public void setData(CalendarData calendarData)
	{
		sTotalSelectDay.add(calendarData);
		mPagerAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v)
	{
		int i = mViewPager.getCurrentItem();
		switch (v.getId())
		{
			case R.id.img_back:// 涓婁竴涓湀
				mViewPager.setCurrentItem(i - 1, true);
				break;
			case R.id.img_forward:// 涓嬩竴涓湀
				mViewPager.setCurrentItem(i + 1, true);
				break;
			default:
				break;
		}
	}

	public class MypagerAdapter extends FragmentStatePagerAdapter
	{

		private CalendarFragment mFragment;

		public MypagerAdapter(FragmentManager fm)
		{
			super(fm);
			Calendar cal = Calendar.getInstance();
			int sumMonth = cal.get(Calendar.YEAR) * 12
					+ cal.get(Calendar.MONTH) + 1;
			mStartMonth = sumMonth - 5000;
		}

		@Override
		public int getItemPosition(Object object)
		{
			return POSITION_NONE;
		}

		@Override
		public Fragment getItem(int position)
		{
			mFragment = new CalendarFragment();
			CalendarData data = getTheMonth(position);
			if (data == null)
			{
				data = new CalendarData();
				data.setYear(getYearByPosition(position));
				data.setMonth(getMonthByPosition(position));
			}
			mFragment.setData(data);
			return mFragment;
		}

		@Override
		public int getCount()
		{
			return 10000;
		}

		public CalendarData getCalendarData()
		{
			return mFragment.getCalendarData();
		}

	}

	private int getMonthByPosition(int position)
	{
		int resultMonth = (mStartMonth + position) % 12;
		if (resultMonth == 0)
		{
			resultMonth = 12;
		}
		return resultMonth;
	}

	private int getYearByPosition(int position)
	{
		int resultYear = (mStartMonth + position) / 12;
		if (getMonthByPosition(position) == 12)
		{
			resultYear = resultYear - 1;
		}
		return resultYear;
	}

	private static int getTheMonthPosition(int year, int month)
	{
		int position = year * 12 + month - mStartMonth;
		return position;
	}

	private CalendarData getTheMonth(int position)
	{
		for (CalendarData calendarData : sTotalSelectDay)
		{
			int monthPosition = getTheMonthPosition(calendarData.getYear(),
					calendarData.getMonth());
			if (position == monthPosition)
			{
				return calendarData;
			}
		}
		return null;
	}

	private static CalendarData getTheMonth(int year, int month)
	{
		CalendarData data;
		int position = getTheMonthPosition(year, month);
		for (CalendarData calendarData : sTotalSelectDay)
		{
			int monthPosition = getTheMonthPosition(calendarData.getYear(),
					calendarData.getMonth());
			if (position == monthPosition)
			{
				data = calendarData;
				return data;
			}
		}
		return null;
	}

	public static void addTheMonth(CalendarData data)
	{
		CalendarData calendarData = getTheMonth(data.getYear(), data.getMonth());
		if (calendarData == null && data.getSelectDay() != null && data.getSelectDay().size() > 0)
		{
			sTotalSelectDay.add(data);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0)
	{
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2)
	{

	}

	@Override
	public void onPageSelected(final int position)
	{
		mTvMonth.setText(strMonths[getMonthByPosition(position) - 1]);
	}

	public static class CalendarData
	{

		private int year;

		private int month;

		/**
		 * 绛涢�夊嚭鏉ョ殑鏃ユ湡
		 */
		private List<Integer> filterDay;

		/**
		 * 琚�変腑鐨勬棩鏈�
		 */
		private List<Integer> selectDay;

		public int getYear()
		{
			return year;
		}

		public int getMonth()
		{
			return month;
		}

		public List<Integer> getFilterDay()
		{
			return filterDay;
		}

		public void setYear(int year)
		{
			this.year = year;
		}

		public void setMonth(int month)
		{
			this.month = month;
		}

		public void setFilterDay(List<Integer> filterDay)
		{
			this.filterDay = filterDay;
		}

		public List<Integer> getSelectDay()
		{
			return selectDay;
		}

		public void setSelectDay(List<Integer> selectDay)
		{
			this.selectDay = selectDay;
		}

	}

	/**
	 * ViewPager 婊氬姩閫熷害璁剧疆
	 */
	public class ViewPagerScroller extends Scroller
	{
		private int mScrollDuration = 1000; // 婊戝姩閫熷害

		/**
		 * 璁剧疆閫熷害閫熷害
		 * @param duration
		 */
		public void setScrollDuration(int duration)
		{
			this.mScrollDuration = duration;
		}

		public ViewPagerScroller(Context context)
		{
			super(context);
		}

		public ViewPagerScroller(Context context, Interpolator interpolator)
		{
			super(context, interpolator);
		}

		public ViewPagerScroller(Context context, Interpolator interpolator,
		                         boolean flywheel)
		{
			super(context, interpolator, flywheel);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy,
		                        int duration)
		{
			super.startScroll(startX, startY, dx, dy, mScrollDuration);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy)
		{
			super.startScroll(startX, startY, dx, dy, mScrollDuration);
		}

		public void initViewPagerScroll(ViewPager viewPager)
		{
			try
			{
				Field mScroller = ViewPager.class.getDeclaredField("mScroller");
				mScroller.setAccessible(true);
				mScroller.set(viewPager, this);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
