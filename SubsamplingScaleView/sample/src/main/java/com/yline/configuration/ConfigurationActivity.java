package com.yline.configuration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.davemorrissey.labs.subscaleview.test.R;
import com.yline.base.BaseActivity;
import com.yline.view.PageView;

import java.util.Arrays;

public class ConfigurationActivity extends BaseActivity implements PageView.OnPageChangeClickListener {

    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, ConfigurationActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private SubsamplingScaleImageView scaleImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_view);

        scaleImageView = findViewById(R.id.scale_view_sub_sampling);
        scaleImageView.setImage(ImageSource.asset("eagle.jpg"));

        PageView pageView = findViewById(R.id.scale_view_page);
        pageView.setDataList(Arrays.asList(
                new PageView.PageModel("Maximum scale", "The maximum scale has been set to 50dpi. You can zoom in until the image is very pixellated."),
                new PageView.PageModel("Minimum tile DPI", "The minimum tile DPI has been set to 50dpi, to reduce memory usage. The next layer of tiles will not be loaded until the image is very pixellated."),
                new PageView.PageModel("Pan disabled", "Dragging has been disabled. You can only zoom in to the centre point."),
                new PageView.PageModel("Zoom disabled", "Zooming has been disabled. You can drag the image around."),
                new PageView.PageModel("Double tap style", "On double tap, the tapped point is now zoomed to the center of the screen instead of remaining in the same place."),
                new PageView.PageModel("Double tap style", "On double tap, the zoom now happens immediately."),
                new PageView.PageModel("Double tap scale", "The double tap zoom scale has been set to 240dpi."),
                new PageView.PageModel("Pan limit center", "The pan limit has been changed to PAN_LIMIT_CENTER. Panning stops when a corner reaches the centre of the screen."),
                new PageView.PageModel("Pan limit outside", "The pan limit has been changed to PAN_LIMIT_OUTSIDE. Panning stops when the image is just off screen."),
                new PageView.PageModel("Debug", "Debug has been enabled. This shows the tile boundaries and sizes.")), true);
        pageView.setOnPageChangeClickListener(this);
    }

    @Override
    public void onPageChange(int page) {
        if (page == 0) {
            scaleImageView.setMinimumDpi(50);
        } else {
            scaleImageView.setMaxScale(2F);
        }

        if (page == 1) {
            scaleImageView.setMinimumTileDpi(50);
        } else {
            scaleImageView.setMinimumTileDpi(500);
        }

        if (page == 2) {
            scaleImageView.setScaleAndCenter(0f, new PointF(2456, 1632));
            scaleImageView.setPanEnabled(false);
        } else {
            scaleImageView.setPanEnabled(true);
        }

        if (page == 3) {
            scaleImageView.setScaleAndCenter(1f, new PointF(2456, 1632));
            scaleImageView.setZoomEnabled(false);
        } else {
            scaleImageView.setZoomEnabled(true);
        }

        if (page == 4) {
            scaleImageView.setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
        } else if (page == 5) {
            scaleImageView.setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER_IMMEDIATE);
        } else {
            scaleImageView.setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_FIXED);
        }

        if (page == 6) {
            scaleImageView.setDoubleTapZoomDpi(240);
        } else {
            scaleImageView.setDoubleTapZoomScale(1F);
        }

        if (page == 7) {
            scaleImageView.setPanLimit(SubsamplingScaleImageView.PAN_LIMIT_CENTER);
        } else if (page == 8) {
            scaleImageView.setPanLimit(SubsamplingScaleImageView.PAN_LIMIT_OUTSIDE);
        } else {
            scaleImageView.setPanLimit(SubsamplingScaleImageView.PAN_LIMIT_INSIDE);
        }

        if (page == 9) {
            scaleImageView.setDebug(true);
        } else {
            scaleImageView.setDebug(false);
        }
    }
}
