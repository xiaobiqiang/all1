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
			case "@0@@00" : System.out.println("�㲻�ڷ��䣬���ܷ���Ϣ��");
						    break;
			case "@1@@00" : System.out.println("���Ѿ�������Ⱥ�ģ������˳�Ⱥ�ģ�"); 
						    break;
			case "@1@@01" : System.out.println("��Ⱥ���ѱ������������´���Ⱥ�ģ�"); 
						   	break;
			case "@1@@02" : System.out.println("����Ⱥ�ĳɹ������ڿ��Է���Ⱥ�ģ�"); 
			   			   	break;
			case "@2@@00" : System.out.println("���Ѿ���Ⱥ�ģ������Ƴ�Ⱥ�ģ�"); 
			   			   	break;
			case "@2@@01" : System.out.println("��Ⱥ�ķ��䲻���ڣ��������룡"); 
			   			   	break;
			case "@2@@02" : System.out.println("�ɹ�����Ⱥ�ģ�"); 
			   			   	break;
			case "@3@@00" : System.out.println("�㲻��Ⱥ���ڣ����ȼ���Ⱥ�ģ�"); 
			   			   	break;
			case "@3@@01" : 
			case "@3@@02" : System.out.println("���ǹ���Ա���˳�Ⱥ�ĳɹ���Ⱥ����ɾ����"); 
			   			   	break;
			case "@3@@03" : System.out.println("�ɹ��˳�Ⱥ�ģ�"); 
						   	break;
			case "@4@@00" : System.out.println("�㲻�ǹ���Ա����Ȩɾ��Ⱥ�ģ�"); 
						   	break;
			case "@4@@01" : System.out.println("ɾ��Ⱥ�ĳɹ���"); 
			   			   	break;
			case "@5@@00" : System.out.println("�����˳�ϵͳ�������˳����䣡");
							break;
			case "@5@@01" : System.out.println("�˳�ϵͳ�ɹ���");
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
