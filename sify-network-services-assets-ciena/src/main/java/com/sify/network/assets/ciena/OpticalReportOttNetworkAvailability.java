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

public class OpticalReportOttNetworkAvailability extends AssetsAbstractService {

	static Timestamp polled_date = null;
	static Calendar startTime=null;
	static Calendar endTime = null;

	private CienaClient cienaClient = null;
	// private CienaWsClient cienaWsClient = null;
	
	CienaMcpClient mcpClient = null;
	
	private boolean reportFromMCP=false;

	// private static final String COMMA_DELIMITER = ",";

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("nwavailabailityreportlog");

	private DbConnection dbObj = null;

	private HashMap<String, String> deviceMapHuawei = new HashMap<String, String>();

	private long MTTR_TIME = 4L * 60 * 60 * 1000;
	private static long FIBER_CUT_TIME = 5 * 60 * 1000; // 5 minutes

	// int summaryRowCount = 0;

	//private HashMap<String, String> spanLossMap = new HashMap<String, String>();
	
	private HashMap<String,ArrayList<CienaFiberLinkBean>> fiberLinkMap = new HashMap<String,ArrayList<CienaFiberLinkBean>>();

	public OpticalReportOttNetworkAvailability() {
		super();
		cienaClient = new CienaClient();
		dbObj = new DbConnection();

	}

	@Override
	public ArrayList<InventoryDeviceDetails> fetchDeviceDetails() {
		
		
		computeDurationOfReport();
		
		fiberLinkMap.clear();

		logger.info("Cleared FiberLinkMap before report generation ");
		
		initInventory();
		
		

		ArrayList<InventoryDeviceDetails> list = new ArrayList<InventoryDeviceDetails>();
		
		String dateStr=generateReport();

		//ServiceNowRestClient.postNetworkAvailabilityReportToServiceNow(dateStr,"OTT");
		postToServiceNow(dateStr,"OTT");
		
		reportFromMCP = true;
		
		dateStr=generateReport();

		//ServiceNowRestClient.postNetworkAvailabilityReportToServiceNow(dateStr,"OTT");
		postToServiceNow(dateStr,"OTT");

		
		
		
		return list;
	}
	
	private void postToServiceNow(String dateStr, String roadmName) {
		String roadmNameForFile = "";
		String fileName = "";

		if (reportFromMCP) {
			roadmNameForFile = roadmName.replaceAll(" ", "_");
			fileName = roadmNameForFile + "_NW_Availability_Report_MCP_" + dateStr + ".xlsx";

			ServiceNowRestClient.postNetworkAvailabilityReportToServiceNow(dateStr, roadmName, fileName);
		} else {
			roadmNameForFile = roadmName.replaceAll(" ", "_");
			fileName = roadmNameForFile + "_NW_Availability_Report_" + dateStr + ".xlsx";

			ServiceNowRestClient.postNetworkAvailabilityReportToServiceNow(dateStr, roadmName, fileName);
		}

	}

	@Override
	public ArrayList<PerformanceInputBean> loadDeviceIpListCPU() {

		ArrayList<PerformanceInputBean> roadmNetworks = new ArrayList<PerformanceInputBean>();


		return roadmNetworks;
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
		logger.info("OpticalReportOttNetworkAvailability initiated");

		OpticalReportOttNetworkAvailability service = new OpticalReportOttNetworkAvailability();
		service.setServiceName("OpticalReportOttNetworkAvailability");

		DBDetails dbInventory = new DBDetails(CienaProperties.get("db.mysql.ip"), CienaProperties.get("db.mysql.port"),
				CienaProperties.get("db.mysql.username"), CienaProperties.get("db.mysql.password"),
				CienaProperties.get("db.mysql.dbname"));

		service.getDbDetailsMap().put(JobType.INVENTORY, dbInventory);

		//service.setInventoryFromDevice(true);
		service.setInventoryThreadTimeout(120);

		logger.info("OpticalReportOttNetworkAvailability  details added");

		// providing the cron string for the jobs
		service.setAggregateCron("");
		service.setPollCron("");
		service.setInventoryCron(CienaProperties.get("optical.ott.network.availability.report.cron"));

		AssetsCoreRuntime runtime = new AssetsCoreRuntime();
		runtime.register(service);
		logger.info("OpticalReportOttNetworkAvailability registered with core");

	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("config" + File.separator + "log4j_ottnetwork.properties");
		OpticalReportOttNetworkAvailability service = new OpticalReportOttNetworkAvailability();
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
/*		computeDurationOfReport();

		// logger.info("retrieveInventoryFromDevice " + ((CienaFiberLinkBean)
		// input));

		ArrayList<InventoryDeviceDetails> list = new ArrayList<InventoryDeviceDetails>();

		logger.info("Poll date " + polled_date + ", " + input.getDeviceip());

		
		
		String dateStr=generateReport(input.getDeviceip());

		ServiceNowRestClient.postNetworkAvailabilityReportToServiceNow(dateStr,input.getDeviceip());
		
		*/

		ArrayList<InventoryDeviceDetails> list = new ArrayList<InventoryDeviceDetails>();
		return list;
    }

