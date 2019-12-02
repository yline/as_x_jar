package com.baidu.idl.face.example;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.baidu.idl.face.platform.injector.LogManager;
import com.baidu.idl.face.platform.ui.utils.FaceStorageUtils;
import com.kjtpay.face.sample.R;
import com.yline.base.BaseActivity;
import com.yline.view.recycler.adapter.AbstractRecyclerAdapter;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageActivity extends BaseActivity {
    public static void launch(Context context) {
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, ImageActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private ImageRecyclerAdapter mImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        initView();
        initData();
    }

    private void initView() {
        mImageAdapter = new ImageRecyclerAdapter(FaceStorageUtils.getPath(this));

        RecyclerView recyclerView = findViewById(R.id.image_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(mImageAdapter);

        initViewClick();
    }

    private void initViewClick() {
        findViewById(R.id.image_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FaceStorageUtils.delete(FaceStorageUtils.getPath(ImageActivity.this));
                initData();
            }
        });
    }

    private void initData() {
        String path = FaceStorageUtils.getPath(this);
        LogManager.v("path = " + path);

        List<String> childList = new ArrayList<>();
        if (!TextUtils.isEmpty(path)) {
            File dirFile = new File(path);
            if (dirFile.isDirectory()) {
                childList.addAll(Arrays.asList(dirFile.list()));
            }
        }
        mImageAdapter.setDataList(childList, true);
    }

    private static class ImageRecyclerAdapter extends AbstractRecyclerAdapter<String> {
        private final String path;

        public ImageRecyclerAdapter(String path) {
            this.path = path + File.separator;
        }

        @Override
        public int getItemRes() {
            return R.layout.item_image;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHolder viewHolder, int i) {
            String fileName = get(i);
            String filePath = path + fileName;

            ImageView imageView = viewHolder.get(R.id.item_image);
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            if (null != bitmap) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(R.drawable.empty);
            }

            viewHolder.setText(R.id.item_image_text, fileName);
        }
    }
}
