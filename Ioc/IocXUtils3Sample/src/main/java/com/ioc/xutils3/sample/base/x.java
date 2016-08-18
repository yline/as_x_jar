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
public final class x
{
	private x()
	{
	}

	public static boolean isDebug()
	{
		return Ext.debug;
	}

	/**
	 * @return ApplicationContext
	 */
	public static Application app()
	{
		if (Ext.app == null)
		{
			try
			{
				// 在IDE进行布局预览时使用
				Class<?> renderActionClass = Class.forName("com.android.layoutlib.bridge.impl.RenderAction");
				Method method = renderActionClass.getDeclaredMethod("getCurrentContext");
				Context context = (Context) method.invoke(null);
				Ext.app = new MockApplication(context);
			}
			catch (Throwable ignored)
			{
				throw new RuntimeException("please invoke x.Ext.init(app) on Application#onCreate()");
			}
		}
		return Ext.app;
	}

	/**
	 * @return inject函数调用
	 */
	public static ViewInjector view()
	{
		if (Ext.viewInjector == null)
		{
			Ext.setViewInjector(ViewInjectorImpl.registerInstance());
		}
		return Ext.viewInjector;
	}

	/**
	 * 下层调用,上层一般不用
	 *
	 * @author f21
	 * @date 2016-3-10
	 */
	public static class Ext
	{
		private static boolean debug = true;  // 默认为true

		private static Application app;

		private static ViewInjector viewInjector;

		private Ext()
		{
		}

		/** application 中抛出context */
		public static void init(Application app)
		{
			if (Ext.app == null)
			{
				Ext.app = app;
			}
		}

		/** application 中设置打开log */
		public static void setDebug(boolean debug)
		{
			Ext.debug = debug;
		}

		/** 添加view注入 */
		public static void setViewInjector(ViewInjector viewInjector)
		{
			Ext.viewInjector = viewInjector;
		}
	}

	private static class MockApplication extends Application
	{
		public MockApplication(Context baseContext)
		{
			this.attachBaseContext(baseContext);
		}
	}
}
