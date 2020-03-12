package com.ioc.xutils3.sample.base;

import android.app.Application;
import android.content.Context;

import com.ioc.xutils3.lib.com.ioc.xutils3.lib.ViewInjector;
import com.ioc.xutils3.lib.com.ioc.xutils3.lib.view.ViewInjectorImpl;

import java.lang.reflect.Method;

/**
 * 任务控制中心,view注入等接口的入口.
 * 这个控制中心,还可以同样的原理添加其它的工作组件
 * 需要在在application的onCreate中初始化: x.Ext.init(this);
 */
public final class x {
    private x() {
    }

    private static Application sApplication;
    private static ViewInjector sViewInjector;

    public static void init(Application app) {
        if (null == sApplication && null != app) {
            x.sApplication = app;
        }
    }

    /**
     * @return inject函数调用
     */
    public static ViewInjector view() {
        if (null == sViewInjector) {
            // 内部已经保证了同步
            sViewInjector = ViewInjectorImpl.getInstance();
        }
        return sViewInjector;
    }
}
