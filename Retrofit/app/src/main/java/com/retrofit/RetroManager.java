package com.retrofit;

import com.retrofit.model.UserModel;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit2 interface 管理类
 *
 * @author yline 2017/10/26 -- 20:25
 * @version 1.0.0
 */
public class RetroManager {

    public interface IUserModel {
        @GET("android/git_api/libhttp/puppet_list.txt")
        retrofit2.Call<UserModel> getUser(@Query("userId") String userId, @Query("pwd") String pwd);
    }
}
