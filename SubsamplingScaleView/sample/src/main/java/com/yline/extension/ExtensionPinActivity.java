package com.yline.extension;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.davemorrissey.labs.subscaleview.test.R;
import com.yline.extension.view.PinView;
import com.yline.base.BaseActivity;
import com.yline.view.PageView;

import java.util.Arrays;
import java.util.Random;

import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.EASE_OUT_QUAD;
import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.PAN_LIMIT_CENTER;
import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.PAN_LIMIT_INSIDE;

public class ExtensionPinActivity extends BaseActivity implements PageView.OnPageChangeClickListener {
    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, ExtensionPinActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private PinView pinView;
    private int mPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extension_pin);

        pinView = findViewById(R.id.extension_pin_sub_sampling);
        pinView.setImage(ImageSource.asset("squirrel.jpg"));

        mPage = 0;

        PageView pageView = findViewById(R.id.extension_pin_page);
        pageView.setDataList(Arrays.asList(
                new PageView.PageModel("A demo", "Tap the play button. The image will scale and zoom to a random point, shown by a marker."),
                new PageView.PageModel("Limited pan", "If the target point is near the edge of the image, it will be moved as near to the center as possible."),
                new PageView.PageModel("Unlimited pan", "With unlimited or center-limited pan, the target point can always be animated to the center."),
                new PageView.PageModel("Customisation", "Duration and easing are configurable. You can also make animations non-interruptible.")), true);
        pageView.setOnPageChangeClickListener(this);
        pageView.setOnPlayClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
    }

    private void play() {
        Random random = new Random();
        if (pinView.isReady()) {
            float maxScale = pinView.getMaxScale();
            float minScale = pinView.getMinScale();
            float scale = (random.nextFloat() * (maxScale - minScale)) + minScale;
            PointF center = new PointF(random.nextInt(pinView.getSWidth()), random.nextInt(pinView.getSHeight()));
            pinView.setPin(center);

            SubsamplingScaleImageView.AnimationBuilder animationBuilder = pinView.animateScaleAndCenter(scale, center);
            if (mPage == 3) {
                animationBuilder.withDuration(2000).withEasing(EASE_OUT_QUAD).withInterruptible(false).start();
            } else {
                animationBuilder.withDuration(750).start();
            }
        }
    }

    @Override
    public void onPageChange(int page) {
        mPage = page;

        if (page == 2) {
            pinView.setPanLimit(PAN_LIMIT_CENTER);
        } else {
            pinView.setPanLimit(PAN_LIMIT_INSIDE);
        }
    }
}
