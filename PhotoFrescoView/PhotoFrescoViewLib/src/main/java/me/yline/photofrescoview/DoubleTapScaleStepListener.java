package me.yline.photofrescoview;

import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.DraweeView;

/**
 * 阶梯型 双击效果
 *
 * @author yline 2017/11/28 -- 10:16
 * @version 1.0.0
 */
public class DoubleTapScaleStepListener implements GestureDetector.OnDoubleTapListener {
    private static float DEFAULT_SCALE_STEP = 1f;

    private final PhotoAttach mPhotoAttach;
    private final float mScaleStep;

    public DoubleTapScaleStepListener(PhotoAttach attacher, float scaleStep) {
        this.mPhotoAttach = attacher;

        if (scaleStep >= 0) {
            mScaleStep = scaleStep;
        } else {
            mScaleStep = DEFAULT_SCALE_STEP;
        }
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        if (mPhotoAttach == null) {
            return false;
        }

        DraweeView<GenericDraweeHierarchy> draweeView = mPhotoAttach.getDraweeView();
        if (draweeView == null) {
            return false;
        }

        // 图片，点击
        if (mPhotoAttach.getOnPhotoTapListener() != null) {
            final RectF displayRect = mPhotoAttach.getDisplayRect();

            if (null != displayRect) {
                final float x = e.getX(), y = e.getY();
                if (displayRect.contains(x, y)) {
                    float xResult = (x - displayRect.left) / displayRect.width();
                    float yResult = (y - displayRect.top) / displayRect.height();
                    mPhotoAttach.getOnPhotoTapListener().onPhotoTap(draweeView, xResult, yResult);
                    return true;
                }
            }
        }

        // 空白处，点击
        if (mPhotoAttach.getOnViewTapListener() != null) {
            mPhotoAttach.getOnViewTapListener().onViewTap(draweeView, e.getX(), e.getY());
            return true;
        }

        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        if (mPhotoAttach == null) {
            return false;
        }

        try {
            float scale = mPhotoAttach.getScale();
            float x = event.getX();
            float y = event.getY();

            // min, step, max
            float newScale = scale;
            if (scale < mPhotoAttach.getMaximumScale()) {
                newScale += mScaleStep;
                if (newScale > mPhotoAttach.getMaximumScale()) {
                    newScale = mPhotoAttach.getMaximumScale();
                }
            } else {
                newScale = mPhotoAttach.getMinimumScale();
            }

            mPhotoAttach.setScale(newScale, x, y, true);
        } catch (Exception e) {
            e.printStackTrace();
            // Can sometimes happen when getX() and getY() is called
        }
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        return false;
    }
}
