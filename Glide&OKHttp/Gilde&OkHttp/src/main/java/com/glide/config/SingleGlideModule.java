package com.glide.config;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpGlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.yline.log.LogFileUtil;

public class SingleGlideModule extends OkHttpGlideModule
{
	@Override
	public void applyOptions(Context context, GlideBuilder builder)
	{
		// 设置 缓存 内存大小
		int maxMemory = (int) Runtime.getRuntime().maxMemory(); // 获取系统分配给应用的总内存大小
		int memoryCacheSize = maxMemory / 8;//设置图片内存缓存占用八分之一  (总大小 一般为 200-250M)

		LogFileUtil.v("maxMemory = " + maxMemory + ",memoryCacheSize = " + memoryCacheSize);
		//设置内存缓存大小
		builder.setMemoryCache(new LruResourceCache(memoryCacheSize));

		MemorySizeCalculator calculator = new MemorySizeCalculator(context);
		int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
		int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

		LogFileUtil.v("defaultMemoryCacheSize = " + defaultMemoryCacheSize + ",defaultBitmapPoolSize = " + defaultBitmapPoolSize);

		// 设置 缓存 磁盘 大小
		builder.setDiskCache(new DiskLruCacheFactory(context.getExternalFilesDir("GlidePicture").getAbsolutePath(), 512 * 1024 * 1024));

		//设置图片解码格式
		builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888); // 默认格式RGB_565使用内存是ARGB_8888的一半
	}

	@Override
	public void registerComponents(Context context, Glide glide)
	{
		super.registerComponents(context, glide);
		LogFileUtil.v("registerComponents ");
	}
}
