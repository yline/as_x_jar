package me.yline.photofrescoview;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ScrollerCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.DraweeView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

/**
 * PhotoView 帮助类
 *
 * @author yline 2017/11/28 -- 9:18
 * @version 1.0.0
 */
public class PhotoAttach implements PhotoScaleDetector.OnPhotoScaleGestureListener {
    public final static float DEFAULT_MAX_SCALE = 3.5f;
    public final static float DEFAULT_MID_SCALE = 1.75f;
    public final static float DEFAULT_MIN_SCALE = 1.0f;
    public final static long ZOOM_DURATION = 200L;

    private static final int EDGE_NONE = -1;
    private static final int EDGE_LEFT = 0, EDGE_TOP = 0;
    private static final int EDGE_RIGHT = 1, EDGE_BOTTOM = 1;
    private static final int EDGE_BOTH = 2;

    @IntDef({HORIZONTAL, VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OrientationMode {
    }

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private int mOrientation = HORIZONTAL;

    private final float[] mMatrixValues = new float[9];
    private final RectF mDisplayRect = new RectF();
    private final Interpolator mZoomInterpolator = new AccelerateDecelerateInterpolator();

    private float mMinScale = PhotoAttach.DEFAULT_MIN_SCALE;
    private float mMidScale = PhotoAttach.DEFAULT_MID_SCALE;
    private float mMaxScale = PhotoAttach.DEFAULT_MAX_SCALE;
    private long mZoomDuration = PhotoAttach.ZOOM_DURATION;

    private PhotoScaleDetector mScaleDragDetector;
    private GestureDetectorCompat mGestureDetector;

    private boolean mBlockParentIntercept = false;
    private boolean mAllowParentInterceptOnEdge = true;
    private int mScrollEdgeX = EDGE_BOTH;
    private int mScrollEdgeY = EDGE_BOTH;

    private final Matrix mMatrix = new Matrix();
    private int mImageInfoHeight = -1, mImageInfoWidth = -1;
    private FlingRunnable mCurrentFlingRunnable;
    private WeakReference<DraweeView<GenericDraweeHierarchy>> mWeakDraweeView;

    private OnPhotoTapListener mPhotoTapListener;
    private OnViewTapListener mViewTapListener;
    private View.OnLongClickListener mLongClickListener;
    private OnScaleChangeListener mScaleChangeListener;

    public PhotoAttach(DraweeView<GenericDraweeHierarchy> draweeView) {
        mWeakDraweeView = new WeakReference<>(draweeView);

        draweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
        draweeView.setOnTouchListener(new OnPhotoTouchListener());

        initAttachListener(draweeView.getContext());
    }

    private void initAttachListener(Context context) {
        mScaleDragDetector = new PhotoScaleDetector(context, this);
        mGestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                if (null != mLongClickListener) {
                    mLongClickListener.onLongClick(getDraweeView());
                }
            }
        });
        mGestureDetector.setOnDoubleTapListener(new DoubleTapDefaultListener(this));
    }

    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener newOnDoubleTapListener) {
        if (newOnDoubleTapListener != null) {
            this.mGestureDetector.setOnDoubleTapListener(newOnDoubleTapListener);
        } else {
            this.mGestureDetector.setOnDoubleTapListener(new DoubleTapDefaultListener(this));
        }
    }

    public DraweeView<GenericDraweeHierarchy> getDraweeView() {
        return mWeakDraweeView.get();
    }

    public float getMinimumScale() {
        return mMinScale;
    }

    public float getMediumScale() {
        return mMidScale;
    }

    public float getMaximumScale() {
        return mMaxScale;
    }

    public float getScale() {
        return (float) Math.sqrt(
                (float) Math.pow(getMatrixValue(mMatrix, Matrix.MSCALE_X), 2) + (float) Math.pow(
                        getMatrixValue(mMatrix, Matrix.MSKEW_Y), 2));
    }

    public void setScale(float scale, boolean animate) {
        DraweeView<GenericDraweeHierarchy> draweeView = getDraweeView();
        if (draweeView != null) {
            setScale(scale, (draweeView.getRight()) / 2, (draweeView.getBottom()) / 2, animate);
        }
    }

    public void setScale(float scale, float focalX, float focalY, boolean animate) {
        DraweeView<GenericDraweeHierarchy> draweeView = getDraweeView();

        if (draweeView == null || scale < mMinScale || scale > mMaxScale) {
            return;
        }

        if (animate) {
            draweeView.post(new AnimatedZoomRunnable(getScale(), scale, focalX, focalY));
        } else {
            mMatrix.setScale(scale, scale, focalX, focalY);
            checkMatrixAndInvalidate();
        }
    }

    public void setOrientation(@OrientationMode int orientation) {
        mOrientation = orientation;
    }

    public void setOnPhotoTapListener(OnPhotoTapListener listener) {
        mPhotoTapListener = listener;
    }

    public void setOnLongClickListener(View.OnLongClickListener listener) {
        this.mLongClickListener = listener;
    }

    public void setOnViewTapListener(OnViewTapListener listener) {
        mViewTapListener = listener;
    }

    public OnPhotoTapListener getOnPhotoTapListener() {
        return mPhotoTapListener;
    }

    public OnViewTapListener getOnViewTapListener() {
        return mViewTapListener;
    }

    public void update(int imageInfoWidth, int imageInfoHeight) {
        mImageInfoWidth = imageInfoWidth;
        mImageInfoHeight = imageInfoHeight;
        updateBaseMatrix();
    }

    private static void checkZoomLevels(float minZoom, float midZoom, float maxZoom) {
        if (minZoom >= midZoom) {
            throw new IllegalArgumentException("MinZoom has to be less than MidZoom");
        } else if (midZoom >= maxZoom) {
            throw new IllegalArgumentException("MidZoom has to be less than MaxZoom");
        }
    }

    private int getViewWidth() {
        DraweeView<GenericDraweeHierarchy> draweeView = getDraweeView();
        if (draweeView != null) {
            return draweeView.getWidth() - draweeView.getPaddingLeft() - draweeView.getPaddingRight();
        }

        return 0;
    }

    private int getViewHeight() {
        DraweeView<GenericDraweeHierarchy> draweeView = getDraweeView();
        if (draweeView != null) {
            return draweeView.getHeight()
                    - draweeView.getPaddingTop()
                    - draweeView.getPaddingBottom();
        }
        return 0;
    }

    private float getMatrixValue(Matrix matrix, int whichValue) {
        matrix.getValues(mMatrixValues);
        return mMatrixValues[whichValue];
    }

    public Matrix getDrawMatrix() {
        return mMatrix;
    }

    public RectF getDisplayRect() {
        checkMatrixBounds();
        return getDisplayRect(getDrawMatrix());
    }

    public void checkMatrixAndInvalidate() {
        DraweeView<GenericDraweeHierarchy> draweeView = getDraweeView();

        if (draweeView == null) {
            return;
        }

        if (checkMatrixBounds()) {
            draweeView.invalidate();
        }
    }

    public boolean checkMatrixBounds() {
        RectF rect = getDisplayRect(getDrawMatrix());
        if (rect == null) {
            return false;
        }

        float height = rect.height();
        float width = rect.width();
        float deltaX = 0.0F;
        float deltaY = 0.0F;
        int viewHeight = getViewHeight();

        if (height <= (float) viewHeight) {
            deltaY = (viewHeight - height) / 2 - rect.top;
            mScrollEdgeY = EDGE_BOTH;
        } else if (rect.top > 0.0F) {
            deltaY = -rect.top;
            mScrollEdgeY = EDGE_TOP;
        } else if (rect.bottom < (float) viewHeight) {
            deltaY = viewHeight - rect.bottom;
            mScrollEdgeY = EDGE_BOTTOM;
        } else {
            mScrollEdgeY = EDGE_NONE;
        }
        int viewWidth = getViewWidth();
        if (width <= viewWidth) {
            deltaX = (viewWidth - width) / 2 - rect.left;
            mScrollEdgeX = EDGE_BOTH;
        } else if (rect.left > 0) {
            deltaX = -rect.left;
            mScrollEdgeX = EDGE_LEFT;
        } else if (rect.right < viewWidth) {
            deltaX = viewWidth - rect.right;
            mScrollEdgeX = EDGE_RIGHT;
        } else {
            mScrollEdgeX = EDGE_NONE;
        }

        mMatrix.postTranslate(deltaX, deltaY);
        return true;
    }

    private RectF getDisplayRect(Matrix matrix) {
        DraweeView<GenericDraweeHierarchy> draweeView = getDraweeView();
        if (draweeView == null || (mImageInfoWidth == -1 && mImageInfoHeight == -1)) {
            return null;
        }
        mDisplayRect.set(0.0F, 0.0F, mImageInfoWidth, mImageInfoHeight);
        draweeView.getHierarchy().getActualImageBounds(mDisplayRect);
        matrix.mapRect(mDisplayRect);
        return mDisplayRect;
    }

    private void updateBaseMatrix() {
        if (mImageInfoWidth == -1 && mImageInfoHeight == -1) {
            return;
        }
        resetMatrix();
    }

    private void resetMatrix() {
        mMatrix.reset();
        checkMatrixBounds();
        DraweeView<GenericDraweeHierarchy> draweeView = getDraweeView();
        if (draweeView != null) {
            draweeView.invalidate();
        }
    }

    private void checkMinScale() {
        DraweeView<GenericDraweeHierarchy> draweeView = getDraweeView();
        if (draweeView == null) {
            return;
        }

        if (getScale() < mMinScale) {
            RectF rect = getDisplayRect();
            if (null != rect) {
                draweeView.post(new AnimatedZoomRunnable(getScale(), mMinScale, rect.centerX(),
                        rect.centerY()));
            }
        }
    }

    public void onDetachedFromWindow() {
        cancelFling();
    }

    private void cancelFling() {
        if (mCurrentFlingRunnable != null) {
            mCurrentFlingRunnable.cancelFling();
            mCurrentFlingRunnable = null;
        }
    }

    private void postOnAnimation(View view, Runnable runnable) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.postOnAnimation(runnable);
        } else {
            view.postDelayed(runnable, 16L);
        }
    }

    @Override
    public void onScale(float scaleFactor, float focusX, float focusY) {
        if (getScale() < mMaxScale || scaleFactor < 1.0F) {

            if (mScaleChangeListener != null) {
                mScaleChangeListener.onScaleChange(scaleFactor, focusX, focusY);
            }

            mMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY);
            checkMatrixAndInvalidate();
        }
    }

    @Override
    public void onScaleEnd() {
        checkMinScale();
    }

    @Override
    public void onScaleDrag(float dx, float dy) {
        DraweeView<GenericDraweeHierarchy> draweeView = getDraweeView();

        if (draweeView != null && !mScaleDragDetector.isScaling()) {
            mMatrix.postTranslate(dx, dy);
            checkMatrixAndInvalidate();

            ViewParent parent = draweeView.getParent();
            if (parent == null) {
                return;
            }
            if (mAllowParentInterceptOnEdge && !mScaleDragDetector.isScaling() && !mBlockParentIntercept) {
                if (mOrientation == HORIZONTAL &&
                        (mScrollEdgeX == EDGE_BOTH || (mScrollEdgeX == EDGE_LEFT && dx >= 1f)
                                || (mScrollEdgeX == EDGE_RIGHT && dx <= -1f))) {
                    parent.requestDisallowInterceptTouchEvent(false);
                } else if (mOrientation == VERTICAL &&
                        (mScrollEdgeY == EDGE_BOTH || (mScrollEdgeY == EDGE_TOP && dy >= 1f)
                                || (mScrollEdgeY == EDGE_BOTTOM && dy <= -1f))) {
                    parent.requestDisallowInterceptTouchEvent(false);
                }
            } else {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }
    }

    @Override
    public void onScaleFling(float startX, float startY, float velocityX, float velocityY) {
        DraweeView<GenericDraweeHierarchy> draweeView = getDraweeView();
        if (draweeView == null) {
            return;
        }

        mCurrentFlingRunnable = new FlingRunnable(draweeView.getContext());
        mCurrentFlingRunnable.fling(getViewWidth(), getViewHeight(), (int) velocityX,
                (int) velocityY);
        draweeView.post(mCurrentFlingRunnable);
    }

    private class OnPhotoTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = MotionEventCompat.getActionMasked(event);
            switch (action) {
                case MotionEvent.ACTION_DOWN: {
                    ViewParent parent = v.getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    cancelFling();
                }
                break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: {
                    ViewParent parent = v.getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(false);
                    }
                }
                break;
                default:
                    break;
            }

            boolean wasScaling = mScaleDragDetector.isScaling();
            boolean wasDragging = mScaleDragDetector.isDragging();

            boolean handled = mScaleDragDetector.onTouchEvent(event);

            boolean noScale = !wasScaling && !mScaleDragDetector.isScaling();
            boolean noDrag = !wasDragging && !mScaleDragDetector.isDragging();
            mBlockParentIntercept = noScale && noDrag;

            if (mGestureDetector.onTouchEvent(event)) {
                handled = true;
            }

            return handled;
        }
    }

    private class AnimatedZoomRunnable implements Runnable {
        private final float mFocalX, mFocalY;
        private final long mStartTime;
        private final float mZoomStart, mZoomEnd;

        private AnimatedZoomRunnable(final float currentZoom, final float targetZoom, final float focalX, final float focalY) {
            mFocalX = focalX;
            mFocalY = focalY;
            mStartTime = System.currentTimeMillis();
            mZoomStart = currentZoom;
            mZoomEnd = targetZoom;
        }

        @Override
        public void run() {
            DraweeView<GenericDraweeHierarchy> draweeView = getDraweeView();
            if (draweeView == null) {
                return;
            }

            float t = interpolate();
            float scale = mZoomStart + t * (mZoomEnd - mZoomStart);
            float deltaScale = scale / getScale();

            onScale(deltaScale, mFocalX, mFocalY);

            if (t < 1f) {
                postOnAnimation(draweeView, this);
            }
        }

        private float interpolate() {
            float t = 1f * (System.currentTimeMillis() - mStartTime) / mZoomDuration;
            t = Math.min(1f, t);
            t = mZoomInterpolator.getInterpolation(t);
            return t;
        }
    }

    private class FlingRunnable implements Runnable {

        private final ScrollerCompat mScroller;
        private int mCurrentX, mCurrentY;

        private FlingRunnable(Context context) {
            mScroller = ScrollerCompat.create(context);
        }

        private void cancelFling() {
            mScroller.abortAnimation();
        }

        private void fling(int viewWidth, int viewHeight, int velocityX, int velocityY) {
            final RectF rect = getDisplayRect();
            if (null == rect) {
                return;
            }

            final int startX = Math.round(-rect.left);
            final int minX, maxX, minY, maxY;

            if (viewWidth < rect.width()) {
                minX = 0;
                maxX = Math.round(rect.width() - viewWidth);
            } else {
                minX = maxX = startX;
            }

            final int startY = Math.round(-rect.top);
            if (viewHeight < rect.height()) {
                minY = 0;
                maxY = Math.round(rect.height() - viewHeight);
            } else {
                minY = maxY = startY;
            }

            mCurrentX = startX;
            mCurrentY = startY;

            if (startX != maxX || startY != maxY) {
                mScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, 0, 0);
            }
        }

        @Override
        public void run() {
            if (mScroller.isFinished()) {
                return;
            }

            DraweeView<GenericDraweeHierarchy> draweeView = getDraweeView();

            if (draweeView != null && mScroller.computeScrollOffset()) {
                final int newX = mScroller.getCurrX();
                final int newY = mScroller.getCurrY();
                mMatrix.postTranslate(mCurrentX - newX, mCurrentY - newY);
                draweeView.invalidate();
                mCurrentX = newX;
                mCurrentY = newY;
                postOnAnimation(draweeView, this);
            }
        }
    }

    public interface OnScaleChangeListener {
        /**
         * Callback for when the scale changes
         *
         * @param scaleFactor the scale factor
         * @param focusX      focal point X position
         * @param focusY      focal point Y position
         */
        void onScaleChange(float scaleFactor, float focusX, float focusY);
    }

    /**
     * 单击，图片
     */
    public interface OnPhotoTapListener {

        /**
         * A callback to receive where the user taps on a photo. You will only receive a callback if
         * the user taps on the actual photo, tapping on 'whitespace' will be ignored.
         *
         * @param view - View the user tapped.
         * @param x    - where the user tapped from the of the Drawable, as percentage of the
         *             Drawable width.
         * @param y    - where the user tapped from the top of the Drawable, as percentage of the
         *             Drawable height.
         */
        void onPhotoTap(View view, float x, float y);
    }

    /**
     * 空白处，点击
     */
    public interface OnViewTapListener {
        /**
         * A callback to receive where the user taps on a ImageView. You will receive a callback if
         * the user taps anywhere on the view, tapping on 'whitespace' will not be ignored.
         *
         * @param view - View the user tapped.
         * @param x    - where the user tapped from the left of the View.
         * @param y    - where the user tapped from the top of the View.
         */
        void onViewTap(View view, float x, float y);
    }
}
