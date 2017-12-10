package com.myclasses.multidownload_redownload;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

public class MultiDownloadUI extends JFrame implements ActionListener{
	private static final long serialVersionUID = 32112215L;
	
	private JTextField urlText = null;
	private JTextField dstText = null;
	private JTextField numText = null;
	
	private JButton startBtn = null;
	private JButton pauseBtn = null;
	
	private JProgressBar[] bar = null;
	
	private DownloadManager manager = null;
	
	public MultiDownloadUI(String title){
		super(title);
		this.initUI();
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(!MultiDownloadUI.this.manager.checkIsComplete())
					MultiDownloadUI.this.manager.saveMetaFile();
				
				System.exit(0);
			}
		});
	}
	
	private void initUI() {
		this.setBounds(550, 200, 500, 450);
		this.setResizable(false);
		this.setLayout(null);
		
		JLabel urlLabel = new JLabel("网址", JLabel.CENTER);
		JLabel dstLabel = new JLabel("目标文件", JLabel.CENTER);
		JLabel numLabel = new JLabel("线程数", JLabel.CENTER);
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
			//pauseBtn不可用有三种情况：下载还没开始前，下载完，断点续传还没点击开始下载按钮。
			//前两种都需要重新读取文本框的内容，然后创建对象开始下载，
			//后一种不用，若是断点续传，我们仅需读取dstpath和url，然后不用创建整套对象。
			if(!this.pauseBtn.isEnabled()) {
				//判断是否是断点续传
				
				//获取downloading文件路径
				String dst = this.dstText.getText();
				File dstFile = new File(dst);
				//在ui类中尽量不去改变Info类中的值，ui类只获取值，然后传递给Manager类去使用，其他与他无关。
				//UI和业务要分开完成。
				String dstDownloading = dstFile.getAbsolutePath().substring(0, dstFile.getAbsolutePath().lastIndexOf("."))+".downloading";
				
				this.manager = new DownloadManager();
				String url = this.urlText.getText();
				//是断点续传
				if(null != url && url.equalsIgnoreCase(this.manager.checkOldDownload(dstDownloading))) {
					//是，不需要获取线程数量
					manager = new DownloadManager(this, url, dst, -1);
				}else {
					//不是，获取线程数量，从头开始
					int numThread = Integer.parseInt(this.numText.getText());
					manager = new DownloadManager(this, url, dst, numThread);
				}
/*				int numThread = Integer.parseInt(this.numText.getText());
				
				DownloadInfo.numThread = numThread;*/
//				File dstFile = new File(dst);
//				DownloadInfo.downloadFilepath = dstFile.getAbsolutePath().substring(0, dstFile.getAbsolutePath().lastIndexOf("."))+".downloading";
				
//				this.addProgressBar(DownloadInfo.numThread);
//				manager = new DownloadManager(this, url, dst, numThread);
				this.pauseBtn.setEnabled(true);
			}
			//暂停之后点击开始仍需继续下载，但是不用获取新的信息
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
