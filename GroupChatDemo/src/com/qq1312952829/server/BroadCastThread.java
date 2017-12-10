package com.qq1312952829.server;

import java.io.BufferedOutputStream;
import java.io.IOException;

public class BroadCastThread extends Thread {
	private BufferedOutputStream bos = null;
	private String msg = null;
	
	public BroadCastThread(BufferedOutputStream bos, String msg) {
		this.bos = bos;
		this.msg = msg;
	}
	
	@Override
	public void run() {
		try {
			System.out.println("BroadCastThread");
			bos.write(msg.getBytes());
			bos.flush();
		} catch (IOException e) {
		}
	}

}
