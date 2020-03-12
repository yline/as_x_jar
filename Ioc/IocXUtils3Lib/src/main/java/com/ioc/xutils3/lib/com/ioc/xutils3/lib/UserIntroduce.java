package com.ioc.xutils3.lib.com.ioc.xutils3.lib;

/**
 * ioc框架
 * 使用说明
 */
public class UserIntroduce {
    /**
     * 要求:
     * Activity中注入
     * zc.view().inject(this);
     * zc.view().inject(mHolder,this.getWindow().getDecorView());
     *
     * Fragment中注入
     * zc.view().inject(this, this.getView());
     * zc.view().inject(this, inflater, container);
     * zc.view().inject(mHolder, this.getView());
     */

    /**
     * 使用:
     * @ContentView(R.layout.activity_main)
     *
     * @ViewInject(R.id.id_btn)
     * private Button mBtn;
     *
     * @Event(R.id.id_btn)
     * @Event(value={R.id.id_btn,R.id.id_btn02},type=CompoundButton.OnCheckedChangeListener.class)
     * private void CLick(View v){	// 传参一定要与通常的点击事件相同
     * 		ToastUtils.show(getApplicationContext(), "content");
     * }
     */

    /**
     * 是否打log
     * com.lib.ioc.utils包中	LogIoc中
     * isDebug改这个参数即可
     */
}


















