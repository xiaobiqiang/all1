package com.qq1312952829.screenbroadcast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.ImageIcon;

import com.myclasses.mathtools.NumberConvert;

public class ScreenBroadcastReceiver extends Thread {
	/**
	 * ������յ�ͼƬ��С����Map,�԰���ʱ�����Ϊkey,��ͬʱ����İ�����ͬһ��value��:TreeSet����,
	 * ˵����Щ����ͬʱ�����С����һ����������Ļ��ͼ�����ݡ�
	 * TreeSet����С���ĵ�12λ����16λ(��0��ʼ���1λ��������16λ)ת��intֵ�Ĵ�С��Ϊ�������ݣ�Ҳ����
	 * С���ڴ���е���š�
	 */
	private volatile TreeMap<Long, TreeSet<ReceivedSortedData>> packMap = new TreeMap<>();
	//��¼��ǰ(����)���յ����ݵ�ʱ���
	private volatile long dataTime = 0L; 
	
	private ScreenBroadcastReceiverUI mainUI = null;
	
	public ScreenBroadcastReceiver(ScreenBroadcastReceiverUI u) {
		this.mainUI = u;
	}
	
	@Override
	public void run() {
		try {
			DatagramSocket receiver = new DatagramSocket(9999);
			//������datagram��size��Ϊ���ջ�������ȷ�����ݽ�����ȫ��
			byte[] buffer = new byte[1024*64];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			//��һ�ν��յ���ʱ��������UI��label���̣߳�ʵ����ʾ��
			//�����յ����ݾͲ����ٿ�����
			boolean first = true;
			
			while(true) {				
				receiver.receive(packet);
				//��ȡʱ���
				byte[] temp = new byte[8];
				System.arraycopy(packet.getData(), 0, temp, 0, 8);
//				dataTime = this.byteArray2Long(temp);
				dataTime = NumberConvert.byteArrayCvtLong(temp, 0);
				
				//����ReceivedSortedData��������(UDP���յ����ݳ��ȿ�����packet.getLength()��ȡ)
				ReceivedSortedData dataSort = new ReceivedSortedData();
				
				//���ݳ���λ�ڵ�16λ����20λ��
				byte[] dataLen = new byte[4];
				System.arraycopy(packet.getData(), 16, dataLen, 0, 4);
				//data����Ӧ���Ǵ�ͼ�����ݳ���+ͷ�ĳ���(0-19,��20λ)
				dataSort.data = new byte[NumberConvert.byteArrayCvtInt(dataLen, 0)+20];
				
				System.arraycopy(packet.getData(), 0, dataSort.data, 0, dataSort.data.length);
				
				//����ͬʱ����ķ���ͬһ��TreeSet�У�����ͬһ��ʱ�������ӽ�Map��
				if(packMap.containsKey(dataTime)) {
					packMap.get(dataTime).add(dataSort);
				}else {
					TreeSet<ReceivedSortedData> dataSet = new TreeSet<>();
					dataSet.add(dataSort);
					packMap.put(dataTime, dataSet);
				}
				
				//���յ�����֮��Ͳ����ٿ����ˡ�
				if(first) {
					new UpdateUI().start();
					first = false;
				}				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
/*	private long byteArray2Long(byte[] b) throws Exception {
		if(null == b || b.length != 8)
			throw new Exception("length of the array is not equal to 8");
		long result = 0;
		
		for(int i=0; i<8; i++)
			result |= (long)(b[i] & 0xff) << (8*i);
		
		return result;
	}*/
	
	/**
	 * ���ౣ����յ�packet���ݣ�ʵ��Comparable�ӿڣ�������Map��ʵ������
	 * ����һ��������Entry<Long, TreeSet<ReceivedSortedData>>��Ӧ��Long�� TreeSet<ReceivedSortedData>�ĵ�һ��
	 * Ԫ���ϣ����һ�����������ʵ������
	 */
	class ReceivedSortedData implements Comparable<ReceivedSortedData> {
		private byte[] data = null;
		
		@Override
		public int compareTo(ReceivedSortedData o) {
			if(null == o)
				throw new NullPointerException("the obj to compare is null");

			if(this == o)
				return 0;
			
			byte[] toInt = new byte[4];
			System.arraycopy(this.data, 12, toInt, 0, 4);
//			int thisNum = this.byteArray2Int(toInt);
			int thisNum = NumberConvert.byteArrayCvtInt(toInt, 0);
			System.arraycopy(o.data, 12, toInt, 0, 4);
			int objNum = NumberConvert.byteArrayCvtInt(toInt, 0);
			
			if(thisNum == objNum) return 0;
			else if(thisNum > objNum) return 1;
			else return -1;
		}
		
/*		private int byteArray2Int(byte[] b) {
			if(b.length != 4)
				throw new NumberFormatException("length of the array is not equal to 4");
			
			int i = (b[0] & 0xff) | ((b[1] & 0xff) << 8) | ((b[2] & 0xff) << 16) | ((b[3] & 0xff) << 24);
			return i;
		}*/
		
		/**
		 * ��ȡ����ͷ����Ϣ�����ݳ��ȡ�
		 */
		public int getDataUsefulLength() {
			if(data == null)
				return 0;
			
			byte[] t = new byte[4];
			System.arraycopy(data, 16, t, 0, 4);
			
			return NumberConvert.byteArrayCvtInt(t, 0);
		}
	}
	
	class UpdateUI extends Thread {	
		@Override
		public void run() {
			while(true) {
				Entry<Long, TreeSet<ReceivedSortedData>> firstEntry = packMap.firstEntry();
				//���ʱ��Ҫע�⣺�߳����������ͺܿ죬�����յ��ٶȱȽ�����
				//��˿��ܽ������һ�������Ĵ����ˢ����label��ɾ���ˣ�
				//����û���յ���һ��ͼ��İ������ʱ��firstEntry����null�����Ҫ�ж��Ƿ���null��
				if(null != firstEntry) {
					Long firstKey = firstEntry.getKey();
					TreeSet<ReceivedSortedData> sortSet = firstEntry.getValue();
					//�����һ��(���絽��ģ�Ҳ����ʾ�ǰ��)�Ѿ���������С��
					//��ô��Ӧ�ð������Ļͼ����ʾ
					if(sortSet.size() == getFirstNumPackets(sortSet.first())) {
						byte[] buffer = getScreenByteArray(sortSet);  //��С�������ݺϳɴ����
						packMap.remove(firstKey); //������ʾ֮��Ӧ�ð����Ƴ����ú�һ֡��ͼ����Ϣ����
						updateUI(buffer);
					}else {
						//�����ڽ��յ�ͼ��ʱ��ȵ�һ����û��ʾ��ͼ��ʱ������һ����ֵ��
						//��ôӦ�ðѵ�һ����ĻͼƬɾ����
						Entry<Long, TreeSet<ReceivedSortedData>> lastEntry = packMap.lastEntry();
						checkTimedOutFirst(firstKey, lastEntry.getKey());
					}
				}
			}
		}
		
		/**
		 *��ȡһ��ͼƬ��ɢ��С�������� 
		 */
		public int getFirstNumPackets(ReceivedSortedData first) {
			byte[] numArray = new byte[4];
			System.arraycopy(first.data, 8, numArray, 0, 4);
			
			return NumberConvert.byteArrayCvtInt(numArray, 0);
		}
		
		/**
		 *С���ϳɴ�� 
		 */
		public byte[] getScreenByteArray(TreeSet<ReceivedSortedData> set) {
			int len = 0;
			Iterator<ReceivedSortedData> iter = set.iterator();
			
			while(iter.hasNext()) {
				len += iter.next().getDataUsefulLength();
			}
			//���µ�һ�������������������Ǹ��������Ѿ���������ĩβ�ˡ�
			//�ر�ע�⣺iterator�������ĸ�λ���ˣ�����һ�������ʹ�ü���next����ʾ��ͬ�Ķ��󣬴���ģ�����������������
			byte[] buffer = new byte[len];
			Iterator<ReceivedSortedData> iter2 = set.iterator();
			int preEnd = 0; //ǰһ��С���ڴ����ĩβλ�á�
			
			while(iter2.hasNext()) {
				ReceivedSortedData dataSorted = iter2.next();
				System.arraycopy(dataSorted.data, 20, buffer, preEnd, dataSorted.getDataUsefulLength());
				preEnd += dataSorted.getDataUsefulLength();
			}
			
			return buffer;
		}
		
		public void updateUI(byte[] imageBytes) {
			ImageIcon icon = new ImageIcon(imageBytes, "jpg");
			mainUI.getImageLabel().setIcon(icon);
			mainUI.getImageLabel().repaint();
		}
		
		public void checkTimedOutFirst(Long firstKey, Long lastKey) {			
			if((lastKey-firstKey) > 700) {
				packMap.remove(firstKey);
			}
		}
	}
}
