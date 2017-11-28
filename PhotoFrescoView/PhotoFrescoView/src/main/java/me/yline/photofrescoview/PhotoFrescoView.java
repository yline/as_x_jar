package me.yline.photofrescoview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

/**
 * PhotoView + Fresco
 *
 * @author yline 2017/11/28 -- 9:23
 * @version 1.0.0
 */
public class PhotoFrescoView extends SimpleDraweeView {
    private PhotoAttach mPhotoAttach;
    private boolean mEnableDraweeMatrix = true;

    public PhotoFrescoView(Context context) {
        super(context);
        initHelper();
    }

    public PhotoFrescoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHelper();
    }

    public PhotoFrescoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initHelper();
    }

    private void initHelper() {
        if (mPhotoAttach == null || mPhotoAttach.getDraweeView() == null) {
            mPhotoAttach = new PhotoAttach(this);
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        int saveCount = canvas.save();
        if (mEnableDraweeMatrix) {
            canvas.concat(mPhotoAttach.getDrawMatrix());
        }
        super.onDraw(canvas);
        canvas.restoreToCount(saveCount);
    }

    @Override
    protected void onAttachedToWindow() {
        initHelper();
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        mPhotoAttach.onDetachedFromWindow();
        super.onDetachedFromWindow();
    }

    public void setOrientation(@PhotoAttach.OrientationMode int orientation) {
        mPhotoAttach.setOrientation(orientation);
    }

    /**
     * 双击，图片
     */
    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener listener) {
        mPhotoAttach.setOnDoubleTapListener(listener);
    }

    /**
     * 单击，图片
     */
    public void setOnPhotoTapListener(PhotoAttach.OnPhotoTapListener listener) {
        mPhotoAttach.setOnPhotoTapListener(listener);
    }

    @Override
    public void setOnLongClickListener(@Nullable OnLongClickListener listener) {
        mPhotoAttach.setOnLongClickListener(listener);
    }

    /**
     * 单击，除图片外，空白处
     */
    public void setOnViewTapListener(PhotoAttach.OnViewTapListener listener) {
        mPhotoAttach.setOnViewTapListener(listener);
    }

    public void update(int imageInfoWidth, int imageInfoHeight) {
        mPhotoAttach.update(imageInfoWidth, imageInfoHeight);
    }

    public PhotoAttach getPhotoAttach() {
        return mPhotoAttach;
    }

    public void setEnableDraweeMatrix(boolean enable) {
        this.mEnableDraweeMatrix = enable;
    }

    public void setPhotoUri(String uri) {
        setEnableDraweeMatrix(false);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setOldController(getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>() {
                    @Override
                    public void onFailure(String id, Throwable throwable) {
                        super.onFailure(id, throwable);
                        setEnableDraweeMatrix(false);
                    }

                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        super.onFinalImageSet(id, imageInfo, animatable);
                        setEnableDraweeMatrix(true);
                        if (imageInfo != null) {
                            update(imageInfo.getWidth(), imageInfo.getHeight());
                        }
                    }

                    @Override
                    public void onIntermediateImageFailed(String id, Throwable throwable) {
                        super.onIntermediateImageFailed(id, throwable);
                        setEnableDraweeMatrix(false);
                    }

                    @Override
                    public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
                        super.onIntermediateImageSet(id, imageInfo);
                        setEnableDraweeMatrix(true);
                        if (imageInfo != null) {
                            update(imageInfo.getWidth(), imageInfo.getHeight());
                        }
                    }
                })
                .build();
        setController(controller);
    }
}
