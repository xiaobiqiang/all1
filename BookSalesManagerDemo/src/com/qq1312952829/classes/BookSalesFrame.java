package com.qq1312952829.classes;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * 
 * @author Xiaobiqiang
 * 主窗口线程类，通过SaleSystemUI类调用显示
 * frame采用了BoxLayout，分为上中下三个Panel,采用BoxLayout.Y_AXIS.
 * 在组件中加入一定长度的，不可见的Struts组件来得到合适的界面
 *
 */
class BookSalesFrame extends MouseAdapter implements Runnable{
	public BookSalesFrame() {
		this.m = Manager.getInstance();
		this.m.setBookSalesFrame(this);
	}
	
	/**
	 * 线程执行的方法，用于初始化窗口的内容资源以及显示
	 */
	@Override
	public void run() {
		initFrame();
		initTopPanel();
		initMiddlePanel();
		initBottomPanel();
		addAll();
		initBtnsListener();
	}

	private void addAll() {
		this.mainFrame.add(this.topPnl);
		this.mainFrame.add(this.middlePnl);
		this.mainFrame.add(this.bottomPnl);
	}
	
	/**
	 * 上部面板初始化(添加三个按钮),新建了一个按钮面板采用BoxLayout.X_AXIS布局，先在水平方向
	 * 加入合适长度的Struts,控制好水平方向的布局，随后类成员变量topPnl采用BoxLayout.Y_AXIS布局，
	 * 通过在topBtnsPnl上下两侧加入适当长度的Struts来得到合适的竖直方向的布局
	 */
	private void initTopPanel() {
		this.topPnl = new JPanel();
		this.topPnl.setLayout(new BoxLayout(topPnl, BoxLayout.Y_AXIS));
		
		Font btnsFont = new Font("楷体", Font.PLAIN, 16);
		//上部分按钮
		this.showRemainBtn = new JButton("显示库存");
		this.showRemainBtn.setFont(btnsFont);
		this.showRemainBtn.setFocusPainted(false);
		
		this.saleImgBtn = new JButton("销售图");
		this.saleImgBtn.setFont(btnsFont);
		this.saleImgBtn.setFocusPainted(false);
		
		this.saveAndExitBtn = new JButton("保存并退出");
		this.saveAndExitBtn.setFont(btnsFont);
		this.saveAndExitBtn.setFocusPainted(false);
		
		JPanel topBtnPane = new JPanel();
		topBtnPane.setLayout(new BoxLayout(topBtnPane, BoxLayout.X_AXIS));
		topBtnPane.add(Box.createHorizontalStrut(100));
		topBtnPane.add(showRemainBtn);
		topBtnPane.add(Box.createHorizontalStrut(150));
		topBtnPane.add(saleImgBtn);
		topBtnPane.add(Box.createHorizontalStrut(150));
		topBtnPane.add(saveAndExitBtn);
		topBtnPane.add(Box.createHorizontalStrut(100));
		
		this.topPnl.add(Box.createVerticalStrut(20));
		this.topPnl.add(topBtnPane);
		this.topPnl.add(Box.createVerticalStrut(30));
	}
	
	/**
	 * 中部采用BoxLayout布局管理器，较为简单，直接采用BoxLayout.X_AXIS,在Table和边框之间加入水平方向的
	 * Struts即可。
	 */
	private void initMiddlePanel() {
		this.middlePnl = new JPanel();
		this.middlePnl.setLayout(new BoxLayout(this.middlePnl, BoxLayout.X_AXIS));
		
		Vector<String> colNames = new Vector<>(7);
		colNames.addElement("序号");
		colNames.addElement("编号");
		colNames.addElement("书名");
		colNames.addElement("销售员");
		colNames.addElement("数量");
		colNames.addElement("单价");
		colNames.addElement("总价");
		TableModel tblMdl = new DefaultTableModel(colNames, 50);
		this.salingTbl = new JTable(tblMdl);
		this.salingTbl.setRowHeight(24);
		this.salingTbl.setFont(new Font("楷体", Font.PLAIN, 14));
		this.salingTbl.setAutoscrolls(true);
		
		JScrollPane srollPane = new JScrollPane(this.salingTbl);
		
		this.middlePnl.add(Box.createHorizontalStrut(20));
		this.middlePnl.add(srollPane);
		this.middlePnl.add(Box.createHorizontalStrut(20));
	}
	
