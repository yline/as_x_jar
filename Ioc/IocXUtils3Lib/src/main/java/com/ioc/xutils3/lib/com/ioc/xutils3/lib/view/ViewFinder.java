package com.ioc.xutils3.lib.com.ioc.xutils3.lib.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;

/**
 * findViewById绑定Id,兼容父类和子类
 *
 * @author fatenliyer
 * @date 2016-1-2
 */
final class ViewFinder
{
	private View view;

	private Activity activity;
	
	public ViewFinder(View view)
	{
		this.view = view;
	}
	
	public ViewFinder(Activity activity)
	{
		this.activity = activity;
	}
	
	public View findViewById(int id)
	{
		if (null != view)
		{
			return view.findViewById(id);
		}
		if (null != activity)
		{
			return activity.findViewById(id);
		}
		return null;
	}
	
	public View findViewByInfo(ViewInfo info)
	{
		return findViewById(info.value, info.parentId);
	}
	
	public View findViewById(int id, int pid)
	{
		View pView = null;
		if (pid > 0)
		{
			pView = this.findViewById(pid);
		}
		
		View view = null;
		if (null != pView)
		{
			view = pView.findViewById(id);
		}
		else
		{
			view = this.findViewById(id);
		}
		return view;
	}
	
	public Context getContext()
	{
		if (null != view)
		{
			return view.getContext();
		}
		if (null != activity)
		{
			return activity;
		}
		return null;
	}
	
}




















