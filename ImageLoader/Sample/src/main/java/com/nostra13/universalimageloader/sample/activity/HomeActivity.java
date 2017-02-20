package com.nostra13.universalimageloader.sample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.sample.Constants;
import com.nostra13.universalimageloader.sample.R;
import com.nostra13.universalimageloader.sample.fragment.ImageGalleryLoaderFragment;
import com.nostra13.universalimageloader.sample.fragment.ImageGridLoaderFragment;
import com.nostra13.universalimageloader.sample.fragment.ImageListLoaderFragment;
import com.nostra13.universalimageloader.sample.fragment.ImagePagerLoaderFragment;
import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class HomeActivity extends BaseAppCompatActivity
{
	private static final String TEST_FILE_NAME = "Universal Image Loader @#&=+-_.,!()~'%20.png";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_home);

		File testImageOnSdCard = new File("/mnt/sdcard", TEST_FILE_NAME);
		if (!testImageOnSdCard.exists())
		{
			LogFileUtil.v("test");
			copyTestImageToSdCard(testImageOnSdCard);
		}
	}
	
	public void onImageListClick(View view)
	{
		Intent intent = new Intent(this, SimpleImageActivity.class);
		intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageListLoaderFragment.INDEX);
		startActivity(intent);
	}

	public void onImageGridClick(View view)
	{
		Intent intent = new Intent(this, SimpleImageActivity.class);
		intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageGridLoaderFragment.INDEX);
		startActivity(intent);
	}

	public void onImagePagerClick(View view)
	{
		Intent intent = new Intent(this, SimpleImageActivity.class);
		intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImagePagerLoaderFragment.INDEX);
		startActivity(intent);
	}

	public void onImageGalleryClick(View view)
	{
		Intent intent = new Intent(this, SimpleImageActivity.class);
		intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageGalleryLoaderFragment.INDEX);
		startActivity(intent);
	}

	public void onFragmentsClick(View view)
	{
		Intent intent = new Intent(this, ComplexImageActivity.class);
		startActivity(intent);
	}

	@Override
	public void onBackPressed()
	{
		ImageLoader.getInstance().stop();
		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
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

	private void copyTestImageToSdCard(final File testImageOnSdCard)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					InputStream is = getAssets().open(TEST_FILE_NAME);
					FileOutputStream fos = new FileOutputStream(testImageOnSdCard);
					byte[] buffer = new byte[8192];
					int read;
					try
					{
						while ((read = is.read(buffer)) != -1)
						{
							fos.write(buffer, 0, read);
						}
					}
					finally
					{
						fos.flush();
						fos.close();
						is.close();
					}
				}
				catch (IOException e)
				{
					LogFileUtil.e("", "Can't copy test image onto SD card", e);
				}
			}
		}).start();
	}
}