package com.qq1312952829.classes;

import javax.swing.SwingUtilities;

/**
 * �������ʾ���õĽӿ�
 *
 */
public class SaleSystemUI {
	
	public void displayUI() {
		SwingUtilities.invokeLater(new BookSalesFrame());
	}
}
