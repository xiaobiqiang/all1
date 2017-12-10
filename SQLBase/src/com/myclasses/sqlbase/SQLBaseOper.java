package com.myclasses.sqlbase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @Time <2017/11/24> 
 * @describe this class is to private some SQL base operation such as
 *       create,delete,update,insert,select and drop table.
 *       it quoted the tool class of JDBCUtil.
 *       
 *       notice to close the connection after the programming so as to 
 *       refresh the resource.
 */
public class SQLBaseOper {
	private Connection conn = null;
	private String db_name = null;
	private String t_name = null;

	public void getConnection(String dbName) {
		this.db_name = dbName;
		this.conn = JDBCUtil.getConnection(dbName);
	}

	/**
	 * create table in a given database;
	 * 
	 * @return true represents successfully. false contrast,it can caused by
	 *         connection failed,params are null or contain null, size of the list
	 *         is 0 and describe is not correct.
	 * 
	 */
	public boolean createTable(String T_name, ArrayList<String> fieldsDescribe) throws SQLException {
		if (null == T_name || null == fieldsDescribe)
			return false;

		int size = fieldsDescribe.size();
		if (size == 0)
			return false;

		for (int i = 0; i < size; i++) {
			String ele = fieldsDescribe.get(i);
			if (null == ele)
				return false;
		}

		StringBuilder sqlLinker = new StringBuilder("create table " + T_name + "(");
		for (int i = 0; i < fieldsDescribe.size(); i++) {
			if (i == fieldsDescribe.size() - 1)
				sqlLinker.append(fieldsDescribe.get(i) + ");");
			else
				sqlLinker.append(fieldsDescribe.get(i) + ",");
		}

		if (!JDBCUtil.execute(conn, sqlLinker.toString()))
			return false;

		this.t_name = T_name;

		return true;
	}

	/**
	 * colLabel can be null,at this time it represent select all fields. but when
	 * values is null,it will return false. param colLabels grammar is like
	 * "id,name,age",and that of values is like "12,'Jerry',22".
	 */
	public boolean insertRecord(String colLabels, String values) throws SQLException {
		if (null == this.t_name || null == values)
			return false;

		String sql = "insert into " + this.t_name;

		if (null == colLabels)
			sql = sql + " values(" + values + ");";
		else
			sql = sql + "(" + colLabels + ")" + " values(" + values + ");";

		if (!JDBCUtil.execute(conn, sql))
			return false;

		return true;
	}
	
	/**
	 * the updateDescribe should be such as "id=5,and student_name = 'xxxx'",instead, 
	 * when referred to String,it should be included with '';
	 * another thing to notice is that condition can be null,which represent that there is no condition.
	 * updateDescribe can't instead.
	 */
	public boolean updateRecords(String updateDescribe, String conditions) throws SQLException {
		if (null == updateDescribe)
			return false;

		String sql = "update " + this.t_name + " set " + updateDescribe;

		if (null != conditions)
			sql = sql + " where " + conditions;

		if (!JDBCUtil.execute(conn, sql))
			return false;

		return true;
	}
	
	/**
	 * this method is to delete some records with some given conditions,
	 * when the condition is null,it represent that delete all records 
	 * contained in this table.
	 */
	public boolean deleteRecords(String conditions) throws SQLException {
		if (null == this.t_name)
			return false;

		String sql = "delete from " + this.t_name;

		if (null != conditions)
			sql = sql + " where " + conditions;

		if (!JDBCUtil.execute(conn, sql))
			return false;

		return true;
	}
	
	/**
	 *this method is designed to select some records or part of some records with some given conditions.
	 *conditions can be null,which represents that there is no condition,and all records are corresponding.
	 *but colLabels can't.
	 *colLabels is as "id,student_name",and conditions should be such as "id = 5 or students = 'xxxxx'";
	 */
	public ResultSet selectRecords(String colLabels, String conditions) throws SQLException {
		if (null == this.t_name || null == colLabels)
			return null;

		String sql = "select " + colLabels + " from " + this.t_name;

		if (null != conditions)
			sql = sql + " where " + conditions;
		
		ResultSet rs = null;
		if(null == (rs = JDBCUtil.executeQuery(conn, sql)))
			return null;

		return rs;
	}

	public boolean dropTable() throws SQLException {
		if (null == this.t_name)
			return false;

		String sql = "drop table " + this.t_name;

		if (!JDBCUtil.execute(conn, sql))
			return false;
		
		return true;
	}
	
	public void closeConnection() {
		JDBCUtil.closeConnection(conn);
	}
	
	/**
	 *when you want to change a table to operate,
	 *you can use this method to set the table you want,
	 *then all of your operate is to influence this table until
	 *you use this method again to change another table.  
	 */
	public void setTableName(String name) {
		this.t_name = name;
	}

	public String getTableName() {
		return this.db_name + ":" + this.t_name;
	}
}
