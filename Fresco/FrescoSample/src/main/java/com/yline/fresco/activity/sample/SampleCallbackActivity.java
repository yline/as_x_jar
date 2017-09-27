package com.yline.fresco.activity.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;
import com.yline.base.BaseAppCompatActivity;
import com.yline.fresco.activity.IApplication;
import com.yline.fresco.common.FrescoUtil;
import com.yline.fresco.sample.R;
import com.yline.fresco.view.FrescoView;

import java.util.Locale;

public class SampleCallbackActivity extends BaseAppCompatActivity {
    private static final String TAG = "xxx-Callback";

    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_callback);

        FrescoView frescoView = (FrescoView) findViewById(R.id.callback_fresco_view);
        TextView tvHint = (TextView) findViewById(R.id.callback_tv_hint);

        String uriStr = IApplication.BenDiUrl; // UrlConstant.getJpg_640_960();
        uri = Uri.parse(uriStr);

        PipelineDraweeControllerBuilder controllerBuilder = Fresco.newDraweeControllerBuilder();
        controllerBuilder.setOldController(frescoView.getController());
        controllerBuilder.setTapToRetryEnabled(true);
        controllerBuilder.setUri(uriStr);
        controllerBuilder.setControllerListener(new BaseControllerListener<ImageInfo>(){
            @Override
            public void onSubmit(String id, Object callerContext) {
                super.onSubmit(id, callerContext);
                Log.i(TAG, "onSubmit: id = " + id + ", context = " + callerContext);
            }

            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                Log.i(TAG, "onFinalImageSet: id = " + id + ", imageInfo = " + imageInfo + ", animatable = " + animatable);
                if (null != imageInfo){
                    String content = String.format(Locale.CHINA, "Final image received! Size %d x %d Quality level %d, good enough: %s, full quality: %s",
                            imageInfo.getWidth(), imageInfo.getHeight(),
                            imageInfo.getQualityInfo().getQuality(), imageInfo.getQualityInfo().isOfGoodEnoughQuality(), imageInfo.getQualityInfo().isOfFullQuality());
                    Log.i(TAG, content);
                }
            }

            @Override
            public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
                super.onIntermediateImageSet(id, imageInfo);
                Log.i(TAG, "onIntermediateImageSet: id = " + id + ", imageInfo = " + imageInfo);
            }

            @Override
            public void onIntermediateImageFailed(String id, Throwable throwable) {
                super.onIntermediateImageFailed(id, throwable);
                Log.i(TAG, "onIntermediateImageFailed: id = " + id + ", throwable = " + throwable);
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
                Log.i(TAG, "onFailure: id = " + id + ", throwable = " + throwable);
            }

            @Override
            public void onRelease(String id) {
                super.onRelease(id);
                Log.i(TAG, "onRelease: id = " + id);
            }
        });

        frescoView.setController(controllerBuilder.build());

        tvHint.setText(uriStr);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        FrescoUtil.removeCaches(uri);
    }

    public static void launcher(Context context)
    {
    	if (null != context)
    	{
    		Intent intent = new Intent(context, SampleCallbackActivity.class);
    		if (!(context instanceof Activity))
    		{
    			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		}
    		context.startActivity(intent);
    	}
    }
}
