package net.sourceforge.simcpux.yline;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yline.utils.LogUtil;

import net.sourceforge.simcpux.Constants;
import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.pool.ThreadPool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 微信分享
 *
 * @author yline 2019/3/6 -- 17:21
 */
public class ShareWechat {
    private static final int DEFAULT_QUALITY = 80; // 采样率
    private static final int THUMB_SIZE = 150;  // 取缩略图[大小]

    private static final int SCENE_FRIEND = SendMessageToWX.Req.WXSceneSession; // 好友
    private static final int SCENE_CIRCLE = SendMessageToWX.Req.WXSceneTimeline; // 朋友圈
    private static final int SCENE_FAVORITE = SendMessageToWX.Req.WXSceneFavorite; // 收藏

    public static void shareText2Friend(Context context, @NonNull String text) {
        new ShareWechat(context).sendText(text, SCENE_FRIEND);
    }

    public static void shareWebpage2Friend(Context context, @NonNull String webpageUrl, @NonNull String title, String description, @NonNull String picUrl) {
        new ShareWechat(context).sendWebpage(context, webpageUrl, title, description, picUrl, SCENE_FRIEND);
    }

    public static void shareWebpage2Circle(Context context, @NonNull String webpageUrl, @NonNull String title, String description, @NonNull String picUrl) {
        new ShareWechat(context).sendWebpage(context, webpageUrl, title, description, picUrl, SCENE_CIRCLE);
    }

    public static void shareImage2Friend(Context context, @NonNull String imagePath) {
        new ShareWechat(context).sendImage(imagePath, SCENE_FRIEND);
    }

    public static void shareImage2Circle(Context context, @NonNull String imagePath) {
        new ShareWechat(context).sendImage(imagePath, SCENE_CIRCLE);
    }

    private IWXAPI mWxApi;

    private ShareWechat(Context context) {
        mWxApi = WXAPIFactory.createWXAPI(context, Constants.WECHAT_APP_ID);
        mWxApi.registerApp(Constants.WECHAT_APP_ID);
    }

