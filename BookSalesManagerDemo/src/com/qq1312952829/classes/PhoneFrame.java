package com.qq1312952829.classes;

import java.awt.Font;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * ��ʾ�����
 * @author Xiaobiqiang
 *
 */
public class PhoneFrame {
	//��Ҫ�����鼮���б�
	public PhoneFrame(BookList bList) {
		this.bList = bList;
	}
	
	public void displayRemainFrame() {
		SwingUtilities.invokeLater(new FrameExe());
	}
	
	private void initComponents() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		//����
		Object[] colNames = {"ͼ����", "����", "���"};
		TableModel model = new DefaultTableModel(colNames, bList.getBookList().size());
		this.t = new JTable(model);
		this.t.setRowHeight(24);
		this.t.setFont(new Font("����", Font.PLAIN, 14));
		this.t.setAutoscrolls(true);
		
		scrPane = new JScrollPane(t);
		
		fr = new JFrame("������");
		fr.add(scrPane);
		
		fr.setBounds(600, 400, 400, 300);
		fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		fr.setVisible(true);
	}
	
	/**
	 * ��������������鼮�ĵ�ǰ״̬��Ϣ
	 */
	private void addRecordToTable() {
		List<BookList.Book> l = this.bList.getBookList();
		for(int i=0; i<l.size(); i++) {
			this.t.getModel().setValueAt(l.get(i).getBookId(), i, 0);
			this.t.getModel().setValueAt(l.get(i).getBookName(), i, 1);
			this.t.getModel().setValueAt(l.get(i).getBookRemain(), i, 2);
		}
	}
	
	/**
	 * 
	 * @author Xiaobiqiang
	 *
	 */
	private class FrameExe implements Runnable {
		@Override
		public void run() {
			initComponents();
			addRecordToTable();
		}
	}
	
	private JTable t = null;
	private JScrollPane scrPane = null;
	private JFrame fr = null;
	
	private BookList bList = null;
}
