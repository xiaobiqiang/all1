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
 * 显示销售情况条形图类，通过重绘实现
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
		this.fr = new JFrame("销售情况统计图");
		this.fr.setBounds(600, 400, 500, 500);
		this.fr.setLayout(new BoxLayout(this.fr.getContentPane(), BoxLayout.X_AXIS));
		
		this.fr.add(cp);
		this.fr.setVisible(true);
	}
	
	/**
	 * 主实现类(重绘对象类)
	 * @author Xiaobiqiang
	 *
	 */
	private class ChartPanel extends JPanel {
		//具体坐标算法解释见readme.md文件,Markdown文件(文本文件文法插图，没装office,太笨重的软件)
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			//画轴
			g.drawLine(50, this.getHeight()-100, 50, 50);
			g.drawLine(50, this.getHeight()-100, this.getWidth()-50, this.getHeight()-100);
			//画Y轴刻度
			for(int i=0; i<=10; i++) {
				g.drawString(i*10+"", 20, this.getHeight()-100-(this.getHeight()-150)/10*i);
			}
			
			//画条形图
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
			
			//画图名称
			g.setColor(Color.BLACK);
			g.setFont(new Font("宋体", Font.BOLD, 14));
			g.drawString("Sold", this.getWidth()/2, this.getHeight()-55);
		}
	}
	
	private JFrame fr = null;
	private ChartPanel cp = null;
	
	private BookList bList = null;
}
