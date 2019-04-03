package com.baidu.tts.sample.check;

public interface CheckInterceptor {
    /**
     * 检查信息
     *
     * @param lastLog 上一次的log
     * @return 本次处理后，返回的log
     */
    StringBuilder check(StringBuilder lastLog);
}
