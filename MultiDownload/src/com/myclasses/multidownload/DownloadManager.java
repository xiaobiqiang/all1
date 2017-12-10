package com.myclasses.multidownload;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DownloadManager {
	private MultiDownloadUI ui = null;
	private String url = null;
	private String dst = null;
	private int numThread = 0;
	private ArrayList<DownloadInfo> infoList = null;
	private volatile boolean isFirstStart = true;
	
	public DownloadManager(MultiDownloadUI ui, String url, String dst, int numThread) {
		this.ui = ui;
		this.url = url;
		this.dst = dst;
		this.numThread = numThread;
		this.infoList = new ArrayList<>();
		DownloadInfo.numThread = this.numThread;
	}
	
	private int getFileLength() {
		try {
			URL rUrl = new URL(this.url);
			HttpURLConnection conn = (HttpURLConnection)rUrl.openConnection();
			int fileLen = conn.getContentLength();
			return fileLen;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	private void initDownloadInfoList(int fileLen, int numThread) {
		if(fileLen <= 0 || numThread <= 0)
			return;
		infoList = new ArrayList<>();
		DownloadInfo info = null;
		int avgLen = fileLen/numThread;
		
		for(int i=0; i<numThread; i++) {
			info = new DownloadInfo();
			info.length = i == (numThread-1) ? 
							(fileLen - infoList.get(i-1).start - avgLen) :
								avgLen;
			info.start = i*avgLen;
			info.index = i;
			infoList.add(info);
		}
	}
			
	public void startDownload() {
		if(!this.isFirstStart && this.infoList.get(0).isPausing) {
			for(int i=0; i<this.numThread; i++) {
				this.infoList.get(i).isPausing = false;
			}
		}
		
		if(isFirstStart) {
			this.isFirstStart = false;
			int fileLen = this.getFileLength();
			initDownloadInfoList(fileLen, this.numThread);
			
			for(int i=0; i<this.numThread; i++) {
				DownloadInfo info = infoList.get(i);
				new DownloadThread(info, this.url, this.dst, this.ui, this).start();
			}
		}
	}
	
	public void pauseDownload() {
		for(int i=0; i<this.numThread; i++) {
			this.infoList.get(i).isPausing = true;
		}
	}
	
	public void setIsFirstStart(boolean state) {
		this.isFirstStart = state;
	}
}
