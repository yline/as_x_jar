package com.zxing.sample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;
import com.zxing.qrscan.CaptureActivity;
import com.zxing.sample.R;

public class MainActivity extends BaseAppCompatActivity
{
	private final static int SCANNIN_GREQUEST_CODE = 1;

	private static final String TAG = "QRScan";

	private EditText et_phone;

	private String QRScanResult;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn_scan).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				LogFileUtil.v(TAG, "btn_scan");
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, CaptureActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			}
		});

		et_phone = (EditText) findViewById(R.id.et_phone);
		findViewById(R.id.bt_createqrcode).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if ("".equals(et_phone.getText().toString().trim()))
				{
					MainApplication.toast("输入内容为空");
				}
				else
				{
					CreateQRcodeActivity.actionStart(MainActivity.this, et_phone.getText().toString().trim());
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == SCANNIN_GREQUEST_CODE)
		{
			if (resultCode == RESULT_OK)
			{
				QRScanResult = CaptureActivity.getResult(data);

				LogFileUtil.v(TAG, "QRScanResult = " + QRScanResult);
				MainApplication.toast("QRScanResult = " + QRScanResult);
			}
		}
	}
}
