package com.yline.event;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.davemorrissey.labs.subscaleview.test.R;
import com.yline.base.BaseActivity;
import com.yline.view.PageView;

import java.util.Arrays;

public class EventHandlingAdvancedActivity extends BaseActivity {
    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, EventHandlingAdvancedActivity.class);
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
        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (scaleImageView.isReady()) {
                    PointF sCoord = scaleImageView.viewToSourceCoord(e.getX(), e.getY());
                    Toast.makeText(getApplicationContext(), "Single tap: " + ((int) sCoord.x) + ", " + ((int) sCoord.y), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Single tap: Image not ready", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                if (scaleImageView.isReady()) {
                    PointF sCoord = scaleImageView.viewToSourceCoord(e.getX(), e.getY());
                    Toast.makeText(getApplicationContext(), "Long press: " + ((int) sCoord.x) + ", " + ((int) sCoord.y), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Long press: Image not ready", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (scaleImageView.isReady()) {
                    PointF sCoord = scaleImageView.viewToSourceCoord(e.getX(), e.getY());
                    Toast.makeText(getApplicationContext(), "Double tap: " + ((int) sCoord.x) + ", " + ((int) sCoord.y), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Double tap: Image not ready", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        scaleImageView.setImage(ImageSource.asset("squirrel.jpg"));
        scaleImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });

        PageView pageView = findViewById(R.id.scale_view_page);
        pageView.setDataList(Arrays.asList(
                new PageView.PageModel("Overriding gestures", "Some gestures can be overridden with your own GestureDetector without affecting the image view. This allows you to get the coordinates of the event."),
                new PageView.PageModel("onSingleTapConfirmed", "onSingleTapConfirmed has been overridden. Tap the image to see coordinates."),
                new PageView.PageModel("onDoubleTap", "onDoubleTap has been overridden. Tap the image to see coordinates. This overrides the default zoom in behaviour."),
                new PageView.PageModel("onLongPress", "onLongPress has been overridden. Press and hold the image to see coordinates."),
                new PageView.PageModel("Other events", "You can override any event you want, but customising swipe, fling and zoom gestures will stop the view working normally.")), true);
    }
}
