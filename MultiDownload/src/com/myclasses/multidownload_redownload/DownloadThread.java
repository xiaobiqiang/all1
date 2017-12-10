package com.myclasses.multidownload_redownload;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class DownloadThread extends Thread {
	private String url = null;
	private String dst = null;
	
	private DownloadInfo info = null;
	private DownloadManager manager = null;
	private MultiDownloadUI ui = null; 
	
	private Properties prop = null;
	
	private int tempAllFinished = 0;
	
	public DownloadThread(DownloadInfo info, String url, String dst, MultiDownloadUI ui, DownloadManager manager, Properties p) {
		super();
		
		this.info = info;
		this.url = url;
		this.dst = dst;
		
		this.ui = ui;
		this.ui.getProgressBar(this.info.index).setMaximum(this.info.originLength);
		this.ui.getProgressBar(this.info.index).setValue(this.info.hasAllFinished);
		
		this.tempAllFinished = this.info.hasAllFinished;
		
		this.manager = manager;
		this.prop = p;
/*		this.bar = ui.getProgressBar(this.info.index);
		this.bar.setMaximum(this.info.length);*/
	}

	@Override
	public void run() {
		RandomAccessFile raf = null;
		BufferedInputStream bis = null;
		InputStream is = null;
		try {
			prop.setProperty("thread."+info.index+".start", info.start+"");
			prop.setProperty("thread."+info.index+".length", info.length+"");
			prop.setProperty("thread."+info.index+".originLength", info.originLength+"");
//			prop.store(fos, null);
			
			if(info.length > 0) {
				URL rUrl = new URL(this.url);
				HttpURLConnection conn = (HttpURLConnection)rUrl.openConnection();
				conn.setRequestProperty("Range", "bytes=" + this.info.start + "-" + (this.info.start+info.length-1));
				
				raf = new RandomAccessFile(dst, "rw");
				raf.seek(info.start);
				
				is = conn.getInputStream();
				bis = new BufferedInputStream(is);
				byte[] buffer = new byte[1024*128];
				int len = -1;
				int hasWrite = 0;
				
				while((len=bis.read(buffer)) != -1) {
					while(this.info.isPausing) {
						Thread.sleep(50);
					}
					
					hasWrite += len;
					info.hasFinished = hasWrite;
					prop.setProperty("thread." + info.index + ".finished", info.hasFinished+"");
					info.hasAllFinished = this.tempAllFinished + info.hasFinished;
					prop.setProperty("thread." + info.index + ".hasAllFinished", info.hasAllFinished+"");
					
					raf.write(buffer, 0, len);
					this.updateBar(info.hasFinished);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bis.close();
				raf.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		this.manager.addCompleteCount();
		if(this.manager.checkIsComplete())
			this.manager.reStartDownload();
	}
	
	private void updateBar(int value) {
		this.ui.getProgressBar(this.info.index).setValue(this.tempAllFinished + value);
	}
}
