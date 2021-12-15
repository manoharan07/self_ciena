package com.sify.network.assets.ciena;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.ws.WebServiceException;

import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sify.network.alarms.ciena.Notification;
import com.sify.network.assets.core.api.AssetsAbstractService;
import com.sify.network.assets.core.api.AssetsCoreRuntime;
import com.sify.network.assets.core.api.bean.DBDetails;
import com.sify.network.assets.core.api.bean.InventoryDeviceDetails;
import com.sify.network.assets.core.api.bean.JobType;
import com.sify.network.assets.core.api.bean.PerformanceInputBean;
import com.sify.network.assets.core.api.bean.PerformanceOutputBean;

public class OpticalReportAlarmHealthCheckup extends AssetsAbstractService {

	static Timestamp polled_date = null;
	static Calendar startTime=null;
	static Calendar endTime = null;
	
	static ArrayList<Notification> currentAlarmsList = new ArrayList<Notification>();

	private CienaClient cienaClient = null;
	// private CienaWsClient cienaWsClient = null;

	// private static final String COMMA_DELIMITER = ",";

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("alarmhealthcheckup");

	private DbConnection dbObj = null;

	private HashMap<String, String> deviceMapHuawei = new HashMap<String, String>();
	private HashMap<String, String> deviceMapCiena = new HashMap<String, String>();

	private long MTTR_TIME = 4L * 60 * 60 * 1000;

	// int summaryRowCount = 0;

	private HashMap<String, String> spanLossMap = new HashMap<String, String>();

