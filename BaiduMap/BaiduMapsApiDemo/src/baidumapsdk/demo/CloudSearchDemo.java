package baidumapsdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CloudSearchDemo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cloud_search_demo);
	}

	public void startCloudSearchDemo(View view) {
		Intent intent = new Intent();
		intent.setClass(this, CloudSearchActivity.class);
		startActivity(intent);

	}
}
