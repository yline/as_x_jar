package com.yline.fresco.activity.lib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.facebook.imagepipeline.image.ImageInfo;
import com.yline.base.BaseAppCompatActivity;
import com.yline.fresco.FrescoManager;
import com.yline.fresco.activity.IApplication;
import com.yline.fresco.common.FrescoCallback;
import com.yline.fresco.sample.R;
import com.yline.fresco.view.FrescoView;
import com.yline.test.UrlConstant;

import java.util.Locale;

public class LibControllerUriActivity extends BaseAppCompatActivity {
    private static final String TAG = "xxx-ControllerUri";

    private static final int Duration = 5_000;

    private FrescoView frescoView;

    private TextView tvHint;

    private String uriStr;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    uriStr = UrlConstant.getJpg_640_960();
                    break;
                case 1:
                    uriStr = UrlConstant.getPng_420_300();
                    break;
                case 2:
                    uriStr = IApplication.BenDiUrl;
                    break;
                case 3:
                    uriStr = UrlConstant.getWebp_Static();
                    break;
                case 4:
                    uriStr = UrlConstant.getGif();
                    break;
                case 5:
                    uriStr = UrlConstant.getWebp_Dynamic();
                    Log.i("xxx-", "handleMessage: stop");
                    break;
            }

            FrescoManager.setImageUri(frescoView, uriStr, true, simpleLoadCallback);
            tvHint.setText(uriStr);

            if (msg.what < 5) {
                sendEmptyMessageDelayed(msg.what + 1, Duration);
            }
        }
    };

    private FrescoCallback.OnSimpleLoadCallback simpleLoadCallback = new FrescoCallback.OnSimpleLoadCallback() {
        @Override
        public void onStart(String id, Object callerContext) {
            Log.i(TAG, "onStart: id = " + id + ", callerContext = " + callerContext);
        }

        @Override
        public void onFailure(String id, Throwable throwable) {
            Log.i(TAG, "onStart: id = " + id + ", throwable = " + throwable);
        }

        @Override
        public void onSuccess(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
            Log.i(TAG, "onStart: id = " + id + ", imageInfo = " + imageInfo + ", animatable = " + animatable);
            if (null != imageInfo){
                String content = String.format(Locale.CHINA, "Final image received! Size %d x %d Quality level %d, good enough: %s, full quality: %s",
                        imageInfo.getWidth(), imageInfo.getHeight(),
                        imageInfo.getQualityInfo().getQuality(), imageInfo.getQualityInfo().isOfGoodEnoughQuality(), imageInfo.getQualityInfo().isOfFullQuality());
                Log.i(TAG, content);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib_controller_uri);

        frescoView = (FrescoView) findViewById(R.id.lib_controller_uri_fresco_view);
        tvHint = (TextView) findViewById(R.id.lib_controller_uri_tv_hint);
        
        mHandler.sendEmptyMessage(0);
    }
    
    public static void launcher(Context context)
    {
    	if (null != context)
    	{
    		Intent intent = new Intent(context, LibControllerUriActivity.class);
    		if (!(context instanceof Activity))
    		{
    			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);	
    		}
    		context.startActivity(intent);
    	}
    }
}
