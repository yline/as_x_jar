package com.yline.fresco.activity.lib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.facebook.imagepipeline.image.ImageInfo;
import com.yline.fresco.FrescoManager;
import com.yline.fresco.activity.IApplication;
import com.yline.fresco.common.FrescoCallback;
import com.yline.fresco.sample.R;
import com.yline.fresco.view.FrescoView;
import com.yline.test.UrlConstant;

import java.util.ArrayList;

public class LibViewPagerActivity extends AppCompatActivity {
    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, LibViewPagerActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private ArrayList<String> mImageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib_view_pager);

        mImageList = new ArrayList<>();
        mImageList.add(UrlConstant.getGif(0));
        mImageList.add(UrlConstant.getGif(1));
        mImageList.add(UrlConstant.getGif(2));
        mImageList.add(UrlConstant.getGif(3));
        mImageList.add(UrlConstant.getGif(4));

        ViewPager viewPager = (ViewPager) findViewById(R.id.lib_view_pager);
        viewPager.setAdapter(new ImageAdapter());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                IApplication.toast("select position = " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class ImageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageList == null ? 0 : mImageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            ImageBrowseView imageBrowseView = new ImageBrowseView(LibViewPagerActivity.this);
            imageBrowseView.setData(mImageList.get(position));
            container.addView(imageBrowseView, 0);
            return imageBrowseView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public static class ImageBrowseView extends RelativeLayout {
        private FrescoView frescoView;

        public ImageBrowseView(Context context) {
            super(context);
            initView();
        }

        public ImageBrowseView(Context context, AttributeSet attrs) {
            super(context, attrs);
            initView();
        }

        public ImageBrowseView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initView();
        }

        private void initView() {
            LayoutInflater.from(getContext()).inflate(R.layout.item_recycler, this, true);
            frescoView = findViewById(R.id.fresco_view_recycler);
        }

        public void setData(final String imageUrl) {
            FrescoManager.setImageUri(frescoView, imageUrl, false, new FrescoCallback.OnSimpleLoadCallback() {
                @Override
                public void onStart(String id, Object callerContext) {

                }

                @Override
                public void onFailure(String id, Throwable throwable) {

                }

                @Override
                public void onSuccess(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                    frescoView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            IApplication.toast("onClick");
                        }
                    });
                    frescoView.setOnLongClickListener(new OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            IApplication.toast("onLongClick");
                            return false;
                        }
                    });


                    if (null != animatable) {
                        String filePath = FrescoManager.getCacheFilePath(imageUrl);
                        if (!TextUtils.isEmpty(filePath)) {
                            IApplication.toast("filePath = " + filePath);
                        }

                        animatable.start();
                    }
                }
            });
        }
    }
}
