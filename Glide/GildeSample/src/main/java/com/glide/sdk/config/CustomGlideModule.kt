package com.glide.sdk.config

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat.PREFER_RGB_565
import com.bumptech.glide.load.Option
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.HttpUrlFetcher
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelCache
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import java.io.File
import java.io.InputStream

/**
 * 做一些全局的策略，设置
 *
 * created on 2020-06-09 -- 12:08
 * @author yline
 */
@GlideModule
class CustomGlideModule : AppGlideModule() {

    companion object {
        fun getCacheFile(
            context: Context
        ): File? {
            val innerCacheDir = getCacheDirInner(context, CACHE_DIR)

            if (null != innerCacheDir && innerCacheDir.exists()) {
                return innerCacheDir
            }

            val cacheDir = context.externalCacheDir
            if (null == cacheDir || !cacheDir.canWrite()) {
                return innerCacheDir
            }

            return File(cacheDir, CACHE_DIR)
        }

        private fun getCacheDirInner(
            context: Context,
            diskCacheName: String?
        ): File? {
            val cacheDir = context.cacheDir
            if (null == cacheDir) {
                return cacheDir
            }

            if (null != diskCacheName) {
                return File(cacheDir, diskCacheName)
            }
            return cacheDir
        }

        private const val CACHE_SIZE = 150L shl 20
        private const val CACHE_DIR = "glide-cache"
    }

    override fun applyOptions(
        context: Context,
        builder: GlideBuilder
    ) {
        super.applyOptions(context, builder)

        // 确定地址，和上述提供的api地址相同
        builder.setDiskCache(ExternalPreferredCacheDiskCacheFactory(context, CACHE_DIR, CACHE_SIZE))

        // 图片质量降低
        builder.setDefaultRequestOptions(RequestOptions().format(PREFER_RGB_565))
    }

    override fun registerComponents(
        context: Context,
        glide: Glide,
        registry: Registry
    ) {
        super.registerComponents(context, glide, registry)

        registry.replace(GlideUrl::class.java, InputStream::class.java, HttpUrlLoaderFactory())
    }

    /**
     * 上述使用
     */
    inner class HttpUrlLoader constructor(private val modelCache: ModelCache<GlideUrl, GlideUrl>? = null) :
            ModelLoader<GlideUrl, InputStream> {

        /**
         * An integer option that is used to determine the maximum connect and read timeout durations (in
         * milliseconds) for network connections.
         *
         *
         * Defaults to 2500ms.
         */
        private val TIMEOUT = Option.memory(
                "com.bumptech.glide.load.model.stream.HttpGlideUrlLoader.Timeout", 2500
        )

        override fun buildLoadData(
            model: GlideUrl,
            width: Int,
            height: Int,
            options: Options
        ): ModelLoader.LoadData<InputStream>? {
            // GlideUrls memoize parsed URLs so caching them saves a few object instantiations and time
            // spent parsing urls.
            var url: GlideUrl? = model
            if (modelCache != null) {
                url = modelCache.get(model, 0, 0)
                if (url == null) {
                    modelCache.put(model, 0, 0, model)
                    url = model
                }
            }
            val timeout = options.get(TIMEOUT)!!
            return ModelLoader.LoadData(url!!, HttpUrlFetcher(url, timeout))
        }

        override fun handles(model: GlideUrl): Boolean {
            return true
        }
    }

    /**
     * The default factory for [HttpUrlLoader]s.
     */
    inner class HttpUrlLoaderFactory : ModelLoaderFactory<GlideUrl, InputStream> {
        private val modelCache = ModelCache<GlideUrl, GlideUrl>(500)

        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<GlideUrl, InputStream> {
            return HttpUrlLoader(modelCache)
        }

        override fun teardown() {
            // Do nothing.
        }
    }
}


