package com.yline.fresco.activity.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.yline.base.BaseAppCompatActivity;
import com.yline.fresco.sample.R;
import com.yline.fresco.view.FrescoView;
import com.yline.test.UrlConstant;

public class SampleBitmapActivity extends BaseAppCompatActivity {
    private static final String TAG = "xxx-Bitmap";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_bitmap);

        FrescoView frescoView = (FrescoView) findViewById(R.id.bitmap_fresco_view);
        TextView tvHint = (TextView) findViewById(R.id.bitmap_tv_hint);

        String uriStr = UrlConstant.getJpg_640_1120();
        Uri uri = Uri.parse(uriStr);

        ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(uri);
        imageRequestBuilder.setPostprocessor(new BasePostprocessor() {
            @Override
            public void process(Bitmap bitmap) {
                super.process(bitmap);

                Log.i(TAG, "process: Width = " + bitmap.getWidth() + ", height = " + bitmap.getHeight() + ", density = " + bitmap.getDensity());
            }
        });

        PipelineDraweeControllerBuilder controllerBuilder = Fresco.newDraweeControllerBuilder();
        controllerBuilder.setOldController(frescoView.getController());
        controllerBuilder.setImageRequest(imageRequestBuilder.build());

        frescoView.setController(controllerBuilder.build()); // 必须展示，才会去调用下载bitmap

        tvHint.setText(uriStr);
    }
    
    public static void launcher(Context context)
    {
    	if (null != context)
    	{
    		Intent intent = new Intent(context, SampleBitmapActivity.class);
    		if (!(context instanceof Activity))
    		{
    			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);	
    		}
    		context.startActivity(intent);
    	}
    }
}
