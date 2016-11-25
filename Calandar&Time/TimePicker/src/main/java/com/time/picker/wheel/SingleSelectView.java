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

import java.util.ArrayList;

public class SingleSelectView extends PopupWindow implements OnWheelScrollListener
{

	private WheelView wheelView;

	private View contentView;

	private OnSelectedListener listener;

	private Context context;

	public SingleSelectView(Context context)
	{
		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		contentView = inflater.inflate(R.layout.view_singleselect, null);
		wheelView = (WheelView) contentView.findViewById(R.id.wv_single_select);
		wheelView.addScrollingListener(this);
		setWindow();
	}
	
	public void setCyclic(boolean isCyclic)
	{
		wheelView.setCyclic(isCyclic);
	}

	public void setCurrentItem(int index)
	{
		wheelView.setCurrentItem(index);
	}

	@Override
	public void onScrollingStarted(WheelView wheel)
	{

	}

	@Override
	public void onScrollingFinished(WheelView wheel)
	{
		if (listener != null)
		{
			String str = wheel.getTextItem(wheel.getCurrentItem());
			listener.OnSelected(this, str);
		}

	}

	public void setAdapter(String[] array)
	{
		wheelView.setAdapter(new NumericWheelAdapter(ChangeArray2List(array)));
	}
	
	public void setAdapter(ArrayList<String> list)
	{
		wheelView.setAdapter(new NumericWheelAdapter(list));
	}
	
	private ArrayList<String> ChangeArray2List(String[] array)
	{
		ArrayList<String> list = new ArrayList<String>();
		for (String string : array)
		{
			list.add(string);
		}
		return list;
	}

	private void setWindow()
	{
		setContentView(contentView);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		setBackgroundDrawable(dw);
		contentView.setOnTouchListener(new OnTouchListener()
		{

			public boolean onTouch(View v, MotionEvent event)
			{

				int height = contentView.findViewById(R.id.wv_single_select)
						.getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP)
				{
					if (y < height)
					{
						dismiss();
					}
				}
				return true;
			}
		});
	}
	
	public void show(View v)
	{
		hideKeyBoard();
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
	
	public int getCurrentItem()
	{
		return wheelView.getCurrentItem();
	}

	public interface OnSelectedListener
	{
		public void OnSelected(SingleSelectView singleSelectView, String str);
	}

	public void setOnSelectedListener(OnSelectedListener listener)
	{
		this.listener = listener;
	}

	
}
