package com.qq1312952829.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class CopeMsgThread extends Thread {
	private Socket sock = null;
	private int numRoom = Integer.MIN_VALUE;
	private boolean isInRoom = false;
	private boolean isCreated = false;
	private boolean isMaster = false;
	
	public CopeMsgThread(Socket s) {
		this.sock = s;
	}
	
	public void run() {
		try {
			BufferedInputStream bis = new BufferedInputStream(sock.getInputStream());
			BufferedOutputStream bos = new BufferedOutputStream(sock.getOutputStream());
			byte[] buf = new byte[1024*8];
			int len = -1;
			boolean isContinu = true;
			System.out.println("serverThread");
			while(isContinu && ((len = bis.read(buf)) != -1)) {
				String msg = new String(buf, 0, len);
				System.out.println(msg);
				String echo = msgSwitch(msg, bos);
				System.out.println(echo);
				if(null != echo) {
					bos.write(echo.getBytes());
					bos.flush();
				}
				isContinu = setIsContinuByEcho(echo);
			}
			
			bis.close();
			bos.close();
			sock.close();
		} catch (IOException e) {
		}
	}
	
	public String msgSwitch(String msg, BufferedOutputStream bos) {
		if(msg.startsWith("create_room")) {
			int num = Integer.parseInt(msg.substring(msg.lastIndexOf(" ") + 1));
			return createRoom(num, bos);
		}else if(msg.startsWith("enter_room")) {
			int num = Integer.parseInt(msg.substring(msg.lastIndexOf(" ") + 1));
			return enterRoom(num, bos);
		}else if("exit_room".equalsIgnoreCase(msg))
			return exitRoom();
		else if(msg.startsWith("delete_room")) 
			return deleteRoom();
		else if(msg.equalsIgnoreCase("exit_system"))
			return exitSystem();
		else
			return broadcastAll(msg);
	}

	public boolean setIsContinuByEcho(String echo) {
		if(null == echo) return true;
		switch(echo) {
			case "@5@@01" :
				return false;
			default:
				return true;
		}
	}
	
	public String createRoom(int num, BufferedOutputStream bos) {
		if(isCreated)
			return "@1@00";
		synchronized(CopeMsgThread.class) {
			Set<Integer> numSet = ChatServer.getClients().keySet();
			if(numSet.contains(num))
				return "@1@@01";
			HashMap<Socket, BufferedOutputStream> roomClients = new HashMap<>();
			roomClients.put(sock, bos);
			ChatServer.getClients().put(num, roomClients);
			HashSet<CopeMsgThread> tSet = new HashSet<>();
			tSet.add(this);
			ChatServer.getThreadMap().put(num, tSet);
			this.isMaster = true;
			this.isInRoom = true;
			this.numRoom = num;
			this.isCreated = true;
			return "@1@@02";
		}
	}
	
	public String enterRoom(int num, BufferedOutputStream bos) {
		if(isInRoom)
			return "@2@@00";
		Set<Integer> numSet = ChatServer.getClients().keySet();
		if(!numSet.contains(num))
			return "@2@@01";
		ChatServer.getClients().get(num).put(sock, bos);
		ChatServer.getThreadMap().get(num).add(this);
		this.numRoom = num;
		this.isInRoom = true;
		return "@2@@02";
	}
	
	public String exitRoom() {
		if(!isInRoom)
			return "@3@@00";
		if(isMaster) {
			return "@4@@00".equals(deleteRoom()) ? "@3@@01" : "@3@@02";
		}
		else {
			HashMap<Socket, BufferedOutputStream> clients = ChatServer.getClients().get(this.numRoom);
			clients.remove(sock);
			ChatServer.getThreadMap().get(numRoom).remove(this);
			this.isInRoom = false;
		}
		return "@3@@03";
	}
	
	public String deleteRoom() {
		if(!isMaster)
			return "@4@@00";
		broadcastAll("管理员解散了群聊！");
		ChatServer.getClients().remove(this.numRoom);
		
		HashSet<CopeMsgThread> tSet = ChatServer.getThreadMap().remove(this.numRoom);
		Iterator<CopeMsgThread> iter = tSet.iterator();
		while(iter.hasNext()) {
			CopeMsgThread t = iter.next();
			t.isInRoom = false;
		}
		
		this.isCreated = false;
		this.isInRoom = false;
		this.isMaster = false;
		return null;
	}
	
	private String exitSystem() {
		if(isInRoom)
			return "@5@@00";
		
		return "@5@@01";
	}
	
	public String broadcastAll(String msg) {
		if(!isInRoom)
			return "@0@@00";
		HashMap<Socket, BufferedOutputStream> clients = ChatServer.getClients().get(this.numRoom);	
		Set<Entry<Socket, BufferedOutputStream>> entrySet = clients.entrySet();
		Iterator<Entry<Socket, BufferedOutputStream>> iter = entrySet.iterator();
		
		while(iter.hasNext()) {
			Entry<Socket, BufferedOutputStream> entry = iter.next();
			new BroadCastThread(entry.getValue(), msg).start();
		}
		
		return null;
	}
}
