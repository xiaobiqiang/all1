package com.qq1312952829.classes;

import java.util.ArrayList;

import javax.swing.table.TableModel;
/**
 * @author Xiaobiqiang
 *������ʵ����
 */
public class Manager {
	private Manager() {
		bList = new BookList();
		sList = new SaleList();
		this.recordEveTimeSoldSalerAndNum = new ArrayList[bList.getBookList().size()];			
	}
	
	/**
	 * ��ʾ��淽��������ʵ������PhoneFrame����
	 */
	public void showRemaining() {
		new PhoneFrame(this.bList).displayRemainFrame();
	}
	
	/**
	 * ��ʾ������������ͼ����������ʵ����ChartFrame
	 */
	public void showSaleImage() {
		new ChartFrame(this.bList).displayChartFrameUI();
	}
	
	/**
	 * �÷�����Ҫ����⵵����������ÿ���ύ������saler���ֺ�����num�ĺϳɽ⵵
	 * �õ����۹��Ȿ������е�����Ա�����Լ�ÿ�����۵Ĵ������顣
	 * @param l ��¼��Щ����ÿ��������ۼ�¼(saler+num)�б�
	 * @param num �⵵���������Ŀ������
	 * @return �⵵�õ�������Աsaler����
	 */
	@SuppressWarnings("rawtypes")
	private String[] decodeSalerAndNum(ArrayList l, int[] num) {
		String[] saler = new String[l.size()];
		for(int i=0; i<l.size(); i++) {
			String str = (String)l.get(i);
			int indexFlag = str.lastIndexOf("#");
			num[i] = Integer.parseInt(str.substring(indexFlag + 1));
			saler[i] = str.substring(0, indexFlag);
		}
		
		return saler;
	}
	
	/**
	 * ��ť"������˳�"����ʵ�ַ���
	 */
	public void saveAndExit() {
		SaleList.Sale s = null;  //����һ��Sale(���ڱ���ÿ�����ۼ�¼����)������
		for(int i=0; i<bList.getBookList().size(); i++) {
			s = new SaleList.Sale();
			s.setBook(bList.getBookList().get(i)); //����������ۼ�¼���鼮(Book����)
			
			//����������۵ļ�¼����null��˵�����б����۹�
			if(recordEveTimeSoldSalerAndNum[i] != null) {
				//����һ��num�������ڱ���⵵֮��ĸ���ÿ�����۵���������С�͸������ۼ�¼�Ĵ���һ����
				int[] num = new int[recordEveTimeSoldSalerAndNum[i].size()];
				//�⵵
				String[] saler = decodeSalerAndNum(recordEveTimeSoldSalerAndNum[i], num);
				//����������ۼ�¼������������Ա
				s.setNum(num);
				s.setSaler(saler);
			}
			else {
				//�������û�б����۹�����ô��¼��Ϊ0
				s.setNum(new int[0]);
				s.setSaler(new String[0]);
			}
			//��¼�Ȿ����������ۼ�¼
			sList.getSaleList().add(s);
		}
		//д�뵽�ļ���
		this.sList.writeSaleTxt();
		//�˳�ϵͳ
		System.exit(0);
	}
	
