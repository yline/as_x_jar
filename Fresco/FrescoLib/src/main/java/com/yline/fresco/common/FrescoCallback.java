package com.yline.fresco.common;


import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.support.annotation.Nullable;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;

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
     * 获取bitmap，回调
     */
    public interface OnSimpleFetchCallback{
        /**
         * 加载失败
         * @param dataSource 提示信息
         */
        void onFailure(DataSource<CloseableReference<CloseableImage>> dataSource);

        /**
         * 1，无法获取动图
         * 2，该Bitmap会被回收，无法用于显示
         * 3，可以传给通知栏或Remote View，因为它在共享内存中copy了一份
         * @param bitmap bitmap
         */
        void onSuccess(Bitmap bitmap);
    }
}
