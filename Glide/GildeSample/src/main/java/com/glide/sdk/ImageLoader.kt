package com.glide.sdk

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.request.target.BaseTarget
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.util.Util
import com.glide.sdk.config.GlideApp

/**
 * GlideApp 是 配置了 @GlideModule 之后自动生成的。生成的目录在：build/generated/source/kapt/debug/ 下
 *
 * created on 2020-06-09 -- 15:17
 * @author yline
 */
object ImageLoader {
    /**
     * 最简单的展示图片
     *
     * 测试效果：
     * 能够简单的展示
     */
    fun displayImage(
        imageView: ImageView,
        url: String
    ) {
        GlideApp.with(imageView.context)
                .load(url)
                .into(imageView)
    }

    /**
     * 展示图片，使用占位符
     *
     * 占位符在主线程中加载，尽量比较小，而且是被系统缓存过的资源
     *
     * 测试效果：
     * 能够简单的展示，失败了会展示红色
     *
     * @param placeHolder 当请求正在执行时被展示的 Drawable
     * @param error 在请求永久性失败时展示, 请求为 null 时，没有设置 fallback 则展示error
     * @param fallback 在请求的url/model为 null 时展示
     */
    fun displayImage(
        imageView: ImageView,
        url: String,
        placeHolder: Drawable,
        error: Drawable,
        fallback: Drawable
    ) {
        GlideApp.with(imageView.context)
                .load(url)
                .placeholder(placeHolder)
                .error(error)
                .fallback(fallback)
                .into(imageView)
    }

    /**
     * 清除单张图片
     *
     * 测试效果：
     * 即使已经展示的图片，调用之后，也会清除
     *
     * 原因:
     * 如果 url 为 null，Glide 会清空 View 的内容
     */
    fun clearSingleImage(imageView: ImageView) {
        GlideApp.with(imageView.context)
                .clear(imageView)
    }

    /**
     * 清除 某一个加载目标
     *
     * 未测试
     */
    fun <T> clearSingleTarget(
        context: Context,
        target: Target<T>
    ) {
        GlideApp.with(context)
                .clear(target)
    }

    /**
     * 清除内存缓存
     *
     * 未测试
     */
    fun clearMemoryCache(context: Context) {
        GlideApp.get(context)
                .clearMemory()
    }

    /**
     * 清除磁盘缓存
     *
     * 未测试
     */
    fun clearDiskCache(context: Context): Boolean {
        if (!Util.isOnBackgroundThread()) {
            return false
        }

        GlideApp.get(context)
                .clearDiskCache()
        return true
    }

    /**
     * 后台异步加载
     *
     * 未测试
     */
    fun fetchSingleImage(
        context: Context,
        url: String
    ) {
        val target = GlideApp.with(context)
                .asBitmap()
                .load(url)
                .into(object : BaseTarget<Bitmap>() {
                    override fun getSize(cb: SizeReadyCallback) {
                    }

                    override fun removeCallback(cb: SizeReadyCallback) {
                    }

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                    }
                })

    }

}

