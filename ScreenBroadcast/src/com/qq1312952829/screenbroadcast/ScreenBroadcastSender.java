package com.qq1312952829.screenbroadcast;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import javax.imageio.ImageIO;

import com.myclasses.mathtools.NumberConvert;

public class ScreenBroadcastSender {
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int DEFAULT_PACKET_SIZE = 1024*60;
	private static DatagramSocket sender = null;
	
	public static void main(String[] args) {
		ScreenBroadcastSender.startSend();
	}
	
	/**
	 * ץȡ��Ļͼ�񲢷��أ�ץȡʧ�ܷ���null.
	 */
	private static BufferedImage getScreenImage() {
		try {
			Robot bot = new Robot();
			BufferedImage screen = bot.createScreenCapture(new Rectangle(0, 0, (int)screenSize.getWidth(), (int)screenSize.getHeight()));
			return screen;
		} catch (AWTException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 *��ͼƬ����תΪ�ֽ�:ʹ��ByteArrayOutputStream��ImageIO.
	 *ʧ�ܷ���null,���򷵻��ֽ����顣 
	 */
	private static byte[] image2ByteArray(BufferedImage image) {
		if(null == image)
			return null;
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", baos);
			byte[] imageByte = baos.toByteArray();
			baos.close();
			return imageByte;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 *��������ݼ�����Ϣͷ�ض�ΪС������ ��
	 */
	private static DatagramPacket[] apartPacket(byte[] b) {
		if((null == b) || (b.length == 0))
			return null;
		
		int t = b.length / DEFAULT_PACKET_SIZE;
		int numPack = b.length%DEFAULT_PACKET_SIZE == 0 ? t : (t+1); //��������
		DatagramPacket[] packArray= new DatagramPacket[numPack];
		int lastLength = b.length - (numPack-1)*DEFAULT_PACKET_SIZE; //���һ������ͼ�����ݳ��ȡ�
		long time = System.currentTimeMillis();  //����ʱ���(���������Ƿ���ͬһʱ�̵���Ļͼ��İ�)
		
		for(int i=0; i<numPack; i++) {
			int len = i == (numPack-1) ? lastLength : DEFAULT_PACKET_SIZE;
			byte[] temp = new byte[len];
			
			System.arraycopy(b, DEFAULT_PACKET_SIZE*i, temp, 0, len);
			
			byte[] packData = addPacketDataHead(temp, time, numPack, i+1, len);
			
			packArray[i] = new DatagramPacket(packData, 0, packData.length);
			packArray[i].setSocketAddress(new InetSocketAddress("localhost", 9999));
		}
		
		return packArray;
	}
	
	/**
	 *��С����ͷ�������Ϣ�� 
	 */
	private static byte[] addPacketDataHead(byte[] b, long time, int numPackets, int count, int length) {
		if(null == b)
			return null;
		
		byte[] result = new byte[b.length + 20];
		
		System.arraycopy(b, 0, result, 20, b.length);
		
		System.arraycopy(NumberConvert.longCvtByteArray(time), 0, result, 0, 8);
		System.arraycopy(NumberConvert.intCvtByteArray(numPackets), 0, result, 8, 4);
		System.arraycopy(NumberConvert.intCvtByteArray(count), 0, result, 12, 4);
		System.arraycopy(NumberConvert.intCvtByteArray(length), 0, result, 16, 4);
		
		return result;
	}
/*	
	private static byte[] long2ByteArray(long n) {
		byte[] b = new byte[8];
		
		for(int i=0; i<b.length; i++) 
			b[i] = (byte)(n >> (8*i));
		
		return b;
	}*/
	
	public static boolean startSend() {	
		//ֻ����һ�����ͷ���
		if(null == sender) {
			synchronized(ScreenBroadcastSender.class) {
				if(null == sender) {
					try {
						sender = new DatagramSocket(new InetSocketAddress(8888));
					} catch (SocketException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		if(null == sender)
			return false;
		
		DatagramPacket[] packets = null;
		
		while(true) {
			packets = apartPacket(image2ByteArray(getScreenImage()));
			if(null == packets)
				return false;
			
			for(int i=0; i<packets.length; i++) {
				try {
					sender.send(packets[i]);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
