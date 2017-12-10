package com.qq1312952829.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
//import java.io.BufferedOutputStream;
//import java.io.BufferedReader;
import java.io.IOException;
//import java.io.InputStreamReader;
import java.net.Socket;

public class ReceiveMsgThread extends Thread{
	private Socket client = null;
	private ChatClient cc = null;
	public ReceiveMsgThread(Socket s, ChatClient cc) {
		this.client = s;
		this.cc = cc;
	}
	
	@Override
	public void run() {
		try {
//			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			BufferedInputStream bis = new BufferedInputStream(client.getInputStream());
			FileOutputStream fos = new FileOutputStream("H:/c.txt", true);
			
			byte[] buf = new byte[1024*8];
			int len = -1;
			
			while(cc.isContinue) {
//				String command = br.readLine();
//				System.out.print(command);
//				bos.write(command.getBytes());
//				bos.flush();
				len = bis.read(buf);
				String msg = new String(buf, 0, len);
				msgSwitch(msg, fos);
//				System.out.println(msg);
				cc.isContinue = msg.equalsIgnoreCase("@5@@01") ? false : true;
			}
			
//			br.close();
			bis.close();
			fos.close();
//			bos.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void msgSwitch(String msg, FileOutputStream fos) {
		switch(msg) {
			case "@0@@00" : System.out.println("你不在房间，不能发消息！");
						    break;
			case "@1@@00" : System.out.println("你已经创建了群聊，请先退出群聊！"); 
						    break;
			case "@1@@01" : System.out.println("该群聊已被创建，请重新创建群聊！"); 
						   	break;
			case "@1@@02" : System.out.println("创建群聊成功，现在可以发起群聊！"); 
			   			   	break;
			case "@2@@00" : System.out.println("你已经在群聊，请先推出群聊！"); 
			   			   	break;
			case "@2@@01" : System.out.println("该群聊房间不存在，重新输入！"); 
			   			   	break;
			case "@2@@02" : System.out.println("成功进入群聊！"); 
			   			   	break;
			case "@3@@00" : System.out.println("你不在群聊内，请先加入群聊！"); 
			   			   	break;
			case "@3@@01" : 
			case "@3@@02" : System.out.println("你是管理员，退出群聊成功，群聊已删除！"); 
			   			   	break;
			case "@3@@03" : System.out.println("成功退出群聊！"); 
						   	break;
			case "@4@@00" : System.out.println("你不是管理员，无权删除群聊！"); 
						   	break;
			case "@4@@01" : System.out.println("删除群聊成功！"); 
			   			   	break;
			case "@5@@00" : System.out.println("不可退出系统，请先退出房间！");
							break;
			case "@5@@01" : System.out.println("退出系统成功！");
							break;
			default: try {
						fos.write((msg+"\r\n").getBytes());
						fos.flush();
					 } catch (IOException e) {
						 e.printStackTrace();
					 }//System.out.println(msg);
		}
	}
}
