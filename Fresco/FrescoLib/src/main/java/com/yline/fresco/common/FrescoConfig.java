package com.yline.fresco.common;

import android.content.Context;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.logging.FLog;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpNetworkFetcher;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import okhttp3.OkHttpClient;

/**
 * Fresco 工具类
 *
 * @author yline 2017/9/23 -- 14:52
 * @version 1.0.0
 */
public class FrescoConfig {
    // bitmap缓存
    private static final int MAX_BITMAP_CACHE_SIZE = 30 * ByteConstants.MB; // 最大缓存，磁盘大小
    private static final int MAX_BITMAP_CACHE_ENTRIES = 64; // 最大缓存，图片个数； 按照单张图片512kb计算而来
    private static final int MAX_BITMAP_CACHE_EVICTION_SIZE = 10 * ByteConstants.MB; // 缓存回收的最大,内存大小
    private static final int MAX_BITMAP_CACHE_EVICTION_ENTRIES = 32; // 缓存回收的最大，缓存个数
    private static final int MAX_BITMAP_CACHE_ENTRY_SINGLE_SIZE = 2 * ByteConstants.MB; // 单个图片，最大内存

    // encode缓存
    private static final int MAX_ENCODED_CACHE_SIZE = 4 * ByteConstants.MB; // 最大缓存，磁盘大小
    private static final int MAX_ENCODED_CACHE_ENTRIES = 32; // 最大缓存，图片个数； 按照单张图片512kb计算而来

    public static void initConfig(Context context, boolean isDebug) {
        // 日志记录
        Set<RequestListener> requestListeners = new HashSet<>();
        requestListeners.add(new RequestLoggingListener());

        // 大图片，磁盘缓存
        File parentPathFile = context.getExternalCacheDir();
        DiskCacheConfig mainDiskCacheConfig;
        if (null == parentPathFile) {
            mainDiskCacheConfig = DiskCacheConfig.newBuilder(context).build();
        } else {
            mainDiskCacheConfig = DiskCacheConfig.newBuilder(context)
                    .setBaseDirectoryPath(parentPathFile)
                    .setBaseDirectoryName("fresco")
                    .setMaxCacheSize(1024 * ByteConstants.MB)
                    .setMaxCacheSizeOnLowDiskSpace(100 * ByteConstants.MB)
                    .build();
        }


        // 图片内存缓存 DefaultBitmapMemoryCacheParamsSupplier
        Supplier<MemoryCacheParams> bitmapMemoryCacheParamsSupplier = new Supplier<MemoryCacheParams>() {
            @Override
            public MemoryCacheParams get() {
                return new MemoryCacheParams(MAX_BITMAP_CACHE_SIZE, MAX_BITMAP_CACHE_ENTRIES, MAX_BITMAP_CACHE_EVICTION_SIZE, MAX_BITMAP_CACHE_EVICTION_ENTRIES, MAX_BITMAP_CACHE_ENTRY_SINGLE_SIZE);
            }
        };

        // 解码内存缓存  DefaultEncodedMemoryCacheParamsSupplier  4M，采用默认的
        Supplier<MemoryCacheParams> encodedMemoryCacheParamsSupplier = new Supplier<MemoryCacheParams>() {
            @Override
            public MemoryCacheParams get() {
                return new MemoryCacheParams(MAX_ENCODED_CACHE_SIZE, MAX_ENCODED_CACHE_ENTRIES, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
            }
        };

        // 从网络，从本地文件系统，本地资源加载图片和管理
        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(context)
                .setBitmapMemoryCacheParamsSupplier(bitmapMemoryCacheParamsSupplier) // 图片 bitmap内存大小
                .setEncodedMemoryCacheParamsSupplier(encodedMemoryCacheParamsSupplier) // 图片，encode内存大小
                .setMainDiskCacheConfig(mainDiskCacheConfig)        // 图片 磁盘大小
                .setDownsampleEnabled(true)
                .setRequestListeners(requestListeners)          // 监听器
                .setNetworkFetcher(new OkHttpNetworkFetcher(new OkHttpClient()))
                .setDownsampleEnabled(true) // 默认对图片进行自动缩放特性
                .build();

        Fresco.initialize(context, imagePipelineConfig);  // 初始化

        if (isDebug) {
            FLog.setMinimumLoggingLevel(FLog.VERBOSE); // Fresco的日志工具
        } else {
            FLog.setMinimumLoggingLevel(FLog.WARN);
        }
    }
}
