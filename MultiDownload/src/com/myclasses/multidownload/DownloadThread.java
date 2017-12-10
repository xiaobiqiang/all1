package com.myclasses.multidownload;

import java.io.BufferedInputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadThread extends Thread {
	private DownloadInfo info = null;
	private String url = null;
	private String dst = null;
	private DownloadManager manager = null;
	private MultiDownloadUI ui = null; 
	
	public DownloadThread(DownloadInfo info, String url, String dst, MultiDownloadUI ui, DownloadManager manager) {
		super();
		this.info = info;
		this.url = url;
		this.dst = dst;
		this.ui = ui;
		this.ui.getProgressBar(this.info.index).setMaximum(this.info.length);
		this.manager = manager;
/*		this.bar = ui.getProgressBar(this.info.index);
		this.bar.setMaximum(this.info.length);*/
	}

	public DownloadInfo getInfo() {
		return info;
	}

	@Override
	public void run() {
		try {
			URL rUrl = new URL(this.url);
			HttpURLConnection conn = (HttpURLConnection)rUrl.openConnection();
			conn.setRequestProperty("Range", "bytes=" + this.info.start + "-" + (this.info.start+info.length-1));
			
			RandomAccessFile raf = new RandomAccessFile(dst, "rw");
			raf.seek(info.start);
			
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
			byte[] buffer = new byte[1024*128];
			int len = -1;
			int hasWrite = 0;
			
			while((len=bis.read(buffer)) != -1) {
				while(this.info.isPausing) {
					Thread.sleep(50);
				}
				raf.write(buffer, 0, len);
				hasWrite += len;
				this.updateBar(hasWrite);
			}
			
			bis.close();
			raf.close();
			DownloadInfo.completeCount++;
			this.updatePauseBtn();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateBar(int value) {
		this.ui.getProgressBar(this.info.index).setValue(value);
	}
	
	public void updatePauseBtn() {
		if(DownloadInfo.completeCount == DownloadInfo.numThread) {
			this.ui.getPauseBtn().setEnabled(false);
			this.manager.setIsFirstStart(true);
			DownloadInfo.completeCount = 0;
		}
	}
}
