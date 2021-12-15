package com.sify.network.assets.ciena;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sify.network.assets.core.api.AssetsAbstractService;
import com.sify.network.assets.core.api.AssetsCoreRuntime;
import com.sify.network.assets.core.api.bean.DBDetails;
import com.sify.network.assets.core.api.bean.InventoryDeviceDetails;
import com.sify.network.assets.core.api.bean.JobType;
import com.sify.network.assets.core.api.bean.PerformanceInputBean;
import com.sify.network.assets.core.api.bean.PerformanceOutputBean;

public class CienaReportServiceFbDaily extends AssetsAbstractService {
	
	
	private CienaProperties cienaProperties = new CienaProperties();
	
	static Timestamp polled_date=null;

	private CienaClient cienaClient = null;
	private CienaWsClient cienaWsClient = null;
	
	//private static final String COMMA_DELIMITER = ",";

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("fbdailyreportlog");

	//private DbConnection dbObj = null;
	
	String ipList6500[] = {"100.65.244.98","100.65.244.106",   //Chennai Facebook
			               "100.70.41.190","100.70.41.186"};   //Mumbai Facebook
	
	String ipListWS[] = {"100.65.244.99","100.65.244.100","100.65.244.107","100.65.244.108",   //Chennai Facebook
			             "100.70.141.210","100.70.141.211","100.70.141.194","100.70.141.195"}; //Mumbai Facebook
	
	static HashMap<String,String> nodeNameMap = new HashMap<String,String>();
	
	static ArrayList<String> ignorePortParameters = new ArrayList<String>();
	
	static ArrayList<String> wpsPorts = new ArrayList<String>();
	
	String opticalInstances[] = { "1-1-OpticalPower","1-2-OpticalPower","2-1-OpticalPower","2-2-OpticalPower" };
	String modemInstances[] = { "1-1-Modem", "1-2-Modem","2-1-Modem", "2-2-Modem" };
	String oduInstances[] = { "1-1.1-ODU", "1-2.1-ODU","2-1.1-ODU", "2-2.1-ODU" };
	
	

	public CienaReportServiceFbDaily() {
		super();
/*		cienaClient = new CienaClient();
		cienaWsClient = new CienaWsClient();
*/		
		nodeNameMap.put("100.65.244.98", "MAASSF-MCD-O-I657-01");
		nodeNameMap.put("100.65.244.99", "MAASSF-MCD-O-IWAI-01");
		nodeNameMap.put("100.65.244.100", "MAASSF-MCD-O-IWAI-02");
		
		nodeNameMap.put("100.65.244.106", "MAAFPS-MCD-O-I657-01");
		nodeNameMap.put("100.65.244.107", "MAAFPS-MCD-O-IWAI-01");
		nodeNameMap.put("100.65.244.108", "MAAFPS-MCD-O-IWAI-02");
		
		nodeNameMap.put("100.70.41.190", "TANGPF-MCD-O-I657-04");
		nodeNameMap.put("100.70.141.210", "TANGPF-MCD-O-IWSAi-05");
		nodeNameMap.put("100.70.141.211", "TANGPF-MCD-O-IWSAi-06");
		
		nodeNameMap.put("100.70.41.186", "TANRAL-MCD-O-I657-02");
		nodeNameMap.put("100.70.141.194", "TANRAL-MCD-O-IWSAI-03");
		nodeNameMap.put("100.70.141.195", "TANRAL-MCD-O-IWSAI-04");

		
		
		
		//ArrayList ignorePortParameters = new ArrayList();
		
		ignorePortParameters.add("MAASSF-MCD-O-I657-01:AMP-1-4-6:OPIN-OTS");
		ignorePortParameters.add("MAASSF-MCD-O-I657-01:AMP-1-4-8:OPOUT-OTS");	
		ignorePortParameters.add("MAASSF-MCD-O-I657-01:AMP-1-6-6:OPIN-OTS");
		ignorePortParameters.add("MAASSF-MCD-O-I657-01:AMP-1-6-8:OPOUT-OTS");
		
		ignorePortParameters.add("MAAFPS-MCD-O-I657-01:AMP-1-4-6:OPIN-OTS");
		ignorePortParameters.add("MAAFPS-MCD-O-I657-01:AMP-1-4-8:OPOUT-OTS");	
		ignorePortParameters.add("MAAFPS-MCD-O-I657-01:AMP-1-6-6:OPIN-OTS");
		ignorePortParameters.add("MAAFPS-MCD-O-I657-01:AMP-1-6-8:OPOUT-OTS");
		
		
		ignorePortParameters.add("TANGPF-MCD-O-I657-04:AMP-1-4-6:OPIN-OTS");
		ignorePortParameters.add("TANGPF-MCD-O-I657-04:AMP-1-4-8:OPOUT-OTS");	
		ignorePortParameters.add("TANGPF-MCD-O-I657-04:AMP-1-6-6:OPIN-OTS");
		ignorePortParameters.add("TANGPF-MCD-O-I657-04:AMP-1-6-8:OPOUT-OTS");
		
		ignorePortParameters.add("TANRAL-MCD-O-I657-02:AMP-1-4-6:OPIN-OTS");
		ignorePortParameters.add("TANRAL-MCD-O-I657-02:AMP-1-4-8:OPOUT-OTS");	
		ignorePortParameters.add("TANRAL-MCD-O-I657-02:AMP-1-6-6:OPIN-OTS");
		ignorePortParameters.add("TANRAL-MCD-O-I657-02:AMP-1-6-8:OPOUT-OTS");

		
		wpsPorts.add("MAASSF-MCD-O-I657-01:OPTMON-1-5-13");
		wpsPorts.add("MAASSF-MCD-O-I657-01:OPTMON-1-5-15");
		wpsPorts.add("MAAFPS-MCD-O-I657-01:OPTMON-1-5-13");
		wpsPorts.add("MAAFPS-MCD-O-I657-01:OPTMON-1-5-15");
		
		
		wpsPorts.add("TANGPF-MCD-O-I657-04:OPTMON-1-5-13");
		wpsPorts.add("TANGPF-MCD-O-I657-04:OPTMON-1-5-15");
		wpsPorts.add("TANRAL-MCD-O-I657-02:OPTMON-1-5-13");
		wpsPorts.add("TANRAL-MCD-O-I657-02:OPTMON-1-5-15");

		
		
	}

	@Override
	public ArrayList<InventoryDeviceDetails> fetchDeviceDetails() {
		return null;
	}

