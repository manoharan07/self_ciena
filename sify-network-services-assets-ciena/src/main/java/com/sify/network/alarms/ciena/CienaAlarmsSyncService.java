package com.sify.network.alarms.ciena;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.PropertyConfigurator;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sify.network.assets.ciena.CienaClient;
import com.sify.network.assets.ciena.CienaProperties;
import com.sify.network.assets.ciena.DbConnection;
import com.sify.network.assets.ciena.ServiceNowRestClient;
import com.sify.network.assets.core.api.AssetsAbstractService;
import com.sify.network.assets.core.api.AssetsCoreRuntime;
import com.sify.network.assets.core.api.bean.DBDetails;
import com.sify.network.assets.core.api.bean.InventoryDeviceDetails;
import com.sify.network.assets.core.api.bean.JobType;
import com.sify.network.assets.core.api.bean.PerformanceInputBean;
import com.sify.network.assets.core.api.bean.PerformanceOutputBean;

public class CienaAlarmsSyncService extends AssetsAbstractService {

/*	static Timestamp polled_date = null;
	static Calendar startTime=null;
	static Calendar endTime = null;
*/	
	//static ArrayList<Notification> currentAlarmsList = new ArrayList<Notification>();

	private CienaClient cienaClient = null;
	// private CienaWsClient cienaWsClient = null;

	// private static final String COMMA_DELIMITER = ",";

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("alarmssync");

	private DbConnection dbObj = null;

	//private HashMap<String, String> deviceMapCiena = new HashMap<String, String>();



