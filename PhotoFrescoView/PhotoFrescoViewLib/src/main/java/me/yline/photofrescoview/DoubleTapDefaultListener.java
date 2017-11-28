package me.yline.photofrescoview;

import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.DraweeView;

/**
 * 双击 默认效果
 *
 * @author yline 2017/11/28 -- 10:09
 * @version 1.0.0
 */
public class DoubleTapDefaultListener implements GestureDetector.OnDoubleTapListener {
    private final PhotoAttach mPhotoAttach;

    public DoubleTapDefaultListener(PhotoAttach attach) {
        mPhotoAttach = attach;
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

            // min, mid, max
            if (scale < mPhotoAttach.getMediumScale()) {
                mPhotoAttach.setScale(mPhotoAttach.getMediumScale(), x, y, true);
            } else if (scale >= mPhotoAttach.getMediumScale() && scale < mPhotoAttach.getMaximumScale()) {
                mPhotoAttach.setScale(mPhotoAttach.getMaximumScale(), x, y, true);
            } else {
                mPhotoAttach.setScale(mPhotoAttach.getMinimumScale(), x, y, true);
            }
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
