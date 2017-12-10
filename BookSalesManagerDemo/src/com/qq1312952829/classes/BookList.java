package com.qq1312952829.classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取的书籍的列表类，核心就是一个ArrayList<Book>
 * @author Xiaobiqiang
 *
 */
public class BookList {
	private List<Book> bList = new ArrayList<>();
	
	/**
	 * 书信息类，保存了书的信息
	 *
	 */
	static class Book {
		private String bId = null;
		private String bName = null;
		private float bPrice = 0.0f;
		private int bRemain = 100; //初始库存为100
		
		String getBookId() {
			return bId;
		}
		
		int getBookRemain() {
			return bRemain;
		}
		
		/**
		 * 给外界更新库存的接口，其他属性不能随意更改
		 * @param remain
		 */
		void setBookRemain(int remain) {
			this.bRemain = remain;
		}
		
		String getBookName() {
			return bName;
		}
		
		float getBookPrice() {
			return bPrice;
		}
	}
	
	public BookList() {
		initBookList();
	}
	
	/**
	 * 从硬盘文件中读取书籍信息
	 */
	private void initBookList() {
		String bookTxtPath = "resources/Books.txt";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(bookTxtPath));
			
			Book book = null;
			String line = null;
			for( ; (line=br.readLine()) != null; ){
				String[] ss = line.split(" |\\t"); //假设文本默认使用" "和"\t来分隔"
				/*
				 * 异常的应用，可以帮助我们省去一些其他的判断
				 * 我们第一次读的不是书的信息，而是头，可以利用异常直接处理了，
				 * 不用直接首先读一行再继续读下一行然后再处理，可以让程序更直观点
				 */
				try {
					book = new Book();
					int t = 0;
					//读取的时候如果发现有不止3个信息，那么只接收前三个
					Object[] os = new Object[3];
					for(int i=0; i<ss.length; i++) {
						if("".equals(ss[i]))
							continue;
						if(t < 3)
							os[t] = ss[i];
						t ++;
					}
					
					book.bId = (String)os[0];
					book.bName = (String)os[1];
					book.bPrice = Float.parseFloat((String)os[2]);
				} catch(Exception e) {
					//如果发现Float.parseFloat((String)os[2])出问题了
					//说明信息有错，我们直接跳过这本书，不添加到列表中。
					//其次第一行由于是头，所以肯定异常，我们直接开始下一行即可。
					//不用花额外的代码去处理文本第一行
					continue;
				}
				bList.add(book);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public List<Book> getBookList(){
		return bList;
	}
}
