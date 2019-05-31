package com.yline.sm.test;

import com.yline.sm.crypt.SM3Utils;
import com.yline.utils.LogUtil;

public class SM3Test {
    public void testSM3() {
        LogUtil.v(SM3Utils.encrypt("abc".getBytes()));
    }
}
