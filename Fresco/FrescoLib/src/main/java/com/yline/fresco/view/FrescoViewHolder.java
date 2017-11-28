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
import com.facebook.imagepipeline.common.ImageDecodeOptions;
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
    private Uri imageUri; // 网络链接
    protected FrescoView frescoView;
    private FrescoCallback.OnBdttErrorCallback onBdttErrorCallback; // 全局统一错误回调

    private ViewGroup.LayoutParams layoutParams; // View的大小
    private ResizeOptions resizeOptions; // 内存图片大小

    private Executor fetchExecutor; // 获取图片的线程

    private boolean isAutoPlayAnimations; // 自动播放
    private boolean isTapToRetryEnable; // 重试，仅仅4次机会

    private FrescoCallback.OnSimpleLoadCallback onSimpleLoadCallback; // 简易回调
    private FrescoCallback.OnSimpleProcessorCallback onSimpleProcessorCallback; // Bitmap处理简单回调
    private FrescoCallback.OnSimpleFetchCallback onSimpleFetchCallback; // 获取bitmap，回调

    protected void setOnBdttErrorCallback(FrescoCallback.OnBdttErrorCallback onBdttErrorCallback) {
        this.onBdttErrorCallback = onBdttErrorCallback;
    }

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

    public void setResizeOptions(ResizeOptions resizeOptions) {
        this.resizeOptions = resizeOptions;
    }

    public void setAutoPlayAnimations(boolean autoPlayAnimations) {
        isAutoPlayAnimations = autoPlayAnimations;
    }

    public void setTapToRetryEnable(boolean tapToRetryEnable) {
        isTapToRetryEnable = tapToRetryEnable;
    }

    public void setOnSimpleLoadCallback(FrescoCallback.OnSimpleLoadCallback onSimpleLoadCallback) {
        this.onSimpleLoadCallback = onSimpleLoadCallback;
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

        if (null != layoutParams) {
            frescoView.setLayoutParams(layoutParams);
        }

        ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(imageUri);
        if (null != resizeOptions) {
            imageRequestBuilder.setResizeOptions(resizeOptions);
            imageRequestBuilder.setImageDecodeOptions(ImageDecodeOptions.newBuilder().setBitmapConfig(Bitmap.Config.RGB_565).build());
        }

        PipelineDraweeControllerBuilder controllerBuilder = Fresco.newDraweeControllerBuilder();
        controllerBuilder.setOldController(frescoView.getController());
        controllerBuilder.setAutoPlayAnimations(isAutoPlayAnimations);
        controllerBuilder.setTapToRetryEnabled(isTapToRetryEnable);
        controllerBuilder.setImageRequest(imageRequestBuilder.build());
        controllerBuilder.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onSubmit(String id, Object callerContext) {
                super.onSubmit(id, callerContext);

                if (null != onSimpleLoadCallback) {
                    onSimpleLoadCallback.onStart(id, callerContext);
                }
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);

                if (null != onSimpleLoadCallback) {
                    onSimpleLoadCallback.onFailure(id, throwable);
                }

                if (null != onBdttErrorCallback) {
                    onBdttErrorCallback.onFailure(imageUri, "buildControllerUri");
                }
            }

            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);

                if (null != onSimpleLoadCallback) {
                    onSimpleLoadCallback.onSuccess(id, imageInfo, animatable);
                }
            }
        });

        frescoView.setController(controllerBuilder.build());
    }

    public void setOnSimpleProcessorCallback(FrescoCallback.OnSimpleProcessorCallback onSimpleProcessorCallback) {
        this.onSimpleProcessorCallback = onSimpleProcessorCallback;
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

        if (null != layoutParams) {
            frescoView.setLayoutParams(layoutParams);
        }

        ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(imageUri);
        if (null != resizeOptions) {
            imageRequestBuilder.setResizeOptions(resizeOptions);
            imageRequestBuilder.setImageDecodeOptions(ImageDecodeOptions.newBuilder().setBitmapConfig(Bitmap.Config.RGB_565).build());
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
        controllerBuilder.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);

                if (null != onBdttErrorCallback) {
                    onBdttErrorCallback.onFailure(imageUri, "buildProcessorUri");
                }
            }
        });

        frescoView.setController(controllerBuilder.build());
    }

    public void setFetchExecutor(Executor fetchExecutor) {
        this.fetchExecutor = fetchExecutor;
    }

    public void setOnSimpleFetchCallback(FrescoCallback.OnSimpleFetchCallback onSimpleFetchCallback) {
        this.onSimpleFetchCallback = onSimpleFetchCallback;
    }

    public void buildFetchDecodedImage() {
        if (null == imageUri) {
            return;
        }

        if (null == fetchExecutor) {
            return;
        }

        ImageRequest imageRequest = ImageRequest.fromUri(imageUri);
        DataSource<CloseableReference<CloseableImage>> dataSource = Fresco.getImagePipeline().fetchDecodedImage(imageRequest, null);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            protected void onNewResultImpl(Bitmap bitmap) {
                if (null != onSimpleFetchCallback) {
                    onSimpleFetchCallback.onSuccess(bitmap);
                }
            }

            @Override
            protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                if (null != onSimpleFetchCallback) {
                    onSimpleFetchCallback.onFailure(dataSource);
                }

                if (null != onBdttErrorCallback) {
                    onBdttErrorCallback.onFailure(imageUri, "buildFetchDecodedImage");
                }
            }
        }, fetchExecutor);
    }
}
