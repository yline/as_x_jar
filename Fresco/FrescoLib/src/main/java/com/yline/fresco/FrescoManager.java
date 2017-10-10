package com.yline.fresco;

import android.net.Uri;

import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.util.UriUtil;
import com.yline.fresco.common.FrescoCallback;
import com.yline.fresco.common.FrescoUtil;
import com.yline.fresco.view.FrescoView;
import com.yline.fresco.view.FrescoViewSafelyHolder;

import java.util.concurrent.Executors;

/**
 * Fresco调用工具类；大部分常用的都放在这里了；
 * 少量不常用的api，在FrescoUtil
 *
 * @author yline 2017/9/23 -- 14:48
 * @version 1.0.0
 */
public class FrescoManager {
    private FrescoManager() {
    }

    /**
     * 显示本地图片；Res目录下的
     */
    public static void setImageResource(FrescoView frescoView, int imageId) {
        FrescoViewSafelyHolder safelyHolder = new FrescoViewSafelyHolder(frescoView);

        Uri imageUri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME).path(String.valueOf(imageId)).build();
        safelyHolder.setImageUri(imageUri);
        safelyHolder.buildImageUri();
    }

    public static void setImageUri(FrescoView frescoView, String imageUri) {
        FrescoViewSafelyHolder safelyHolder = new FrescoViewSafelyHolder(frescoView);

        safelyHolder.setImageUri(imageUri);
        safelyHolder.buildImageUri();
    }

    /**
     * 显示静态图
     *
     * @param width  控件宽度
     * @param height 控件高度
     */
    public static void setImageUri(FrescoView frescoView, String imageUri, int width, int height) {
        FrescoViewSafelyHolder safelyHolder = new FrescoViewSafelyHolder(frescoView);

        safelyHolder.setLayoutParams(width, height);
        safelyHolder.setImageUri(imageUri);
        safelyHolder.buildImageUri();
    }

    public static void setImageUri(FrescoView frescoView, String imageUri, boolean isRetry, FrescoCallback.OnSimpleLoadCallback onSimpleLoadCallback) {
        FrescoViewSafelyHolder safelyHolder = new FrescoViewSafelyHolder(frescoView);

        safelyHolder.setImageUri(imageUri);
        safelyHolder.setTapToRetryEnable(isRetry);
        safelyHolder.setOnSimpleLoadCallback(onSimpleLoadCallback);
        safelyHolder.buildControllerUri();
    }

    public static void setImageUri(FrescoView frescoView, String lowerImageUri, String imageUri) {
        FrescoViewSafelyHolder safelyHolder = new FrescoViewSafelyHolder(frescoView);

        safelyHolder.setImageUriLower(lowerImageUri);
        safelyHolder.setImageUri(imageUri);
        safelyHolder.buildControllerComplexUri();
    }

    public static void setDynamicUri(FrescoView frescoView, String dynamicUri, boolean isRetry, FrescoCallback.OnSimpleLoadCallback onSimpleLoadCallback) {
        FrescoViewSafelyHolder safelyHolder = new FrescoViewSafelyHolder(frescoView);

        safelyHolder.setImageUri(dynamicUri);
        safelyHolder.setAutoPlayAnimations(true);
        safelyHolder.setTapToRetryEnable(isRetry);
        safelyHolder.setOnSimpleLoadCallback(onSimpleLoadCallback);
        safelyHolder.buildControllerUri();
    }

    public static void setDynamicUri(FrescoView frescoView, String dynamicUri, boolean isRetry, boolean isAutoPlayer, FrescoCallback.OnSimpleLoadCallback onSimpleLoadCallback) {
        FrescoViewSafelyHolder safelyHolder = new FrescoViewSafelyHolder(frescoView);

        safelyHolder.setImageUri(dynamicUri);
        safelyHolder.setAutoPlayAnimations(isAutoPlayer);
        safelyHolder.setTapToRetryEnable(isRetry);
        safelyHolder.setOnSimpleLoadCallback(onSimpleLoadCallback);
        safelyHolder.buildControllerUri();
    }

    /**
     * 显示需要处理的图片，例如高斯模糊
     */
    public static void setProcessorUri(FrescoView frescoView, String imageUri, FrescoCallback.OnSimpleProcessorCallback callback) {
        FrescoViewSafelyHolder safelyHolder = new FrescoViewSafelyHolder(frescoView);

        safelyHolder.setImageUri(imageUri);
        safelyHolder.setOnSimpleProcessorCallback(callback);
        safelyHolder.buildProcessorUri();
    }

    /**
     * 加载图片，回调在子线程上执行
     */
    public static void fetchDecodedImageThread(FrescoView frescoView, String imageUri, FrescoCallback.OnSimpleFetchCallback callback) {
        FrescoViewSafelyHolder safelyHolder = new FrescoViewSafelyHolder(frescoView);

        safelyHolder.setImageUri(imageUri);
        safelyHolder.setOnSimpleFetchCallback(callback);
        safelyHolder.setFetchExecutor(Executors.newSingleThreadExecutor());
        safelyHolder.buildFetchDecodedImage();
    }

    /**
     * 加载图片，回调在UI上执行
     */
    public static void fetchDecodedImageUi(FrescoView frescoView, String imageUri, FrescoCallback.OnSimpleFetchCallback callback) {
        FrescoViewSafelyHolder safelyHolder = new FrescoViewSafelyHolder(frescoView);

        safelyHolder.setImageUri(imageUri);
        safelyHolder.setOnSimpleFetchCallback(callback);
        safelyHolder.setFetchExecutor(UiThreadImmediateExecutorService.getInstance());
        safelyHolder.buildFetchDecodedImage();
    }

    /**
     * 单纯的预加载
     */
    public static void prefetchToDiskCache(String httpUri) {
        FrescoUtil.prefetchToDiskCache(httpUri);
    }

    /**
     * 单纯的预加载
     */
    public static void prefetchToBitmapCache(String httpUri) {
        FrescoUtil.prefetchToBitmapCache(httpUri);
    }
}
