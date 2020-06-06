package com.yline.modulefunction.iprovider

import com.alibaba.android.arouter.facade.template.IProvider

/**
 * created on 2020-06-06 -- 19:54
 * @author yline
 */
interface HelloService : IProvider {
    fun sayHello(name: String): String

}