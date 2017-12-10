package com.qq1312952829.classes;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 销售记录列表类，类似BookList类
 * @author Xiaobiqiang
 *
 */
public class SaleList {
	/**
	 * 保存了所有书籍的销售记录
	 */
	private List<Sale> sList = new ArrayList<>();
	
	/**
	 * 该类主要是记录一本书所有的销售记录
	 * @author Xiaobiqiang
	 *
	 */
	static class Sale{
		/**
		 * 所有的销售过这本书的销售员
		 */
		private String[] salers = null;
		private BookList.Book book = null;
		/**
		 * 所有销售过这本书的数量记录
		 */
		private int[] num = null;
		
		public String[] getSaler() {
			return salers;
		}
		
		public void setSaler(String[] saler) {
			this.salers = saler;
		}
		
		public BookList.Book getBook() {
			return book;
		}
		
		public void setBook(BookList.Book book) {
			this.book = book;
		}
		
		public int[] getNum() {
			return num;
		}
		
		public void setNum(int[] num) {
			this.num = num;
		}
	}
	
	/**
	 * 将所有书籍的销售记录情况写入文件中
	 */
	void writeSaleTxt() {
		String salesTxtPath = "resources/sales.txt";
		BufferedWriter bw = null;
		BookList.Book b = null;
		try {
			bw = new BufferedWriter(new FileWriter(salesTxtPath));
			
			bw.write("\t图书销售统计");
			bw.newLine();
			bw.write("=================================\n");
			bw.newLine();
			for(int i=0; i<sList.size(); i++) {
				b = sList.get(i).book;
				bw.write(b.getBookId() + "号  " + b.getBookName() + ": 单价  " + b.getBookPrice());
				bw.newLine();
				
				int allNum = 0;
				for(int j=0; j<sList.get(i).salers.length; j++) {
					bw.write(sList.get(i).salers[j] + "\t\t" + sList.get(i).num[j] + " @ Y " + b.getBookPrice() + " = Y " + (sList.get(i).num[j]*b.getBookPrice()));
					bw.newLine();
					allNum += sList.get(i).num[j];
				}
				
				bw.write("========================================");
				bw.newLine();
				bw.write("总销量： " + allNum + "\t\tY " + (allNum*sList.get(i).book.getBookPrice()));
				bw.newLine();
				bw.write("========================================");
				bw.newLine();
				bw.newLine();
				bw.newLine();
			}
			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public List<Sale> getSaleList(){
		return sList;
	}
}