	public InventoryDeviceDetails retrieveSpanLossFiberLink(CienaFiberLinkBean fiberLink) {

		Double aEndSpanLoss = retrieveSpanLossPort(fiberLink.aEndNodeIp, fiberLink.aEndPort);
		Double zEndSpanLoss = retrieveSpanLossPort(fiberLink.zEndNodeIp, fiberLink.zEndPort);

		logger.info(
				fiberLink.linkName + ", " + fiberLink.aEndNodeName + ", " + fiberLink.aEndPort + " : " + aEndSpanLoss);
		logger.info(
				fiberLink.linkName + ", " + fiberLink.zEndNodeName + ", " + fiberLink.zEndPort + " : " + zEndSpanLoss);

		Double aEndBol = null;
		Double zEndBol = null;

		boolean aEndSpanDegraded1 = false;
		boolean zEndSpanDegraded1 = false;

		Double designBOL = null;

		if (fiberLink.roadmName.equals("Chennai Facebook")) {

			String bolStr = fiberLink.designedBOL;
			String bolArray[] = bolStr.split(":");
			aEndBol = Double.valueOf(bolArray[0]);
			zEndBol = Double.valueOf(bolArray[1]);

			aEndSpanDegraded1 = (aEndSpanLoss != null && aEndSpanLoss >= aEndBol) ? true : false;
			zEndSpanDegraded1 = (zEndSpanLoss != null && zEndSpanLoss >= zEndBol) ? true : false;

		} else {
			designBOL = Double.parseDouble(fiberLink.designedBOL);
			aEndSpanDegraded1 = (aEndSpanLoss != null && aEndSpanLoss >= designBOL) ? true : false;
			zEndSpanDegraded1 = (zEndSpanLoss != null && zEndSpanLoss >= designBOL) ? true : false;
		}

		/*
		 * boolean aEndSpanDegraded = (aEndSpanLoss != null && aEndSpanLoss >=
		 * designBOL) ? true : false; boolean zEndSpanDegraded = (zEndSpanLoss
		 * != null && zEndSpanLoss >= designBOL) ? true : false;
		 */

		boolean aEndSpanDegraded = aEndSpanDegraded1;
		boolean zEndSpanDegraded = zEndSpanDegraded1;

		InventoryDeviceDetails perfData = new InventoryDeviceDetails(fiberLink.linkName) {
			String sql = "INSERT INTO optical_reports.span_loss ("
					+ "device_type, roadm_name, link_name, aend_node_name, aend_node_ip, aend_port, "
					+ "zend_node_name, zend_node_ip, zend_port, design_optical_length_km, design_bol, "
					+ "aend_span_loss, zend_span_loss, aend_span_degraded, zend_span_degraded, collection_date, updated_time) VALUES('"
					+ fiberLink.getDevice_type() + "', '" + fiberLink.getRoadmName() + "','" + fiberLink.getLinkName()
					+ "', '" + fiberLink.aEndNodeName + "', '" + fiberLink.aEndNodeIp + "', '" + fiberLink.aEndPort
					+ "', '" + fiberLink.getzEndNodeName() + "', '" + fiberLink.getzEndNodeIp() + "', '"
					+ fiberLink.getzEndPort() + "', '" + fiberLink.getDesignedOpticalLength() + "', '"
					+ fiberLink.getDesignedBOL() + "', '" + aEndSpanLoss + "', '" + zEndSpanLoss + "', "
					+ aEndSpanDegraded + ", " + zEndSpanDegraded + ", '" + polled_date + "', '" + polled_date + "') "
					+ "ON DUPLICATE KEY UPDATE aend_span_loss='" + aEndSpanLoss + "',zend_span_loss='" + zEndSpanLoss
					+ "'," + "aend_span_degraded=" + aEndSpanDegraded + ",zend_span_degraded=" + zEndSpanDegraded
					+ ", updated_time='" + polled_date + "'";

			@Override
			public String getInsertQuery() {
				return sql;
			}
		};

		return perfData;
	}

