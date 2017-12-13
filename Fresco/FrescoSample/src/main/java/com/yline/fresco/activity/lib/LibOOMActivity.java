package com.yline.fresco.activity.lib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yline.base.BaseActivity;
import com.yline.fresco.sample.R;
import com.yline.fresco.view.FrescoView;
import com.yline.test.UrlConstant;
import com.yline.view.recycler.adapter.CommonRecyclerAdapter;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 解决OOM问题
 *
 * @author yline 2017/12/11 -- 15:29
 * @version 1.0.0
 */
public class LibOOMActivity extends BaseActivity {
    private OOMRecyclerApdater recyclerApdater;

    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, LibOOMActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_recycler);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerApdater = new OOMRecyclerApdater();
        recyclerView.setAdapter(recyclerApdater);

        // setData
        List<String> urlList = new ArrayList<>();
        for (int i = 0; i < 9_000; i++) {
            urlList.add(UrlConstant.getUrl());
        }
        recyclerApdater.setDataList(urlList);
    }

    private class OOMRecyclerApdater extends CommonRecyclerAdapter<String> {
        @Override
        public int getItemRes() {
            return R.layout.item_recycler;
        }

        @Override
        public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            String url = getItem(position);

            FrescoView frescoView = holder.get(R.id.fresco_view_recycler);
            frescoView.setImageURI(url);

//            FrescoManager.setImageUri(frescoView, getItem(position));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        FrescoUtil.clearCaches();
        System.gc();
    }
}
