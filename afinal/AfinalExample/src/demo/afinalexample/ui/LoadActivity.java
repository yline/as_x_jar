package demo.afinalexample.ui;

import java.io.File;
import java.io.FileNotFoundException;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import net.tsz.afinal.http.HttpHandler;
import android.app.Activity;
import android.app.SearchManager.OnCancelListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import demo.afinalexample.R;

/**
 * download
 */
public class LoadActivity extends Activity implements OnClickListener{
	private F21LogTool mlog = new F21LogTool("LoadActivity");
	
	// 2854 k  jpg
	private String fileURL ="http://i8.download.fd.pchome.net/g1/M00/0F/16/oYYBAFVRwgaIDeOfACyXCJ8iJ1MAACehQGv9R0ALJcg813.jpg";
	// 3579 k  png
	//	private String fileURL = "http://i3.download.fd.pchome.net/g1/M00/0F/16/ooYBAFVRwW6IKlyXADfr2NpaFTYAACehAHe54IAN-vw804.jpg";

	private ViewHolder mHolder = new ViewHolder();
	private class ViewHolder{
		Button btn_file;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_load);

		initView();
		initData();
	}

	private void initView(){
		mHolder.btn_file = (Button) this.findViewById(R.id.btn_load_file);
	}

	private void initData(){
		mHolder.btn_file.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_load_file:
			getFileAsync(fileURL);
			break;
		}
	}

	/**
	 * 普通get方法
	 * @param url
	 */
	private void getFile(String url){
		FinalHttp fHttp = new FinalHttp();
		fHttp.get(url, new AjaxCallBack<File>(){

			@Override	//开始http请求的时候回调
			public void onStart() {
				super.onStart();
				mlog.w("onStart");
			}

			@Override	//每1秒钟自动被回调一次
			public void onLoading(long count, long current) {
				super.onLoading(count, current);
				mlog.w("onLoading");
			}

			@Override	//加载失败的时候回调
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				mlog.w("onFailure");
			}

			@Override	//加载成功的时候回调
			public void onSuccess(File t) {
				super.onSuccess(t);
				mlog.w("onSuccess");
			}
		});
	}

	/**
	 * 服务器怎么接收		这个没法测试，出现问题看 下面 链接
	 * http://www.oschina.net/question/105836_85825?fromerr=upsShrOR
	 * @param url
	 */
	private void postFile(String url){
		AjaxParams params = new AjaxParams();
		params.put("username", "f21");
		params.put("password", "123456");
		params.put("email","test@tsz.net");
		try {	// 上传文件
			params.put("profile_picture",new File("/mnt/sdcard/pic.jpg"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			mlog.e("e = " + e);
		}

		// params.put("profile_picture2", inputStream); // 上传数据流
		// params.put("profile_picture3", new ByteArrayInputStream(bytes)); // 提交字节流
		FinalHttp fHttp = new FinalHttp();
		fHttp.post(url, new AjaxCallBack<File>() {

			@Override
			public void onStart() {
				super.onStart();
				mlog.w("onStart");
			}

			@Override  //每1秒钟自动被回调一次
			public void onLoading(long count, long current) {
				super.onLoading(count, current);
				mlog.w("onLoading = " + (100.0 * current/count) + "%");
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				mlog.w("onFailure + " + " t = " + t + " errorNo = " + errorNo + " strMsg = " + strMsg);
			}

			@Override
			public void onSuccess(File t) {
				super.onSuccess(t);
				mlog.w("onSuccess");
			}
		});
	}

	/**
	 * 支持断点续传，随时停止下载任务 或者 开始任务
	 * handler.stop(); 来停止
	 * @param url
	 */
	private void getFileAsync(String url){
		String path = F21SDCardUtils.getSDCardPath() + "test_2854k.jpg";
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
		FinalHttp fHttp = new FinalHttp();
		fHttp.configRequestExecutionRetryCount(1);
		// download 开始
		HttpHandler handler = fHttp.download(url, path,true,new AjaxCallBack<File>() {  // true 支持断点

			@Override	//开始http请求的时候回调
			public void onStart() {
				super.onStart();
				mlog.w("onStart");
			}

			@Override  //每1秒钟自动被回调一次
			public void onLoading(long count, long current) {
				super.onLoading(count, current);
				mlog.w("onLoading = " + (100.0 * current/count) + "%");
			}

			@Override	//加载失败的时候回调
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				mlog.w("onFailure + " + " t = " + t + " errorNo = " + errorNo + " strMsg = " + strMsg);
			}

			@Override	//加载成功的时候回调
			public void onSuccess(File t) {
				super.onSuccess(t);
				mlog.w("onSuccess");
			}
			
			
		});
	}

}




