	public OpticalReportAlarmHealthCheckup() {
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
			String query = "SELECT node_ip FROM ciena.devices_list_manual";
			// System.out.println(query);
			logger.info("query is " + query);

			stmt = connection.createStatement();
			result = stmt.executeQuery(query);
			
			while (result.next()) {
				PerformanceInputBean bean = new PerformanceInputBean();
				String nodeIp = result.getString("node_ip");
				bean.setDeviceip(nodeIp);
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

		OpticalReportAlarmHealthCheckup service = new OpticalReportAlarmHealthCheckup();
		service.setServiceName("OpticalReportAlarmHealthCheckup");

		DBDetails dbInventory = new DBDetails(CienaProperties.get("db.mysql.ip"), CienaProperties.get("db.mysql.port"),
				CienaProperties.get("db.mysql.username"), CienaProperties.get("db.mysql.password"),
				CienaProperties.get("db.mysql.dbname"));

		service.getDbDetailsMap().put(JobType.INVENTORY, dbInventory);

		service.setInventoryFromDevice(true);
		service.setInventoryThreadTimeout(120);
		service.setLivePollThreadCound(80);

		logger.info("OpticalReportAlarmHealthCheckup  details added");

		// providing the cron string for the jobs
		service.setAggregateCron("");
		service.setPollCron("");
		service.setInventoryCron(CienaProperties.get("optical.alarm.health.checkup.report.cron"));

		AssetsCoreRuntime runtime = new AssetsCoreRuntime();
		runtime.register(service);
		logger.info("OpticalReportAlarmHealthCheckup registered with core");

	}

	public static void main(String[] args) {
		
		/*HuwaeiU2000WebServicesImpleService soapclient = new HuwaeiU2000WebServicesImpleService();
		HuwaeiU2000WebServices soapclientservices = soapclient.getHuwaeiU2000WebServicesImplePort();
		String json = soapclientservices.getAllEmsActiveAlarms("ALL");
		System.out.println("SOAP Response : "+json);*/
		
		PropertyConfigurator.configure("config" + File.separator + "log4j_alarmhealthcheckup.properties");
		OpticalReportAlarmHealthCheckup service = new OpticalReportAlarmHealthCheckup();
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



	public String generateReport() {

		String dateStr = "";

		try {

			// String dateStr = "2020-07-05";

			Calendar cal = Calendar.getInstance();

			Calendar startTime;
			startTime = (Calendar) cal.clone();
			// startTime.add(Calendar.MONTH, -1);
			startTime.add(Calendar.DAY_OF_MONTH, 1);
			startTime.set(Calendar.HOUR_OF_DAY, 0);
			startTime.set(Calendar.MINUTE, 0);
			startTime.set(Calendar.SECOND, 0);

			Calendar endTime;
			endTime = (Calendar) cal.clone();
			/*
			 * endTime.add(Calendar.DAY_OF_MONTH, -1);
			 * endTime.set(Calendar.HOUR_OF_DAY, 23);
			 * endTime.set(Calendar.MINUTE, 59); endTime.set(Calendar.SECOND,
			 * 59);
			 */

			Date startDate = startTime.getTime();
			Date endDate = endTime.getTime();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String startDateStr = sdf.format(startDate);
			String endDateStr = sdf.format(endDate);

			DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			logger.info(f.format(polled_date));

			dateStr = f.format(polled_date);

			logger.info("Report generation started");

		//	String roadmNameForFile = roadmName.replaceAll(" ", "_");

			String filePath = CienaProperties.get("optical.network.availability.report.folder");
			String excelPath = filePath + File.separator + "AlarmHealthCheckupReport_" + dateStr
					+ ".xlsx";

			FileOutputStream fileOut = new FileOutputStream(excelPath);
			XSSFWorkbook workbook = createWorkBook();

			generateSheets(workbook, startDateStr, endDateStr);

			workbook.write(fileOut);
			fileOut.flush();
			fileOut.close();
			logger.info("Report generation completed");
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		return dateStr;
	}

	private XSSFWorkbook createWorkBook() throws Exception {
		return new XSSFWorkbook();
	}

	private void generateSheets(XSSFWorkbook workbook, String startDate, String endDate) {

		int summaryRowCount = 3;

		
		DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		logger.info(f.format(polled_date));

		String dateStr = f.format(polled_date);

		XSSFSheet summarySheet = workbook.createSheet("Summary");

		Row row;
		row = summarySheet.createRow(summaryRowCount);
		row.createCell(0).setCellValue("Vendor");
		row.createCell(1).setCellValue("Active Alarms");
		row.createCell(2).setCellValue("Critical");
		row.createCell(3).setCellValue("Major");
		row.createCell(4).setCellValue("Minor");
		row.createCell(5).setCellValue("Warning");
		summaryRowCount++;

		createSheet6500Alarms(workbook, dateStr,summaryRowCount);
		
		summaryRowCount++;
		
		createSheetHuaweiAlarms(workbook, dateStr,summaryRowCount);

		for (int i = 0; i < 15; i++) {
			summarySheet.autoSizeColumn(i);
		}


	}

	public HashMap<String, String> loadDeviceMapHuawei() {

		HashMap<String, String> deviceMap = new HashMap<String, String>();

		Connection connection = null;
		ResultSet result = null;
		Statement stmt = null;

		try {
			connection = dbObj.getDBConnectionHuawei();
			String query = "SELECT device_id, name, location, owner, productname, createddate, lastupdatedate FROM HuwaeiU2000.elements";
			// logger.info(query);
			logger.info("query is " + query);

			stmt = connection.createStatement();
			result = stmt.executeQuery(query);

			while (result.next()) {

				String elementId = result.getString("device_id");
				String nodeName = result.getString("name").trim();
				deviceMap.put(elementId, nodeName);
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
	
	public HashMap<String, String> loadDeviceMapCiena() {

		HashMap<String, String> deviceMap = new HashMap<String, String>();

		Connection connection = null;
		ResultSet result = null;
		Statement stmt = null;

		try {
			connection = dbObj.getDBConnectionHuawei();
			String query = "SELECT device_ip, node_name, shelf FROM ciena.devices";
			// logger.info(query);
			logger.info("query is " + query);

			stmt = connection.createStatement();
			result = stmt.executeQuery(query);

			while (result.next()) {

				String deviceip = result.getString("device_ip");
				String nodeName = result.getString("node_name").trim();
			    String shelf = result.getString("shelf");
			    
			    deviceMapCiena.put(deviceip, nodeName+":"+shelf);
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


	
	public void createSheet6500Alarms(XSSFWorkbook wworkbook,String dateStr,int summaryRowCount) {
		
		Date startDate = startTime.getTime();
		Date endDate = endTime.getTime();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startDateStr = sdf.format(startDate);
		String endDateStr = sdf.format(endDate);
		
		int rowcount=0;
		
		System.out.println("Retrieving Ciena alarms from ServiceNow between "+startDateStr+" and "+endDateStr);
		
		try{
			XSSFSheet sheet = wworkbook.createSheet("Ciena");
	        Row row;
	        Cell cell;	
	        //int rowcount=0;
	 	
		rowcount = addRows6500CurrentAlarms(wworkbook,sheet,startDateStr,endDateStr,rowcount,summaryRowCount);
		rowcount = addRows6500HistoryAlarms(wworkbook,sheet,startDateStr,endDateStr,rowcount+5);
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		
			
		
	}
	
	public int addRows6500CurrentAlarms(XSSFWorkbook workbook, XSSFSheet sheet, String startDateStr, String endDateStr, int rowcount,int summaryRowCount) {
		
		int criticalCount=0;
		int majorCount=0;
		int minorCount=0;
		
		int activeAlarmsCount=0;
		
		
		

		Row row;
		Cell cell;
		
		row = sheet.createRow(rowcount);
		row.createCell(3).setCellValue("Active Alarms "+startDateStr);
		rowcount++; rowcount++;
		
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
	
	        activeAlarmsCount=currentAlarmsList.size();
	        
				for (Notification alarm:currentAlarmsList) {
					
					//System.out.println(alarmObj.getString("atagSeq") + alarmObj.getString("conditionType")
						//	+ alarmObj.getString("severity"));
					
					String nodeIp = alarm.getDeviceip();
					String nodeName = deviceMapCiena.get(nodeIp);
					String severity = alarm.getNtfcncde();
					
					
					if (severity.equals("CR")) {
						criticalCount++;
					} else if (severity.equals("MJ")) {
						majorCount++;
					} else if (severity.equals("MN")) {
						minorCount++;
					}
						
					
					String impact =alarm.getServiceAffected();
					String component = alarm.getAid();
					String raiseTime = alarm.getDate();
					String clearTime = "";
					String description = alarm.getCondtype();
					String acknowledged = "";
					String annotation = "";
					String additionalText = alarm.getConddescr();
					;
					String alarmType = alarm.getSubType();
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
				
				XSSFSheet summarySheet = workbook.getSheet("Summary");
				row = summarySheet.createRow(summaryRowCount);
				row.createCell(0).setCellValue("Ciena");
				row.createCell(1).setCellValue(activeAlarmsCount);
				row.createCell(2).setCellValue(criticalCount);
				row.createCell(3).setCellValue(majorCount);
				row.createCell(4).setCellValue(minorCount);
				


		return rowcount;
	}

	
	public int addRows6500HistoryAlarms(XSSFWorkbook wworkbook, XSSFSheet sheet, String startDateStr, String endDateStr, int rowcount) {

		Row row;
		Cell cell;
		
		row = sheet.createRow(rowcount);
		row.createCell(3).setCellValue("History Alarms "+startDateStr);
		rowcount++; rowcount++;
		
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
	

		String response = ServiceNowRestClient.getCiena6500AlarmsHistory("ALL", startDateStr, endDateStr);

		if (response != null) {

			JSONObject responseJsonObj = new JSONObject(response);

			JSONObject jsonObj = responseJsonObj.getJSONObject("result");

			if (jsonObj != null && jsonObj.getString("Result").equals("Success")) {
				JSONArray alarmsList = jsonObj.getJSONObject("Data").getJSONArray("alarms");

				JSONObject alarmObj = null;
				
				System.out.println("Ciena history alarms count : "+alarmsList.length());
				
				for (int i = 0; i < alarmsList.length(); i++) {
					alarmObj = alarmsList.getJSONObject(i);
					//System.out.println(alarmObj.getString("atagSeq") + alarmObj.getString("conditionType")
						//	+ alarmObj.getString("severity"));
					
					String nodeIp = alarmObj.getString("deviceip");
					String nodeName = deviceMapCiena.get(nodeIp);
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
				System.out.println("Unable to retrieve alarms from ServiceNow for "+".");
			}

		}

		return rowcount;
	}
	
	public void createSheetHuaweiAlarms(XSSFWorkbook wworkbook,String dateStr,int summaryRowCount) {
		
		
		Date startDate = startTime.getTime();
		Date endDate = endTime.getTime();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startDateStr = sdf.format(startDate);
		String endDateStr = sdf.format(endDate);
		
		int rowcount=0;
		
	//	System.out.println("Retrieving Huawei history alarms from ServiceNow between "+startDateStr+" and "+endDateStr);
		
		try{
			XSSFSheet sheet = wworkbook.createSheet("Huawei");
	        Row row;
	        Cell cell;	
	        //int rowcount=0;
	 
	        rowcount++;
		
	    rowcount = addRowsHuaweiCurrentAlarms(wworkbook,sheet,startDateStr,endDateStr,rowcount,summaryRowCount);
		rowcount = addRowsHuaweiHistoryAlarms(wworkbook,sheet,startDateStr,endDateStr,rowcount+5);
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		
			
		System.out.println("Huawei history alarms completed");
		
	}
	
	public int addRowsHuaweiCurrentAlarms(XSSFWorkbook workbook, XSSFSheet sheet, String startDateStr, String endDateStr, int rowcount,int summaryRowCount) {

		Row row;
		Cell cell;
		
		int criticalCount=0;
		int majorCount=0;
		int minorCount=0;
		int warningCount=0;
		int activeAlarmsCount=0;
		
		row = sheet.createRow(rowcount);
		row.createCell(3).setCellValue("Active Alarms "+startDateStr);
		rowcount++; rowcount++;
		
		row = sheet.createRow(rowcount);
		
		row.createCell(0).setCellValue("Severity");
		row.createCell(1).setCellValue("Alarm Name");
		row.createCell(2).setCellValue("Alarm Source");
		row.createCell(3).setCellValue("Location Info");
		row.createCell(4).setCellValue("Occurred Time");
		row.createCell(5).setCellValue("Cleared Time");
		row.createCell(6).setCellValue("Alarm SerialNo");
		row.createCell(7).setCellValue("Node Name");
		rowcount++;

		URL url=null;
		
		try {
			url = new URL(CienaProperties.get("huawei.u2000.webservice.url"));
		}  catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
		HuwaeiU2000WebServicesImpleService soapclient = new HuwaeiU2000WebServicesImpleService(url);
		HuwaeiU2000WebServices soapclientservices = soapclient.getHuwaeiU2000WebServicesImplePort();
		String response = soapclientservices.getAllEmsActiveAlarms("ALL");
		System.out.println("SOAP Response : "+response);
		
		
		if (response != null) {

			JSONArray alarmsList = new JSONArray(response);

			JSONObject alarmObj=null;
			
			System.out.println("Huawei Current alarms count : "+alarmsList.length());
			
			activeAlarmsCount=alarmsList.length();

			for (int i = 0; i < alarmsList.length(); i++) {
				alarmObj = alarmsList.getJSONObject(i);

				// "nativeEmsName":"Huawei/U2000;
				// MH-MUM-NTG-B1-NM;/rack=1/shelf=MUMNTG-MCD-R-U9PS-01/sub_shelf=1/slot=5/domain=wdm/port=1"
				String nativeEmsName = alarmObj.getString("nativeEMSName");
				String shelfName="";
				try {
				String nameStr[] = nativeEmsName.split(";");
				String portStr[] = nameStr[2].split("/");
				shelfName = portStr[2];

				//System.out.println("ShelfName : " + shelfName);

				shelfName = shelfName.replace("shelf", nameStr[1]);
				shelfName = shelfName.replace("=", " / ");
				} catch(Exception e) {
					System.out.println("Error parsing nativeEMSName : "+alarmObj.getString("nativeEMSName"));
					//e.printStackTrace();
				}

				String alarmSource = shelfName;
				
				String elementId = alarmObj.optString("element_id");
				String nodeName = deviceMapHuawei.get(elementId);
				String severity = alarmObj.getString("severity");
				
				
				if (severity.equals("PS_CRITICAL")) {
					criticalCount++;
				} else if (severity.equals("PS_MAJOR")) {
					majorCount++;
				} else if (severity.equals("PS_MINOR")) {
					minorCount++;
				} else if (severity.equals("PS_WARNING")) {
					warningCount++;
				}
					
				
				String alarmName = alarmObj.getString("nativeProbableCause");
				String locationInfo = alarmObj.getString("locationInfo");
				String alarmPort = alarmObj.optString("rack");
				String raiseTime = alarmObj.getString("neTime");
				String clearTime = "";
				String alarmSerialNumber = alarmObj.optString("alarmSerialNo");

				row = sheet.createRow(rowcount);
				cell = row.createCell(0);
				cell.setCellValue(severity);
				cell = row.createCell(1);
				cell.setCellValue(alarmName);
				cell = row.createCell(2);
				cell.setCellValue(alarmSource);
				cell = row.createCell(3);
				cell.setCellValue(locationInfo);
				cell = row.createCell(4);
				cell.setCellValue(raiseTime);
				cell = row.createCell(5);
				cell.setCellValue(clearTime);
				
				cell = row.createCell(6);
				cell.setCellValue(alarmSerialNumber);
				
				cell = row.createCell(7);
				cell.setCellValue(nodeName);


				rowcount++;

			}

				for (int i = 0; i < 17; i++) {
					sheet.autoSizeColumn(i);
				}
				
				XSSFSheet summarySheet = workbook.getSheet("Summary");
				row = summarySheet.createRow(summaryRowCount);
				row.createCell(0).setCellValue("Huawei");
				row.createCell(1).setCellValue(activeAlarmsCount);
				row.createCell(2).setCellValue(criticalCount);
				row.createCell(3).setCellValue(majorCount);
				row.createCell(4).setCellValue(minorCount);
				row.createCell(5).setCellValue(warningCount);
		}

		return rowcount;
	}

	
	public int addRowsHuaweiHistoryAlarms(XSSFWorkbook wworkbook, XSSFSheet sheet, String startDateStr, String endDateStr, int rowcount) {

		Row row;
		Cell cell;
		
		row = sheet.createRow(rowcount);
		row.createCell(3).setCellValue("History Alarms "+startDateStr);
		rowcount++; rowcount++;
		
		row = sheet.createRow(rowcount);
		row.createCell(0).setCellValue("Severity");
		row.createCell(1).setCellValue("Alarm Name");
		row.createCell(2).setCellValue("Alarm Source");
		row.createCell(3).setCellValue("Location Info");
		row.createCell(4).setCellValue("Occurred Time");
		row.createCell(5).setCellValue("Cleared Time");
		row.createCell(6).setCellValue("Alarm SerialNo");
		row.createCell(7).setCellValue("Node Name");
		rowcount++;

		String response = ServiceNowRestClient.getHuaweiAlarms("ALL", "", startDateStr, endDateStr);

		if (response != null) {

			JSONObject responseJsonObj = new JSONObject(response);

			JSONObject jsonObj = responseJsonObj.getJSONObject("result");
			
			JSONObject alarmObj=null;

			if (jsonObj != null && jsonObj.getString("Result").equals("Success")) {
				JSONArray alarmsList = jsonObj.getJSONObject("Data").getJSONArray("alarms");
				
				System.out.println("Huawei history alarms count : "+alarmsList.length());

				for (int i = 0; i < alarmsList.length(); i++) {
					alarmObj = alarmsList.getJSONObject(i);

					// "nativeEmsName":"Huawei/U2000;
					// MH-MUM-NTG-B1-NM;/rack=1/shelf=MUMNTG-MCD-R-U9PS-01/sub_shelf=1/slot=5/domain=wdm/port=1"
					String nativeEmsName = alarmObj.getString("nativeEmsName");
					String shelfName="";
					try {
					String nameStr[] = nativeEmsName.split(";");
					String portStr[] = nameStr[2].split("/");
					shelfName = portStr[2];

					//System.out.println("ShelfName : " + shelfName);

					shelfName = shelfName.replace("shelf", nameStr[1]);
					shelfName = shelfName.replace("=", " / ");
					} catch(Exception e) {
						e.printStackTrace();
					}

					String alarmSource = shelfName;
					String elementId = alarmObj.getString("deviceid");
					String nodeName = deviceMapHuawei.get(elementId);
					String severity = alarmObj.getString("severity");
					String alarmName = alarmObj.getString("nativeProbableCause");
					String locationInfo = alarmObj.getString("locationInfo");
					String alarmPort = alarmObj.getString("port");
					String raiseTime = alarmObj.getString("alarmDate");
					String clearTime = alarmObj.optString("clearedTime");
					String alarmSerialNumber = alarmObj.getString("alarmSerialNumber");

					row = sheet.createRow(rowcount);
					cell = row.createCell(0);
					cell.setCellValue(severity);
					cell = row.createCell(1);
					cell.setCellValue(alarmName);
					cell = row.createCell(2);
					cell.setCellValue(alarmSource);
					cell = row.createCell(3);
					cell.setCellValue(locationInfo);
					cell = row.createCell(4);
					cell.setCellValue(raiseTime);
					cell = row.createCell(5);
					cell.setCellValue(clearTime);
					
					cell = row.createCell(6);
					cell.setCellValue(alarmSerialNumber);
					
					cell = row.createCell(7);
					cell.setCellValue(nodeName);



					rowcount++;

				}


				for (int i = 0; i < 15; i++) {
					sheet.autoSizeColumn(i);
				}

			} else {
				System.out.println("Unable to retrieve Huawei alarms from ServiceNow");
			}

		}
		return rowcount;
	}



	public AvailabilityStats create6500HistoryAlarmsCiena(XSSFWorkbook wworkbook, String dateStr,
			CienaFiberLinkBean fiberLink) {

		AvailabilityStats stats = null;


		Date startDate = startTime.getTime();
		Date endDate = endTime.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startDateStr = sdf.format(startDate);
		String endDateStr = sdf.format(endDate);

		SimpleDateFormat sdfMonth = new SimpleDateFormat("MMM-yyyy");
		String monthStr = sdf.format(startDate);

		double totalMsOfDuration = endDate.getTime() - startDate.getTime();
		long durationInMs = endDate.getTime() - startDate.getTime();

		long durationInDays = TimeUnit.DAYS.convert(durationInMs, TimeUnit.MILLISECONDS);
		long durationInMinutes = TimeUnit.MINUTES.convert(durationInMs, TimeUnit.MILLISECONDS);
		
		XSSFSheet summarySheet = wworkbook.getSheet("Summary");
		
		Row row = summarySheet.createRow(0);
		row.createCell(0).setCellValue("Start Date");
		row.createCell(1).setCellValue(startDateStr);
		row.createCell(2).setCellValue("End Date");
		row.createCell(3).setCellValue(endDateStr);

		int rowcount = 11;

		System.out.println("Retrieving history alarms from ServiceNow between " + startDateStr + " and " + endDateStr);

		try {
			XSSFSheet sheet = wworkbook.createSheet(fiberLink.getLinkName());

			AvailabilityStats aEndStats = new AvailabilityStats();
			AvailabilityStats zEndStats = new AvailabilityStats();

			rowcount = addRows6500HistoryAlarmsCiena(aEndStats, wworkbook, sheet, startDateStr, endDateStr,
					fiberLink.aEndNodeIp, fiberLink.aEndNodeName, fiberLink.aEndPort, rowcount);
			addRows6500HistoryAlarmsCiena(zEndStats, wworkbook, sheet, startDateStr, endDateStr, fiberLink.zEndNodeIp,
					fiberLink.zEndNodeName, fiberLink.zEndPort, rowcount + 5);

			// AvailabilityStats stats = null;
			if (aEndStats.outageMsTotal > zEndStats.outageMsTotal) {
				stats = aEndStats;
			} else {
				stats = zEndStats;
			}

			rowcount = 0;
			//Row row;

			row = sheet.createRow(rowcount);
			row.createCell(0).setCellValue(fiberLink.linkName);
			;
			row.createCell(4).setCellValue("Month");
			row.createCell(5).setCellValue(monthStr);
			rowcount++;

			row = sheet.createRow(rowcount);
			row.createCell(4).setCellValue("Outage in HH:MM:SS");
			row.createCell(5).setCellValue(stats.outageMsTotalStr);
			rowcount++;

			row = sheet.createRow(rowcount);
			row.createCell(4).setCellValue("MTTR Slippage");
			row.createCell(5).setCellValue(stats.mttrSlippage);
			rowcount++;

			row = sheet.createRow(rowcount);
			row.createCell(4).setCellValue("No.of Outages");
			row.createCell(5).setCellValue(stats.outages);
			rowcount++;

			row = sheet.createRow(rowcount);
			row.createCell(4).setCellValue("No.of FiberCuts");
			row.createCell(5).setCellValue(stats.fiberCuts);
			rowcount++;

			row = sheet.createRow(rowcount);
			row.createCell(4).setCellValue("No. of Fluctuations");
			row.createCell(5).setCellValue(stats.fluctuations);

			rowcount++;

			for (int i = 0; i < 15; i++) {
				sheet.autoSizeColumn(i);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		double outagePercentage = 100 - (stats.outageMsTotal / totalMsOfDuration * 100);
		stats.outagePercentage = String.format("%.2f", outagePercentage);

		stats.durationInMinutes = durationInMinutes;
		stats.durationInDays = durationInDays;

		return stats;

	}

	public int addRows6500HistoryAlarmsCiena(AvailabilityStats stats, XSSFWorkbook wworkbook, XSSFSheet sheet,
			String startDateStr, String endDateStr, String ip, String nodeName, String port, int rowcount) {

		long totalMsPerIp = 0;
		int rowCountStart = rowcount;
		
		String shelfslotport[] = port.split("-");

		String optmonPort = "OPTMON-" + shelfslotport[1] + "-" + shelfslotport[2] + "-" + 8;
		String esamSlot = "ESAM-" + shelfslotport[1] + "-" + shelfslotport[2];


		Row row;
		Cell cell;

		row = sheet.createRow(rowcount);
		cell = row.createCell(0);
		cell.setCellValue(nodeName);
		cell = row.createCell(1);
		cell.setCellValue(ip);
		cell = row.createCell(2);
		cell.setCellValue(optmonPort);
		rowcount++;

		// int rowcount=0;
		row = sheet.createRow(rowcount);
		row.createCell(0).setCellValue("Node Name");
		;
		row.createCell(1).setCellValue("Node IP");
		row.createCell(2).setCellValue("Severity");
		row.createCell(3).setCellValue("Alarm Name");
		row.createCell(4).setCellValue("Alarm Source");
		row.createCell(5).setCellValue("Occurred Time");
		row.createCell(6).setCellValue("Cleared Time");
		row.createCell(7).setCellValue("Alarm Duration");
		row.createCell(8).setCellValue("Outage Duration");
		rowcount++;


		String response = ServiceNowRestClient.getCiena6500AlarmsForAvailability(ip, optmonPort, esamSlot, startDateStr,
				endDateStr);

		if (response != null) {

			JSONObject responseJsonObj = new JSONObject(response);

			JSONObject jsonObj = responseJsonObj.getJSONObject("result");

			if (jsonObj != null && jsonObj.getString("Result").equals("Success")) {
				JSONArray alarmsList = jsonObj.getJSONObject("Data").getJSONArray("alarms");

				for (int i = 0; i < alarmsList.length(); i++) {
					JSONObject alarmObj = alarmsList.getJSONObject(i);

					String nodeIp = alarmObj.getString("deviceip");
					String severity = alarmObj.getString("severity");
					String alarmName = alarmObj.getString("conditionDescription");
					String alarmSource = alarmObj.getString("aid");
					String raiseTime = alarmObj.getString("alarmDate");
					String clearTime = alarmObj.optString("clearedTime");

					row = sheet.createRow(rowcount);
					cell = row.createCell(0);
					cell.setCellValue(nodeName);
					cell = row.createCell(1);
					cell.setCellValue(nodeIp);
					cell = row.createCell(2);
					cell.setCellValue(severity);
					cell = row.createCell(3);
					cell.setCellValue(alarmName);
					cell = row.createCell(4);
					cell.setCellValue(alarmSource);
					cell = row.createCell(5);
					cell.setCellValue(raiseTime);
					cell = row.createCell(6);
					cell.setCellValue(clearTime);

					Date startTime = null;
					Date endTime = null;
					String hms = "";
					Long durationMs = null;

					try {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						startTime = sdf.parse(raiseTime);
						endTime = sdf.parse(clearTime);
						durationMs = endTime.getTime() - startTime.getTime();

						hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(durationMs),
								TimeUnit.MILLISECONDS.toMinutes(durationMs) % TimeUnit.HOURS.toMinutes(1),
								TimeUnit.MILLISECONDS.toSeconds(durationMs) % TimeUnit.MINUTES.toSeconds(1));

						if (durationMs <= 5000) {
							// less than 5 secs => flucuation
							stats.fluctuations++;
						} else if (durationMs > 5000) {
							// greater than 5 secs == > fiber cut
							stats.fiberCuts++;
						}

						if (durationMs >= MTTR_TIME) {
							stats.mttrSlippage++;
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

					totalMsPerIp += durationMs;

					String alarmDuration = hms;

					cell = row.createCell(7);
					cell.setCellValue(alarmDuration);
					cell = row.createCell(8);
					cell.setCellValue("");

					rowcount++;

				}

				stats.outageMsTotal = totalMsPerIp;
				stats.outages = alarmsList.length();

				String hmsOutage = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(totalMsPerIp),
						TimeUnit.MILLISECONDS.toMinutes(totalMsPerIp) % TimeUnit.HOURS.toMinutes(1),
						TimeUnit.MILLISECONDS.toSeconds(totalMsPerIp) % TimeUnit.MINUTES.toSeconds(1));

				stats.outageMsTotalStr = hmsOutage;

				// sheet.addMergedRegion(new
				// CellRangeAddress(rowCountStart,rowcount-1,8,8));
				// sheet.getRow(rowCountStart).getCell(8).setCellValue(hmsOutage);

				if (alarmsList.length() > 0) {
					// sheet.addMergedRegion(new
					// CellRangeAddress(rowCountStart,rowcount-1,8,8));

					cell = sheet.getRow(rowCountStart+2).getCell(8) != null ? sheet.getRow(rowCountStart+2).createCell(8)
							: sheet.getRow(rowCountStart+2).getCell(8);
					cell.setCellValue(hmsOutage);
				}

				
				for (int i = 0; i < 15; i++) { sheet.autoSizeColumn(i); }
				

			} else {
				System.out.println("Unable to retrieve alarms from ServiceNow for " + ip);
			}

		}

		return rowcount;
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
		
		System.out.println("6500 alarms for ip "+ip+" : "+jsonArrayAlarms.length());
		
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
			//String additionalInfo = jsonObj.optString("additional-info");
			
			Notification alarm = new Notification();
			
			alarm.setNtfcncde(severity);
			alarm.setConddescr(conditionDescription);
			alarm.setCondtype(conditionType);
			alarm.setAid(instanceAid);
			alarm.setDeviceip(ip);
			alarm.setServiceAffected(serviceAffecting);
			alarm.setDate(alarmDateStr);
			
			
			currentAlarmsList.add(alarm);
			
			
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


	public AvailabilityStats createHistoryAlarmsHuawei(XSSFWorkbook wworkbook, String dateStr,
			HuaweiFiberLinkBean fiberLink) {

		AvailabilityStats stats = null;

/*		Calendar cal = Calendar.getInstance();
		
		Calendar startTime = null;
		Calendar endTime = null;
		
		if (CienaProperties.get("optical.network.availability.report.duration").equals("LAST_WEEK")) {

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

		} else if (CienaProperties.get("optical.network.availability.report.duration").equals("LAST_MONTH")) {

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

			endTime = (Calendar) cal.clone();
			//endTime.set(Calendar.MONTH, -1);
			//endTime.set(Calendar.DAY_OF_MONTH, endTime.getActualMaximum(Calendar.DAY_OF_MONTH));
			endTime.add(Calendar.DAY_OF_MONTH,-1);
		//	endTime.set(Calendar.MONTH, startTime.get(Calendar.MONTH)); // logically not needed but the previous line sets month to preivous month.need to debug.
			endTime.set(Calendar.HOUR_OF_DAY, 23);
			endTime.set(Calendar.MINUTE, 59);
			endTime.set(Calendar.SECOND, 59);

		}
*/
		Date startDate = startTime.getTime();
		Date endDate = endTime.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startDateStr = sdf.format(startDate);
		String endDateStr = sdf.format(endDate);

		SimpleDateFormat sdfMonth = new SimpleDateFormat("MMM-yyyy");
		String monthStr = sdf.format(startDate);
		
		double totalMsOfDuration = endDate.getTime() - startDate.getTime();
		long durationInMs = endDate.getTime() - startDate.getTime();

		long durationInDays = TimeUnit.DAYS.convert(durationInMs, TimeUnit.MILLISECONDS);
		long durationInMinutes = TimeUnit.MINUTES.convert(durationInMs, TimeUnit.MILLISECONDS);
		
		
		XSSFSheet summarySheet = wworkbook.getSheet("Summary");
		
		Row row = summarySheet.createRow(0);
		row.createCell(0).setCellValue("Start Date");
		row.createCell(1).setCellValue(startDateStr);
		row.createCell(2).setCellValue("End Date");
		row.createCell(3).setCellValue(endDateStr);
		


		int rowcount = 11;

		System.out.println("Retrieving history alarms from ServiceNow between " + startDateStr + " and " + endDateStr);

		try {
			XSSFSheet sheet = wworkbook.createSheet(fiberLink.getLinkName());

			getTopologicalLinkPorts(fiberLink);

			AvailabilityStats aEndStats = new AvailabilityStats();
			AvailabilityStats zEndStats = new AvailabilityStats();

			rowcount = addRowsHistoryAlarmsHuawei(aEndStats, wworkbook, sheet, startDateStr, endDateStr, fiberLink.aEndSrcNodeName,
					fiberLink.aEndTopologyDeviceId, fiberLink.aEndTopologyPort, rowcount);
			addRowsHistoryAlarmsHuawei(zEndStats, wworkbook, sheet, startDateStr, endDateStr, fiberLink.zEndSrcNodeName,
					fiberLink.zEndTopologyDeviceId, fiberLink.zEndTopologyPort, rowcount + 5);

			// AvailabilityStats stats = null;
			if (aEndStats.outageMsTotal > zEndStats.outageMsTotal) {
				stats = aEndStats;
			} else {
				stats = zEndStats;
			}

			rowcount = 0;
			//Row row;

			row = sheet.createRow(rowcount);
			row.createCell(0).setCellValue(fiberLink.linkName);
			;
			row.createCell(4).setCellValue("Month");
			row.createCell(5).setCellValue(monthStr);
			rowcount++;

			row = sheet.createRow(rowcount);
			row.createCell(4).setCellValue("Outage in HH:MM:SS");
			row.createCell(5).setCellValue(stats.outageMsTotalStr);
			rowcount++;

			row = sheet.createRow(rowcount);
			row.createCell(4).setCellValue("MTTR Slippage");
			row.createCell(5).setCellValue(stats.mttrSlippage);
			rowcount++;

			row = sheet.createRow(rowcount);
			row.createCell(4).setCellValue("No.of Outages");
			row.createCell(5).setCellValue(stats.outages);
			rowcount++;

			row = sheet.createRow(rowcount);
			row.createCell(4).setCellValue("No.of FiberCuts");
			row.createCell(5).setCellValue(stats.fiberCuts);
			rowcount++;

			row = sheet.createRow(rowcount);
			row.createCell(4).setCellValue("No. of Fluctuations");
			row.createCell(5).setCellValue(stats.fluctuations);

			rowcount++;

			for (int i = 0; i < 15; i++) {
				sheet.autoSizeColumn(i);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		double outagePercentage = 100 - (stats.outageMsTotal / totalMsOfDuration * 100);
		stats.outagePercentage = String.format("%.2f", outagePercentage);

		stats.durationInMinutes = durationInMinutes;
		stats.durationInDays = durationInDays;


		return stats;

	}

	private HuaweiFiberLinkBean getTopologicalLinkPorts(HuaweiFiberLinkBean fiberLink) {

		// HuaweiFiberLinkBean topoLinks = new HuaweiFiberLinkBean();

		String aEndLoc = fiberLink.aEndSrcNodeName.substring(3, 6);
		String zEndLoc = fiberLink.zEndSrcNodeName.substring(3, 6);

		String aEndDeviceId = deviceMapHuawei.get(aEndLoc);
		String zEndDeviceId = deviceMapHuawei.get(zEndLoc);

		Connection connection = null;
		ResultSet result = null;
		Statement stmt = null;

		try {
			connection = dbObj.getDBConnectionHuawei();
			String query = "SELECT tl.aend_device, tl.zend_device, tl.native_emsname, tl.aend_ptp, tl.zend_ptp  "
					+ "FROM HuwaeiU2000.topological_links tl where tl.aend_device = " + aEndDeviceId
					+ " and tl.zend_device = " + zEndDeviceId + "";
			// logger.info(query);
			logger.info("query is " + query);

			System.out.println("query is " + query);

			stmt = connection.createStatement();
			result = stmt.executeQuery(query);

			while (result.next()) {

				String aEndTopologyDeviceId = aEndDeviceId;
				String aEndTopologyPort = result.getString("aend_ptp");
				String zEndTopologyPort = result.getString("zend_ptp");
				String zEndTopologyDeviceId = zEndDeviceId;

				fiberLink.setaEndTopologyDeviceId(aEndTopologyDeviceId);
				fiberLink.setaEndTopologyPort(aEndTopologyPort);
				fiberLink.setzEndTopologyDeviceId(zEndTopologyDeviceId);
				fiberLink.setzEndTopologyPort(zEndTopologyPort);

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

		System.out.println("With topological port : " + fiberLink);

		return fiberLink;

	}

	public int addRowsHistoryAlarmsHuawei(AvailabilityStats stats, XSSFWorkbook wworkbook, XSSFSheet sheet, String startDateStr,
			String endDateStr, String nodeName, String deviceId, String port, int rowcount) {

		long totalMsPerIp = 0;
		int rowCountStart = rowcount;

		Row row;
		Cell cell;
		// int rowcount=0;

		row = sheet.createRow(rowcount);
		cell = row.createCell(0);
		cell.setCellValue(nodeName);
		cell = row.createCell(1);
		cell.setCellValue(deviceId);
		cell = row.createCell(2);
		cell.setCellValue(port);
		rowcount++;

		row = sheet.createRow(rowcount);
		row.createCell(0).setCellValue("Severity");
		;
		row.createCell(1).setCellValue("Alarm Name");
		row.createCell(2).setCellValue("Alarm Source");
		row.createCell(3).setCellValue("Location Info");
		row.createCell(4).setCellValue("Occurred Time");
		row.createCell(5).setCellValue("Cleared Time");
		row.createCell(6).setCellValue("Alarm Duration");
		row.createCell(7).setCellValue("Outage Duration");
		row.createCell(8).setCellValue("Alarm SerialNo");
		rowcount++;

		String response = ServiceNowRestClient.getHuaweiAlarms(deviceId, port, startDateStr, endDateStr);

		if (response != null) {

			JSONObject responseJsonObj = new JSONObject(response);

			JSONObject jsonObj = responseJsonObj.getJSONObject("result");

			if (jsonObj != null && jsonObj.getString("Result").equals("Success")) {
				JSONArray alarmsList = jsonObj.getJSONObject("Data").getJSONArray("alarms");

				for (int i = 0; i < alarmsList.length(); i++) {
					JSONObject alarmObj = alarmsList.getJSONObject(i);

					// "nativeEmsName":"Huawei/U2000;
					// MH-MUM-NTG-B1-NM;/rack=1/shelf=MUMNTG-MCD-R-U9PS-01/sub_shelf=1/slot=5/domain=wdm/port=1"
					String nativeEmsName = alarmObj.getString("nativeEmsName");

					String nameStr[] = nativeEmsName.split(";");
					String portStr[] = nameStr[2].split("/");
					String shelfName = portStr[2];

					System.out.println("ShelfName : " + shelfName);

					shelfName.replace("shelf", nameStr[1]);
					shelfName.replace("=", " / ");

					String alarmSource = shelfName;
					String nodeIp = alarmObj.getString("deviceid");
					String severity = alarmObj.getString("severity");
					String alarmName = alarmObj.getString("nativeProbableCause");
					String locationInfo = alarmObj.getString("locationInfo");
					String alarmPort = alarmObj.getString("port");
					String raiseTime = alarmObj.getString("alarmDate");
					String clearTime = alarmObj.optString("clearedTime");
					String alarmSerialNumber = alarmObj.getString("alarmSerialNumber");

					row = sheet.createRow(rowcount);
					cell = row.createCell(0);
					cell.setCellValue(severity);
					cell = row.createCell(1);
					cell.setCellValue(alarmName);
					cell = row.createCell(2);
					cell.setCellValue(alarmSource);
					cell = row.createCell(3);
					cell.setCellValue(locationInfo);
					cell = row.createCell(4);
					cell.setCellValue(raiseTime);
					cell = row.createCell(5);
					cell.setCellValue(clearTime);

					Date startTime = null;
					Date endTime = null;
					String hms = "";
					Long durationMs = null;

					try {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						startTime = sdf.parse(raiseTime);
						if (!clearTime.isEmpty()) {
							endTime = sdf.parse(clearTime);
						} else {
							endTime = startTime;
						}
						durationMs = endTime.getTime() - startTime.getTime();

						hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(durationMs),
								TimeUnit.MILLISECONDS.toMinutes(durationMs) % TimeUnit.HOURS.toMinutes(1),
								TimeUnit.MILLISECONDS.toSeconds(durationMs) % TimeUnit.MINUTES.toSeconds(1));
						
						if (durationMs <= 5000) {
							// less than 5 secs => flucuation
							stats.fluctuations++;
						} else if (durationMs > 5000) {
							// greater than 5 secs == > fiber cut
							stats.fiberCuts++;
						}

						if (durationMs >= MTTR_TIME) {
							stats.mttrSlippage++;
						}


					} catch (Exception e) {
						e.printStackTrace();
						durationMs = 0L;
					}

					totalMsPerIp += durationMs;

					String alarmDuration = hms;
					
					stats.outageMsTotal = totalMsPerIp;
					stats.outages = alarmsList.length();

					cell = row.createCell(6);
					cell.setCellValue(alarmDuration);
					cell = row.createCell(7);
					cell.setCellValue("");

					cell = row.createCell(8);
					cell.setCellValue(alarmSerialNumber);
					
					String hmsOutage = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(totalMsPerIp),
							TimeUnit.MILLISECONDS.toMinutes(totalMsPerIp) % TimeUnit.HOURS.toMinutes(1),
							TimeUnit.MILLISECONDS.toSeconds(totalMsPerIp) % TimeUnit.MINUTES.toSeconds(1));

					
					stats.outageMsTotalStr = hmsOutage;


/*					if (alarmsList.length() > 0) {

						cell = sheet.getRow(rowcount - 1).getCell(7) != null ? sheet.getRow(rowcount - 1).createCell(7)
								: sheet.getRow(rowcount - 1).getCell(7);
						cell.setCellValue(hmsOutage);
					}
*/

					rowcount++;

				}

				String hmsOutge = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(totalMsPerIp),
						TimeUnit.MILLISECONDS.toMinutes(totalMsPerIp) % TimeUnit.HOURS.toMinutes(1),
						TimeUnit.MILLISECONDS.toSeconds(totalMsPerIp) % TimeUnit.MINUTES.toSeconds(1));

				if (alarmsList.length() > 0) {
					// sheet.addMergedRegion(new
					// CellRangeAddress(rowCountStart,rowcount-1,8,8));

					cell = sheet.getRow(rowCountStart+2).getCell(7) != null ? sheet.getRow(rowCountStart+2).createCell(7)
							: sheet.getRow(rowCountStart+2).getCell(7);
					cell.setCellValue(hmsOutge);
				}

				for (int i = 0; i < 15; i++) {
					sheet.autoSizeColumn(i);
				}

			} else {
				System.out.println("Unable to retrieve alarms from ServiceNow for " + deviceId);
			}

		}

		return rowcount;
	}

	public void initPolling() {

		logger.info("initPolling called");
		currentAlarmsList.clear();
		
		loadDeviceMapCiena();
		loadDeviceMapHuawei();

	}

	public void finishPolling() {

		logger.info("finishPolling called");
		
		
		
		System.out.println("Current Alarms Count : "+currentAlarmsList.size()+"\n"+currentAlarmsList);
		
		computeDurationOfReport();
		String dateStr = generateReport();
		
		ServiceNowRestClient.postAlarmHealthReportToServiceNow(dateStr);
		
		System.out.println("Report generation completed");

		logger.info("finishPolling completed");

	}
	
	
	private void computeDurationOfReport() {
		
		Date currentDate = new Date();
		polled_date = new Timestamp(currentDate.getTime());
		
		Calendar cal = Calendar.getInstance();
		
		/*Calendar startTime=null;
		Calendar endTime = null;*/
		
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

	}

	class AvailabilityStats {

		public long durationInDays;
		public long durationInMinutes;
		String outagePercentage;
		String outageMsTotalStr = "";
		long outageMsTotal = 0;
		int mttrSlippage = 0;
		int outages = 0;
		int fiberCuts = 0;
		int fluctuations = 0;

	}

}
