package com.yline.modulefunction.degradeservice

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import com.alibaba.android.arouter.exception.HandlerException
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.DegradeService
import com.alibaba.android.arouter.launcher.ARouter
import com.yline.utils.LogUtil

/**
 * 这里是处理异常逻辑
 *
 * created on 2020-06-06 -- 19:30
 * @author yline
 */
@Route(path = "/function/service_a")
class DegradeServiceImpl : DegradeService {
    private val TAG = DegradeServiceImpl::class.java.simpleName

    override fun onLost(
        context: Context,
        postcard: Postcard
    ) {
        LogUtil.v("")
        
        val raw: String?
        if (postcard.uri != null) {
            val uri = postcard.uri
            raw = uri.getQueryParameter("raw")
            if ("true" == uri.getQueryParameter("debuggable")) {
                ARouter.openDebug()
            }
        } else {
            val uri = Uri.parse(postcard.path)
            raw = uri.getQueryParameter("raw")
            if ("true" == uri.getQueryParameter("debuggable")) {
                ARouter.openDebug()
            }
        }
        if (!TextUtils.isEmpty(raw)) {
            val rawUri = Uri.parse(raw)
            val uri = transform(postcard, context, rawUri)
            if (uri != null) {
                val path = uri.path
                if ("xiaoxingqiu" == postcard.group) {
                    try {
                        postcard.group = extractGroup(path)
                    } catch (e: HandlerException) {
                        Log.e(TAG, e.message)
                    }

                }
                postcard.path = path
                postcard.uri = uri
                postcard.navigation(context)
                Log.d(TAG, String.format("转换URL，从\n%s\n转换到\n%s\n", raw, uri.toString()))
            } else {
                Log.d(TAG, String.format("不通过ARouter跳转:%s", raw))
            }
        }
    }

    override fun init(context: Context) {
        LogUtil.v("")
    }

    private fun transform(
        postcard: Postcard,
        context: Context,
        uri: Uri
    ): Uri? {
        try {
            val scheme = uri.scheme

            val host = uri.host

            if ("bixin" == scheme || "xiaoxingqiu" == scheme || "yupaopao" == scheme) {
                if ("npage" == host) {
                    // 按新的协议规范，不需要处理
                    return uri
                }
                if ("webpage" == host) {
                    // H5页面
                    val isFullScreen = uri.getQueryParameter("isHideNavigation")
                    if ("game" == uri.getQueryParameter("type")) {
                        postcard.withBoolean("iscanshare", true)
                        postcard.withInt("0", 0)
                    }
                    postcard.withBoolean("hiddenNavigation", "1" == isFullScreen)
                    return uri.buildUpon()
                            .path("/webpage/entry")
                            .build()
                }

                if ("plugin" == host || "yupaopao.com" == host) {
                    // 新老Scheme映射
                    val path = mapToOldRule(postcard, context, uri)
                    return if (TextUtils.isEmpty(path)) {
                        null
                    } else uri.buildUpon().path(path).build()
                }
            }
            if ("http" == scheme || "https" == scheme) {
                return uri.buildUpon()
                        .scheme("xiaoxingqiu")
                        .authority("npage")
                        .path("/webpage/entry")
                        .appendQueryParameter("url", uri.toString())
                        .build()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return uri
    }

    private fun mapToOldRule(
        postcard: Postcard,
        context: Context,
        uri: Uri
    ): String? {
        var action = uri.path
        if (TextUtils.isEmpty(action)) {
            action = uri.host
        }
        if (TextUtils.isEmpty(action)) {
            action = ""
        }
        when (action) {
            "/loginPlugin" -> return "/login/entry"
            "/mainPlugin" -> {
                val isFromLogin = uri.getBooleanQueryParameter("isFromLogin", false)
                postcard.withBoolean("isFromLogin", isFromLogin)
                return "/main/entry"
            }
//            "/LivePlugin" -> {
//                postcard.withString("liveId", SchemeUtil.Companion.getValue(uri, "liveId"))
//                postcard.withString("liveRoomId", SchemeUtil.Companion.getValue(uri, "liveRoomId"))
//                postcard.withString("anchorId", SchemeUtil.Companion.getValue(uri, "anchorId"))
//                postcard.withString("cover", SchemeUtil.Companion.getValue(uri, "anchorAvatar"))
//                postcard.withInt(
//                        "direction", if (SchemeUtil.Companion.getBoolenValue(uri, "landscape"))
//                    LiveTemplateType.KEY_TEMPLATE_OF_PC
//                else
//                    LiveTemplateType.KEY_TEMPLATE_OF_ORDINARY
//                )
//                return "/live/entry"
//            }
            "/Userdetail" -> {
                postcard.withString("uid", uri.getQueryParameter("uid"))
                return "/userCenter/personal/detail"
            }
//            "/MessagePlugin", "/MsgPlugin" -> {
//                val p2pChatExt = P2PChatExt()
//                p2pChatExt.setContactId(uri.getQueryParameter("fromAccId"))
//                p2pChatExt.setUserId(uri.getQueryParameter("fromUserId"))
//                p2pChatExt.setAvatar(uri.getQueryParameter("fromAvatar"))
//                p2pChatExt.setName(uri.getQueryParameter("fromName"))
//                p2pChatExt.setUid(uri.getQueryParameter("fromUid"))
//
//                val bundle = Bundle()
//                bundle.putSerializable("p2pChatExt", p2pChatExt)
//                postcard.with(bundle)
//                return "/p2p/entry"
//            }

            "/NotificationPlugin" -> {
                val type = uri.getQueryParameter("type")
                if ("1" == type) {
                    return "/notify/system"
                } else if ("2" == type) {
                    return "/notify/interact"
                } else if ("3" == type) {
                    return "/notify/fansList"
                } else if ("4" == type) {
                    return "/notify/official"
                } else if ("5" == type) {
                    return "/notify/activity"
                } else if ("6" == type) {
                    return "/notify/anchor"
                } else if ("7" == type) {
                    return "/notify/union"
                }
                return "/notify/system"
            }

//            "/LoginPlugin" -> {
//                AccountService.getInstance()
//                        .gotoLogin()
//                return null
//            }
            "/ChatRoomLoader" -> {
                val roomId = uri.getQueryParameter("roomId")
                if (!TextUtils.isEmpty(roomId)) {
                    postcard.withString("ROOM_ID", roomId)
                            .withInt("ENTER_TYPE", 1)
                }
                return "/chatroom/enter"
            }
            else -> return null
        }
    }

    private fun extractGroup(path: String?): String? {
        if (TextUtils.isEmpty(path) || !path!!.startsWith("/")) {
            throw HandlerException(
                    "$TAG: Extract the default group failed, the path must be start with '/' and contain more than 2 '/'!"
            )
        }
        try {
            val defaultGroup = path.substring(1, path.indexOf("/", 1))
            return if (TextUtils.isEmpty(defaultGroup)) {
                throw HandlerException(
                        "$TAG: Extract the default group failed! There's nothing between 2 '/'!"
                )
            } else {
                defaultGroup
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

}