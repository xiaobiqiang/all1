package com.qq1312952829.classes;

import java.util.ArrayList;

import javax.swing.table.TableModel;
/**
 * @author Xiaobiqiang
 *主功能实现类
 */
public class Manager {
	private Manager() {
		bList = new BookList();
		sList = new SaleList();
		this.recordEveTimeSoldSalerAndNum = new ArrayList[bList.getBookList().size()];			
	}
	
	/**
	 * 显示库存方法，具体实现是在PhoneFrame类中
	 */
	public void showRemaining() {
		new PhoneFrame(this.bList).displayRemainFrame();
	}
	
	/**
	 * 显示销售数量条形图方法，具体实现在ChartFrame
	 */
	public void showSaleImage() {
		new ChartFrame(this.bList).displayChartFrameUI();
	}
	
	/**
	 * 该方法主要负责解档操作，负责将每次提交产生的saler名字和数量num的合成解档
	 * 得到销售过这本书的所有的销售员数组以及每次销售的次数数组。
	 * @param l 记录这些书中每本书的销售记录(saler+num)列表
	 * @param num 解档数量数组的目的数组
	 * @return 解档得到的销售员saler数组
	 */
	@SuppressWarnings("rawtypes")
	private String[] decodeSalerAndNum(ArrayList l, int[] num) {
		String[] saler = new String[l.size()];
		for(int i=0; i<l.size(); i++) {
			String str = (String)l.get(i);
			int indexFlag = str.lastIndexOf("#");
			num[i] = Integer.parseInt(str.substring(indexFlag + 1));
			saler[i] = str.substring(0, indexFlag);
		}
		
		return saler;
	}
	
	/**
	 * 按钮"保存和退出"功能实现方法
	 */
	public void saveAndExit() {
		SaleList.Sale s = null;  //声明一个Sale(用于保存每次销售记录的类)的引用
		for(int i=0; i<bList.getBookList().size(); i++) {
			s = new SaleList.Sale();
			s.setBook(bList.getBookList().get(i)); //设置这次销售记录的书籍(Book对象)
			
			//如果该书销售的记录不是null，说明它有被销售过
			if(recordEveTimeSoldSalerAndNum[i] != null) {
				//定义一个num数组用于保存解档之后的该书每次销售的数量，大小和该书销售记录的次数一样大
				int[] num = new int[recordEveTimeSoldSalerAndNum[i].size()];
				//解档
				String[] saler = decodeSalerAndNum(recordEveTimeSoldSalerAndNum[i], num);
				//设置这次销售记录的数量和销售员
				s.setNum(num);
				s.setSaler(saler);
			}
			else {
				//如果该书没有被销售过，那么记录数为0
				s.setNum(new int[0]);
				s.setSaler(new String[0]);
			}
			//记录这本书的所有销售记录
			sList.getSaleList().add(s);
		}
		//写入到文件中
		this.sList.writeSaleTxt();
		//退出系统
		System.exit(0);
	}
	
