/*******************************************************************************
 * Copyright 2014 Sergey Tarasevich
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.nostra13.universalimageloader.sample.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.nostra13.universalimageloader.sample.R;
import com.nostra13.universalimageloader.sample.fragment.ImageGridLoaderFragment;
import com.nostra13.universalimageloader.sample.fragment.ImageListLoaderFragment;
import com.yline.base.BaseFragmentActivity;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class ComplexImageActivity extends BaseFragmentActivity
{

	private static final String STATE_POSITION = "STATE_POSITION";

	private ViewPager pager;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_complex);

		int pagerPosition = savedInstanceState == null ? 0 : savedInstanceState.getInt(STATE_POSITION);

		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(new ImagePagerAdapter(getSupportFragmentManager()));
		pager.setCurrentItem(pagerPosition);
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		outState.putInt(STATE_POSITION, pager.getCurrentItem());
	}

	private class ImagePagerAdapter extends FragmentPagerAdapter
	{

		Fragment listFragment;

		Fragment gridFragment;

		ImagePagerAdapter(FragmentManager fm)
		{
			super(fm);
			listFragment = new ImageListLoaderFragment();
			gridFragment = new ImageGridLoaderFragment();
		}

		@Override
		public int getCount()
		{
			return 2;
		}

		@Override
		public Fragment getItem(int position)
		{
			switch (position)
			{
				case 0:
					return listFragment;
				case 1:
					return gridFragment;
				default:
					return null;
			}
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			switch (position)
			{
				case 0:
					return getString(R.string.title_list);
				case 1:
					return getString(R.string.title_grid);
				default:
					return null;
			}
		}
	}
}