package com.glide.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.glide.R;
import com.glide.helper.Constant;
import com.yline.base.BaseAppCompatActivity;
import com.yline.base.common.CommonListAdapter;

import java.util.Arrays;

public class ListActivity extends BaseAppCompatActivity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		ListView lvShow = (ListView) findViewById(R.id.lv_show);
		CommonListAdapter listAdapter = new ListAdapter(this);
		lvShow.setAdapter(listAdapter);
		
		listAdapter.addAll(Arrays.asList(Constant.IMAGES));
	}
	
	private class ListAdapter extends CommonListAdapter<String>
	{
		
		public ListAdapter(Context context)
		{
			super(context);
		}
		
		@Override
		protected int getItemRes(int i)
		{
			return R.layout.item_activity_list;
		}
		
		@Override
		protected void setViewContent(int i, ViewGroup viewGroup, ViewHolder viewHolder)
		{
			viewHolder.setText(R.id.tv_item, sList.get(i));
			
			ImageView imageView = viewHolder.get(R.id.iv_item);
			
			// Glide.get(sContext).register(GlideUrl.class, InputStream.class, new HttpUrlGlideUrlLoader.Factory());
			
			Glide.with(sContext)
					.load(sList.get(i))
					.into(imageView);
		}
	}
	
	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, ListActivity.class));
	}
}
