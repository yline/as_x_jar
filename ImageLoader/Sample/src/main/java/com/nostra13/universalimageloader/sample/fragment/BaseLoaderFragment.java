package com.nostra13.universalimageloader.sample.fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.sample.R;
import com.yline.base.BaseFragment;

/**
 * 初始化头部;-->;清理内存
 */
public abstract class BaseLoaderFragment extends BaseFragment
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.main_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.item_clear_memory_cache:
				ImageLoader.getInstance().clearMemoryCache();
				return true;
			case R.id.item_clear_disc_cache:
				ImageLoader.getInstance().clearDiskCache();
				return true;
			default:
				return false;
		}
	}
}
