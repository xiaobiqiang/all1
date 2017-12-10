package com.qq1312952829.screenbroadcast;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class ScreenBroadcastReceiverUI extends JFrame{
	private JLabel imageLabel = null;
	private ScreenBroadcastReceiver receiver = null;
	
	public ScreenBroadcastReceiverUI() {
		this.initUI();
	}
	
	private void initUI() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(0, 0, d.width, d.height);
		this.setLayout(null);
		
		imageLabel = new JLabel();
		imageLabel.setBounds(0, 0, this.getWidth(), this.getHeight());
		
		this.getContentPane().add(imageLabel);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	
	public void startReceive() {
		receiver = new ScreenBroadcastReceiver(this);
		receiver.start();
	}
	
	
	public JLabel getImageLabel() {
		return imageLabel;
	}

	public static void main(String[] args) {
		ScreenBroadcastReceiverUI receiverUI= new ScreenBroadcastReceiverUI();
		receiverUI.startReceive();
	}
	
}