    private void sendWebpage(final Context context, final String webpageUrl, final String title, final String description, final String picUrl, final int scene) {
        if (TextUtils.isEmpty(webpageUrl) || TextUtils.isEmpty(title)) {
            LogUtil.v("分享内容有误，缺少必要信息，webpageUrl = " + webpageUrl + ", title = " + title);
            return;
        }

        if (!mWxApi.isWXAppInstalled()) {
            LogUtil.v("未安装微信或者微信版本太低");
            return;
        }

        if (!TextUtils.isEmpty(picUrl)) {
            ThreadPool.fixedThreadExecutor(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap;
                    try {
                        URLConnection urlConnection = new URL(picUrl).openConnection();
                        urlConnection.setConnectTimeout(3000);
                        urlConnection.setReadTimeout(3000);
                        bitmap = BitmapFactory.decodeStream(urlConnection.getInputStream());
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
                    }

                    if (null != bitmap) {
                        Bitmap thumbBitmap = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
                        byte[] thumbBytes = compressToByte(thumbBitmap, Bitmap.CompressFormat.JPEG, 30_000);
                        sendWebpageInner(mWxApi, webpageUrl, title, description, thumbBytes, scene);
                    } else { // 无图片的网页分享
                        sendWebpageInner(mWxApi, webpageUrl, title, description, null, scene);
                    }
                }
            });
        } else { // 无图片的网页分享
            sendWebpageInner(mWxApi, webpageUrl, title, description, null, scene);
        }
    }

    private void sendImage(final String imagePath, final int scene) {
        if (TextUtils.isEmpty(imagePath)) {
            LogUtil.v("分享内容有误，缺少必要信息，imagePath is null");
            return;
        }

        if (!mWxApi.isWXAppInstalled()) {
            LogUtil.v("未安装微信或者微信版本太低");
            return;
        }

        ThreadPool.fixedThreadExecutor(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                if (null != bitmap) {
                    int height = bitmap.getHeight() * THUMB_SIZE / bitmap.getWidth();
                    height = Math.max(height, THUMB_SIZE);

                    Bitmap thumbBitmap = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, height, true);
                    byte[] thumbBytes = compressToByte(thumbBitmap, Bitmap.CompressFormat.JPEG, 30_000);
                    sendImageInner(mWxApi, imagePath, thumbBytes, scene);
                }
            }
        });
    }

    private void sendText(String text, int scene) {
        if (TextUtils.isEmpty(text)) {
            LogUtil.v("分享内容有误，缺少必要信息, text is null");
            return;
        }

        sendTextInner(mWxApi, text, scene);
    }

    /**
     * 压缩并转换成 bytes
     *
     * @param bitmap  图像
     * @param format  图像格式
     * @param aimSize 目标大小
     * @return 数组
     */
    private static byte[] compressToByte(@NonNull Bitmap bitmap, Bitmap.CompressFormat format, int aimSize) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        int quality = DEFAULT_QUALITY;
        for (int i = DEFAULT_QUALITY; i > 0; i -= 10) {
            output.reset();

            quality = i;
            bitmap.compress(format, i, output);
            if (output.size() < aimSize) {
                break;
            }
        }

        bitmap.recycle();

        LogUtil.v("quality = " + quality);
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 分享网页，给微信
     *
     * @param webpageUrl  html链接，限制长度不超过10kb
     * @param title       消息标题，限制长度不超过512bytes
     * @param description 消息描述，限制长度不超过1kb
     * @param thumbBytes  缩略图的二进制数据，限制内容大小不超过32kb【如果没有，会采用微信的icon】
     * @param scene       好友(0)、朋友圈(1)、收藏(2)
     */
    private static void sendWebpageInner(IWXAPI wxapi, String webpageUrl, String title, String description, byte[] thumbBytes, int scene) {
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = webpageUrl;

        WXMediaMessage mediaMessage = new WXMediaMessage();
        mediaMessage.mediaObject = webpageObject; // 点击后打开的内容
        mediaMessage.title = title; // 链接标题
        mediaMessage.description = description; // 链接描述
        mediaMessage.thumbData = thumbBytes; // 链接描述的图片

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = mediaMessage;
        req.scene = scene; // 发送给谁

        wxapi.sendReq(req);
    }

    /**
     * 分享文字，给微信
     *
     * @param description 文案描述 长度需大于0且不超过10KB
     * @param scene       好友(0)、朋友圈(1)、收藏(2)
     */
    private static void sendTextInner(IWXAPI wxapi, String description, int scene) {
        WXTextObject textObject = new WXTextObject();
        textObject.text = description;

        WXMediaMessage mediaMessage = new WXMediaMessage();
        mediaMessage.mediaObject = textObject;
        mediaMessage.description = description;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = mediaMessage;
        req.scene = scene;

        wxapi.sendReq(req);
    }

    /**
     * 分享大图，给微信
     *
     * @param imagePath  图片本地路径，限制对应图片内容大小不超过10MB
     * @param thumbBytes 略图的二进制数据，限制内容大小不超过32kb【如果没有，会采用微信的icon】
     * @param scene      好友(0)、朋友圈(1)、收藏(2)
     */
    private static void sendImageInner(IWXAPI wxapi, String imagePath, byte[] thumbBytes, int scene) {
        WXImageObject imageObject = new WXImageObject();
        imageObject.setImagePath(imagePath);

        WXMediaMessage mediaMessage = new WXMediaMessage();
        mediaMessage.mediaObject = imageObject; // 点击后打开的内容
        mediaMessage.thumbData = thumbBytes;   // 链接描述的图片[小图片]

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("image");
        req.message = mediaMessage;
        req.scene = scene; // 发送给谁

        wxapi.sendReq(req);
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
