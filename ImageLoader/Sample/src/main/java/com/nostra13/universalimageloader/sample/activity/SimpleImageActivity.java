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

import com.nostra13.universalimageloader.sample.Constants;
import com.nostra13.universalimageloader.sample.R;
import com.nostra13.universalimageloader.sample.fragment.ImageGalleryLoaderFragment;
import com.nostra13.universalimageloader.sample.fragment.ImageGridLoaderFragment;
import com.nostra13.universalimageloader.sample.fragment.ImageListLoaderFragment;
import com.nostra13.universalimageloader.sample.fragment.ImagePagerLoaderFragment;
import com.yline.base.BaseFragmentActivity;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class SimpleImageActivity extends BaseFragmentActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		int frIndex = getIntent().getIntExtra(Constants.Extra.FRAGMENT_INDEX, 0);
		Fragment fr;
		String tag;
		int titleRes;
		switch (frIndex)
		{
			default:
			case ImageListLoaderFragment.INDEX:
				tag = ImageListLoaderFragment.class.getSimpleName();
				fr = getSupportFragmentManager().findFragmentByTag(tag);
				if (fr == null)
				{
					fr = new ImageListLoaderFragment();
				}
				titleRes = R.string.ac_name_image_list;
				break;
			case ImageGridLoaderFragment.INDEX:
				tag = ImageGridLoaderFragment.class.getSimpleName();
				fr = getSupportFragmentManager().findFragmentByTag(tag);
				if (fr == null)
				{
					fr = new ImageGridLoaderFragment();
				}
				titleRes = R.string.ac_name_image_grid;
				break;
			case ImagePagerLoaderFragment.INDEX:
				tag = ImagePagerLoaderFragment.class.getSimpleName();
				fr = getSupportFragmentManager().findFragmentByTag(tag);
				if (fr == null)
				{
					fr = new ImagePagerLoaderFragment();
					fr.setArguments(getIntent().getExtras());
				}
				titleRes = R.string.ac_name_image_pager;
				break;
			case ImageGalleryLoaderFragment.INDEX:
				tag = ImageGalleryLoaderFragment.class.getSimpleName();
				fr = getSupportFragmentManager().findFragmentByTag(tag);
				if (fr == null)
				{
					fr = new ImageGalleryLoaderFragment();
				}
				titleRes = R.string.ac_name_image_gallery;
				break;
		}

		setTitle(titleRes);
		getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fr, tag).commit();
	}
}