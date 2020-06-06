package com.yline.router

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.callback.NavCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.yline.modulefunction.iprovider.HelloService
import com.yline.test.BaseTestActivity
import com.yline.utils.LogUtil

/**
 * created on 2020-06-06 -- 14:35
 * @author yline
 */
@Route(path = "/main/test")
class MainActivity : BaseTestActivity() {

    override fun testStart(
        view: View?,
        savedInstanceState: Bundle?
    ) {
        ARouter.getInstance()
                .inject(this)

        addButton("url 简单") {
            ARouter.getInstance()
                    .build("/function/a")
                    .navigation(this)
        }

        addButton("url 带参数") {
            ARouter.getInstance()
                    .build("/function/a")
                    .withString("name", "yline")
                    .withInt("age", 26)
                    .navigation()
        }

        addButton("url kotlin") {
            ARouter.getInstance()
                    .build("/function/c")
                    .navigation(this)
        }

        // MainActivity -> SchemeActivity -> FunctionAActivity
        addButton("scheme 协议跳转") {
            val intent = Intent(Intent.ACTION_VIEW, null)
            intent.data = Uri.parse("yline://com.arouter" + "/function/a")

            if (null != intent.resolveActivity(packageManager)) {
                startActivity(intent)
            } else {
                LogUtil.e("target not exist")
            }
        }

        // 拦截器 查看 Framework -> com.yline.framework.router -> RouterInterceptor

        addButton("url 处理跳转结果") {
            ARouter.getInstance()
                    .build("/function/a")
                    .navigation(this, object : NavCallback() {
                        override fun onFound(postcard: Postcard?) {
                            LogUtil.v("")
                        }

                        override fun onInterrupt(postcard: Postcard?) {
                            LogUtil.v("")
                        }

                        override fun onLost(postcard: Postcard?) {
                            LogUtil.v("")
                        }

                        override fun onArrival(postcard: Postcard?) {
                            LogUtil.v("")
                        }

                    })
        }

        // 处理 路由找不到的情况，异常容错处理； 非异常情况，只会执行 init, 不会执行 onLost
        addButton("DegradeService「降级容错」") {
            ARouter.getInstance()
                    .build("/function/service_a")
                    .navigation(this, object : NavCallback() {
                        override fun onLost(postcard: Postcard?) {
                            LogUtil.v("")
                        }

                        override fun onArrival(postcard: Postcard?) {

                        }

                    })
        }

        // 代理模式呀
        addButton("IProvider 注入服务") {
            val helloServiceA = ARouter.getInstance()
                    .build("/function/provider_a")
                    .navigation()
            if (helloServiceA is HelloService) {
                LogUtil.v("helloServiceB = ${helloServiceA.sayHello("dahui")}")
            } else {
                LogUtil.e("helloServiceA is bad")
            }

            val helloServiceB = ARouter.getInstance()
                    .navigation(HelloService::class.java)
            if (null != helloServiceB) {
                LogUtil.v("helloServiceB = ${helloServiceB.sayHello("alun")}")
            } else {
                LogUtil.e("helloServiceB is bad")
            }
        }

        addButton("url + 预处理服务") {
            // 逻辑实现在 ModuleFunction -> com.yline.modulefunction.pretreatmentservice -> PretreatmentServiceImpl
            ARouter.getInstance()
                    .build("/function/b")
                    .navigation(this)
        }

    }

}