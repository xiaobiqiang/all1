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
	 * 保存接收的图片的小包的Map,以包的时间戳作为key,相同时间戳的包放在同一个value中:TreeSet对象,
	 * 说明这些个相同时间戳的小包是一个完整的屏幕截图的数据。
	 * TreeSet根据小包的第12位到第16位(从0开始算第1位，不含第16位)转成int值的大小作为排序依据，也就是
	 * 小包在大包中的序号。
	 */
	private volatile TreeMap<Long, TreeSet<ReceivedSortedData>> packMap = new TreeMap<>();
	//记录当前(最新)接收的数据的时间戳
	private volatile long dataTime = 0L; 
	
	private ScreenBroadcastReceiverUI mainUI = null;
	
	public ScreenBroadcastReceiver(ScreenBroadcastReceiverUI u) {
		this.mainUI = u;
	}
	
	@Override
	public void run() {
		try {
			DatagramSocket receiver = new DatagramSocket(9999);
			//以最大的datagram的size作为接收缓冲区，确保数据接收完全。
			byte[] buffer = new byte[1024*64];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			//第一次接收到包时开启更新UI中label的线程，实现显示。
			//随后接收到数据就不能再开启。
			boolean first = true;
			
			while(true) {				
				receiver.receive(packet);
				//获取时间戳
				byte[] temp = new byte[8];
				System.arraycopy(packet.getData(), 0, temp, 0, 8);
//				dataTime = this.byteArray2Long(temp);
				dataTime = NumberConvert.byteArrayCvtLong(temp, 0);
				
				//设置ReceivedSortedData对象数据(UDP接收的数据长度可以用packet.getLength()获取)
				ReceivedSortedData dataSort = new ReceivedSortedData();
				
				//数据长度位于第16位到第20位。
				byte[] dataLen = new byte[4];
				System.arraycopy(packet.getData(), 16, dataLen, 0, 4);
				//data长度应该是纯图像数据长度+头的长度(0-19,共20位)
				dataSort.data = new byte[NumberConvert.byteArrayCvtInt(dataLen, 0)+20];
				
				System.arraycopy(packet.getData(), 0, dataSort.data, 0, dataSort.data.length);
				
				//有相同时间戳的放在同一个TreeSet中，不是同一个时间戳就添加进Map中
				if(packMap.containsKey(dataTime)) {
					packMap.get(dataTime).add(dataSort);
				}else {
					TreeSet<ReceivedSortedData> dataSet = new TreeSet<>();
					dataSet.add(dataSort);
					packMap.put(dataTime, dataSet);
				}
				
				//接收到数据之后就不能再开启了。
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
	 * 该类保存接收的packet数据，实现Comparable接口，用于在Map中实现排序。
	 * 即第一个包放在Entry<Long, TreeSet<ReceivedSortedData>>对应的Long的 TreeSet<ReceivedSortedData>的第一个
	 * 元素上，最后一个包放在最后，实现排序。
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
		 * 获取不含头部信息的数据长度。
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
				//这个时候要注意：线程运行起来就很快，而接收的速度比较慢，
				//因此可能接收完第一个完整的大包后刷新完label后删除了，
				//若还没接收到下一个图像的包，这个时候firstEntry就是null。因此要判断是否是null。
				if(null != firstEntry) {
					Long firstKey = firstEntry.getKey();
					TreeSet<ReceivedSortedData> sortSet = firstEntry.getValue();
					//如果第一个(最早到达的，也是显示最靠前的)已经接收完了小包
					//那么就应该把这个屏幕图像显示
					if(sortSet.size() == getFirstNumPackets(sortSet.first())) {
						byte[] buffer = getScreenByteArray(sortSet);  //把小包的数据合成大包。
						packMap.remove(firstKey); //集齐显示之后应该把他移除，让后一帧的图像信息置首
						updateUI(buffer);
					}else {
						//若现在接收的图像时间比第一个还没显示的图像时间晚了一个阈值，
						//那么应该把第一个屏幕图片删掉。
						Entry<Long, TreeSet<ReceivedSortedData>> lastEntry = packMap.lastEntry();
						checkTimedOutFirst(firstKey, lastEntry.getKey());
					}
				}
			}
		}
		
		/**
		 *获取一副图片打散的小包的数量 
		 */
		public int getFirstNumPackets(ReceivedSortedData first) {
			byte[] numArray = new byte[4];
			System.arraycopy(first.data, 8, numArray, 0, 4);
			
			return NumberConvert.byteArrayCvtInt(numArray, 0);
		}
		
		/**
		 *小包合成大包 
		 */
		public byte[] getScreenByteArray(TreeSet<ReceivedSortedData> set) {
			int len = 0;
			Iterator<ReceivedSortedData> iter = set.iterator();
			
			while(iter.hasNext()) {
				len += iter.next().getDataUsefulLength();
			}
			//重新得一个迭代器，别用上面那个，上面已经迭代到最末尾了。
			//特别注意：iterator迭代到哪个位置了，别在一个语句中使用几次next来表示相同的对象，错误的！！！！！！！！！
			byte[] buffer = new byte[len];
			Iterator<ReceivedSortedData> iter2 = set.iterator();
			int preEnd = 0; //前一个小包在大包的末尾位置。
			
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
