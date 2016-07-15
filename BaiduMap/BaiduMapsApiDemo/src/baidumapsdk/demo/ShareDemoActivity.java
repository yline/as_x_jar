package baidumapsdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.share.LocationShareURLOption;
import com.baidu.mapapi.search.share.OnGetShareUrlResultListener;
import com.baidu.mapapi.search.share.PoiDetailShareURLOption;
import com.baidu.mapapi.search.share.ShareUrlResult;
import com.baidu.mapapi.search.share.ShareUrlSearch;

/**
 * 演示poi搜索功能
 */
public class ShareDemoActivity extends Activity implements
		OnGetPoiSearchResultListener, OnGetShareUrlResultListener,
		OnGetGeoCoderResultListener, BaiduMap.OnMarkerClickListener {

	private MapView mMapView = null;
	private PoiSearch mPoiSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private ShareUrlSearch mShareUrlSearch = null;
	private GeoCoder mGeoCoder = null;
	// 保存搜索结果地址
	private String currentAddr = null;
	// 搜索城市
	private String mCity = "北京";
	// 搜索关键字
	private String searchKey = "餐馆";
	// 反地理编译点坐标
	private LatLng mPoint = new LatLng(40.056878, 116.308141);
	private BaiduMap mBaiduMap = null;
	private Marker mAddrMarker = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_demo_activity);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		mShareUrlSearch = ShareUrlSearch.newInstance();
		mShareUrlSearch.setOnGetShareUrlResultListener(this);
		mGeoCoder = GeoCoder.newInstance();
		mGeoCoder.setOnGetGeoCodeResultListener(this);
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mMapView.onDestroy();
		mPoiSearch.destroy();
		mShareUrlSearch.destroy();
		super.onDestroy();
	}

	public void sharePoi(View view) {
		// 发起poi搜索
		mPoiSearch.searchInCity((new PoiCitySearchOption()).city(mCity)
				.keyword(searchKey));
		Toast.makeText(this, "在" + mCity + "搜索 " + searchKey,
				Toast.LENGTH_SHORT).show();
	}

	public void shareAddr(View view) {
		// 发起反地理编码请求
		mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(mPoint));
		Toast.makeText(
				this,
				String.format("搜索位置： %f，%f", mPoint.latitude, mPoint.longitude),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onGetPoiResult(PoiResult result) {

		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(ShareDemoActivity.this, "抱歉，未找到结果",
					Toast.LENGTH_LONG).show();
			return;
		}
		mBaiduMap.clear();
		PoiShareOverlay poiOverlay = new PoiShareOverlay(mBaiduMap);
		mBaiduMap.setOnMarkerClickListener(poiOverlay);
		poiOverlay.setData(result);
		poiOverlay.addToMap();
		poiOverlay.zoomToSpan();
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {

	}

	@Override
	public void onGetPoiDetailShareUrlResult(ShareUrlResult result) {

		// 分享短串结果
		Intent it = new Intent(Intent.ACTION_SEND);
		it.putExtra(Intent.EXTRA_TEXT, "您的朋友通过百度地图SDK与您分享一个位置: " + currentAddr
				+ " -- " + result.getUrl());
		it.setType("text/plain");
		startActivity(Intent.createChooser(it, "将短串分享到"));

	}

	@Override
	public void onGetLocationShareUrlResult(ShareUrlResult result) {

		// 分享短串结果
		Intent it = new Intent(Intent.ACTION_SEND);
		it.putExtra(Intent.EXTRA_TEXT, "您的朋友通过百度地图SDK与您分享一个位置: " + currentAddr
				+ " -- " + result.getUrl());
		it.setType("text/plain");
		startActivity(Intent.createChooser(it, "将短串分享到"));

	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(ShareDemoActivity.this, "抱歉，未找到结果",
					Toast.LENGTH_LONG).show();
			return;
		}
		mBaiduMap.clear();
		mBaiduMap.setOnMarkerClickListener(this);
		mAddrMarker = (Marker) mBaiduMap.addOverlay(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_marka))
				.title(result.getAddress()).position(result.getLocation()));

	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		if (marker == mAddrMarker) {
			mShareUrlSearch
					.requestLocationShareUrl(new LocationShareURLOption()
							.location(marker.getPosition()).snippet("测试分享点")
							.name(marker.getTitle()));
		}
		return true;
	}

	/**
	 * 使用PoiOverlay 展示poi点，在poi被点击时发起短串请求.
	 */
	private class PoiShareOverlay extends PoiOverlay {

		public PoiShareOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public boolean onPoiClick(int i) {
			PoiInfo info = getPoiResult().getAllPoi().get(i);
			currentAddr = info.address;
			mShareUrlSearch
					.requestPoiDetailShareUrl(new PoiDetailShareURLOption()
							.poiUid(info.uid));
			return true;
		}
	}
}
