package com.qq1312952829.client;

import java.net.Socket;

public class ChatClient{
	public volatile boolean isContinue = true;
	
	public void ClientStart() {
		try {
			Socket client = new Socket("localhost", 8888);
			System.out.println("连接成功");
			new ReceiveMsgThread(client, this).start();
			new SendMsgThread(client, this).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
