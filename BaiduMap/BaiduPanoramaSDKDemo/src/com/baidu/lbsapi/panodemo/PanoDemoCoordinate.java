package com.baidu.lbsapi.panodemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.lbsapi.tools.CoordinateConverter;
import com.baidu.lbsapi.tools.CoordinateConverter.COOR_TYPE;
import com.baidu.lbsapi.tools.Point;

/**
 * 坐标转换
 */
public class PanoDemoCoordinate extends Activity {

	private RadioGroup radioGroup;
	private Button btn,btn_ll2mc;
	private TextView baiduResult,mcResult;
	private EditText input_lat, input_lont;
	// 转换百度坐标系
	Point resultPoint = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.panodemo_coordinate);

		radioGroup = (RadioGroup) findViewById(R.id.panodemo_coordinate_rgroup);
		btn = (Button) findViewById(R.id.panodemo_coordinate_btn);
		btn_ll2mc = (Button)findViewById(R.id.panodemo_ll2mc_btn);
		baiduResult = (TextView) findViewById(R.id.panodemo_coordinate_result);
		mcResult = (TextView) findViewById(R.id.panodemo_ll2mc_result);
		input_lat = (EditText) findViewById(R.id.panodemo_coordinate_input_lat);
		input_lont = (EditText) findViewById(R.id.panodemo_coordinate_input_lont);

		// 测试高德经纬度
		input_lat.setText(39.907687 + "");
		input_lont.setText(116.397539 + "");
		// 测试腾讯经纬度
		// input_lat.setText(39.907741 + "");
		// input_lont.setText(116.397577 + "");
		// 测试Google经纬度
		// input_lat.setText(39.907723 + "");
		// input_lont.setText(116.397543 + "");
		// 测试原始GPS经纬度
		// input_lat.setText(40.040286 + "");
		// input_lont.setText(116.30085 + "");

		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (null == input_lat.getText() || "".equals(input_lat.getText().toString())) {
					Toast.makeText(PanoDemoCoordinate.this, "请输入纬度", Toast.LENGTH_SHORT).show();
					return;
				}
				if (null == input_lont.getText() || "".equals(input_lont.getText().toString())) {
					Toast.makeText(PanoDemoCoordinate.this, "请输入经度", Toast.LENGTH_SHORT).show();
					return;
				}

				// 原始点经纬度
				Point sourcePoint = new Point(Double.valueOf(input_lont.getText().toString()), Double.valueOf(input_lat
						.getText().toString()));

				switch (radioGroup.getCheckedRadioButtonId()) {
				case R.id.panodemo_coordinate_rgaode:
					resultPoint = CoordinateConverter.converter(COOR_TYPE.COOR_TYPE_GCJ02, sourcePoint);
					break;
				case R.id.panodemo_coordinate_rtencent:
					resultPoint = CoordinateConverter.converter(COOR_TYPE.COOR_TYPE_GCJ02, sourcePoint);
					break;
				case R.id.panodemo_coordinate_rgoogle:
					resultPoint = CoordinateConverter.converter(COOR_TYPE.COOR_TYPE_GCJ02, sourcePoint);
					break;
				case R.id.panodemo_coordinate_rgps:
					resultPoint = CoordinateConverter.converter(COOR_TYPE.COOR_TYPE_WGS84, sourcePoint);
					break;
				default:
					break;
				}

				if (resultPoint != null) {
					baiduResult.setText("百度坐标:\nx: " + resultPoint.x + "\ny: " + resultPoint.y);
					btn_ll2mc.setVisibility(View.VISIBLE);
				}
			}
		});
		btn_ll2mc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Point mcPoint = CoordinateConverter.LLConverter2MC(resultPoint.x,resultPoint.y);
				int mercatorX = (int) mcPoint.x;
				int mercatorY = (int) mcPoint.y;
				mcResult.setText("墨卡托坐标:\nx: " + mercatorX + "\ny: " + mercatorY);
			}
		});
	}
}
