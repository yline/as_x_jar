package com.yline.jetpack.start.ui.start;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yline.jetpack.R;
import com.yline.utils.LogUtil;
import com.yline.view.fresco.FrescoManager;
import com.yline.view.fresco.view.FrescoView;
import com.yline.view.recycler.adapter.AbstractRecyclerAdapter;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.List;

public class StartFragment extends Fragment {
    private StartViewModel mViewModel;
    private StartRecyclerAdapter mRecyclerAdapter;

    public static StartFragment newInstance() {
        return new StartFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.start_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
    }

    private void initView(View view) {
        initDataObserve();

        mRecyclerAdapter = new StartRecyclerAdapter();
        RecyclerView recyclerView = view.findViewById(R.id.start_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(mRecyclerAdapter);

        initViewClick(view);
    }

    private void initDataObserve() {
        mViewModel = ViewModelProviders.of(this).get(StartViewModel.class);

        // 数据修改后，通知到这
        mViewModel.getImgLiveData().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> stringList) {
                LogUtil.v("onChanged, string size = " + (null == stringList ? "null" : stringList.size()));
                mRecyclerAdapter.setDataList(stringList, true);
            }
        });
    }

    private void initViewClick(View view) {
        view.findViewById(R.id.start_container_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    private void initData() {
        mViewModel.updateImgViewData();
    }

    private static class StartRecyclerAdapter extends AbstractRecyclerAdapter<String> {

        @Override
        public int getItemRes() {
            return R.layout.item_start;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHolder viewHolder, int i) {
            FrescoView frescoView = viewHolder.get(R.id.item_start_fresco);
            FrescoManager.setImageUri(frescoView, get(i));
        }
    }
}
