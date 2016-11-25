package com.time.picker.wheel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.time.picker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TimePickerView extends LinearLayout implements OnWheelScrollListener
{
	private View mContentView;

	/*
	 * 第一个滑轮
	 */
	private WheelView mWvFirst;

	/*
	 * 第二个滑轮
	 */
	private WheelView mWvSecond;

	/*
	 * 第三个滑轮
	 */
	private WheelView mWvThird;

	/**
	 * 存储天数选择的字符串数组
	 */
	private ArrayList<String> mDate;

	/**
	 * 控制是否需要早于某个时间点开关
	 */
	private boolean mCanBefor = true;

	/**
	 * 控制是否需要晚于某个时间点开关
	 */
	private boolean mCanOver = true;

	/**
	 * 起始时间
	 */
	private long mStartTime;

	/**
	 * 时间控件切换开关
	 */
	private boolean mIsSelecteDay = false;

	/**
	 * 储存第一个滑轮时间的Long型数据
	 */
	private List<Long> mLongs;

	private int yearX, monthX, dayX, dayNum, poor;

	private OnScrolledListener mListener;

	private SimpleDateFormat mFormat;

	private SimpleDateFormat mSimpleDateFormat;

	private Calendar mCalendar;

	/**
	 * 第一个滑轮的前后与现在位置的间隔
	 */
	private int mGap = 365 * 2;

	/**
	 * 给滑轮加锁 防止过快滑动出现误差
	 */
	private boolean mCanFirstScroll = false;

	private boolean mCanSecondScrool = false;

	private boolean mCanThirdScrool = false;

	private int diff;

	private boolean mResetDay = true;

	public TimePickerView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		LayoutInflater inflater = LayoutInflater.from(context);
		mContentView = inflater.inflate(R.layout.view_time_picker, this);
		mWvFirst = (WheelView) mContentView.findViewById(R.id.wv_first);
		mWvSecond = (WheelView) mContentView.findViewById(R.id.wv_second);
		mWvThird = (WheelView) mContentView.findViewById(R.id.wv_third);
		initWheelView();

		mWvFirst.addScrollingListener(this);
		mWvSecond.addScrollingListener(this);
		mWvThird.addScrollingListener(this);
	}

	/**
	 * @param
	 * @return void
	 * @throws
	 * @Method : initWheelView
	 * @Description : 初始化时间控件
	 * @data 2014-10-21下午7:21:57
	 * @auth : sid'pc
	 */
	public void initWheelView()
	{
		mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
		mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd",
				Locale.getDefault());
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd     EE",
				Locale.getDefault());
		mDate = new ArrayList<String>();
		mLongs = new ArrayList<Long>();
		mCalendar = Calendar.getInstance();
		for (int i = -mGap; i < mGap; i++)
		{
			long time = mCalendar.getTimeInMillis() + (i * 24 * 3600000l);
			mLongs.add(time);
			if (i == 0)
			{
				mDate.add("今天");
			}
			else
			{
				mDate.add(dateFormat.format(time));
			}
		}

		// 设置wheelView的adapter
		mWvFirst.setAdapter(new NumericWheelAdapter(mDate));
		mWvSecond.setAdapter(new NumericWheelAdapter(0, 23, "%02d"));
		mWvThird.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
		// 设置可循环滚动
		mWvFirst.setCyclic(true);
		mWvSecond.setCyclic(true);
		mWvThird.setCyclic(true);
		// 设置初始位置
		mWvFirst.setCurrentItem(mGap);
		mWvSecond.setCurrentItem(mCalendar.get(Calendar.HOUR_OF_DAY));
		mWvThird.setCurrentItem(mCalendar.get(Calendar.MINUTE));
	}

	/**
	 * @param
	 * @return void
	 * @throws
	 * @Method : setSelectTime2Day
	 * @Description : 将时间控件转换为选择天数
	 * @data 2014-12-18下午2:57:56
	 * @auth : sid'pc
	 */
	public void setSelectTime2Day()
	{
		mIsSelecteDay = true;
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, 1);
		mWvFirst.setLayoutParams(params);
		mFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		Calendar c = Calendar.getInstance();
		yearX = c.get(Calendar.YEAR);
		monthX = c.get(Calendar.MONTH) + 1;
		dayX = c.get(Calendar.DAY_OF_MONTH);
		poor = yearX - mGap;

		mWvFirst.setAdapter(new NumericWheelAdapter(poor, yearX + mGap));
		mWvFirst.setLabel("年");
		mWvFirst.setCyclic(true);

		mWvSecond.setAdapter(new NumericWheelAdapter(1, 12, "%02d"));
		mWvSecond.setLabel("月");
		mWvSecond.setCyclic(true);

		dayNum = getWheelDay(mWvFirst.getCurrentItem() + poor,
				mWvSecond.getCurrentItem() + 1);
		mWvThird.setAdapter(new NumericWheelAdapter(1, dayNum, "%02d"));
		mWvThird.setLabel("日");
		mWvThird.setCyclic(true);

		mWvFirst.setCurrentItem(mGap);
		mWvSecond.setCurrentItem(monthX - 1);
		mWvThird.setCurrentItem(dayX - 1);
	}

	/**
	 * @param @param  year
	 * @param @param  month
	 * @param @return
	 * @return int
	 * @throws
	 * @Method : setwheelDay
	 * @Description : 获得某月的天数
	 * @data 2014-12-10下午2:00:27
	 * @auth : sid'pc
	 */
	private int getWheelDay(int year, int month)
	{
		int day = 31;
		if (month == 2)
		{
			if ((year % 4 == 0) && ((year % 100 != 0) | (year % 400 == 0)))
			{
				day = 29;
			}
			else
			{
				day = 28;
			}
		}
		if (month == 4 || month == 6 || month == 9 || month == 11)
		{
			day = 30;
		}
		return day;
	}

	/**
	 * @param @return
	 * @return String
	 * @throws
	 * @Method : getTime
	 * @Description : 获取控件时间字符串
	 * @data 2014-10-13上午10:34:58
	 * @auth : sid'pc
	 */
	public String getTime()
	{
		String time;
		if (mIsSelecteDay)
		{
			yearX = mWvFirst.getCurrentItem() + poor;
			monthX = mWvSecond.getCurrentItem() + 1;
			dayX = mWvThird.getCurrentItem() + 1;
			time = yearX + "-" + String.format("%02d", monthX) + "-"
					+ String.format("%02d", dayX);
		}
		else
		{
			Long timeLong = mLongs.get(mWvFirst.getCurrentItem());
			time = mSimpleDateFormat.format(timeLong) + " "
					+ String.format("%02d", mWvSecond.getCurrentItem()) + ":"
					+ String.format("%02d", mWvThird.getCurrentItem());
		}
		return time;
	}

	/**
	 * @Method : changTime2Long
	 * @Description : 将string的时间转化为long
	 * @data 2014-11-25下午7:07:58
	 * @auth : sid'pc
	 */
	public long changTime2Long(String time)
	{
		long result = 0;
		try
		{
			result = mFormat.parse(time).getTime();
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @param @param time
	 * @return void
	 * @throws
	 * @Method : changeLong2View
	 * @Description : 将时间设置给时间控件
	 * @data 2014-11-25下午8:51:25
	 * @auth : sid'pc
	 */
	@SuppressLint("DefaultLocale")
	public void changeLong2View(long time)
	{
		String changedTime;
		String str_first;
		String str_second;
		String str_third;
		int int_second;
		int int_third;
		setState(0);
		if (mIsSelecteDay)
		{
			diff = 1;
			changedTime = mFormat.format(time);
			str_first = changedTime.substring(0, 4);
			str_second = changedTime.substring(5, 7);
			str_third = changedTime.substring(8, 10);
			if (mCanFirstScroll)
			{
				mWvFirst.setCurrentItem(Integer.valueOf(str_first) - poor, true);
			}
		}
		else
		{
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd     EE HH:mm", Locale.getDefault());
			changedTime = format.format(time);
			str_first = changedTime.substring(5, 17);
			str_second = changedTime.substring(18, 20);
			str_third = changedTime.substring(21, 23);
			int positon = mDate.indexOf(str_first);
			if (mCanFirstScroll)
			{
				if (positon == -1)
				{
					mWvFirst.setCurrentItem(mGap, true);
				}
				else
				{
					mWvFirst.setCurrentItem(positon, true);
				}
			}
		}
		int_second = Integer.valueOf(str_second);
		int_third = Integer.valueOf(str_third);
		setState(1);
		if (mCanSecondScrool && SecondWheelNeedScroll(int_second))
		{
			mWvSecond.setCurrentItem(int_second - diff, true);
		}
		else
		{
			str_second = mWvSecond.getCurrentItem() + diff + "";
			str_second = String.format("%02d", Integer.valueOf(str_second));
		}
		setState(2);
		if (mIsSelecteDay)
		{
			// 这里需要从新计算天数
			dayNum = getWheelDay(Integer.valueOf(str_first),
					Integer.valueOf(str_second));
			mWvThird.setAdapter(new NumericWheelAdapter(1, dayNum, "%02d"));
			if (dayNum < mWvThird.getCurrentItem() + 1)
			{
				mWvThird.setCurrentItem(0);
				dayX = 1;
			}
		}
		if (mCanThirdScrool && ThirdWheelNeedScroll(int_second, int_third))
		{
			mWvThird.setCurrentItem(int_third - diff, true);
		}
		else
		{
			str_third = mWvThird.getCurrentItem() + diff + "";
			str_third = String.format("%02d", Integer.valueOf(str_third));
		}
		setState(3);
		if (mIsSelecteDay)
		{
			mListener
					.OnScrolled(str_first + "-" + str_second + "-" + str_third);
		}
		else
		{
			int position = mDate.indexOf(str_first);
			if (position == -1)
			{
				position = mGap;
			}
			Long timeLong = mLongs.get(position);
			mListener.OnScrolled(mSimpleDateFormat.format(timeLong) + " "
					+ str_second + ":" + str_third);
		}
	}

	/**
	 * @param @param i
	 * @return void
	 * @throws
	 * @Method : setState
	 * @Description : 设置时间控件可以滑动轮的状态
	 * @data 2014-12-18下午3:03:35
	 * @auth : sid'pc
	 */
	private void setState(int i)
	{
		switch (i)
		{
			case 0:
				mCanFirstScroll = true;
				mCanSecondScrool = false;
				mCanThirdScrool = false;
				break;
			case 1:
				mCanFirstScroll = false;
				mCanSecondScrool = true;
				mCanThirdScrool = false;
				break;
			case 2:
				mCanFirstScroll = false;
				mCanSecondScrool = false;
				mCanThirdScrool = true;
				break;
			case 3:
				mCanFirstScroll = false;
				mCanSecondScrool = false;
				mCanThirdScrool = false;
				break;

			default:
				break;
		}

	}

	/**
	 * @param third2
	 * @param @param  third
	 * @param @return
	 * @return boolean
	 * @throws
	 * @Method : ThirdWheelNeedScroll
	 * @Description : 判断第三个滑轮是否需要滑动
	 * @data 2014-12-18下午3:05:18
	 * @auth : sid'pc
	 */
	private boolean ThirdWheelNeedScroll(int second, int third)
	{
		if (SecondWheelNeedScroll(second)
				|| mWvSecond.getCurrentItem() == second - diff)
		{
			if (!mCanBefor && mWvThird.getCurrentItem() < third - diff)
			{
				return true;
			}
			if (!mCanOver && mWvThird.getCurrentItem() > third - diff)
			{
				return true;
			}
		}
		if (mCanBefor && mCanOver)
		{
			return true;
		}
		return false;
	}

	/**
	 * @param @param  third
	 * @param @return
	 * @return boolean
	 * @throws
	 * @Method : SecondWheelNeedScroll
	 * @Description : 判断第二个滑轮是否需要滑动
	 * @data 2014-12-18下午3:05:18
	 * @auth : sid'pc
	 */
	private boolean SecondWheelNeedScroll(int secord)
	{
		if (!mCanBefor && mWvSecond.getCurrentItem() < secord - diff)
		{
			return true;
		}
		if (!mCanOver && mWvSecond.getCurrentItem() > secord - diff)
		{
			return true;
		}
		if (mCanBefor && mCanOver)
		{
			return true;
		}
		return false;
	}

	/**
	 * @param @param  startTime
	 * @param @param  endTime
	 * @param @return
	 * @return String
	 * @throws
	 * @Method : timeDiff
	 * @Description : 计算时间差
	 * @data 2014-11-26上午10:31:22
	 * @auth : sid'pc
	 */
	public String timeDiff(long startTime, long endTime)
	{
		long timeDiff = endTime - startTime;
		long hour = (timeDiff / 3600000);
		String result = "";
		if (timeDiff < 0)
		{
			return result;
		}
		if (hour < 1)
		{
			result = timeDiff / 60000 + "分钟";
		}
		else
		{
			if (hour < 24)
			{
				result = hour + "小时";
			}
			else
			{
				int day = (int) (hour / 24);
				hour = hour - day * 24;
				result = +day + "天" + hour + "小时";
			}
		}
		return result;
	}

	/**
	 * @param @param  startTime
	 * @param @param  endTime
	 * @param @return
	 * @return String
	 * @throws
	 * @Method : timeDiff
	 * @Description : 计算时间差（重载）
	 * @data 2014-11-26上午10:31:22
	 * @auth : sid'pc
	 */
	public String timeDiff(String startTime, String endTime)
	{
		long start_time = changTime2Long(startTime);
		long end_time = changTime2Long(endTime);
		return timeDiff(start_time, end_time);
	}

	@Override
	public void onScrollingStarted(WheelView wheel)
	{
		setState(0);
	}

	@Override
	public void onScrollingFinished(WheelView wheel)
	{
		if (wheelNeedChange())
		{
			if (mCanFirstScroll || mCanSecondScrool || mCanThirdScrool)
			{
				changeLong2View(mStartTime);
			}
		}
		else
		{
			if (mIsSelecteDay)
			{
				switch (wheel.getId())
				{
					case R.id.wv_first:
						if (mResetDay)
						{
							resetDayNum();
						}
						break;
					case R.id.wv_second:
						if (mResetDay)
						{
							resetDayNum();
						}
						else
						{
							mResetDay = true;
						}
						break;
				}
			}
			mListener.OnScrolled(getTime());
		}
	}

	private boolean wheelNeedChange()
	{
		if (!mCanBefor && changTime2Long(getTime()) < mStartTime)
		{
			return true;
		}
		if (!mCanOver && changTime2Long(getTime()) > mStartTime)
		{
			return true;
		}
		return false;
	}

	/**
	 * @param
	 * @return void
	 * @throws
	 * @Method : resetDayNum
	 * @Description : 根据选择的年月刷新该月的天数
	 * @data 2014-12-18下午3:18:25
	 * @auth : sid'pc
	 */
	private void resetDayNum()
	{
		dayNum = getWheelDay(mWvFirst.getCurrentItem() + poor,
				mWvSecond.getCurrentItem() + 1);
		mWvThird.setAdapter(new NumericWheelAdapter(1, dayNum, "%02d"));
		if (dayNum < mWvThird.getCurrentItem() + 1)
		{
			mWvThird.setCurrentItem(0);
			dayX = 1;
		}
	}

	public interface OnScrolledListener
	{
		void OnScrolled(String time);
	}

	public void setOnTimeViewScrolledListener(OnScrolledListener listener)
	{
		mListener = listener;
	}

	public void setCanOverStartTime(boolean canOver)
	{
		this.mCanBefor = canOver;
	}

	public void setStartTime(long startTime)
	{
		mStartTime = startTime;
	}
	
	public void setNoLimit()
	{
		mCanBefor = true;
		mCanOver = true;
		mResetDay = false;
	}

	public void setCanBeforeNow(boolean canBeforeNow)
	{
		mStartTime = mCalendar.getTimeInMillis();
		mCanBefor = canBeforeNow;
		mCanOver = true;
	}

	public void setCanOverNow(boolean canOverNow)
	{
		mStartTime = mCalendar.getTimeInMillis();
		mCanOver = canOverNow;
		mCanBefor = true;
	}
}
