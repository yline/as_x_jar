package com.baidu.idl.face.platform.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.idl.face.platform.ui.R;
import com.baidu.idl.face.platform.ui.utils.FaceUIScreenUtil;

import java.util.List;
import java.util.Map;

public class FaceDetectView extends FrameLayout implements SurfaceHolder.Callback {
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;

    private FaceDetectRoundView mRoundMaskView;

    private TextView mTopTipsView, mBottomTipsView;
    private ImageView mSuccessImageView;
    private ImageView mSoundImageView;

    private LinearLayout mImageResultContainer;

    public FaceDetectView(Context context) {
        this(context, null);
    }

    public FaceDetectView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.faceui_view_face_detect, this);
        initView();
    }

    private void initView() {
        mRoundMaskView = findViewById(R.id.view_face_detect_mask);

        mTopTipsView = findViewById(R.id.view_face_detect_top_tips);
        mBottomTipsView = findViewById(R.id.view_face_detect_bottom_tips);

        mSuccessImageView = findViewById(R.id.view_face_detect_success_image);
        mSoundImageView = findViewById(R.id.view_face_detect_sound);

        mImageResultContainer = findViewById(R.id.view_face_detect_image);
        initSurfaceView();

        initViewClick();
    }

    private void initSurfaceView() {
        FrameLayout surfaceContainer = findViewById(R.id.view_face_detect_surface);

        mSurfaceView = new SurfaceView(getContext());
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setSizeFromLayout();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        int width = (int) (FaceUIScreenUtil.getScreenWidth(getContext()) * FaceDetectRoundView.SURFACE_RATIO);
        int height = (int) (FaceUIScreenUtil.getScreenHeight(getContext()) * FaceDetectRoundView.SURFACE_RATIO);
        FrameLayout.LayoutParams surfaceParam = new FrameLayout.LayoutParams(width, height, Gravity.CENTER);
        mSurfaceView.setLayoutParams(surfaceParam);

        surfaceContainer.addView(mSurfaceView);
    }

    private void initViewClick() {
        // 关闭
        findViewById(R.id.view_face_detect_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).finish();
                }
            }
        });
    }

    public void setOnSoundClickListener(View.OnClickListener listener) {
        mSoundImageView.setOnClickListener(listener);
    }

    public void updateSoundView(boolean enable) {
        mSoundImageView.setImageResource(enable ? R.drawable.faceui_ic_enable_sound_ext : R.drawable.faceui_ic_disable_sound_ext);
    }

    public void setTopTips(int stringId) {
        mTopTipsView.setText(stringId);
    }

    public SurfaceHolder checkSurfaceHolder() {
        if (null != mSurfaceView && null != mSurfaceView.getHolder()) {
            mSurfaceHolder = mSurfaceView.getHolder();
            mSurfaceHolder.addCallback(this);
        }
        return mSurfaceHolder;
    }

    public void showOKResult(String topMessage) {
        setTopTipsView(false, topMessage);
        mBottomTipsView.setText("");
        mRoundMaskView.processDrawState(false);
        setSuccessImageViewVisible(true);
    }

    public void showActionResult(String topMessage) {
        setTopTipsView(false, topMessage);
        mBottomTipsView.setText("");
        mRoundMaskView.processDrawState(false);
        setSuccessImageViewVisible(false);
    }

    public void showOutOfRangeResult(String topMessage, String bottomMessage) {
        setTopTipsView(true, topMessage);
        mBottomTipsView.setText(bottomMessage);
        mRoundMaskView.processDrawState(true);
        setSuccessImageViewVisible(false);
    }

    public void showDefaultResult(String topMessage) {
        setTopTipsView(false, topMessage);
        mBottomTipsView.setText("");
        mRoundMaskView.processDrawState(true);
        setSuccessImageViewVisible(false);
    }

    private void setTopTipsView(boolean isWarning, String message) {
        if (isWarning) {
            Drawable tipsIcon = getResources().getDrawable(R.drawable.faceui_ic_warning);
            tipsIcon.setBounds(0, 0, (int) (tipsIcon.getMinimumWidth() * 0.7f),
                    (int) (tipsIcon.getMinimumHeight() * 0.7f));
            mTopTipsView.setCompoundDrawablePadding(15);

            mTopTipsView.setBackgroundResource(R.drawable.faceui_bg_tips);
            mTopTipsView.setText(R.string.faceui_detect_standard);
            mTopTipsView.setCompoundDrawables(tipsIcon, null, null, null);
        } else {
            mTopTipsView.setBackgroundResource(R.drawable.faceui_bg_tips_no);
            mTopTipsView.setCompoundDrawables(null, null, null, null);
        }

        mTopTipsView.setText(message);
    }

    private void setSuccessImageViewVisible(boolean visible) {
        if (null == mSuccessImageView.getTag()) {
            Rect rect = mRoundMaskView.getFaceRoundRect();
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) mSuccessImageView.getLayoutParams();
            rlp.setMargins(rect.centerX() - (mSuccessImageView.getWidth() / 2), rect.top - (mSuccessImageView.getHeight() / 2),
                    0, 0);
            mSuccessImageView.setLayoutParams(rlp);
            mSuccessImageView.setTag("setLayout");
        }
        mSuccessImageView.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    public void setImageResultList(List<Bitmap> imageList) {
        if (null == imageList || imageList.isEmpty()) {
            return;
        }

        mImageResultContainer.removeAllViews();
        for (Bitmap bmp : imageList) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageBitmap(bmp);
            mImageResultContainer.addView(imageView, new LinearLayout.LayoutParams(300, 300));
        }
    }

    public void setImageResultList(Map<String, Bitmap> imageMap) {
        if (null == imageMap || imageMap.isEmpty()) {
            return;
        }

        mImageResultContainer.removeAllViews();
        for (Bitmap bmp : imageMap.values()) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageBitmap(bmp);
            mImageResultContainer.addView(imageView, new LinearLayout.LayoutParams(300, 300));
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (null == holder.getSurface()) {
            return;
        }

        if (null != onDetectCallback) {
            onDetectCallback.onSurfaceChanged(holder, format, width, height);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void release() {
        if (null != mSurfaceHolder) {
            mSurfaceHolder.removeCallback(this);
        }
    }

    private OnDetectCallback onDetectCallback;

    public void setOnDetectCallback(OnDetectCallback onDetectCallback) {
        this.onDetectCallback = onDetectCallback;
    }

    public interface OnDetectCallback {
        void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height);
    }
}
