package org.image;

import java.io.File;

import org.base.BaseFragment;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.common.util.LogUtil;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import f21.xutilsexample.R;

@ContentView(R.layout.fragment_image_bind)
public class ImageBindFragment extends BaseFragment{

	@ViewInject(R.id.iv_bind)
	private ImageView bindIv;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		//		gifBind();
		//		localBind("/storage/emulated/0/DCIM/Camera/IMG20150728192757.jpg");
		//		netBind();
		netLocalBind();
	}

	// assets file
	private void gifBind(){
		ImageOptions options;

		options = new ImageOptions.Builder()
		.setLoadingDrawableId(R.drawable.ic_launcher)
		.setFailureDrawableId(R.drawable.ic_launcher)
		.setIgnoreGif(false)
		.build();

		x.image().bind(bindIv, "assets://test.gif", options);
	}

	// local file	传入路径
	private void localBind(String path){
		ImageOptions options;

		options = new ImageOptions.Builder().build();

		x.image().bind(bindIv, path, options);
	}

	/**
	 * 本地的话，就不会复制过去的
	 */
	private void netLocalBind(){
		ImageOptions options;

		options = new ImageOptions.Builder().build();

		x.image().loadFile("/storage/emulated/0/DCIM/Camera/IMG20150728192757.jpg",
				options, new CommonCallback<File>() {

			@Override
			public void onSuccess(File result) {
				
			}

			@Override
			public void onFinished() {

			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {

			}

			@Override
			public void onCancelled(CancelledException cex) {

			}
		});
	}

	// 网络图片
	// /storage/emulated/0/Android/data/f21.xutilsexample/cache/xUtils_img/3e98a798735370e17a02436732acc656 下载的地址
	// http://f.hiphotos.baidu.com/image/pic/item/faf2b2119313b07e97f760d908d7912396dd8c9c.jpg
	private void netBind() {
		ImageOptions options;
		options = new ImageOptions.Builder().build();

		x.image().loadFile("http://f.hiphotos.baidu.com/image/pic/item/faf2b2119313b07e97f760d908d7912396dd8c9c.jpg",
				options, new CommonCallback<File>() {

			@Override
			public void onSuccess(File result) {
				LogUtil.i(result.getPath());
			}

			@Override
			public void onFinished() {

			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {

			}

			@Override
			public void onCancelled(CancelledException cex) {

			}
		});
	}

	/*
	options = new ImageOptions.Builder()	// 所有单位都是px
	.setLoadingDrawableId(R.drawable.ic_launcher)
	.setFailureDrawableId(R.drawable.ic_launcher)

	.setSize(100, 100) 	// 小于0时不采样压缩. 等于0时自动识别ImageView的宽高和(maxWidth, maxHeight)
	.setCrop(true) 
	.setRadius(50) 
	.setSquare(true)
	.setCircular(true)

	.setAutoRotate(true)
	.setUseMemCache(true)

	.setIgnoreGif(false)
	.build();
	 */
}






















