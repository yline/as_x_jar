package com.yline.framework.router;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.yline.framework.router.impl.TestImpl;

/**
 * 这个类，负责
 * 1，初始化操作
 * 2，分发到模块的具体实现
 *
 * @author yline 2020/4/7 -- 13:33
 */
public class RouterManager {
    public static void init(Application application) {
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        ARouter.openLog();     // 打印日志
        ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)

        ARouter.init(application); // 尽可能早，推荐在Application中初始化
    }

    public static TestImpl moduleTest() {
        return new TestImpl();
    }
}
