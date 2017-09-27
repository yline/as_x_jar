package com.yline.fresco.activity.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.imagepipeline.image.CloseableImage;
import com.yline.base.BaseAppCompatActivity;
import com.yline.fresco.FrescoManager;
import com.yline.fresco.common.FrescoCallback;
import com.yline.fresco.common.FrescoUtil;
import com.yline.fresco.sample.R;
import com.yline.fresco.view.FrescoView;
import com.yline.test.UrlConstant;

public class SamplePrefetchActivity extends BaseAppCompatActivity {
    private static final String TAG = "xxx-fetch";

    private Uri uri;

    private FrescoView frescoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_prefetch);

         frescoView = (FrescoView) findViewById(R.id.prefetch_fresco_view);
        TextView tvHint = (TextView) findViewById(R.id.prefetch_tv_hint);

        String uriStr = UrlConstant.getJpg_640_960();
        uri = Uri.parse(uriStr);

        FrescoManager.fetchDecodedImageUi(frescoView, uriStr, new FrescoCallback.OnSimpleFetchCallback() {
            @Override
            public void onFailure(DataSource<CloseableReference<CloseableImage>> dataSource) {
                String content = "onFailure: cause = " + dataSource.getFailureCause() + ", result = " + dataSource.getResult() + ", progress = " + dataSource.getProgress();
                Log.i(TAG, content);

                Toast.makeText(SamplePrefetchActivity.this, content, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Bitmap bitmap) {
                String content = "onSuccess: width = " + bitmap.getWidth() + ", height = " + bitmap.getHeight() + ", density = " + bitmap.getDensity();
                Log.i(TAG, content);

                Toast.makeText(SamplePrefetchActivity.this, content, Toast.LENGTH_SHORT).show();

                frescoView.setImageBitmap(bitmap); // 哈哈，没有回收
            }
        });

        tvHint.setText(uriStr);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        FrescoUtil.removeCaches(uri);
    }

    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, SamplePrefetchActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }
}
