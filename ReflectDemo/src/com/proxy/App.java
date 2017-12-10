package com.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class App {
	//newProxyInstance�����Ĺ��̲²⣺
	//1.����cl��clazzs����һ��ʵ��clazzs���࣬��SubjectImpl����
	//2.����clazzs����ӿڵ�����Method���󣬸�����handler�����invoke������
	//3.newProxyInstance���ص����½����ʵ�����½���ʵ�������еĽӿڡ�
	//4.���ת��Ϊ���еĽӿڶ���û���⣬���÷���ʱ���ݴ��δ���handler��invoke������args��
	public static void main(String[] args) {
		ClassLoader cl = SubjectImpl.class.getClassLoader();
		Class[] clazzs = {Subject.class};
		InvocationHandler handler = new MyInvocationHandler(new SubjectImpl());
		Subject s = (Subject)Proxy.newProxyInstance(cl, clazzs, handler);
		s.run();
	}

}
