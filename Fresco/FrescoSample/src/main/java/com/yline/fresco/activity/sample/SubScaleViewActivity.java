package com.yline.fresco.activity.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.widget.ImageView;

import com.facebook.imagepipeline.request.ImageRequest;
import com.yline.base.BaseActivity;
import com.yline.fresco.FrescoManager;
import com.yline.fresco.activity.IApplication;
import com.yline.fresco.common.FrescoCallback;
import com.yline.fresco.sample.R;
import com.yline.fresco.subscaleview.ImageSource;
import com.yline.fresco.subscaleview.SubsamplingScaleImageView;
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
    private ImageView mImageView;
    private long mTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_scale_view);

        mImageView = findViewById(R.id.sub_scale_image_view);
        mSubsamplingScaleImageView = findViewById(R.id.sub_scale_view);
        mSubsamplingScaleImageView.setPanLimit(SubsamplingScaleImageView.PAN_LIMIT_INSIDE);
//        mSubsamplingScaleImageView.setMaxScale(10);// px 的最大倍数

        final int width = UIScreenUtil.getScreenWidth(this);
        final int height = UIScreenUtil.getScreenHeight(this);

        mTime = System.currentTimeMillis();

        final String httpUrl = "http://img.benditoutiao.com/circle-image/alioss_1511577868047.jpeg";
//        final String httpUrl = "http://192.168.0.143/android/git_api/libhttp/scaleview_alioss.jpeg";
//        final String httpUrl = UrlConstant.getSuper_Big(2); // 清明上河图，最大
        LogUtil.v("httpUrl id = " + Process.myPid() + ", " + Process.myTid() + ", " + Process.myUid() + ", " + Thread.currentThread().getId());

        LogUtil.v("currentId = " + Thread.currentThread().getId());
        FrescoManager.fetchDecodedImage(httpUrl, new FrescoCallback.OnSimpleFetchCallback() {
            @Override
            public void onStart(ImageRequest request, Object callerContext, String requestId, boolean isPrefetch) {
                LogUtil.v("currentId = " + Thread.currentThread().getId());
            }

            @Override
            public void onFailure(ImageRequest request, String requestId, Throwable throwable, boolean isPrefetch) {
                LogUtil.v("onRequestFailure throwable = " + throwable);

                LogUtil.v("currentId = " + Thread.currentThread().getId());
            }

            @Override
            public void onSuccess(ImageRequest request, String requestId, boolean isPrefetch) {
                LogUtil.v("currentId = " + Thread.currentThread().getId());

                // 首次下载成功，需要缓存数据时间，测试：20M图片，300ms足够
//                int waitTime = 300;
//                final String fileUrl = FrescoManager.getCacheFilePath(httpUrl);
//                if (!TextUtils.isEmpty(fileUrl)) {
//                    waitTime = 0;
//                }

//                LogUtil.v("prefetch diffTime = " + (System.currentTimeMillis() - mTime) + ", id = " + Process.myTid() + ", waitTime = " + waitTime);
                mTime = System.currentTimeMillis();

                // 1.7M; 只需要 100S; 19.7M; 500s足够
                IApplication.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        mSubsamplingScaleImageView.resetScaleAndCenter();
                        String filePath = FrescoManager.getCacheFilePath(httpUrl);
                        mSubsamplingScaleImageView.setImage(ImageSource.uri(filePath)); // 图片太大，刚刚下载完成，第一次展示必定失败；所以，可以设置加载失败的图

//                        mImageView.setImageURI(Uri.parse(filePath));

                        LogUtil.v("id = " + Process.myPid() + ", " + Process.myTid() + ", " + Process.myUid() + ", " + Thread.currentThread().getId());

                        LogUtil.v("show ScaleView diffTime = " + (System.currentTimeMillis() - mTime) + ", filePath = " + filePath);
                        mTime = System.currentTimeMillis();
                    }
                });
            }
        });
    }
}
