package com.yline.fresco.common;

import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;

/**
 * Fresco 图片加载，最简单的回调
 *
 * @author yline 2017/9/26 -- 21:25
 * @version 1.0.0
 */
public class FrescoCallback {

    /**
     * 最简单的加载，自定义的回调
     */
    public interface OnSimpleLoadCallback {

        /**
         * Load 开始
         *
         * @param id            任务 唯一标识
         * @param callerContext 上下文，很可能为null
         */
        void onStart(String id, Object callerContext);

        /**
         * Load 失败回调
         *
         * @param id        任务 唯一标识
         * @param throwable 错误类型
         */
        void onFailure(String id, Throwable throwable);

        /**
         * Load 成功回调
         *
         * @param id         任务唯一标识
         * @param imageInfo  图片信息
         * @param animatable 动画的控制器
         */
        void onSuccess(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable);
    }

    /**
     * 获取bitmap，回调
     */
    public interface OnSimpleFetchCallback {
        /**
         * Fetch 开始 {UI线程}
         *
         * @param request       请求配置
         * @param callerContext 上下文，很可能为null
         * @param requestId     任务 唯一标识
         * @param isPrefetch    是否已经预加载
         */
        void onStart(ImageRequest request, Object callerContext, String requestId, boolean isPrefetch);

        /**
         * Fetch 失败回调 {子线程}
         *
         * @param request    请求配置
         * @param requestId  任务 唯一标识
         * @param throwable  异常
         * @param isPrefetch 是否已经预加载
         */
        void onFailure(ImageRequest request, String requestId, Throwable throwable, boolean isPrefetch);

        /**
         * Fetch 成功回调 {子线程}
         *
         * @param request    请求配置
         * @param requestId  任务 唯一标识
         * @param isPrefetch 是否已经预加载
         */
        void onSuccess(ImageRequest request, String requestId, boolean isPrefetch);
    }

    /**
     * 最简单的，展示图片，bitmap处理，回调
     */
    public interface OnSimpleProcessorCallback {
        /**
         * 处理图片 回调，引用Bitmap会被自动清空
         *
         * @param bitmap 操作bitmap
         */
        void onProcess(Bitmap bitmap);
    }

    /**
     * 图片加载失败，全局统一一个回调
     */
    public interface OnBdttErrorCallback {
        /**
         * 本地头条，加载失败
         */
        void onFailure(Uri imageUrl, String hint);
    }
}
