package com.qq1312952829.server;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;

public class ChatServer {
	private static HashMap<Integer, HashMap<Socket, BufferedOutputStream>> clients = new HashMap<>();
	private static HashMap<Integer, HashSet<CopeMsgThread>> threadMap = new HashMap<>();
	
	public void ServerStart() {	
		try {
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(8888);
			
			while(true) {
				Socket s = server.accept();
				System.out.println("连进来了");
				new CopeMsgThread(s).start();;
			}
		} catch (IOException e) {
		}
	}

	public static HashMap<Integer, HashMap<Socket, BufferedOutputStream>> getClients() {
		return clients;
	}

	public static HashMap<Integer, HashSet<CopeMsgThread>> getThreadMap() {
		return threadMap;
	}
}
