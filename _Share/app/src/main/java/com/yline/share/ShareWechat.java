package com.yline.share;

import android.content.Context;
import android.text.TextUtils;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yline.utils.LogUtil;

/**
 * 微信分享
 *
 * @author yline 2019/3/6 -- 17:21
 */
public class ShareWechat {
    public static void shareText(Context context, String text) {
        new ShareWechat(context).shareTextInner(text);
    }

    private static final int SCENE_FRIEND = SendMessageToWX.Req.WXSceneSession; // 好友
    private static final int SCENE_CIRCLE = SendMessageToWX.Req.WXSceneTimeline; // 朋友圈
    private static final int SCENE_FAVORITE = SendMessageToWX.Req.WXSceneFavorite; // 收藏

    private IWXAPI mWxApi;

    private ShareWechat(Context context) {
        mWxApi = WXAPIFactory.createWXAPI(context, Constants.WECHAT_APP_ID);
        mWxApi.registerApp(Constants.WECHAT_APP_ID);
    }

    private void shareTextInner(String text) {
        if (TextUtils.isEmpty(text)) {
            LogUtil.e("text is null");
            return;
        }

        WXMediaMessage mediaMessage = new WXMediaMessage();
        mediaMessage.mediaObject = new WXTextObject(text);
        mediaMessage.description = text;
        mediaMessage.mediaTagName = "TagName";

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = mediaMessage;
        req.scene = SCENE_FRIEND; // 好友

        mWxApi.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
