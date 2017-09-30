package com.yline.fresco.activity.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.yline.base.BaseAppCompatActivity;
import com.yline.fresco.activity.IApplication;
import com.yline.fresco.common.FrescoUtil;
import com.yline.fresco.sample.R;
import com.yline.fresco.view.FrescoView;
import com.yline.test.UrlConstant;

public class DebugActivity extends BaseAppCompatActivity {

    private TextView tvHint;

    private FrescoView frescoView;

    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, DebugActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        frescoView = (FrescoView) findViewById(R.id.debug_fresco_view);
        tvHint = (TextView) findViewById(R.id.debug_tv);
    }

    public void onJpgClick(View view) {
        clearCache();

        String imageStr = UrlConstant.getPng_1024_1024();
        showFrescoView(imageStr);
    }

    public void onPngClick(View view) {
        clearCache();

        String imageStr = UrlConstant.getJpg_640_1120();
        showFrescoView(imageStr);
    }

    public void onFailedClick(View view) {
        clearCache();

        String imageStr = IApplication.BenDiUrl;
        showFrescoView(imageStr);
    }

    private void showFrescoView(String imageUriString) {
        Uri imageUri = Uri.parse(imageUriString);

        // frescoView.setImageURI(imageUriString); // if user this , instead of next code; all is ok

        /* ------------------- Start ---------------- */
        ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(imageUri);
        imageRequestBuilder.setProgressiveRenderingEnabled(true);
        ImageRequest imageRequest = imageRequestBuilder.build();

        PipelineDraweeControllerBuilder controllerBuilder = Fresco.newDraweeControllerBuilder();
        controllerBuilder.setImageRequest(imageRequest);
        DraweeController controller = controllerBuilder.build();
        frescoView.setController(controller);
        /*------------------- End ------------------*/

        tvHint.setText(imageUriString);
    }

    private void clearCache() {
        FrescoUtil.clearCaches();
    }
}
