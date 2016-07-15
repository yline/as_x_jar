package demo.afinalexample.ui;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import demo.afinalexample.R;

/**
 * 注入 点击事件
 * 1,	点击事件必须是 public类型的
 * 2,	Fragment 中使用的是	FinalActivity.initInjectedView(this,mContainer);
 */
public class InnerActivity extends Activity {
	
	@ViewInject(id= R.id.btn_inner_activity,click = "click")
	TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_inner);
		
		FinalActivity.initInjectedView(this);	// 可以没有，但这是IOC注入的前提
	}

	public void click(View v){
		Toast.makeText(this,"this is a  viewInject example ",Toast.LENGTH_LONG).show();
	}
}
