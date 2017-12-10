package com.qq1312952829.myclasses;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

class ConnectionPool {
	synchronized Connection getConnection() {
		try {
			while(pool.isEmpty())
				this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//��ͬ�����ܶ���߳�ͬʱ������ռ��֮�������߳���ռ�ᱨ��
		return pool.remove(0);
	}
	
	synchronized void addConnection(Connection conn) {
		pool.add(conn);
		this.notifyAll();
	}
	
	private List<Connection> pool = new ArrayList<Connection>();
	private final int MAX_NUM = 10;
}
