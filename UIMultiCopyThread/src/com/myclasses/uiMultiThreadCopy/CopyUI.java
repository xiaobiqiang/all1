package com.myclasses.uiMultiThreadCopy;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

public class CopyUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 16413643132L;
	
	private CopyManager manager = null;
	
	private JTextField srcText = null;
	private JTextField dstText = null;
	private JTextField numText = null;
	
	private JButton startBtn = null;
	
	private JProgressBar[] bar = null;
	
	public CopyUI(String title){
		super(title);
		this.initUI();
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
	
	private void initUI() {
		this.setBounds(550, 200, 500, 450);
		this.setResizable(false);
		this.setLayout(null);
		
		JLabel srcLabel = new JLabel("源文件", JLabel.CENTER);
		JLabel dstLabel = new JLabel("目标文件", JLabel.CENTER);
		JLabel numLabel = new JLabel("线程数", JLabel.CENTER);
		srcLabel.setBounds(50, 10, 100, 20);
		dstLabel.setBounds(50, 60, 100, 20);
		numLabel.setBounds(50, 110, 100, 20);
		
		srcText = new JTextField(100);
		dstText = new JTextField(100);
		numText = new JTextField(100);
		srcText.setBounds(140, 10, 300, 20);
		dstText.setBounds(140, 60, 300, 20);
		numText.setBounds(140, 110, 300, 20);
		
		startBtn = new JButton("开始复制");
		startBtn.setBounds(200, 170, 100, 30);
		startBtn.setHorizontalAlignment(JButton.CENTER);
		
		startBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					//获取文件路径以及所要的线程数量
					String src = srcText.getText();
					String dst = dstText.getText();
					int numThread = Integer.parseInt(numText.getText());
					
					//添加线程数量个进度条，以便显示每个线程的进度
					CopyUI.this.addProgressBars(numThread);
					CopyUI.this.repaint();
					
					//把信息传递给管理员，让管理员开始复制
					CopyUI.this.manager = new CopyManager(numThread, src, dst, CopyUI.this);
					CopyUI.this.manager.startCopy();
				}
			}
		});
		
		this.add(srcLabel);
		this.add(dstLabel);
		this.add(numLabel);
		this.add(srcText);
		this.add(dstText);
		this.add(numText);
		this.add(startBtn);
		
		this.setVisible(true);
	}
	
	private void addProgressBars(int num) {
		bar = new JProgressBar[num];
		
		for(int i=0; i<num; i++) {
			bar[i] = new JProgressBar();
			bar[i].setBackground(Color.lightGray);
			bar[i].setBounds(0, 240 + 40*i, 493, 30);
			this.add(bar[i]);
		}
	}
	
	public void updateBar(int value, int index) {
		this.bar[index].setValue(value);
	}
	
	public JProgressBar getProgressBar(int index) {
		return this.bar[index];
	}
}
