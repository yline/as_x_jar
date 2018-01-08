package com.bugly

import android.app.Application
import com.yline.application.SDKConfig
import com.yline.application.SDKManager

/**
 * 程序入口
 * @author yline 2018/1/8 -- 10:28
 * @version 1.0.0
 */
class IApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        SDKManager.init(this, SDKConfig())

        BuglyConfig.initConfig(this)
    }
}