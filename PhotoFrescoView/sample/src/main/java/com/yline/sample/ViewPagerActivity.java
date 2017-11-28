package com.yline.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yline.base.BaseActivity;
import com.yline.photofrescoview.sample.R;
import com.yline.test.UrlConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import me.yline.photofrescoview.PhotoFrescoView;

public class ViewPagerActivity extends BaseActivity {
    private TextView tvHint;

    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, ViewPagerActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);

        tvHint = (TextView) findViewById(R.id.view_pager_hint);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        final DraweePagerAdapter adapter = new DraweePagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tvHint.setText(String.format(Locale.CHINA, "T:%d,I:%d", adapter.getCount(), position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        List<String> urlList = new ArrayList<>();
        int size = new Random().nextInt(15) + 6;
        for (int i = 0; i < size; i++) {
            urlList.add(UrlConstant.getUrl());
        }
        adapter.setDataList(urlList, true);
    }

    private class DraweePagerAdapter extends PagerAdapter {
        private List<String> mDataList;

        private DraweePagerAdapter() {
            mDataList = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup viewGroup, int position) {
            final PhotoFrescoView photoDraweeView = new PhotoFrescoView(viewGroup.getContext());
            photoDraweeView.setPhotoUri(mDataList.get(position));

            try {
                viewGroup.addView(photoDraweeView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return photoDraweeView;
        }

        private void setDataList(List<String> list, boolean isNotify) {
            if (null != list && list.size() > 0) {
                mDataList = new ArrayList<>(list);
                if (isNotify) {
                    notifyDataSetChanged();
                }
            }
        }
    }
}
