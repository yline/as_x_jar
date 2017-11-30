package com.yline.basicfeatures;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.davemorrissey.labs.subscaleview.test.R;
import com.yline.base.BaseActivity;
import com.yline.view.PageView;

import java.util.Arrays;

/**
 * 展示大图
 *
 * @author yline 2017/11/29 -- 15:09
 * @version 1.0.0
 */
public class BasicFeaturesLargeActivity extends BaseActivity {
    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, BasicFeaturesLargeActivity.class);
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

        SubsamplingScaleImageView scaleImageView = findViewById(R.id.scale_view_sub_sampling);
        scaleImageView.setImage(ImageSource.asset("card.png"));

        PageView pageView = findViewById(R.id.scale_view_page);
        pageView.setDataList(Arrays.asList(
                new PageView.PageModel("pinch to zoom", "两个手指同时操作实现缩放；缩放中心：手指之间中心位置"),
                new PageView.PageModel("quick scale", "双击某个位置，将以该位置进行快速缩放"),
                new PageView.PageModel("drag", "单手拖拽"),
                new PageView.PageModel("Fling", "单手快速拖拽，会有物理规律的滑动")), true);
    }

}
