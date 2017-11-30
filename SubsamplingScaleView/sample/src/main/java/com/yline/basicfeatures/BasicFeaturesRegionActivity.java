package com.yline.basicfeatures;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.davemorrissey.labs.subscaleview.test.R;
import com.yline.base.BaseActivity;
import com.yline.view.PageView;

import java.util.Arrays;

public class BasicFeaturesRegionActivity extends BaseActivity {
    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, BasicFeaturesRegionActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_view);

        final SubsamplingScaleImageView scaleImageView = findViewById(R.id.scale_view_sub_sampling);
        scaleImageView.setOrientation(SubsamplingScaleImageView.ORIENTATION_90);
//        scaleImageView.setImage(ImageSource.asset("card.png").region(new Rect(0, 0, 3778, 2834)));
        scaleImageView.setImage(ImageSource.asset("eagle.jpg"));

        PageView pageView = findViewById(R.id.scale_view_page);
        pageView.setDataList(Arrays.asList(
                new PageView.PageModel("旋转", "点击旋转按钮，实现旋转功能；本地文件直接支持 EXIF 旋转")), true);
        pageView.setOnRotateClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scaleImageView.setOrientation((scaleImageView.getOrientation() + 90) % 360);
            }
        });
    }

}
