package com.qq1312952829.classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ��ȡ���鼮���б��࣬���ľ���һ��ArrayList<Book>
 * @author Xiaobiqiang
 *
 */
public class BookList {
	private List<Book> bList = new ArrayList<>();
	
	/**
	 * ����Ϣ�࣬�����������Ϣ
	 *
	 */
	static class Book {
		private String bId = null;
		private String bName = null;
		private float bPrice = 0.0f;
		private int bRemain = 100; //��ʼ���Ϊ100
		
		String getBookId() {
			return bId;
		}
		
		int getBookRemain() {
			return bRemain;
		}
		
		/**
		 * �������¿��Ľӿڣ��������Բ����������
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
	 * ��Ӳ���ļ��ж�ȡ�鼮��Ϣ
	 */
	private void initBookList() {
		String bookTxtPath = "resources/Books.txt";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(bookTxtPath));
			
			Book book = null;
			String line = null;
			for( ; (line=br.readLine()) != null; ){
				String[] ss = line.split(" |\\t"); //�����ı�Ĭ��ʹ��" "��"\t���ָ�"
				/*
				 * �쳣��Ӧ�ã����԰�������ʡȥһЩ�������ж�
				 * ���ǵ�һ�ζ��Ĳ��������Ϣ������ͷ�����������쳣ֱ�Ӵ����ˣ�
				 * ����ֱ�����ȶ�һ���ټ�������һ��Ȼ���ٴ��������ó����ֱ�۵�
				 */
				try {
					book = new Book();
					int t = 0;
					//��ȡ��ʱ����������в�ֹ3����Ϣ����ôֻ����ǰ����
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
					//�������Float.parseFloat((String)os[2])��������
					//˵����Ϣ�д�����ֱ�������Ȿ�飬����ӵ��б��С�
					//��ε�һ��������ͷ�����Կ϶��쳣������ֱ�ӿ�ʼ��һ�м��ɡ�
					//���û�����Ĵ���ȥ�����ı���һ��
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