	/**
	 * 下部布局采用了较多的面板由里向外一层层包装好，与topPnl类似。
	 */
	private void initBottomPanel() {
		this.bottomPnl = new JPanel();
		this.bottomPnl.setLayout(new BoxLayout(bottomPnl, BoxLayout.X_AXIS));
		
		Font btnsFont = new Font("楷体", Font.PLAIN, 16);
		//上部分按钮
		this.commitBtn = new JButton("提交");
		this.commitBtn.setFont(btnsFont);
		this.commitBtn.setFocusPainted(false);
		
		this.cleanBtn = new JButton("清除");
		this.cleanBtn.setFont(btnsFont);
		this.cleanBtn.setFocusPainted(false);
		
		JPanel bottomLeftMiddlePane = new JPanel();
		bottomLeftMiddlePane.setLayout(new BoxLayout(bottomLeftMiddlePane, BoxLayout.Y_AXIS));
		bottomLeftMiddlePane.add(Box.createVerticalStrut(25));
		bottomLeftMiddlePane.add(commitBtn);
		bottomLeftMiddlePane.add(Box.createVerticalStrut(60));
		bottomLeftMiddlePane.add(cleanBtn);
		bottomLeftMiddlePane.add(Box.createVerticalStrut(25));
		
		JPanel buttomLeftPane = new JPanel();
		buttomLeftPane.setLayout(new BoxLayout(buttomLeftPane, BoxLayout.X_AXIS));
		buttomLeftPane.add(Box.createHorizontalStrut(100));
		buttomLeftPane.add(bottomLeftMiddlePane);
		buttomLeftPane.add(Box.createHorizontalStrut(250));
		
		Font lblFont = new Font("楷体", Font.PLAIN, 16);
		Font txtFont = new Font("楷体", Font.PLAIN, 18);
		//初始化Label
		this.numLbl = new JLabel("数    量", JLabel.RIGHT);
		this.numLbl.setFont(lblFont);
		
		this.bookNumLbl = new JLabel("图书编号", JLabel.RIGHT);
		this.bookNumLbl.setFont(lblFont);
		
		this.salerLbl = new JLabel("销 售 员", JLabel.RIGHT);
		this.salerLbl.setFont(lblFont);
		//初始化TextField
		this.numTxt = new JTextField(30);
		this.numTxt.setFont(txtFont);
		
		this.bookNumTxt = new JTextField(30);
		this.bookNumTxt.setFont(txtFont);
		
		this.salerTxt = new JTextField(30);
		this.salerTxt.setFont(txtFont);
		
		JPanel bottomRightMiddleLeftPane = new JPanel();
		bottomRightMiddleLeftPane.setLayout(new BoxLayout(bottomRightMiddleLeftPane, BoxLayout.Y_AXIS));
		bottomRightMiddleLeftPane.add(numLbl);
		bottomRightMiddleLeftPane.add(Box.createVerticalStrut(25));
		bottomRightMiddleLeftPane.add(bookNumLbl);
		bottomRightMiddleLeftPane.add(Box.createVerticalStrut(25));
		bottomRightMiddleLeftPane.add(salerLbl);
		
		JPanel bottomRightMiddleRightPane = new JPanel();
		bottomRightMiddleRightPane.setLayout(new BoxLayout(bottomRightMiddleRightPane, BoxLayout.Y_AXIS));
		bottomRightMiddleRightPane.add(Box.createVerticalStrut(20));
		bottomRightMiddleRightPane.add(numTxt);
		bottomRightMiddleRightPane.add(Box.createVerticalStrut(10));
		bottomRightMiddleRightPane.add(bookNumTxt);
		bottomRightMiddleRightPane.add(Box.createVerticalStrut(10));
		bottomRightMiddleRightPane.add(salerTxt);
		bottomRightMiddleRightPane.add(Box.createVerticalStrut(20));
		
		JPanel bottomRightPane = new JPanel();
		bottomRightPane.setLayout(new BoxLayout(bottomRightPane, BoxLayout.X_AXIS));
		bottomRightPane.add(bottomRightMiddleLeftPane);
		bottomRightPane.add(Box.createHorizontalStrut(20));
		bottomRightPane.add(bottomRightMiddleRightPane);
		bottomRightPane.add(Box.createHorizontalStrut(80));
		
		this.bottomPnl.add(buttomLeftPane);
		this.bottomPnl.add(bottomRightPane);
	}

