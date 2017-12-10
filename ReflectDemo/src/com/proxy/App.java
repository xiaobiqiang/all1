package com.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class App {
	//newProxyInstance发生的过程猜测：
	//1.利用cl和clazzs生成一个实现clazzs的类，和SubjectImpl并列
	//2.利用clazzs构造接口的所有Method对象，给后面handler里面的invoke方法。
	//3.newProxyInstance返回的是新建类的实例，新建类实现了所有的接口。
	//4.因此转换为所有的接口对象没问题，调用方法时根据传参传给handler的invoke方法的args。
	public static void main(String[] args) {
		ClassLoader cl = SubjectImpl.class.getClassLoader();
		Class[] clazzs = {Subject.class};
		InvocationHandler handler = new MyInvocationHandler(new SubjectImpl());
		Subject s = (Subject)Proxy.newProxyInstance(cl, clazzs, handler);
		s.run();
	}

}
