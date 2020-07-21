package com.retrofit.server;

import com.retrofit.model.ChapterModel;
import com.yline.utils.LogUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerManager {
    private static final String BASE_URL = "https://wanandroid.com/";

    public static void request() {
        LogUtil.v("request start, thread = " + Thread.currentThread().getId());

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        ServerApi.ChapterService chapterService = retrofit.create(ServerApi.ChapterService.class);

        // 实际对象：retrofit2.OkHttpCall  对应的CallAdapter.Factory 为：ExecutorCallAdapterFactory
        Call<ChapterModel> chapterListCall = chapterService.getChapterListCall();
        chapterListCall.enqueue(new Callback<ChapterModel>() {
            @Override
            public void onResponse(Call<ChapterModel> call, Response<ChapterModel> response) {
                LogUtil.v("response, thread = " + Thread.currentThread().getId());

                ChapterModel chapterModel = response.body();
                if (null != chapterModel) {
                    LogUtil.v(chapterModel.getErrorCode() + ", " + chapterModel.getErrorMsg());
                    LogUtil.v(chapterModel.toString());
                }
            }

            @Override
            public void onFailure(Call<ChapterModel> call, Throwable t) {
                LogUtil.e("failure, thread = " + Thread.currentThread().getId(), t);
            }
        });
    }
}
