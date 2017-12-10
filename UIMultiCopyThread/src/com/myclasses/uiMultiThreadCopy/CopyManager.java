package com.myclasses.uiMultiThreadCopy;

import java.io.RandomAccessFile;

public class CopyManager {
	private CopyUI ui = null;
	
	private int numThread = 0;
	private String src = null;
	private String dst = null;
	
	private int completeCount = 0;
	
	private CopyInfo[] infoArr = null;
	
	public CopyManager(int numThread, String src, String dst, CopyUI ui) {
		this.numThread = numThread;
		this.src = src;
		this.dst = dst;
		
		this.infoArr = new CopyInfo[this.numThread];
		this.ui = ui;
	}
	
	private boolean figureInfo() {
		try {
			RandomAccessFile raf = new RandomAccessFile(src, "r");
			long fileLen = raf.length();
			raf.close();
			
			for(int i=0; i<this.numThread; i++) {
				infoArr[i] = new CopyInfo();
				
				infoArr[i].src = this.src;
				infoArr[i].dst = this.dst;
				
				infoArr[i].index = i;
				
				infoArr[i].start = i==0 ? 0 : (infoArr[i-1].start + infoArr[i-1].length);
				infoArr[i].length = i<(this.numThread-1) ? 
										(int)(fileLen/this.numThread) : 
											(int)(fileLen-infoArr[i-1].start-infoArr[i-1].length);
			}
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	public void startCopy() {
		if(this.figureInfo()) {
			for(int i=0; i<this.numThread; i++) 
				new CopyThread(infoArr[i], this.ui, this).start();
		}
	}
	
	public void addCompleteCount() {
		this.completeCount ++;
	}
	
	public boolean checkIsComplete() {
		if(this.completeCount == this.numThread)
			return true;
		
		return false;
	}
}
