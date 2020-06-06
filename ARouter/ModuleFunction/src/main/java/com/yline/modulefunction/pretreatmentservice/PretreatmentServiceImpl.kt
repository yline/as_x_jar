package com.yline.modulefunction.pretreatmentservice

import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.PretreatmentService
import com.yline.utils.LogUtil

/**
 * 这个预处理，是所有 navigation 的预处理，有bug, path 有相同时, 就不会按照原有逻辑跳转
 * created on 2020-06-06 -- 20:06
 * @author yline
 */
@Route(path = "/function/function____________noduble")
class PretreatmentServiceImpl : PretreatmentService {
    override fun onPretreatment(
        context: Context?,
        postcard: Postcard?
    ): Boolean {
        LogUtil.v("预处理完成")

        // 跳转前预处理，如果需要自行处理跳转，该方法返回 false 即可

        // true -> 如果 path 相同, 则不会跳转，不同会按照原有逻辑跳转
        // false -> 所有情况，都不会按照原有逻辑跳转

        return true
    }

    override fun init(context: Context?) {
        LogUtil.v("init!!!")
    }

}