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
 * �������߳��࣬ͨ��SaleSystemUI�������ʾ
 * frame������BoxLayout����Ϊ����������Panel,����BoxLayout.Y_AXIS.
 * ������м���һ�����ȵģ����ɼ���Struts������õ����ʵĽ���
 *
 */
class BookSalesFrame extends MouseAdapter implements Runnable{
	public BookSalesFrame() {
		this.m = Manager.getInstance();
		this.m.setBookSalesFrame(this);
	}
	
	/**
	 * �߳�ִ�еķ��������ڳ�ʼ�����ڵ�������Դ�Լ���ʾ
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
	 * �ϲ�����ʼ��(���������ť),�½���һ����ť������BoxLayout.X_AXIS���֣�����ˮƽ����
	 * ������ʳ��ȵ�Struts,���ƺ�ˮƽ����Ĳ��֣�������Ա����topPnl����BoxLayout.Y_AXIS���֣�
	 * ͨ����topBtnsPnl������������ʵ����ȵ�Struts���õ����ʵ���ֱ����Ĳ���
	 */
	private void initTopPanel() {
		this.topPnl = new JPanel();
		this.topPnl.setLayout(new BoxLayout(topPnl, BoxLayout.Y_AXIS));
		
		Font btnsFont = new Font("����", Font.PLAIN, 16);
		//�ϲ��ְ�ť
		this.showRemainBtn = new JButton("��ʾ���");
		this.showRemainBtn.setFont(btnsFont);
		this.showRemainBtn.setFocusPainted(false);
		
		this.saleImgBtn = new JButton("����ͼ");
		this.saleImgBtn.setFont(btnsFont);
		this.saleImgBtn.setFocusPainted(false);
		
		this.saveAndExitBtn = new JButton("���沢�˳�");
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
	 * �в�����BoxLayout���ֹ���������Ϊ�򵥣�ֱ�Ӳ���BoxLayout.X_AXIS,��Table�ͱ߿�֮�����ˮƽ�����
	 * Struts���ɡ�
	 */
	private void initMiddlePanel() {
		this.middlePnl = new JPanel();
		this.middlePnl.setLayout(new BoxLayout(this.middlePnl, BoxLayout.X_AXIS));
		
		Vector<String> colNames = new Vector<>(7);
		colNames.addElement("���");
		colNames.addElement("���");
		colNames.addElement("����");
		colNames.addElement("����Ա");
		colNames.addElement("����");
		colNames.addElement("����");
		colNames.addElement("�ܼ�");
		TableModel tblMdl = new DefaultTableModel(colNames, 50);
		this.salingTbl = new JTable(tblMdl);
		this.salingTbl.setRowHeight(24);
		this.salingTbl.setFont(new Font("����", Font.PLAIN, 14));
		this.salingTbl.setAutoscrolls(true);
		
		JScrollPane srollPane = new JScrollPane(this.salingTbl);
		
		this.middlePnl.add(Box.createHorizontalStrut(20));
		this.middlePnl.add(srollPane);
		this.middlePnl.add(Box.createHorizontalStrut(20));
	}
	
	/**
	 * �²����ֲ����˽϶�������������һ����װ�ã���topPnl���ơ�
	 */
	private void initBottomPanel() {
		this.bottomPnl = new JPanel();
		this.bottomPnl.setLayout(new BoxLayout(bottomPnl, BoxLayout.X_AXIS));
		
		Font btnsFont = new Font("����", Font.PLAIN, 16);
		//�ϲ��ְ�ť
		this.commitBtn = new JButton("�ύ");
		this.commitBtn.setFont(btnsFont);
		this.commitBtn.setFocusPainted(false);
		
		this.cleanBtn = new JButton("���");
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
		
		Font lblFont = new Font("����", Font.PLAIN, 16);
		Font txtFont = new Font("����", Font.PLAIN, 18);
		//��ʼ��Label
		this.numLbl = new JLabel("��    ��", JLabel.RIGHT);
		this.numLbl.setFont(lblFont);
		
		this.bookNumLbl = new JLabel("ͼ����", JLabel.RIGHT);
		this.bookNumLbl.setFont(lblFont);
		
		this.salerLbl = new JLabel("�� �� Ա", JLabel.RIGHT);
		this.salerLbl.setFont(lblFont);
		//��ʼ��TextField
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
		//���ó�ʼλ������Ļ�м䣬size:800*600
		this.mainFrame = new JFrame("ͼ�����۹���");
		this.mainFrame.setBounds((int) (d.getWidth()/2-400), (int) (d.getHeight()/2-300), 800, 600);
		this.mainFrame.setLayout(new BoxLayout(this.mainFrame.getContentPane(), BoxLayout.Y_AXIS));
		this.mainFrame.setResizable(false);
		
		this.mainFrame.setVisible(true);
		this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * ��Ӱ�ť������
	 */
	private void initBtnsListener() {
		this.showRemainBtn.addMouseListener(this);
		this.saleImgBtn.addMouseListener(this);
		this.saveAndExitBtn.addMouseListener(this);
		this.commitBtn.addMouseListener(this);
		this.cleanBtn.addMouseListener(this);
	}

	/**
	 * ������¼���������
	 */
	@Override
	public void mouseClicked(MouseEvent e) { 
		//���������������ֱ�ӷ���
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
	 * @param type  1����治��
	 * 				2����Ų���
	 * 				3�������������˲������ֵ�����
	 * 				4.����ϵͳ�����Ĵ���
	 */
	public void showMessage(int type) {
		String message = type == 1 ?
				"��治����" : type == 2 ?
						"��Ʒ��Ŵ���" : type == 3 ? 
								"��������" : "��������";
		JDialog log = new JDialog(this.mainFrame, "����", true);
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

	//���ܰ�ť
	private JButton showRemainBtn = null;
	private JButton saleImgBtn = null;
	private JButton saveAndExitBtn = null;
	private JButton commitBtn = null;
	private JButton cleanBtn = null;
	
	private JLabel numLbl = null;
	private JLabel bookNumLbl = null;
	private JLabel salerLbl = null;
	//������Ϣ�ı�
	private JTextField numTxt = null;
	private JTextField bookNumTxt = null;
	private JTextField salerTxt = null;
	//�ύ���۵��鼮���б�
	private JTable salingTbl = null;
	//�����
	private JPanel topPnl = null;
	private JPanel middlePnl = null;
	private JPanel bottomPnl = null;
	
	private JFrame mainFrame = null;
	
	/**
	 * ����������Ŧ�������������ť�Ĺ��ܣ����߽���ť��ʵ�ֹ���ת��
	 * �������ദ����ͨ��Ŧ��
	 */
	private Manager m = null;
}
