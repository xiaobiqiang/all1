package com.main;

public class TestGC {
	public static void main(String[] args) {
		byte[] b = new byte[1024*1024*8];
		System.gc(); //�����壬���߱����յ�����
		//Ҫ�ñ����գ�zhi null,�߱��˱����յ�����
		b = null;
		System.gc(); //�����ˣ���ʾ�Ļ���
	}
}
