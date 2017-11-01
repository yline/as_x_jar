package com.yline.fresco.view;

import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.view.ViewGroup;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.yline.fresco.common.FrescoCallback;

import java.util.concurrent.Executor;

/**
 * 特定用来配置 Fresco参数
 *
 * @author yline 2017/9/23 -- 17:41
 * @version 1.0.0
 */
class FrescoViewHolder {
    private ViewGroup.LayoutParams layoutParams; // View的大小
    private ResizeOptions resizeOptions; // 内存图片大小

    private Uri imageUri; // 网络链接
    private Uri imageUriLower; // 低分辨率 图片链接

    protected FrescoView frescoView;
    private Executor fetchExecutor; // 获取图片的线程

    private boolean isAutoPlayAnimations; // 自动播放
    private boolean isTapToRetryEnable; // 重试，仅仅4次机会

    private FrescoCallback.OnSimpleLoadCallback simpleLoadCallback; // 简易回调
    private FrescoCallback.OnSimpleProcessorCallback onSimpleProcessorCallback; // Bitmap处理简单回调
    private FrescoCallback.OnSimpleFetchCallback simpleFetchCallback; // 获取bitmap，回调

    public FrescoViewHolder(FrescoView view) {
        assert null != view;
        this.frescoView = view;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public void setLayoutParams(ViewGroup.LayoutParams layoutParams) {
        this.layoutParams = layoutParams;
    }

    /**
     * 加载静态：
     * 1）静态图，测试过的支持 png、jpg、webp
     * 2）动态图，则只显示第一帧，测试过的支持 gif、webp
     */
    public void buildImageUri() {
        if (null == imageUri) {
            return;
        }

        if (null != layoutParams) {
            frescoView.setLayoutParams(layoutParams);
        }

        frescoView.setImageURI(imageUri);
    }

    public void setAutoPlayAnimations(boolean autoPlayAnimations) {
        isAutoPlayAnimations = autoPlayAnimations;
    }

    public void setTapToRetryEnable(boolean tapToRetryEnable) {
        isTapToRetryEnable = tapToRetryEnable;
    }

    public void setOnSimpleLoadCallback(FrescoCallback.OnSimpleLoadCallback simpleLoadCallback) {
        this.simpleLoadCallback = simpleLoadCallback;
    }

    /**
     * 设置为加载静态：
     * 1）静态图，测试过的支持 png、jpg、webp
     * 2）动态图，则只显示第一帧，测试过的支持 gif、webp
     * 设置为加载动态：
     * 1）静态图，则显示静态图片，测试过的支持 png、jpg、webp
     * 2）动态图，则动画动起来，测试过的支持 gif、webp
     */
    public void buildControllerUri() {
        if (null == imageUri) {
            return;
        }

        PipelineDraweeControllerBuilder controllerBuilder = Fresco.newDraweeControllerBuilder();
        controllerBuilder.setOldController(frescoView.getController());
        controllerBuilder.setAutoPlayAnimations(isAutoPlayAnimations);
        controllerBuilder.setTapToRetryEnabled(isTapToRetryEnable);
        controllerBuilder.setUri(imageUri);
        if (null != simpleLoadCallback) {
            controllerBuilder.setControllerListener(new BaseControllerListener<ImageInfo>() {
                @Override
                public void onSubmit(String id, Object callerContext) {
                    super.onSubmit(id, callerContext);
                    simpleLoadCallback.onStart(id, callerContext);
                }

                @Override
                public void onFailure(String id, Throwable throwable) {
                    super.onFailure(id, throwable);
                    simpleLoadCallback.onFailure(id, throwable);
                }

                @Override
                public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                    super.onFinalImageSet(id, imageInfo, animatable);
                    simpleLoadCallback.onSuccess(id, imageInfo, animatable);
                }
            });
        }

        frescoView.setController(controllerBuilder.build());
    }

    public void setImageUriLower(Uri imageUriLower) {
        this.imageUriLower = imageUriLower;
    }

    /**
     * 只支持静态图；测试过的支持：png、jpg、webp
     */
    public void buildControllerComplexUri() {
        if (null == imageUri) {
            return;
        }

        if (null == imageUriLower) {
            return;
        }

        PipelineDraweeControllerBuilder controllerBuilder = Fresco.newDraweeControllerBuilder();
        controllerBuilder.setLowResImageRequest(ImageRequest.fromUri(imageUriLower));
        controllerBuilder.setImageRequest(ImageRequest.fromUri(imageUri));
        controllerBuilder.setOldController(frescoView.getController());

        frescoView.setController(controllerBuilder.build());
    }

    public void setOnSimpleProcessorCallback(FrescoCallback.OnSimpleProcessorCallback onSimpleProcessorCallback) {
        this.onSimpleProcessorCallback = onSimpleProcessorCallback;
    }

    public void setResizeOptions(ResizeOptions resizeOptions) {
        this.resizeOptions = resizeOptions;
    }

    /**
     * 加载静态：
     * 1）静态图，测试过的支持 png、jpg、webp
     * 2）动态图，直接显示第一帧高清图，不会对图片进行处理
     */
    public void buildProcessorUri() {
        if (null == imageUri) {
            return;
        }

        ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(imageUri);
        if (null != resizeOptions){
            imageRequestBuilder.setResizeOptions(resizeOptions);
        }

        imageRequestBuilder.setPostprocessor(new BasePostprocessor() {
            @Override
            public void process(Bitmap bitmap) {
                super.process(bitmap);
                if (null != onSimpleProcessorCallback) {
                    onSimpleProcessorCallback.onProcess(bitmap);
                }
            }
        });

        PipelineDraweeControllerBuilder controllerBuilder = Fresco.newDraweeControllerBuilder();
        controllerBuilder.setOldController(frescoView.getController());
        controllerBuilder.setImageRequest(imageRequestBuilder.build());

        frescoView.setController(controllerBuilder.build());
    }

    public void setFetchExecutor(Executor fetchExecutor) {
        this.fetchExecutor = fetchExecutor;
    }

    public void setOnSimpleFetchCallback(FrescoCallback.OnSimpleFetchCallback simpleFetchCallback) {
        this.simpleFetchCallback = simpleFetchCallback;
    }

    public void buildFetchDecodedImage() {
        if (null == imageUri) {
            return;
        }

        if (null == fetchExecutor){
            return;
        }

        ImageRequest imageRequest = ImageRequest.fromUri(imageUri);
        DataSource<CloseableReference<CloseableImage>> dataSource = Fresco.getImagePipeline().fetchDecodedImage(imageRequest, null);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            protected void onNewResultImpl(Bitmap bitmap) {
                if (null != simpleFetchCallback) {
                    simpleFetchCallback.onSuccess(bitmap);
                }
            }

            @Override
            protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                if (null != simpleFetchCallback) {
                    simpleFetchCallback.onFailure(dataSource);
                }
            }
        }, fetchExecutor);
    }
}
