package com.time.picker.wheel.multi;

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
import java.util.List;

public class MultiSelectView extends PopupWindow implements
		OnWheelClickedListener
{

	private View contentView;

	private String[] stringList;

	private List<Integer> idList = new ArrayList<Integer>();

	boolean[] selected;

	private WheelView2 wheelView;

	private Context context;

	private TypeTextAdapter typeTextAdapter;

	private onClickListenr listenr;

	public MultiSelectView(Context context)
	{
		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		contentView = inflater.inflate(R.layout.view_multiselect, null);
		wheelView = (WheelView2) contentView
				.findViewById(R.id.wheel_multiselect);
		wheelView.addClickingListener(this);
		setWindow();
	}

	private void setWindow()
	{
		this.setContentView(contentView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		this.setBackgroundDrawable(dw);
		contentView.setOnTouchListener(new OnTouchListener()
		{

			public boolean onTouch(View v, MotionEvent event)
			{

				int height = contentView.findViewById(R.id.wheel_multiselect).getTop();
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

	public void setAdapter(String[] stringList)
	{
		this.stringList = stringList;
		selected = new boolean[stringList.length];
		typeTextAdapter = new TypeTextAdapter(context);
		wheelView.setViewAdapter(typeTextAdapter);
	}

	class TypeTextAdapter extends AbstractWheelTextAdapter
	{
		protected TypeTextAdapter(Context context)
		{
			super(context, R.layout.item_multiselect, R.id.type_name, selected);
		}

		@Override
		public int getItemsCount()
		{
			return stringList.length;
		}

		@Override
		protected CharSequence getItemText(int index)
		{
			return stringList[index].toString();
		}

	}

	@Override
	public void onItemClicked(WheelView2 wheel, int itemIndex)
	{
		if (selected[itemIndex])
		{
			selected[itemIndex] = false;
		}
		else
		{
			selected[itemIndex] = true;
		}
		typeTextAdapter.setchange(selected);
		List<String> selectStr = new ArrayList<String>();
		idList = new ArrayList<Integer>();
		for (int i = 0; i < stringList.length; i++)
		{
			if (selected[i])
			{
				selectStr.add(stringList[i]);
				idList.add(i);
			}
		}
		if (listenr != null)
		{
			listenr.onClick(this, selectStr, idList);
		}
	}

	public interface onClickListenr
	{
		public void onClick(MultiSelectView multiSelectView,
		                    List<String> selectStr, List<Integer> idList);
	}

	public void setonClickListenr(onClickListenr listenr)
	{
		this.listenr = listenr;
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
}
