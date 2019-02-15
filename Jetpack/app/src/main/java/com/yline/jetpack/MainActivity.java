package com.yline.jetpack;

import android.os.Bundle;
import android.view.View;

import com.yline.jetpack.binding.BindingActivity;
import com.yline.jetpack.start.StartActivity;
import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("DataBinding", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BindingActivity.launch(MainActivity.this);
            }
        });

        addButton("ViewModel", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartActivity.launch(MainActivity.this);
            }
        });
    }
}
