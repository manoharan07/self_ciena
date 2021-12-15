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

import com.sify.network.assets.ciena.OpticalReportOttNetworkAvailability.AvailabilityStats;
import com.sify.network.assets.core.api.AssetsAbstractService;
import com.sify.network.assets.core.api.AssetsCoreRuntime;
import com.sify.network.assets.core.api.bean.DBDetails;
import com.sify.network.assets.core.api.bean.InventoryDeviceDetails;
import com.sify.network.assets.core.api.bean.JobType;
import com.sify.network.assets.core.api.bean.PerformanceInputBean;
import com.sify.network.assets.core.api.bean.PerformanceOutputBean;

public class OpticalReportNetworkAvailability extends AssetsAbstractService { 

	static Timestamp polled_date = null;
	static Calendar startTime=null;
	static Calendar endTime = null;

	private CienaClient cienaClient = null;
	// private CienaWsClient cienaWsClient = null;

	// private static final String COMMA_DELIMITER = ",";
	
	//private boolean reportFromMCP=false;
	
	private HashMap<String,Boolean> reportFromMcpMap = new HashMap<String,Boolean>();
	
	CienaMcpClient mcpClient = null;

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("nwavailabailityreportlog");

	private DbConnection dbObj = null;

	private HashMap<String, String> deviceMapHuawei = new HashMap<String, String>();

	private static long MTTR_TIME = 4L * 60 * 60 * 1000; // 4 hrs
	private static long FIBER_CUT_TIME = 5 * 60 * 1000; // 5 minutes 

	// int summaryRowCount = 0;

	private HashMap<String, String> spanLossMap = new HashMap<String, String>();

	public OpticalReportNetworkAvailability() {
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

		ArrayList<PerformanceInputBean> roadmNetworks = new ArrayList<PerformanceInputBean>();

		PerformanceInputBean bean = new PerformanceInputBean();
		bean.setDeviceip("Chennai ROADM");
		roadmNetworks.add(bean);

		bean = new PerformanceInputBean();
		bean.setDeviceip("BGL ROADM");
		roadmNetworks.add(bean);

		bean = new PerformanceInputBean();
		bean.setDeviceip("Delhi ROADM");
		roadmNetworks.add(bean);

/*		bean = new PerformanceInputBean();
		bean.setDeviceip("Chennai Facebook");
		roadmNetworks.add(bean);
*/
		bean = new PerformanceInputBean();
		bean.setDeviceip("Mumbai ROADM");
		roadmNetworks.add(bean);

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
		logger.info("OpticalReportNetworkAvailability initiated");

		OpticalReportNetworkAvailability service = new OpticalReportNetworkAvailability();
		service.setServiceName("OpticalReportNetworkAvailability");

		DBDetails dbInventory = new DBDetails(CienaProperties.get("db.mysql.ip"), CienaProperties.get("db.mysql.port"),
				CienaProperties.get("db.mysql.username"), CienaProperties.get("db.mysql.password"),
				CienaProperties.get("db.mysql.dbname"));

		service.getDbDetailsMap().put(JobType.INVENTORY, dbInventory);

		service.setInventoryFromDevice(true);
		service.setInventoryThreadTimeout(120);

		logger.info("OpticalReportNetworkAvailability  details added");

		// providing the cron string for the jobs
		service.setAggregateCron("");
		service.setPollCron("");
		service.setInventoryCron(CienaProperties.get("optical.network.availability.report.cron"));

		AssetsCoreRuntime runtime = new AssetsCoreRuntime();
		runtime.register(service);
		logger.info("OpticalReportNetworkAvailability registered with core");

	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("config" + File.separator + "log4j_nwavailability.properties");
		OpticalReportNetworkAvailability service = new OpticalReportNetworkAvailability();
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


	//	reportFromMCP = false;
		
		reportFromMcpMap.put(input.getDeviceip(), false);
		
		String dateStr=generateReport(input.getDeviceip());

		//ServiceNowRestClient.postNetworkAvailabilityReportToServiceNow(dateStr,input.getDeviceip());
		postToServiceNow(dateStr,input.getDeviceip());
		
	//	reportFromMCP = true;
		reportFromMcpMap.put(input.getDeviceip(), true);
		
		dateStr=generateReport(input.getDeviceip());

		//ServiceNowRestClient.postNetworkAvailabilityReportToServiceNow(dateStr,"OTT");
		postToServiceNow(dateStr,input.getDeviceip());


		return list;
	}
	
