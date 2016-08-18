package com.ioc.xutils3.lib.com.ioc.xutils3.lib.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ioc.xutils3.lib.com.ioc.xutils3.lib.ViewInjector;
import com.ioc.xutils3.lib.com.ioc.xutils3.lib.utils.LogIoc;
import com.ioc.xutils3.lib.com.ioc.xutils3.lib.view.annomation.ContentView;
import com.ioc.xutils3.lib.com.ioc.xutils3.lib.view.annomation.Event;
import com.ioc.xutils3.lib.com.ioc.xutils3.lib.view.annomation.ViewInject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;

public class ViewInjectorImpl implements ViewInjector
{
	// 不太懂
	private static final HashSet<Class<?>> IGNORED = new HashSet<Class<?>>();

	static
	{
		IGNORED.add(Object.class);
		IGNORED.add(Activity.class);
		IGNORED.add(android.app.Fragment.class);
		try
		{
			IGNORED.add(Class.forName("android.support.v4.app.Fragment"));
			IGNORED.add(Class.forName("android.support.v4.app.FragmentActivity"));
		}
		catch (Throwable ignored)
		{
		}
	}

	@Override
	public void inject(View view)
	{
		injectObject(view, view.getClass(), new ViewFinder(view));
	}

	private static final Object lock = new Object();

	private static ViewInjectorImpl instance;

	private ViewInjectorImpl()
	{
	}

	public static ViewInjectorImpl registerInstance()
	{
		if (null == instance)
		{
			synchronized (lock)
			{
				if (null == instance)
				{
					instance = new ViewInjectorImpl();
				}
			}
		}
		return instance;
		//	zc.Ext.setViewInjector(instance);
	}

	@Override
	public void inject(Activity activity)
	{
		Class<?> handlerType = activity.getClass();

		try
		{
			ContentView contentView = findContentView(handlerType);
			if (null != contentView)
			{
				int viewId = contentView.value();
				if (viewId > 0)
				{
					Method setContentViewMethod = handlerType.getMethod("setContentView", int.class);
					setContentViewMethod.invoke(activity, viewId);
				}
			}
		}
		catch (Throwable ex)
		{
			LogIoc.e(ex.getMessage(), ex);
		}
		
		injectObject(activity, handlerType, new ViewFinder(activity));
	}

	@Override
	public void inject(Object handler, View view)
	{
		injectObject(handler, handler.getClass(), new ViewFinder(view));
	}

	@Override
	public View inject(Object fragment, LayoutInflater inflater, ViewGroup container)
	{
		// inject ContentView
		View view = null;

		Class<?> handlerType = fragment.getClass();
		try
		{
			ContentView contentView = findContentView(handlerType);
			if (contentView != null)
			{
				int viewId = contentView.value();
				if (viewId > 0)
				{
					view = inflater.inflate(viewId, container, false);
				}
			}
		}
		catch (Throwable ex)
		{
			LogIoc.e(ex.getMessage(), ex);
		}

		// inject res & event
		injectObject(fragment, handlerType, new ViewFinder(view));

		return view;
	}

	/**
	 * 从父类获取注解View(子类找不到就从父类再找几次)
	 *
	 * @return
	 */
	private static ContentView findContentView(Class<?> thisCls)
	{
		if (null == thisCls || IGNORED.contains(thisCls))
		{
			return null;
		}
		ContentView contentView = thisCls.getAnnotation(ContentView.class);
		if (null == contentView)
		{
			return findContentView(thisCls.getSuperclass());
		}
		return contentView;
	}

	private static void injectObject(Object handler, Class<?> handlerType, ViewFinder finder)
	{
		if (null == handlerType || IGNORED.contains(handlerType))
		{
			return;
		}

		// inject view
		Field[] fields = handlerType.getDeclaredFields();
		if (null != fields && fields.length > 0)
		{
			for (Field field : fields)
			{
				Class<?> fieldType = field.getType();
				if (
						/* 不注入静态字段 */     Modifier.isStatic(field.getModifiers()) ||
						/* 不注入final字段 */    Modifier.isFinal(field.getModifiers()) ||
						/* 不注入基本类型字段 */  fieldType.isPrimitive() ||
						/* 不注入数组类型字段 */  fieldType.isArray())
				{
					continue;
				}

				ViewInject viewInject = field.getAnnotation(ViewInject.class);
				if (null != viewInject)
				{
					try
					{
						View view = finder.findViewById(viewInject.value(), viewInject.parentId());
						if (view != null)
						{
							field.setAccessible(true);
							field.set(handler, view);
						}
						else
						{
							throw new RuntimeException("Invalid id(" + viewInject.value() + ") for @ViewInject!" + handlerType.getSimpleName());
						}
					}
					catch (Throwable ex)
					{
						LogIoc.e(ex.getMessage(), ex);
					}
				}
			}
		}

		// inject event
		Method[] methods = handlerType.getDeclaredMethods();
		if (null != methods && methods.length > 0)
		{
			for (Method method : methods)
			{
				if (Modifier.isStatic(method.getModifiers()) || !Modifier.isPrivate(method.getModifiers()))
				{
					continue;
				}

				//检查当前方法是否是event注解的方法
				Event event = method.getAnnotation(Event.class);
				if (event != null)
				{
					try
					{
						// id参数
						int[] values = event.value();
						int[] parentIds = event.parentId();
						int parentIdsLen = parentIds == null ? 0 : parentIds.length;
						//循环所有id，生成ViewInfo并添加代理反射
						for (int i = 0; i < values.length; i++)
						{
							int value = values[i];
							if (value > 0)
							{
								ViewInfo info = new ViewInfo();
								info.value = value;
								info.parentId = parentIdsLen > i ? parentIds[i] : 0;
								method.setAccessible(true);
								EventListenerManager.addEventMethod(finder, info, event, handler, method);
							}
						}
					}
					catch (Throwable ex)
					{
						LogIoc.e(ex.getMessage(), ex);
					}
				}
			}
		}
		// 父类
		injectObject(handler, handlerType.getSuperclass(), finder);
	}

}


















