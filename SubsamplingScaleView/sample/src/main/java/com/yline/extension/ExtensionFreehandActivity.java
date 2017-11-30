package com.yline.extension;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.test.R;
import com.yline.base.BaseActivity;
import com.yline.extension.view.FreehandView;
import com.yline.view.PageView;

import java.util.Arrays;

public class ExtensionFreehandActivity extends BaseActivity {

    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, ExtensionFreehandActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extension_freehand);

        final FreehandView freehandView = findViewById(R.id.extension_freehand_sub_sampling);
        freehandView.setImage(ImageSource.asset("squirrel.jpg"));

        PageView pageView = findViewById(R.id.extension_freehand_page);
        pageView.setDataList(Arrays.asList(
                new PageView.PageModel("Freehand drawing", "This subclass adds event detection. Draw a freehand line; it will move with the image." +
                        " (Due to a limitation in Android, your drawing may disappear when it\\'s larger than 2048px.)")), true);
        pageView.setOnResetClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                freehandView.reset();
            }
        });

    }
}
