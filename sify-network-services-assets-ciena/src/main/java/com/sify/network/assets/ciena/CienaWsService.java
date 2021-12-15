package com.sify.network.assets.ciena;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.PropertyConfigurator;

import com.sify.network.assets.core.api.AssetsAbstractService;
import com.sify.network.assets.core.api.AssetsCoreRuntime;
import com.sify.network.assets.core.api.bean.DBDetails;
import com.sify.network.assets.core.api.bean.InventoryDeviceDetails;
import com.sify.network.assets.core.api.bean.JobType;
import com.sify.network.assets.core.api.bean.PerformanceInputBean;
import com.sify.network.assets.core.api.bean.PerformanceOutputBean;

public class CienaWsService extends AssetsAbstractService {

	private CienaWsClient cienaHttp = null;
//	private static final String COMMA_DELIMITER = ",";

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("cienawsservice");

	private DbConnection dbObj = null;

	public CienaWsService() {
		super();
		
		cienaHttp = new CienaWsClient();
		dbObj = new DbConnection();
	}

	@Override
	public ArrayList<InventoryDeviceDetails> fetchDeviceDetails() {
		return null;
	}
	
	

	@Override
	public ArrayList<PerformanceInputBean> loadDeviceIpListCPU() {
		
		//List<String> ipList = getDeviceIpList();
		ArrayList<PerformanceInputBean> ipBeanList=new ArrayList<PerformanceInputBean>();
		
/*		for(String ip:ipList) {
			PerformanceInputBean bean = new PerformanceInputBean();
			bean.setDeviceip(ip);
			ipBeanList.add(bean);
		}
*/		
		
		Connection connection = null;
		ResultSet result = null;
		Statement stmt = null;

		try {
			connection = dbObj.getDBConnection();
			String query = "SELECT node_ip,rest_api_prefix FROM ciena.devices_list_manual_ws where active_status='active'";
			// System.out.println(query);
			logger.info("query is " + query);

			stmt = connection.createStatement();
			result = stmt.executeQuery(query);
			
			while (result.next()) {
				PerformanceInputBean bean = new PerformanceInputBean();
				String nodeIp = result.getString("node_ip");
				String restPrefix = result.getString("rest_api_prefix");
				bean.setDeviceip(nodeIp);
				bean.setDevice_type(restPrefix);
				ipBeanList.add(bean);
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

	@Override
	public ArrayList<PerformanceInputBean> loadDeviceIpListMemory() {
		return null;
	}

	@Override
	public ArrayList<PerformanceInputBean> loadInterfacesList() {
		return null;
	}

	@Override
	public ArrayList<PerformanceOutputBean> retrieveCpuUsage(PerformanceInputBean input, long startTimestamp, long endTimestamp) {
		return null;
	}

	@Override
	public ArrayList<PerformanceOutputBean> retrieveMemoryUsage(PerformanceInputBean input, long startTimestamp,
			long endTimestamp) {
				return null;}

	@Override
	public ArrayList<PerformanceOutputBean> retrieveLinkUsage(PerformanceInputBean input, long startTimestamp,
			long endTimestamp) {
				return null;}

	@Override
	public Map<String, String> retrieveLiveCpuUsage(PerformanceInputBean pd, long startTime, long endTime) {
		return null;}

	// as of now skipped for performance
	@Override
	public Map<String, String> retrieveLiveMemoryUsage(PerformanceInputBean pd, long startTime, long endTime) {
		return null;}

	@Override
	public ArrayList<Map<String, String>> retrieveLiveCpuUsageList(PerformanceInputBean pd, long startTime,
			long endTime) {
				return null;}

	@Override
	public ArrayList<Map<String, String>> retrieveLiveLinkUsageList(PerformanceInputBean pd, long startTime,
			long endTime) {
				return null;}

	public void init() {
		logger.info("ciena service initiated");

		CienaWsService service = new CienaWsService();
		service.setServiceName("ciena");
		// service.setLivePollThreadCound(versaProperties.get("thread.count", 10));
		// logger.info("versa total thread is " + versaProperties.get("thread.count",
		// 10));

		// inventory
		
		 DBDetails dbInventory = new DBDetails(CienaProperties.get("db.mysql.ip"),
		 CienaProperties.get("db.mysql.port"),
		 CienaProperties.get("db.mysql.username"),
		 CienaProperties.get("db.mysql.password"),
		 CienaProperties.get("db.mysql.dbname"));
		  
		 service.getDbDetailsMap().put(JobType.INVENTORY, dbInventory);
		 
		 service.setInventoryFromDevice(true);
		 service.setInventoryThreadTimeout(30);
		 
		
		 
		logger.info("ciena service inventory details added");

	
		// providing the cron string for the jobs
		service.setAggregateCron(CienaProperties.get("ciena.aggregate.cron"));
		service.setPollCron(CienaProperties.get("ciena.poll.cron"));
		service.setInventoryCron(CienaProperties.get("ciena.waveserver.inventory.cron"));
		// service.setRedisTTLvalue(versaProperties.get("versa.live.ttl",350));

		// service.setLiveThreadTimeout(versaProperties.get("versa.livethreads.completion.timeout.mins",10));

		AssetsCoreRuntime runtime = new AssetsCoreRuntime();
		runtime.register(service);
		logger.info("ciena ws service registered with core");

	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("config" + File.separator + "log4j_cienawsservice.properties");
		CienaWsService service = new CienaWsService();
		service.init();

	}

	@Override
	public Map<String, String> retrieveLiveLinkUsage(PerformanceInputBean pd, long startTime, long endTime) {
		return null;
		
	}

/*	public List<String> getDeviceIpList() {
		String[] values = null;
		List<String> ipAddresses = new ArrayList<String>();
		try {
			try (BufferedReader br = new BufferedReader(new FileReader("CienaDevicesIPList.csv"))) {
				String line;
				while ((line = br.readLine()) != null) {
					values = line.split(COMMA_DELIMITER);
					ipAddresses.add(values[5]);
				}
			}
			ipAddresses.remove(0);
			
			 * String deviceIps = "100.66.28.116,100.66.28.132"; String ipList[] =
			 * deviceIps.split(","); return Arrays.asList(ipList);
			 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("ipAddresses....." + ipAddresses);
		return ipAddresses;
	}*/
	
	
	/*
	 * public boolean isLiveStatusCompleted() { return deviceLiveStatusCompleted; }
	 */

	@Override
	public ArrayList<InventoryDeviceDetails> retrieveInventoryFromDevice(PerformanceInputBean input) {
		ArrayList<InventoryDeviceDetails> list = cienaHttp.retrieveInventoryAll(input.getDeviceip(),input.getDevice_type());
		return list;
	}
	
	
	public void initPolling() {
		System.out.println("Ciena Waveserver Inventory polling started");
		cienaHttp.hostNamesMap.clear();
	}
	
	public void finishPolling() {
		System.out.println("Ciena Waveserver Inventory polling completed");
		System.out.println(cienaHttp.circuitMap);
	}

}
