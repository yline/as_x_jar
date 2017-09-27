package com.yline.fresco.activity.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.yline.base.BaseAppCompatActivity;
import com.yline.fresco.sample.R;
import com.yline.fresco.view.FrescoView;
import com.yline.test.UrlConstant;
import com.yline.view.recycler.adapter.CommonRecyclerAdapter;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

public class SampleRecyclerActivity extends BaseAppCompatActivity {

    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, SampleRecyclerActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private RecyclerAdapter mRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_recycler);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        mRecyclerAdapter = new RecyclerAdapter();
        recyclerView.setAdapter(mRecyclerAdapter);

        List<String> resultList = new ArrayList<>();
        for (int i = 0; i < 400; i++) {
            resultList.add(UrlConstant.getUrl());
        }
        mRecyclerAdapter.setDataList(resultList);
    }

    private class RecyclerAdapter extends CommonRecyclerAdapter<String> {

        @Override
        public int getItemRes() {
            return R.layout.item_recycler;
        }

        @Override
        public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
            FrescoView frescoView = holder.get(R.id.fresco_view_recycler);
            frescoView.setImageURI(sList.get(position));
            frescoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("xxx-", "onClick: uri = " + sList.get(holder.getAdapterPosition()));
                }
            });
        }
    }
}
