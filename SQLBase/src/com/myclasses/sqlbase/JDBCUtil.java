package com.myclasses.sqlbase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCUtil {
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection(String dbName) {
		String url = "jdbc:mysql://localhost:3306/" + dbName;
		String user = "root";
		String password = "root";
		
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			return conn;
		} catch (SQLException e) {
		}
		
		return null;
	}
	
	public static boolean execute(Connection conn, String sql) throws SQLException {
		if(null == conn || null == sql)
			return false;
		
		Statement st = conn.createStatement();
		if(null == st || !st.execute(sql))
			return false;
		
		JDBCUtil.closeStatement(st);
		return true;
	}
	
	public static ResultSet executeQuery(Connection conn, String sql) throws SQLException {
		if(null == conn || null == sql)
			return null;
		
		Statement st = conn.createStatement();
		if(null == st)
			return null;
		
		ResultSet rs = st.executeQuery(sql);
		
		JDBCUtil.closeStatement(st);
		return rs;
	}
	
	public static void closeConnection(Connection conn) {
		try {
			if(conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeStatement(Statement st) {
		try {
			if(st != null && !st.isClosed())
				st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void closePreparedStatement(PreparedStatement ppst) {
		try {
			if(ppst != null && !ppst.isClosed())
				ppst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeResultSet(ResultSet rs) {
		try {
			if(rs != null && !rs.isClosed())
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
