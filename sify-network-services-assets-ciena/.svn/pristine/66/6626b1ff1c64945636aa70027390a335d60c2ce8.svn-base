package com.sify.network.alarms.ciena;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.PropertyConfigurator;

import com.sify.network.assets.ciena.CienaProperties;
import com.sify.network.assets.ciena.DbConnection;


public class CienaEventReceiver {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("cienaalarms");

	private static DbConnection dbObj = new DbConnection();
	
	private static CienaProperties cienaProperties = new CienaProperties();

	public static void main(String[] args) {

		PropertyConfigurator.configure("config" + File.separator + "log4j_cienaalarms.properties");
		
		List<String> deviceIPs = null;
		try {
			//deviceIPs = Files.readAllLines(Paths.get("inputips.txt"));
			deviceIPs = loadIPListFromDB();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		Integer port =cienaProperties.get("ciena.alarm.api.port",8443);
		String username = CienaProperties.get("ciena.api.username");
		String password = CienaProperties.get("ciena.api.password");
		Integer retryAfterMs =cienaProperties.get("ciena.alarm.retry.ms",5000);
		
		Integer threadCount = cienaProperties.get("ciena.alarm.thread.count",100);

		ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadCount);

		// Submitting the threads
		for (String deviceIP : deviceIPs) {
			
			/*try {
				logger.info("Retrieving certificate for : " + deviceIP);
				InstallCert.retrieveCertificateAndStore(deviceIP, port);
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}*/
			
			logger.info("submitting for : " + deviceIP);
			executorService.submit(new EventReceiverThread(deviceIP, username, password, port,retryAfterMs));
		}
		
		if ( CienaProperties.get("optical.activemq.enabled").equals("true")) {
		
		//int numberOfConsumers = 1;
		ArrayList<OpticalAlarmConsumer> consumerList = new ArrayList<OpticalAlarmConsumer>();

	/*	for (int index = 0; index < numberOfConsumers; index++) {
			OpticalAlarmConsumer alertConsumer = new OpticalAlarmConsumer();
			consumerList.add(alertConsumer);
			alertConsumer.start();
		}*/
		
		for (String deviceIP : deviceIPs) {
			OpticalAlarmConsumer alertConsumer = new OpticalAlarmConsumer(deviceIP);
			consumerList.add(alertConsumer);
			alertConsumer.start();
		}
		
		}

		// Verify the completed threads
		boolean check = true;
		long startTime = System.currentTimeMillis();
		try {
			while (check) {
				long duration = System.currentTimeMillis() - startTime;
				if (executorService.getTaskCount() == executorService.getCompletedTaskCount()) {
					check = false;
					logger.info("timeout for ExecutorService exceeded..");
				}
				logger.info(
						"Number of Running Threads Status : " + executorService.getCompletedTaskCount() + "/" + executorService.getTaskCount());
				Thread.sleep(5000);
			}
		} catch (Exception e) {
			logger.error("Got exception in running alarm receiver",e);
		}

		logger.info("shutting down service");

		// Shutting down of the ThreadpoolExecutor
		executorService.shutdownNow();

	}
	
public static ArrayList<String> loadIPListFromDB() {
		
		logger.info("Loading device list from DB");
		ArrayList<String> ipBeanList=new ArrayList<String>();
		
		Connection connection = null;
		ResultSet result = null;
		Statement stmt = null;

		try {
			connection = dbObj.getDBConnection();
			String query = "SELECT node_ip FROM ciena.devices_list_manual where active_status='active'";
			// logger.info(query);
			logger.info("query is " + query);

			stmt = connection.createStatement();
			result = stmt.executeQuery(query);
			
			while (result.next()) {
			
				String nodeIp = result.getString("node_ip");
				logger.info("Adding "+nodeIp);
				
				ipBeanList.add(nodeIp);
			}

		} catch (Exception e) {
		} finally {
			if (result != null) {
				try {
					result.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			dbObj.closeConnection(connection);
		}
		
		
		return ipBeanList;
	}
	
	
}