	private void postToServiceNow(String dateStr, String roadmName) {
		String roadmNameForFile = "";
		String fileName = "";

		if (reportFromMcpMap.get(roadmName)) {
			roadmNameForFile = roadmName.replaceAll(" ", "_");
			fileName = roadmNameForFile + "_NW_Availability_Report_MCP_" + dateStr + ".xlsx";

			ServiceNowRestClient.postNetworkAvailabilityReportToServiceNow(dateStr, roadmName, fileName);
		} else {
			roadmNameForFile = roadmName.replaceAll(" ", "_");
			fileName = roadmNameForFile + "_NW_Availability_Report_" + dateStr + ".xlsx";

			ServiceNowRestClient.postNetworkAvailabilityReportToServiceNow(dateStr, roadmName, fileName);
		}

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
			System.out.println("Got error in https://" + ip + ":8443/api/v1/datastore/oscs/osc/" + port);
			e.printStackTrace(System.out);
		}

		return spanLoss;
	}

	public String generateReport(String roadmName) {

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
			
			String excelPath = "";

			String roadmNameForFile = roadmName.replaceAll(" ", "_");

			String filePath = CienaProperties.get("optical.network.availability.report.folder");
			
			if (reportFromMcpMap.get(roadmName)) {
				excelPath = filePath + File.separator + roadmNameForFile + "_NW_Availability_Report_MCP_" + dateStr
						+ ".xlsx";
			} else {
				excelPath = filePath + File.separator + roadmNameForFile + "_NW_Availability_Report_" + dateStr
						+ ".xlsx";
			}

			FileOutputStream fileOut = new FileOutputStream(excelPath);
			XSSFWorkbook workbook = createWorkBook();

			generateSheets(workbook, roadmName, startDateStr, endDateStr);

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

	private void generateSheets(XSSFWorkbook workbook, String roadmName, String startDate, String endDate) {

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
		row.createCell(0).setCellValue("Span Details");
		;
		row.createCell(1).setCellValue("Outage in HH:MM:SS");
		row.createCell(2).setCellValue("MTTR Slippage");
		row.createCell(3).setCellValue("No.of Outages");
		row.createCell(4).setCellValue("No.of FiberCuts");
		row.createCell(5).setCellValue("No. of Fluctuations");
		row.createCell(6).setCellValue("Span degraded beyond Designed BOL");
		row.createCell(7).setCellValue("Total Outage in Minutes");
		row.createCell(8).setCellValue("Availability in %");
		row.createCell(9).setCellValue("Date of Planned Maintenance Activity");
		row.createCell(10).setCellValue("No. Maintenance Activity (Planned)");
		summaryRowCount++;

		if (roadmName.equals("Mumbai ROADM")) {

			deviceMapHuawei = loadDeviceMapHuawei();

			ArrayList<PerformanceInputBean> fiberLinks = loadFiberLinksHuawei(roadmName);

			generateSpanLossSheetHuawei(workbook, roadmName, dateStr);
			generateIncidentsSheet(workbook, roadmName, dateStr);

			for (PerformanceInputBean fiberLink : fiberLinks) {
				AvailabilityStats stats = createHistoryAlarmsHuawei(workbook, dateStr, (HuaweiFiberLinkBean) fiberLink);

				HuaweiFiberLinkBean link = (HuaweiFiberLinkBean) fiberLink;

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

		} else {

			ArrayList<PerformanceInputBean> fiberLinks = loadFiberLinksCiena(roadmName);

			generateSpanLossSheetCiena(workbook, roadmName, dateStr);
			generateIncidentsSheet(workbook, roadmName, dateStr);

			for (PerformanceInputBean fiberLink : fiberLinks) {
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


		}
		
		
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
					try {
					String key = nodeNameArray[2];
					deviceMap.put(key, elementId);
					} catch(Exception e) {
						logger.info("Unable to parse "+nodeName);
					}
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
					logger.error("Got error while generating report",e);
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					logger.error("Got error while generating report",e);
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
					logger.error("Got error while generating report",e);
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					logger.error("Got error while generating report",e);
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
					logger.error("Got error while generating report",e);
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					logger.error("Got error while generating report",e);
				}
			}
			dbObj.closeConnection(connection);
		}

		return fiberLinks;
	}

	public void generateSpanLossSheetHuawei(XSSFWorkbook wworkbook, String roadmName, String dateStr) {
		try {
			XSSFSheet sheet = wworkbook.createSheet("Span Loss");
			Row row;
			Cell cell;
			int rowcount = 0;
			row = sheet.createRow(rowcount);
			row.createCell(0).setCellValue("Link Details");
			;
			row.createCell(1).setCellValue("Source Node");
			row.createCell(2).setCellValue("Source Port");
			row.createCell(3).setCellValue("Sink Node");
			row.createCell(4).setCellValue("Sink Port");

			row.createCell(5).setCellValue("Design EOL");
			row.createCell(6).setCellValue("Span Loss");
			row.createCell(7).setCellValue("Attenuation");
			row.createCell(8).setCellValue("Span Loss - Attenuation");
			row.createCell(9).setCellValue("Span Loss Degarded");
			rowcount++;

			Connection connection = null;
			ResultSet result = null;
			Statement stmt = null;

			DbConnection dbObj = new DbConnection();
			connection = dbObj.getDBConnectionHuawei();

			String query = "SELECT device_type, roadm_name, link_name, aend_source_node_name, aend_source_port, aend_sink_node_name, aend_sink_port, "
					+ "zend_source_node_name, zend_source_port, zend_sink_node_name, zend_sink_port, design_optical_length_km, design_eol, "
					+ "aend_span_loss_actual, zend_span_loss_actual, aend_sink_attenuation, zend_sink_attenuation, "
					+ "aend_span_loss, zend_span_loss, aend_span_degraded, zend_span_degraded, collection_date, updated_time FROM HuwaeiU2000.span_loss "
					+ "where collection_date='" + dateStr + "' and roadm_name='" + roadmName + "'";
			logger.info(query);
			logger.info("query is " + query);
			System.out.println("########## "+ query);

			stmt = connection.createStatement();
			result = stmt.executeQuery(query);

			while (result.next()) {

				String linkName = result.getString("link_name");

				logger.info("preparing excel values for " + linkName);

				Double dolkm = null;
				Double bol = null;

				dolkm = Double.valueOf(result.getString("design_optical_length_km"));
				bol = Double.valueOf(result.getString("design_eol"));

				String aEndSourceNode = result.getString("aend_source_node_name");
				String aEndSourcePort = result.getString("aend_source_port");

				String aEndSinkNode = result.getString("aend_sink_node_name");
				String aEndSinkPort = result.getString("aend_sink_port");

				Double aendSpanlossActual = null;
				if (!result.getString("aend_span_loss_actual").equals("null")) {
					aendSpanlossActual = Double.valueOf(result.getString("aend_span_loss_actual"));
				}

				Double aendSinkAttenuation = null;
				if (!result.getString("aend_sink_attenuation").equals("null")) {
					aendSinkAttenuation = Double.valueOf(result.getString("aend_sink_attenuation"));
				}

				Double aendSpanloss = null;
				if (!result.getString("aend_span_loss").equals("null")) {
					aendSpanloss = Double.valueOf(result.getString("aend_span_loss"));
				}
				boolean aendSpanDegraded = result.getBoolean("aend_span_degraded");

				row = sheet.createRow(rowcount);
				cell = row.createCell(0);
				cell.setCellValue(linkName);
				cell = row.createCell(1);
				cell.setCellValue(aEndSourceNode);
				cell = row.createCell(2);
				cell.setCellValue(aEndSourcePort);
				cell = row.createCell(3);
				cell.setCellValue(aEndSinkNode);
				cell = row.createCell(4);
				cell.setCellValue(aEndSinkPort);
				cell = row.createCell(5);
				cell.setCellValue(bol);
				cell = row.createCell(6);
				cell.setCellValue(aendSpanlossActual);
				cell = row.createCell(7);
				cell.setCellValue(aendSinkAttenuation);
				cell = row.createCell(8);
				cell.setCellValue(aendSpanloss);
				cell = row.createCell(9);

				if (aendSpanDegraded) {
					cell.setCellValue("YES");
					// cell.setCellStyle(style1);
				} else {
					cell.setCellValue("NO");
				}

				rowcount++;

				String zEndSourceNode = result.getString("zend_source_node_name");
				String zEndSourcePort = result.getString("zend_source_port");

				String zEndSinkNode = result.getString("zend_sink_node_name");
				String zEndSinkPort = result.getString("zend_sink_port");

				Double zendSpanlossActual = null;
				if (!result.getString("zend_span_loss_actual").equals("null")) {
					zendSpanlossActual = Double.valueOf(result.getString("zend_span_loss_actual"));
				}

				Double zendSinkAttenuation = null;
				if (!result.getString("zend_sink_attenuation").equals("null")) {
					zendSinkAttenuation = Double.valueOf(result.getString("zend_sink_attenuation"));
				}

				Double zendSpanloss = null;
				if (!result.getString("zend_span_loss").equals("null")) {
					zendSpanloss = Double.valueOf(result.getString("zend_span_loss"));
				}
				boolean zendSpanDegraded = result.getBoolean("zend_span_degraded");

				row = sheet.createRow(rowcount);
				cell = row.createCell(0);
				cell.setCellValue(linkName);
				cell = row.createCell(1);
				cell.setCellValue(zEndSourceNode);
				cell = row.createCell(2);
				cell.setCellValue(zEndSourcePort);
				cell = row.createCell(3);
				cell.setCellValue(zEndSinkNode);
				cell = row.createCell(4);
				cell.setCellValue(zEndSinkPort);
				cell = row.createCell(5);
				cell.setCellValue(bol);
				cell = row.createCell(6);
				cell.setCellValue(zendSpanlossActual);
				cell = row.createCell(7);
				cell.setCellValue(zendSinkAttenuation);
				cell = row.createCell(8);
				cell.setCellValue(zendSpanloss);
				cell = row.createCell(9);

				if (zendSpanDegraded) {
					cell.setCellValue("YES");
					// cell.setCellStyle(style1);
				} else {
					cell.setCellValue("NO");
				}
				
				if (aendSpanDegraded || zendSpanDegraded) {
					spanLossMap.put(linkName, "YES");
				} else {
					spanLossMap.put(linkName, "NO");
				}

				rowcount++;
			}

			for (int i = 0; i < 11; i++) {
				sheet.autoSizeColumn(i);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Got error while generating report",e);
		}
	}

	public void generateSpanLossSheetCiena(XSSFWorkbook wworkbook, String roadmName, String dateStr) {
		try {
			XSSFSheet sheet = wworkbook.createSheet("Span Loss");
			Row row;
			Cell cell;
			int rowcount = 0;
			row = sheet.createRow(rowcount);
			row.createCell(0).setCellValue("Link Details");
			;
			row.createCell(1).setCellValue("Node Name");
			row.createCell(2).setCellValue("ESAM Port");
			row.createCell(3).setCellValue("Design OL(KM)");
			row.createCell(4).setCellValue("Design BOL");
			row.createCell(5).setCellValue("Span Loss " + dateStr);

			if (roadmName.equals("Chennai Facebook")) {
				row.createCell(6).setCellValue("Attenuation ");
				row.createCell(7).setCellValue("Span Loss - Attenuation");
				row.createCell(8).setCellValue("Span Loss Degarded");
				rowcount++;

			} else {

				row.createCell(6).setCellValue("Span Loss Degarded");
				rowcount++;
			}

			Connection connection = null;
			ResultSet result = null;
			Statement stmt = null;

			DbConnection dbObj = new DbConnection();
			connection = dbObj.getDBConnection();
			String query = "SELECT device_type, roadm_name, link_name, aend_node_name, aend_node_ip, aend_port, "
					+ "zend_node_name, zend_node_ip, zend_port, design_optical_length_km, design_bol, "
					+ "aend_span_loss, zend_span_loss, aend_span_degraded, zend_span_degraded, collection_date, updated_time FROM optical_reports.span_loss "
					+ "where collection_date='" + dateStr + "' and roadm_name='" + roadmName + "'";
			logger.info(query);
			logger.info("query is " + query);

			stmt = connection.createStatement();
			result = stmt.executeQuery(query);

			Double fbAttenuation = Double.parseDouble(CienaProperties.get("ciena.spanlossreport.fb.attenuation.value"));

			while (result.next()) {
				Double aEndDol = null;
				Double zEndDol = null;
				Double aEndBol = null;
				Double zEndBol = null;

				Double dolkm = null;
				Double bol = null;

				boolean aendSpanDegraded = result.getBoolean("aend_span_degraded");
				boolean zendSpanDegraded = result.getBoolean("zend_span_degraded");

				Double aendSpanloss = null;
				if (!result.getString("aend_span_loss").equals("null")) {
					aendSpanloss = Double.valueOf(result.getString("aend_span_loss"));
				}

				Double zendSpanloss = null;
				if (!result.getString("zend_span_loss").equals("null")) {
					zendSpanloss = Double.valueOf(result.getString("zend_span_loss"));
				}

				if (roadmName.equals("Chennai Facebook")) {
					String dolStr = result.getString("design_optical_length_km");
					String dolArray[] = dolStr.split(":");
					aEndDol = Double.valueOf(dolArray[0]);
					zEndDol = Double.valueOf(dolArray[1]);

					String bolStr = result.getString("design_bol");
					String bolArray[] = bolStr.split(":");
					aEndBol = Double.valueOf(bolArray[0]);
					zEndBol = Double.valueOf(bolArray[1]);

					dolkm = aEndDol;
					bol = aEndBol;
				} else {
					dolkm = Double.valueOf(result.getString("design_optical_length_km"));
					bol = Double.valueOf(result.getString("design_bol"));
				}

				String linkName = result.getString("link_name");
				String nodeName = result.getString("aend_node_name");
				String port = result.getString("aend_port");

				logger.info("preparing excel values for " + linkName);

				row = sheet.createRow(rowcount);
				cell = row.createCell(0);
				cell.setCellValue(linkName);
				cell = row.createCell(1);
				cell.setCellValue(nodeName);
				cell = row.createCell(2);
				cell.setCellValue(port);
				cell = row.createCell(3);
				cell.setCellValue(dolkm);
				cell = row.createCell(4);
				cell.setCellValue(bol);
				cell = row.createCell(5);
				if (aendSpanloss != null) {
					cell.setCellValue(aendSpanloss);
				}

				if (roadmName.equals("Chennai Facebook")) {

					double spanlossMinusAttenuation = aendSpanloss - fbAttenuation;

					cell = row.createCell(6);
					cell.setCellValue(fbAttenuation);
					cell = row.createCell(7);
					cell.setCellValue(spanlossMinusAttenuation);
					cell = row.createCell(8);

					if (spanlossMinusAttenuation >= bol) {
						aendSpanDegraded = true;
					} else {
						aendSpanDegraded = false;
					}

				} else {
					cell = row.createCell(6);
				}

				if (aendSpanDegraded) {
					cell.setCellValue("YES");
					// cell.setCellStyle(style1);
					spanLossMap.put(linkName, "YES");
				} else {
					cell.setCellValue("NO");
					spanLossMap.put(linkName, "NO");
				}

				rowcount++;

				if (roadmName.equals("Chennai Facebook")) {
					dolkm = zEndDol;
					bol = zEndBol;
				}

				// linkName = result.getString("link_name");
				nodeName = result.getString("zend_node_name");
				port = result.getString("zend_port");
				// dolkm = result.getString("design_optical_length_km");
				// bol = result.getString("design_bol");

				row = sheet.createRow(rowcount);
				cell = row.createCell(0);
				cell.setCellValue(linkName);
				cell = row.createCell(1);
				cell.setCellValue(nodeName);
				cell = row.createCell(2);
				cell.setCellValue(port);
				cell = row.createCell(3);
				cell.setCellValue(dolkm);
				cell = row.createCell(4);
				cell.setCellValue(bol);
				cell = row.createCell(5);
				if (zendSpanloss != null) {
					cell.setCellValue(zendSpanloss);
				}

				if (roadmName.equals("Chennai Facebook")) {

					double spanlossMinusAttenuation = zendSpanloss - fbAttenuation;

					cell = row.createCell(6);
					cell.setCellValue(fbAttenuation);
					cell = row.createCell(7);
					cell.setCellValue(spanlossMinusAttenuation);
					cell = row.createCell(8);

					if (spanlossMinusAttenuation >= bol) {
						zendSpanDegraded = true;
					} else {
						zendSpanDegraded = false;
					}

				} else {
					cell = row.createCell(6);
				}

				if (zendSpanDegraded) {
					cell.setCellValue("YES");
					// cell.setCellStyle(style1);
				} else {
					cell.setCellValue("NO");
				}

				sheet.addMergedRegion(new CellRangeAddress(rowcount - 1, rowcount, 0, 0));

				if (!roadmName.equals("Chennai Facebook")) {
					sheet.addMergedRegion(new CellRangeAddress(rowcount - 1, rowcount, 3, 3));
					sheet.addMergedRegion(new CellRangeAddress(rowcount - 1, rowcount, 4, 4));
				}
				
				if (aendSpanDegraded || zendSpanDegraded) {
					spanLossMap.put(linkName, "YES");
				} else {
					spanLossMap.put(linkName, "NO");
				}

				rowcount++;
			}

			if (!roadmName.equals("Chennai Facebook")) {
				for (int i = 0; i < 7; i++) {
					sheet.autoSizeColumn(i);
				}
			} else {
				for (int i = 0; i < 9; i++) {
					sheet.autoSizeColumn(i);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void generateIncidentsSheet(XSSFWorkbook wworkbook, String roadmName, String dateStr) {

/*		Calendar cal = Calendar.getInstance();

		Calendar startTime;
		startTime = (Calendar) cal.clone();

		startTime.set(Calendar.MONTH, -2);
		startTime.set(Calendar.DAY_OF_MONTH, 1);
		startTime.set(Calendar.HOUR_OF_DAY, 0);
		startTime.set(Calendar.MINUTE, 0);
		startTime.set(Calendar.SECOND, 0);

		Calendar endTime;
		endTime = (Calendar) cal.clone();
		endTime.set(Calendar.DAY_OF_MONTH, endTime.getActualMaximum(Calendar.DAY_OF_MONTH));
		endTime.set(Calendar.HOUR_OF_DAY, 23);
		endTime.set(Calendar.MINUTE, 59);
		endTime.set(Calendar.SECOND, 59);
*/
		Date startDate = startTime.getTime();
		Date endDate = endTime.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startDateStr = sdf.format(startDate);
		String endDateStr = sdf.format(endDate);

		SimpleDateFormat sdfMonth = new SimpleDateFormat("MMM-yyyy");
		String monthStr = sdf.format(startDate);

		int rowcount = 11;

		System.out.println("Retrieving Incidents from ServiceNow between " + startDateStr + " and " + endDateStr);

		XSSFSheet sheet = wworkbook.createSheet("SR for Fiber Outage");

		long totalMsPerIp = 0;
		int rowCountStart = rowcount;

		Row row;
		Cell cell;

		// int rowcount=0;
		row = sheet.createRow(rowcount);
		row.createCell(0).setCellValue("Tac Number");
		;
		row.createCell(1).setCellValue("Date of Request");
		row.createCell(2).setCellValue("Occured On");
		row.createCell(3).setCellValue("Cleared On");
		row.createCell(4).setCellValue("Alarm Duration");
		row.createCell(5).setCellValue("Problem Incident Identified");
		row.createCell(6).setCellValue("Comments");
		row.createCell(7).setCellValue("Lat. Long.");
		rowcount++;

		String taskGroup = null;

		if (roadmName.equals("Chennai ROADM")) {
			taskGroup = "FSM_CHN";
		} else if (roadmName.equals("BGL ROADM")) {
			taskGroup = "FSM_BLR";
		} else if (roadmName.equals("Delhi ROADM")) {
			taskGroup = "FSM_DEL";
		} else if (roadmName.equals("Mumbai ROADM")) {
			taskGroup = "FSM_MUM";
		}

		String response = ServiceNowRestClient.getIncidentsFromServiceNow(taskGroup, startDateStr, endDateStr);

		if (response != null) {

			JSONObject responseJsonObj = new JSONObject(response);

			JSONObject jsonObj = responseJsonObj.getJSONObject("result");

			if (jsonObj != null && jsonObj.getString("Result").equals("Success")) {
				JSONArray incidentsList = jsonObj.getJSONObject("Data").getJSONArray("incidents");

				for (int i = 0; i < incidentsList.length(); i++) {
					JSONObject incidentObj = incidentsList.getJSONObject(i);

					String incidentNumber = incidentObj.optString("number");
					String occurredDate = incidentObj.optString("openedDate");
					String clearedDate = incidentObj.optString("closedDate");
					String alarmDuration = incidentObj.optString("duration");
					String problemIncident = incidentObj.optString("shortDescription");

					String latlong = "";

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

					row = sheet.createRow(rowcount);
					cell = row.createCell(0);
					cell.setCellValue(incidentNumber);
					cell = row.createCell(1);
					cell.setCellValue("");
					cell = row.createCell(2);
					cell.setCellValue(occurredDate);
					cell = row.createCell(3);
					cell.setCellValue(clearedDate);
					// cell = row.createCell(4);
					// cell.setCellValue(alarmDuration);
					cell = row.createCell(5);
					cell.setCellValue(problemIncident);
					cell = row.createCell(6);
					cell.setCellValue(comments);
					cell = row.createCell(7);
					cell.setCellValue(latlong);

					Date occurredTime = null;
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

					alarmDuration = hms;

					cell = row.createCell(4);
					cell.setCellValue(alarmDuration);

					rowcount++;

				}

				for (int i = 0; i < 17; i++) {
					sheet.autoSizeColumn(i);
				}

			} else {
				System.out.println("Unable to retrieve incidents from ServiceNow for " + roadmName);
			}

		}

	}

	public AvailabilityStats create6500HistoryAlarmsCiena(XSSFWorkbook wworkbook, String dateStr,
			CienaFiberLinkBean fiberLink) {

		AvailabilityStats stats = null;

/*		Calendar cal = Calendar.getInstance();
		
		Calendar startTime=null;
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

			endTime = (Calendar) cal.clone(); endTime.getTime();
			
			//endTime.set(Calendar.DAY_OF_MONTH, endTime.getActualMaximum(Calendar.DAY_OF_MONTH));
			endTime.add(Calendar.DAY_OF_MONTH,-1);
			//endTime.set(Calendar.MONTH, startTime.get(Calendar.MONTH)); // logically not needed but the previous line sets month to preivous month.need to debug.
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

			AvailabilityStats aEndStats = new AvailabilityStats();
			AvailabilityStats zEndStats = new AvailabilityStats();

			rowcount = addRows6500HistoryAlarmsCiena(fiberLink.roadmName, aEndStats, wworkbook, sheet, startDateStr, endDateStr,
					fiberLink.aEndNodeIp, fiberLink.aEndNodeName, fiberLink.aEndPort, rowcount);
			addRows6500HistoryAlarmsCiena(fiberLink.roadmName,zEndStats, wworkbook, sheet, startDateStr, endDateStr, fiberLink.zEndNodeIp,
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
	
	public int addRows6500HistoryAlarmsCiena(String roadmName,AvailabilityStats stats, XSSFWorkbook wworkbook, XSSFSheet sheet,
			String startDateStr, String endDateStr, String nodeIP, String nodeName, String port, int rowcount) {
		if (reportFromMcpMap.get(roadmName)) {
			return addRows6500HistoryAlarmsCienaMCP(stats,wworkbook,sheet,startDateStr,endDateStr,nodeIP,nodeName,port,rowcount);
		} else {
			return addRows6500HistoryAlarmsCienaSN(stats,wworkbook,sheet,startDateStr,endDateStr,nodeIP,nodeName,port,rowcount);
		}
		
	}


	public int addRows6500HistoryAlarmsCienaSN(AvailabilityStats stats, XSSFWorkbook wworkbook, XSSFSheet sheet,
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
		
		int ignoredCount=0;

		if (response != null) {

			JSONObject responseJsonObj = new JSONObject(response);

			JSONObject jsonObj = responseJsonObj.getJSONObject("result");

			if (jsonObj != null && jsonObj.getString("Result").equals("Success")) {
				JSONArray alarmsList = jsonObj.getJSONObject("Data").getJSONArray("alarms");

				for (int i = 0; i < alarmsList.length(); i++) {
					JSONObject alarmObj = alarmsList.getJSONObject(i);
					
					String alarmName = alarmObj.optString("conditionDescription");
					
					if (alarmName.equals("Loss Of Signal") || alarmName.equals("Low Received Span Loss")) {
						ignoredCount++;
						continue;
						}

					String nodeIp = alarmObj.getString("deviceip");
					String severity = alarmObj.getString("severity");
					
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

					Date alarmStartTime = null;
					Date alarmEndTime = null;
					String hms = "";
					Long durationMs = null;

					try {
						
						Date reportStartDate = startTime.getTime();
						Date reportEndDate = endTime.getTime();
						
					
						
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						alarmStartTime = sdf.parse(raiseTime);
						
						if (!clearTime.equals("")) {
							alarmEndTime = sdf.parse(clearTime);

							if (alarmEndTime.after(reportEndDate)) {
								alarmEndTime = reportEndDate;
							}

						} else {
							alarmEndTime = reportEndDate;
						}
						
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
						logger.error("Got error while parsing alarm date ",e);
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
				stats.outages = alarmsList.length()-ignoredCount;

				String hmsOutage = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(totalMsPerIp),
						TimeUnit.MILLISECONDS.toMinutes(totalMsPerIp) % TimeUnit.HOURS.toMinutes(1),
						TimeUnit.MILLISECONDS.toSeconds(totalMsPerIp) % TimeUnit.MINUTES.toSeconds(1));

				stats.outageMsTotalStr = hmsOutage;

				// sheet.addMergedRegion(new
				// CellRangeAddress(rowCountStart,rowcount-1,8,8));
				// sheet.getRow(rowCountStart).getCell(8).setCellValue(hmsOutage);

				if ((alarmsList.length()-ignoredCount) > 0) {
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
	
	public int addRows6500HistoryAlarmsCienaMCP(AvailabilityStats stats, XSSFWorkbook wworkbook, XSSFSheet sheet,
			String startDateStr, String endDateStr, String nodeIP, String nodeName, String port, int rowcount) {

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
		cell.setCellValue(nodeIP);
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


		//String response = ServiceNowRestClient.getCiena6500AlarmsForAvailability(ip, optmonPort, esamSlot, startDateStr,
		//		endDateStr);
		
	//	HttpsURLConnection loginConn = mcpClient.doLogin();
		
	//	mcpClient.getBearerToken(loginConn);
		
		Date startDate = startTime.getTime();
		Date endDate = endTime.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH'%3A'mm'%3A'ss");
		startDateStr = sdf.format(startDate);
		endDateStr = sdf.format(endDate);
		
		System.out.println("Start Date "+startDateStr);
		System.out.println("End Date "+endDateStr);


		
		String response = mcpClient.retrieveAlarms(nodeName, optmonPort, esamSlot, startDateStr, endDateStr);

		System.out.println("MCP Response for "+nodeIP+", "+response);
		int ignoredCount=0;

		if (response != null) {

			JSONObject responseJsonObj = new JSONObject(response);


				JSONArray alarmsList = responseJsonObj.getJSONArray("data");

				for (int i = 0; i < alarmsList.length(); i++) {
					JSONObject alarmObj = alarmsList.getJSONObject(i).getJSONObject("attributes");
					
					String alarmName = alarmObj.optString("native-condition-type-qualifier");
					
					if (alarmName.equals("Loss Of Signal") || alarmName.equals("Low Received Span Loss")) {
						ignoredCount++;
						continue;
						}

					
					//String nodeIp = alarmObj.optString("ip-address");
					String nodeIp = nodeIP;
					String severity = alarmObj.optString("condition-severity");
					String alarmSource = alarmObj.optString("resource-id");
					String raiseTime = alarmObj.optString("last-raise-time");
					String clearTime = alarmObj.optString("clear-time");
					
					if (nodeIp.isEmpty()) {
						System.out.println("IP address is empty in "+alarmObj);
					}


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

					Date alarmStartTime = null;
					Date alarmEndTime = null;
					String hms = "";
					Long durationMs = null;

					try {
						
						Date reportStartDate = startTime.getTime();
						Date reportEndDate = endTime.getTime();
						
					
						
						sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
						alarmStartTime = sdf.parse(raiseTime);
						
						if (!clearTime.equals("")) {
							alarmEndTime = sdf.parse(clearTime);

							if (alarmEndTime.after(reportEndDate)) {
								alarmEndTime = reportEndDate;
							}

						} else {
							alarmEndTime = reportEndDate;
						}
						
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
						logger.error("Got error while parsing alarm date ",e);
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
				stats.outages = alarmsList.length()-ignoredCount;

				String hmsOutage = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(totalMsPerIp),
						TimeUnit.MILLISECONDS.toMinutes(totalMsPerIp) % TimeUnit.HOURS.toMinutes(1),
						TimeUnit.MILLISECONDS.toSeconds(totalMsPerIp) % TimeUnit.MINUTES.toSeconds(1));

				stats.outageMsTotalStr = hmsOutage;

				// sheet.addMergedRegion(new
				// CellRangeAddress(rowCountStart,rowcount-1,8,8));
				// sheet.getRow(rowCountStart).getCell(8).setCellValue(hmsOutage);

				if ((alarmsList.length()-ignoredCount) > 0) {
					// sheet.addMergedRegion(new
					// CellRangeAddress(rowCountStart,rowcount-1,8,8));

					cell = sheet.getRow(rowCountStart+2).getCell(8) != null ? sheet.getRow(rowCountStart+2).createCell(8)
							: sheet.getRow(rowCountStart+2).getCell(8);
					cell.setCellValue(hmsOutage);
				}

				
				for (int i = 0; i < 15; i++) { sheet.autoSizeColumn(i); }
				

			} else {
				System.out.println("Unable to retrieve alarms from Ciena MCP for " + nodeIP);
			}
		return rowcount;
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
			logger.error("Got error while generating report",e);
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
					logger.error("Got error while generating report",e);
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					logger.error("Got error while generating report",e);
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

					//System.out.println("ShelfName : " + shelfName);

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

					Date alarmStartTime = null;
					Date alarmEndTime = null;
					String hms = "";
					Long durationMs = null;

					try {
						
						Date reportStartDate = startTime.getTime();
						Date reportEndDate = endTime.getTime();
						
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						alarmStartTime = sdf.parse(raiseTime);
						if (!clearTime.isEmpty()) {
							alarmEndTime = sdf.parse(clearTime);
						} else {
							alarmEndTime = alarmStartTime;
						}
						
						if (alarmStartTime.before(reportStartDate)) {
							alarmStartTime = reportStartDate;
						}
						
						if (alarmEndTime.after(reportEndDate)) {
							alarmEndTime = reportEndDate;
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
						logger.error("Got error while generating report",e);
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
		initInventory();

	}
	
	public void finishPolling() {

		logger.info("finishPolling called");


		logger.info("finishPolling completed");

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
