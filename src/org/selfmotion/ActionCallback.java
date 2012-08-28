package org.selfmotion;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Intent;

public class ActionCallback {

	private final Method method;
	private final Object target;

	public ActionCallback(Method method, Object target) {
		super();
		this.method = method;
		this.target = target;
	}

	public Object execute(Intent intent, int responseCode) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		if(method.getParameterTypes().length != 2){
			throw new RuntimeException("Callback methods must receive two argument: (Intent data, int responseCode)");
		}
		return method.invoke(target, intent, responseCode);		
	}

	public Object getTarget() {
		return target;
	}
	
	public Method getMethod() {
		return method;
	}

}