	/**
	 * 按钮"提交"的功能实现方法
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void commitSaleInfo() {
		//首先获取销售员输入的信息
		String num = this.uiFrame.getNumTxt().getText();
		String bid = this.uiFrame.getBookNumTxt().getText();
		String saler = this.uiFrame.getSalerTxt().getText();
		
		//检查是否输入信息有误，根据返回值做相应的处理
		int swt = checkValidInput(bid, num);
		
		//如果大于0，说明在库存列表中有这本书并且库存也足够
		if(swt >= 0) {
			TableModel model = this.uiFrame.getSalingTbl().getModel();
			//得到这本书
			BookList.Book b = this.bList.getBookList().get(swt);
			//在表中添加它
			model.setValueAt(orderNum+1, orderNum, 0);
			model.setValueAt(bid, orderNum, 1);
			model.setValueAt(b.getBookName(), orderNum, 2);
			model.setValueAt(saler, orderNum, 3);
			model.setValueAt(num, orderNum, 4);
			model.setValueAt(b.getBookPrice(), orderNum, 5);
			model.setValueAt(Integer.parseInt(num)*b.getBookPrice(), orderNum, 6);
			//更新库存
			b.setBookRemain(b.getBookRemain()-Integer.parseInt(num));
			//记录这次销售记录
			//如果是null，说明这本书以前还没被销售过，新建对象来保存
			if(null == recordEveTimeSoldSalerAndNum[swt])
				recordEveTimeSoldSalerAndNum[swt] = new ArrayList();
			//简单归档
			recordEveTimeSoldSalerAndNum[swt].add(saler + "#" + num);
			//序号自增
			orderNum ++;
			this.cleanInfo();
		} else {
			//如果不大于0，说明信息有误
			switch(swt) {
			case Integer.MIN_VALUE:  //库存不够
				this.uiFrame.showMessage(1); break;
			case Integer.MIN_VALUE+1: //没有这本书
				this.uiFrame.showMessage(2); break;
			case Integer.MIN_VALUE+2: //在数量栏输入了错误的信息，比如字母等等东西
				this.uiFrame.showMessage(3); break;
			default: 
			}
		}
	}
	
	public void cleanInfo() {
		uiFrame.getNumTxt().setText("");
		uiFrame.getBookNumTxt().setText("");
		uiFrame.getSalerTxt().setText("");
	}
	
	public void setBookSalesFrame(BookSalesFrame uiFrame) {
		this.uiFrame = uiFrame;
	}
	
	/**
	 * 检查输入的信息是否正确。
	 * 正确返回这本书在BookList中的位置
	 * 错误：Integer.MIN_VALUE：表示超过库存数量
	 * 		Integer.MIN_VALUE+1：没有这本书
	 * 		Integer.MIN_VALUE+2：输入格式有误
	 */
	private int checkValidInput(String bId, String bNum) {
		int n = bList.getBookList().size();
		
		try {
			int num = Integer.parseInt(bNum);
			BookList.Book b = null;
			
			for(int i = 0; i < n; i ++) {
				b = bList.getBookList().get(i);
				if(b.getBookRemain() >= num && b.getBookId().equals(bId))
					return i;
				if(b.getBookId().equals(bId) && b.getBookRemain()<num)
					return Integer.MIN_VALUE;
			}
		} catch(Exception e) {
			return Integer.MIN_VALUE+2;
		}
		
		return Integer.MIN_VALUE+1;
	}
	
	public static Manager getInstance() {
		if(null == m) {
			synchronized(Manager.class) {
				if(null == m)
					m = new Manager();
			}
		}
		
		return m;
	}
	
	private BookSalesFrame uiFrame = null;
	private BookList bList = null;
	private SaleList sList = null;
	/**
	 * 用于记录每本书的所有销售记录(销售员+数量)，采用数组加列表的形式,类似HashMap，
	 * 数组大小与书的种类一样大，每次销售书籍，就在对应的数组元素的ArrayList上记录这次销售记录
	 * 当然我们可以采用TreeMap和HashMap，不过HashMap要实现排序要借助Collection.sort函数并且指明排序规则，TreeMap类似，
	 * 麻烦是一方面。另一方面可能还无法和原来读取的书的顺序保持一致。
	 * 因而借鉴Map的put方法的核心数据结构思想，利用数组加链表。
	 * 当然还有一种方法就是在保存退出的时候遍历表，然后在拼接，那样记录多时比较耗时，但是内存可能使用较少，
	 * 采用的方法内存消耗比较大，数量较多时，但耗时很少，数组的查询是很快的。
	 * 保存的顺序都和读取的时候一样，若不是，我推荐前面的Map方法
	 */
	@SuppressWarnings("rawtypes")
	private ArrayList[] recordEveTimeSoldSalerAndNum = null;
	
	private int orderNum = 0;
	private static Manager m = null;
}
