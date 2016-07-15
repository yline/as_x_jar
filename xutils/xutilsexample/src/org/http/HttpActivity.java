package org.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.base.BaseActivity;
import org.http.download.DownloadService;
import org.http.ioc.BaiduParams;
import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import f21.xutilsexample.R;

@ContentView(R.layout.activity_http)
public class HttpActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * 点击事件
	 * 1. 方法必须私有限定,
	 * 2. 方法以Click或Event结尾, 方便配置混淆编译参数 :
	 * -keepattributes *Annotation*
	 * -keepclassmembers class * {
	 * void *(android.view.View);
	 * *** *Click(...);
	 * *** *Event(...);
	 * }
	 * 3. 方法参数形式必须和type对应的Listener接口一致.
	 * 4. 注解参数value支持数组: value={id1, id2, id3}
	 * 5. 其它参数说明见{@link org.xutils.view.annotation.Event}类的说明.
	 **/
	@Event(value = R.id.btn_usual,type = View.OnClickListener.class)
	private void onGetUsual(View view) {
		LogUtil.v("HttpActivity - onGetUsual - start");

		// 地址：http://appserver.1035.mobi/MobiSoft/Group_List?id=110022&size=12
		RequestParams params = new RequestParams("http://appserver.1035.mobi/MobiSoft/Group_List");
		params.addBodyParameter("id", "110022");
		params.addBodyParameter("size", "12");

		/**
		 * 1. callback的泛型:
		 * callback参数默认支持的泛型类型参见{@link org.xutils.http.loader.LoaderFactory},
		 * 例如: 指定泛型为File则可实现文件下载, 使用params.setSaveFilePath(path)指定文件保存的全路径.
		 * 默认支持断点续传(采用了文件锁和尾端校验续传文件的一致性).
		 * 其他常用类型可以自己在LoaderFactory中注册,
		 * 也可以使用{@link org.xutils.http.annotation.HttpResponse}
		 * 将注解HttpResponse加到自定义返回值类型上, 实现自定义ResponseParser接口来统一转换.
		 * 如果返回值是json形式, 那么利用第三方的json工具将十分容易定义自己的ResponseParser.
		 * 如示例代码{@link org.xutils.sample.http.BaiduResponse}, 可直接使用BaiduResponse作为
		 * callback的泛型.
		 *
		 * 2. callback的组合:
		 * 可以用基类或接口组合个种类的Callback, 见{@link org.xutils.common.Callback}.
		 * 例如:
		 * a. 组合使用CacheCallback将使请求检测缓存或将结果存入缓存(仅GET请求生效).
		 * b. 组合使用PrepareCallback的prepare方法将为callback提供一次后台执行耗时任务的机会,
		 * 然后将结果给onCache或onSuccess.
		 * c. 组合使用ProgressCallback将提供进度回调.
		 * ...(可参考{@link org.xutils.image.ImageLoader}
		 * 或 示例代码中的 {@link org.xutils.sample.download.DownloadCallback})
		 *
		 * 3. 请求过程拦截或记录日志: 参考 {@link org.xutils.http.app.RequestTracker}
		 *
		 * 4. 请求Header获取: 参考 {@link org.xutils.http.app.InterceptRequestListener}
		 *
		 * 5. 其他(线程池, 超时, 重定向, 重试, 代理等): 参考 {@link org.xutils.http.RequestParams}
		 *
		 **/
		Callback.Cancelable cancelable 
		= x.http().get(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String result) {
				Toast.makeText(x.app(), "onGetUsual onSuccess", Toast.LENGTH_SHORT).show();
				LogUtil.v("onGetUsual - result = " + result);
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				LogUtil.e("ex = " + ex);
				if (ex instanceof HttpException) { // 网络错误
					HttpException httpEx = (HttpException) ex;
					int responseCode = httpEx.getCode();
					String responseMsg = httpEx.getMessage();
					String errorResult = httpEx.getResult();
					// ...
				} else { // 其他错误
					// ...
				}
			}

			@Override
			public void onCancelled(CancelledException cex) {
				Toast.makeText(x.app(), "onGetUsual onCancelled", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFinished() {

			}
		});
		// cancelable.cancel(); // 取消
		// 如果需要记录请求的日志, 可使用RequestTracker接口(优先级依次降低, 找到一个实现后会忽略后面的):
		// 1. 自定义Callback同时实现RequestTracker接口;
		// 2. 自定义ResponseParser同时实现RequestTracker接口;
		// 3. 在LoaderFactory注册.
	}

	@Event(value = R.id.btn_ioc,type = View.OnClickListener.class)
	private void onGetIOC(View view) {
		/**
		 * 示例用的是注入是写法
		 * 自定义实体参数类请参考:
		 * 请求注解 {@link org.xutils.http.annotation.HttpRequest}
		 * 请求注解处理模板接口 {@link org.xutils.http.app.ParamsBuilder}
		 *
		 * 需要自定义类型作为callback的泛型时, 参考:
		 * 响应注解 {@link org.xutils.http.annotation.HttpResponse}
		 * 响应注解处理模板接口 {@link org.xutils.http.app.ResponseParser}
		 */
		BaiduParams params = new BaiduParams();
		params.id = "110022";
		params.size = "12";
		// 地址：http://appserver.1035.mobi/MobiSoft/Group_List?id=110022&size=12

		Callback.Cancelable cancelable  
		= x.http().get(params, new Callback.CommonCallback<String>() {

			@Override
			public void onSuccess(String result) {
				Toast.makeText(x.app(), "onGetIOC onSuccess", Toast.LENGTH_SHORT).show();
				LogUtil.v("onGetIOC - result = " + result);
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				LogUtil.e("ex = " + ex);
				if (ex instanceof HttpException) { // 网络错误
					HttpException httpEx = (HttpException) ex;
					int responseCode = httpEx.getCode();
					String responseMsg = httpEx.getMessage();
					String errorResult = httpEx.getResult();
					// ...
				} else { // 其他错误
					// ...
				}
			}

			@Override
			public void onCancelled(CancelledException cex) {
				Toast.makeText(x.app(), "onGetIOC onCancelled", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFinished() {

			}
		});
	}

	/**
	 * 缓存示例, 更复杂的例子参考 {@link org.xutils.image.ImageLoader}
	 */
	@Event(value = R.id.btn_cache,type = View.OnClickListener.class)
	private void onGetCache(View view) {
		// 地址：http://appserver.1035.mobi/MobiSoft/Group_List?id=110022&size=12
		RequestParams params = new RequestParams("http://appserver.1035.mobi/MobiSoft/Group_List");
		params.addBodyParameter("id", "110022");
		params.addBodyParameter("size", "12");

		Callback.Cancelable cancelable  
		// 使用CacheCallback, xUtils将为该请求缓存数据.
		= x.http().get(params, new Callback.CacheCallback<String>() {
			private boolean hasError = false;
			private String result = null;

			@Override
			public boolean onCache(String result) {
				// 得到缓存数据
				//
				// * 客户端会根据服务端返回的 header 中 max-age 或 expires 来确定本地缓存是否给 onCache 方法.
				//   如果服务端没有返回 max-age 或 expires, 那么缓存将一直保存, 除非这里自己定义了返回false的
				//   逻辑, 那么xUtils将请求新数据, 来覆盖它.
				//
				// * 如果信任该缓存返回 true, 将不再请求网络;
				//   返回 false 继续请求网络, 但会在请求头中加上ETag, Last-Modified等信息,
				//   如果服务端返回304, 则表示数据没有更新, 不继续加载数据.
				//
				this.result = result;
				return false; // true: 信任缓存数据; false不信任缓存数据.
			}

			@Override
			public void onSuccess(String result) {
				// 注意: 如果服务返回304或 onCache 选择了信任缓存, 这里将不会被调用,
				// 但是 onFinished 总会被调用.
				this.result = result;
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				hasError = true;
				Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
				if (ex instanceof HttpException) { // 网络错误
					HttpException httpEx = (HttpException) ex;
					int responseCode = httpEx.getCode();
					String responseMsg = httpEx.getMessage();
					String errorResult = httpEx.getResult();
					// ...
				} else { // 其他错误
					// ...
				}
			}

			@Override
			public void onCancelled(CancelledException cex) {

			}

			@Override
			public void onFinished() {
				if (!hasError && result != null) {
					// 成功获取数据
					LogUtil.v("onGetCache - result = " + result);
				}
			}

		});
	}

	// 上传多文件,没有目标就不测试不上传了
	@Event(R.id.btn_post_upload)	
	private void onPostUpload(View view) {
		// 目标地址
		RequestParams params = new RequestParams("http://192.168.0.13:8080/upload");
		// 加到url里的参数, http://xxxx/s?wd=xUtils
		params.addQueryStringParameter("wd", "xUtils");
		// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
		// params.addBodyParameter("wd", "xUtils");

		// 使用multipart表单上传文件
		params.setMultipart(true);
		params.addBodyParameter(
				"file",
				new File("/sdcard/test.jpg"),
				null); // 如果文件没有扩展名, 最好设置contentType参数.

		try {	// 这种方式 抛出异常也不崩溃
			params.addBodyParameter(
					"file2",
					new FileInputStream(new File("/sdcard/test2.jpg")),
					"image/jpeg",
					// 测试中文文件名
					"你+& \" 好.jpg"); // InputStream参数获取不到文件名, 最好设置, 除非服务端不关心这个参数.
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 

		x.http().post(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				Toast.makeText(x.app(), result, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
			}

			@Override
			public void onCancelled(CancelledException cex) {
				Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onFinished() {

			}
		});
	}
	
	@ViewInject(R.id.tv_download)
	private TextView tv_show_path;
	private String show_path_temp = "";

	// 添加到下载列表
	@Event(R.id.btn_download)
	private void testGetServiceLoadClick(View view){
		for (int i = 0; i < 3; i++) {
			String url = "http://www.bz55.com/uploads/allimg/150309/139-150309101A8.jpg";
			String label = i + "_xutils_" + System.nanoTime();
			String savePath = "/sdcard/xUtils/";
			try {
				show_path_temp = savePath + label + ".png";
				
				DownloadService.getDownloadManager().startDownload(url, label,
						show_path_temp, true, false, null);
				
				tv_show_path.append(show_path_temp + "\n");  // 显示地址
			} catch (DbException e) {
				e.printStackTrace();
			}
		}
	}

	// 打开下载列表页
	@Event(R.id.btn_open_download)
	private void testOpenServiceLoadClick(View view){
		Intent intent = new Intent(this, DownloadActivity.class);
		this.startActivity(intent);
	}
}










