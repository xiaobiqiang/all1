package com.qq1312952829.classes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.qq1312952829.classes.BookList.Book;

/**
 * ��ʾ�����������ͼ�࣬ͨ���ػ�ʵ��
 * @author Xiaobiqiang
 *
 */
public class ChartFrame {
	public ChartFrame(BookList bList) {
		this.bList = bList;
	}
	
	public void displayChartFrameUI() {
		SwingUtilities.invokeLater(new ShowChartUI());
	}
	
	private class ShowChartUI implements Runnable {
		@Override
		public void run() {
			initComponents();
		}
	}
	
	private void initComponents() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.cp = new ChartPanel();
		this.fr = new JFrame("�������ͳ��ͼ");
		this.fr.setBounds(600, 400, 500, 500);
		this.fr.setLayout(new BoxLayout(this.fr.getContentPane(), BoxLayout.X_AXIS));
		
		this.fr.add(cp);
		this.fr.setVisible(true);
	}
	
	/**
	 * ��ʵ����(�ػ������)
	 * @author Xiaobiqiang
	 *
	 */
	private class ChartPanel extends JPanel {
		//���������㷨���ͼ�readme.md�ļ�,Markdown�ļ�(�ı��ļ��ķ���ͼ��ûװoffice,̫���ص����)
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			//����
			g.drawLine(50, this.getHeight()-100, 50, 50);
			g.drawLine(50, this.getHeight()-100, this.getWidth()-50, this.getHeight()-100);
			//��Y��̶�
			for(int i=0; i<=10; i++) {
				g.drawString(i*10+"", 20, this.getHeight()-100-(this.getHeight()-150)/10*i);
			}
			
			//������ͼ
			List<Book> l = bList.getBookList();
			int x = 0;
			for(int i=0; i<l.size(); i++) {
				g.setColor(Color.BLACK);
				x = 50+(this.getWidth()-100)/(3*l.size())*(1+3*i) ;
				g.drawString(l.get(i).getBookId(), x, this.getHeight()-80);
				g.setColor(Color.BLUE);
				int h = (int)((100-l.get(i).getBookRemain()) * (this.getHeight()-150)/100);
				g.fillRect(x, this.getHeight()-100-h, (this.getWidth()-100)/(3*l.size()), h);
			}
			
			//��ͼ����
			g.setColor(Color.BLACK);
			g.setFont(new Font("����", Font.BOLD, 14));
			g.drawString("Sold", this.getWidth()/2, this.getHeight()-55);
		}
	}
	
	private JFrame fr = null;
	private ChartPanel cp = null;
	
	private BookList bList = null;
}
