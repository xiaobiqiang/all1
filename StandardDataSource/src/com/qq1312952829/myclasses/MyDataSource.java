package com.qq1312952829.myclasses;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class MyDataSource implements DataSource {
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public MyDataSource() {
		initPool();
	}

	private void initPool() {
		try {
			for(int i=0; i<pool.MAX_NUM; i++) {
				Connection conn = DriverManager.getConnection(url, user, pwd);
				pool.addConnection(new ConnectionWrapper(this, conn));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Connection getConnection() throws SQLException {
		return pool.getConnection();
	}
	
	ConnectionPool getConnectioPool() {
		return this.pool;
	}
	
	private ConnectionPool pool = new ConnectionPool();
	private String url = "jdbc:mysql:/localhost:3306/mydb";
	private String user = "root";
	private String pwd = "root";
	
	class ConnectionPool {
		synchronized Connection getConnection() {
			try {
				while(pool.isEmpty())
					this.wait();
				
				return pool.remove(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		synchronized void addConnection(Connection conn) {
			pool.add(conn);
			this.notifyAll();
		}
		
		private List<Connection> pool = new ArrayList<Connection>();
		private final int MAX_NUM = 10;
	}
	
	@Override
	public Connection getConnection(String arg0, String arg1) throws SQLException {
		return null;
	}
	
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}
	
	@Override
	public int getLoginTimeout() throws SQLException {
		return 0;
	}
	
	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}
	
	@Override
	public void setLogWriter(PrintWriter arg0) throws SQLException {
	}
	
	@Override
	public void setLoginTimeout(int arg0) throws SQLException {
	}
	
	@Override
	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		return false;
	}
	
	@Override
	public <T> T unwrap(Class<T> arg0) throws SQLException {
		return null;
	}
}