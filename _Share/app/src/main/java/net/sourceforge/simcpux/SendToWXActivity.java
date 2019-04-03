package net.sourceforge.simcpux;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.opensdk.modelmsg.WXGameVideoFileObject;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoFileObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yline.base.BaseActivity;

import net.sourceforge.simcpux.uikit.CameraUtil;
import net.sourceforge.simcpux.uikit.MMAlert;

import java.io.File;

public class SendToWXActivity extends BaseActivity {
    private final static String TAG = "SendToWXActivity";

    private static final int THUMB_SIZE = 150;

    private static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();

    private IWXAPI api;
    private static final int MMAlertSelect1 = 0; // 分享到好友
    private static final int MMAlertSelect2 = 1; // 分享到朋友圈
    private static final int MMAlertSelect3 = 2; // 收藏
    private static final int MMAlertSelect4 = 3;
    private static final int MMAlertSelect5 = 4;
    private static final int MMAlertSelect6 = 5;

    private int mTargetScene = SendMessageToWX.Req.WXSceneSession;
    private String user_openId, accessToken, refreshToken, scope;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, Constants.WECHAT_APP_ID, false);

        setContentView(R.layout.send_to_wx);
        initView();
    }

    private void initView() {
        // send to weixin
        findViewById(R.id.send_text).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editor = new EditText(SendToWXActivity.this);
                editor.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                editor.setText(R.string.send_text_default);

                MMAlert.showAlert(SendToWXActivity.this, "send text", editor, getString(R.string.app_share), getString(R.string.app_cancel), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = editor.getText().toString();
                        if (text == null || text.length() == 0) {
                            return;
                        }

                        WXTextObject textObj = new WXTextObject();
                        textObj.text = text;

                        WXMediaMessage msg = new WXMediaMessage();
                        msg.mediaObject = textObj;
                        // msg.title = "Will be ignored";
                        msg.description = text;
                        msg.mediaTagName = "我是mediaTagName啊";

                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = buildTransaction("text");
                        req.message = msg;
                        req.scene = mTargetScene;

                        api.sendReq(req);
                        //finish();
                    }
                }, null);
            }
        });

        findViewById(R.id.send_img).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MMAlert.showAlert(SendToWXActivity.this, getString(R.string.send_img),
                        SendToWXActivity.this.getResources().getStringArray(R.array.send_img_item),
                        null, new MMAlert.OnAlertSelectId() {

                            @Override
                            public void onClick(int whichButton) {
                                switch (whichButton) {
                                    case MMAlertSelect1: {
                                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.send_img);
                                        WXImageObject imgObj = new WXImageObject(bmp);

                                        WXMediaMessage msg = new WXMediaMessage();
                                        msg.mediaObject = imgObj;

                                        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                                        bmp.recycle();
                                        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

                                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                                        req.transaction = buildTransaction("img");
                                        req.message = msg;
                                        req.scene = mTargetScene;
                                        api.sendReq(req);

                                        //finish();
                                        break;
                                    }
                                    case MMAlertSelect2: {
                                        String path = SDCARD_ROOT + "/test.png";
                                        File file = new File(path);
                                        if (!file.exists()) {
                                            String tip = SendToWXActivity.this.getString(R.string.send_img_file_not_exist);
                                            Toast.makeText(SendToWXActivity.this, tip + " path = " + path, Toast.LENGTH_LONG).show();
                                            break;
                                        }

                                        WXImageObject imgObj = new WXImageObject();
                                        imgObj.setImagePath(path);

                                        WXMediaMessage msg = new WXMediaMessage();
                                        msg.mediaObject = imgObj;

                                        Bitmap bmp = BitmapFactory.decodeFile(path);
                                        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                                        bmp.recycle();
                                        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

                                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                                        req.transaction = buildTransaction("img");
                                        req.message = msg;
                                        req.scene = mTargetScene;
                                        api.sendReq(req);

                                        //finish();
                                        break;
                                    }
                                    default:
                                        break;
                                }
                            }

                        });
            }
        });

        findViewById(R.id.send_music).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                MMAlert.showAlert(SendToWXActivity.this, getString(R.string.send_music),
                        SendToWXActivity.this.getResources().getStringArray(R.array.send_music_item),
                        null, new MMAlert.OnAlertSelectId() {

                            @Override
                            public void onClick(int whichButton) {
                                switch (whichButton) {
                                    case MMAlertSelect1: {
                                        WXMusicObject music = new WXMusicObject();
                                        //music.musicUrl = "http://www.baidu.com";
                                        music.musicUrl = "http://staff2.ustc.edu.cn/~wdw/softdown/index.asp/0042515_05.ANDY.mp3";
                                        //music.musicUrl="http://120.196.211.49/XlFNM14sois/AKVPrOJ9CBnIN556OrWEuGhZvlDF02p5zIXwrZqLUTti4o6MOJ4g7C6FPXmtlh6vPtgbKQ==/31353278.mp3";

                                        WXMediaMessage msg = new WXMediaMessage();
                                        msg.mediaObject = music;
                                        msg.title = "Music Title Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long";
                                        msg.description = "Music Album Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long";

                                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.send_music_thumb);
                                        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                                        bmp.recycle();
                                        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

                                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                                        req.transaction = buildTransaction("music");
                                        req.message = msg;
                                        req.scene = mTargetScene;
                                        api.sendReq(req);

                                        //finish();
                                        break;
                                    }
                                    case MMAlertSelect2: {
                                        WXMusicObject music = new WXMusicObject();
                                        music.musicLowBandUrl = "http://www.qq.com";

                                        WXMediaMessage msg = new WXMediaMessage();
                                        msg.mediaObject = music;
                                        msg.title = "Music Title";
                                        msg.description = "Music Album";

                                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.send_music_thumb);
                                        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                                        bmp.recycle();
                                        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

                                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                                        req.transaction = buildTransaction("music");
                                        req.message = msg;
                                        req.scene = mTargetScene;
                                        api.sendReq(req);

                                        //finish();
                                        break;
                                    }
                                    default:
                                        break;
                                }
                            }
                        });
            }
        });

        findViewById(R.id.send_video).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MMAlert.showAlert(SendToWXActivity.this, getString(R.string.send_video),
                        SendToWXActivity.this.getResources().getStringArray(R.array.send_video_item),
                        null, new MMAlert.OnAlertSelectId() {

                            @Override
                            public void onClick(int whichButton) {
                                switch (whichButton) {
                                    case MMAlertSelect1: {
                                        WXVideoObject video = new WXVideoObject();
                                        video.videoUrl = "http://www.qq.com";

                                        WXMediaMessage msg = new WXMediaMessage(video);

                                        msg.mediaTagName = "mediaTagName";
                                        msg.messageAction = "MESSAGE_ACTION_SNS_VIDEO#gameseq=1491995805&GameSvrEntity=87929&RelaySvrEntity=2668626528&playersnum=10";

                                        msg.title = "Video Title Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long";
                                        msg.description = "Video Description Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long";
                                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.send_music_thumb);
                                        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                                        bmp.recycle();
                                        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

                                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                                        req.transaction = buildTransaction("video");
                                        req.message = msg;
                                        req.scene = mTargetScene;
                                        api.sendReq(req);

                                        //finish();
                                        break;
                                    }
                                    case MMAlertSelect2: {
                                        WXVideoObject video = new WXVideoObject();
                                        video.videoLowBandUrl = "http://www.qq.com";

                                        WXMediaMessage msg = new WXMediaMessage(video);

                                        msg.mediaTagName = "mediaTagName";
                                        msg.messageAction = "MESSAGE_ACTION_SNS_VIDEO#gameseq=1491995805&GameSvrEntity=87929&RelaySvrEntity=2668626528&playersnum=10";

                                        msg.title = "Video Title";
                                        msg.description = "Video Description";

                                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                                        req.transaction = buildTransaction("video");
                                        req.message = msg;
                                        req.scene = mTargetScene;
                                        api.sendReq(req);

                                        //finish();
                                        break;
                                    }
                                    case MMAlertSelect3: {
                            /*Intent intent = new Intent();
                            intent.setType("video/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, 0x102);*/
                                        final WXGameVideoFileObject gameVideoFileObject = new WXGameVideoFileObject();
                                        final String path = "/sdcard/test_video.mp4";
                                        gameVideoFileObject.filePath = path;

                                        final WXMediaMessage msg = new WXMediaMessage();
                                        msg.setThumbImage(Util.extractThumbNail(path, 150, 150, true));
                                        msg.title = "this is title";
                                        msg.description = "this is description";
                                        msg.mediaObject = gameVideoFileObject;

                                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                                        req.transaction = buildTransaction("appdata");
                                        req.message = msg;
                                        req.scene = mTargetScene;
                                        api.sendReq(req);
                                        break;
                                    }
                                    default:
                                        break;
                                }
                            }
                        });
            }
        });

        findViewById(R.id.send_webpage).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MMAlert.showAlert(SendToWXActivity.this, getString(R.string.send_webpage),
                        SendToWXActivity.this.getResources().getStringArray(R.array.send_webpage_item),
                        null, new MMAlert.OnAlertSelectId() {

                            @Override
                            public void onClick(int whichButton) {
                                switch (whichButton) {
                                    case MMAlertSelect1:
                                        WXWebpageObject webpage = new WXWebpageObject();
                                        webpage.webpageUrl = "http://www.qq.com";

                                        WXMediaMessage msg = new WXMediaMessage(webpage);
                                        msg.title = "Title";
                                        msg.description = "Description";

                                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.send_img);
                                        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                                        bmp.recycle();
                                        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

                                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                                        req.transaction = buildTransaction("webpage");
                                        req.message = msg;
                                        req.scene = mTargetScene;
                                        api.sendReq(req);

                                        //finish();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
            }
        });

        // get token
        findViewById(R.id.get_token).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // send oauth request
                final SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo,snsapi_friend,snsapi_message,snsapi_contact";
                req.state = "none";
                api.sendReq(req);
                //finish();
            }
        });

        // get user info by token
        findViewById(R.id.get_info).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (user_openId == null || accessToken == null) {
                    showToast("请先获取code");
                } else {
                    Intent intent = new Intent(SendToWXActivity.this, UserInfoActivity.class);
                    intent.putExtra("openId", user_openId);
                    intent.putExtra("accessToken", accessToken);
                    intent.putExtra("refreshToken", refreshToken);
                    intent.putExtra("scope", scope);
                    startActivity(intent);
                }
            }
        });

        // unregister from weixin
        findViewById(R.id.unregister).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                api.unregisterApp();
            }
        });
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case 0x101: {
                if (resultCode == RESULT_OK) {
                    final WXAppExtendObject appdata = new WXAppExtendObject();
                    final String path = CameraUtil.getResultPhotoPath(this, data, SDCARD_ROOT + "/tencent/");
                    appdata.filePath = path;
                    appdata.extInfo = "this is ext info";

                    final WXMediaMessage msg = new WXMediaMessage();
                    msg.setThumbImage(Util.extractThumbNail(path, 150, 150, true));
                    msg.title = "this is title";
                    msg.description = "this is description";
                    msg.mediaObject = appdata;

                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("appdata");
                    req.message = msg;
                    req.scene = mTargetScene;
                    api.sendReq(req);

                    //finish();
                }
                break;
            }

            case 0x102: {
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                    String path = null;
                    String name = "";
                    if (uri != null) {
                        cursor.moveToFirst();
                        // String imgNo = cursor.getString(0); // 图片编号
                        path = cursor.getString(1); // 图片文件路径
                        String size = cursor.getString(2); // 图片大小
                        name = cursor.getString(3); // 图片文件名
                    } else {
                        path = uri.getPath();
                    }
                    if (path == null) {
                        Toast.makeText(SendToWXActivity.this, "video path not existed.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    WXVideoFileObject video = new WXVideoFileObject();
                    video.filePath = path;

                    if (!new File(video.filePath).exists()) {
                        Toast.makeText(SendToWXActivity.this, "video is not existed.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    WXMediaMessage msg = new WXMediaMessage(video);

                    msg.mediaTagName = "mediaTagName";
                    msg.messageAction = "MESSAGE_ACTION_SNS_VIDEO#gameseq=1491995805&GameSvrEntity=87929&RelaySvrEntity=2668626528&playersnum=10";

                    msg.title = name;
                    msg.description = "local gallery";
                    Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.send_music_thumb);
                    Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                    bmp.recycle();
                    msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("video");
                    req.message = msg;
                    req.scene = mTargetScene;
                    api.sendReq(req);

                    //finish();
                }
                break;
            }
            case 0x103: {
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                    String path = null;
                    if (cursor != null) {
                        cursor.moveToFirst();
                        // String imgNo = cursor.getString(0); // 图片编号
                        path = cursor.getString(1); // 图片文件路径
                        String size = cursor.getString(2); // 图片大小
                        String name = cursor.getString(3); // 图片文件名
                    } else {
                        path = uri.getPath();
                    }

                    if (path == null) {
                        Toast.makeText(SendToWXActivity.this, "video path not existed.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    WXGameVideoFileObject video = new WXGameVideoFileObject();
                    video.filePath = path;
                    if (!new File(video.filePath).exists()) {
                        Toast.makeText(SendToWXActivity.this, "video is not existed.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    video.videoUrl = "http://shzjwxsns.video.qq.com/102/20202/snsvideodownload?filekey=30270201010420301e02016604025348041073ff7a3ff67948755d57c9c409db2a8602032cbed00400&hy=SH&storeid=32303137303431373039343430323030306138383362613562303238613436663561333230613030303030303636&bizid=1023";
                    video.thumbUrl = "http://vweixinthumb.tc.qq.com/150/20250/snsvideodownload?filekey=30270201010420301e02020096040253480410d97d9ca5a4001348336662c5b432e50b020261820400&hy=SH&storeid=32303137303431373039343430323030306130393635613562303238613436663561333230613030303030303936&bizid=1023";
                    WXMediaMessage msg = new WXMediaMessage(video);

                    msg.mediaTagName = "mediaTagName";
                    msg.messageAction = "MESSAGE_ACTION_SNS_VIDEO#gameseq=1491995805&GameSvrEntity=87929&RelaySvrEntity=2668626528&playersnum=10";

                    msg.title = "Video Title";
                    msg.description = "Video Description";

                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("video");
                    req.message = msg;
                    req.scene = SendMessageToWX.Req.WXSceneSpecifiedContact;
                    api.sendReq(req);
                    //finish();
                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        user_openId = intent.getStringExtra("openId");
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        scope = intent.getStringExtra("scope");
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public void onRadioButtonClicked(View view) {
        if (!(view instanceof RadioButton)) {
            return;
        }

        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.target_scene_session:
                if (checked) {
                    mTargetScene = SendMessageToWX.Req.WXSceneSession;
                }
                break;
            case R.id.target_scene_timeline:
                if (checked) {
                    mTargetScene = SendMessageToWX.Req.WXSceneTimeline;
                }
                break;
            case R.id.target_scene_favorite:
                if (checked) {
                    mTargetScene = SendMessageToWX.Req.WXSceneFavorite;
                }
                break;
        }
    }
}
