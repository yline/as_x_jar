package com.lib.ioc.view.annomation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewInject {
	/** 绑定的内容 */
	int value();
	
	/** parent view id */
	int parentId() default 0;
}
