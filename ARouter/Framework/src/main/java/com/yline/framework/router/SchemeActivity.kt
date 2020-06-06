package com.yline.framework.router

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.yline.base.BaseActivity
import com.yline.utils.LogUtil

/**
 * created on 2020-06-06 -- 15:05
 * @author yline
 */
class SchemeActivity : BaseActivity() {

    companion object {
        private fun compatUrl(intent: Intent): Uri? {
            val uri = intent.data
            LogUtil.v("scheme uri = " + (uri ?: "null"))

            if (null == uri) {
                return null
            }

            val path = uri.path ?: return null

            if (!path.startsWith('/')) {
                return Uri.parse("/function/b")
            }

            return Uri.parse(path);
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uri = compatUrl(intent)
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