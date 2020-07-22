package com.rxjava

import android.os.Bundle
import android.view.View
import com.rxjava.demo.BasicUtil
import com.rxjava.helper.SimpleSubscriber

import com.yline.log.LogUtil
import com.yline.test.BaseTestActivity
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers

import java.util.concurrent.TimeUnit

/**
 * 借鉴：https://juejin.im/user/573dba2171cfe448aa97b7b0
 * @author yline 2017/10/25 -- 20:55
 * @version 1.0.0
 */
class MainActivity : BaseTestActivity() {

    override fun testStart(view: View, savedInstanceState: Bundle?) {
        addButton("testSubscribe", View.OnClickListener {
            BasicUtil.testSubscribe()
        })

        addButton("testConsumer", View.OnClickListener {
            BasicUtil.testConsumer()
        })

        addButton("testThreadSwitch", View.OnClickListener {
            BasicUtil.testThreadSwitch()
        })

        addButton("testMap", View.OnClickListener {
            BasicUtil.testMap()
        })

        addButton("testFlatMap", View.OnClickListener {
            BasicUtil.testFlatMap()
        })

        addButton("testZip", View.OnClickListener {
            BasicUtil.testZip()
        })


        /////////////// 实际问题
        addTextView("")
        addTextView("")
        addTextView("")
        addButton("intervalRange", View.OnClickListener {
            intervalRange(3000, null)
        })
    }

    /**
     * 输出：
     *
     * LogUtil -> remain = 3000
     * LogUtil -> remain = 2500
     * LogUtil -> remain = 2000
     * LogUtil -> remain = 1500
     * LogUtil -> remain = 1000
     * LogUtil -> remain = 500
     * LogUtil -> remain = 0
     * LogUtil -> remain = -500
     *
     */
    private fun intervalRange(delay: Long, finishCallback: (() -> Unit)?) {
        val count = (delay / 500) + 2
        val disposable = Flowable.intervalRange(0, count, 50, 500, TimeUnit.MILLISECONDS)
                .map { remain ->
                    delay - remain * 500
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : SimpleSubscriber<Long>() {
                    override fun onResult(success: Boolean, remain: Long?) {
                        remain?.let {

                            LogUtil.v("remain = $it")
                            if (it <= 0L) {
                                finishCallback?.invoke()
                            }
                        }
                    }
                })
        disposable.onComplete()
    }
}
