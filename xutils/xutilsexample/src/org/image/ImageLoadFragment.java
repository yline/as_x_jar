package org.image;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.base.BaseFragment;
import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.common.util.DensityUtil;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.image.ImageOptions.Builder;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import f21.xutilsexample.R;

@ContentView(R.layout.fragment_image_load)
public class ImageLoadFragment extends BaseFragment{
	private String[] imgSites = {
			"http://image.baidu.com/",
			"http://image.baidu.com/",
			"http://www.moko.cc/",
			"http://eladies.sina.com.cn/photo/",
			"http://www.youzi4.com/"
	};
	
	/** 配置信息 */
	private ImageOptions imageOptions;
	
	@ViewInject(R.id.lv_img)
	private ListView imageListView;
	private ImageListAdapter imageListAdapter;
	

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Builder builder = new ImageOptions.Builder();  // 以下可以采用连缀
		builder.setSize(DensityUtil.dip2px(60), DensityUtil.dip2px(60)); // 大小
		builder.setRadius(DensityUtil.dip2px(5));  // 四个角的圆度
		builder.setCrop(true);	// 如果ImageView的大小不是定义为wrap_content, 不要crop.
		builder.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
		builder.setLoadingDrawableId(R.drawable.ic_launcher);	// 加载前图片
		builder.setFailureDrawableId(R.drawable.ic_launcher);	// 加载失败图片
		imageOptions = builder.build();

		imageListAdapter = new ImageListAdapter();
		imageListView.setAdapter(imageListAdapter);

		// 加载url请求返回的图片连接给listview
		// 这里只是简单的示例，并非最佳实践，图片较多时，最好上拉加载更多...
		for (String url : imgSites) {
			loadImgList(url);
		}
	}
	
	@Event(value = R.id.lv_img, type = AdapterView.OnItemClickListener.class)
	private void onImageItemClick(AdapterView<?> parent, View view, int position, long id){
        BigImageActivity.actionStart(getActivity(), imageListAdapter.getItem(position).toString());
	}
	
	/**
	 * 添加网络地址 --> 通知adapter更新  -->  依据Url 加载图片,并监听(结束显示图片)
	 * @param url
	 */
	private void loadImgList(String url) {
		// 异步GET请求
		x.http().get(new RequestParams(url), new CommonCallback<String>() {

			@Override
			public void onSuccess(String result) {
				imageListAdapter.addSrc(getImgSrcList(result));
				imageListAdapter.notifyDataSetChanged();//通知listview更新数据
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				LogUtil.i("loadImgList onError", ex);
			}

			@Override
			public void onCancelled(CancelledException cex) {

			}

			@Override
			public void onFinished() {

			}
		});
	}
	
	/**
	 * 正则并不懂
	 * @param htmlStr http://image.baidu.com/
	 * @return 依据 htmlStr 生产的一列图片Url字符串
	 */
	public static List<String> getImgSrcList(String htmlStr) {
		List<String> pics = new ArrayList<String>();
		String regEx_img = "<img.*?src=\"http://(.*?).jpg\""; // 图片链接地址
		Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
		Matcher m_image = p_image.matcher(htmlStr);
		while (m_image.find()) {
			String src = m_image.group(1);
			if (src.length() < 100) {
				pics.add("http://" + src + ".jpg");
				LogUtil.v("http://" + src + ".jpg");  // 有兴趣可以看log 查看相应地址
			}
		}
		
		return pics;
	}

	/**
	 * 适配器
	 */
	private class ImageListAdapter extends BaseAdapter{
		private final LayoutInflater mInflater;
		private ArrayList<String> imgSrcList;

		public ImageListAdapter() {
			super();
			mInflater = LayoutInflater.from(getActivity());  // getContext()
			imgSrcList = new ArrayList<String>();
		}

		public void addSrc(List<String> imgSrcList) {
			this.imgSrcList.addAll(imgSrcList);
		}

		public void addSrc(String imgUrl) {
			this.imgSrcList.add(imgUrl);
		}

		@Override
		public int getCount() {  // 要求setAdapter之前必须new
			return imgSrcList.size();
		}

		@Override
		public Object getItem(int position) {
			return imgSrcList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageItemHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.fragment_image_load_item, null);
				holder = new ImageItemHolder();
				x.view().inject(holder, convertView);
				convertView.setTag(holder);
			} else {
				holder = (ImageItemHolder) convertView.getTag();
			}

			holder.imgPb.setProgress(0);
			x.image().bind(holder.imgItem,
					imgSrcList.get(position),
					imageOptions,
					new CustomBitmapLoadCallBack(holder));   // 加载图片,并监听(结束显示图片)
			return convertView;
		}
	}

	private class ImageItemHolder{
		@ViewInject(R.id.img_item)
		private ImageView imgItem;

		@ViewInject(R.id.img_pb)
		private ProgressBar imgPb;
	}

	/**
     * 更新 加载条
     */
    public class CustomBitmapLoadCallBack implements Callback.ProgressCallback<Drawable> {
        private final ImageItemHolder holder;

        public CustomBitmapLoadCallBack(ImageItemHolder holder) {
            this.holder = holder;
        }

        @Override
        public void onWaiting() {
            this.holder.imgPb.setProgress(0);
        }

        @Override
        public void onStarted() {

        }

        @Override	// ing
        public void onLoading(long total, long current, boolean isDownloading) {
            this.holder.imgPb.setProgress((int) (current * 100 / total));
        }

        @Override	// end
        public void onSuccess(Drawable result) {
            this.holder.imgPb.setProgress(100);
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
        }

        @Override
        public void onCancelled(CancelledException cex) {

        }

        @Override
        public void onFinished() {

        }
    }
}


































