package com.yline.fresco.activity.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;

import com.facebook.common.util.UriUtil;
import com.facebook.imagepipeline.listener.BaseRequestListener;
import com.facebook.imagepipeline.request.ImageRequest;
import com.yline.base.BaseActivity;
import com.yline.fresco.FrescoManager;
import com.yline.fresco.activity.IApplication;
import com.yline.fresco.sample.R;
import com.yline.fresco.subscaleview.ImageSource;
import com.yline.fresco.subscaleview.SubsamplingScaleImageView;
import com.yline.test.UrlConstant;
import com.yline.utils.LogUtil;
import com.yline.utils.UIScreenUtil;

public class SubScaleViewActivity extends BaseActivity {
    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, SubScaleViewActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private SubsamplingScaleImageView mSubsamplingScaleImageView;
    private long mTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_scale_view);

        mSubsamplingScaleImageView = findViewById(R.id.sub_scale_view);
        mSubsamplingScaleImageView.setMaxScale(10);// px 的最大倍数

        final int width = UIScreenUtil.getScreenWidth(this);
        final int height = UIScreenUtil.getScreenHeight(this);

        mTime = System.currentTimeMillis();

//        final String httpUrl = "http://img.benditoutiao.com/circle-image/alioss_1511577868047.jpeg";
//        final String httpUrl = "http://192.168.2.184/android/git_api/libhttp/qingming.jpg";

        final String httpUrl = UrlConstant.getSuper_Big(0); // 清明上河图，最大
        LogUtil.v("start ScaleView time = " + mTime + ", httpUrl = " + httpUrl + ", id = " + Process.myTid());
        FrescoManager.prefetchToDiskCache(httpUrl, new BaseRequestListener() {
            @Override
            public void onRequestFailure(ImageRequest request, String requestId, Throwable throwable, boolean isPrefetch) {
                super.onRequestFailure(request, requestId, throwable, isPrefetch);
                LogUtil.v("onRequestFailure throwable = " + throwable);
            }

            @Override
            public void onRequestSuccess(ImageRequest request, String requestId, boolean isPrefetch) {
                super.onRequestSuccess(request, requestId, isPrefetch);

                final String filePath = FrescoManager.getCacheFilePath(httpUrl);
                Uri imageUri = new Uri.Builder().scheme(UriUtil.LOCAL_FILE_SCHEME).path(filePath).build();

                LogUtil.v("prefetch diffTime = " + (System.currentTimeMillis() - mTime) + ", imageUri = " + imageUri + ", id = " + Process.myTid());
                mTime = System.currentTimeMillis();

                IApplication.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        mSubsamplingScaleImageView.setImage(ImageSource.uri(filePath)); // 图片太大，刚刚下载完成，第一次展示必定失败；所以，可以设置加载失败的图

                        LogUtil.v("show ScaleView diffTime = " + (System.currentTimeMillis() - mTime));
                        mTime = System.currentTimeMillis();
                    }
                });
            }
        });


    }
}
