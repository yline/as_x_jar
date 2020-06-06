package com.yline.framework.router

import android.os.Bundle
import android.os.PersistableBundle
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.yline.base.BaseActivity

class FunctionSchemeActivity : BaseActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onCreate(savedInstanceState, persistentState)

        val uri = intent.data
        if (null == uri) {
            finish()
            return
        }

        ARouter.getInstance()
                .build(uri)
                .navigation(this, object : NavCallback() {
                    override fun onArrival(postcard: Postcard?) {
                        finish()
                    }
                })
    }
}