	public Double retrieveSpanLossPort(String ip, String port) {
		Double spanLoss = null;

		try {

			HttpsURLConnection loginConn = cienaClient.doLoginUrlEncoded("https://" + ip + ":8443/api/login");

			if (loginConn == null) {
				logger.info("Unable to connect with " + ip + ", continuing with next ip");
				// fw.write(ip + "," + aid + "," + tx_tag + "," + rx_actual);

				logger.info(ip + "," + "," + ",");

				return spanLoss;
			}

			Map<String, String> sessionMap = cienaClient.getSessionId(loginConn);

			HttpsURLConnection apiConnOscs = cienaClient.getHttpsConnectionWithoutCertificate(
					"https://" + ip + ":8443/api/v1/datastore/oscs/osc/" + port, "GET", sessionMap);

			JSONArray jsonArrayOscs = (JSONArray) cienaClient.getJson(apiConnOscs);

			JSONObject jsonObj = jsonArrayOscs.getJSONObject(0);

			spanLoss = jsonObj.getDouble("span-loss");

		} catch (Exception e) {
			logger.info("Got error in https://" + ip + ":8443/api/v1/datastore/oscs/osc/" + port);
			e.printStackTrace(System.out);
		}

		return spanLoss;
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
			
			String excelPath = null;
			

			String filePath = CienaProperties.get("optical.ott.network.availability.report.folder");
			if (reportFromMCP) {
				excelPath = filePath + File.separator + "OTT_NW_Availability_Report_MCP_" + dateStr + ".xlsx";

			} else {
				excelPath = filePath + File.separator + "OTT_NW_Availability_Report_" + dateStr + ".xlsx";
			}

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

	private void generateSheets(XSSFWorkbook workbook,  String startDate, String endDate) {

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
		

		
			//ArrayList<PerformanceInputBean> fiberLinks = loadFiberLinksCiena(roadmName);
			
			loadFiberLinksCiena();

			//generateSpanLossSheetCiena(workbook, roadmName, dateStr);
			//generateIncidentsSheet(workbook, roadmName, dateStr);
			
			for (String fiberLinkKey:fiberLinkMap.keySet()) {
				
			ArrayList<CienaFiberLinkBean> fiberLinks = fiberLinkMap.get(fiberLinkKey);

			//for (PerformanceInputBean fiberLink : fiberLinks) {
				ArrayList<AvailabilityStats> statsList = create6500HistoryAlarmsCiena(workbook, dateStr,
						fiberLinks);

				//CienaFiberLinkBean link = (CienaFiberLinkBean) fiberLink;

				// XSSFSheet summarySheet = wworkbook.getSheet("Summary");
				
				for (AvailabilityStats stats:statsList) {
					
					logger.info("################## "+stats);
					
					  int scolindex=0;
						
					    if (stats.linkType.equals("Primary")) {
					    	scolindex=0;
					    } else if (stats.linkType.equals("Secondary")) {
					    	scolindex=5;
					    } else {
					    	scolindex=10;
					    }
					
					
					row = summarySheet.getRow(summaryRowCount)!=null?summarySheet.getRow(summaryRowCount):summarySheet.createRow(summaryRowCount);
					row.createCell(scolindex+4).setCellValue("Path Type ");
					row.createCell(scolindex+5).setCellValue(stats.linkType);
					//summaryRowCount++;
					
					row = summarySheet.getRow(summaryRowCount+1)!=null?summarySheet.getRow(summaryRowCount+1):summarySheet.createRow(summaryRowCount+1);
		
					row.createCell(0).setCellValue("Span Details");
				    row.createCell(1).setCellValue("Capacity");
				    row.createCell(2).setCellValue("Isolation Event");
			
				    row.createCell(scolindex+3).setCellValue("No. of Cuts");
					row.createCell(scolindex+4).setCellValue("No. of Flaps");
					row.createCell(scolindex+5).setCellValue("No. Planned Activity");
					row.createCell(scolindex+6).setCellValue("Availability in %");
					//summaryRowCount++;

				row = summarySheet.getRow(summaryRowCount+2)!=null?summarySheet.getRow(summaryRowCount+2):summarySheet.createRow(summaryRowCount+2);

				row.createCell(0).setCellValue(stats.linkName);
				row.createCell(1).setCellValue(stats.capacity);
				row.createCell(2).setCellValue("");
				

				row.createCell(scolindex+3).setCellValue(stats.fiberCuts);
				fiberCutCount += stats.fiberCuts;

				row.createCell(scolindex+4).setCellValue(stats.fluctuations);
				fluctuationsCount += stats.fluctuations;
				
				row.createCell(scolindex+5).setCellValue(0);

				row.createCell(scolindex+6).setCellValue(stats.outagePercentage);

				statsSample = stats;
				
				}

				summaryRowCount += 4;

			//}
			
			}


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
		for (int i = 0; i < 20; i++) {
			summarySheet.autoSizeColumn(i);
		}


	}



	public ArrayList<PerformanceInputBean> loadFiberLinksCiena() {
		
		logger.info("Loading Fiber links");

		ArrayList<PerformanceInputBean> fiberLinks = new ArrayList<PerformanceInputBean>();
		Connection connection = null;
		ResultSet result = null;
		Statement stmt = null;

		try {
			connection = dbObj.getDBConnection();
			String query = "SELECT customer_name, location, link_name, path_type, aend_node_name, aend_node_ip, aend_port, "
					+ "zend_node_name, zend_node_ip, zend_port, capacity "
					+ "FROM optical_reports.ott_fiber_links ";
			
			// logger.info(query);
			logger.info("query is " + query);

			stmt = connection.createStatement();
			result = stmt.executeQuery(query);

			while (result.next()) {

				CienaFiberLinkBean input = new CienaFiberLinkBean();
				
				String customerName = result.getString("customer_name");
				String location = result.getString("location");
				String linkType = result.getString("path_type");


				String aEndNodeName = result.getString("aend_node_name");
				String aEndNodeIp = result.getString("aend_node_ip");
				String aEndPort = result.getString("aend_port");

				String zEndNodeName = result.getString("zend_node_name");
				String zEndNodeIp = result.getString("zend_node_ip");
				String zEndPort = result.getString("zend_port");

				String linkName = result.getString("link_name");
				String capacity = result.getString("capacity");

				input.setaEndNodeName(aEndNodeName);
				input.setaEndNodeIp(aEndNodeIp);
				input.setaEndPort(aEndPort);

				input.setzEndNodeName(zEndNodeName);
				input.setzEndNodeIp(zEndNodeIp);
				input.setzEndPort(zEndPort);

				//input.setRoadmName(roadmName);
				input.setLinkName(linkName);
				
				input.setCustomerName(customerName);
				input.setLocation(location);
				input.setLinkType(linkType);
				//input.setDesignedOpticalLength(opticalLength);
				//input.setDesignedBOL(bol);
				
				input.setCapacity(capacity);

				input.setDeviceip(linkName);
				
				String fiberLinkKey = customerName + ":" + location + ":" + linkName;
				
				ArrayList<CienaFiberLinkBean> fiberLinkList=null;
				
				if (fiberLinkMap.containsKey(fiberLinkKey)) {
					fiberLinkList = fiberLinkMap.get(fiberLinkKey);
				} else {
					fiberLinkList = new ArrayList<CienaFiberLinkBean>();
					
				}
				
				fiberLinkList.add(input);
				fiberLinkMap.put(fiberLinkKey, fiberLinkList);

				//fiberLinks.add(input);
			}

		} catch (Exception e) {
			e.printStackTrace();
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
		
		logger.info("FiberLink Map : "+fiberLinkMap);
		
		for (String fiberLinkKey:fiberLinkMap.keySet()) {
			ArrayList<CienaFiberLinkBean> fiberLinkList = fiberLinkMap.get(fiberLinkKey);
			
			logger.info("Loaded Span Name : "+fiberLinkKey+", Number of Paths : "+fiberLinkList.size() + ", "+fiberLinkList);
		}

		return fiberLinks;
	}





	public ArrayList<AvailabilityStats> create6500HistoryAlarmsCiena(XSSFWorkbook wworkbook, String dateStr,
			ArrayList<CienaFiberLinkBean> fiberLinks) {
		
		ArrayList<AvailabilityStats> statsList = new ArrayList<AvailabilityStats>();

		AvailabilityStats statsPrimary = null;
		AvailabilityStats statsSecondary = null;
		AvailabilityStats statsTertiary = null;
		
		AvailabilityStats statsCurrent = null;


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

		logger.info("Retrieving history alarms from ServiceNow between " + startDateStr + " and " + endDateStr);
		
		XSSFSheet sheet=null;
		
		CienaFiberLinkBean link = null;
		
		for(CienaFiberLinkBean fiberLink:fiberLinks) {
			
			String sheetName = fiberLink.getCustomerName()+ "-" + fiberLink.getLocation() + "(" + fiberLink.getLinkName()+")";
			String capacity = fiberLink.getCapacity();
			
			logger.info("###########Capacity "+capacity);
			
			logger.info("\n\nUpdating excel values for "+sheetName+", "+fiberLink.linkType);

		try {
			
			AvailabilityStats aEndStatsPrimary = new AvailabilityStats();
			AvailabilityStats zEndStatsPrimary = new AvailabilityStats();
			
			AvailabilityStats aEndStatsSecondary = new AvailabilityStats();
			AvailabilityStats zEndStatsSecondary = new AvailabilityStats();
			
			AvailabilityStats aEndStatsTertiary = new AvailabilityStats();
			AvailabilityStats zEndStatsTertiary = new AvailabilityStats();
			
			if (sheet == null) {
				sheet = wworkbook.createSheet(fiberLink.getCustomerName()+ "-" + fiberLink.getLocation() + "(" + fiberLink.getLinkName()+")");
				link = fiberLink;
				
				int srowcount = 0;
				//Row row;
				srowcount++;

				row = (sheet.getRow(srowcount)!=null) ? sheet.getRow(srowcount): sheet.createRow(srowcount);
				
				row.createCell(0).setCellValue("AEnd Node Name");
				row.createCell(1).setCellValue(link.aEndNodeName);
				srowcount++;
				
				row = (sheet.getRow(srowcount)!=null) ? sheet.getRow(srowcount): sheet.createRow(srowcount);
				row.createCell(0).setCellValue("AEnd Node IP");
				row.createCell(1).setCellValue(link.aEndNodeIp);
				srowcount++;
				srowcount++;

				row = (sheet.getRow(srowcount)!=null) ? sheet.getRow(srowcount): sheet.createRow(srowcount);
				row.createCell(0).setCellValue("ZEnd Node Name");
				row.createCell(1).setCellValue(link.zEndNodeName);
				srowcount++;
				
				row = (sheet.getRow(srowcount)!=null) ? sheet.getRow(srowcount): sheet.createRow(srowcount);
				row.createCell(0).setCellValue("ZEnd Node IP");
				row.createCell(1).setCellValue(link.zEndNodeIp);

			} 
			
			  if (fiberLink.linkType.equals("Primary")) {
				  
				  	rowcount+=5;
			    	row = sheet.createRow(rowcount);
					row.createCell(0).setCellValue("Path Type ");
					row.createCell(1).setCellValue(fiberLink.linkType);
					row.createCell(2).setCellValue("AEnd Port");
					row.createCell(3).setCellValue(fiberLink.aEndPort);
					rowcount++;
				  
					rowcount = addRows6500HistoryAlarmsCiena(aEndStatsPrimary, wworkbook, sheet, startDateStr, endDateStr,
							fiberLink.aEndNodeIp, fiberLink.aEndNodeName, fiberLink.aEndPort, rowcount);
					
					
					rowcount+=3;
			    	row = sheet.createRow(rowcount);
					row.createCell(0).setCellValue("Path Type ");
					row.createCell(1).setCellValue(fiberLink.linkType);
					row.createCell(2).setCellValue("ZEnd Port");
					row.createCell(3).setCellValue(fiberLink.zEndPort);
					rowcount++;	
					
					rowcount = addRows6500HistoryAlarmsCiena(zEndStatsPrimary, wworkbook, sheet, startDateStr, endDateStr, fiberLink.zEndNodeIp,
							fiberLink.zEndNodeName, fiberLink.zEndPort, rowcount);

					// AvailabilityStats stats = null;
					if (aEndStatsPrimary.outageMsTotal >= zEndStatsPrimary.outageMsTotal) {
						statsPrimary = aEndStatsPrimary;
					} else {
						statsPrimary = zEndStatsPrimary;
					}
					
					double outagePercentage = 100 - (statsPrimary.outageMsTotal / totalMsOfDuration * 100);
					statsPrimary.outagePercentage = Double.valueOf(String.format("%.2f", outagePercentage));

					statsPrimary.durationInMinutes = durationInMinutes;
					statsPrimary.durationInDays = durationInDays;
					statsPrimary.linkName=sheetName;
					statsPrimary.linkType="Primary";
					statsPrimary.capacity=capacity;
					statsList.add(statsPrimary);
					
			    } else if (fiberLink.linkType.equals("Secondary")) {
			    	
			    	rowcount+=5;
			    	row = sheet.createRow(rowcount);
					row.createCell(0).setCellValue("Path Type ");
					row.createCell(1).setCellValue(fiberLink.linkType);
					row.createCell(2).setCellValue("AEnd Port");
					row.createCell(3).setCellValue(fiberLink.aEndPort);
					rowcount++;
				  
					rowcount = addRows6500HistoryAlarmsCiena(aEndStatsSecondary, wworkbook, sheet, startDateStr, endDateStr,
							fiberLink.aEndNodeIp, fiberLink.aEndNodeName, fiberLink.aEndPort, rowcount);
					
					rowcount+=3;
					row = sheet.createRow(rowcount);
					row.createCell(0).setCellValue("Path Type ");
					row.createCell(1).setCellValue(fiberLink.linkType);
					row.createCell(2).setCellValue("ZEnd Port");
					row.createCell(3).setCellValue(fiberLink.zEndPort);
					rowcount++;	
					
					rowcount = addRows6500HistoryAlarmsCiena(zEndStatsSecondary, wworkbook, sheet, startDateStr, endDateStr, fiberLink.zEndNodeIp,
							fiberLink.zEndNodeName, fiberLink.zEndPort, rowcount );

					// AvailabilityStats stats = null;
					if (aEndStatsSecondary.outageMsTotal > zEndStatsSecondary.outageMsTotal) {
						statsSecondary = aEndStatsSecondary;
					} else {
						statsSecondary = zEndStatsSecondary;
					}
					
					double outagePercentage = 100 - (statsSecondary.outageMsTotal / totalMsOfDuration * 100);
					statsSecondary.outagePercentage = Double.valueOf(String.format("%.2f", outagePercentage));;

					statsSecondary.durationInMinutes = durationInMinutes;
					statsSecondary.durationInDays = durationInDays;
					statsSecondary.linkName=sheetName;
					statsSecondary.linkType="Secondary";
					statsSecondary.capacity=capacity;
					statsList.add(statsSecondary);
					
			    } else {
			    	rowcount+=5;
			    	row = sheet.createRow(rowcount);
					row.createCell(0).setCellValue("Path Type ");
					row.createCell(1).setCellValue(fiberLink.linkType);
					row.createCell(2).setCellValue("AEnd Port");
					row.createCell(3).setCellValue(fiberLink.aEndPort);
					rowcount++;
				  
					rowcount = addRows6500HistoryAlarmsCiena(aEndStatsTertiary, wworkbook, sheet, startDateStr, endDateStr,
							fiberLink.aEndNodeIp, fiberLink.aEndNodeName, fiberLink.aEndPort, rowcount);
					
					rowcount+=3;
			    	row = sheet.createRow(rowcount);
					row.createCell(0).setCellValue("Path Type ");
					row.createCell(1).setCellValue(fiberLink.linkType);
					row.createCell(2).setCellValue("ZEnd Port");
					row.createCell(3).setCellValue(fiberLink.zEndPort);
					rowcount++;	
					
					rowcount = addRows6500HistoryAlarmsCiena(zEndStatsTertiary, wworkbook, sheet, startDateStr, endDateStr, fiberLink.zEndNodeIp,
							fiberLink.zEndNodeName, fiberLink.zEndPort, rowcount);

					// AvailabilityStats stats = null;
					if (aEndStatsTertiary.outageMsTotal > zEndStatsTertiary.outageMsTotal) {
						statsTertiary = aEndStatsTertiary;
					} else {
						statsTertiary = zEndStatsTertiary;
					}
					
					double outagePercentage = 100 - (statsTertiary.outageMsTotal / totalMsOfDuration * 100);
					statsTertiary.outagePercentage = Double.valueOf(String.format("%.2f", outagePercentage));;

					statsTertiary.durationInMinutes = durationInMinutes;
					statsTertiary.durationInDays = durationInDays;
					statsTertiary.linkName=sheetName;
					statsTertiary.linkType="Tertiary";
					statsTertiary.capacity=capacity;
					statsList.add(statsTertiary);
			    }
			

			

			
			
		}  catch (Exception e) {
			e.printStackTrace();
		}
		
		    int scolindex=0;
		
		    if (fiberLink.linkType.equals("Primary")) {
		    	scolindex=0;
		    	statsCurrent=statsPrimary;
		    } else if (fiberLink.linkType.equals("Secondary")) {
		    	scolindex=3;
		    	statsCurrent=statsSecondary;
		    } else {
		    	scolindex=6;
		    	statsCurrent = statsTertiary;
		    }
		

			int srowcount = 0;
			//Row row;

			row = (sheet.getRow(srowcount)!=null) ? sheet.getRow(srowcount): sheet.createRow(srowcount);
			//row.createCell(0).setCellValue(link.linkName);		;
			row.createCell(scolindex+4).setCellValue("Path Type");
			row.createCell(scolindex+5).setCellValue(fiberLink.linkType);
			
			
			srowcount++;

			row = (sheet.getRow(srowcount)!=null) ? sheet.getRow(srowcount): sheet.createRow(srowcount);
			
		/*	row.createCell(0).setCellValue("AEnd Node Name");
			row.createCell(1).setCellValue(link.aEndNodeName);
			row.createCell(2).setCellValue("AEnd Node IP");
			row.createCell(3).setCellValue(link.aEndNodeIp);
		*/	
			row.createCell(scolindex+4).setCellValue("Outage in HH:MM:SS");
			row.createCell(scolindex+5).setCellValue(statsCurrent.outageMsTotalStr);
			srowcount++;

		/*	row = (sheet.getRow(srowcount)!=null) ? sheet.getRow(srowcount): sheet.createRow(srowcount);
			row.createCell(0).setCellValue("ZEnd Node Name");
			row.createCell(1).setCellValue(link.zEndNodeName);
			row.createCell(2).setCellValue("ZEnd Node IP");
			row.createCell(3).setCellValue(link.zEndNodeIp);
		*/
			
		/*	row.createCell(scolindex+4).setCellValue("MTTR Slippage");
			row.createCell(scolindex+5).setCellValue(statsPrimary.mttrSlippage);
			srowcount++;

			row = (sheet.getRow(srowcount)!=null) ? sheet.getRow(srowcount): sheet.createRow(srowcount);
			row.createCell(scolindex+4).setCellValue("No.of Outages");
			row.createCell(scolindex+5).setCellValue(statsPrimary.outages);
			srowcount++;*/

			row = (sheet.getRow(srowcount)!=null) ? sheet.getRow(srowcount): sheet.createRow(srowcount);
			row.createCell(scolindex+4).setCellValue("No.of Cuts");
			row.createCell(scolindex+5).setCellValue(statsCurrent.fiberCuts);
			srowcount++;

			row = (sheet.getRow(srowcount)!=null) ? sheet.getRow(srowcount): sheet.createRow(srowcount);
			row.createCell(scolindex+4).setCellValue("No. of Flaps");
			row.createCell(scolindex+5).setCellValue(statsCurrent.fluctuations);
			srowcount++;
			
			row = (sheet.getRow(srowcount)!=null) ? sheet.getRow(srowcount): sheet.createRow(srowcount);
			row.createCell(scolindex+4).setCellValue("No. Planned Events");
			row.createCell(scolindex+5).setCellValue(statsCurrent.outagePercentage);


			row = (sheet.getRow(srowcount)!=null) ? sheet.getRow(srowcount): sheet.createRow(srowcount);
			row.createCell(scolindex+4).setCellValue("Availability");
			row.createCell(scolindex+5).setCellValue(statsCurrent.outagePercentage);

			//srowcount++;

			for (int i = 0; i < 15; i++) {
				sheet.autoSizeColumn(i);
			}

				
		
		}

		return statsList;

	}
	

	public int addRows6500HistoryAlarmsCiena(AvailabilityStats stats, XSSFWorkbook wworkbook, XSSFSheet sheet,
			String startDateStr, String endDateStr, String ip, String nodeName, String port, int rowcount) {
		if (reportFromMCP) {
			return addRows6500HistoryAlarmsCienaMCP(stats,wworkbook,sheet,startDateStr,endDateStr,ip,nodeName,port,rowcount);
		} else {
			return addRows6500HistoryAlarmsCienaSN(stats,wworkbook,sheet,startDateStr,endDateStr,ip,nodeName,port,rowcount);
		}
		
	}

	public int addRows6500HistoryAlarmsCienaSN(AvailabilityStats stats, XSSFWorkbook wworkbook, XSSFSheet sheet,
			String startDateStr, String endDateStr, String ip, String nodeName, String port, int rowcount) {

		long totalMsPerIp = 0;
		int rowCountStart = rowcount;
		
	    int skippedCount=0;
		
		String shelfslotport[] = port.split("-");

		//String optmonPort = "OPTMON-" + shelfslotport[1] + "-" + shelfslotport[2] + "-" + 8;
		String optmonPort = port;
		String esamSlot = "LIM-" + shelfslotport[1] + "-" + shelfslotport[2];


		Row row;
		Cell cell;

/*		row = sheet.createRow(rowcount);
	//	cell = row.createCell(0);
	//	cell.setCellValue(nodeName);
	//	cell = row.createCell(1);
	//	cell.setCellValue(ip);
		cell = row.createCell(0);
		cell.setCellValue("Port");
		cell = row.createCell(1);
		cell.setCellValue(optmonPort);
		rowcount++;*/

		// int rowcount=0;
		row = sheet.createRow(rowcount);
		row.createCell(0).setCellValue("Severity");
		row.createCell(1).setCellValue("Alarm Name");
		row.createCell(2).setCellValue("Alarm Source");
		row.createCell(3).setCellValue("Occurred Time");
		row.createCell(4).setCellValue("Cleared Time");
		row.createCell(5).setCellValue("Alarm Duration");
		row.createCell(6).setCellValue("Outage Duration");
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
					
					String alarmName = alarmObj.getString("conditionDescription");
					
					if ("Shutoff Threshold Crossed".equals(alarmName) ||
							"Low Received Span Loss".equals(alarmName)) {
						skippedCount++;
						continue;
					}
					
					if (port.startsWith("AMP")) {
						if ("High Received Span Loss".equals(alarmName) || 
							"Gauge Threshold Crossing Alert Summary".equals(alarmName) || 
							"Input Loss Of Signal".equals(alarmName)) {
							skippedCount++;
							continue;
						}
					}
					
				

					String nodeIp = alarmObj.getString("deviceip");
					String severity = alarmObj.getString("severity");
					String alarmSource = alarmObj.getString("aid");
					String raiseTime = alarmObj.getString("alarmDate");
					String clearTime = alarmObj.optString("clearedTime");

					row = sheet.createRow(rowcount);
					cell = row.createCell(0);
					cell.setCellValue(severity);
					cell = row.createCell(1);
					cell.setCellValue(alarmName);
					cell = row.createCell(2);
					cell.setCellValue(alarmSource);
					cell = row.createCell(3);
					cell.setCellValue(raiseTime);
					cell = row.createCell(4);
					cell.setCellValue(clearTime);

					Date alarmStartTime = null;
					Date alarmEndTime = null;
					String hms = "";
					Long durationMs = null;

					try {
						
						Date reportStartDate = startTime.getTime();
						Date reportEndDate = endTime.getTime();
						
						
						
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						alarmStartTime = sdf.parse(raiseTime);
						alarmEndTime = sdf.parse(clearTime);
						
						if (alarmStartTime.before(reportStartDate)) {
							alarmStartTime = reportStartDate;
						}
						
						if (!clearTime.equals("")) {
							alarmEndTime = sdf.parse(clearTime);

							if (alarmEndTime.after(reportEndDate)) {
								alarmEndTime = reportEndDate;
							}

						} else {
							alarmEndTime = alarmStartTime;
						}
						
						
						durationMs = alarmEndTime.getTime() - alarmStartTime.getTime();

						hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(durationMs),
								TimeUnit.MILLISECONDS.toMinutes(durationMs) % TimeUnit.HOURS.toMinutes(1),
								TimeUnit.MILLISECONDS.toSeconds(durationMs) % TimeUnit.MINUTES.toSeconds(1));

						if (durationMs < FIBER_CUT_TIME) {
							// less than 5 minutes => flucuation
							stats.fluctuations++;
						} else if (durationMs >= FIBER_CUT_TIME) {
							// greater than 5 minutes == > fiber cut
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

					cell = row.createCell(5);
					cell.setCellValue(alarmDuration);
					cell = row.createCell(6);
					cell.setCellValue("");

					rowcount++;

				}

				stats.outageMsTotal = totalMsPerIp;
				stats.outages = alarmsList.length() - skippedCount; //exclude shutoff alarms.

				String hmsOutage = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(totalMsPerIp),
						TimeUnit.MILLISECONDS.toMinutes(totalMsPerIp) % TimeUnit.HOURS.toMinutes(1),
						TimeUnit.MILLISECONDS.toSeconds(totalMsPerIp) % TimeUnit.MINUTES.toSeconds(1));

				stats.outageMsTotalStr = hmsOutage;

				// sheet.addMergedRegion(new
				// CellRangeAddress(rowCountStart,rowcount-1,8,8));
				// sheet.getRow(rowCountStart).getCell(8).setCellValue(hmsOutage);

				if ((alarmsList.length()- skippedCount) > 0) {
					// sheet.addMergedRegion(new
					// CellRangeAddress(rowCountStart,rowcount-1,8,8));
					
					if (sheet.getRow(rowCountStart+2)!=null) {

					cell = sheet.getRow(rowCountStart+2).getCell(7) != null ? sheet.getRow(rowCountStart+2).getCell(7)
							: sheet.getRow(rowCountStart+2).createCell(7);
					cell.setCellValue(hmsOutage);
					} else {
						sheet.createRow(rowCountStart+2);
						
						cell = sheet.getRow(rowCountStart+2).getCell(7) != null ? sheet.getRow(rowCountStart+2).getCell(7)
								: sheet.getRow(rowCountStart+2).createCell(7);
						cell.setCellValue(hmsOutage);
					}
				}

				
				for (int i = 0; i < 15; i++) { sheet.autoSizeColumn(i); }
				

			} else {
				logger.info("Unable to retrieve alarms from ServiceNow for " + ip);
			}

		}

