package com.yline.modulefunction.pathreplaceservice

import android.content.Context
import android.net.Uri
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.PathReplaceService
import com.yline.utils.LogUtil

/**
 *
 * 这个路径替换，是所有 navigation 的路径替换，有bug, path 有相同时, 就不会按照原有逻辑跳转
 *
 * created on 2020-06-06 -- 20:14
 * @author yline
 */
@Route(path = "/function/function___18234_nodouble")
class PathReplaceServiceImpl : PathReplaceService {
    override fun forString(path: String): String {
        LogUtil.v("path = $path")
        return path;
    }

    override fun forUri(uri: Uri): Uri {
        LogUtil.v("uri = $uri")
        return uri
    }

    override fun init(context: Context?) {
        LogUtil.v("")
    }

}