package org.image;

import org.base.BaseActivity;
import org.xutils.x;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import f21.xutilsexample.R;

@ContentView(R.layout.activity_imagebig)
public class BigImageActivity extends BaseActivity {

    @ViewInject(R.id.iv_big_img)
    private ImageView iv_big_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageOptions imageOptions = new ImageOptions.Builder()
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                // 默认自动适应大小
                // .setSize(...)
                .setIgnoreGif(false)
                .setImageScaleType(ImageView.ScaleType.CENTER)
                .build();

        x.image().bind(iv_big_img, getIntent().getStringExtra("url"), imageOptions);
    }
    
    public static void actionStart(Context context,String msg){
    	Intent intent = new Intent(context, BigImageActivity.class);
    	intent.putExtra("url", msg);
    	context.startActivity(intent);
    }
    
}
