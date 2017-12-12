package com.glide.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.glide.R;
import com.yline.base.BaseAppCompatActivity;
import com.yline.test.UrlConstant;
import com.yline.view.recycler.adapter.AbstractCommonRecyclerAdapter;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

public class SampleSingleActivity extends BaseAppCompatActivity {
    private SampleSingleAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_single);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_sample_single);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerAdapter = new SampleSingleAdapter();
        recyclerView.setAdapter(recyclerAdapter);

        initData();
    }

    private void initData() {
        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < 9_000; i++) {
            dataList.add(UrlConstant.getUrl());
        }
        recyclerAdapter.setDataList(dataList, true);
    }

    private class SampleSingleAdapter extends AbstractCommonRecyclerAdapter<String> {

        @Override
        public int getItemRes() {
            return R.layout.item_sample_single;
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder viewHolder, int position) {
            viewHolder.setText(R.id.tv_title, getItem(position));

            ImageView imageView = viewHolder.get(R.id.iv_title);
            Glide.with(SampleSingleActivity.this) // 初始化
                    .load(getItem(position)) // 下载资源
                    .asBitmap() // 确认是静态图片,就不加载动态了
                    .override(400, 400) // 设置加载的图片尺寸
                    .placeholder(R.mipmap.global_load_failed) // 占位图（图形和将要加载的图，需要保持比例一致；否则会导致拉伸）
                    .diskCacheStrategy(DiskCacheStrategy.RESULT) // 占位符  默认RESULT
                    .error(R.mipmap.global_load_failed)
                    .into(imageView); // 放置到View中
            /*
            Glide.get(SampleSingleActivity.this).clearDiskCache(); // 清理磁盘缓存 需要在子线程中执行
			Glide.get(SampleSingleActivity.this).clearMemory(); // 清理内存缓存  可以在UI主线程中进行*/
        }
    }

    public static void actionStart(Context context) {
        context.startActivity(new Intent(context, SampleSingleActivity.class));
    }
}
