package com.yline.router

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.yline.application.SDKConfig
import com.yline.application.SDKManager
import com.yline.utils.LogUtil

/**
 * created on 2020-06-06 -- 14:35
 * @author yline
 */
class MainApplication : Application() {

    companion object {
        private fun initARouter(
            application: Application,
            isDebug: Boolean
        ) {
            val startTime = System.currentTimeMillis()

            if (isDebug) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
                ARouter.openLog()     // 打印日志
                ARouter.openDebug()   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)

                // ARouter.printStackTrace(); // 打印日志的时候打印线程堆栈
            }

            ARouter.init(application) // 尽可能早，推荐在Application中初始化

            LogUtil.v("initARouter cost:" + (System.currentTimeMillis() - startTime) + "ms")
        }
    }

    override fun onCreate() {
        super.onCreate()

        SDKManager.init(this, SDKConfig())

        initARouter(this, true)
    }

}