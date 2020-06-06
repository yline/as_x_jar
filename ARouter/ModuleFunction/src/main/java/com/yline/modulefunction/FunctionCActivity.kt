package com.yline.modulefunction

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.yline.test.BaseTestActivity

@Route(path = "/function/c")
class FunctionCActivity : BaseTestActivity() {
    override fun testStart(
        view: View?,
        savedInstanceState: Bundle?
    ) {
        addTextView("function c")
    }

}