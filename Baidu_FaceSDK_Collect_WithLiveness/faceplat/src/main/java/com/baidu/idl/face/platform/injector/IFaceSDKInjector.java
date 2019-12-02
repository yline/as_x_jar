package com.baidu.idl.face.platform.injector;

/**
 * 依赖上层注册的实现方法
 *
 * @author yline 2019/11/19 -- 10:19
 */
public interface IFaceSDKInjector {
    /**
     * 打印日志
     *
     * @param content 内容
     * @param level   等级[0-v，1-d，2-i，3-w，其他-e]
     */
    void log(String content, int level);
}
