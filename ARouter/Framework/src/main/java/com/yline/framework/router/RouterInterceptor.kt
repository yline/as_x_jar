package com.yline.framework.router

import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.yline.utils.LogUtil

@Interceptor(priority = 2, name = "测试使用")
class RouterBBBBInterceptor : IInterceptor {

    override fun process(
        postcard: Postcard,
        callback: InterceptorCallback
    ) {
        LogUtil.v("ARouter process " + postcard.uri)
        callback.onContinue(postcard)
    }

    override fun init(context: Context?) {
        LogUtil.v("ARouter is init")
    }

}