package demo.afinalexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import demo.afinalexample.ui.BitmapActivity;
import demo.afinalexample.ui.DBActivity;
import demo.afinalexample.ui.InnerActivity;
import demo.afinalexample.ui.LoadActivity;

public class MainActivity extends Activity implements OnClickListener{
	private Button btn_viewinject_activity;
	private Button btn_load_activity;
	private Button btn_db_activity;
	private Button btn_bitmap_activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initView();
		initData();
	}

	private void initView(){
		btn_viewinject_activity = (Button) this.findViewById(R.id.btn_viewinject_activity);
		btn_load_activity = (Button) this.findViewById(R.id.btn_load_activity);
		btn_db_activity = (Button) this.findViewById(R.id.btn_db_activity);
		btn_bitmap_activity = (Button) this.findViewById(R.id.btn_bitmap_activity);
	}

	private void initData(){
		btn_viewinject_activity.setOnClickListener(this);
		btn_load_activity.setOnClickListener(this);
		btn_db_activity.setOnClickListener(this);
		btn_bitmap_activity.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_viewinject_activity:
			startActivity(new Intent(this, InnerActivity.class));
			break;
		case R.id.btn_load_activity:
			startActivity(new Intent(this, LoadActivity.class));
			break;
		case R.id.btn_db_activity:
			startActivity(new Intent(this, DBActivity.class));
			break;
		case R.id.btn_bitmap_activity:
			startActivity(new Intent(this, BitmapActivity.class));
			break;
		}
	}
}
