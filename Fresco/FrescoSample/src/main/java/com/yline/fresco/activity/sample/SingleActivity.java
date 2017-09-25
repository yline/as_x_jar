package com.yline.fresco.activity.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.facebook.cache.common.CacheKey;
import com.facebook.common.logging.FLog;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.UriUtil;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.yline.base.BaseAppCompatActivity;
import com.yline.fresco.contant.UrlConstant;
import com.yline.fresco.sample.R;
import com.yline.fresco.util.blur.BitmapBlurHelper;
import com.yline.fresco.view.FrescoView;

import java.util.concurrent.Executors;

public class SingleActivity extends BaseAppCompatActivity {
    private static final String TAG = "xxx-";

    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, SingleActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_single);

        FrescoView frescoView = (FrescoView) findViewById(R.id.fresco_view_single);

        frescoView.setAspectRatio(3.0f / 4); // w/h

        new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME).path(String.valueOf(R.drawable.image_retry)).build();

        ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(UrlConstant.getJpg_1366_768()));
        imageRequestBuilder.setProgressiveRenderingEnabled(true);
        imageRequestBuilder.setPostprocessor(new BasePostprocessor() {
            @Override
            public String getName() {
                return super.getName();
            }

            // 复制图片为不同的大小
            @Override
            public CloseableReference<Bitmap> process(Bitmap sourceBitmap, PlatformBitmapFactory bitmapFactory) {
                return super.process(sourceBitmap, bitmapFactory);
            }

            // 复制的图片 + 原图
            @Override
            public void process(Bitmap destBitmap, Bitmap sourceBitmap) {
                super.process(destBitmap, sourceBitmap);

                destBitmap.setHasAlpha(true);
            }

            // 复制后的图片
            @Override
            public void process(Bitmap destBitmap) {
                // super.process(bitmap);
                BitmapBlurHelper.blur(destBitmap, 5); // 高斯模糊
            }

            // 设定有值，则会返回缓存；key - value对应的关系
            @Nullable
            @Override
            public CacheKey getPostprocessorCacheKey() {
                return super.getPostprocessorCacheKey();
            }
        });
        imageRequestBuilder.setAutoRotateEnabled(true);
        imageRequestBuilder.setRotationOptions(RotationOptions.autoRotate());

        imageRequestBuilder.setLowestPermittedRequestLevel(ImageRequest.RequestLevel.BITMAP_MEMORY_CACHE);

        ImageRequest imageRequest = imageRequestBuilder.build();


        PipelineDraweeControllerBuilder controllerBuilder = Fresco.newDraweeControllerBuilder();
        controllerBuilder.setOldController(frescoView.getController());
        controllerBuilder.setAutoPlayAnimations(true);
        controllerBuilder.setImageRequest(imageRequest);
        DraweeController draweeController = controllerBuilder.build();

        frescoView.getHierarchy().setProgressBarImage(new ProgressBarDrawable() {
            @Override
            protected boolean onLevelChange(int level) {
                return super.onLevelChange(level);
            }
        });

        frescoView.setController(draweeController);
/*
        ColorFilter colorFilter = new ColorMatrixColorFilter(new float[]{});
        frescoView.getHierarchy().setActualImageColorFilter(colorFilter);
*/

        controllerBuilder.setControllerListener(new BaseControllerListener<ImageInfo>() {

            // 图片加载成功，触发
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);

                if (null == imageInfo) {
                    return;
                }

                QualityInfo qualityInfo = imageInfo.getQualityInfo();
                FLog.d("Final image received! Size %d x %d",
                        "Quality level %d, good enough: %s, full quality: %s",
                        imageInfo.getWidth(), imageInfo.getHeight(),
                        qualityInfo.getQuality(), qualityInfo.isOfGoodEnoughQuality(), qualityInfo.isOfFullQuality());

                // 手动控制 动画 播放
                if (null != animatable) {
                    animatable.start();

                    // later
                    animatable.stop();
                }
            }

            // 渐进式，每个扫描被解码后，回调
            @Override
            public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
                super.onIntermediateImageSet(id, imageInfo);
                FLog.d("Intermediate image received", "");
            }

            // 图片加载失败，触发
            @Override
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
                FLog.e(getClass(), throwable, "Error loading %s", id);
            }
        });

        ImagePipeline imagePipeline = Fresco.getImagePipeline();

        // 获取未解码的图片
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, null);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            protected void onNewResultImpl(Bitmap bitmap) {
                // TODO
            }

            @Override
            protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                Throwable t = dataSource.getFailureCause();
                // handle failure
            }
        }, Executors.newSingleThreadExecutor());

        // UiThreadImmediateExecutorService.getInstance() 异步UI操作；放到UI线程执行
        // CallerThreadExecutor.getInstance() 回调事较少，不涉及UI，该回调的执行是得不到保证的
        // Executors.newSingleThreadExecutor()   比较复杂、耗时的操作，并且不涉及UI，就用自己新建出来的

        /* 两个监听事件
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(UrlConstant.getPng_1024_1024()))
                 .setPostprocessor(new BasePostprocessor() { // 高斯模糊
                    @Override
                    public void process(Bitmap bitmap) {
                        // super.process(bitmap);
                        BitmapBlurHelper.blur(bitmap, 5);
                    }
                })
                .build();

        controllerBuilder.setControllerListener(new BaseControllerListener<ImageInfo>());
        */
    }
}
