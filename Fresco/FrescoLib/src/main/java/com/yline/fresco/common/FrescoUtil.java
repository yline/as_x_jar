package com.yline.fresco.common;

import android.net.Uri;
import android.text.TextUtils;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.listener.BaseRequestListener;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;

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

    public static DataSource<Void> prefetchToDiskCache(String httpUrl, BaseRequestListener requestListener) {
        if (TextUtils.isEmpty(httpUrl)) {
            return null;
        }

        Uri imageUri = Uri.parse(httpUrl);

        ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(imageUri);
        imageRequestBuilder.setRequestListener(requestListener);

        return Fresco.getImagePipeline().prefetchToDiskCache(imageRequestBuilder.build(), null);
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

    /**
     * 获取本地缓存路径
     *
     * @param imgUrl 图片url
     * @return 图片路径
     */
    public static String getPathFromDiskCache(String imgUrl) {
        if (!TextUtils.isEmpty(imgUrl)) {
            CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(imgUrl), null);

            BinaryResource binaryResource = null;
            if (ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey)) {
                binaryResource = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
            } else if (ImagePipelineFactory.getInstance().getSmallImageFileCache().hasKey(cacheKey)) {
                binaryResource = ImagePipelineFactory.getInstance().getSmallImageFileCache().getResource(cacheKey);
            }

            if (null != binaryResource && binaryResource instanceof FileBinaryResource) {
                File localFile = ((FileBinaryResource) binaryResource).getFile();
                if (null != localFile) {
                    return localFile.getPath();
                }
            }
        }
        return "";
    }
}
