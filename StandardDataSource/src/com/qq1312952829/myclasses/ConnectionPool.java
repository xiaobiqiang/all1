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
		//不同步可能多个线程同时抢，抢占完之后，其他线程抢占会报错
		return pool.remove(0);
	}
	
	synchronized void addConnection(Connection conn) {
		pool.add(conn);
		this.notifyAll();
	}
	
	private List<Connection> pool = new ArrayList<Connection>();
	private final int MAX_NUM = 10;
}
