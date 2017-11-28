package com.yline.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yline.base.BaseActivity;
import com.yline.photofrescoview.sample.R;
import com.yline.test.UrlConstant;
import com.yline.view.recycler.adapter.AbstractCommonRecyclerAdapter;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.yline.photofrescoview.PhotoAttach;
import me.yline.photofrescoview.PhotoFrescoView;

public class RecyclerViewActivity extends BaseActivity {

    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, RecyclerViewActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DraweeAdapter adapter = new DraweeAdapter();
        recyclerView.setAdapter(adapter);

        List<String> urlList = new ArrayList<>();
        int size = new Random().nextInt(30) + 10;
        for (int i = 0; i < size; i++) {
            urlList.add(UrlConstant.getUrl());
        }
        adapter.setDataList(urlList, true);
    }

    private class DraweeAdapter extends AbstractCommonRecyclerAdapter<String> {
        @Override
        public int getItemRes() {
            return R.layout.item_photo_view;
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            PhotoFrescoView photoFrescoView = holder.get(R.id.photo_drawee_view);
            photoFrescoView.setOrientation(PhotoAttach.VERTICAL);

            photoFrescoView.setPhotoUri(getItem(position));
        }
    }
}
