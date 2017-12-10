package com.myclasses.multidownload_redownload;

public class DownloadInfo {
	//每个线程每次的开始位置，metafile保存的是这次的长度,
	//因此下次长度应该是上次的长度减去上次完成的长度
	public int start = 0;
	//每个线程每次的下载长度
	public int length = 0;
	//线程序号
	public int index = 0;
	//每个线程每次的完成长度
	public int hasFinished = 0;
	//每个线程的总长度，由第一次确定
	public int originLength = 0;
	//每个线程的所有次数完成长度，等于originLength时说明这个线程的内容完毕
	public int hasAllFinished = 0;
	public volatile boolean isPausing = false;
	public static int numThread = 0;
	//这个保存的是下载过程中产生的downloading属性文件的路径
	public static String downloadFilepath = null;
	public static String srcUrl = null;
}
