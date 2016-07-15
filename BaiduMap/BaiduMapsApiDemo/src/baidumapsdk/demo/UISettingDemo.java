package baidumapsdk.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.UiSettings;

/**
 * 演示地图UI控制功能
 */
public class UISettingDemo extends Activity {

	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private UiSettings mUiSettings;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_uisetting);

		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mUiSettings = mBaiduMap.getUiSettings();

		MapStatus ms = new MapStatus.Builder().overlook(-30).build();
		MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(ms);
		mBaiduMap.animateMapStatus(u, 1000);
	}

	/**
	 * 是否启用缩放手势
	 * 
	 * @param v
	 */
	public void setZoomEnable(View v) {
		mUiSettings.setZoomGesturesEnabled(((CheckBox) v).isChecked());
	}

	/**
	 * 是否启用平移手势
	 * 
	 * @param v
	 */
	public void setScrollEnable(View v) {
		mUiSettings.setScrollGesturesEnabled(((CheckBox) v).isChecked());
	}

	/**
	 * 是否启用旋转手势
	 * 
	 * @param v
	 */
	public void setRotateEnable(View v) {
		mUiSettings.setRotateGesturesEnabled(((CheckBox) v).isChecked());
	}

	/**
	 * 是否启用俯视手势
	 * 
	 * @param v
	 */
	public void setOverlookEnable(View v) {
		mUiSettings.setOverlookingGesturesEnabled(((CheckBox) v).isChecked());
	}

	/**
	 * 是否启用指南针图层
	 * 
	 * @param v
	 */
	public void setCompassEnable(View v) {
		mUiSettings.setCompassEnabled(((CheckBox) v).isChecked());
	}
	/**
     * 是否显示底图默认标注
     * 
     * @param v
     */
	public void setMapPoiEnable(View v) {
	    mBaiduMap.showMapPoi(((CheckBox) v).isChecked());
	}
	@Override
	protected void onPause() {
		// MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		mMapView.onDestroy();
		super.onDestroy();
	}

}
