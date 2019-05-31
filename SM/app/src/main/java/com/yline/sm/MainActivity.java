package com.yline.sm;

import android.os.Bundle;
import android.view.View;

import com.yline.sm.test.SM2Test;
import com.yline.sm.test.SM3Test;
import com.yline.sm.test.SM4Test;
import com.yline.test.BaseTestActivity;

/**
 * 参考：
 * https://github.com/gotoworld/hsd-cipher-sm  --  代码主体
 * https://blog.csdn.net/wkernel/article/details/71193413 -- 对应关系
 * https://github.com/xjfme/SM2_SM3_SM4Encrypt -- SM2 秘钥对，生成
 * http://maven.outofmemory.cn/org.bouncycastle/  -- jar包下载地址，我下载的是 jdk16_1.46
 *
 * @author yline 2019/5/31 -- 18:04
 */
public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("SM2", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SM2Test sm2Test = new SM2Test();
                sm2Test.testSM2Provider();
                sm2Test.testSM2Creator();
            }
        });

        addButton("SM3", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SM3Test().testSM3();
            }
        });

        addButton("SM4", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SM4Test().testSM4();
            }
        });
    }
}
