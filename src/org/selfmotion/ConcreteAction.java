package org.selfmotion;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.dsol.annotation.ReturnValue;

import android.content.Context;

public class ConcreteAction {

	private final Method method;
	private final Object target;
	private final int priority;

	public ConcreteAction(Method method, Object target, int priority) {
		super();
		this.method = method;
		this.target = target;
		this.priority = priority;
	}

	public void setContext(Context context) {
		Method method;
		try {
			method = target.getClass().getMethod("setContext", Context.class);
			method.invoke(target, context);
		} catch (Exception e) {}
	}

	public Object execute(Object... params) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		Object returnValue = method.invoke(target, params);
		return returnValue;
	}

	public Class[] getParamTypes() {
		return method.getParameterTypes();
	}

	public Object getTarget() {
		return target;
	}

	public String getName() {
		return method.getName();
	}

	public int getPriority() {
		return priority;
	}
	
	public Method getMethod() {
		return method;
	}
}
