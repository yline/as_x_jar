package com.yline.fresco.activity.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.yline.fresco.FrescoManager;
import com.yline.fresco.sample.R;
import com.yline.log.LogFileUtil;

public class LocalImageActivity extends AppCompatActivity {

    public static void launcher(Context context,String path)
    {
    	if (null != context)
    	{
    		Intent intent = new Intent(context, LocalImageActivity.class);
            intent.putExtra("httpUrl", path);
    		if (!(context instanceof Activity))
    		{
    			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		}
    		context.startActivity(intent);
    	}
    }

    private ImageView imageView;
    private String httpUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_image);

        imageView = (ImageView) findViewById(R.id.local_image);
        httpUrl = getIntent().getStringExtra("httpUrl");
    }

    public void onShowClick(View view) {
        String path = FrescoManager.getCacheFilePath(httpUrl);
        LogFileUtil.v("httpUrl = " + httpUrl + ", path = " + path);

        Bitmap bitmap = BitmapFactory.decodeFile(path);
        LogFileUtil.v("bitmap = " + bitmap);
        imageView.setImageBitmap(bitmap);
    }
}