	/**
	 * ��ť"�ύ"�Ĺ���ʵ�ַ���
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void commitSaleInfo() {
		//���Ȼ�ȡ����Ա�������Ϣ
		String num = this.uiFrame.getNumTxt().getText();
		String bid = this.uiFrame.getBookNumTxt().getText();
		String saler = this.uiFrame.getSalerTxt().getText();
		
		//����Ƿ�������Ϣ���󣬸��ݷ���ֵ����Ӧ�Ĵ���
		int swt = checkValidInput(bid, num);
		
		//�������0��˵���ڿ���б������Ȿ�鲢�ҿ��Ҳ�㹻
		if(swt >= 0) {
			TableModel model = this.uiFrame.getSalingTbl().getModel();
			//�õ��Ȿ��
			BookList.Book b = this.bList.getBookList().get(swt);
			//�ڱ��������
			model.setValueAt(orderNum+1, orderNum, 0);
			model.setValueAt(bid, orderNum, 1);
			model.setValueAt(b.getBookName(), orderNum, 2);
			model.setValueAt(saler, orderNum, 3);
			model.setValueAt(num, orderNum, 4);
			model.setValueAt(b.getBookPrice(), orderNum, 5);
			model.setValueAt(Integer.parseInt(num)*b.getBookPrice(), orderNum, 6);
			//���¿��
			b.setBookRemain(b.getBookRemain()-Integer.parseInt(num));
			//��¼������ۼ�¼
			//�����null��˵���Ȿ����ǰ��û�����۹����½�����������
			if(null == recordEveTimeSoldSalerAndNum[swt])
				recordEveTimeSoldSalerAndNum[swt] = new ArrayList();
			//�򵥹鵵
			recordEveTimeSoldSalerAndNum[swt].add(saler + "#" + num);
			//�������
			orderNum ++;
			this.cleanInfo();
		} else {
			//���������0��˵����Ϣ����
			switch(swt) {
			case Integer.MIN_VALUE:  //��治��
				this.uiFrame.showMessage(1); break;
			case Integer.MIN_VALUE+1: //û���Ȿ��
				this.uiFrame.showMessage(2); break;
			case Integer.MIN_VALUE+2: //�������������˴������Ϣ��������ĸ�ȵȶ���
				this.uiFrame.showMessage(3); break;
			default: 
			}
		}
	}
	
	public void cleanInfo() {
		uiFrame.getNumTxt().setText("");
		uiFrame.getBookNumTxt().setText("");
		uiFrame.getSalerTxt().setText("");
	}
	
	public void setBookSalesFrame(BookSalesFrame uiFrame) {
		this.uiFrame = uiFrame;
	}
	
	/**
	 * ����������Ϣ�Ƿ���ȷ��
	 * ��ȷ�����Ȿ����BookList�е�λ��
	 * ����Integer.MIN_VALUE����ʾ�����������
	 * 		Integer.MIN_VALUE+1��û���Ȿ��
	 * 		Integer.MIN_VALUE+2�������ʽ����
	 */
	private int checkValidInput(String bId, String bNum) {
		int n = bList.getBookList().size();
		
		try {
			int num = Integer.parseInt(bNum);
			BookList.Book b = null;
			
			for(int i = 0; i < n; i ++) {
				b = bList.getBookList().get(i);
				if(b.getBookRemain() >= num && b.getBookId().equals(bId))
					return i;
				if(b.getBookId().equals(bId) && b.getBookRemain()<num)
					return Integer.MIN_VALUE;
			}
		} catch(Exception e) {
			return Integer.MIN_VALUE+2;
		}
		
		return Integer.MIN_VALUE+1;
	}
	
	public static Manager getInstance() {
		if(null == m) {
			synchronized(Manager.class) {
				if(null == m)
					m = new Manager();
			}
		}
		
		return m;
	}
	
	private BookSalesFrame uiFrame = null;
	private BookList bList = null;
	private SaleList sList = null;
	/**
	 * ���ڼ�¼ÿ������������ۼ�¼(����Ա+����)������������б����ʽ,����HashMap��
	 * �����С���������һ����ÿ�������鼮�����ڶ�Ӧ������Ԫ�ص�ArrayList�ϼ�¼������ۼ�¼
	 * ��Ȼ���ǿ��Բ���TreeMap��HashMap������HashMapҪʵ������Ҫ����Collection.sort��������ָ���������TreeMap���ƣ�
	 * �鷳��һ���档��һ������ܻ��޷���ԭ����ȡ�����˳�򱣳�һ�¡�
	 * ������Map��put�����ĺ������ݽṹ˼�룬�������������
	 * ��Ȼ����һ�ַ��������ڱ����˳���ʱ�������Ȼ����ƴ�ӣ�������¼��ʱ�ȽϺ�ʱ�������ڴ����ʹ�ý��٣�
	 * ���õķ����ڴ����ıȽϴ������϶�ʱ������ʱ���٣�����Ĳ�ѯ�Ǻܿ�ġ�
	 * �����˳�򶼺Ͷ�ȡ��ʱ��һ���������ǣ����Ƽ�ǰ���Map����
	 */
	@SuppressWarnings("rawtypes")
	private ArrayList[] recordEveTimeSoldSalerAndNum = null;
	
	private int orderNum = 0;
	private static Manager m = null;
}
