package com.time.calendar.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.time.calendar.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarAdapter extends BaseAdapter
{

	private LayoutInflater inflater;

	private int mSundayColor;

	private int mNormalColor;

	private int mUnselectColor;

	private int mYear;

	private int mMonth;

	private int mDay;

	private MyCalendarView.CalendarData mData;

	private String[] mDataArrays;

	private List<Integer> mFilterDay;

	private List<Integer> mSelectDay;

	/**
	 * 浠婂ぉ涔嬪墠涓嶅彲閫夊紑鍏�
	 */
	public static final boolean NOT_BEFOR_TODAY = true;

	private SpecialCalendar mCalendar = new SpecialCalendar();

	private ArrayList<Integer> mMoveDay = new ArrayList<Integer>();

	public CalendarAdapter(Context context, MyCalendarView.CalendarData data)
	{
		mData = data;
		inflater = LayoutInflater.from(context);

		Calendar cal = Calendar.getInstance();
		mYear = cal.get(Calendar.YEAR);
		mMonth = cal.get(Calendar.MONTH) + 1;
		mDay = cal.get(Calendar.DAY_OF_MONTH);

		mSundayColor = context.getResources().getColor(R.color.sche_sunday);
		mNormalColor = context.getResources().getColor(R.color.gray_text);
		mUnselectColor = context.getResources().getColor(
				R.color.text_unselected);

		mDataArrays = getTheMonthData(mData.getYear(), mData.getMonth());

		mSelectDay = new ArrayList<Integer>();
	}

	public void resetData(MyCalendarView.CalendarData data)
	{
		mData = data;
		mSelectDay = data.getSelectDay();
		mFilterDay = data.getFilterDay();
		if (mSelectDay == null)
		{
			mSelectDay = new ArrayList<Integer>();
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		return getTheMonthCount(mData.getYear(), mData.getMonth());
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
		TextView tvDate;
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.item_grid_calendar, null);
			tvDate = (TextView) convertView.findViewById(R.id.tv_date);
			convertView.setTag(tvDate);
		}
		else
		{
			tvDate = (TextView) convertView.getTag();
		}
		String strDate = mDataArrays[position];
		tvDate.setText(strDate);
		int day = -1;
		if (!strDate.isEmpty())
		{
			day = Integer.valueOf(strDate);
		}

		setTextView(position, tvDate, day);

		boolean contains = false;
		// 涓洪�変腑鐨勬棩鏈熻缃儗鏅拰棰滆壊
		if (mSelectDay.contains(day))
		{
			tvDate.setBackgroundResource(R.drawable.bg_date_selected);
			tvDate.setTextColor(Color.WHITE);
			contains = true;
		}

		// 鍋囧婊戝姩閫変腑鏃ユ湡鍖呭惈鐨勫綋澶�
		if (mMoveDay.contains(Integer.valueOf(position)))
		{
			if (contains)
			{
				// 鍋囧宸茬粡鏄�変腑鏃ユ湡锛岃繖鎭㈠涓烘湭閫変腑鐘舵��
				setTextView(position, tvDate, day);
			}
			else
			{
				// 鍋囧涓鸿閫変腑涓旀棩鏈熶笉涓虹┖锛岃缃负閫変腑
				if (day != -1)
				{
					tvDate.setBackgroundResource(R.drawable.bg_date_selected);
					tvDate.setTextColor(Color.WHITE);
				}
			}
		}

		return convertView;
	}

	/**
	 * @Description : 鍒濆鍖栨棩鏈熺殑鑳屾櫙鍜岄鑹�
	 * @auth : sid'pc
	 */
	private void setTextView(int position, TextView tvDate, int day)
	{

		// 璁剧疆鏃ユ湡鑳屾櫙
		if (mData.getYear() == mYear && mData.getMonth() == mMonth
				&& day == mDay)
		{
			tvDate.setBackgroundResource(R.drawable.bg_date_today);
		}
		else
		{
			tvDate.setBackgroundResource(R.drawable.bg_round_zero);
		}

		// 璁剧疆鏃ユ湡棰滆壊
		if (mData.getFilterDay() != null)
		{
			// 闈炵瓫閫夋棩鏄贰鐏拌壊
			tvDate.setTextColor(mUnselectColor);
			for (int theDay : mData.getFilterDay())
			{
				if (day == theDay)
				{
					// 绛涢�夋棩鏄繁鐏拌壊
					tvDate.setTextColor(mNormalColor);
					// 鍛ㄦ湯鏄伆鑹�
					if ((position % 7 == 0) || (position % 7 == 6))
					{
						tvDate.setTextColor(mSundayColor);
					}
				}
			}
		}
		else
		{
			tvDate.setTextColor(mNormalColor);
			if ((position % 7 == 0) || (position % 7 == 6))
			{
				tvDate.setTextColor(mSundayColor);
			}
			if (!isAfterToday(mData.getYear(), mData.getMonth(), day)
					&& NOT_BEFOR_TODAY)
			{
				tvDate.setTextColor(mUnselectColor);
			}
		}
	}

	/**
	 * @Description : 灏嗘粦鍔ㄩ�夋嫨瀹岀殑鏃ユ湡娣诲姞鍒拌閫夋嫨鏃ユ湡鏁扮粍
	 * @auth : sid'pc
	 */
	public void ChangeMoveDayToSelected()
	{
		for (int i = 0; i < mMoveDay.size(); i++)
		{
			if (mMoveDay.get(i) >= mDataArrays.length)
			{
				return;
			}
			String strDate = mDataArrays[mMoveDay.get(i)];
			int day = -1;
			if (!strDate.isEmpty())
			{
				day = Integer.valueOf(strDate);
			}

			if (day != -1
					&& isAfterToday(mData.getYear(), mData.getMonth(), day)
					&& NOT_BEFOR_TODAY)
			{
				if (mSelectDay.contains(day))
				{
					mSelectDay.remove((Integer) day);
				}
				else
				{
					if (mFilterDay == null
							|| (mFilterDay != null && mFilterDay.contains(day)))
					{
						mSelectDay.add(day);
					}
				}
				mData.setSelectDay(mSelectDay);
			}
		}
		notifyDataSetChanged();
	}

	public MyCalendarView.CalendarData getCalendarData()
	{
		return mData;
	}

	public List<Integer> getSelectDay()
	{
		return mSelectDay;
	}

	public ArrayList<Integer> getMoveDay()
	{
		return mMoveDay;
	}

	/**
	 * @Description : 鍒ゆ柇鏄惁瓒呰繃浠婂ぉ
	 * @auth : sid'pc
	 */
	boolean isAfterToday(int year, int month, int day)
	{
		if (year < mYear)
		{
			return false;
		}
		else if (year == mYear && month < mMonth)
		{
			return false;
		}
		else if (year == mYear && month == mMonth && day < mDay)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * @Description : 鑾峰緱鏌愬勾鏌愭湀鐨勬棩鏈熸暟缁�
	 * @auth : sid'pc
	 */
	private String[] getTheMonthData(int year, int month)
	{
		String[] dataArray = new String[42];
		// 鏄惁鏄棸骞�
		boolean isLeapyear = mCalendar.isLeapYear(year);
		// 鑾峰彇褰撴湀1鍙风殑鏄熸湡鏁�
		int firstInWeek = mCalendar.getWeekdayOfMonth(year, month);
		// 鑾峰彇褰撴湀澶╂暟
		int monthDays = mCalendar.getDaysOfMonth(isLeapyear, month);

		// 缁勮鏁版嵁
		for (int i = 0; i < dataArray.length; i++)
		{

			if (i >= firstInWeek && i < monthDays + firstInWeek)
			{
				dataArray[i] = i - firstInWeek + 1 + "";
			}
			else
			{
				dataArray[i] = "";
			}

		}
		return dataArray;
	}

	/**
	 * @Description : 鑾峰緱GridView item鐨勪釜鏁�
	 * @auth : sid'pc
	 */
	public int getTheMonthCount(int year, int month)
	{
		// 鏄惁鏄棸骞�
		boolean isLeapyear = mCalendar.isLeapYear(year);
		// 鑾峰彇褰撴湀1鍙风殑鏄熸湡鏁�
		int firstInWeek = mCalendar.getWeekdayOfMonth(year, month);
		// 鑾峰彇褰撴湀澶╂暟
		int monthDays = mCalendar.getDaysOfMonth(isLeapyear, month);
		if ((monthDays == 31))
		{
			if (firstInWeek < 5)
			{
				return 35;
			}
			else
			{
				return 42;
			}
		}
		else if ((monthDays == 30))
		{
			if (firstInWeek < 6)
			{
				return 35;
			}
			else
			{
				return 42;
			}
		}
		else if (monthDays == 28)
		{
			if (firstInWeek == 0)
			{
				return 28;
			}
			else
			{
				return 35;
			}
		}
		else
		{
			return 35;
		}
	}
}
