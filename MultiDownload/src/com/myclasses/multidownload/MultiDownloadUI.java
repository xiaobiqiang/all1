package com.myclasses.multidownload;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

public class MultiDownloadUI extends JFrame implements ActionListener{
	private static final long serialVersionUID = 32112215L;
	private JLabel urlLabel = null;
	private JLabel dstLabel = null;
	private JLabel numLabel = null;
	private JTextField urlText = null;
	private JTextField dstText = null;
	private JTextField numText = null;
	private JButton startBtn = null;
	private JButton pauseBtn = null;
	private JProgressBar[] bar = null;
	private DownloadManager manager = null;
//	private boolean isFirstPauseBtn = true;
	
	public MultiDownloadUI(String title){
		super(title);
		this.initUI();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void initUI() {
		this.setBounds(550, 200, 500, 450);
		this.setResizable(false);
		this.setLayout(null);
		
		urlLabel = new JLabel("网址", JLabel.CENTER);
		dstLabel = new JLabel("目标文件", JLabel.CENTER);
		numLabel = new JLabel("线程数", JLabel.CENTER);
		urlLabel.setBounds(50, 10, 100, 20);
		dstLabel.setBounds(50, 60, 100, 20);
		numLabel.setBounds(50, 110, 100, 20);
		
		urlText = new JTextField(100);
		dstText = new JTextField(100);
		numText = new JTextField(100);
		urlText.setBounds(140, 10, 300, 20);
		dstText.setBounds(140, 60, 300, 20);
		numText.setBounds(140, 110, 300, 20);
		
		startBtn = new JButton("开始下载");
		startBtn.setBounds(100, 170, 100, 30);
		startBtn.setHorizontalAlignment(JButton.CENTER);
		startBtn.addActionListener(this);
		
		pauseBtn = new JButton("暂停");
		pauseBtn.setBounds(300, 170, 100, 30);
		pauseBtn.setHorizontalAlignment(JButton.CENTER);
		pauseBtn.setEnabled(false);
		pauseBtn.addActionListener(this);
		
		this.add(urlLabel);
		this.add(dstLabel);
		this.add(numLabel);
		this.add(urlText);
		this.add(dstText);
		this.add(numText);
		this.add(startBtn);
		this.add(pauseBtn);
		
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.startBtn) {
			if(!this.pauseBtn.isEnabled()) {
				String url = this.urlText.getText();
				String dst = this.dstText.getText();
				int numThread = Integer.parseInt(this.numText.getText());
				this.addProgressBar(numThread);
				manager = new DownloadManager(this, url, dst, numThread);
				this.pauseBtn.setEnabled(true);
			}
			manager.startDownload();
		}
		
		if(e.getSource() == this.pauseBtn) {
			if(null != this.manager) {
				this.manager.pauseDownload();
			}
		}
	}
	
	public void addProgressBar(int num) {
		bar = new JProgressBar[num];
		
		for(int i=0; i<num; i++) {
			bar[i] = new JProgressBar();
			bar[i].setBackground(Color.lightGray);
			bar[i].setBounds(0, 240 + 40*i, 493, 30);
			this.add(bar[i]);
		}
	}
	
	public JProgressBar getProgressBar(int index) {
		return this.bar[index];
	}
	
	public JButton getPauseBtn() {
		return this.pauseBtn;
	}
}
