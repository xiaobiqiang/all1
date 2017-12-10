package com.myclasses.multidownload_redownload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

public class DownloadManager {
	private MultiDownloadUI ui = null;
	
	private String url = null;
	private String dst = null;
	private int numThread = -1;
	
	private ArrayList<DownloadInfo> infoList = null;
	
	private boolean isRedownload = false;
	private volatile boolean isFirstStart = true;
	private int completeCount = 0;
	
	private Properties prop = null;
	
	public DownloadManager() {
		
	}
	
	//构造方法尽量只涉及修改自己类的值。
	//修改Info类的值尽量去建一个方法集中修改。
	/**
	 * if it is the state of redownload,please make param of numThread equal to -1.
	 */
	public DownloadManager(MultiDownloadUI ui, String url, String dst, int numThread) {
		this.ui = ui;
		
		this.url = url;
		this.dst = dst;
		this.numThread = numThread;
		
		this.isRedownload = this.numThread == -1 ? true : false;
		
		this.infoList = new ArrayList<>();
		prop = new Properties();
	}
	
	private int getFileLength() {
		int fileLen = -1;
		
		try {
			URL rUrl = new URL(this.url);
			HttpURLConnection conn = (HttpURLConnection)rUrl.openConnection();
			fileLen = conn.getContentLength();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return fileLen;
	}
	
	private void initDownloadInfoListBynew(int fileLen, int numThread) {
		if(fileLen <= 0 || numThread <= 0)
			return;
		
		DownloadInfo.numThread = numThread;
		DownloadInfo.srcUrl = this.url;
		
		DownloadInfo info = null;
		int avgLen = fileLen/numThread;
		
		for(int i=0; i<numThread; i++) {
			info = new DownloadInfo();
			
			info.length = i == (numThread-1) ? 
							(fileLen - infoList.get(i-1).start - avgLen) :
								avgLen;
			info.start = i*avgLen;
			info.index = i;
			info.originLength = info.length;
			info.hasAllFinished = 0;
			info.hasFinished = 0;
			
			infoList.add(info);
		}
	}
	
	private void initDownloadInfoListByold() {
		DownloadInfo.srcUrl = this.url;
		
		try {
			FileInputStream fis = new FileInputStream(DownloadInfo.downloadFilepath);
			prop.load(fis);
			DownloadInfo.numThread = Integer.parseInt(prop.getProperty("numThread"));
			
			DownloadInfo info;
			for(int i=0; i<DownloadInfo.numThread; i++) {
				info = new DownloadInfo();
				
				info.index = i;
				info.hasFinished = Integer.parseInt(prop.getProperty("thread." + i + "." + "finished"));
				info.length = Integer.parseInt(prop.getProperty("thread." + i + "." + "length")) - info.hasFinished;
				info.start = Integer.parseInt(prop.getProperty("thread." + i + "." + "start")) + info.hasFinished;
				info.hasAllFinished = Integer.parseInt(prop.getProperty("thread." + i + "." + "hasAllFinished"));
				info.originLength = Integer.parseInt(prop.getProperty("thread." + i + "." + "originLength"));
				
				infoList.add(info);
			}
			
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return null when not exist downloading file.
	 * 			urlpath contrast.
	 */
	public String checkOldDownload(String dst) {
		DownloadInfo.downloadFilepath = dst;
		
		File fold = new File(dst);
		if(!fold.exists()) {
			return null;
		}
		
		Properties prop = new Properties();
		FileInputStream fis = null;
		String url = null;
		
		try {
			fis = new FileInputStream(fold);
			prop.load(fis);
			
			url = prop.getProperty("srcUrl");
			
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return url;
	}
	
	private void initDownloadInfoList(int option, int... args) {
		switch(option) {
		case 1:	initDownloadInfoListBynew(args[0], args[1]);
		break;
		
		case 2:initDownloadInfoListByold();
		break;
		}
	}
			
	public void startDownload() {
		//如果之前已经点击了开始下载按钮，并且发现线程处于暂停状态
		//那么这次开始是暂停之后恢复下载。
		//不是一直点击开始，也不是第一次开始还没。
		if(!isFirstStart && this.infoList.get(0).isPausing) {
			for(int i=0; i<this.numThread; i++) {
				this.infoList.get(i).isPausing = false;
			}
		}
		
		//如果是第一次开始下载
		if(isFirstStart) {
			this.isFirstStart = false;
			
			//如果不是断点续传
			if(!this.isRedownload) {
				int fileLen = this.getFileLength();
				initDownloadInfoList(1, fileLen, this.numThread);
			}else {
				initDownloadInfoList(2);
			}
			
			this.ui.addProgressBar(DownloadInfo.numThread);
			
			//保存url和numThread到属性文件
			prop.setProperty("numThread", DownloadInfo.numThread+"");
			prop.setProperty("srcUrl", DownloadInfo.srcUrl);
			
			for(int i=0; i<DownloadInfo.numThread; i++) {
				DownloadInfo info = infoList.get(i);
				new DownloadThread(info, this.url, this.dst, this.ui, this, prop).start();
			}
		}
	}
	
	public void pauseDownload() {
		for(int i=0; i<this.numThread; i++) {
			this.infoList.get(i).isPausing = true;
		}
	}
	
	public void saveMetaFile() {
		try {
			FileOutputStream fos = new FileOutputStream(DownloadInfo.downloadFilepath, false);
			this.prop.store(fos, null);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteMetaFile() {
		File file = new File(DownloadInfo.downloadFilepath);
		
		if(file.exists())
			file.delete();
	}
	
	public synchronized void addCompleteCount() {
		this.completeCount ++;
	}
	
	public boolean checkIsComplete() {
		if(this.completeCount == DownloadInfo.numThread)
			return true;
		
		return false;
	}
	
	public void reStartDownload() {
		this.ui.getPauseBtn().setEnabled(false);
		this.isFirstStart = true;
		this.completeCount = 0;
		this.deleteMetaFile();
	}
}
