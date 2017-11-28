package com.yline.fresco;

import android.net.Uri;

import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.util.UriUtil;
import com.facebook.imagepipeline.listener.BaseRequestListener;
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
     * 1）静态图，测试过的支持 png、jpg、webp
     * 2）动态图，则只显示第一帧，测试过的支持 gif、webp
     */
    public static void setImageUri(FrescoView frescoView, String imageUri) {
        FrescoViewSafelyHolder safelyHolder = new FrescoViewSafelyHolder(frescoView);

        safelyHolder.setImageUri(imageUri);
        safelyHolder.buildControllerUri();
    }

    /**
     * 1）静态图，测试过的支持 png、jpg、webp
     * 2）动态图，则只显示第一帧，测试过的支持 gif、webp
     *
     * @param width  控件宽度
     * @param height 控件高度
     */
    public static void setImageUri(FrescoView frescoView, String imageUri, int width, int height) {
        FrescoViewSafelyHolder safelyHolder = new FrescoViewSafelyHolder(frescoView);

        safelyHolder.setLayoutParams(width, height);
        safelyHolder.setResizeOptions(width, height);
        safelyHolder.setImageUri(imageUri);
        safelyHolder.buildControllerUri();
    }

    /**
     * 1）静态图，测试过的支持 png、jpg、webp
     * 2）动态图，则只显示第一帧，测试过的支持 gif、webp
     */
    public static void setImageUri(FrescoView frescoView, String imageUri, boolean isRetry, FrescoCallback.OnSimpleLoadCallback onSimpleLoadCallback) {
        FrescoViewSafelyHolder safelyHolder = new FrescoViewSafelyHolder(frescoView);

        safelyHolder.setImageUri(imageUri);
        safelyHolder.setTapToRetryEnable(isRetry);
        safelyHolder.setOnSimpleLoadCallback(onSimpleLoadCallback);
        safelyHolder.buildControllerUri();
    }

    /*******************************************不常用的方法*******************************************/
    /**
     * 显示本地图片；Res目录下的
     *
     * @param frescoView 展示的控件
     * @param imageId    图片资源
     */
    public static void setImageResource(FrescoView frescoView, int imageId) {
        FrescoViewSafelyHolder safelyHolder = new FrescoViewSafelyHolder(frescoView);

        Uri imageUri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME).path(String.valueOf(imageId)).build();
        safelyHolder.setImageUri(imageUri);
        safelyHolder.buildControllerUri();
    }

    /**
     * 显示本地图片；Res目录下的
     *
     * @param frescoView 展示的控件
     * @param imageId    图片资源
     * @param width      控件宽度
     * @param height     控件高度
     */
    public static void setImageResource(FrescoView frescoView, int imageId, int width, int height) {
        FrescoViewSafelyHolder safelyHolder = new FrescoViewSafelyHolder(frescoView);

        Uri imageUri = new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME).path(String.valueOf(imageId)).build();
        safelyHolder.setImageUri(imageUri);
        safelyHolder.setLayoutParams(width, height);
        safelyHolder.buildControllerUri();
    }

    /**
     * 显示本地图片；文件夹下的；
     * 不需要file:///格式
     * 若路劲有file://开头，则直接使用setImageUri()即可
     *
     * @param frescoView 展示的控件
     * @param path       图片路径
     */
    public static void setImageLocal(FrescoView frescoView, String path, FrescoCallback.OnSimpleLoadCallback onSimpleLoadCallback) {
        FrescoViewSafelyHolder safelyHolder = new FrescoViewSafelyHolder(frescoView);

        Uri imageUri = new Uri.Builder().scheme(UriUtil.LOCAL_FILE_SCHEME).path(path).build();
        safelyHolder.setImageUri(imageUri);
        safelyHolder.setOnSimpleLoadCallback(onSimpleLoadCallback);
        safelyHolder.buildControllerUri();
    }

    /**
     * 显示本地图片；文件夹下的；
     * 不需要file:///格式
     * 若路劲有file://开头，则直接使用setImageUri()即可
     *
     * @param frescoView   展示的控件
     * @param path         图片路径
     * @param bitmapWidth  期望图片的宽度
     * @param bitmapHeight 期望图片的高度
     */
    public static void setImageLocal(FrescoView frescoView, String path, int bitmapWidth, int bitmapHeight) {
        FrescoViewSafelyHolder safelyHolder = new FrescoViewSafelyHolder(frescoView);

        Uri imageUri = new Uri.Builder().scheme(UriUtil.LOCAL_FILE_SCHEME).path(path).build();
        safelyHolder.setImageUri(imageUri);
        safelyHolder.setResizeOptions(bitmapWidth, bitmapHeight);
        safelyHolder.buildControllerUri();
    }

    public static void setImageLocal(FrescoView frescoView, String path, int bitmapWidth, int bitmapHeight, FrescoCallback.OnSimpleLoadCallback onSimpleLoadCallback) {
        FrescoViewSafelyHolder safelyHolder = new FrescoViewSafelyHolder(frescoView);

        Uri imageUri = new Uri.Builder().scheme(UriUtil.LOCAL_FILE_SCHEME).path(path).build();
        safelyHolder.setImageUri(imageUri);
        safelyHolder.setResizeOptions(bitmapWidth, bitmapHeight);
        safelyHolder.setOnSimpleLoadCallback(onSimpleLoadCallback);
        safelyHolder.buildControllerUri();
    }

    /**
     * 显示需要处理的图片；例如高斯模糊
     *
     * @param frescoView   展示的控件
     * @param imageUri     图片链接
     * @param bitmapWidth  期望图片的宽度
     * @param bitmapHeight 期望图片的高度
     * @param callback     处理图片的回调
     */
    public static void setProcessorUri(FrescoView frescoView, String imageUri, int bitmapWidth, int bitmapHeight, FrescoCallback.OnSimpleProcessorCallback callback) {
        FrescoViewSafelyHolder safelyHolder = new FrescoViewSafelyHolder(frescoView);

        safelyHolder.setImageUri(imageUri);
        safelyHolder.setResizeOptions(bitmapWidth, bitmapHeight);
        safelyHolder.setOnSimpleProcessorCallback(callback);
        safelyHolder.buildProcessorUri();
    }

    /**
     * 加载图片
     *
     * @param frescoView 展示的控件
     * @param imageUri   图片链接
     * @param callback   回调，在子线程上，执行
     */
    public static void fetchDecodedImageThread(FrescoView frescoView, String imageUri, FrescoCallback.OnSimpleFetchCallback callback) {
        FrescoViewSafelyHolder safelyHolder = new FrescoViewSafelyHolder(frescoView);

        safelyHolder.setImageUri(imageUri);
        safelyHolder.setOnSimpleFetchCallback(callback);
        safelyHolder.setFetchExecutor(Executors.newSingleThreadExecutor());
        safelyHolder.buildFetchDecodedImage();
    }

    /**
     * 加载图片
     *
     * @param frescoView 展示的控件
     * @param imageUri   图片链接
     * @param callback   回调，在UI上执行
     */
    public static void fetchDecodedImageUi(FrescoView frescoView, String imageUri, FrescoCallback.OnSimpleFetchCallback callback) {
        FrescoViewSafelyHolder safelyHolder = new FrescoViewSafelyHolder(frescoView);

        safelyHolder.setImageUri(imageUri);
        safelyHolder.setOnSimpleFetchCallback(callback);
        safelyHolder.setFetchExecutor(UiThreadImmediateExecutorService.getInstance());
        safelyHolder.buildFetchDecodedImage();
    }

    /*******************************************转接其它工具类*******************************************/
    /**
     * 预加载图片，到磁盘中
     *
     * @param httpUri         图片链接
     * @param requestListener 请求回调
     */
    public static void prefetchToDiskCache(String httpUri, BaseRequestListener requestListener) {
        FrescoUtil.prefetchToDiskCache(httpUri, requestListener);
    }

    /**
     * 预加载图片，到内存中
     *
     * @param httpUri 图片链接
     */
    public static void prefetchToBitmapCache(String httpUri) {
        FrescoUtil.prefetchToBitmapCache(httpUri);
    }

    /**
     * 获取图片加载的，磁盘路径
     *
     * @param httpUrl 图片链接
     * @return 路径
     */
    public static String getCacheFilePath(String httpUrl) {
        return FrescoUtil.getPathFromDiskCache(httpUrl);
    }
}
