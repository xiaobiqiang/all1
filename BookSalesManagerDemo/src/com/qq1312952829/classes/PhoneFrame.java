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
 * 显示库存类
 * @author Xiaobiqiang
 *
 */
public class PhoneFrame {
	//需要所有书籍的列表
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
		//建表
		Object[] colNames = {"图书编号", "名称", "库存"};
		TableModel model = new DefaultTableModel(colNames, bList.getBookList().size());
		this.t = new JTable(model);
		this.t.setRowHeight(24);
		this.t.setFont(new Font("楷体", Font.PLAIN, 14));
		this.t.setAutoscrolls(true);
		
		scrPane = new JScrollPane(t);
		
		fr = new JFrame("库存情况");
		fr.add(scrPane);
		
		fr.setBounds(600, 400, 400, 300);
		fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		fr.setVisible(true);
	}
	
	/**
	 * 往表中添加所有书籍的当前状态信息
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
