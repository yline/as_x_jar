package com.yline.jetpack.start.ui.start;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.yline.test.UrlConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StartViewModel extends ViewModel {
    private MutableLiveData<List<String>> imgLiveData;

    public LiveData<List<String>> getImgLiveData() {
        if (null == imgLiveData) {
            imgLiveData = new MutableLiveData<>();
        }
        return imgLiveData;
    }

    public void updateImgViewData() {
        int size = new Random().nextInt(20) + 10;

        // 更新数据
        List<String> urlList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            urlList.add(UrlConstant.getUrlSquare());
        }
        imgLiveData.setValue(urlList);
    }
}
