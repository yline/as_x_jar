package com.retrofit.server;

import com.retrofit.model.ChapterModel;

import retrofit2.http.GET;

/**
 * Retrofit2 interface 管理类
 *
 * @author yline 2017/10/26 -- 20:25
 * @version 1.0.0
 */
public class ServerApi {
    private static final String WX_ARTICLE_CHAPTERS = "wxarticle/chapters/json";

    public interface ChapterService {
        @GET(WX_ARTICLE_CHAPTERS)
        retrofit2.Call<ChapterModel> getChapterListCall();
    }
}
