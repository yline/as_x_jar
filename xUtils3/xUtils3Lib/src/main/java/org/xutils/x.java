package org.xutils;

import android.app.Application;
import android.content.Context;

import org.xutils.common.TaskController;
import org.xutils.common.task.TaskControllerImpl;
import org.xutils.db.DbManagerImpl;
import org.xutils.http.HttpManagerImpl;
import org.xutils.image.ImageManagerImpl;
import org.xutils.view.ViewInjectorImpl;

import java.lang.reflect.Method;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;


/**
 * 任务控制中心, http, image, db, view注入等接口的入口.
 * 需要在在application的onCreate中初始化: x.Ext.init(this);
 * <p/>
 * 队列具有优先值:Priority{UI_TOP > UI_NORMAL > UI_LOW > DEFAULT > BG_TOP > BG_NORMAL > BG_LOW;}
 * 规则:
 * 有优先级别时:严格安装优先级别划分
 * 无优先级别时:基本按照堆栈，后进先出
 * 部分有优先级别时:有的部分按照优先级别执行,没有的部分是乱序。
 * @author yline 2016/10/5 --> 7:36
 * @version 1.0.0
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
				throw new RuntimeException("please invoke x.Ext.init(app) on Application#onCreate()" + " and register your Application in manifest.");
			}
		}
		return Ext.app;
	}

	public static TaskController task()
	{
		return Ext.taskController;
	}

	public static HttpManager http()
	{
		if (Ext.httpManager == null)
		{
			HttpManagerImpl.registerInstance();
		}
		return Ext.httpManager;
	}

	public static ImageManager image()
	{
		if (Ext.imageManager == null)
		{
			ImageManagerImpl.registerInstance();
		}
		return Ext.imageManager;
	}

	public static ViewInjector view()
	{
		if (Ext.viewInjector == null)
		{
			ViewInjectorImpl.registerInstance();
		}
		return Ext.viewInjector;
	}

	public static DbManager getDb(DbManager.DaoConfig daoConfig)
	{
		return DbManagerImpl.getInstance(daoConfig);
	}

	public static class Ext
	{
		private static boolean debug;

		private static Application app;

		private static TaskController taskController;

		private static HttpManager httpManager;

		private static ImageManager imageManager;

		private static ViewInjector viewInjector;

		private Ext()
		{
		}

		static
		{
			TaskControllerImpl.registerInstance();
			// 默认信任所有https域名
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
			{
				@Override
				public boolean verify(String hostname, SSLSession session)
				{
					return true;
				}
			});
		}

		public static void init(Application app)
		{
			if (Ext.app == null)
			{
				Ext.app = app;
			}
		}

		public static void setDebug(boolean debug)
		{
			Ext.debug = debug;
		}

		public static void setTaskController(TaskController taskController)
		{
			if (Ext.taskController == null)
			{
				Ext.taskController = taskController;
			}
		}

		public static void setHttpManager(HttpManager httpManager)
		{
			Ext.httpManager = httpManager;
		}

		public static void setImageManager(ImageManager imageManager)
		{
			Ext.imageManager = imageManager;
		}

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
