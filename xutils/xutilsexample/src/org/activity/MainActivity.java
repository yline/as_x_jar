package org.activity;

import org.base.BaseActivity;
import org.base.BaseFragment;
import org.image.ImageBindFragment;
import org.image.ImageLoadFragment;
import org.xutils.view.annotation.ContentView;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import f21.xutilsexample.R;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity{
	private FragmentManager fragmentManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		fragmentManager = this.getSupportFragmentManager();
		
		addImageLoad();
//		addImageBind();
	}
	
	private void addImageLoad() {	
		BaseFragment fragment = new ImageLoadFragment();
		
		if (null != fragment) {	// 自带管理队列
			fragmentManager.beginTransaction().add(R.id.ll_image_load, fragment).commit();
		}
	}
	
	private void addImageBind(){
		BaseFragment fragment = new ImageBindFragment();
		
		if (null != fragment) {
			fragmentManager.beginTransaction().add(R.id.ll_image_bind, fragment).commit();
		}
	}
}
