package com.time.picker.wheel;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import com.time.picker.R;


public class SelectTimeView extends PopupWindow implements TimePickerView.OnScrolledListener
{
	private View mContentView;

	private OnTimeGotListener mListener;

	private TimePickerView mTimePickerView;
	
	private boolean changed = false;
	
	private boolean dismissWindow = false;
	
	private Context context;

	public SelectTimeView(Context context)
	{
		super(context);
		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		mContentView = inflater.inflate(R.layout.view_time_select, null);
		mTimePickerView = (TimePickerView) mContentView
				.findViewById(R.id.time_picker);
		
		mTimePickerView.setOnTimeViewScrolledListener(this);
		setWindow();
		setSelectTime2Day();
		setCanOverNow(false);
	}

	public SelectTimeView(Context context, final String originalValue)
	{
		super(context);
		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		mContentView = inflater.inflate(R.layout.view_time_select, null);
		mTimePickerView = (TimePickerView) mContentView
				.findViewById(R.id.time_picker);
		
		
		mTimePickerView.setOnTimeViewScrolledListener(this);
		setWindow();
		setSelectTime2Day();
		setCanOverNow(false);
	}

	
	private void setWindow()
	{
		// 设置PopupWindow的View
		this.setContentView(mContentView);
		// 设置PopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置PopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置PopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 实例化一个ColorDrawable颜色为透明
		ColorDrawable dw = new ColorDrawable(0x00000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		mContentView.setOnTouchListener(new OnTouchListener()
		{

			public boolean onTouch(View v, MotionEvent event)
			{

				int height = mContentView.findViewById(R.id.time_picker)
						.getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP)
				{
					if (y < height)
					{
						dismissWindow = true;
						if (!changed)
						{
							mListener.OnTimeGot(mTimePickerView.getTime());
						}
						dismiss();
						//						long time = changTime2Long("2014-10-15");
						//						setTime2View(time);
					}
					else
					{
						dismissWindow = false;
					}
				}
				return true;
			}
		});
	}

	public interface OnTimeGotListener
	{
		void OnTimeGot(String time);
	}

	public boolean getDismissWindow()
	{
		return dismissWindow;
	}
	
	public void setOnTimeGotListener(OnTimeGotListener listener)
	{
		mListener = listener;
	}

	public void getTime()
	{
		mListener.OnTimeGot(mTimePickerView.getTime());
	}

	@Override
	public void OnScrolled(String time)
	{
		mListener.OnTimeGot(time);
		System.out.println(time);
		changed = true;
	}

	public void setCanOverNow(boolean canOverNow)
	{
		mTimePickerView.setCanOverNow(canOverNow);
	}
	
	public void setCanBeforNow(boolean canBeforNow)
	{
		mTimePickerView.setCanBeforeNow(canBeforNow);
	}

	public void setSelectTime2Day()
	{
		mTimePickerView.setSelectTime2Day();
	}
	
	public String getToday()
	{
		return mTimePickerView.getTime();
	}
	
	public void setBackgroundBlack()
	{
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
	}
	
	public long changTime2Long(String time)
	{
		long result = 0;
		result = mTimePickerView.changTime2Long(time);
		return result;
	}
	
	public void setTime2View(long time)
	{
		mTimePickerView.setNoLimit();
		mTimePickerView.changeLong2View(time);
	}

	public void setTime2View(final String time, final boolean canOverNow, final boolean canBeforeNow)
	{
		mTimePickerView.setNoLimit();
		mTimePickerView.postDelayed(new Runnable()
		{
			
			@Override
			public void run()
			{
				mTimePickerView.changeLong2View(changTime2Long(time));
				mTimePickerView.setCanOverNow(canOverNow);
				if (canOverNow)
				{
					mTimePickerView.setCanBeforeNow(canBeforeNow);
				}
			}
		}, 300);
		
	}
	
	public void show(View v)
	{
		hideKeyBoard();
		//		showAsDropDown(v);
		showAtLocation(v, Gravity.BOTTOM, 0, 0);
	}
	
	public void hideKeyBoard()
	{
		View view = ((Activity) context).getWindow().peekDecorView();
		if (view != null)
		{
			InputMethodManager inputmanger = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}
	
}
