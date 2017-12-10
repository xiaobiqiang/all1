package com.main;

public class TestGC {
	public static void main(String[] args) {
		byte[] b = new byte[1024*1024*8];
		System.gc(); //无意义，不具备回收的条件
		//要让被回收，zhi null,具备了被回收的条件
		b = null;
		System.gc(); //回收了，显示的回收
	}
}