	@Override
	public ArrayList<PerformanceInputBean> loadDeviceIpListCPU() {
		
	//	List<String> ipList = getDeviceIpList();
		ArrayList<PerformanceInputBean> ipBeanList=new ArrayList<PerformanceInputBean>();
		
		for(String ip:ipList6500) {
			PerformanceInputBean bean = new PerformanceInputBean();
			bean.setDeviceip(ip);
			bean.setDevice_type("6500");
			ipBeanList.add(bean);
		}
		
		for(String ip:ipListWS) {
			PerformanceInputBean bean = new PerformanceInputBean();
			bean.setDeviceip(ip);
			bean.setDevice_type("WaveServer");
			ipBeanList.add(bean);
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
		logger.info("ciena fb report service initiated");

		CienaReportServiceFbDaily service = new CienaReportServiceFbDaily();
		service.setServiceName("CienaReportServiceFbDaily");
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
		 service.setLivePollThreadCound(cienaProperties.get("ciena.fbdailyreport.thread.count",20));
		 
		
		 
		logger.info("ciena report service  details added");

		// providing the cron string for the jobs
		service.setAggregateCron(CienaProperties.get("ciena.aggregate.cron"));
		service.setPollCron(CienaProperties.get("ciena.aggregate.cron"));
		service.setInventoryCron(CienaProperties.get("ciena.fbdailyreport.cron"));
		// service.setRedisTTLvalue(versaProperties.get("versa.live.ttl",350));

		// service.setLiveThreadTimeout(versaProperties.get("versa.livethreads.completion.timeout.mins",10));

		AssetsCoreRuntime runtime = new AssetsCoreRuntime();
		runtime.register(service);
		logger.info("ciena service registered with core");

	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("config" + File.separator + "log4j_fbdaily.properties");
		CienaReportServiceFbDaily service = new CienaReportServiceFbDaily();
		service.init();
		
//		String dateStr=service.generateReport();
		
//		ServiceNowRestClient.postFbReportToServiceNow(dateStr);
		
		//ServiceNowRestClient.getCiena6500Alarms("100.66.74.134", "2020-07-27 00:00:00", "2020-07-27 23:59:59");

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
		logger.info("ipAddresses....." + ipAddresses);
		return ipAddresses;
	}*/
	/*
	 * public boolean isLiveStatusCompleted() { return deviceLiveStatusCompleted; }
	 */

	@Override
	public ArrayList<InventoryDeviceDetails> retrieveInventoryFromDevice(PerformanceInputBean input) {
		
		Date currentDate = new Date();
		polled_date = new Timestamp(currentDate.getTime());
		
		ArrayList<InventoryDeviceDetails> list=null;
				
		if (input.getDevice_type().equals("6500")) {
			list = retrievePeformance6500(input.getDeviceip());
		} else if (input.getDevice_type().equals("WaveServer")) {
			list = retrievePeformanceWS(input.getDeviceip());
		}
		
/*		ArrayList<InventoryDeviceDetails> list = retrievePeformance6500(input.getDeviceip());
		
		for(int i=0;i<ipList6500.length;i++) {
			list.addAll(retrievePeformance6500(input.getDeviceip()));
		}
		
		for(int i=0;i<ipListWS.length;i++) {
			list.addAll(retrievePeformanceWS(input.getDeviceip()));
		}
*/		
		return list;
	}
	
	public ArrayList<InventoryDeviceDetails> retrievePeformance6500(String ip) {
		ArrayList<InventoryDeviceDetails> performanceList = new ArrayList<InventoryDeviceDetails>();
		
		performanceList.addAll(retrieve6500Parameters(ip));
		return performanceList;
	}
	
	public ArrayList<InventoryDeviceDetails> retrieve6500Parameters(String ip) {
		ArrayList<InventoryDeviceDetails> performanceList = new ArrayList<InventoryDeviceDetails>();
		
		String nodeName = nodeNameMap.get(ip);
		Map<String, String> sessionMap=new HashMap();
		

		try {
			
			
				HttpsURLConnection loginConn = cienaClient.doLoginUrlEncoded("https://" + ip + ":8443/api/login");
				
				
				if (loginConn==null) {
					logger.info("Unable to connect with "+ip+", continuing with next ip");
					//fw.write(ip + "," + aid + "," + tx_tag + "," + rx_actual);
					
					logger.info(ip + "," +  "," +  "," );
					
					return performanceList;
				}

				sessionMap = cienaClient.getSessionId(loginConn);

				HttpsURLConnection apiConn = cienaClient.getHttpsConnectionWithoutCertificate(
						"https://" + ip + ":8443/api/v1/datastore/amps/amp", "GET", sessionMap);

				JSONArray jsonArray = (JSONArray) cienaClient.getJson(apiConn);
				
				for(int i=0;i<jsonArray.length();i++) {
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					
					String portId = jsonObj.getString("amp");
					String parameter = "GAIN";
					Double value = jsonObj.getDouble("gain");
					
					logger.info(jsonObj.get("amp") +" : "+"Gain : "+jsonObj.get("gain"));
					
					InventoryDeviceDetails perfData=new InventoryDeviceDetails(ip) {
						String sql="INSERT INTO optical_reports.fb_6500_perf_current(node_name, node_ip, port_id, parameter_name, parameter_value, collection_date, updated_time) "
								+ "VALUES('"+nodeName+"', '"+ip+"', '"+portId+"', '"+parameter+"', '"+value+"', '"+polled_date+"', '"+polled_date+"') ON DUPLICATE KEY UPDATE parameter_value='"+value+"', updated_time='"+polled_date+"'";
						@Override
						public String getInsertQuery() {
							return sql;
						}
					};
					
					performanceList.add(perfData);
					
				/*	if(jsonObj.get("amp").equals("AMP-1-4-6")) {
			
						logger.info("#########"+jsonObj.get("amp") +" : "+"Gain : "+jsonObj.get("gain"));
						
					}
					
					if(jsonObj.get("amp").equals("AMP-1-4-8")) {
						logger.info("#########"+jsonObj.get("amp") +" : "+"Gain : "+jsonObj.get("gain"));
						
					}*/
				}
				
				HttpsURLConnection apiConnCounts = cienaClient.getHttpsConnectionWithoutCertificate(
						"https://" + ip + ":8443/api/v1/datastore/pm/amp", "GET", sessionMap);
				
				JSONObject jsonObjectCounts = (JSONObject) cienaClient.getJson(apiConnCounts);

				JSONArray jsonArrayCounts = jsonObjectCounts.getJSONArray("counts");
				
				for(int i=0;i<jsonArrayCounts.length();i++) {
					JSONObject jsonObj = jsonArrayCounts.getJSONObject(i);
					
					
					
					//if (((JSONArray)jsonObj.get("montype")).get(0).equals("OPOUT-OTS") && jsonObj.get("ampaid").equals("AMP-1-4-6"))
					if (((JSONArray)jsonObj.get("montype")).get(0).equals("OPOUT-OTS") )
					{
						logger.info(jsonObj.get("ampaid") +" : "+"Tx Power : " + jsonObj.get("monval"));
						
						String portId = jsonObj.getString("ampaid");
						String parameter = "OPOUT-OTS";
						Double value = jsonObj.getDouble("monval");
						
						String ignorePort = nodeName + ":" + portId + ":" + parameter;
						
						if (ignorePortParameters.contains(ignorePort)) {
							logger.info("###########Skipped "+ignorePort);
							continue;
						}
						
						
						InventoryDeviceDetails perfData=new InventoryDeviceDetails(ip) {
							String sql="INSERT INTO optical_reports.fb_6500_perf_current(node_name, node_ip, port_id, parameter_name, parameter_value, collection_date, updated_time) "
									+ "VALUES('"+nodeName+"', '"+ip+"', '"+portId+"', '"+parameter+"', '"+value+"', '"+polled_date+"', '"+polled_date+"') ON DUPLICATE KEY UPDATE parameter_value='"+value+"', updated_time='"+polled_date+"'";
							@Override
							public String getInsertQuery() {
								return sql;
							}
						};
						
						performanceList.add(perfData);
					}
					
					//if (((JSONArray)jsonObj.get("montype")).get(0).equals("OPIN-OTS") && jsonObj.get("ampaid").equals("AMP-1-4-8"))
					if (((JSONArray)jsonObj.get("montype")).get(0).equals("OPIN-OTS") )
					{
						logger.info(jsonObj.get("ampaid") +" : "+"Rx Power : " + jsonObj.get("monval"));
						
						String portId = jsonObj.getString("ampaid");
						String parameter = "OPIN-OTS";
						Double value = jsonObj.getDouble("monval");
						
						String ignorePort = nodeName + ":" + portId + ":" + parameter;
						
						if (ignorePortParameters.contains(ignorePort)) {
							logger.info("###########Skipped "+ignorePort);
							continue;
						}
						
						
						InventoryDeviceDetails perfData=new InventoryDeviceDetails(ip) {
							String sql="INSERT INTO optical_reports.fb_6500_perf_current(node_name, node_ip, port_id, parameter_name, parameter_value, collection_date, updated_time) "
									+ "VALUES('"+nodeName+"', '"+ip+"', '"+portId+"', '"+parameter+"', '"+value+"', '"+polled_date+"', '"+polled_date+"') ON DUPLICATE KEY UPDATE parameter_value='"+value+"', updated_time='"+polled_date+"'";
							@Override
							public String getInsertQuery() {
								return sql;
							}
						};
						
						performanceList.add(perfData);
					}
					
					
					if (((JSONArray)jsonObj.get("montype")).get(0).equals("ORL-OTS") )
					{
						logger.info(jsonObj.get("ampaid") +" : "+ jsonObj.get("montype") +" : "+ jsonObj.get("monval"));
						
						String portId = jsonObj.getString("ampaid");
						String parameter = "ORL-OTS";
						Double value = jsonObj.getDouble("monval");
						
						
						InventoryDeviceDetails perfData=new InventoryDeviceDetails(ip) {
							String sql="INSERT INTO optical_reports.fb_6500_perf_current(node_name, node_ip, port_id, parameter_name, parameter_value, collection_date, updated_time) "
									+ "VALUES('"+nodeName+"', '"+ip+"', '"+portId+"', '"+parameter+"', '"+value+"', '"+polled_date+"', '"+polled_date+"') ON DUPLICATE KEY UPDATE parameter_value='"+value+"', updated_time='"+polled_date+"'";
							@Override
							public String getInsertQuery() {
								return sql;
							}
						};
						
						performanceList.add(perfData);
						
					}
					
					
				}
				
				
				HttpsURLConnection apiConnOscs = cienaClient.getHttpsConnectionWithoutCertificate(
						"https://" + ip + ":8443/api/v1/datastore/oscs", "GET", sessionMap);
				
				JSONObject jsonObjectOscs = (JSONObject) cienaClient.getJson(apiConnOscs);

				JSONArray jsonArrayOscs = jsonObjectOscs.getJSONArray("osc");
				
				for(int i=0;i<jsonArrayOscs.length();i++) {
					JSONObject jsonObj = jsonArrayOscs.getJSONObject(i);
					
					logger.info(jsonObj.get("osc")+", spanloss : "+jsonObj.get("span-loss"));
					
					String portId = jsonObj.getString("osc");
					String parameter = "SPAN LOSS";
					Double value = jsonObj.getDouble("span-loss");
					
					
					InventoryDeviceDetails perfData=new InventoryDeviceDetails(ip) {
						String sql="INSERT INTO optical_reports.fb_6500_perf_current(node_name, node_ip, port_id, parameter_name, parameter_value, collection_date, updated_time) "
								+ "VALUES('"+nodeName+"', '"+ip+"', '"+portId+"', '"+parameter+"', '"+value+"', '"+polled_date+"', '"+polled_date+"') ON DUPLICATE KEY UPDATE parameter_value='"+value+"', updated_time='"+polled_date+"'";
						@Override
						public String getInsertQuery() {
							return sql;
						}
					};
					
					performanceList.add(perfData);
					
				}
				
				HttpsURLConnection apiConnOptMon = cienaClient.getHttpsConnectionWithoutCertificate(
						"https://" + ip + ":8443/api/v1/datastore/optmons", "GET", sessionMap);
				
				JSONObject jsonObjectOptMon = (JSONObject) cienaClient.getJson(apiConnOptMon);

				JSONArray jsonArrayOptmon = jsonObjectOptMon.getJSONArray("optmon");
								
				
				JSONArray jsonArrayProtectionGroup = jsonObjectOptMon.getJSONArray("prot-group");
				
				for(int i=0;i<jsonArrayOptmon.length();i++) {
					JSONObject jsonObj = jsonArrayOptmon.getJSONObject(i);
					
					String facilityId = jsonObj.getString("optmon");
					
					String wpsPort = nodeName + ":" + facilityId;
					
					if (!wpsPorts.contains(wpsPort)) {
						continue;
					}
					
					String facilityState = (jsonObj.getJSONArray("sst")).toString();
					
					JSONObject grpObj=null;
					
					String memberType =null;
				//	String wps_scheme=null;
				//	String remoteStandard=null;
					logger.info("######## checking "+facilityId+ " member type");
					for(int j=0;j<jsonArrayProtectionGroup.length();j++) {
						grpObj = jsonArrayProtectionGroup.getJSONObject(j);
						if (grpObj.getString("wrkgaid").equals(facilityId)) {
							memberType = "Working";
							
							logger.info("######## "+facilityId+ " is "+memberType);
							//wps_scheme = grpObj.getString("prot-scheme");
							//remoteStandard = grpObj.getString("rem-standard");
							break;
						} else if (grpObj.getString("protaid").equals(facilityId)) {
							memberType = "Protection";
							logger.info("######## "+facilityId+ " is "+memberType);
							//wps_scheme = grpObj.getString("prot-scheme");
							//remoteStandard = grpObj.getString("rem-standard");
							break;
						} 
					}
					
					if (grpObj !=null) {
					String wpsMemberType = memberType;
					String scheme = grpObj.getString("prot-scheme");
					String remoteStandard =  grpObj.getString("rem-standard");
					
					InventoryDeviceDetails perfData=new InventoryDeviceDetails(ip) {
						String sql="INSERT INTO optical_reports.fb_6500_wps_status	(node_name, node_ip, facility_id, scheme, memeber_type, remote_standard, "
								+ "facility_state, switch, reason, collection_date, updated_time)	"
								+ "VALUES('"+nodeName+"', '"+ip+"', '"+facilityId+"', '"+scheme+"', '"+wpsMemberType+"', '"+remoteStandard+"', '"
								+facilityState+"', '', '', '"+polled_date+"', '"+polled_date+"') ON DUPLICATE KEY UPDATE facility_state='"+facilityState+"', updated_time='"+polled_date+"'";
						@Override
						public String getInsertQuery() {
							logger.info("$$$$$$$$$$$$$$$ "+sql);
							return sql;
						}
					};
					
					performanceList.add(perfData);

					}
						
				/*	if (jsonObj.get("optmon").equals("OPTMON-1-5-13")) {
					
					logger.info("OPTMON-1-5-13 working status : "+((JSONArray)jsonObj.get("sst")).get(0));
					}
					
					if (jsonObj.get("optmon").equals("OPTMON-1-5-15")) {
						
						logger.info("OPTMON-1-5-15 working status : "+((JSONArray)jsonObj.get("sst")).get(0));
					}*/
						
					
				}
				
				
							
		

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		
		performanceList.addAll(retrieve6500CurrentAlarms(ip,nodeName,sessionMap));
		performanceList.addAll(retrieve6500pm(ip,nodeName,sessionMap));
		
		logger.info(performanceList);
		
		return performanceList;
	}
	
	public ArrayList<InventoryDeviceDetails> retrieve6500CurrentAlarms(String ip, String nodeName, Map<String,String> sessionMap) {
		
		ArrayList<InventoryDeviceDetails> performanceList = new ArrayList<InventoryDeviceDetails>();
		
		HttpsURLConnection apiConnAlarms = cienaClient.getHttpsConnectionWithoutCertificate(
				"https://" + ip + ":8443/api/v1/datastore/alarms", "GET", sessionMap);
		
		JSONObject jsonObjectAlarms = (JSONObject) cienaClient.getJson(apiConnAlarms);

		JSONArray jsonArrayAlarms =null;
		
		try {
		jsonArrayAlarms = jsonObjectAlarms.getJSONArray("alarm");
		} catch(org.json.JSONException e) {
			System.out.println(e);
			return performanceList;
		}
		
		
		
		for(int i=0;i<jsonArrayAlarms.length();i++) {
			JSONObject jsonObj = jsonArrayAlarms.getJSONObject(i);
			
			System.out.println("6500 Alarm ########## "+jsonObj.get("aid")+", condtype : "+jsonObj.get("condtype"));
			logger.info(jsonObj.get("aid")+", condtype : "+jsonObj.get("condtype"));
			
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
								
			
			
			InventoryDeviceDetails perfData=new InventoryDeviceDetails(ip) {
				String sql="INSERT INTO optical_reports.fb_6500_alarms_current(node_name, node_ip, instance_aid, aid_type, severity, condition_type, service_affecting, "
						+ "alarm_date_time, location, direction, condition_description, aid_detail, dgn_type, mode, collection_date, updated_time) "
						+ "VALUES('"+nodeName+"', '"+ip+"', '"+instanceAid+"', '"+aidType+"', '"+severity+"', '"+conditionType+"', '"+serviceAffecting+"', '"
						+alarm_date_time+"', '"+location+"', '"+direction+"', '"+conditionDescription+"', '"+aidDetail+"', '"+dgnType+"', '"+mode+"', '"+polled_date+"', '"+polled_date+"') ON DUPLICATE KEY UPDATE instance_aid='"+instanceAid+"', updated_time='"+polled_date+"'";
				@Override
				public String getInsertQuery() {
					return sql;
				}
			};
			
			performanceList.add(perfData);
			
		}
		
		return performanceList;

	}
	
	
	public ArrayList<InventoryDeviceDetails> retrieve6500pm(String ip, String nodeName, Map<String,String> sessionMap) {

		ArrayList<InventoryDeviceDetails> performanceList = new ArrayList<InventoryDeviceDetails>();

		HttpsURLConnection apiConnCounts = cienaClient.getHttpsConnectionWithoutCertificate(
				"https://" + ip + ":8443/api/v1/datastore/pm/amp", "GET", sessionMap);

		JSONObject jsonObjectCounts = (JSONObject) cienaClient.getJson(apiConnCounts);

		JSONArray jsonArrayCounts = jsonObjectCounts.getJSONArray("counts");

		for (int i = 0; i < jsonArrayCounts.length(); i++) {
			JSONObject jsonObj = jsonArrayCounts.getJSONObject(i);
			
			

				String portId = jsonObj.getString("ampaid");
				String parameter = ((JSONArray)jsonObj.get("montype")).getString(0); 
				String value = jsonObj.getString("monval");
				
				System.out.println("@@@@@@@@@@@@ "+portId+","+parameter+","+value);
				
				String location = jsonObj.getString("locn");
				String direction = jsonObj.getString("dirn");

				InventoryDeviceDetails perfData = new InventoryDeviceDetails(ip) {
					String sql = "INSERT INTO optical_reports.fb_6500_pm(node_name, node_ip, port_id, parameter_name, parameter_value, location, direction, collection_date, updated_time) "
							+ "VALUES('" + nodeName + "', '" + ip + "', '" + portId + "', '" + parameter + "', '"
							+ value + "', '" + location + "','" + direction + "','" + polled_date + "', '" + polled_date
							+ "') ON DUPLICATE KEY UPDATE parameter_value='" + value + "', updated_time='" + polled_date
							+ "'";

					@Override
					public String getInsertQuery() {
						return sql;
					}
				};

				performanceList.add(perfData);
			
		}
		
		apiConnCounts = cienaClient.getHttpsConnectionWithoutCertificate(
				"https://" + ip + ":8443/api/v1/datastore/pm/optmon", "GET", sessionMap);

		jsonObjectCounts = (JSONObject) cienaClient.getJson(apiConnCounts);

		jsonArrayCounts = jsonObjectCounts.getJSONArray("counts");

		for (int i = 0; i < jsonArrayCounts.length(); i++) {
			JSONObject jsonObj = jsonArrayCounts.getJSONObject(i);

				String portId = jsonObj.getString("optmonaid");
				String parameter = ((JSONArray)jsonObj.get("montype")).getString(0);;
				String value = jsonObj.getString("monval");
				
				String location = jsonObj.getString("locn");
				String direction = jsonObj.getString("dirn");

				InventoryDeviceDetails perfData = new InventoryDeviceDetails(ip) {
					String sql = "INSERT INTO optical_reports.fb_6500_pm(node_name, node_ip, port_id, parameter_name, parameter_value, location, direction, collection_date, updated_time) "
							+ "VALUES('" + nodeName + "', '" + ip + "', '" + portId + "', '" + parameter + "', '"
							+ value + "', '" + location + "','" + direction + "','" + polled_date + "', '" + polled_date
							+ "') ON DUPLICATE KEY UPDATE parameter_value='" + value + "', updated_time='" + polled_date
							+ "'";

					@Override
					public String getInsertQuery() {
						return sql;
					}
				};

				performanceList.add(perfData);
			
		}
		
		
		return performanceList;

	}

	
	public ArrayList<InventoryDeviceDetails> retrievePeformanceWS(String ip) {
		ArrayList<InventoryDeviceDetails> performanceList = new ArrayList<InventoryDeviceDetails>();
		performanceList.addAll(retrieveWsPerformanceCurrent(ip));
		performanceList.addAll(retrievePerformanceHistoryOptical(ip));
		performanceList.addAll(retrievePerformanceHistoryModem(ip));
		performanceList.addAll(retrievePerformanceHistoryOdu(ip));
		performanceList.addAll(retrieveActiveAlarmsWS(ip));
		return performanceList;
	}
	
	public ArrayList<InventoryDeviceDetails> retrieveWsPerformanceCurrent(String ip) {
		
		ArrayList<InventoryDeviceDetails> performanceList = new ArrayList<InventoryDeviceDetails>();
		
		String nodeName = nodeNameMap.get(ip);

		try {

				HttpsURLConnection apiConn = cienaWsClient.getHttpsConnectionWithoutCertificate(
						"https://" + ip + "/restconf/data/waveserver-ptps", "GET", null);

				JSONObject jsonObject = (JSONObject) cienaWsClient.getJson(apiConn);
				
				JSONArray jsonArray =(JSONArray) ((JSONObject)jsonObject.get("ciena-waveserver-ptp:waveserver-ptps")).get("ptps");
				
				
				
				for(int i=0;i<jsonArray.length();i++) {
					
				//	String txPort="";
					String txParam="";
					String txValue= "";
				//	String rxPort="";
					String rxParam = "";
					String rxValue="";
					
					String txValueStr="";
					String rxValueStr="";
					
					String txParamStr="";
					String rxParamStr="";
					
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					
					logger.info(jsonObj.get("ptp-id") );
					
					String txPort = jsonObj.getString("ptp-id");
					String rxPort = jsonObj.getString("ptp-id");
					
					JSONObject lanesObj = jsonObj.getJSONObject("properties").getJSONObject("lanes");
					
					if (lanesObj.getInt("number-of-lanes")==1) {
					
					JSONObject laneObj = jsonObj.getJSONObject("properties").getJSONObject("lanes").getJSONArray("lane").getJSONObject(0);
					
					txParam = "Tx Power ";
					txValue = String.valueOf(laneObj.getJSONObject("tx").getJSONObject("power").getDouble("actual"));
					rxParam = "Rx Power ";
					rxValue = String.valueOf(laneObj.getJSONObject("rx").getJSONObject("power").getDouble("actual"));
					
					logger.info("Rx Power "+laneObj.getJSONObject("rx").getJSONObject("power").get("actual"));
					logger.info("Tx Power "+laneObj.getJSONObject("tx").getJSONObject("power").get("actual"));
					
					} else {
						
						JSONArray lanesArray = jsonObj.getJSONObject("properties").getJSONObject("lanes").getJSONArray("lane");
						
					
						
						for (int laneIndex=0;laneIndex<lanesArray.length();laneIndex++) {
							JSONObject laneObj = lanesArray.getJSONObject(laneIndex);
							txParamStr += "L"+ (laneIndex+1) + "/";
							txValueStr += laneObj.getJSONObject("tx").getJSONObject("power").get("actual") + "/";
							
							rxParamStr += "L"+ (laneIndex+1) + "/";
							rxValueStr += laneObj.getJSONObject("rx").getJSONObject("power").get("actual") + "/";
							
							//logger.info("Lane "+laneIndex+", Rx Power "+laneObj.getJSONObject("rx").getJSONObject("power").get("actual"));
							//logger.info("Lane "+laneIndex+", Tx Power "+laneObj.getJSONObject("tx").getJSONObject("power").get("actual"));
						}
						
						
						txParam = "Tx Power "+txPort+" "+txParamStr.substring(0,txParamStr.length()-1);
						txValue = txValueStr.substring(0,txValueStr.length()-1);
						rxParam = "Rx Power "+rxPort+" "+rxParamStr.substring(0,rxParamStr.length()-1);
						rxValue = rxValueStr.substring(0,rxValueStr.length()-1);
						
					/*	logger.info("Tx Power "+jsonObj.get("ptp-id")+" "+txPort.substring(0,txPort.length()-1)+" : "+txValue.substring(0,txValue.length()-1));
						logger.info("Rx Power "+jsonObj.get("ptp-id")+" "+rxPort.substring(0,rxPort.length()-1)+" : "+rxValue.substring(0,rxValue.length()-1));
					*/	
						logger.info("Tx Power "+jsonObj.get("ptp-id")+" "+txParam+" : "+txValue);
						logger.info("Rx Power "+jsonObj.get("ptp-id")+" "+rxParam+" : "+rxValue);
					
						
					}
					
					String portIdTx = txPort;
					String parameterTx = txParam;
					String valueTx = txValue;
										
					InventoryDeviceDetails perfDataTx=new InventoryDeviceDetails(ip) {
						String sql="INSERT INTO optical_reports.fb_ws_perf_current(node_name, node_ip, port_id, parameter_name, parameter_value, collection_date, updated_time) "
								+ "VALUES('"+nodeName+"', '"+ip+"', '"+portIdTx+"', '"+parameterTx+"', '"+valueTx+"', '"+polled_date+"', '"+polled_date+"') ON DUPLICATE KEY UPDATE parameter_value='"+valueTx+"', updated_time='"+polled_date+"'";
						@Override
						public String getInsertQuery() {
							return sql;
						}
					};
					
					performanceList.add(perfDataTx);
					
					String portIdRx = rxPort;
					String parameterRx = rxParam;
					String valueRx = rxValue;
										
					InventoryDeviceDetails perfDataRx=new InventoryDeviceDetails(ip) {
						String sql="INSERT INTO optical_reports.fb_ws_perf_current(node_name, node_ip, port_id, parameter_name, parameter_value, collection_date, updated_time) "
								+ "VALUES('"+nodeName+"', '"+ip+"', '"+portIdRx+"', '"+parameterRx+"', '"+valueRx+"', '"+polled_date+"', '"+polled_date+"') ON DUPLICATE KEY UPDATE parameter_value='"+valueRx+"', updated_time='"+polled_date+"'";
						@Override
						public String getInsertQuery() {
							return sql;
						}
					};
					
					performanceList.add(perfDataRx);
							
				}
				
				HttpsURLConnection fecConn = cienaWsClient.getHttpsConnectionWithoutCertificate(
						"https://" + ip + "/restconf/data/waveserver-pm/modem-performance-instances", "GET", null);

				JSONObject instancesJsonObject = (JSONObject) cienaWsClient.getJson(fecConn);
				
				JSONArray fecJsonArray = instancesJsonObject.getJSONArray("ciena-waveserver-pm:modem-performance-instances");
				
				for(int fecIndex=0;fecIndex<fecJsonArray.length();fecIndex++) {
					JSONObject fecJsonObject = fecJsonArray.getJSONObject(fecIndex);
					JSONObject attachedInterfaceObj = fecJsonObject.getJSONObject("attached-interface");
					logger.info("attached interface "+attachedInterfaceObj.get("name"));
					
					JSONObject currentBinJsonObj = fecJsonObject.getJSONObject("current-bin").getJSONObject("statistics");
					logger.info("pre fec ber "+currentBinJsonObj.getJSONObject("pre-fec-bit-error-rate").getJSONObject("bit-error-rate").getDouble("value"));
					logger.info("q-factor "+currentBinJsonObj.getJSONObject("q-factor").getJSONObject("q-factor").getDouble("value"));
					
					String portId = attachedInterfaceObj.getString("name");
					String parameterQ = "Q Factor";
					String valueQ = String.valueOf(currentBinJsonObj.getJSONObject("q-factor").getJSONObject("q-factor").getDouble("value"));
										
					InventoryDeviceDetails perfDataQ=new InventoryDeviceDetails(ip) {
						String sql="INSERT INTO optical_reports.fb_ws_perf_current(node_name, node_ip, port_id, parameter_name, parameter_value, collection_date, updated_time) "
								+ "VALUES('"+nodeName+"', '"+ip+"', '"+portId+"', '"+parameterQ+"', '"+valueQ+"', '"+polled_date+"', '"+polled_date+"') ON DUPLICATE KEY UPDATE parameter_value='"+valueQ+"', updated_time='"+polled_date+"'";
						@Override
						public String getInsertQuery() {
							return sql;
						}
					};
					
					performanceList.add(perfDataQ);
					
					String parameterPFB = "PRE-FEC-BER";
					String valuePFB = String.valueOf(currentBinJsonObj.getJSONObject("pre-fec-bit-error-rate").getJSONObject("bit-error-rate").getDouble("value"));
										
					InventoryDeviceDetails perfDataPFB=new InventoryDeviceDetails(ip) {
						String sql="INSERT INTO optical_reports.fb_ws_perf_current(node_name, node_ip, port_id, parameter_name, parameter_value, collection_date, updated_time) "
								+ "VALUES('"+nodeName+"', '"+ip+"', '"+portId+"', '"+parameterPFB+"', '"+valuePFB+"', '"+polled_date+"', '"+polled_date+"') ON DUPLICATE KEY UPDATE parameter_value='"+valuePFB+"', updated_time='"+polled_date+"'";
						@Override
						public String getInsertQuery() {
							return sql;
						}
					};
					
					performanceList.add(perfDataPFB);
					
					
				}

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		
		return performanceList;
	}
	
	
	public ArrayList<InventoryDeviceDetails> retrievePerformanceHistoryOptical(String ip) {

		ArrayList<InventoryDeviceDetails> performanceList = new ArrayList<InventoryDeviceDetails>();
		
		String nodeName = nodeNameMap.get(ip);
		
		for (String instanceName:opticalInstances) {

		try {

			
				HttpsURLConnection apiConn = cienaWsClient.getHttpsConnectionWithoutCertificate(
						"https://" + ip + "/restconf/data/waveserver-pm/optical-power-instances="+instanceName, "GET", null);

				JSONObject jsonObject = (JSONObject) cienaWsClient.getJson(apiConn);
				
				JSONObject instanceJsonObject = jsonObject.getJSONArray("ciena-waveserver-pm:optical-power-instances").getJSONObject(0);
				
				String profileType = instanceJsonObject.getJSONObject("id").getString("profile-type");
				
				String adminState = instanceJsonObject.getJSONObject("state").getString("admin-state");
				String operState = instanceJsonObject.getJSONObject("state").getString("operational-state");
				String collectionStartTime = instanceJsonObject.getJSONObject("state").getString("collection-start-date-time");
				String collectionEndTime = instanceJsonObject.getJSONObject("state").getString("collection-end-date-time");
				
				String configuredMode = instanceJsonObject.getJSONObject("properties").getString("configuration-mode");
				String configuredBinCount = instanceJsonObject.getJSONObject("properties").getInt("configured-bin-count") + " " + instanceJsonObject.getJSONObject("properties").getInt("configured-bin-duration");
				
				String port = instanceJsonObject.getJSONObject("attached-interface").getString("name");
				String portType = instanceJsonObject.getJSONObject("attached-interface").getString("type");
				
				//configured_mode, interface_name, interface_type, profile_type, "
					//	+ "collection_start_time, collection_end_time, configured_bin_count, admin_state, oper_state, "
					
				
				JSONObject currentBin =  instanceJsonObject.getJSONObject("current-bin");
				JSONObject current24hrBin =  instanceJsonObject.getJSONObject("current-24-hour-bin");
				JSONObject untimedBin =  instanceJsonObject.getJSONObject("untimed-bin");
				JSONObject history24hrBin = instanceJsonObject.getJSONObject("history-24-hour-bin");
				
				JSONArray allBins = instanceJsonObject.getJSONObject("history").getJSONArray("bins");
				
				allBins.put(currentBin);
				allBins.put(current24hrBin);
				allBins.put(untimedBin);
				//allBins.put(historyBins);
				allBins.put(history24hrBin);
				
				
				for(int i=0;i<allBins.length();i++) {
					JSONObject binObj = allBins.getJSONObject(i);
					
					String id = binObj.getJSONObject("id").getString("bin-name");
					String binName = id;
					
					if (id.equals("history")) {
						;
						binName = "History Bin #" + String.format("%02d", binObj.getInt("bin-number"));
					}
					
					logger.info(port + ", "+binName);
					
					//+ "bin_collection_start_time, bin_collection_end_time
					
					logger.info(port + ", Start Time : "+binObj.getJSONObject("state").getString("start-date-time"));
					
					String binStartTime = binObj.getJSONObject("state").getString("start-date-time");
					String binEndTime  = "";
					
					if (binObj.getJSONObject("state").has("cleared-date-time")) {
						logger.info(port + ", Cleared Time : "+binObj.getJSONObject("state").getString("cleared-date-time"));
					} else {
						logger.info(port + ", End time : "+binObj.getJSONObject("state").getString("end-date-time"));
						binEndTime = binObj.getJSONObject("state").getString("end-date-time");
					}

					
					
					
					logger.info(port + ",  : "+binName);
					
					//System.out.println(port + ",  : "+binName);
					
					JSONArray laneArray = binObj.getJSONObject("statistics").optJSONArray("optical-power");
					
					if (laneArray == null ) laneArray = new JSONArray();
					
					HashMap<String,String> valuesMap = new HashMap<String,String>();
					HashMap<String,Boolean> idfMap = new HashMap<String,Boolean>();
					
					Integer laneNumber=null;;
					
					for(int laneIndex=0;laneIndex<laneArray.length();laneIndex++) {
						JSONObject laneObject = laneArray.getJSONObject(laneIndex);
						laneNumber = laneObject.getInt("lane-number");
						logger.info(port + ", Lane : "+laneNumber);
						
						String rxMin = laneObject.getJSONObject("rx-power").getJSONObject("minimum").optString("value");
						String rxMax = laneObject.getJSONObject("rx-power").getJSONObject("maximum").optString("value");
						String rxAvg = laneObject.getJSONObject("rx-power").getJSONObject("average").optString("value");
						
						boolean rxMinIDF = laneObject.getJSONObject("rx-power").getJSONObject("minimum").getBoolean("invalid-data-flag");
						boolean rxMaxIDF = laneObject.getJSONObject("rx-power").getJSONObject("maximum").getBoolean("invalid-data-flag");
						boolean rxAvgIDF = laneObject.getJSONObject("rx-power").getJSONObject("average").getBoolean("invalid-data-flag");

						
						logger.info("Rx Power Minimum :"+rxMin);
						logger.info("Rx Power Maximum :"+rxMax);
						logger.info("Rx Power Average :"+rxAvg);

						valuesMap.put("Rx Power Minimum",rxMin);
						valuesMap.put("Rx Power Maximum",rxMax);
						valuesMap.put("Rx Power Average",rxAvg);
						
						idfMap.put("Rx Power Minimum",rxMinIDF);
						idfMap.put("Rx Power Maximum",rxMaxIDF);
						idfMap.put("Rx Power Average",rxAvgIDF);

					
						String txMin = laneObject.getJSONObject("tx-power").getJSONObject("minimum").optString("value");
						String txMax = laneObject.getJSONObject("tx-power").getJSONObject("maximum").optString("value");
						String txAvg = laneObject.getJSONObject("tx-power").getJSONObject("average").optString("value");
						
						boolean txMinIDF = laneObject.getJSONObject("tx-power").getJSONObject("minimum").getBoolean("invalid-data-flag");
						boolean txMaxIDF = laneObject.getJSONObject("tx-power").getJSONObject("maximum").getBoolean("invalid-data-flag");
						boolean txAvgIDF = laneObject.getJSONObject("tx-power").getJSONObject("average").getBoolean("invalid-data-flag");
						
						logger.info("Tx Power minimum :"+txMin);
						logger.info("Tx Power maximum :"+txMax);
						logger.info("Tx Power average :"+txAvg);
						
						valuesMap.put("Tx Power Minimum",txMin);
						valuesMap.put("Tx Power Maximum",txMax);
						valuesMap.put("Tx Power Average",txAvg);
						
						idfMap.put("Tx Power Minimum",txMinIDF);
						idfMap.put("Tx Power Maximum",txMaxIDF);
						idfMap.put("Tx Power Average",txAvgIDF);
						
					}
					
					JSONObject laneObject = binObj.getJSONObject("statistics").getJSONObject("channel-power");
					
					String rxMin = laneObject.getJSONObject("rx-power").getJSONObject("minimum").optString("value");
					String rxMax = laneObject.getJSONObject("rx-power").getJSONObject("maximum").optString("value");
					String rxAvg = laneObject.getJSONObject("rx-power").getJSONObject("average").optString("value");
					
					boolean rxMinIDF = laneObject.getJSONObject("rx-power").getJSONObject("minimum").getBoolean("invalid-data-flag");
					boolean rxMaxIDF = laneObject.getJSONObject("rx-power").getJSONObject("maximum").getBoolean("invalid-data-flag");
					boolean rxAvgIDF = laneObject.getJSONObject("rx-power").getJSONObject("average").getBoolean("invalid-data-flag");
					
					logger.info("Rx Channel Power minimum :"+rxMin);
					logger.info("Rx Channel Power maximum :"+rxMax);
					logger.info("Rx Channel Power average :"+rxAvg);
					
					valuesMap.put("Rx Channel Power Minimum",rxMin);
					valuesMap.put("Rx Channel Power Maximum",rxMax);
					valuesMap.put("Rx Channel Power Average",rxAvg);
					
					idfMap.put("Rx Channel Power Minimum",rxMinIDF);
					idfMap.put("Rx Channel Power Maximum",rxMaxIDF);
					idfMap.put("Rx Channel Power Average",rxAvgIDF);
					
					String laneNumberStr = laneNumber.toString();
					String binEndTimeStr = binEndTime;
					String binNameStr = binName;
					
					
					for(String key:valuesMap.keySet())  {
					
					String parameter = key;
					String value = valuesMap.get(key);
					logger.info("Key"+key+", IDF MAP : "+idfMap);
					boolean valueIdf = idfMap.get(key);
										
					InventoryDeviceDetails perfData=new InventoryDeviceDetails(ip) {
						String sql="INSERT INTO optical_reports.fb_ws_perf_history (node_name, node_ip, configured_mode, interface_name, interface_type, profile_type, "
								+ "collection_start_time, collection_end_time, configured_bin_count, admin_state, oper_state, "
								+ "bin_collection_start_time, bin_collection_end_time, "
								+ "lane_number, bin_name, parameter_name, parameter_value, parameter_idf, collection_date, updated_time)	VALUES('"+nodeName+"', '"+ip+"', '"+configuredMode+"', '"+port+"', '"+portType+"', '"+profileType+"', '"
								+ collectionStartTime +"', '"+collectionEndTime+"', '"+configuredBinCount+"', '"+adminState+"', '"+operState+"', '"
								+ binStartTime+"', '"+binEndTimeStr+"', '"
								+laneNumberStr+"', '"+binNameStr+"', '"+ parameter +"', '"+value+"', "+valueIdf+",'"+polled_date+"', '"+polled_date+"') "
										+ "ON DUPLICATE KEY UPDATE parameter_value='"+value+"', parameter_idf="+valueIdf+", updated_time='"+polled_date+"'";
						@Override
						public String getInsertQuery() {
							return sql;
						}
					};
					
					performanceList.add(perfData);
					
					}
					
					logger.info("-------------------------------");	
				}
				
							
			

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		
		} // instance loop
		
		return performanceList;
	
	}
	
	
	public ArrayList<InventoryDeviceDetails> retrievePerformanceHistoryModem(String ip) {

		ArrayList<InventoryDeviceDetails> performanceList = new ArrayList<InventoryDeviceDetails>();
		
		String nodeName = nodeNameMap.get(ip);
		
		for(String modemInstance:modemInstances) {

		try {


				HttpsURLConnection apiConn = cienaWsClient.getHttpsConnectionWithoutCertificate(
						"https://" + ip + "/restconf/data/waveserver-pm/modem-performance-instances="+modemInstance, "GET", null);

				JSONObject jsonObject = (JSONObject) cienaWsClient.getJson(apiConn);
				
				JSONObject instanceJsonObject = jsonObject.getJSONArray("ciena-waveserver-pm:modem-performance-instances").getJSONObject(0);

				String profileType = instanceJsonObject.getJSONObject("id").getString("profile-type");
				
				String adminState = instanceJsonObject.getJSONObject("state").getString("admin-state");
				String operState = instanceJsonObject.getJSONObject("state").getString("operational-state");
				String collectionStartTime = instanceJsonObject.getJSONObject("state").getString("collection-start-date-time");
				String collectionEndTime = instanceJsonObject.getJSONObject("state").getString("collection-end-date-time");
				
				String configuredMode = instanceJsonObject.getJSONObject("properties").getString("configuration-mode");
				String configuredBinCount = instanceJsonObject.getJSONObject("properties").getInt("configured-bin-count") + " " + instanceJsonObject.getJSONObject("properties").getInt("configured-bin-duration");
				
				String port = instanceJsonObject.getJSONObject("attached-interface").getString("name");
				String portType = instanceJsonObject.getJSONObject("attached-interface").getString("type");
				
				//configured_mode, interface_name, interface_type, profile_type, "
					//	+ "collection_start_time, collection_end_time, configured_bin_count, admin_state, oper_state, "
	
				JSONObject currentBin =  instanceJsonObject.getJSONObject("current-bin");
				JSONObject current24hrBin =  instanceJsonObject.getJSONObject("current-24-hour-bin");
				JSONObject untimedBin =  instanceJsonObject.getJSONObject("untimed-bin");
				JSONObject history24hrBin = instanceJsonObject.getJSONObject("history-24-hour-bin");
				
				JSONArray allBins = instanceJsonObject.getJSONObject("history").getJSONArray("bins");
				
				//JSONArray allBins = new JSONArray();
				
				allBins.put(currentBin);
				allBins.put(current24hrBin);
				allBins.put(untimedBin);
				//allBins.put(historyBins);
				allBins.put(history24hrBin);
				
				HashMap<String,String> valuesMap = new HashMap<String,String>();
				HashMap<String,Boolean> idfMap = new HashMap<String,Boolean>();

			
				for(int i=0;i<allBins.length();i++) {
					JSONObject binObj = allBins.getJSONObject(i);
					
					String id = binObj.getJSONObject("id").getString("bin-name");
					String binName = id;
					
					if (id.equals("history")) {
						;
						binName = "History Bin #" +String.format("%02d", binObj.getInt("bin-number"));
					}
					
					logger.info(port + ", "+binName);
					
					//+ "bin_collection_start_time, bin_collection_end_time
					
					logger.info(port + ", Start Time : "+binObj.getJSONObject("state").getString("start-date-time"));
					
					String binStartTime = binObj.getJSONObject("state").getString("start-date-time");
					String binEndTime  = "";
					
					if (binObj.getJSONObject("state").has("cleared-date-time")) {
						logger.info(port + ", Cleared Time : "+binObj.getJSONObject("state").getString("cleared-date-time"));
					} else {
						logger.info(port + ", End time : "+binObj.getJSONObject("state").getString("end-date-time"));
						binEndTime = binObj.getJSONObject("state").getString("end-date-time");
					}
					
					logger.info(port + ",  : "+binName);
				
					JSONObject statsObject = binObj.getJSONObject("statistics");
					
					String preFecBER = statsObject.getJSONObject("pre-fec-bit-error-rate").getJSONObject("bit-error-rate").optString("value");
					String preFecBERMax = statsObject.getJSONObject("pre-fec-bit-error-rate").getJSONObject("maximum").optString("value");
					
					String preFecBERIdf = statsObject.getJSONObject("pre-fec-bit-error-rate").getJSONObject("bit-error-rate").optString("invalid-data-flag");
					String preFecBERMaxIdf = statsObject.getJSONObject("pre-fec-bit-error-rate").getJSONObject("maximum").optString("invalid-data-flag");

					logger.info("Pre-FEC BER :"+preFecBER);
					logger.info("Pre-FEC BER maximum :"+preFecBERMax);
					
					logger.info("Pre-FEC BER :"+preFecBER);
					logger.info("Pre-FEC BER maximum :"+preFecBERMax);

					
					String qFactor = statsObject.getJSONObject("q-factor").getJSONObject("q-factor").optString("value");
					String qFactorMin = statsObject.getJSONObject("q-factor").getJSONObject("minimum").optString("value");
					String qFactorMax = statsObject.getJSONObject("q-factor").getJSONObject("maximum").optString("value");
					
					boolean qFactorIdf = statsObject.getJSONObject("q-factor").getJSONObject("q-factor").getBoolean("invalid-data-flag");
					boolean qFactorMinIdf = statsObject.getJSONObject("q-factor").getJSONObject("minimum").getBoolean("invalid-data-flag");
					boolean qFactorMaxIdf = statsObject.getJSONObject("q-factor").getJSONObject("maximum").getBoolean("invalid-data-flag");
					
					logger.info("Q-factor :"+qFactor);
					logger.info("Q-factor Min :"+qFactorMin);
					logger.info("Q-factor Max :"+qFactorMax);
					
					valuesMap.put("Q-factor",qFactor);
					valuesMap.put("Q-factor Min",qFactorMin);
					valuesMap.put("Q-factor Max",qFactorMax);

					idfMap.put("Q-factor",qFactorIdf);
					idfMap.put("Q-factor Min",qFactorMinIdf);
					idfMap.put("Q-factor Max",qFactorMaxIdf);
					

					JSONObject fecFrameErrorCountObj = statsObject.getJSONObject("fec-error").optJSONObject("frame-error-count");
					String fecFrameErrorCount="";
					if (fecFrameErrorCountObj!=null) {
					    fecFrameErrorCount = fecFrameErrorCountObj.optString("value");
					} 
					
					String fecFrameErrorCountSecond = statsObject.getJSONObject("fec-error").getJSONObject("frame-error-count-second").optString("value");
					String fecUncorrectedBlockCount = statsObject.getJSONObject("fec-error").getJSONObject("uncorrected-block-count").optString("value");
					String highCorrectionCountSecond = statsObject.getJSONObject("fec-error").getJSONObject("high-correction-count-seconds").optString("value");
					
					boolean fecFrameErrorCountIdf = statsObject.getJSONObject("fec-error").getJSONObject("frame-error-count").getBoolean("invalid-data-flag");
					boolean fecFrameErrorCountSecondIdf = statsObject.getJSONObject("fec-error").getJSONObject("frame-error-count-second").getBoolean("invalid-data-flag");
					boolean fecUncorrectedBlockCountIdf = statsObject.getJSONObject("fec-error").getJSONObject("uncorrected-block-count").getBoolean("invalid-data-flag");
					boolean highCorrectionCountSecondIdf = statsObject.getJSONObject("fec-error").getJSONObject("high-correction-count-seconds").getBoolean("invalid-data-flag");
					
					logger.info("fecFrameErrorCount :"+fecFrameErrorCount);
					logger.info("fecFrameErrorCountSecond :"+fecFrameErrorCountSecond);
					logger.info("fecUncorrectedBlockCount :"+fecUncorrectedBlockCount);
					logger.info("highCorrectionCountSecond :"+highCorrectionCountSecond);
					
					valuesMap.put("fecFrameErrorCount",fecFrameErrorCount);
					valuesMap.put("fecFrameErrorCountSecond",fecFrameErrorCountSecond);
					valuesMap.put("fecUncorrectedBlockCount",fecUncorrectedBlockCount);
					valuesMap.put("highCorrectionCountSecond",highCorrectionCountSecond);
					
					idfMap.put("fecFrameErrorCount",fecFrameErrorCountIdf);
					idfMap.put("fecFrameErrorCountSecond",fecFrameErrorCountSecondIdf);
					idfMap.put("fecUncorrectedBlockCount",fecUncorrectedBlockCountIdf);
					idfMap.put("highCorrectionCountSecond",highCorrectionCountSecondIdf);

					
					String dgdAvg = statsObject.getJSONObject("dgd").getJSONObject("average").optString("value");
					String dgdMax = statsObject.getJSONObject("dgd").getJSONObject("maximum").optString("value");
					
					boolean dgdAvgIdf = statsObject.getJSONObject("dgd").getJSONObject("average").getBoolean("invalid-data-flag");
					boolean dgdMaxIdf = statsObject.getJSONObject("dgd").getJSONObject("maximum").getBoolean("invalid-data-flag");
					
					logger.info("dgdAvg :"+dgdAvg);
					logger.info("dgdMax :"+dgdMax);
					
					valuesMap.put("dgdAvg",dgdAvg);
					valuesMap.put("dgdMax",dgdMax);

					idfMap.put("dgdAvg",dgdAvgIdf);
					idfMap.put("dgdMax",dgdMaxIdf);

					
					String pdlAvg = statsObject.getJSONObject("pdl").getJSONObject("average").optString("value");
					String pdlMax = statsObject.getJSONObject("pdl").getJSONObject("maximum").optString("value");
					
					boolean pdlAvgIdf = statsObject.getJSONObject("pdl").getJSONObject("average").getBoolean("invalid-data-flag");
					boolean pdlMaxIdf = statsObject.getJSONObject("pdl").getJSONObject("maximum").getBoolean("invalid-data-flag");
					
					logger.info("pdlAvg :"+pdlAvg);
					logger.info("pdlMax :"+pdlMax);
					
					valuesMap.put("pdlAvg",pdlAvg);
					valuesMap.put("pdlMax",pdlMax);

					idfMap.put("pdlAvg",pdlAvgIdf);
					idfMap.put("pdlMax",pdlMaxIdf);

					
					String laneNumberStr = "";
					String binEndTimeStr = binEndTime;
					String binNameStr = binName;

					
					for(String key:valuesMap.keySet())  {
						
					String parameter = key;
					String value = valuesMap.get(key);
					logger.info("Key"+key+", IDF MAP : "+idfMap);
					boolean valueIdf = idfMap.get(key);
										
					InventoryDeviceDetails perfData=new InventoryDeviceDetails(ip) {
						String sql="INSERT INTO optical_reports.fb_ws_perf_history (node_name, node_ip, configured_mode, interface_name, interface_type, profile_type, "
								+ "collection_start_time, collection_end_time, configured_bin_count, admin_state, oper_state, "
								+ "bin_collection_start_time, bin_collection_end_time, "
								+ "lane_number, bin_name, parameter_name, parameter_value, parameter_idf, collection_date, updated_time)	VALUES('"+nodeName+"', '"+ip+"', '"+configuredMode+"', '"+port+"', '"+portType+"', '"+profileType+"', '"
								+ collectionStartTime +"', '"+collectionEndTime+"', '"+configuredBinCount+"', '"+adminState+"', '"+operState+"', '"
								+ binStartTime+"', '"+binEndTimeStr+"', '"
								+laneNumberStr+"', '"+binNameStr+"', '"+ parameter +"', '"+value+"', "+valueIdf+",'"+polled_date+"', '"+polled_date+"') "
										+ "ON DUPLICATE KEY UPDATE parameter_value='"+value+"', parameter_idf="+valueIdf+", updated_time='"+polled_date+"'";
						@Override
						public String getInsertQuery() {
							return sql;
						}
					};
					
					performanceList.add(perfData);
					
					}

					
					
					logger.info("-------------------------------");	
				}
				
	
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		
		}
		
		return performanceList;
	
	}
	
	
	public ArrayList<InventoryDeviceDetails> retrievePerformanceHistoryOdu(String ip) {

		ArrayList<InventoryDeviceDetails> performanceList = new ArrayList<InventoryDeviceDetails>();
		
		String nodeName = nodeNameMap.get(ip);

		for (String oduInstance:oduInstances) {
			
		
		try {


				HttpsURLConnection apiConn = cienaWsClient.getHttpsConnectionWithoutCertificate(
						"https://" + ip + "/restconf/data/waveserver-pm/odu-performance-instances="+oduInstance, "GET", null);

				JSONObject jsonObject = (JSONObject) cienaWsClient.getJson(apiConn);
				
				JSONObject instanceJsonObject = jsonObject.getJSONArray("ciena-waveserver-pm:odu-performance-instances").getJSONObject(0);
				
				String profileType = instanceJsonObject.getJSONObject("id").getString("profile-type");
				
				String adminState = instanceJsonObject.getJSONObject("state").getString("admin-state");
				String operState = instanceJsonObject.getJSONObject("state").getString("operational-state");
				String collectionStartTime = instanceJsonObject.getJSONObject("state").getString("collection-start-date-time");
				String collectionEndTime = instanceJsonObject.getJSONObject("state").getString("collection-end-date-time");
				
				String configuredMode = instanceJsonObject.getJSONObject("properties").getString("configuration-mode");
				String configuredBinCount = instanceJsonObject.getJSONObject("properties").getInt("configured-bin-count") + " " + instanceJsonObject.getJSONObject("properties").getInt("configured-bin-duration");
				
				String port = instanceJsonObject.getJSONObject("attached-interface").getString("name");
				String portType = instanceJsonObject.getJSONObject("attached-interface").getString("type");
				
				//configured_mode, interface_name, interface_type, profile_type, "
					//	+ "collection_start_time, collection_end_time, configured_bin_count, admin_state, oper_state, "

				
				JSONObject currentBin =  instanceJsonObject.getJSONObject("current-bin");
				JSONObject current24hrBin =  instanceJsonObject.getJSONObject("current-24-hour-bin");
				JSONObject untimedBin =  instanceJsonObject.getJSONObject("untimed-bin");
				JSONObject history24hrBin = instanceJsonObject.getJSONObject("history-24-hour-bin");
				
				JSONArray allBins = instanceJsonObject.getJSONObject("history").getJSONArray("bins");
				
				//JSONArray allBins = new JSONArray();
				
				allBins.put(currentBin);
				allBins.put(current24hrBin);
				allBins.put(untimedBin);
				//allBins.put(historyBins);
				allBins.put(history24hrBin);
				
				HashMap<String,String> valuesMap = new HashMap<String,String>();
				HashMap<String,Boolean> idfMap = new HashMap<String,Boolean>();

				
				for(int i=0;i<allBins.length();i++) {
					JSONObject binObj = allBins.getJSONObject(i);
					
					String id = binObj.getJSONObject("id").getString("bin-name");
					String binName = id;
					
					if (id.equals("history")) {
						;
						binName = "History Bin #" +String.format("%02d", binObj.getInt("bin-number"));
					}
					
					logger.info(port + ", "+binName);
					
					//+ "bin_collection_start_time, bin_collection_end_time
					
					logger.info(port + ", Start Time : "+binObj.getJSONObject("state").getString("start-date-time"));
					
					String binStartTime = binObj.getJSONObject("state").getString("start-date-time");
					String binEndTime  = "";
					
					if (binObj.getJSONObject("state").has("cleared-date-time")) {
						logger.info(port + ", Cleared Time : "+binObj.getJSONObject("state").getString("cleared-date-time"));
					} else {
						logger.info(port + ", End time : "+binObj.getJSONObject("state").getString("end-date-time"));
						binEndTime = binObj.getJSONObject("state").getString("end-date-time");
					}
					
					logger.info(port + ",  : "+binName);
				
					JSONObject statsObject = binObj.getJSONObject("statistics");
					
					String oduBBE = statsObject.getJSONObject("background-block-errors").optString("value");
					String oduES = statsObject.getJSONObject("errored-seconds").optString("value");
					String oduSES = statsObject.getJSONObject("severely-errored-seconds").optString("value");
					String oduUAS = statsObject.getJSONObject("unavailable-seconds").optString("value");
					
					boolean oduBBEIdf = statsObject.getJSONObject("background-block-errors").getBoolean("invalid-data-flag");
					boolean oduESIdf = statsObject.getJSONObject("errored-seconds").getBoolean("invalid-data-flag");
					boolean oduSESIdf = statsObject.getJSONObject("severely-errored-seconds").getBoolean("invalid-data-flag");
					boolean oduUASIdf = statsObject.getJSONObject("unavailable-seconds").getBoolean("invalid-data-flag");

					
					String oduBBEfarEnd = statsObject.getJSONObject("far-end").getJSONObject("background-block-errors").optString("value");
					String oduESfarEnd = statsObject.getJSONObject("far-end").getJSONObject("errored-seconds").optString("value");
					String oduSESfarEnd = statsObject.getJSONObject("far-end").getJSONObject("severely-errored-seconds").optString("value");
					String oduUASfarEnd = statsObject.getJSONObject("far-end").getJSONObject("unavailable-seconds").optString("value");
					
					boolean oduBBEfarEndIdf = statsObject.getJSONObject("far-end").getJSONObject("background-block-errors").getBoolean("invalid-data-flag");
					boolean oduESfarEndIdf = statsObject.getJSONObject("far-end").getJSONObject("errored-seconds").getBoolean("invalid-data-flag");
					boolean oduSESfarEndIdf = statsObject.getJSONObject("far-end").getJSONObject("severely-errored-seconds").getBoolean("invalid-data-flag");
					boolean oduUASfarEndIdf = statsObject.getJSONObject("far-end").getJSONObject("unavailable-seconds").getBoolean("invalid-data-flag");

					logger.info("oduBBE :"+oduBBE);
					logger.info("oduES :"+oduES);
					logger.info("oduSES :"+oduSES);
					logger.info("oduUAS :"+oduUAS);
					
					logger.info("oduBBEfarEnd :"+oduBBEfarEnd);
					logger.info("oduESfarEnd :"+oduESfarEnd);
					logger.info("oduSESfarEnd :"+oduSESfarEnd);
					logger.info("oduUASfarEnd :"+oduUASfarEnd);
					
					valuesMap.put("ODU BBE",oduBBE);
					valuesMap.put("ODU ES",oduES);
					valuesMap.put("ODU SES",oduSES);
					valuesMap.put("ODU UAS",oduUAS);
					
					valuesMap.put("ODU BBE - FE",oduBBEfarEnd);
					valuesMap.put("ODU ES - FE",oduESfarEnd);
					valuesMap.put("ODU SES - FE",oduSESfarEnd);
					valuesMap.put("ODU UAS - FE",oduUASfarEnd);
					
					idfMap.put("ODU BBE",oduBBEIdf);
					idfMap.put("ODU ES",oduESIdf);
					idfMap.put("ODU SES",oduSESIdf);
					idfMap.put("ODU UAS",oduUASIdf);
					
					idfMap.put("ODU BBE - FE",oduBBEfarEndIdf);
					idfMap.put("ODU ES - FE",oduESfarEndIdf);
					idfMap.put("ODU SES - FE",oduSESfarEndIdf);
					idfMap.put("ODU UAS - FE",oduUASfarEndIdf);
					
					String laneNumberStr = "";
					String binEndTimeStr = binEndTime;
					String binNameStr = binName;

					
					for(String key:valuesMap.keySet())  {
						
					String parameter = key;
					String value = valuesMap.get(key);
					logger.info("Key"+key+", IDF MAP : "+idfMap);
					boolean valueIdf = idfMap.get(key);
										
					InventoryDeviceDetails perfData=new InventoryDeviceDetails(ip) {
						String sql="INSERT INTO optical_reports.fb_ws_perf_history (node_name, node_ip, configured_mode, interface_name, interface_type, profile_type, "
								+ "collection_start_time, collection_end_time, configured_bin_count, admin_state, oper_state, "
								+ "bin_collection_start_time, bin_collection_end_time, "
								+ "lane_number, bin_name, parameter_name, parameter_value, parameter_idf, collection_date, updated_time)	VALUES('"+nodeName+"', '"+ip+"', '"+configuredMode+"', '"+port+"', '"+portType+"', '"+profileType+"', '"
								+ collectionStartTime +"', '"+collectionEndTime+"', '"+configuredBinCount+"', '"+adminState+"', '"+operState+"', '"
								+ binStartTime+"', '"+binEndTimeStr+"', '"
								+laneNumberStr+"', '"+binNameStr+"', '"+ parameter +"', '"+value+"', "+valueIdf+",'"+polled_date+"', '"+polled_date+"') "
										+ "ON DUPLICATE KEY UPDATE parameter_value='"+value+"', parameter_idf="+valueIdf+", updated_time='"+polled_date+"'";
						@Override
						public String getInsertQuery() {
							return sql;
						}
					};
					
					performanceList.add(perfData);
					
					}

					
					logger.info("-------------------------------");	
				}
				
	
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		
		}
		
		return performanceList;
	
	}
	
	
	public ArrayList<InventoryDeviceDetails> retrieveActiveAlarmsWS(String ip) {

		ArrayList<InventoryDeviceDetails> performanceList = new ArrayList<InventoryDeviceDetails>();
		
		String nodeName = nodeNameMap.get(ip);
		
		try {

				HttpsURLConnection apiConn = cienaWsClient.getHttpsConnectionWithoutCertificate(
						"https://" + ip + "/restconf/data/waveserver-alarms/active", "GET", null);

				JSONObject jsonObject = (JSONObject) cienaWsClient.getJson(apiConn);
				
				JSONArray alarmsArray = jsonObject.getJSONArray("ciena-waveserver-alarm:active");
				
				
				for(int i=0;i<alarmsArray.length();i++) {
					JSONObject alarmObj = alarmsArray.getJSONObject(i);
					
					int alarm_instance_id = alarmObj.getInt("alarm-instance-id");
					boolean acknowledged = alarmObj.getBoolean("acknowledged");
					boolean intermittent = alarmObj.getBoolean("intermittent");
					int alarm_table_id = alarmObj.getInt("alarm-table-id");
					String severity = alarmObj.getString("severity");
					
					
					
					String alarmDate = alarmObj.getString("local-date-time");
					
					DateFormat dateFormat = new SimpleDateFormat(
				            "EEE MMM dd HH:mm:ss yyyy");
					
					DateFormat sqlDateFormat = new SimpleDateFormat(
				            "yyyy-MM-dd HH:mm:ss");
				
					String local_date_time = sqlDateFormat.format(dateFormat.parse(alarmDate));
					
					String instance = alarmObj.getString("instance");
					String description = alarmObj.getString("description");
					int site_identifier = alarmObj.getInt("site-identifier");
					int group_identifier = alarmObj.getInt("group-identifier");
					int member_identifier = alarmObj.getInt("member-identifier");
					
					/*logger.info("alarm-instance-id : "+alarmObj.getInt("alarm-instance-id"));
					logger.info("acknowledged : "+alarmObj.getBoolean("acknowledged"));
					logger.info("intermittent : "+alarmObj.getBoolean("intermittent"));
					logger.info("alarm-table-id : "+alarmObj.getInt("alarm-table-id"));
					logger.info("severity : "+alarmObj.getString("severity"));
					logger.info("local-date-time : "+alarmObj.getString("local-date-time"));
					logger.info("instance : "+alarmObj.getString("instance"));
					logger.info("description : "+alarmObj.getString("description"));
					logger.info("site-identifier : "+alarmObj.getInt("site-identifier"));
					logger.info("group-identifier : "+alarmObj.getInt("group-identifier"));
					logger.info("member-identifier : "+alarmObj.getInt("member-identifier"));
					logger.info("-------------------------------");	*/
					
					
					
					InventoryDeviceDetails perfData=new InventoryDeviceDetails(ip) {
						String sql="INSERT INTO optical_reports.fb_ws_alarms_current(node_name, node_ip, alarm_instance_id, acknowledged, intermittent, "
								+ "alarm_table_id, severity, local_date_time, instance_name, description, "
								+ "site_identifier, group_identifier, member_identifier, collection_date, updated_time) VALUES("
								+ "'"+nodeName+"', '"+ip+"', '"+alarm_instance_id+"', "+acknowledged+", "+intermittent+", "
								+ "'"+alarm_table_id+"', '"+severity+"', '"+local_date_time+"', '"+instance+"', '"+description+"', "
								+ "'"+site_identifier+"',"+group_identifier+", '"+member_identifier+"', '"+polled_date+"', '"+polled_date+"')"
								+ " ON DUPLICATE KEY UPDATE updated_time='"+polled_date+"'";
						@Override
						public String getInsertQuery() {
							return sql;
						}
					};
					
					performanceList.add(perfData);
	

				}

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		
		return performanceList;
	
	}
	
	public String generateReport(String location,String fileNamePrefix) {
		
		DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		logger.info(f.format(polled_date));
		
		String dateStr = f.format(polled_date);
		
		//String dateStr = "2020-07-12";
		try {
		String filePath = CienaProperties.get("ciena.fbdailyreport.folder");
		File file = new File(filePath);
		if (!file.exists()) {
			System.out.println("Folder "+filePath+" does not exists. Creating it.");
			logger.info("Folder "+filePath+" does not exists. Creating it.");
			file.createNewFile();
		} 
		
		String excelPath = filePath + File.separator +  fileNamePrefix + dateStr+".xlsx";
		
		logger.info("FB Daily Report generation started for "+location+", "+dateStr);
		FileOutputStream fileOut = new FileOutputStream(excelPath);
	    XSSFWorkbook wworkbook=createWorkBook();
	    
	    if (location.equals("Chennai")) {
	    
	    	create6500PerformanceCurrent(wworkbook,dateStr,"'100.65.244.98','100.65.244.106'");
		
	    	createWsPerformanceCurrent(wworkbook,dateStr,"Wave Server-1",ipListWS[0],ipListWS[2]);
	    	createWsPerformanceCurrent(wworkbook,dateStr,"Wave Server-2",ipListWS[1],ipListWS[3]);
		
	    	createWsPerformanceHistory(wworkbook, dateStr, "SSF Physical 15m&24 Hr PM", ipListWS[0],ipListWS[1]);
	    	createWsPerformanceHistory(wworkbook, dateStr, "FPS Physical 15m&24 Hr PM", ipListWS[2],ipListWS[3]);
		
	    	create6500pm(wworkbook,dateStr,ipList6500[0],ipList6500[1]);
		
	    	createWsCurrentAlarms(wworkbook,dateStr,"'100.65.244.99','100.65.244.100','100.65.244.107','100.65.244.108'");
		
	    	create6500CurrentAlarms(wworkbook,dateStr,"'100.65.244.98','100.65.244.106'");
		
	    	create6500HistoryAlarms(wworkbook,dateStr,ipList6500[0],ipList6500[1]);
		
	    	createOPSStatus(wworkbook,dateStr,"'100.65.244.98','100.65.244.106'");
		
	    } else if (location.equals("Mumbai")) {
	    	
           
		    
			create6500PerformanceCurrent(wworkbook,dateStr,"'100.70.41.190','100.70.41.186'");
			
			createWsPerformanceCurrent(wworkbook,dateStr,"Wave Server-1",ipListWS[4],ipListWS[6]);
			createWsPerformanceCurrent(wworkbook,dateStr,"Wave Server-2",ipListWS[5],ipListWS[7]);
			
			createWsPerformanceHistory(wworkbook, dateStr, "GPX Physical 15m&24 Hr PM", ipListWS[4],ipListWS[5]);
			createWsPerformanceHistory(wworkbook, dateStr, "Rabale Physical 15m&24 Hr PM", ipListWS[6],ipListWS[7]);
			
			create6500pm(wworkbook,dateStr,ipList6500[2],ipList6500[3]);
			
			createWsCurrentAlarms(wworkbook,dateStr,"'100.70.141.210','100.70.141.211','100.70.141.194','100.70.141.195'");
			
			create6500CurrentAlarms(wworkbook,dateStr,"'100.70.41.190','100.70.41.186'");
			
			create6500HistoryAlarms(wworkbook,dateStr,ipList6500[2],ipList6500[3]);
			
			createOPSStatus(wworkbook,dateStr,"'100.70.41.190','100.70.41.186'");
			
		    
	    	
	    }
		
		wworkbook.write(fileOut);
		fileOut.flush();
		fileOut.close();
		logger.info("FB Daily Report generation completed for "+location+", "+dateStr);
		} catch(Exception e) {
			e.printStackTrace(System.out);
		}
		
		return dateStr;
	}
	
	private XSSFWorkbook createWorkBook() throws Exception{
		  return  new XSSFWorkbook();
	}
	
	public void create6500PerformanceCurrent(XSSFWorkbook wworkbook,String dateStr,String ipList) {
		try{
			XSSFSheet sheet = wworkbook.createSheet("6500 Parameters");
	        Row row;
	        Cell cell;	
	        int rowcount=0;
	        row = sheet.createRow(rowcount);
	        cell = row.createCell(0);
	        cell.setCellValue("Node Name");;
	      
	        row.createCell(1).setCellValue("Node IP");
	        row.createCell(2).setCellValue("Port");
	        row.createCell(3).setCellValue("Parameter");
	        row.createCell(4).setCellValue("Value");
	        
	        
	        rowcount++;
	        
	        Connection connection = null;
			ResultSet result = null;
			Statement stmt = null;
	        
			DbConnection  dbObj = new DbConnection();
	    	connection = dbObj.getDBConnection();
			String query = "select node_name,node_ip,port_id,parameter_name,parameter_value from optical_reports.fb_6500_perf_current where collection_date='"+dateStr+"' and node_ip in ("+ipList+") order by node_ip";
			//			logger.info(query);
			logger.info("query is " + query);

			stmt = connection.createStatement();
			result = stmt.executeQuery(query);

			while (result.next()) {
				
				String nodeName = result.getString("node_name");
				String nodeIp = result.getString("node_ip");
				String portId = result.getString("port_id");
				String parameterName = result.getString("parameter_name");
				Double parameterValue  = result.getDouble("parameter_value");
				
				row = sheet.createRow(rowcount);
	            cell = row.createCell(0);
	            cell.setCellValue(nodeName);
	            cell = row.createCell(1);
	            cell.setCellValue(nodeIp);
	            cell = row.createCell(2);
	            cell.setCellValue(portId);
	            cell = row.createCell(3);
	            cell.setCellValue(parameterName);
	            cell = row.createCell(4);
	            cell.setCellValue(parameterValue);
	            rowcount++;
			}
			
	        for (int i = 0; i < 5; i++) {
	            sheet.autoSizeColumn(i);
	        }
	
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void create6500pm(XSSFWorkbook wworkbook,String dateStr,String ip1, String ip2) {
		
		XSSFSheet sheet = wworkbook.createSheet("6500 PM");
		addRowsFor6500pm(sheet,dateStr,ip1,0,0);
		addRowsFor6500pm(sheet,dateStr,ip2,0,9);
	}
	
	public void addRowsFor6500pm(XSSFSheet sheet,String dateStr,String ip,int rowcount,int cellIndex) {
		try{
			
	        Row row;
	        Cell cell;	
	        //int rowcount=0;
	        row = (sheet.getRow(rowcount)==null)?sheet.createRow(rowcount): sheet.getRow(rowcount);;

	        row.createCell(cellIndex+0).setCellValue("Node Name");;
	        row.createCell(cellIndex+1).setCellValue("Node IP");
	        row.createCell(cellIndex+2).setCellValue("Port");
	        row.createCell(cellIndex+3).setCellValue("location");
	        row.createCell(cellIndex+4).setCellValue("direction");
	        row.createCell(cellIndex+5).setCellValue("Parameter");
	        row.createCell(cellIndex+6).setCellValue("Value");
	        
	        rowcount++;
	        
	        Connection connection = null;
			ResultSet result = null;
			Statement stmt = null;
	        
			DbConnection  dbObj = new DbConnection();
	    	connection = dbObj.getDBConnection();
			String query = "select node_name,node_ip,port_id,parameter_name,parameter_value,location,direction from optical_reports.fb_6500_pm where collection_date='"+dateStr+"' and node_ip='"+ip+"' order by node_name,port_id,parameter_name";
			//			logger.info(query);
			logger.info("query is " + query);

			stmt = connection.createStatement();
			result = stmt.executeQuery(query);

			while (result.next()) {
				
				String nodeName = result.getString("node_name");
				String nodeIp = result.getString("node_ip");
				String portId = result.getString("port_id");
				String location = result.getString("location");
				String direction = result.getString("direction");
				String parameterName = result.getString("parameter_name");
				String parameterValue  = result.getString("parameter_value");
				
				row = (sheet.getRow(rowcount)==null)?sheet.createRow(rowcount): sheet.getRow(rowcount);
	            cell = row.createCell(cellIndex+0);
	            cell.setCellValue(nodeName);
	            cell = row.createCell(cellIndex+1);
	            cell.setCellValue(nodeIp);
	            cell = row.createCell(cellIndex+2);
	            cell.setCellValue(portId);
	            cell = row.createCell(cellIndex+3);
	            cell.setCellValue(location);
	            cell = row.createCell(cellIndex+4);
	            cell.setCellValue(direction);
	            cell = row.createCell(cellIndex+5);
	            cell.setCellValue(parameterName);
	            cell = row.createCell(cellIndex+6);
	            cell.setCellValue(parameterValue);
	            rowcount++;
			}
			
	        for (int i = 0; i < 7; i++) {
	            sheet.autoSizeColumn(cellIndex+i);
	        }
	
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
	public void createWsPerformanceCurrent(XSSFWorkbook wworkbook,String dateStr,String sheetname,String wsIp1, String wsIp2) {
		try{
			XSSFSheet sheet = wworkbook.createSheet(sheetname);
	        Row row;
	        Cell cell;	
	        int rowcount=0;
	        row = sheet.createRow(rowcount);
	        row.createCell(0).setCellValue("Node Name");;
	        row.createCell(1).setCellValue("Node IP");
	        row.createCell(2).setCellValue("Port");
	        row.createCell(3).setCellValue("Parameter");
	        row.createCell(4).setCellValue("Value");
	        rowcount++;
	        
	        
	        Connection connection = null;
			ResultSet result = null;
			Statement stmt = null;
	        
			DbConnection  dbObj = new DbConnection();
	    	connection = dbObj.getDBConnection();
			String query = "select node_name,node_ip,port_id,parameter_name,parameter_value from optical_reports.fb_ws_perf_current where collection_date='"+dateStr+"' and node_ip in ('"+wsIp1+"','"+wsIp2+"') order by node_ip,port_id";
			//			logger.info(query);
			logger.info("query is " + query);

			stmt = connection.createStatement();
			result = stmt.executeQuery(query);

			while (result.next()) {
				
				String nodeName = result.getString("node_name");
				String nodeIp = result.getString("node_ip");
				String portId = result.getString("port_id");
				String parameterName = result.getString("parameter_name");
				String parameterValue  = result.getString("parameter_value");
				
			//	String portNumber[] = portId.split("-");
				
				
				row = sheet.createRow(rowcount);
	            cell = row.createCell(0);
	            cell.setCellValue(nodeName);
	            cell = row.createCell(1);
	            cell.setCellValue(nodeIp);
	            cell = row.createCell(2);
	            cell.setCellValue(portId);
	            cell = row.createCell(3);
	            cell.setCellValue(parameterName);
	            cell = row.createCell(4);
	            cell.setCellValue(parameterValue);
	            
	            rowcount++;

//	            if (rowcount+Integer.parseInt(portNumber[1]) == 10) rowcount=11;
			}
			
	        for (int i = 0; i < 5; i++) {
	            sheet.autoSizeColumn(i);
	        }

			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void createWsPerformanceHistory(XSSFWorkbook wworkbook,String dateStr,String sheetname,String wsIp1, String wsIp2) {
		XSSFSheet sheet = wworkbook.createSheet(sheetname);
		int nextRowIndex = addRowsForWsPerformanceHistory(wworkbook,dateStr,wsIp1,"1-1","optical-power",sheet,0,0);
		nextRowIndex = addRowsForWsPerformanceHistory(wworkbook,dateStr,wsIp1,"1-1","modem-performance",sheet,nextRowIndex,0);
		nextRowIndex = addRowsForWsPerformanceHistory(wworkbook,dateStr,wsIp1,"1-1.1","odu-performance",sheet,nextRowIndex,0);
		
		nextRowIndex = addRowsForWsPerformanceHistory(wworkbook,dateStr,wsIp1,"1-2","optical-power",sheet,nextRowIndex,0);
		nextRowIndex = addRowsForWsPerformanceHistory(wworkbook,dateStr,wsIp1,"1-2","modem-performance",sheet,nextRowIndex,0);
		nextRowIndex = addRowsForWsPerformanceHistory(wworkbook,dateStr,wsIp1,"1-2.1","odu-performance",sheet,nextRowIndex,0);
		
		nextRowIndex = addRowsForWsPerformanceHistory(wworkbook,dateStr,wsIp1,"2-1","optical-power",sheet,nextRowIndex,0);
		nextRowIndex = addRowsForWsPerformanceHistory(wworkbook,dateStr,wsIp1,"2-1","modem-performance",sheet,nextRowIndex,0);
		nextRowIndex = addRowsForWsPerformanceHistory(wworkbook,dateStr,wsIp1,"2-1.1","odu-performance",sheet,nextRowIndex,0);
		
		nextRowIndex = addRowsForWsPerformanceHistory(wworkbook,dateStr,wsIp1,"2-2","optical-power",sheet,nextRowIndex,0);
		nextRowIndex = addRowsForWsPerformanceHistory(wworkbook,dateStr,wsIp1,"2-2","modem-performance",sheet,nextRowIndex,0);
		nextRowIndex = addRowsForWsPerformanceHistory(wworkbook,dateStr,wsIp1,"2-2.1","odu-performance",sheet,nextRowIndex,0);
		
		nextRowIndex = addRowsForWsPerformanceHistory(wworkbook,dateStr,wsIp2,"1-1","optical-power",sheet,0,4);
		nextRowIndex = addRowsForWsPerformanceHistory(wworkbook,dateStr,wsIp2,"1-1","modem-performance",sheet,nextRowIndex,4);
		nextRowIndex = addRowsForWsPerformanceHistory(wworkbook,dateStr,wsIp2,"1-1.1","odu-performance",sheet,nextRowIndex,4);
		
		nextRowIndex = addRowsForWsPerformanceHistory(wworkbook,dateStr,wsIp2,"1-2","optical-power",sheet,nextRowIndex,4);
		nextRowIndex = addRowsForWsPerformanceHistory(wworkbook,dateStr,wsIp2,"1-2","modem-performance",sheet,nextRowIndex,4);
		nextRowIndex = addRowsForWsPerformanceHistory(wworkbook,dateStr,wsIp2,"1-2.1","odu-performance",sheet,nextRowIndex,4);
		
		nextRowIndex = addRowsForWsPerformanceHistory(wworkbook,dateStr,wsIp2,"2-1","optical-power",sheet,nextRowIndex,4);
		nextRowIndex = addRowsForWsPerformanceHistory(wworkbook,dateStr,wsIp2,"2-1","modem-performance",sheet,nextRowIndex,4);
		nextRowIndex = addRowsForWsPerformanceHistory(wworkbook,dateStr,wsIp2,"2-1.1","odu-performance",sheet,nextRowIndex,4);
		
		nextRowIndex = addRowsForWsPerformanceHistory(wworkbook,dateStr,wsIp2,"2-2","optical-power",sheet,nextRowIndex,4);
		nextRowIndex = addRowsForWsPerformanceHistory(wworkbook,dateStr,wsIp2,"2-2","modem-performance",sheet,nextRowIndex,4);
		nextRowIndex = addRowsForWsPerformanceHistory(wworkbook,dateStr,wsIp2,"2-2.1","odu-performance",sheet,nextRowIndex,4);

		
        for (int i = 0; i < 10; i++) {
            sheet.autoSizeColumn(i);
        }

		
	
	}
	

	public int addRowsForWsPerformanceHistory(XSSFWorkbook wworkbook,String dateStr,String wsIp1,String portName, String profileType,XSSFSheet sheet,int rowIndex, int cellIndex) {
		
		int rowcount=rowIndex;
		try{
			//XSSFSheet sheet = wworkbook.createSheet(sheetname);
	        Row row;
	        Cell cell;	
	        
	        
	        Connection connection = null;
			ResultSet result = null;
			Statement stmt = null;
	        
			DbConnection  dbObj = new DbConnection();
	    	connection = dbObj.getDBConnection();
			String query = "SELECT node_name, node_ip, configured_mode, interface_name, interface_type, profile_type, collection_start_time, "
					+ "collection_end_time, configured_bin_count, admin_state, oper_state, bin_collection_start_time, bin_collection_end_time, "
					+ "lane_number, bin_name, parameter_name, parameter_value, parameter_idf, collection_date, updated_time FROM optical_reports.fb_ws_perf_history where collection_date='"+dateStr+"' and node_ip ='"+wsIp1+"' and interface_name ='"+portName+"' and profile_type ='"+profileType+"'  order by bin_name";
			//			logger.info(query);
			logger.info("query is " + query);

			stmt = connection.createStatement();
			result = stmt.executeQuery(query);
			
			boolean recordExists = result.next();
			if (!recordExists) return rowcount;
			
			row = (sheet.getRow(rowcount)==null)?sheet.createRow(rowcount): sheet.getRow(rowcount); // empty row.
			rowcount++;
			row = (sheet.getRow(rowcount)==null)?sheet.createRow(rowcount): sheet.getRow(rowcount); // empty row.
			rowcount++;

			
			row = (sheet.getRow(rowcount)==null)?sheet.createRow(rowcount): sheet.getRow(rowcount);
			row.createCell(cellIndex+0).setCellValue(result.getString("node_name"));
			rowcount++;
			
			row = (sheet.getRow(rowcount)==null)?sheet.createRow(rowcount): sheet.getRow(rowcount); 
			row.createCell(cellIndex+0).setCellValue("+--- PERFORMANCE MONITORING INSTANCE ----+");
			rowcount++;
			
			
			row = (sheet.getRow(rowcount)==null)?sheet.createRow(rowcount): sheet.getRow(rowcount);
			row.createCell(cellIndex+0).setCellValue("Parameter");
			row.createCell(cellIndex+1).setCellValue("Value");
			rowcount++;

			
			row = (sheet.getRow(rowcount)==null)?sheet.createRow(rowcount): sheet.getRow(rowcount);
			row.createCell(cellIndex+0).setCellValue("Configured Mode");
			row.createCell(cellIndex+1).setCellValue(result.getString("configured_mode"));
			rowcount++;
			
			row = (sheet.getRow(rowcount)==null)?sheet.createRow(rowcount): sheet.getRow(rowcount);
			row.createCell(cellIndex+0).setCellValue("Interface Type");
			row.createCell(cellIndex+1).setCellValue(result.getString("interface_type"));
			rowcount++;
			
			row = (sheet.getRow(rowcount)==null)?sheet.createRow(rowcount): sheet.getRow(rowcount);
			row.createCell(cellIndex+0).setCellValue("Interface Name");
			row.createCell(cellIndex+1).setCellValue(result.getString("interface_name"));
			rowcount++;
			
			row = (sheet.getRow(rowcount)==null)?sheet.createRow(rowcount): sheet.getRow(rowcount);
			row.createCell(cellIndex+0).setCellValue("Profile Type ");
			row.createCell(cellIndex+1).setCellValue(result.getString("profile_type"));
			rowcount++;
			
			row = (sheet.getRow(rowcount)==null)?sheet.createRow(rowcount): sheet.getRow(rowcount);
			row.createCell(cellIndex+0).setCellValue("Collection Start Date/Time");
			row.createCell(cellIndex+1).setCellValue(result.getString("collection_start_time"));
			rowcount++;

			row = (sheet.getRow(rowcount)==null)?sheet.createRow(rowcount): sheet.getRow(rowcount);
			row.createCell(cellIndex+0).setCellValue("Collection End Date/Time");
			row.createCell(cellIndex+1).setCellValue(result.getString("collection_end_time"));
			rowcount++;

			row = (sheet.getRow(rowcount)==null)?sheet.createRow(rowcount): sheet.getRow(rowcount);
			row.createCell(cellIndex+0).setCellValue("Admin State");
			row.createCell(cellIndex+1).setCellValue(result.getString("admin_state"));
			rowcount++;

			row = (sheet.getRow(rowcount)==null)?sheet.createRow(rowcount): sheet.getRow(rowcount);
			row.createCell(cellIndex+0).setCellValue("Oper State");
			row.createCell(cellIndex+1).setCellValue(result.getString("oper_state"));
			rowcount++;
			
			String excelBinName = "";
			
			row = (sheet.getRow(rowcount)==null)?sheet.createRow(rowcount): sheet.getRow(rowcount); // empty row.
			rowcount++;
		

			while (result.next()) {
				
				String recordBinName = result.getString("bin_name");
				
				if ( !excelBinName.equals(recordBinName)) {
					
					excelBinName = recordBinName;
					
					row = (sheet.getRow(rowcount)==null)?sheet.createRow(rowcount): sheet.getRow(rowcount); // empty row.
					rowcount++;
	
				
					row = (sheet.getRow(rowcount)==null)?sheet.createRow(rowcount): sheet.getRow(rowcount);
					row.createCell(cellIndex+0).setCellValue(excelBinName);
					rowcount++;

					row = (sheet.getRow(rowcount)==null)?sheet.createRow(rowcount): sheet.getRow(rowcount);
					row.createCell(cellIndex+0).setCellValue("Collection Start Date/Time");
					row.createCell(cellIndex+1).setCellValue(result.getString("bin_collection_start_time"));
					rowcount++;

					row = (sheet.getRow(rowcount)==null)?sheet.createRow(rowcount): sheet.getRow(rowcount);
					row.createCell(cellIndex+0).setCellValue("Collection End Date/Time");
					row.createCell(cellIndex+1).setCellValue(result.getString("bin_collection_end_time"));
					rowcount++;
					
					row = (sheet.getRow(rowcount)==null)?sheet.createRow(rowcount): sheet.getRow(rowcount);
					row.createCell(cellIndex+0).setCellValue("Statistic");
					row.createCell(cellIndex+1).setCellValue("Value");
					row.createCell(cellIndex+2).setCellValue("IDF");
					rowcount++;

				}
				
				row = (sheet.getRow(rowcount)==null)?sheet.createRow(rowcount): sheet.getRow(rowcount);
				row.createCell(cellIndex+0).setCellValue(result.getString("parameter_name"));
				row.createCell(cellIndex+1).setCellValue(result.getString("parameter_value"));
				row.createCell(cellIndex+2).setCellValue(result.getBoolean("parameter_idf"));
	            
	            rowcount++;
			} 
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return rowcount;
	}
	
	
	public void createWsCurrentAlarms(XSSFWorkbook wworkbook,String dateStr,String ipList) {
		try{
			XSSFSheet sheet = wworkbook.createSheet("Wave Server Current Alarms");
	        Row row;
	        Cell cell;	
	        int rowcount=0;
	        row = sheet.createRow(rowcount);
	        row.createCell(0).setCellValue("Node Name");;
	        row.createCell(1).setCellValue("Node IP");
	        row.createCell(2).setCellValue("Severity");
	        row.createCell(3).setCellValue("Description");
	        row.createCell(4).setCellValue("Alarm Date");
	        row.createCell(5).setCellValue("Instance");
	        rowcount++;
	        
	        
	        Connection connection = null;
			ResultSet result = null;
			Statement stmt = null;
	        
			DbConnection  dbObj = new DbConnection();
	    	connection = dbObj.getDBConnection();
			String query = "SELECT node_name, node_ip, alarm_instance_id, acknowledged, intermittent, alarm_table_id, severity, local_date_time, "
					+ "instance_name, description, site_identifier, group_identifier, member_identifier, collection_date, updated_time FROM optical_reports.fb_ws_alarms_current "
					+ "where collection_date='"+dateStr+"' and node_ip in ("+ipList+") order by node_ip";
			//			logger.info(query);
			logger.info("query is " + query);

			stmt = connection.createStatement();
			result = stmt.executeQuery(query);

			while (result.next()) {
				
				String nodeName = result.getString("node_name");
				String nodeIp = result.getString("node_ip");
				String severity = result.getString("severity");
				String description = result.getString("description");
				String alarmDate  = result.getString("local_date_time");
				String instanceName  = result.getString("instance_name");
				
			//	String portNumber[] = portId.split("-");
				
				
				row = sheet.createRow(rowcount);
	            cell = row.createCell(0);
	            cell.setCellValue(nodeName);
	            cell = row.createCell(1);
	            cell.setCellValue(nodeIp);
	            cell = row.createCell(2);
	            cell.setCellValue(severity);
	            cell = row.createCell(3);
	            cell.setCellValue(description);
	            cell = row.createCell(4);
	            cell.setCellValue(alarmDate);
	            cell = row.createCell(5);
	            cell.setCellValue(instanceName);
	            
	            rowcount++;

//	            if (rowcount+Integer.parseInt(portNumber[1]) == 10) rowcount=11;
			}
			
	        for (int i = 0; i < 6; i++) {
	            sheet.autoSizeColumn(i);
	        }

			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void create6500CurrentAlarms(XSSFWorkbook wworkbook,String dateStr, String ipList) {
		try{
			XSSFSheet sheet = wworkbook.createSheet("6500 Current Alarms");
	        Row row;
	        Cell cell;	
	        int rowcount=0;
	        row = sheet.createRow(rowcount);
	        row.createCell(0).setCellValue("Node Name");;
	        row.createCell(1).setCellValue("Node IP");
	        row.createCell(2).setCellValue("Severity");
	        row.createCell(3).setCellValue("Impact");
	        row.createCell(4).setCellValue("Component");
	        row.createCell(5).setCellValue("Raise Time");
	        row.createCell(6).setCellValue("Clear Time");
	        row.createCell(7).setCellValue("Description");
	        row.createCell(8).setCellValue("Acknowledged");
	        row.createCell(9).setCellValue("Acknowledged");
	        row.createCell(10).setCellValue("Additional Text");
	        row.createCell(11).setCellValue("Alarm Type");
	        row.createCell(12).setCellValue("Probable Cause");
	        row.createCell(13).setCellValue("Equipment Type");
	        row.createCell(14).setCellValue("Cleared By");
	        row.createCell(15).setCellValue("CLFI");
	        row.createCell(16).setCellValue("Suppression Flag");
	        rowcount++;
	        
	        
	        Connection connection = null;
			ResultSet result = null;
			Statement stmt = null;
	        
			DbConnection  dbObj = new DbConnection();
	    	connection = dbObj.getDBConnection();
			String query = "SELECT node_name, node_ip, instance_aid, aid_type, severity, condition_type, service_affecting, alarm_date_time, location, direction, "
					+ "condition_description, aid_detail, dgn_type, mode, collection_date, updated_time "
					+ "FROM optical_reports.fb_6500_alarms_current where collection_date='"+dateStr+"'  and node_ip in ("+ipList+") order by node_ip";

			//			logger.info(query);
			logger.info("query is " + query);

			stmt = connection.createStatement();
			result = stmt.executeQuery(query);

			while (result.next()) {
				
				String nodeName = result.getString("node_name");
				String nodeIp = result.getString("node_ip");
				String severity = result.getString("severity");
				String impact = result.getString("service_affecting");
				String component = result.getString("instance_aid");
				String raiseTime = result.getString("alarm_date_time");
				String clearTime = "";
				String description = result.getString("condition_type");
				String acknowledged = "";
				String annotation = "";
				String additionalText = result.getString("condition_description");;
				String alarmType  = result.getString("aid_type");
				String probableCause  = "";
				String equipmentType = "";
				String clearedBy = "";
				String clfi = "";
				String suppressionFlag = "";
				
				
				row = sheet.createRow(rowcount);
	            cell = row.createCell(0);
	            cell.setCellValue(nodeName);
	            cell = row.createCell(1);
	            cell.setCellValue(nodeIp);
	            cell = row.createCell(2);
	            cell.setCellValue(severity);
	            cell = row.createCell(3);
	            cell.setCellValue(impact);
	            cell = row.createCell(4);
	            cell.setCellValue(component);
	            cell = row.createCell(5);
	            cell.setCellValue(raiseTime);
	            cell = row.createCell(6);
	            cell.setCellValue(clearTime);
	            cell = row.createCell(7);
	            cell.setCellValue(description);
	            cell = row.createCell(8);
	            cell.setCellValue(acknowledged);
	            cell = row.createCell(9);
	            cell.setCellValue(annotation);
	            cell = row.createCell(10);
	            cell.setCellValue(additionalText);
	            cell = row.createCell(11);
	            cell.setCellValue(alarmType);
	            cell = row.createCell(12);
	            cell.setCellValue(probableCause);
	            cell = row.createCell(13);
	            cell.setCellValue(equipmentType);
	            cell = row.createCell(14);
	            cell.setCellValue(clearedBy);
	            cell = row.createCell(15);
	            cell.setCellValue(clfi);
	            cell = row.createCell(16);
	            cell.setCellValue(suppressionFlag);
	            
	            rowcount++;

//	            if (rowcount+Integer.parseInt(portNumber[1]) == 10) rowcount=11;
			}
			
	        for (int i = 0; i < 17; i++) {
	            sheet.autoSizeColumn(i);
	        }

			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void create6500HistoryAlarms(XSSFWorkbook wworkbook,String dateStr,String ip1,String ip2) {
		
		Calendar cal = Calendar.getInstance();

		Calendar startTime;
		startTime = (Calendar) cal.clone();
		startTime.add(Calendar.DAY_OF_MONTH, -1);
		startTime.set(Calendar.HOUR_OF_DAY, 0);
		startTime.set(Calendar.MINUTE, 0);
		startTime.set(Calendar.SECOND, 0);

		Calendar endTime;
		endTime = (Calendar) cal.clone();
		/*endTime.add(Calendar.DAY_OF_MONTH, -1);
		endTime.set(Calendar.HOUR_OF_DAY, 23);
		endTime.set(Calendar.MINUTE, 59);
		endTime.set(Calendar.SECOND, 59);*/
		
		Date startDate = startTime.getTime();
		Date endDate = endTime.getTime();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startDateStr = sdf.format(startDate);
		String endDateStr = sdf.format(endDate);
		
		int rowcount=0;
		
		System.out.println("Retrieving history alarms from ServiceNow between "+startDateStr+" and "+endDateStr);
		
		try{
			XSSFSheet sheet = wworkbook.createSheet("6500 History Alarms");
	        Row row;
	        Cell cell;	
	        //int rowcount=0;
	        row = sheet.createRow(rowcount);
	        row.createCell(0).setCellValue("Node Name");;
	        row.createCell(1).setCellValue("Node IP");
	        row.createCell(2).setCellValue("Severity");
	        row.createCell(3).setCellValue("Impact");
	        row.createCell(4).setCellValue("Component");
	        row.createCell(5).setCellValue("Raise Time");
	        row.createCell(6).setCellValue("Clear Time");
	        row.createCell(7).setCellValue("Description");
	        row.createCell(8).setCellValue("Acknowledged");
	        row.createCell(9).setCellValue("Acknowledged");
	        row.createCell(10).setCellValue("Additional Text");
	        row.createCell(11).setCellValue("Alarm Type");
	        row.createCell(12).setCellValue("Probable Cause");
	        row.createCell(13).setCellValue("Equipment Type");
	        row.createCell(14).setCellValue("Cleared By");
	        row.createCell(15).setCellValue("CLFI");
	        row.createCell(16).setCellValue("Suppression Flag");
	        rowcount++;
		
		rowcount = addRows6500HistoryAlarms(wworkbook,sheet,startDateStr,endDateStr,ip1,rowcount);
		addRows6500HistoryAlarms(wworkbook,sheet,startDateStr,endDateStr,ip2,rowcount+5);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
			
		
	}
	
	public int addRows6500HistoryAlarms(XSSFWorkbook wworkbook, XSSFSheet sheet, String startDateStr, String endDateStr,
			String ip, int rowcount) {

		Row row;
		Cell cell;

		String response = ServiceNowRestClient.getCiena6500AlarmsHistory(ip, startDateStr, endDateStr);

		if (response != null) {

			JSONObject responseJsonObj = new JSONObject(response);

			JSONObject jsonObj = responseJsonObj.getJSONObject("result");

			if (jsonObj != null && jsonObj.getString("Result").equals("Success")) {
				JSONArray alarmsList = jsonObj.getJSONObject("Data").getJSONArray("alarms");

				for (int i = 0; i < alarmsList.length(); i++) {
					JSONObject alarmObj = alarmsList.getJSONObject(i);
					//System.out.println(alarmObj.getString("atagSeq") + alarmObj.getString("conditionType")
						//	+ alarmObj.getString("severity"));
					String nodeName = nodeNameMap.get(ip);
					String nodeIp = alarmObj.getString("deviceip");
					String severity = alarmObj.getString("severity");
					String impact = alarmObj.getString("serviceAffected");
					String component = alarmObj.getString("aid");
					String raiseTime = alarmObj.getString("alarmDate");
					String clearTime = alarmObj.optString("clearedTime");
					String description = alarmObj.getString("conditionType");
					String acknowledged = "";
					String annotation = "";
					String additionalText = alarmObj.getString("conditionDescription");
					;
					String alarmType = alarmObj.getString("subType");
					String probableCause = "";
					String equipmentType = "";
					String clearedBy = "";
					String clfi = "";
					String suppressionFlag = "";

					row = sheet.createRow(rowcount);
					cell = row.createCell(0);
					cell.setCellValue(nodeName);
					cell = row.createCell(1);
					cell.setCellValue(nodeIp);
					cell = row.createCell(2);
					cell.setCellValue(severity);
					cell = row.createCell(3);
					cell.setCellValue(impact);
					cell = row.createCell(4);
					cell.setCellValue(component);
					cell = row.createCell(5);
					cell.setCellValue(raiseTime);
					cell = row.createCell(6);
					cell.setCellValue(clearTime);
					cell = row.createCell(7);
					cell.setCellValue(description);
					cell = row.createCell(8);
					cell.setCellValue(acknowledged);
					cell = row.createCell(9);
					cell.setCellValue(annotation);
					cell = row.createCell(10);
					cell.setCellValue(additionalText);
					cell = row.createCell(11);
					cell.setCellValue(alarmType);
					cell = row.createCell(12);
					cell.setCellValue(probableCause);
					cell = row.createCell(13);
					cell.setCellValue(equipmentType);
					cell = row.createCell(14);
					cell.setCellValue(clearedBy);
					cell = row.createCell(15);
					cell.setCellValue(clfi);
					cell = row.createCell(16);
					cell.setCellValue(suppressionFlag);

					rowcount++;

				}

				for (int i = 0; i < 17; i++) {
					sheet.autoSizeColumn(i);
				}

			} else {
				System.out.println("Unable to retrieve alarms from ServiceNow for "+ip);
			}

		}

		return rowcount;
	}

	
	public void createOPSStatus(XSSFWorkbook wworkbook,String dateStr,String ipList) {
		try{
			XSSFSheet sheet = wworkbook.createSheet("OPS Status");
	        Row row;
	        Cell cell;	
	        int rowcount=0;
	        row = sheet.createRow(rowcount);
	        row.createCell(0).setCellValue("Node Name");;
	        row.createCell(1).setCellValue("Node IP");
	        row.createCell(2).setCellValue("Facility");
	        row.createCell(3).setCellValue("Scheme");
	        row.createCell(4).setCellValue("MemberType");
	        row.createCell(5).setCellValue("Remote Standard");
	        row.createCell(6).setCellValue("Facility State");
	        
	        rowcount++;
	        
	        
	        Connection connection = null;
			ResultSet result = null;
			Statement stmt = null;
	        
			DbConnection  dbObj = new DbConnection();
	    	connection = dbObj.getDBConnection();
			String query = "SELECT node_name, node_ip, facility_id, scheme, memeber_type, remote_standard, facility_state, switch, reason, collection_date, updated_time "
					+ "FROM optical_reports.fb_6500_wps_status where collection_date='"+dateStr+"'  and node_ip in ("+ipList+")  order by node_ip";
			//			logger.info(query);
			logger.info("query is " + query);

			stmt = connection.createStatement();
			result = stmt.executeQuery(query);

			while (result.next()) {
				
				String nodeName = result.getString("node_name");
				String nodeIp = result.getString("node_ip");
				String facilityId = result.getString("facility_id");
				String scheme = result.getString("scheme");
				String memberType  = result.getString("memeber_type");
				String remoteStandard  = result.getString("remote_standard");
				String facilityState  = result.getString("facility_state");
				
			//	String portNumber[] = portId.split("-");
				
				
				row = sheet.createRow(rowcount);
	            cell = row.createCell(0);
	            cell.setCellValue(nodeName);
	            cell = row.createCell(1);
	            cell.setCellValue(nodeIp);
	            cell = row.createCell(2);
	            cell.setCellValue(facilityId);
	            cell = row.createCell(3);
	            cell.setCellValue(scheme);
	            cell = row.createCell(4);
	            cell.setCellValue(memberType);
	            cell = row.createCell(5);
	            cell.setCellValue(remoteStandard);
	            cell = row.createCell(6);
	            cell.setCellValue(facilityState);
	            
	            rowcount++;

			}
			
	        for (int i = 0; i < 7; i++) {
	            sheet.autoSizeColumn(i);
	        }

			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	public void initPolling() {
		
		logger.info("initPolling called");
		
		cienaClient = new CienaClient();
		cienaWsClient = new CienaWsClient();

		
	}
	
	public void finishInvnetory() {
		finishPolling();
	}

	
	public void finishPolling() {
		
		logger.info("finishPolling called");
		
		String location = "Chennai";
		
		String fileNamePrefix =   location + "_FB_Daily_Report_";
		
		String dateStr=generateReport(location,fileNamePrefix);
		
		ServiceNowRestClient.postFbReportToServiceNow(dateStr,fileNamePrefix);
		
		
		location = "Mumbai";
		  
		fileNamePrefix = location + "_FB_Daily_Report_";
		  
		dateStr=generateReport("Mumbai",fileNamePrefix);
		  
		ServiceNowRestClient.postFbReportToServiceNow(dateStr, fileNamePrefix);
		 		
		
		cienaClient = null;
		cienaWsClient = null;
		
		
		logger.info("finishPolling completed");
		
	}


}
