package com.baidu.idl.face.platform.injector;

import com.baidu.idl.face.platform.FaceSDKManager;

/**
 * 被上层实现的管理类
 *
 * @author yline 2019/11/19 -- 11:29
 */
public class LogManager {
    public static void v(String content) {
        FaceSDKManager.getInstance().getInjector().log(content, 0);
    }

    public static void e(String content) {
        FaceSDKManager.getInstance().getInjector().log(content, 4);
    }
}
