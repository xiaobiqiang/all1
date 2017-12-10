package com.myclasses.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.myclasses.sqlbase.JDBCUtil;

public class Main {

	public static void main(String[] args) throws SQLException {
		insertInPreparedStatement();
	}
	
	/**
	 * conn.setAutoCommit(false);   10284
	 * conn.setAutoCommit(false) + addBatch 9561
	 */
	public static void insertInPreparedStatement() throws SQLException {
		Connection conn = JDBCUtil.getConnection("MyTest");
		
		long start = System.currentTimeMillis();
		
		PreparedStatement ppst = conn.prepareStatement("insert into persons(pname, age) values(?, ?)");
		
		conn.setAutoCommit(false);
		
		for(int i=1; i<=100000; i++) {
			ppst.setString(1, "name-"+i);
			ppst.setInt(2, i%50+1);
//			ppst.executeUpdate();
			ppst.addBatch(); //相当于把这些指令先加入到LIst存储，然后一次发送给Mysql。
			
			if(i%10000 == 0) {
				ppst.executeBatch();
				ppst.clearBatch();
			}
		}
		ppst.executeBatch(); //清理最后一次批操作
		ppst.clearBatch();
		
		conn.commit();
		
		System.out.println(System.currentTimeMillis() - start);
		
		JDBCUtil.closePreparedStatement(ppst);
		JDBCUtil.closeConnection(conn);
	}

}
