package com.qq1312952829.classes;

import javax.swing.SwingUtilities;

/**
 * 给外界显示调用的接口
 *
 */
public class SaleSystemUI {
	
	public void displayUI() {
		SwingUtilities.invokeLater(new BookSalesFrame());
	}
}