		return rowcount;
	}
	
	
	public int addRows6500HistoryAlarmsCienaMCP(AvailabilityStats stats, XSSFWorkbook wworkbook, XSSFSheet sheet,
			String startDateStr, String endDateStr, String ip, String nodeName, String port, int rowcount) {

		long totalMsPerIp = 0;
		int rowCountStart = rowcount;
		
	    int skippedCount=0;
		
		String shelfslotport[] = port.split("-");

		//String optmonPort = "OPTMON-" + shelfslotport[1] + "-" + shelfslotport[2] + "-" + 8;
		String optmonPort = port;
		String esamSlot = "LIM-" + shelfslotport[1] + "-" + shelfslotport[2];


		Row row;
		Cell cell;

/*		row = sheet.createRow(rowcount);
	//	cell = row.createCell(0);
	//	cell.setCellValue(nodeName);
	//	cell = row.createCell(1);
	//	cell.setCellValue(ip);
		cell = row.createCell(0);
		cell.setCellValue("Port");
		cell = row.createCell(1);
		cell.setCellValue(optmonPort);
		rowcount++;*/

		// int rowcount=0;
		row = sheet.createRow(rowcount);
		row.createCell(0).setCellValue("Severity");
		row.createCell(1).setCellValue("Alarm Name");
		row.createCell(2).setCellValue("Alarm Source");
		row.createCell(3).setCellValue("Occurred Time");
		row.createCell(4).setCellValue("Cleared Time");
		row.createCell(5).setCellValue("Alarm Duration");
		row.createCell(6).setCellValue("Outage Duration");
		rowcount++;
		
		Date startDate = startTime.getTime();
		Date endDate = endTime.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH'%3A'mm'%3A'ss");
		startDateStr = sdf.format(startDate);
		endDateStr = sdf.format(endDate);
		
		System.out.println("Start Date "+startDateStr);
		System.out.println("End Date "+endDateStr);



		//String response = ServiceNowRestClient.getCiena6500AlarmsForAvailability(ip, optmonPort, esamSlot, startDateStr,
		//		endDateStr);
		
		
		HttpsURLConnection loginConn = mcpClient.doLogin();
		
		mcpClient.getBearerToken(loginConn);

		
		String response = mcpClient.retrieveAlarms(nodeName, optmonPort, esamSlot, startDateStr, endDateStr);

		if (response != null) {

			JSONObject responseJsonObj = new JSONObject(response);


				JSONArray alarmsList = responseJsonObj.getJSONArray("data");

				for (int i = 0; i < alarmsList.length(); i++) {
					JSONObject alarmObj = alarmsList.getJSONObject(i).getJSONObject("attributes");
					
					String alarmName = alarmObj.getString("native-condition-type-qualifier");
					
					if ("Shutoff Threshold Crossed".equals(alarmName) ||
							"Low Received Span Loss".equals(alarmName)) {
						skippedCount++;
						continue;
					}
					
					if (port.startsWith("AMP")) {
						if ("High Received Span Loss".equals(alarmName) || 
							"Gauge Threshold Crossing Alert Summary".equals(alarmName) || 
							"Input Loss Of Signal".equals(alarmName)) {
							skippedCount++;
							continue;
						}
					}
					
				

					//String nodeIp = alarmObj.getString("ip-address");
					String severity = alarmObj.getString("condition-severity");
					String alarmSource = alarmObj.getString("resource-id");
					String raiseTime = alarmObj.getString("last-raise-time");
					String clearTime = alarmObj.optString("clear-time");

					row = sheet.createRow(rowcount);
					cell = row.createCell(0);
					cell.setCellValue(severity);
					cell = row.createCell(1);
					cell.setCellValue(alarmName);
					cell = row.createCell(2);
					cell.setCellValue(alarmSource);
					cell = row.createCell(3);
					cell.setCellValue(raiseTime);
					cell = row.createCell(4);
					cell.setCellValue(clearTime);

					Date alarmStartTime = null;
					Date alarmEndTime = null;
					String hms = "";
					Long durationMs = null;

					try {
						
						Date reportStartDate = startTime.getTime();
						Date reportEndDate = endTime.getTime();
						
						
						
						sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
						alarmStartTime = sdf.parse(raiseTime);
						alarmEndTime = sdf.parse(clearTime);
						
						if (alarmStartTime.before(reportStartDate)) {
							alarmStartTime = reportStartDate;
						}
						
						if (!clearTime.equals("")) {
							alarmEndTime = sdf.parse(clearTime);

							if (alarmEndTime.after(reportEndDate)) {
								alarmEndTime = reportEndDate;
							}

						} else {
							alarmEndTime = alarmStartTime;
						}
						
						
						durationMs = alarmEndTime.getTime() - alarmStartTime.getTime();

						hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(durationMs),
								TimeUnit.MILLISECONDS.toMinutes(durationMs) % TimeUnit.HOURS.toMinutes(1),
								TimeUnit.MILLISECONDS.toSeconds(durationMs) % TimeUnit.MINUTES.toSeconds(1));

						if (durationMs < FIBER_CUT_TIME) {
							// less than 5 minutes => flucuation
							stats.fluctuations++;
						} else if (durationMs >= FIBER_CUT_TIME) {
							// greater than 5 minutes == > fiber cut
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

					cell = row.createCell(5);
					cell.setCellValue(alarmDuration);
					cell = row.createCell(6);
					cell.setCellValue("");

					rowcount++;

				}

				stats.outageMsTotal = totalMsPerIp;
				stats.outages = alarmsList.length() - skippedCount; //exclude shutoff alarms.

				String hmsOutage = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(totalMsPerIp),
						TimeUnit.MILLISECONDS.toMinutes(totalMsPerIp) % TimeUnit.HOURS.toMinutes(1),
						TimeUnit.MILLISECONDS.toSeconds(totalMsPerIp) % TimeUnit.MINUTES.toSeconds(1));

				stats.outageMsTotalStr = hmsOutage;

				// sheet.addMergedRegion(new
				// CellRangeAddress(rowCountStart,rowcount-1,8,8));
				// sheet.getRow(rowCountStart).getCell(8).setCellValue(hmsOutage);

				if ((alarmsList.length()- skippedCount) > 0) {
					// sheet.addMergedRegion(new
					// CellRangeAddress(rowCountStart,rowcount-1,8,8));
					
					if (sheet.getRow(rowCountStart+2)!=null) {

					cell = sheet.getRow(rowCountStart+2).getCell(7) != null ? sheet.getRow(rowCountStart+2).getCell(7)
							: sheet.getRow(rowCountStart+2).createCell(7);
					cell.setCellValue(hmsOutage);
					} else {
						sheet.createRow(rowCountStart+2);
						
						cell = sheet.getRow(rowCountStart+2).getCell(7) != null ? sheet.getRow(rowCountStart+2).getCell(7)
								: sheet.getRow(rowCountStart+2).createCell(7);
						cell.setCellValue(hmsOutage);
					}
				}

				
				for (int i = 0; i < 15; i++) { sheet.autoSizeColumn(i); }
				

			 

		} else {
			logger.info("Unable to retrieve alarms from Ciena MCP Server for " + ip);
		}

		return rowcount;
	}



	public void initPolling() {
		


		logger.info("initPolling called");

	}

	public void finishPolling() {

		logger.info("finishPolling called");


		logger.info("finishPolling completed");

	}
	
	
	private void computeDurationOfReport() {
		
		Date currentDate = new Date();
		polled_date = new Timestamp(currentDate.getTime());
		
		Calendar cal = Calendar.getInstance();
		
		/*Calendar startTime=null;
		Calendar endTime = null;*/
		
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
		
		String linkName="";
		String linkType="";
		String capacity="";
		
		
		public long durationInDays;
		public long durationInMinutes;
		Double outagePercentage;
		String outageMsTotalStr = "";
		long outageMsTotal = 0;
		int mttrSlippage = 0;
		int outages = 0;
		int fiberCuts = 0;
		int fluctuations = 0;
		
		@Override
		public String toString() {
			return "AvailabilityStats [linkName=" + linkName + ", linkType=" + linkType + ", durationInMinutes="
					+ durationInMinutes + ", outagePercentage=" + outagePercentage + ", outages=" + outages
					+ ", fiberCuts=" + fiberCuts + ", fluctuations=" + fluctuations + "]";
		}

		
		

	}
	
	public void initInventory() {
		
		logger.info("Report generaton started");
		
		String username = "Ciena";
		String password = "Sify@123";
		
		mcpClient = new CienaMcpClient("bpmcp1.sify.net", 443, username, password);
		
		HttpsURLConnection loginConn = mcpClient.doLogin();
		
	    mcpClient.getBearerToken(loginConn);


	}

	public void finishInvnetory() {

	}

}
