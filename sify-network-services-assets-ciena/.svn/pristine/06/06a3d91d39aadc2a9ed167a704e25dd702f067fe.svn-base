package com.sify.network.assets.ciena;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DbConnection {
	
	
	String url = null;
	String dbName = null;
	String userName = null;
	String passWord = null;
	
	String urlHuawei = null;
	String dbNameHuawei = null;
	String userNameHuawei = null;
	String passWordHuawei = null;
	
	
	String urlMCPE = null;
	String dbNameMCPE = null;
	String userNameMCPE = null;
	String passWordMCPE = null;
	
	
	//CienaProperties configProperties = null;
	
	public DbConnection(){
		//configProperties = new CienaProperties();
	}
	
	
	

	public Connection getDBConnection() {
		Connection dbcon = null;
		dbName = CienaProperties.get("db.mysql.dbname");
		userName = CienaProperties.get("db.mysql.username");
		passWord = CienaProperties.get("db.mysql.password");
		url = "jdbc:mysql://" + CienaProperties.get("db.mysql.ip") + ":" + CienaProperties.get("db.mysql.port") + "/";//jdbc:mysql://119.226.225.234:3306/


		if (dbcon == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				dbcon = DriverManager.getConnection(
						url + dbName
								+ "?connectTimeout=0&socketTimeout=0&autoReconnect=true&rewriteBatchedStatements=true",
						userName, passWord);


			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
		return dbcon;

	}
	
	public Connection getDBConnection(String dbName) {
		Connection dbcon = null;
		//dbName = CienaProperties.get("db.mysql.dbname");
		userName = CienaProperties.get("db.mysql.username");
		passWord = CienaProperties.get("db.mysql.password");
		url = "jdbc:mysql://" + CienaProperties.get("db.mysql.ip") + ":" + CienaProperties.get("db.mysql.port") + "/";//jdbc:mysql://119.226.225.234:3306/


		if (dbcon == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				dbcon = DriverManager.getConnection(
						url + dbName
								+ "?connectTimeout=0&socketTimeout=0&autoReconnect=true&rewriteBatchedStatements=true",
						userName, passWord);


			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
		return dbcon;

	}

	
	public Connection getDBConnectionHuawei() {
		Connection dbcon = null;
		dbNameHuawei = CienaProperties.get("db.mysql.dbname.huawei");
		userNameHuawei = CienaProperties.get("db.mysql.username.huawei");
		passWordHuawei = CienaProperties.get("db.mysql.password.huawei");
		urlHuawei = "jdbc:mysql://" + CienaProperties.get("db.mysql.ip.huawei") + ":" + CienaProperties.get("db.mysql.port.huawei") + "/";//jdbc:mysql://119.226.225.234:3306/


		if (dbcon == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				dbcon = DriverManager.getConnection(
						urlHuawei + dbNameHuawei
								+ "?connectTimeout=0&socketTimeout=0&autoReconnect=true&rewriteBatchedStatements=true",
						userNameHuawei, passWordHuawei);


			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
		return dbcon;

	}
	
	public Connection getDBConnectionMCPE() {
		Connection dbcon = null;
		dbNameMCPE = CienaProperties.get("db.mysql.dbname.mcpe");
		userNameMCPE = CienaProperties.get("db.mysql.username.mcpe");
		passWordMCPE = CienaProperties.get("db.mysql.password.mcpe");
		urlMCPE = "jdbc:mysql://" + CienaProperties.get("db.mysql.ip.mcpe") + ":" + CienaProperties.get("db.mysql.port.mcpe") + "/";//jdbc:mysql://119.226.225.234:3306/


		if (dbcon == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				dbcon = DriverManager.getConnection(
						urlMCPE + dbNameMCPE
								+ "?connectTimeout=0&socketTimeout=0&autoReconnect=true&rewriteBatchedStatements=true",
						userNameMCPE, passWordMCPE);


			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
		return dbcon;

	}


	
	public void closeConnection(Connection connection) {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

}
