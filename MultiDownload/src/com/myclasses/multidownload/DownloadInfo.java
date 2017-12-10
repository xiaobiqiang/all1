package com.myclasses.multidownload;

public class DownloadInfo {
	public int start = 0;
	public int length = 0;
	public int index = 0;
	public volatile boolean isPausing = false;
	public static int completeCount = 0;
	public static int numThread = 0;
}
