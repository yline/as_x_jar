package com.yline.viewpager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.davemorrissey.labs.subscaleview.test.R;
import com.yline.base.BaseActivity;
import com.yline.view.PageView;
import com.yline.view.recycler.adapter.AbstractPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewPagerActivity extends BaseActivity {

    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, ViewPagerActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        ViewPager viewPager = findViewById(R.id.view_pager);
        AbstractPagerAdapter viewPagerAdapter = new AbstractPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);

        SubsamplingScaleImageView imageViewA = (SubsamplingScaleImageView) LayoutInflater.from(this).inflate(R.layout.item_scale_view, null);
        imageViewA.setImage(ImageSource.asset("eagle.jpg"));
        SubsamplingScaleImageView imageViewB = (SubsamplingScaleImageView) LayoutInflater.from(this).inflate(R.layout.item_scale_view, null);
        imageViewB.setImage(ImageSource.asset("pony.jpg"));

        List<View> adapterDataList = new ArrayList<>();
        adapterDataList.add(imageViewA);
        adapterDataList.add(imageViewB);
        viewPagerAdapter.setViews(adapterDataList, true);

        PageView pageView = findViewById(R.id.view_pager_page);
        pageView.setDataList(Arrays.asList(
                new PageView.PageModel("Horizontal", "This gallery has two images in a ViewPager. Swipe to move to the next image. If you\\'re zoomed in on an image, you need to pan to the right of it, then swipe again to activate the pager."),
                new PageView.PageModel("Vertical", "Vertical view pagers are also supported. Swipe up to move to the next image. If you\\'re zoomed in on an image, you need to pan to the bottom of it, then swipe again to activate the pager.")), true);
    }
}
