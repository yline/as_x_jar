package com.baidu.tts.sample.check;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

public class AppInfoCheck implements CheckInterceptor {
    private static final String APP_ID = "11005757";
    private static final String APP_KEY = "Ovcz19MGzIKoDDb3IsFFncG1";
    private static final String SECRET_KEY = "e72ebb6d43387fc7f85205ca7e6706e2";

    @Override
    public StringBuilder check(StringBuilder lastLog) {
        lastLog.append("--------------------检查AppId AppKey SecretKey--------------------").append("\n");

        lastLog.append("APPID = ").append(APP_ID).append("\n");
        lastLog.append("APP_KEY = ").append(APP_KEY).append("\n");
        lastLog.append("SECRET_KEY = ").append(SECRET_KEY).append("\n");

        try {
            checkOnline(APP_ID, APP_KEY, SECRET_KEY, lastLog);
            lastLog.append("通过").append("\n");
        } catch (UnknownHostException e) {
            lastLog.append("无网络或者网络不连通，忽略检测 : ").append(e.getMessage()).append("\n");
        } catch (Exception e) {
            lastLog.append(e.getClass().getCanonicalName()).append(":").append(e.getMessage()).append("\n");
            lastLog.append("重新检测appId， appKey， appSecret是否正确").append("\n");
        }

        return lastLog;
    }

    private void checkOnline(String appId, String appKey, String secretKey, StringBuilder lastLog) throws Exception {
        String urlpath = "http://openapi.baidu.com/oauth/2.0/token?grant_type=client_credentials&client_id="
                + appKey + "&client_secret=" + secretKey;
        URL url = new URL(urlpath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(1000);
        InputStream is = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder result = new StringBuilder();
        String line = "";
        do {
            line = reader.readLine();
            if (line != null) {
                result.append(line);
            }
        } while (line != null);

        String res = result.toString();
        lastLog.append("openapi return").append(res).append("\n");

        JSONObject jsonObject = new JSONObject(res);
        String error = jsonObject.optString("error");
        if (error != null && !error.isEmpty()) {
            throw new Exception("appkey secretKey 错误" + ", error:" + error + ", json is" + result);
        }

        String token = jsonObject.getString("access_token");
        if (token == null || !token.endsWith("-" + appId)) {
            throw new Exception("appId 与 appkey及 appSecret 不一致。appId = " + appId + " ,token = " + token);
        }
    }
}
