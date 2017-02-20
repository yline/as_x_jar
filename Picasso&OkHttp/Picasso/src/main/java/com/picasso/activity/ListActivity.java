package com.picasso.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.picasso.R;
import com.picasso.helper.Constant;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
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
		CommonListAdapter listAdapter = new CommonListAdapter<String>(this)
		{
			@Override
			protected int getItemRes(int i)
			{
				return R.layout.item_activity_list;
			}
			
			@Override
			protected void setViewContent(int i, ViewGroup viewGroup, ViewHolder viewHolder)
			{
				ImageView ivItem = viewHolder.get(R.id.iv_item);
				viewHolder.setText(R.id.tv_item, sList.get(i));
				
				Picasso
						.with(ListActivity.this)
						.load(sList.get(i)) // 数据源  Uri,file,resId,path
						.transform(new RectangleTransformation(400, 400)).fit().centerCrop() // 对图片进行特殊操作  并且 设置渲染模式
						// .placeholder() 正在加载是图片
						// .error() 加载失败图片
						.into(ivItem);
			}
		};
		lvShow.setAdapter(listAdapter);

		listAdapter.addAll(Arrays.asList(Constant.IMAGES));
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	/**
	 * 这个是对图片进行处理,决定了图片质量
	 * @author yline 2017/2/20 --> 14:46
	 * @version 1.0.0
	 */
	private class RectangleTransformation implements Transformation
	{
		private int width, height;
		
		private String key = "rectangle";
		
		public RectangleTransformation(int width, int height)
		{
			this.width = width;
			this.height = height;
		}
		
		public RectangleTransformation(int width, int height, String key)
		{
			this.width = width;
			this.height = height;
			this.key = key;
		}
		
		@Override
		public Bitmap transform(Bitmap source)
		{
			Bitmap result = Bitmap.createScaledBitmap(source, width, height, true);
			if (result != source)
			{
				source.recycle();
			}
			return result;
		}
		
		@Override
		public String key()
		{
			return key;
		}
	}
	
	private class CircleTransformation implements Transformation
	{
		private static final int STROKE_WIDTH = 5;
		
		public static final String KEY_RESULT = "circle";
		
		@Override
		public Bitmap transform(Bitmap source)
		{
			int size = Math.min(source.getWidth(), source.getHeight());
			int x = (source.getWidth() - size) / 2;
			int y = (source.getHeight() - size) / 2;
			
			Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
			if (squaredBitmap != source)
			{
				source.recycle();
			}
			Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());
			Canvas canvas = new Canvas(bitmap);
			Paint avatarPaint = new Paint();
			BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
			avatarPaint.setShader(shader);
			
			Paint outlinePaint = new Paint();
			outlinePaint.setColor(Color.WHITE);
			outlinePaint.setStyle(Paint.Style.STROKE);
			outlinePaint.setStrokeWidth(STROKE_WIDTH);
			outlinePaint.setAntiAlias(true);
			
			float r = size / 2f;
			canvas.drawCircle(r, r, r, avatarPaint);
			canvas.drawCircle(r, r, r - STROKE_WIDTH / 2, outlinePaint);
			
			squaredBitmap.recycle();
			return bitmap;
		}
		
		@Override
		public String key()
		{
			return KEY_RESULT;
		}
	}
	
	public static void actionStart(Context context)
	{
		context.startActivity(new Intent(context, ListActivity.class));
	}
}
