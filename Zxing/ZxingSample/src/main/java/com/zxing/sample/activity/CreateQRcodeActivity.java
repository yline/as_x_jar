package com.zxing.sample.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yline.base.BaseActivity;
import com.zxing.qrscan.CreateQRcode;
import com.zxing.sample.R;

/**
 * Created by yline on 2016/10/10.
 */
public class CreateQRcodeActivity extends BaseActivity
{
	private Button bt_back;

	private ImageView img_qrcode;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_qrcode);

		bt_back = (Button) findViewById(R.id.btn_back);
		img_qrcode = (ImageView) findViewById(R.id.iv_qrcode);

		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("二维码");

		Bundle bundle = getIntent().getExtras();
		String phone = bundle.getString("phone");

		CreateQRcode.CreateQRcodeOn(phone, img_qrcode);

		bt_back.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
	}
}
