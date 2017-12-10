package com.myclasses.uiMultiThreadCopy;

import java.io.IOException;
import java.io.RandomAccessFile;

public class CopyThread extends Thread {
	private CopyInfo info = null;
	private CopyUI ui = null;
	private CopyManager manager = null;
	
	public CopyThread(CopyInfo info, CopyUI ui, CopyManager manager) {
		this.ui = ui;
		this.manager = manager;
		this.info = info;
		
		this.ui.getProgressBar(this.info.index).setMaximum(this.info.length);
	}

	@Override
	public void run() {
		long start = info.start;
		int length = info.length;
		
		RandomAccessFile srcRaf = null;
		RandomAccessFile dstRaf = null;
		
		byte[] buf = new byte[1024*128];
		int count = length%buf.length == 0 ? length/buf.length : (length/buf.length + 1);
		
		try {
			srcRaf = new RandomAccessFile(info.src, "r");
			dstRaf = new RandomAccessFile(info.dst, "rw");	
			srcRaf.seek(start);
			dstRaf.seek(start);
			
			int finished = 0;
			for(int i=0; i<count; i++) {
				if((length%buf.length != 0) && (i == count-1))
					buf = new byte[length%buf.length];
				
				srcRaf.readFully(buf);
				dstRaf.write(buf);
				
				finished += buf.length;
				this.ui.updateBar(finished, info.index);
			}
			
			this.manager.addCompleteCount();
		} catch (Exception e) {
		} finally {
			try {
				if(null != srcRaf)
					srcRaf.close();
				if(null != dstRaf)
					dstRaf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
