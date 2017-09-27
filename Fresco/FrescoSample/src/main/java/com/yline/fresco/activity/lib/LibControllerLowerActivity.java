package com.yline.fresco.activity.lib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.yline.base.BaseAppCompatActivity;
import com.yline.fresco.FrescoManager;
import com.yline.fresco.activity.IApplication;
import com.yline.fresco.common.FrescoUtil;
import com.yline.fresco.sample.R;
import com.yline.fresco.view.FrescoView;
import com.yline.test.UrlConstant;

public class LibControllerLowerActivity extends BaseAppCompatActivity {
    private static final int Duration = 6_000;

    private FrescoView frescoView;

    private TextView tvHint;

    private String uriStr;

    private String uriStrLower;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    uriStrLower = UrlConstant.getJpg_300_150();
                    uriStr = UrlConstant.getJpg_1366_768();
                    break;
                case 1:
                    FrescoUtil.removeCaches(Uri.parse(uriStrLower));
                    FrescoUtil.removeCaches(Uri.parse(uriStr));
                    uriStrLower = UrlConstant.getPng_64_64();
                    uriStr = UrlConstant.getPng_1024_1024();
                    break;
                case 2:
                    FrescoUtil.removeCaches(Uri.parse(uriStrLower));
                    FrescoUtil.removeCaches(Uri.parse(uriStr));
                    uriStrLower = UrlConstant.getPng_64_64();
                    uriStr = IApplication.BenDiUrl;
                    Log.i("xxx-", "handleMessage: uriStr = " + uriStr);
                    break;
                case 3:
                    FrescoUtil.removeCaches(Uri.parse(uriStrLower));
                    FrescoUtil.removeCaches(Uri.parse(uriStr));
                    uriStrLower = UrlConstant.getPng_64_64();
                    uriStr = UrlConstant.getWebp_Static();
                    Log.i("xxx-", "handleMessage: stop");
                    break;
            }

            FrescoManager.setImageUri(frescoView, uriStrLower, uriStr);
            tvHint.setText(uriStrLower + "\n\n" + uriStr);

            if (msg.what < 3) {
                sendEmptyMessageDelayed(msg.what + 1, Duration);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib_controller_lower);

        frescoView = (FrescoView) findViewById(R.id.lib_controller_lower_fresco_view);
        tvHint = (TextView) findViewById(R.id.lib_controller_lower_tv_hint);

        mHandler.sendEmptyMessage(0);
    }

    public static void launcher(Context context)
    {
    	if (null != context)
    	{
    		Intent intent = new Intent(context, LibControllerLowerActivity.class);
    		if (!(context instanceof Activity))
    		{
    			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		}
    		context.startActivity(intent);
    	}
    }
}
