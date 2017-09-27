package com.yline.fresco.activity.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.yline.base.BaseAppCompatActivity;
import com.yline.fresco.common.FrescoUtil;
import com.yline.fresco.sample.R;
import com.yline.fresco.view.FrescoView;
import com.yline.test.UrlConstant;

/**
 * Fresco 有bug；BenDi的图片，显示不了
 * @author yline 2017/9/27 -- 10:10
 * @version 1.0.0
 */
public class SampleProgressiveLoadingActivity extends BaseAppCompatActivity {
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_progressive_loading);

        FrescoView frescoView = (FrescoView) findViewById(R.id.progressive_fresco_view);
        TextView tvHint = (TextView) findViewById(R.id.progressive_tv_hint);

        // http://img.benditoutiao.com/material/20170914/081cf6c0-98e8-11e7-aecc-4fb4862aa761.png@!news-list-single-pic
        // failure uri
        String uriStr = UrlConstant.getJpg_640_1120();
        uri = Uri.parse(uriStr);

        ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(uri);
        imageRequestBuilder.setProgressiveRenderingEnabled(true);
        ImageRequest imageRequest = imageRequestBuilder.build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(frescoView.getController())
                .setImageRequest(imageRequest)
                .build();

        frescoView.setController(controller);

        // frescoView.setImageURI(uriStr);

        tvHint.setText(uriStr);
    }

    @Override
    protected void onDestroy() {
        FrescoUtil.removeCaches(uri);

        super.onDestroy();
    }

    public static void launcher(Context context)
    {
    	if (null != context)
    	{
    		Intent intent = new Intent(context, SampleProgressiveLoadingActivity.class);
    		if (!(context instanceof Activity))
    		{
    			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		}
    		context.startActivity(intent);
    	}
    }
}
