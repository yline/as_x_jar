package net.sourceforge.simcpux;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.sourceforge.simcpux.uikit.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.HashMap;

public class UserInfoActivity extends Activity {
    private static String TAG = "MicroMsg.UserInfoActivity";

    private static MyHandler handler;
    private static String refreshToken;
    private static String openId;
    private static String accessToken;
    private static String scope;

    private static class MyHandler extends Handler {
        private final WeakReference<UserInfoActivity> userInfoActivityWR;

        public MyHandler(UserInfoActivity userInfoActivity){
            userInfoActivityWR = new WeakReference<UserInfoActivity>(userInfoActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            int tag = msg.what;
            Bundle data = msg.getData();
            JSONObject json = null;
            switch (tag) {
                case NetworkUtil.CHECK_TOKEN: {
                    try {
                        json = new JSONObject(data.getString("result"));
                        int errcode = json.getInt("errcode");
                        if (errcode == 0) {
                            NetworkUtil.sendWxAPI(handler, String.format("https://api.weixin.qq.com/sns/userinfo?" +
                                    "access_token=%s&openid=%s", accessToken, openId), NetworkUtil.GET_INFO);
                        } else {
                            NetworkUtil.sendWxAPI(handler, String.format("https://api.weixin.qq.com/sns/oauth2/refresh_token?" +
                                    "appid=%s&grant_type=refresh_token&refresh_token=%s", "wxd930ea5d5a258f4f", refreshToken),
                                    NetworkUtil.REFRESH_TOKEN);
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                    break;
                }
                case NetworkUtil.REFRESH_TOKEN: {
                    try {
                        json = new JSONObject(data.getString("result"));
                        openId = json.getString("openid");
                        accessToken = json.getString("access_token");
                        refreshToken = json.getString("refresh_token");
                        scope = json.getString("scope");
                        NetworkUtil.sendWxAPI(handler, String.format("https://api.weixin.qq.com/sns/userinfo?" +
                                "access_token=%s&openid=%s", accessToken, openId), NetworkUtil.GET_INFO);
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                    break;
                }
                case NetworkUtil.GET_INFO: {
                    try {
                        json = new JSONObject(data.getString("result"));
                        final String nickname, sex, province, city, country, headimgurl;
                        headimgurl = json.getString("headimgurl");
                        NetworkUtil.getImage(handler, headimgurl, NetworkUtil.GET_IMG);
                        String encode;
                        encode = getcode(json.getString("nickname"));
                        nickname = "nickname: " + new String(json.getString("nickname").getBytes(encode), "utf-8");
                        sex = "sex: " + json.getString("sex");
                        province = "province: " + json.getString("province");
                        city = "city: " + json.getString("city");
                        country = "country: " + json.getString("country");
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ((TextView)userInfoActivityWR.get().findViewById(R.id.nickname)).setText(nickname);
                                ((TextView)userInfoActivityWR.get().findViewById(R.id.scope)).setText(scope);
                                ((TextView)userInfoActivityWR.get().findViewById(R.id.sex)).setText(sex);
                                ((TextView)userInfoActivityWR.get().findViewById(R.id.province)).setText(province);
                                ((TextView)userInfoActivityWR.get().findViewById(R.id.city)).setText(city);
                                ((TextView)userInfoActivityWR.get().findViewById(R.id.country)).setText(country);
                            }
                        });
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, e.getMessage());
                    }
                    break;
                }
                case NetworkUtil.GET_IMG: {
                    byte[] imgdata = data.getByteArray("imgdata");
                    final Bitmap bitmap;
                    if (imgdata != null) {
                        bitmap = BitmapFactory.decodeByteArray(imgdata, 0, imgdata.length);
                    } else {
                        bitmap = null;
                        showToast(userInfoActivityWR.get(), "头像图片获取失败");
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ((ImageView)userInfoActivityWR.get().findViewById(R.id.headimg)).setImageBitmap(bitmap);
                        }
                    });
                    break;
                }
            }
        }
    }

    private static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences("SDKOauth", Context.MODE_PRIVATE);
        handler = new MyHandler(this);

        Intent data = getIntent();
        openId = data.getStringExtra("openId");
        accessToken = data.getStringExtra("accessToken");
        refreshToken = data.getStringExtra("refreshToken");
        scope = "scope: " + data.getStringExtra("scope");
        if (accessToken != null && openId != null){
            NetworkUtil.sendWxAPI(handler, String.format("https://api.weixin.qq.com/sns/auth?" +
                    "access_token=%s&openid=%s", accessToken, openId), NetworkUtil.CHECK_TOKEN);
        } else {
            Toast.makeText(this, "请先获取code", Toast.LENGTH_LONG).show();
        }

        setContentView(R.layout.user_info_ui);
    }

    private static String getcode (String str) {
        String[] encodelist ={"GB2312","ISO-8859-1","UTF-8","GBK","Big5","UTF-16LE","Shift_JIS","EUC-JP"};
        for(int i =0;i<encodelist.length;i++){
            try {
                if (str.equals(new String(str.getBytes(encodelist[i]),encodelist[i]))) {
                    return encodelist[i];
                }
            } catch (Exception e) {

            } finally{

            }
        } return "";
    }
}
