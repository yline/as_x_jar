package com.yline.extension;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.test.R;
import com.yline.base.BaseActivity;
import com.yline.extension.view.CircleView;
import com.yline.view.PageView;

import java.util.Arrays;

public class ExtensionCircleActivity extends BaseActivity {

    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, ExtensionCircleActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extension_circle);

        CircleView circleView = findViewById(R.id.extension_circle_sub_sampling);
        circleView.setImage(ImageSource.asset("squirrel.jpg"));

        PageView pageView = findViewById(R.id.extension_circle_page);
        pageView.setDataList(Arrays.asList(
                new PageView.PageModel("Overlaid circle", "A slightly more advanced example, this shows a circle that will move and scale with the image. " +
                        "(Due to a limitation in Android, this circle may disappear when it\\'s larger than 2048px.)")), true);

    }
}
