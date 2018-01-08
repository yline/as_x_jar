package com.bugly

import android.os.Bundle
import android.view.View
import com.tencent.bugly.crashreport.BuglyLog
import com.yline.test.BaseTestActivity

class MainActivity : BaseTestActivity() {
    override fun testStart(view: View?, savedInstanceState: Bundle?) {
        addButton("NPE") {
            BuglyLog.v("xxx-", "v") // 01-08 11:59:03.646 18312-18312/com.bugly V/xxx-: v; 日志内容很少
            BuglyLog.d("xxx-", "d")
            BuglyLog.i("xxx-", "i")
            BuglyLog.w("xxx-", "w")
            BuglyLog.e("xxx-", "e")

            val nullStr: String? = null
            nullStr!!.toString()
        }
    }
}
