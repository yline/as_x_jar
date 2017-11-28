package com.yline.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.yline.base.BaseActivity;
import com.yline.photofrescoview.sample.R;
import com.yline.test.UrlConstant;

import me.yline.photofrescoview.DoubleTapScaleStepListener;
import me.yline.photofrescoview.PhotoAttach;
import me.yline.photofrescoview.PhotoFrescoView;

public class SingleActivity extends BaseActivity {

    private PhotoFrescoView mPhotoDraweeView;

    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, SingleActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        mPhotoDraweeView = (PhotoFrescoView) findViewById(R.id.photo_drawee_view);
        mPhotoDraweeView.setOnPhotoTapListener(new PhotoAttach.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                Toast.makeText(view.getContext(), "onPhotoTap :  x =  " + x + ";" + " y = " + y, Toast.LENGTH_SHORT).show();
            }
        });
        mPhotoDraweeView.setOnViewTapListener(new PhotoAttach.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                Toast.makeText(view.getContext(), "onViewTap", Toast.LENGTH_SHORT).show();
            }
        });
        mPhotoDraweeView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(v.getContext(), "onLongClick", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        mPhotoDraweeView.setOnDoubleTapListener(new DoubleTapScaleStepListener(mPhotoDraweeView.getPhotoAttach(), 0.25f));

        mPhotoDraweeView.setPhotoUri(UrlConstant.getJpg_640_960());
    }
}
