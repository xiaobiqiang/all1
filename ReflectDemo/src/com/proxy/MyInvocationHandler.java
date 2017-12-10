package com.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MyInvocationHandler implements InvocationHandler {
	private Subject subject;
	
	public MyInvocationHandler(Subject s) {
		this.subject = s;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] arg2) throws Throwable {
		System.out.println("pre invoke");
		Object o = method.invoke(subject, arg2);
		System.out.println("after invoke");
		return o;
	}

}
