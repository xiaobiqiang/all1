package com.qq1312952829.client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SendMsgThread extends Thread {
	private Socket client = null;
	private ChatClient cc = null;
	public SendMsgThread(Socket s, ChatClient cc) {
		this.client = s;
		this.cc = cc;
	}
	
	@Override
	public void run() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			BufferedOutputStream bos = new BufferedOutputStream(client.getOutputStream());
			
			while(cc.isContinue) {
				String command = br.readLine();
				bos.write(command.getBytes());
				bos.flush();
			}
			
			br.close();
			bos.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
