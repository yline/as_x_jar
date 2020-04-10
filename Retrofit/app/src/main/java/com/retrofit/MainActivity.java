package com.retrofit;

import android.os.Bundle;
import android.view.View;

import com.retrofit.server.ServerManager;
import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {


    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("getSimpleSample", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerManager.request();
            }
        });
    }


}
