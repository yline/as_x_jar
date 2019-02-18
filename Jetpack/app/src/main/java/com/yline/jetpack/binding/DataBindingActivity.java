package com.yline.jetpack.binding;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yline.base.BaseAppCompatActivity;
import com.yline.jetpack.R;
import com.yline.jetpack.binding.custom.CustomActivity;
import com.yline.jetpack.binding.express.ExpressActivity;
import com.yline.jetpack.binding.model.DataBindingItemModel;
import com.yline.jetpack.binding.observe.ObserveActivity;
import com.yline.jetpack.binding.observe.model.ObserveModel;
import com.yline.jetpack.databinding.ActivityDataBindingBinding;
import com.yline.jetpack.databinding.ItemDataBindingBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * DataBinding入口;
 * Recycler实现了DataBinding，可以尝试封装；但是带头部和底部等情况难处理；而且封装感觉不稳定；
 * 相比较之前的，由于Item布局往往比较简单，因此提升程度并不会很明显。
 *
 * @author yline 2019/2/15 -- 16:55
 */
public class DataBindingActivity extends BaseAppCompatActivity {
    public static void launch(Context context) {
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, DataBindingActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityDataBindingBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding);

        DataBindingRecyclerAdapter recyclerAdapter = new DataBindingRecyclerAdapter();

        dataBinding.dataBindingRecycler.setLayoutManager(new LinearLayoutManager(this));
        dataBinding.dataBindingRecycler.setAdapter(recyclerAdapter);
        recyclerAdapter.setDataList();
    }

    private static class DataBindingRecyclerAdapter extends RecyclerView.Adapter<RecyclerBindingHolder> {
        private List<DataBindingItemModel> mTypeList = new ArrayList<>();

        @Override
        public RecyclerBindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 布局，数据处理工具绑定
            ItemDataBindingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_data_binding, parent, false);
            DataBindingPresenter presenter = new DataBindingPresenter();
            binding.setPresenter(presenter);

            // viewHolder 和 布局绑定
            RecyclerBindingHolder bindingHolder = new RecyclerBindingHolder(binding.getRoot());
            bindingHolder.setBinding(binding);
            return bindingHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerBindingHolder bindingHolder, int i) {
            // 动态绑定变量
            // bindingHolder.getBinding().setVariable(BR.data, mTypeList.get(i)); 这样依据全局id，摄入值
            bindingHolder.getBinding().setData(mTypeList.get(i));
            bindingHolder.getBinding().executePendingBindings();
        }

        @Override
        public int getItemCount() {
            return mTypeList.size();
        }

        private void setDataList() {
            mTypeList.clear();
            mTypeList.addAll(DataBindingPresenter.genItemList());
            notifyDataSetChanged();
        }
    }

    /**
     * 最终处理，item的位置
     */
    public static class DataBindingPresenter {
        public void onTypeClick(Context context, DataBindingItemModel itemModel) {
            switch (itemModel.getType()) {
                case 0:
                    ExpressActivity.launch(context);
                    break;
                case 1:
                    ObserveActivity.launch(context);
                    break;
                case 2:
                    CustomActivity.launch(context);
                    break;
                default:
            }
        }

        private static List<DataBindingItemModel> genItemList() {
            List<DataBindingItemModel> itemModelList = new ArrayList<>();
            itemModelList.add(new DataBindingItemModel(0, "Express xml -> Java 数据引用"));
            itemModelList.add(new DataBindingItemModel(1, "Observe Java -> xml 数据修改"));
            itemModelList.add(new DataBindingItemModel(2, "Custom xml组件化"));
            return itemModelList;
        }
    }

    private static class RecyclerBindingHolder extends RecyclerView.ViewHolder {
        private ItemDataBindingBinding binding;

        private RecyclerBindingHolder(@NonNull View itemView) {
            super(itemView);
        }

        public ItemDataBindingBinding getBinding() {
            return binding;
        }

        public void setBinding(ItemDataBindingBinding binding) {
            this.binding = binding;
        }
    }
}