	public CienaAlarmsSyncService() {
		super();
		cienaClient = new CienaClient();
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
		
		ArrayList<String> ipList = new ArrayList<String>();
		
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
			String query = "SELECT node_ip FROM ciena.devices_list_manual where active_status='active'";
			// System.out.println(query);
			logger.info("query is " + query);

			stmt = connection.createStatement();
			result = stmt.executeQuery(query);
			
			while (result.next()) {
				PerformanceInputBean bean = new PerformanceInputBean();
				String nodeIp = result.getString("node_ip");
				bean.setDeviceip(nodeIp);
				ipBeanList.add(bean);
				ipList.add(nodeIp);
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
		
		logger.info("alarms sync : No of devices "+ipList.size() + ", Device List : " + ipList);
				
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
		
		logger.info(Thread.currentThread().getName() + " : " + input.getDeviceip());
		return null;
	}

	@Override
	public ArrayList<PerformanceOutputBean> retrieveMemoryUsage(PerformanceInputBean input, long startTimestamp,
			long endTimestamp) {
		return null;
	}

	@Override
	public ArrayList<PerformanceOutputBean> retrieveLinkUsage(PerformanceInputBean input, long startTimestamp,
			long endTimestamp) {
		return null;
	}

	@Override
	public Map<String, String> retrieveLiveCpuUsage(PerformanceInputBean pd, long startTime, long endTime) {
		return null;
	}

	// as of now skipped for performance
	@Override
	public Map<String, String> retrieveLiveMemoryUsage(PerformanceInputBean pd, long startTime, long endTime) {
		return null;
	}

	@Override
	public ArrayList<Map<String, String>> retrieveLiveCpuUsageList(PerformanceInputBean pd, long startTime,
			long endTime) {
		return null;
	}

	@Override
	public ArrayList<Map<String, String>> retrieveLiveLinkUsageList(PerformanceInputBean pd, long startTime,
			long endTime) {
		return null;
	}

	public void init() {
		logger.info("OpticalReportAlarmHealthCheckup initiated");

		CienaAlarmsSyncService service = new CienaAlarmsSyncService();
		service.setServiceName("CienaAlarmsSyncService");

		DBDetails dbInventory = new DBDetails(CienaProperties.get("db.mysql.ip"), CienaProperties.get("db.mysql.port"),
				CienaProperties.get("db.mysql.username"), CienaProperties.get("db.mysql.password"),
				CienaProperties.get("db.mysql.dbname"));

		service.getDbDetailsMap().put(JobType.INVENTORY, dbInventory);

		service.setInventoryFromDevice(true);
		service.setInventoryThreadTimeout(120);
		try {
			service.setLivePollThreadCound(Integer.parseInt(CienaProperties.get("servicenow.alarm.sync.thread.count")));
		} catch(Exception e) {
			service.setLivePollThreadCound(20);
		}

		logger.info("CienaAlarmsSyncService details added");

		// providing the cron string for the jobs
		service.setAggregateCron("");
		service.setPollCron("");
		service.setInventoryCron(CienaProperties.get("servicenow.alarm.sync.cron"));

		AssetsCoreRuntime runtime = new AssetsCoreRuntime();
		runtime.register(service);
		logger.info("CienaAlarmsSyncService registered with core");

	}

	public static void main(String[] args) {
		
		/*HuwaeiU2000WebServicesImpleService soapclient = new HuwaeiU2000WebServicesImpleService();
		HuwaeiU2000WebServices soapclientservices = soapclient.getHuwaeiU2000WebServicesImplePort();
		String json = soapclientservices.getAllEmsActiveAlarms("ALL");
		System.out.println("SOAP Response : "+json);*/
		
		PropertyConfigurator.configure("config" + File.separator + "log4j_alarmsync.properties");
		CienaAlarmsSyncService service = new CienaAlarmsSyncService();
		service.init();

		// service.generateReport();

	}

	@Override
	public Map<String, String> retrieveLiveLinkUsage(PerformanceInputBean pd, long startTime, long endTime) {
		return null;

	}

	@Override
	public ArrayList<InventoryDeviceDetails> retrieveInventoryFromDevice(PerformanceInputBean input) {
		ArrayList<InventoryDeviceDetails> list = retrieveCurrentAlarms(input.getDeviceip());
		//return list;
		return null;
	}
	
	public ArrayList<InventoryDeviceDetails> retrieveCurrentAlarms(String ip) {
		
		ArrayList<InventoryDeviceDetails> inventoryList = new ArrayList<InventoryDeviceDetails>();
		

		try {

	
				HttpsURLConnection loginConn = cienaClient.doLoginUrlEncoded("https://" + ip + ":8443/api/login");
				
				if (loginConn==null) {
					System.out.println("Unable to connect with "+ip+", continuing with next ip");
					//continue;
					return inventoryList;
				}

				Map<String, String> sessionMap = cienaClient.getSessionId(loginConn);
				
				
				retrieve6500CurrentAlarms(ip, "sample node name", sessionMap);
		
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		
		return inventoryList;
	}

	
/*	public HashMap<String, String> loadDeviceMapCiena() {

		HashMap<String, String> deviceMap = new HashMap<String, String>();

		Connection connection = null;
		ResultSet result = null;
		Statement stmt = null;

		try {
			connection = dbObj.getDBConnection();
			String query = "SELECT device_ip, node_name, shelf FROM ciena.devices";
			// logger.info(query);
			logger.info("query is " + query);

			stmt = connection.createStatement();
			result = stmt.executeQuery(query);

			while (result.next()) {

				String deviceip = result.getString("device_ip");
				String nodeName = result.getString("node_name").trim();
			    String shelf = result.getString("shelf");
			    
			   // deviceMapCiena.put(deviceip, nodeName+":"+shelf);
			}

		} catch (Exception e) {
			logger.error("Got error", e);
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

		}

		System.out.println(deviceMap);

		return deviceMap;

	}
*/
	
	public ArrayList<InventoryDeviceDetails> retrieve6500CurrentAlarms(String ip, String nodeName, Map<String,String> sessionMap) {
		
		ArrayList<InventoryDeviceDetails> performanceList = new ArrayList<InventoryDeviceDetails>();
		
		ArrayList<Notification> alarmsList = new ArrayList<Notification>();
		
		
		//currentAlarms.ip = ip;
		//currentAlarms.alarmList = alarmsList;
		
		
		HttpsURLConnection apiConnAlarms = cienaClient.getHttpsConnectionWithoutCertificate(
				"https://" + ip + ":8443/api/v1/datastore/alarms", "GET", sessionMap);
		
		JSONObject jsonObjectAlarms = (JSONObject) cienaClient.getJson(apiConnAlarms);
		
		logger.info("Current Alarms List of "+ip+" : "+jsonObjectAlarms);

		JSONArray jsonArrayAlarms =null;
		
		try {
		jsonArrayAlarms = jsonObjectAlarms.getJSONArray("alarm");
		} catch(org.json.JSONException e) {
			System.out.println(e);
			
			try {
				DeviceCurrentAlarms currentAlarms = new DeviceCurrentAlarms(ip,alarmsList);
				
				ObjectMapper mapper = new ObjectMapper();
				//mapper.disable(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS);
				
				//mapper.enable(SerializationConfig.Feature.WRITE_EMPTY_JSON_ARRAYS);
				
				//jsonStr = mapper.defaultPrettyPrintingWriter().writeValueAsString(this);
				String jsonStr = mapper.writeValueAsString(currentAlarms);
			//	System.out.println("alarm list to be sent for ip "+ip+", "+currentAlarms);
				logger.info("alarm json to be sent for ip "+ip+", "+jsonStr);
				//System.out.println("alarm json to be sent for ip "+ip+", "+jsonStr);
				ServiceNowRestClient.syncAlarms(ip,jsonStr);
				} catch(Exception ex) {
					logger.error("Unable to sync alarms of "+ip+", "+alarmsList);
					ex.printStackTrace();
					System.out.println("Unable to convert alarms to json for ip "+ip+","+alarmsList);
				}
			
			return performanceList;
		}
		
		logger.info("current alarms count for ip "+ip+" : "+jsonArrayAlarms.length());
		
		try {
		
		
		for(int i=0;i<jsonArrayAlarms.length();i++) {
			JSONObject jsonObj = jsonArrayAlarms.getJSONObject(i);
			
			System.out.println("6500 Alarm ########## "+jsonObj);
			logger.info(ip+ " : " + jsonObj.get("aid")+", condtype : "+jsonObj.get("condtype"));
			
			String instanceAid = jsonObj.getString("aid");
			String aidType = jsonObj.getString("aidtype");
			String severity = jsonObj.getString("ntfcncde");
			String conditionType = jsonObj.getString("condtype");
			String serviceAffecting = jsonObj.getString("srveff");
			String alarmDateStr = jsonObj.getInt("year")+"-"+jsonObj.getString("ocrdat")+" "+jsonObj.getString("ocrtm").replaceAll("-", ":"); 
			String alarm_date_time = alarmDateStr;
			String location = jsonObj.getString("locn");
			String direction = jsonObj.getString("dirn");
			String conditionDescription = jsonObj.getString("conddescr");
			String aidDetail = jsonObj.getString("aiddet");
			String dgnType = jsonObj.getString("dgn-type");
			String mode = jsonObj.getString("mode");
			String cardType = jsonObj.optString("card-type");
			String clfi = jsonObj.optString("clfi");
			
			Notification alarm = new Notification();
			
			
			alarm.setNtfcncde(severity);
			alarm.setCardType(cardType);
			alarm.setConddescr(conditionDescription);
			alarm.setCondtype(conditionType);
			alarm.setAid(instanceAid);
			alarm.setDeviceip(ip);
			alarm.setServiceAffected(serviceAffecting);
			//alarm.setYear(year);
			alarm.setDate(alarmDateStr);
			alarm.setClfi(clfi);
			alarm.setDirn(direction);
			alarm.setLocn(location);
			alarm.setDgnType(dgnType);
			alarm.setMode(mode);
			
			
			
			//currentAlarmsList.add(alarm);
			alarmsList.add(alarm);
			
			
		}
		
		//if (alarmsList.size() > 0) {
			try {
				
				logger.info("About to send alarms to "+ip+", "+alarmsList);
				DeviceCurrentAlarms currentAlarms = new DeviceCurrentAlarms(ip,alarmsList);
			
			ObjectMapper mapper = new ObjectMapper();
			//mapper.disable(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS);
			//mapper.enable(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES);
			
			//jsonStr = mapper.defaultPrettyPrintingWriter().writeValueAsString(this);
			//System.out.println("alarm list to be sent for ip "+ip+", "+currentAlarms);
			String jsonStr = mapper.writeValueAsString(currentAlarms);
			logger.info("alarms json to be sent for ip "+ip+", "+jsonStr);
			ServiceNowRestClient.syncAlarms(ip,jsonStr);
			} catch(Exception e) {
				logger.error("Unable to convert/sync alarms to json of "+ip+", "+alarmsList);
				e.printStackTrace();
				System.out.println("Unable to convert alarms to json of "+ip+", "+alarmsList);
			}
			
		//}
			
		} catch(Exception e) {
			logger.error("Unable to sync alarms with ip "+ip,e);
		}
		
		return performanceList;
		
		

	}



	public void initPolling() {

		logger.info("initPolling called");
		//currentAlarmsList.clear();
		
		//loadDeviceMapCiena();

	}

	public void finishPolling() {

		logger.info("finishPolling called");
		
		
		
		//System.out.println("Current Alarms Count : "+currentAlarmsList.size()+"\n"+currentAlarmsList);
		
		//computeDurationOfReport();
		//String dateStr = generateReport();
		
		//ServiceNowRestClient.postAlarmHealthReportToServiceNow(dateStr);
		
		//System.out.println("Report generation completed");
		System.out.println("finishPolling completed");
		logger.info("finishPolling completed");

	}
	
	
/*	private void computeDurationOfReport() {
		
		Date currentDate = new Date();
		polled_date = new Timestamp(currentDate.getTime());
		
		Calendar cal = Calendar.getInstance();
		
		Calendar startTime=null;
		Calendar endTime = null;
		
		if (CienaProperties.get("optical.alarm.health.checkup.report.duration").equals("LAST_DAY")) {

			// last 1 day upto yesterday midnight.

			startTime = (Calendar) cal.clone();
			startTime.add(Calendar.DAY_OF_MONTH, -1);
			startTime.set(Calendar.HOUR_OF_DAY, 0);
			startTime.set(Calendar.MINUTE, 0);
			startTime.set(Calendar.SECOND, 0);

			endTime = (Calendar) cal.clone();
			endTime.add(Calendar.DAY_OF_MONTH, -1);
			endTime.set(Calendar.HOUR_OF_DAY, 23);
			endTime.set(Calendar.MINUTE, 59);
			endTime.set(Calendar.SECOND, 59);

		} else 	if (CienaProperties.get("optical.alarm.health.checkup.report.duration").equals("LAST_WEEK")) {

			// last 7 days upto yesterday.

			startTime = (Calendar) cal.clone();
			startTime.add(Calendar.DAY_OF_MONTH, -8);
			startTime.set(Calendar.HOUR_OF_DAY, 0);
			startTime.set(Calendar.MINUTE, 0);
			startTime.set(Calendar.SECOND, 0);

			endTime = (Calendar) cal.clone();
			endTime.add(Calendar.DAY_OF_MONTH, -1);
			endTime.set(Calendar.HOUR_OF_DAY, 23);
			endTime.set(Calendar.MINUTE, 59);
			endTime.set(Calendar.SECOND, 59);

		} else if (CienaProperties.get("optical.alarm.health.checkup.report.duration").equals("LAST_MONTH")) {

			// last month

			startTime = (Calendar) cal.clone();
			startTime.add(Calendar.MONTH, -1);
			startTime.set(Calendar.DAY_OF_MONTH, 1);
			startTime.set(Calendar.HOUR_OF_DAY, 0);
			startTime.set(Calendar.MINUTE, 0);
			startTime.set(Calendar.SECOND, 0);

			endTime = (Calendar) cal.clone();
			endTime.add(Calendar.MONTH, -1);
			endTime.set(Calendar.DAY_OF_MONTH, endTime.getActualMaximum(Calendar.DAY_OF_MONTH));
			endTime.set(Calendar.HOUR_OF_DAY, 23);
			endTime.set(Calendar.MINUTE, 59);
			endTime.set(Calendar.SECOND, 59);

		} else {

			// current month upto yesterday

			startTime = (Calendar) cal.clone();
			//startTime.set(Calendar.MONTH, -1);
			startTime.set(Calendar.DAY_OF_MONTH, 1);
			startTime.set(Calendar.HOUR_OF_DAY, 0);
			startTime.set(Calendar.MINUTE, 0);
			startTime.set(Calendar.SECOND, 0);

			endTime = (Calendar) cal.clone(); endTime.getTime();
			
			//endTime.set(Calendar.DAY_OF_MONTH, endTime.getActualMaximum(Calendar.DAY_OF_MONTH));
			endTime.add(Calendar.DAY_OF_MONTH,-1);
			//endTime.set(Calendar.MONTH, startTime.get(Calendar.MONTH)); // logically not needed but the previous line sets month to preivous month.need to debug.
			endTime.set(Calendar.HOUR_OF_DAY, 23);
			endTime.set(Calendar.MINUTE, 59);
			endTime.set(Calendar.SECOND, 59);

		}

	}  */

	
	/*class DeviceCurrentAlarms {
		String ip;
		ArrayList<Notification> alarmList;
		@Override
		public String toString() {
			return "DeviceCurrentAlarms [ip=" + ip + ", alarmList=" + alarmList + "]";
		}
		
		
		
	}*/

}
