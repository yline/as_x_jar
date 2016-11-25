package com.time.calendar.view;

import java.util.Calendar;

public class SpecialCalendar
{

	private int daysOfMonth = 0;      //鏌愭湀鐨勫ぉ鏁�

	private int dayOfWeek = 0;        //鍏蜂綋鏌愪竴澶╂槸鏄熸湡鍑�

	private Calendar cal = Calendar.getInstance();
	
	
	// 鍒ゆ柇鏄惁涓洪棸骞�
	public boolean isLeapYear(int year)
	{
		if (year % 100 == 0 && year % 400 == 0)
		{
			return true;
		}
		else if (year % 100 != 0 && year % 4 == 0)
		{
			return true;
		}
		return false;
	}

	//寰楀埌鏌愭湀鏈夊灏戝ぉ鏁�
	public int getDaysOfMonth(boolean isLeapyear, int month)
	{
		switch (month)
		{
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				daysOfMonth = 31;
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				daysOfMonth = 30;
				break;
			case 2:
				if (isLeapyear)
				{
					daysOfMonth = 29;
				}
				else
				{
					daysOfMonth = 28;
				}

		}
		return daysOfMonth;
	}
	
	//鎸囧畾鏌愬勾涓殑鏌愭湀鐨勭涓�澶╂槸鏄熸湡鍑�
	public int getWeekdayOfMonth(int year, int month)
	{
		cal.set(year, month - 1, 1);
		dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
		return dayOfWeek;
	}
	
	
}
