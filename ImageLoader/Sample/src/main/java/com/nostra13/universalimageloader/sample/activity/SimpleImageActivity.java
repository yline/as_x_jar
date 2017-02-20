package com.nostra13.universalimageloader.sample.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.nostra13.universalimageloader.sample.Constants;
import com.nostra13.universalimageloader.sample.R;
import com.nostra13.universalimageloader.sample.fragment.ImageGalleryLoaderFragment;
import com.nostra13.universalimageloader.sample.fragment.ImageGridLoaderFragment;
import com.nostra13.universalimageloader.sample.fragment.ImageListLoaderFragment;
import com.nostra13.universalimageloader.sample.fragment.ImagePagerLoaderFragment;
import com.yline.base.BaseAppCompatActivity;

public class SimpleImageActivity extends BaseAppCompatActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		int frIndex = getIntent().getIntExtra(Constants.Extra.FRAGMENT_INDEX, 0);

		// 分配值
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

		// 开启对应的界面
		setTitle(titleRes);
		getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fr, tag).commit();
	}
}