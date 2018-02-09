package com.yline.fresco.activity.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.yline.base.BaseAppCompatActivity;
import com.yline.fresco.drawable.LevelLoadingRenderer;
import com.yline.fresco.drawable.LoadingDrawable;
import com.yline.fresco.sample.R;
import com.yline.fresco.view.FrescoView;
import com.yline.test.UrlConstant;

public class SampleSingleActivity extends BaseAppCompatActivity {
    private static final String TAG = "xxx-";

    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, SampleSingleActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_single);

        AutoRotateDrawable drawable;

        FrescoView frescoView = (FrescoView) findViewById(R.id.fresco_view_single);

        final LoadingDrawable loadingDrawable = new LoadingDrawable(new LevelLoadingRenderer.Builder(this).build());

        PipelineDraweeControllerBuilder controllerBuilder = Fresco.newDraweeControllerBuilder();
        controllerBuilder.setOldController(frescoView.getController());
        controllerBuilder.setImageRequest(ImageRequest.fromUri(UrlConstant.getJpn_1920_1280()));
        controllerBuilder.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onSubmit(String id, Object callerContext) {
                super.onSubmit(id, callerContext);

                if (null != loadingDrawable) {
                    loadingDrawable.start();
                }
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
                if (null != loadingDrawable) {
                    loadingDrawable.stop();
                }
            }

            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (null != loadingDrawable) {
                    loadingDrawable.stop();
                }
            }
        });

        GenericDraweeHierarchy genericDraweeHierarchy = frescoView.getHierarchy();
        genericDraweeHierarchy.setProgressBarImage(loadingDrawable, ScalingUtils.ScaleType.CENTER_INSIDE);

        DraweeController controller = controllerBuilder.build();
        controller.setHierarchy(genericDraweeHierarchy);


        frescoView.setController(controller);
    }
}
