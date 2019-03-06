package com.yline.share;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        final EditText shareEditText = addEditText("");
        addButton("分享文案", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = shareEditText.getText().toString().trim();
                ShareWechat.shareText(MainActivity.this, text);
            }
        });
    }
}
