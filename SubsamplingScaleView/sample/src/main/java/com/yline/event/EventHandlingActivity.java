package com.yline.event;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.davemorrissey.labs.subscaleview.test.R;
import com.yline.base.BaseActivity;
import com.yline.view.PageView;

import java.util.Arrays;

public class EventHandlingActivity extends BaseActivity {
    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, EventHandlingActivity.class);
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
        scaleImageView.setImage(ImageSource.asset("pony.jpg"));
        scaleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        scaleImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(v.getContext(), "Long Clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        PageView pageView = findViewById(R.id.scale_view_page);
        pageView.setDataList(Arrays.asList(
                new PageView.PageModel("OnClickListener", "单击"),
                new PageView.PageModel("OnLongClickListener", "长按")), true);
    }
}
