package com.retrofit;

import android.os.Bundle;
import android.view.View;

import com.retrofit.model.UserModel;
import com.yline.log.LogFileUtil;
import com.yline.test.BaseTestActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends BaseTestActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("getSimpleSample", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.4.145/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                RetroManager.IUserModel iUserModel = retrofit.create(RetroManager.IUserModel.class);

                Call<UserModel> userModelCall = iUserModel.getUser("userId", "123456");

                LogFileUtil.v("request");
                userModelCall.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        LogFileUtil.v("onResponse");
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        LogFileUtil.e("onFailure", "", t);
                    }
                });
            }
        });
    }
}
