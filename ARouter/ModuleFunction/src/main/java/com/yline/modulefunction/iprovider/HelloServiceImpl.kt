package com.yline.modulefunction.iprovider

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.yline.utils.LogUtil

@Route(path = "/function/provider_a")
class HelloServiceImpl : HelloService {
    override fun sayHello(name: String): String {
        return "$name say: the girl is so beautiful"
    }

    override fun init(context: Context?) {
        LogUtil.v("init!!!")
    }

}