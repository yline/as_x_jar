package com.yline.jetpack.start;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yline.base.BaseAppCompatActivity;
import com.yline.jetpack.R;
import com.yline.jetpack.start.ui.start.StartFragment;

public class StartActivity extends BaseAppCompatActivity {
    public static void launch(Context context){
    	if (null != context){
    		Intent intent = new Intent();
    		intent.setClass(context, StartActivity.class);
    		if (!(context instanceof Activity)){
    			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		}
    		context.startActivity(intent);
    	}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, StartFragment.newInstance())
                    .commitNow();
        }
    }
}