	private void initFrame() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		//设置初始位置在屏幕中间，size:800*600
		this.mainFrame = new JFrame("图书销售管理");
		this.mainFrame.setBounds((int) (d.getWidth()/2-400), (int) (d.getHeight()/2-300), 800, 600);
		this.mainFrame.setLayout(new BoxLayout(this.mainFrame.getContentPane(), BoxLayout.Y_AXIS));
		this.mainFrame.setResizable(false);
		
		this.mainFrame.setVisible(true);
		this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * 添加按钮监听器
	 */
	private void initBtnsListener() {
		this.showRemainBtn.addMouseListener(this);
		this.saleImgBtn.addMouseListener(this);
		this.saveAndExitBtn.addMouseListener(this);
		this.commitBtn.addMouseListener(this);
		this.cleanBtn.addMouseListener(this);
	}

	/**
	 * 鼠标点击事件监听方法
	 */
	@Override
	public void mouseClicked(MouseEvent e) { 
		//不是鼠标左键点击，直接返回
		if(e.getButton() != MouseEvent.BUTTON1)
			return;
		
		if(e.getSource() == this.showRemainBtn) {
			this.m.showRemaining();
		} else if(e.getSource() == this.saleImgBtn) {
			this.m.showSaleImage();
		} else if(e.getSource() == this.saveAndExitBtn) {
			this.m.saveAndExit();
		} else if(e.getSource() == this.commitBtn) {
			this.m.commitSaleInfo();
		} else if(e.getSource() == this.cleanBtn) {
			this.m.cleanInfo();
		} else
			return;
	}
	
	/**
	 * 
	 * @param type  1：库存不够
	 * 				2：编号不对
	 * 				3：数量栏输入了不是数字的数量
	 * 				4.其他系统发生的错误
	 */
	public void showMessage(int type) {
		String message = type == 1 ?
				"库存不够！" : type == 2 ?
						"商品编号错误！" : type == 3 ? 
								"输入有误！" : "其他错误";
		JDialog log = new JDialog(this.mainFrame, "错误", true);
		log.add(new JLabel(message, JLabel.CENTER));
		log.setBounds(
				this.mainFrame.getX()+this.mainFrame.getWidth()/2-75, 
				this.mainFrame.getY()+this.mainFrame.getHeight()/2-50, 
				150, 
				100
				);
		log.setVisible(true);
		log.setAlwaysOnTop(true);
		
	}
	
	public JTextField getNumTxt() {
		return numTxt;
	}
	
	public JTextField getBookNumTxt() {
		return bookNumTxt;
	}
	
	public JTextField getSalerTxt() {
		return salerTxt;
	}
	
	public JTable getSalingTbl() {
		return salingTbl;
	}

	//功能按钮
	private JButton showRemainBtn = null;
	private JButton saleImgBtn = null;
	private JButton saveAndExitBtn = null;
	private JButton commitBtn = null;
	private JButton cleanBtn = null;
	
	private JLabel numLbl = null;
	private JLabel bookNumLbl = null;
	private JLabel salerLbl = null;
	//出售信息文本
	private JTextField numTxt = null;
	private JTextField bookNumTxt = null;
	private JTextField salerTxt = null;
	//提交销售的书籍的列表
	private JTable salingTbl = null;
	//主面板
	private JPanel topPnl = null;
	private JPanel middlePnl = null;
	private JPanel bottomPnl = null;
	
	private JFrame mainFrame = null;
	
	/**
	 * 管理器，枢纽，负责处理各个按钮的功能，或者将按钮的实现功能转移
	 * 到其他类处理，交通枢纽。
	 */
	private Manager m = null;
}
