package com.yline.fresco.common;

import android.content.Context;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpNetworkFetcher;
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
                    .setMaxCacheSize(1024 * 1024 * 1024)
                    .setMaxCacheSizeOnLowDiskSpace(100 * 1024 * 1024)
                    .build();
        }

        // 从网络，从本地文件系统，本地资源加载图片和管理
        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(context)
                .setMainDiskCacheConfig(mainDiskCacheConfig)        // 图片
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
