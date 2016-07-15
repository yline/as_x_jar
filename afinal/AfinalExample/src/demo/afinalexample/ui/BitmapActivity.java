package demo.afinalexample.ui;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import demo.afinalexample.R;

public class BitmapActivity extends Activity implements OnClickListener{
	private String picURL = "http://h.hiphotos.baidu.com/image/pic/item/63d9f2d3572c11dfcec137f6672762d0f603c2d7.jpg";

	private Button btn_picture;
	private ImageView iv_bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_bitmap);

		initView();
		initData();
	}

	private void initView(){
		btn_picture = (Button) this.findViewById(R.id.btn_bitmap_picture);
		iv_bitmap = (ImageView) this.findViewById(R.id.iv_bitmap);
	}

	private void initData(){
		btn_picture.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		FinalBitmap finalBitmap = FinalBitmap.create(this);

		// ������Խ�������ʮ��������ã�Ҳ���Բ������ã�����֮��������init()����(˽��,����������Ⱑ)
		finalBitmap.configBitmapLoadThreadSize(2);

		finalBitmap.display(iv_bitmap, picURL);
	}
}


















