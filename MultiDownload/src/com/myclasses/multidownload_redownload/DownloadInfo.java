package com.myclasses.multidownload_redownload;

public class DownloadInfo {
	//ÿ���߳�ÿ�εĿ�ʼλ�ã�metafile���������εĳ���,
	//����´γ���Ӧ�����ϴεĳ��ȼ�ȥ�ϴ���ɵĳ���
	public int start = 0;
	//ÿ���߳�ÿ�ε����س���
	public int length = 0;
	//�߳����
	public int index = 0;
	//ÿ���߳�ÿ�ε���ɳ���
	public int hasFinished = 0;
	//ÿ���̵߳��ܳ��ȣ��ɵ�һ��ȷ��
	public int originLength = 0;
	//ÿ���̵߳����д�����ɳ��ȣ�����originLengthʱ˵������̵߳��������
	public int hasAllFinished = 0;
	public volatile boolean isPausing = false;
	public static int numThread = 0;
	//�������������ع����в�����downloading�����ļ���·��
	public static String downloadFilepath = null;
	public static String srcUrl = null;
}
