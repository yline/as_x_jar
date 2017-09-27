package com.yline.fresco.common;

import android.net.Uri;
import android.text.TextUtils;

import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.request.ImageRequest;

public class FrescoUtil {
    /* -------------------------------- 预加载 -------------------------------- */
    public static DataSource<Void> prefetchToBitmapCache(String httpUri) {
        if (TextUtils.isEmpty(httpUri)) {
            return null;
        }

        ImageRequest imageRequest = ImageRequest.fromUri(httpUri);
        return Fresco.getImagePipeline().prefetchToBitmapCache(imageRequest, null);
        // dataSource.close(); // 取消；若已完成，则无效
    }

    public static DataSource<Void> prefetchToDiskCache(String httpUri) {
        if (TextUtils.isEmpty(httpUri)) {
            return null;
        }

        ImageRequest imageRequest = ImageRequest.fromUri(httpUri);
        return Fresco.getImagePipeline().prefetchToDiskCache(imageRequest, null);
        // dataSource.close(); // 取消；若已完成，则无效
    }

    /* -------------------------------- 移除缓存 -------------------------------- */
    public static void removeCaches(Uri uri) {
        Fresco.getImagePipeline().evictFromCache(uri);
    }

    public static void removeMemoryCaches(Uri uri) {
        Fresco.getImagePipeline().evictFromMemoryCache(uri);
    }

    public static void removeDiskCaches(Uri uri) {
        Fresco.getImagePipeline().evictFromDiskCache(uri);
    }

    public static void clearCaches() {
        Fresco.getImagePipeline().clearCaches();
    }

    public static void clearMemoryCaches() {
        Fresco.getImagePipeline().clearMemoryCaches();
    }

    public static void clearDiskCaches() {
        Fresco.getImagePipeline().clearDiskCaches();
    }

    /*  -------------------------------- 判断缓存 -------------------------------- */
    public static long getFileCacheSize() {
        return getFileCacheMainSize() + getFileCacheSmallSize();
    }

    public static long getFileCacheSmallSize() {
        return Fresco.getImagePipelineFactory().getSmallImageFileCache().getSize();
    }

    public static long getFileCacheMainSize() {
        return Fresco.getImagePipelineFactory().getMainFileCache().getSize();
    }

    /* -------------------------------- 检查bitmap是否在缓存 -------------------------------- */
    public static boolean isInBitmapMemoryCache(Uri uri) {
        return Fresco.getImagePipeline().isInBitmapMemoryCache(uri);
    }
}
