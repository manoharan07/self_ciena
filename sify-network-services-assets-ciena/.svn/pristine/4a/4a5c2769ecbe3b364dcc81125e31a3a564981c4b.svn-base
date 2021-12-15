package com.sify.network.assets.ciena;

import java.io.File;
import java.io.FileOutputStream;
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

import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
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

public class OpticalReportServiceAvailability extends AssetsAbstractService {

	static Timestamp polled_date = null;
	static Calendar startTime=null;
	static Calendar endTime = null;

	private CienaClient cienaClient = null;
	// private CienaWsClient cienaWsClient = null;

	// private static final String COMMA_DELIMITER = ",";

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("nwavailabailityreportlog");

	private DbConnection dbObj = null;

	private HashMap<String, String> deviceMapHuawei = new HashMap<String, String>();

	private long MTTR_TIME = 4L * 60 * 60 * 1000;

	// int summaryRowCount = 0;

	private HashMap<String, String> spanLossMap = new HashMap<String, String>();
	
	private HashMap<String,ArrayList<String>> locationMap = new HashMap<String,ArrayList<String>>();

	public OpticalReportServiceAvailability() {
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

		ArrayList<PerformanceInputBean> ottNetworks = new ArrayList<PerformanceInputBean>();

	
		Connection connection = null;
		ResultSet result = null;
		Statement stmt = null;

		try {
			connection = dbObj.getDBConnectionHuawei();
			String query = "select customer,location from optical_reports.ott_circuit_details group by customer,location";
			// logger.info(query);
			logger.info("query is " + query);

			stmt = connection.createStatement();
			result = stmt.executeQuery(query);
			
			String customer=null;
			ArrayList<String> locationList=null;

			while (result.next()) {
				
				customer = result.getString("customer");
				
				locationList = locationMap.get(customer);
				
				if (locationList != null) {
					locationList.add(result.getString("location"));
				} else {
					locationList = new ArrayList<String>();
					locationList.add(result.getString("location"));
					PerformanceInputBean bean = new PerformanceInputBean();
					bean.setDeviceip(result.getString("customer"));
					ottNetworks.add(bean);
				}
				
				locationMap.put(customer, locationList);

			}
			
			System.out.println("############### "+locationMap);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ottNetworks;

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
		CienaFiberLinkBean linkObj = (CienaFiberLinkBean) input;
		logger.info(Thread.currentThread().getName() + " : " + linkObj);
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
		logger.info("OpticalReportServiceNetworkAvailability initiated");

		OpticalReportServiceAvailability service = new OpticalReportServiceAvailability();
		service.setServiceName("OpticalReportServiceAvailability");

		DBDetails dbInventory = new DBDetails(CienaProperties.get("db.mysql.ip"), CienaProperties.get("db.mysql.port"),
				CienaProperties.get("db.mysql.username"), CienaProperties.get("db.mysql.password"),
				CienaProperties.get("db.mysql.dbname"));

		service.getDbDetailsMap().put(JobType.INVENTORY, dbInventory);

		service.setInventoryFromDevice(true);
		service.setInventoryThreadTimeout(120);

		logger.info("OpticalReportServiceAvailability details added");

		// providing the cron string for the jobs
		service.setAggregateCron("");
		service.setPollCron("");
		service.setInventoryCron(CienaProperties.get("optical.service.availability.report.cron"));

		AssetsCoreRuntime runtime = new AssetsCoreRuntime();
		runtime.register(service);
		logger.info("OpticalReportServiceAvailability registered with core");

	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("config" + File.separator + "log4j.properties");
		OpticalReportServiceAvailability service = new OpticalReportServiceAvailability();
		service.init();

		// service.generateReport();

	}

	@Override
	public Map<String, String> retrieveLiveLinkUsage(PerformanceInputBean pd, long startTime, long endTime) {
		return null;

	}

	@Override
	public ArrayList<InventoryDeviceDetails> retrieveInventoryFromDevice(PerformanceInputBean input) {

		//Date currentDate = new Date();
		//polled_date = new Timestamp(currentDate.getTime());
		computeDurationOfReport();

		// logger.info("retrieveInventoryFromDevice " + ((CienaFiberLinkBean)
		// input));

		ArrayList<InventoryDeviceDetails> list = new ArrayList<InventoryDeviceDetails>();

		System.out.println("Poll date " + polled_date + ", " + input.getDeviceip());

		generateReport(input.getDeviceip());

		// list.add(retrieveSpanLossFiberLink((CienaFiberLinkBean) input));

		return list;
	}


	public String generateReport(String ottCustomerName) {

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

			String ottCustomerNameForFile = ottCustomerName.replaceAll(" ", "_");

			String filePath = CienaProperties.get("optical.network.availability.report.folder");
			String excelPath = filePath + File.separator + ottCustomerNameForFile + "_Service_Availability_Report" + dateStr
					+ ".xlsx";

			FileOutputStream fileOut = new FileOutputStream(excelPath);
			XSSFWorkbook workbook = createWorkBook();

			generateSheets(workbook, ottCustomerName, startDateStr, endDateStr);

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

	private void generateSheets(XSSFWorkbook workbook, String ottCustomerName, String startDate, String endDate) {

		int summaryRowCount = 3;

		int mttrCount = 0;
		int outagesCount = 0;
		int outageMinutesTotal = 0;
		int fiberCutCount = 0;
		int fluctuationsCount = 0;

		AvailabilityStats statsSample = null;

		DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		logger.info(f.format(polled_date));

		String dateStr = f.format(polled_date);

		XSSFSheet summarySheet = workbook.createSheet("Summary");

		Row row;
		row = summarySheet.createRow(summaryRowCount);
		row.createCell(0).setCellValue("Region");
		;
		row.createCell(1).setCellValue("Capacity");
		row.createCell(2).setCellValue("MTTR Slippage");
		row.createCell(3).setCellValue(polled_date);
		summaryRowCount++;

		
			ArrayList<PerformanceInputBean> fiberLinks = loadFiberLinksCiena(ottCustomerName);
			
			ArrayList<String> locationList = locationMap.get(ottCustomerName);
			
			for(String location:locationList) {
			generateCircuitsSheet(workbook, ottCustomerName, location, dateStr);
			generateIncidentsSheet(workbook, ottCustomerName, location, dateStr);
			}

		/*	for (PerformanceInputBean fiberLink : fiberLinks) {
				AvailabilityStats stats = create6500HistoryAlarmsCiena(workbook, dateStr,
						(CienaFiberLinkBean) fiberLink);

				CienaFiberLinkBean link = (CienaFiberLinkBean) fiberLink;

				// XSSFSheet summarySheet = wworkbook.getSheet("Summary");

				row = summarySheet.createRow(summaryRowCount);

				row.createCell(0).setCellValue(link.linkName);
				row.createCell(1).setCellValue(stats.outageMsTotalStr);

				row.createCell(2).setCellValue(stats.mttrSlippage);
				mttrCount += stats.mttrSlippage;

				row.createCell(3).setCellValue(stats.outages);
				outagesCount += stats.outages;

				row.createCell(4).setCellValue(stats.fiberCuts);
				fiberCutCount += stats.fiberCuts;

				row.createCell(5).setCellValue(stats.fluctuations);
				fluctuationsCount += stats.fluctuations;

				row.createCell(6).setCellValue(spanLossMap.get(link.linkName));
				long outageInminutes = stats.outageMsTotal / (60 * 1000);

				row.createCell(7).setCellValue(outageInminutes);
				outageMinutesTotal += outageInminutes;

				row.createCell(8).setCellValue(stats.outagePercentage);

				statsSample = stats;

				summaryRowCount++;

			}
*/

		
/*		
		
		row = summarySheet.createRow(summaryRowCount);
		row.createCell(2).setCellValue(mttrCount);
		row.createCell(3).setCellValue(outagesCount);
		row.createCell(4).setCellValue(fiberCutCount);
		row.createCell(5).setCellValue(fluctuationsCount);
		row.createCell(7).setCellValue(outageMinutesTotal);

		summaryRowCount += 4;

		row = summarySheet.createRow(summaryRowCount);

		row.createCell(6).setCellValue("Total Time");
		row.createCell(7).setCellValue(statsSample.durationInMinutes);
		summaryRowCount++;

		row = summarySheet.createRow(summaryRowCount);
		row.createCell(6).setCellValue("No. of Days");
		row.createCell(7).setCellValue(statsSample.durationInDays);
		summaryRowCount++;

		row = summarySheet.createRow(summaryRowCount);
		row.createCell(6).setCellValue("MTTR deviation beyond 4 hrs");
		row.createCell(7).setCellValue(mttrCount);
		summaryRowCount++;

		row = summarySheet.createRow(summaryRowCount);
		row.createCell(6).setCellValue("Average time between each Fiber cut in Hrs");
		Cell cell = row.createCell(7);
		long averageTimeFiberCuts = (long) ((statsSample.durationInMinutes / (double) fiberCutCount) * 60 * 1000);

		String averageTimeFiberCutsHMS = String.format("%02d:%02d:%02d",
				TimeUnit.MILLISECONDS.toHours(averageTimeFiberCuts),
				TimeUnit.MILLISECONDS.toMinutes(averageTimeFiberCuts) % TimeUnit.HOURS.toMinutes(1),
				TimeUnit.MILLISECONDS.toSeconds(averageTimeFiberCuts) % TimeUnit.MINUTES.toSeconds(1));

		cell.setCellValue(averageTimeFiberCutsHMS);
		summaryRowCount++;

		row = summarySheet.createRow(summaryRowCount);
		long averageRestoreTime = (long) ((outageMinutesTotal / (double) fiberCutCount) * 60 * 1000);
		String averageRestoreTimeHMS = String.format("%02d:%02d:%02d",
				TimeUnit.MILLISECONDS.toHours(averageRestoreTime),
				TimeUnit.MILLISECONDS.toMinutes(averageRestoreTime) % TimeUnit.HOURS.toMinutes(1),
				TimeUnit.MILLISECONDS.toSeconds(averageRestoreTime) % TimeUnit.MINUTES.toSeconds(1));

		row.createCell(6).setCellValue("Average time to restore fiber");
		row.createCell(7).setCellValue(averageRestoreTimeHMS);
		summaryRowCount++;
*/
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
				if (nodeName.contains("MUM") || nodeName.contains("TAN")) {
					String nodeNameArray[] = nodeName.split("-");
					String key = nodeNameArray[2];
					deviceMap.put(key, elementId);
				}
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

	public ArrayList<PerformanceInputBean> loadFiberLinksHuawei(String roadmName) {

		ArrayList<PerformanceInputBean> fiberLinks = new ArrayList<PerformanceInputBean>();
		Connection connection = null;
		ResultSet result = null;
		Statement stmt = null;

		try {
			connection = dbObj.getDBConnectionHuawei();
			String query = "SELECT device_type, roadm_name, link_name, aend_source_node_name, aend_source_port, aend_sink_node_name, aend_sink_port, "
					+ "zend_source_node_name, zend_source_port, zend_sink_node_name, zend_sink_port, "
					+ "design_optical_length_km, design_eol, updated_time FROM HuwaeiU2000.fiber_links";
			// logger.info(query);
			logger.info("query is " + query);

			stmt = connection.createStatement();
			result = stmt.executeQuery(query);

			while (result.next()) {

				HuaweiFiberLinkBean input = new HuaweiFiberLinkBean();

				String aEndSrcNodeName = result.getString("aend_source_node_name");
				String aEndSrcPort = result.getString("aend_source_port");
				String aEndSinkNodeName = result.getString("aend_sink_node_name");
				String aEndSinkPort = result.getString("aend_sink_port");

				String zEndSrcNodeName = result.getString("zend_source_node_name");
				String zEndSrcPort = result.getString("zend_source_port");
				String zEndSinkNodeName = result.getString("zend_sink_node_name");
				String zEndSinkPort = result.getString("zend_sink_port");

				// String roadmName = result.getString("roadm_name");
				String linkName = result.getString("link_name");
				String opticalLength = result.getString("design_optical_length_km");
				String bol = result.getString("design_eol");

				input.setaEndSrcNodeName(aEndSrcNodeName);
				input.setaEndSrcPort(aEndSrcPort);
				input.setaEndSinkNodeName(aEndSinkNodeName);
				input.setaEndSinkPort(aEndSinkPort);

				input.setzEndSrcNodeName(zEndSrcNodeName);
				input.setzEndSrcPort(zEndSrcPort);
				input.setzEndSinkNodeName(zEndSinkNodeName);
				input.setzEndSinkPort(zEndSinkPort);

				input.setRoadmName(roadmName);
				input.setLinkName(linkName);
				input.setDesignedOpticalLength(opticalLength);
				input.setDesignedBOL(bol);

				input.setDeviceip(linkName);

				fiberLinks.add(input);
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

		System.out.println(fiberLinks);

		return fiberLinks;

	}

	public ArrayList<PerformanceInputBean> loadFiberLinksCiena(String roadmName) {

		ArrayList<PerformanceInputBean> fiberLinks = new ArrayList<PerformanceInputBean>();
		Connection connection = null;
		ResultSet result = null;
		Statement stmt = null;

		try {
			connection = dbObj.getDBConnection();
			String query = "SELECT roadm_name, link_name, aend_node_name, aend_node_ip, aend_port, "
					+ "zend_node_name, zend_node_ip, zend_port, "
					+ "design_optical_length_km, design_bol FROM optical_reports.fiber_links " + "where roadm_name='"
					+ roadmName + "'";
			// logger.info(query);
			logger.info("query is " + query);

			stmt = connection.createStatement();
			result = stmt.executeQuery(query);

			while (result.next()) {

				CienaFiberLinkBean input = new CienaFiberLinkBean();

				String aEndNodeName = result.getString("aend_node_name");
				String aEndNodeIp = result.getString("aend_node_ip");
				String aEndPort = result.getString("aend_port");

				String zEndNodeName = result.getString("zend_node_name");
				String zEndNodeIp = result.getString("zend_node_ip");
				String zEndPort = result.getString("zend_port");

				// String roadmName = result.getString("roadm_name");
				String linkName = result.getString("link_name");
				String opticalLength = result.getString("design_optical_length_km");
				String bol = result.getString("design_bol");

				input.setaEndNodeName(aEndNodeName);
				input.setaEndNodeIp(aEndNodeIp);
				input.setaEndPort(aEndPort);

				input.setzEndNodeName(zEndNodeName);
				input.setzEndNodeIp(zEndNodeIp);
				input.setzEndPort(zEndPort);

				input.setRoadmName(roadmName);
				input.setLinkName(linkName);
				input.setDesignedOpticalLength(opticalLength);
				input.setDesignedBOL(bol);

				input.setDeviceip(linkName);

				fiberLinks.add(input);
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

		return fiberLinks;
	}


	public void generateCircuitsSheet(XSSFWorkbook wworkbook, String customer, String location, String dateStr) {
		try {
			
			int capacityTotal=0;
			
			XSSFSheet sheet = wworkbook.createSheet(location + " Circuits Availability");
			Row row;
			Cell cell;
			int rowcount = 0;
			
			row = sheet.createRow(rowcount);
			row.createCell(0).setCellValue(customer);
			row.createCell(1).setCellValue("Capacity");
			row.createCell(2).setCellValue("Service Availability in %");
			rowcount++;
			rowcount++;
			rowcount++;
			
			
			
			row = sheet.createRow(rowcount);
			row.createCell(0).setCellValue("S.No");
			row.createCell(1).setCellValue("Circuit ID");
			row.createCell(2).setCellValue("Capacity");
			row.createCell(3).setCellValue("Outage Duration (HH:MM:SS)");
			row.createCell(4).setCellValue("Availability");
			row.createCell(5).setCellValue("Time in Mins");

			rowcount++;

			Connection connection = null;
			ResultSet result = null;
			Statement stmt = null;

			DbConnection dbObj = new DbConnection();
			connection = dbObj.getDBConnection();
			String query = "select circuit_name, capacity from optical_reports.ott_circuit_details ocd  where customer = '"+customer+"' and location='"+location+"' order by circuit_name";
			logger.info(query);
			logger.info("query is " + query);

			stmt = connection.createStatement();
			result = stmt.executeQuery(query);


			while (result.next()) {
				

				String circuitName = result.getString("circuit_name");
				String capacityStr = result.getString("capacity");
				
				Integer capacity = 0;
				
				if (capacityStr.endsWith("G")) {
					capacity = Integer.valueOf(capacityStr.replace("G", "").trim());
				} else if (capacityStr.endsWith("GE")) {
					capacity = Integer.valueOf(capacityStr.replace("GE", "").trim());
				}
				capacityTotal += capacity; 
				
				row = sheet.createRow(rowcount);
				row.createCell(0).setCellValue(rowcount-1);
				row.createCell(1).setCellValue(circuitName);
				row.createCell(2).setCellValue(capacityStr);
				row.createCell(3).setCellValue(""); //outage
				row.createCell(4).setCellValue(""); //availability
				row.createCell(5).setCellValue(""); //timeInMins

				rowcount++;
			}
			
			rowcount=1;
			row = sheet.createRow(rowcount);
			
			
			Double availabilityPercentage=100D;
			
			row.createCell(0).setCellValue(location);
			row.createCell(1).setCellValue(capacityTotal);
			row.createCell(2).setCellValue(availabilityPercentage);
			
			for (int i = 0; i < 15; i++) {
				sheet.autoSizeColumn(i);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void generateIncidentsSheet(XSSFWorkbook wworkbook, String customer,String location, String dateStr) {

		Date startDate = startTime.getTime();
		Date endDate = endTime.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startDateStr = sdf.format(startDate);
		String endDateStr = sdf.format(endDate);

		SimpleDateFormat sdfMonth = new SimpleDateFormat("MMM-yyyy");
		String monthStr = sdf.format(startDate);

		int rowcount = 11;

		System.out.println("Retrieving Incidents from ServiceNow between " + startDateStr + " and " + endDateStr);

		XSSFSheet sheet = wworkbook.createSheet(location+" Incidents");

		long totalMsPerIp = 0;
		int rowCountStart = rowcount;

		Row row;
		Cell cell;

		// int rowcount=0;
		row = sheet.createRow(rowcount);
		row.createCell(0).setCellValue("S.No.");
		row.createCell(1).setCellValue("Date of Incident");
		row.createCell(2).setCellValue("Sify SR");
		row.createCell(3).setCellValue("Google SR");
		row.createCell(4).setCellValue("No. of Circuits Affected");
		row.createCell(5).setCellValue("Circuit IDs");
		row.createCell(6).setCellValue("Alarm Observed");
		row.createCell(7).setCellValue("Occurred On (IST)");
		row.createCell(8).setCellValue("Cleared On (IST)");
		row.createCell(9).setCellValue("RFO");
		row.createCell(10).setCellValue("Attributed to");
		rowcount++;

		String taskGroup = null;

		if (customer.equals("Chennai ROADM")) {
			taskGroup = "FSM_CHN";
		} else if (customer.equals("BGL ROADM")) {
			taskGroup = "FSM_BLR";
		} else if (customer.equals("Delhi ROADM")) {
			taskGroup = "FSM_DEL";
		} else if (customer.equals("Mumbai ROADM")) {
			taskGroup = "FSM_MUM";
		}

		String response = ServiceNowRestClient.getCasesFromServiceNow(customer, startDateStr, endDateStr);

		if (response != null) {

			JSONObject responseJsonObj = new JSONObject(response);

			JSONObject jsonObj = responseJsonObj.getJSONObject("result");

			if (jsonObj != null && jsonObj.getString("Result").equals("Success")) {
				JSONArray incidentsList = jsonObj.getJSONObject("Data").getJSONArray("cases");

				for (int i = 0; i < incidentsList.length(); i++) {
					JSONObject incidentObj = incidentsList.getJSONObject(i);

					String incidentNumber = incidentObj.getString("number");
					String occurredDate = incidentObj.getString("openedDate");
					String clearedDate = incidentObj.getString("closedDate");
					String problemIncident = incidentObj.getString("shortDescription");

/*					String latlong = "";

					String workNotes[] = incidentObj.optString("workNotes").split("\n\n");
					String comments = "";
					for (String workNote : workNotes) {
						
						if (workNote.contains("RFO ::") || workNote.contains("RFO::") || workNote.contains("RFO:-")) {
							System.out.println(incidentNumber + " Worknote "+workNote);
							comments = workNote;
						}

						if (workNote.contains("Latlong:") || workNote.contains("LATLONG:")) {
							System.out.println("#####LATLONG "+workNote);
							try {
								//latlong = workNote.substring(workNote.indexOf("Latlong") + 8,
								//		workNote.length());
								latlong = workNote;
							} catch (Exception e) {
								e.printStackTrace();
								logger.info("LatLong is not in proper format for incident " + incidentNumber);
							}
						}
					}
*/
					row = sheet.createRow(rowcount);
					cell = row.createCell(0);
					cell.setCellValue(rowcount-1);
					cell = row.createCell(1);
					cell.setCellValue(occurredDate);
					cell = row.createCell(2);
					cell.setCellValue(incidentNumber);

/*					Date occurredTime = null;
					Date clearedTime = null;
					String hms = "";
					Long durationMs = null;

					try {
						// SimpleDateFormat sdf = new
						// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						occurredTime = sdf.parse(occurredDate);
						clearedTime = sdf.parse(clearedDate);
						durationMs = clearedTime.getTime() - occurredTime.getTime();

						hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(durationMs),
								TimeUnit.MILLISECONDS.toMinutes(durationMs) % TimeUnit.HOURS.toMinutes(1),
								TimeUnit.MILLISECONDS.toSeconds(durationMs) % TimeUnit.MINUTES.toSeconds(1));

					} catch (Exception e) {
						e.printStackTrace();
					}

					totalMsPerIp += durationMs;

					String alarmDuration = hms;

					cell = row.createCell(4);
					cell.setCellValue(alarmDuration);
*/
					rowcount++;

				}

				for (int i = 0; i < 17; i++) {
					sheet.autoSizeColumn(i);
				}

			} else {
				System.out.println("Unable to retrieve cases from ServiceNow for " + customer);
			}

		}

	}



	public void initPolling() {

		logger.info("initPolling called");

	}

	public void finishPolling() {

		logger.info("finishPolling called");

		// String dateStr=generateReport();

		// ServiceNowRestClient.postSpanLossReportToServiceNow(dateStr);

		logger.info("finishPolling completed");

	}
	
	
	private void computeDurationOfReport() {
		
		Date currentDate = new Date();
		polled_date = new Timestamp(currentDate.getTime());
		
		Calendar cal = Calendar.getInstance();
		
		/*Calendar startTime=null;
		Calendar endTime = null;*/
		
		if (CienaProperties.get("optical.service.availability.report.duration").equals("LAST_WEEK")) {

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

		} else if (CienaProperties.get("optical.service.availability.report.duration").equals("LAST_MONTH")) {

			// last month

			startTime = (Calendar) cal.clone();
			startTime.add(Calendar.MONTH, -3);
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
