package me.yline.photofrescoview;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

/**
 * 手指比例，检测器
 *
 * @author yline 2017/11/27 -- 19:54
 * @version 1.0.0
 */
public class PhotoScaleDetector {
    private static final int INVALID_POINTER_ID = -1;

    private final float mTouchSlop;
    private final float mMinimumVelocity;
    private final ScaleGestureDetector mScaleGestureDetector;
    private final OnPhotoScaleGestureListener mPhotoScaleListener;

    private VelocityTracker mVelocityTracker;
    private boolean mIsDragging;
    private float mLastTouchX;
    private float mLastTouchY;
    private int mActivePointerId = INVALID_POINTER_ID;
    private int mActivePointerIndex = 0;

    public PhotoScaleDetector(Context context, OnPhotoScaleGestureListener scaleDragGestureListener) {
        mPhotoScaleListener = scaleDragGestureListener;

        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mTouchSlop = configuration.getScaledTouchSlop();

        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float scaleFactor = detector.getScaleFactor();

                if (Float.isNaN(scaleFactor) || Float.isInfinite(scaleFactor)) {
                    return false;
                }

                mPhotoScaleListener.onScale(scaleFactor, detector.getFocusX(), detector.getFocusY());
                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                mPhotoScaleListener.onScaleEnd();
            }
        });
    }

    public boolean isScaling() {
        return mScaleGestureDetector.isInProgress();
    }

    public boolean isDragging() {
        return mIsDragging;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        mScaleGestureDetector.onTouchEvent(ev);

        final int action = MotionEventCompat.getActionMasked(ev);
        onTouchActivePointer(action, ev);
        onTouchDragEvent(action, ev);
        return true;
    }

    private float getActiveX(MotionEvent ev) {
        try {
            return ev.getX(mActivePointerIndex);
        } catch (Exception e) {
            return ev.getX();
        }
    }

    private float getActiveY(MotionEvent ev) {
        try {
            return ev.getY(mActivePointerIndex);
        } catch (Exception e) {
            return ev.getY();
        }
    }

    private void onTouchActivePointer(int action, MotionEvent ev) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (ev.getPointerCount() > 0) {
                    mActivePointerId = ev.getPointerId(0);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mActivePointerId = INVALID_POINTER_ID;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex); // ev.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = (pointerIndex == 0) ? 1 : 0;
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                    mLastTouchX = ev.getX(newPointerIndex);
                    mLastTouchY = ev.getY(newPointerIndex);
                }
                break;
            default:
                break;
        }

        mActivePointerIndex = ev.findPointerIndex((mActivePointerId != INVALID_POINTER_ID) ? mActivePointerId : 0);
    }

    private void onTouchDragEvent(int action, MotionEvent ev) {
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mVelocityTracker = VelocityTracker.obtain();
                if (null != mVelocityTracker) {
                    mVelocityTracker.addMovement(ev);
                }

                mLastTouchX = getActiveX(ev);
                mLastTouchY = getActiveY(ev);
                mIsDragging = false;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final float x = getActiveX(ev);
                final float y = getActiveY(ev);
                final float dx = x - mLastTouchX, dy = y - mLastTouchY;

                if (!mIsDragging) {
                    mIsDragging = Math.sqrt((dx * dx) + (dy * dy)) >= mTouchSlop;
                }

                if (mIsDragging) {
                    mPhotoScaleListener.onScaleDrag(dx, dy);
                    mLastTouchX = x;
                    mLastTouchY = y;

                    if (null != mVelocityTracker) {
                        mVelocityTracker.addMovement(ev);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                if (null != mVelocityTracker) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (mIsDragging) {
                    if (null != mVelocityTracker) {
                        mLastTouchX = getActiveX(ev);
                        mLastTouchY = getActiveY(ev);

                        mVelocityTracker.addMovement(ev);
                        mVelocityTracker.computeCurrentVelocity(1000);

                        final float vX = mVelocityTracker.getXVelocity(), vY = mVelocityTracker.getYVelocity();

                        if (Math.max(Math.abs(vX), Math.abs(vY)) >= mMinimumVelocity) {
                            mPhotoScaleListener.onScaleFling(mLastTouchX, mLastTouchY, -vX, -vY);
                        }
                    }
                }
                if (null != mVelocityTracker) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
            }
            default:
                break;
        }
    }

    /**
     * PhotoView 手指 比例 探测器
     */
    public interface OnPhotoScaleGestureListener {
        /**
         * 手指，拖拽，图片； MotionEvent.ACTION_MOVE
         *
         * @param dx x方向偏移量
         * @param dy y方向偏移量
         */
        void onScaleDrag(float dx, float dy);

        /**
         * 手指抬起的位置；MotionEvent.ACTION_UP
         *
         * @param startX    抬起的x方向位置
         * @param startY    抬起的y方向位置
         * @param velocityX 抬起的x方向速率
         * @param velocityY 抬起的y方向速率
         */
        void onScaleFling(float startX, float startY, float velocityX, float velocityY);

        /**
         * 手指滚动时
         *
         * @param scaleFactor 探测器的比例
         * @param focusX      探测器x方向位置
         * @param focusY      探测器y方向位置
         */
        void onScale(float scaleFactor, float focusX, float focusY);

        /**
         * 手指滚动结束
         */
        void onScaleEnd();
    }
}
