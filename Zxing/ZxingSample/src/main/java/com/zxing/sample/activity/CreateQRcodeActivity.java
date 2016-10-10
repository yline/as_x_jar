package com.zxing.sample.activity;

import android.content.Context;
import android.content.Intent;
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
	private static final String KEY_PHONE = "phone";

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

		String phone = getPhoneNumber();
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

	/**
	 * 带数据跳转
	 * @param context
	 * @param phoneNumber
	 */
	public static void actionStart(Context context, String phoneNumber)
	{
		Intent intent = new Intent();
		intent.setClass(context, CreateQRcodeActivity.class);

		Bundle bundle = new Bundle();
		bundle.putString(KEY_PHONE, phoneNumber);

		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	/**
	 * 获取跳转过来的数据
	 * @return
	 */
	private String getPhoneNumber()
	{
		Bundle bundle = getIntent().getExtras();
		return bundle.getString(KEY_PHONE);
	}
}
