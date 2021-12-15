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
import java.util.Date;
import java.util.Map;

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

public class CienaReportServiceSpanLoss extends AssetsAbstractService {

	static Timestamp polled_date = null;

	private CienaClient cienaClient = null;
	//private CienaWsClient cienaWsClient = null;

	//private static final String COMMA_DELIMITER = ",";

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("spanlossreportlog");

	private DbConnection dbObj = null;


	public CienaReportServiceSpanLoss() {
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

		ArrayList<PerformanceInputBean> fiberLinks = new ArrayList<PerformanceInputBean>();
		Connection connection = null;
		ResultSet result = null;
		Statement stmt = null;

		try {
			connection = dbObj.getDBConnection();
			String query = "SELECT roadm_name, link_name, aend_node_name, aend_node_ip, aend_port, zend_node_name, zend_node_ip, zend_port, design_optical_length_km, design_bol FROM optical_reports.fiber_links";
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

				String roadmName = result.getString("roadm_name");
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

	@Override
	public ArrayList<PerformanceInputBean> loadDeviceIpListMemory() {
		
		return null;
	}

	@Override
	public ArrayList<PerformanceInputBean> loadInterfacesList() {
		
		ArrayList<PerformanceInputBean> fiberLinks = new ArrayList<PerformanceInputBean>();
		
		logger.info("Retrieving fiber link details from ServiceNow");
		
		String linksResponse = ServiceNowRestClient.getFiberLinksFromServiceNow();
		
		logger.info("Fiber links retrieved from ServiceNow "+linksResponse);
		
		JSONObject jsonResponse = new JSONObject(linksResponse);
		
		JSONObject resultObj = jsonResponse.getJSONObject("result");
		if (resultObj.getString("Result").equals("Success")) {
			
			JSONArray linksArray = resultObj.getJSONObject("Data").getJSONArray("links");
			
			for(int i=0;i<linksArray.length();i++) {
				JSONObject link = linksArray.getJSONObject(i);
				
				String lmsId = link.getString("lmsId");
				
				String aEndNodeName = link.getString("aEndDeviceName");
				String aEndNodeIp = link.getString("aEndDeviceIP");
				String aEndPort = link.getString("aEndPort");

				String zEndNodeName = link.getString("zEndDeviceName");
				String zEndNodeIp = link.getString("zEndDeviceIP");
				String zEndPort = link.getString("zEndPort");

				String linkName = link.getString("linkName");
				String opticalLength = link.optString("distance");
				//System.out.println(lmsId +", " +opticalLength);
				
				if (opticalLength.equals("0") || link.getString("aEndPort").isEmpty() || link.getString("zEndPort").isEmpty()) {
					logger.info("Skipped "+lmsId+" for spanloss monitoring as distance/port is not available in ServiceNow");
				} else {
				
				CienaFiberLinkBean input = new CienaFiberLinkBean();

			
				input.setaEndNodeName(aEndNodeName);
				input.setaEndNodeIp(aEndNodeIp);
				input.setaEndPort(aEndPort);

				input.setzEndNodeName(zEndNodeName);
				input.setzEndNodeIp(zEndNodeIp);
				input.setzEndPort(zEndPort);

				input.setLinkName(linkName);
				input.setDesignedOpticalLength(opticalLength);
				
				Double opticalLengthKms = Double.parseDouble(opticalLength) / 1000;
				
				Double designBOL =  (opticalLengthKms * 0.5f) + (0.5f * 2);
				
				input.setDesignedBOL(String.valueOf(designBOL));

				input.setDeviceip(lmsId);
				
				input.setLmsId(lmsId);

				fiberLinks.add(input);
				}

			}
			
			logger.info("FiberLinks with port values are "+fiberLinks);
			
		} else {
			System.out.println("Unable to retrieve fiber links from ServiceNow, " + resultObj.getString("Reason"));
		}

		
		return fiberLinks;
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
		logger.info("ciena spanloss report service initiated");

		CienaReportServiceSpanLoss service = new CienaReportServiceSpanLoss();
		service.setServiceName("CienaReportServiceSpanLoss");
		// service.setLivePollThreadCound(versaProperties.get("thread.count",
		// 10));
		// logger.info("versa total thread is " +
		// versaProperties.get("thread.count",
		// 10));

		// inventory

		DBDetails dbInventory = new DBDetails(CienaProperties.get("db.mysql.ip"), CienaProperties.get("db.mysql.port"),
				CienaProperties.get("db.mysql.username"), CienaProperties.get("db.mysql.password"),
				CienaProperties.get("db.mysql.dbname"));

		service.getDbDetailsMap().put(JobType.INVENTORY, dbInventory);
		service.getDbDetailsMap().put(JobType.POLL, dbInventory);

		service.setInventoryFromDevice(true);
		service.setInventoryThreadTimeout(30);

		logger.info("ciena report service  details added");

		// providing the cron string for the jobs
		service.setAggregateCron(CienaProperties.get("ciena.aggregate.cron"));
		service.setPollCron(CienaProperties.get("ciena.spanloss.monitoring.cron"));
		service.setInventoryCron(CienaProperties.get("ciena.spanlossreport.cron"));
		// service.setRedisTTLvalue(versaProperties.get("versa.live.ttl",350));

		// service.setLiveThreadTimeout(versaProperties.get("versa.livethreads.completion.timeout.mins",10));

		AssetsCoreRuntime runtime = new AssetsCoreRuntime();
		runtime.register(service);
		logger.info("ciena service registered with core");

	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("config" + File.separator + "log4j_spanloss.properties");
		CienaReportServiceSpanLoss service = new CienaReportServiceSpanLoss();
		service.init();
		
		//service.loadInterfacesList();

		// service.generateReport();

	}

	@Override
	public Map<String, String> retrieveLiveLinkUsage(PerformanceInputBean pd, long startTime, long endTime) {
		
		//used for spanloss monitoring.
		
		CienaFiberLinkBean fiberLink = (CienaFiberLinkBean) pd;
		
		logger.info("Spanloss monitoring started for " + fiberLink.getLmsId() + "," + fiberLink.linkName);

		retrieveSpanLossAndRaiseTicket(fiberLink);

		return null;

	}

	@Override
	public ArrayList<InventoryDeviceDetails> retrieveInventoryFromDevice(PerformanceInputBean input) {


		logger.info("retrieveInventoryFromDevice " + ((CienaFiberLinkBean) input));

		ArrayList<InventoryDeviceDetails> list = new ArrayList<InventoryDeviceDetails>();

		list.add(retrieveSpanLossFiberLink((CienaFiberLinkBean) input));

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

		
	/*	boolean aEndSpanDegraded = (aEndSpanLoss != null && aEndSpanLoss >= designBOL) ? true : false;
		boolean zEndSpanDegraded = (zEndSpanLoss != null && zEndSpanLoss >= designBOL) ? true : false;*/
		
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
	
	public InventoryDeviceDetails retrieveSpanLossAndRaiseTicket(CienaFiberLinkBean fiberLink) {

		Double aEndSpanLoss = retrieveSpanLossPort(fiberLink.aEndNodeIp, fiberLink.aEndPort);
		Double zEndSpanLoss = retrieveSpanLossPort(fiberLink.zEndNodeIp, fiberLink.zEndPort);

		

		
		boolean aEndSpanDegraded = false;
		boolean zEndSpanDegraded = false;
		
		Double designBOL = null; 
		
		designBOL = Double.parseDouble(fiberLink.designedBOL);
		aEndSpanDegraded = (aEndSpanLoss != null && aEndSpanLoss >= designBOL) ? true : false;
		zEndSpanDegraded = (zEndSpanLoss != null && zEndSpanLoss >= designBOL) ? true : false;
		
		logger.info(
				fiberLink.linkName + ", " + fiberLink.aEndNodeName + ", " + fiberLink.aEndPort + " : " + aEndSpanLoss + ", " + "aEndSpanDegraded " + aEndSpanDegraded);
		logger.info(
				fiberLink.linkName + ", " + fiberLink.zEndNodeName + ", " + fiberLink.zEndPort + " : " + zEndSpanLoss + ", " + "zEndSpanDegraded " + zEndSpanDegraded);

		
		String ticketNumber = "";
		
		if (aEndSpanDegraded || zEndSpanDegraded) {
			// try to create ticket for spanloss degradation

			StringBuffer notesBuffer = new StringBuffer();
			if (aEndSpanDegraded && zEndSpanDegraded) {
				notesBuffer.append("Spanloss degraded at both AEnd and ZEnd");
			} else if (aEndSpanDegraded) {
				notesBuffer.append("Spanloss degraded at AEnd");
			} else if (zEndSpanDegraded) {
				notesBuffer.append("Spanloss degraded at ZEnd");
			}
			notesBuffer.append(", Cable :" + fiberLink.linkName);
			notesBuffer.append(", AEnd : " + fiberLink.aEndNodeName);
			notesBuffer.append(", ZEnd : " + fiberLink.zEndNodeName);
			notesBuffer.append(", Design BOL : " + fiberLink.designedBOL);
			notesBuffer.append(", AEnd loss : " + aEndSpanLoss);
			notesBuffer.append(", ZEnd loss : " + zEndSpanLoss);

			String ticketJson = "{\"notes\":\"" + notesBuffer.toString() + "\",\"problem_code\":\"262\",\"lmsid\":\""
					+ fiberLink.lmsId + "\"}";

			String responseJson = ServiceNowRestClient.createSpanlossTicket(fiberLink, ticketJson);
			
			if (responseJson.isEmpty()) {
				logger.error("LMS ID :" + fiberLink.lmsId + ", Failed createTicket REST call with ServiceNow");
			} else {

				JSONObject json = new JSONObject(responseJson);

				String status = json.getJSONObject("result").getString("status");
				if (status.equals("success")) {
					ticketNumber = json.getJSONObject("result").getString("number");
					if (ticketNumber != null) {
						logger.info("LMS ID :" + fiberLink.lmsId
								+ ", Ticket created sucessfully, Ticket number : " + ticketNumber);
					}
				} else if (status.equals("failed")) {
					String message = json.getJSONObject("result").getString("message");
					if (message.equals("Duplicate SR")) {
						ticketNumber = json.getJSONObject("result").getString("number");
						logger.info("LMS ID :" + fiberLink.lmsId + ", Open ticket already exists, Ticket number : "
								+ ticketNumber);
					} else {
						logger.error("LMS ID :" + fiberLink.lmsId + ", Ticket creation failed due to" + message);
					}
				}

			}

		}
		
		String payload = "{\r\n"
				+ "    \"a_device\": \""+fiberLink.getaEndNodeIp()+"\",\r\n"
				+ "    \"a_interface\": \""+fiberLink.getaEndPort()+"\",\r\n"
				+ "    \"a_node_id\": \""+fiberLink.getaEndNodeName()+"\",\r\n"
				+ "    \"a_rx_id\": \""+""+"\",\r\n"
				+ "    \"a_rx_value\": \""+""+"\",\r\n"
				+ "    \"a_tx_id\": \""+""+"\",\r\n"
				+ "    \"bol\": \""+fiberLink.getDesignedBOL()+"\",\r\n"
				+ "    \"distance\": \""+fiberLink.getDesignedOpticalLength()+"\",\r\n"
				+ "    \"eol\": \""+""+"\",\r\n"
				+ "    \"lms_id\": \""+fiberLink.getLmsId()+"\",\r\n"
				+ "    \"ticket_number\": \""+ticketNumber+"\",\r\n"
				+ "    \"total_loss_a_z\": \""+aEndSpanLoss+"\",\r\n"
				+ "    \"total_loss_z_a\": \""+zEndSpanLoss+"\",\r\n"
				+ "    \"z_device\": \""+fiberLink.getzEndNodeIp()+"\",\r\n"
				+ "    \"z_interface\": \""+fiberLink.getzEndPort()+"\",\r\n"
				+ "    \"z_node_id\": \""+fiberLink.getzEndNodeName()+"\",\r\n"
				+ "    \"z_rx_id\": \""+""+"\",\r\n"
				+ "    \"z_rx_value\": \""+""+"\",\r\n"
				+ "    \"z_tx_id\": \""+""+"\",\r\n"
				+ "    \"z_tx_value\": \""+""+"\",\r\n"
				+ "    \"a_tx_value\": \""+""+"\"\r\n"
				+ "}";

			
	     ServiceNowRestClient.postSpanLossToServiceNow(fiberLink, payload);

		return null;
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

	public String generateReport() {
		
		String dateStr="";

		try {

			// String dateStr = "2020-07-05";

			DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			logger.info(f.format(polled_date));

			dateStr = f.format(polled_date);

			logger.info("Report generation started");
			
			String filePath = CienaProperties.get("ciena.fbdailyreport.folder");
			String excelPath = filePath + File.separator + "span_loss_report_" + dateStr + ".xlsx";
					
			FileOutputStream fileOut = new FileOutputStream(excelPath);
			XSSFWorkbook wworkbook = createWorkBook();
			generateSpanLossSheetCiena(wworkbook, "Chennai ROADM", dateStr);
			generateSpanLossSheetCiena(wworkbook, "BGL ROADM", dateStr);
			generateSpanLossSheetCiena(wworkbook, "Delhi ROADM", dateStr);
			generateSpanLossSheetCiena(wworkbook, "Chennai Facebook", dateStr);
			generateSpanLossSheetHuawei(wworkbook, "Mumbai ROADM", dateStr);
			wworkbook.write(fileOut);
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

	public void generateSpanLossSheetCiena(XSSFWorkbook wworkbook, String roadmName, String dateStr) {
		try {
			XSSFSheet sheet = wworkbook.createSheet(roadmName);
			Row row;
			Cell cell;
			int rowcount = 0;
			row = sheet.createRow(rowcount);
			row.createCell(0).setCellValue("Link Details");			;
			row.createCell(1).setCellValue("Node Name");
			row.createCell(2).setCellValue("ESAM Port");
			row.createCell(3).setCellValue("Design OL(KM)");
			row.createCell(4).setCellValue("Design BOL");
			row.createCell(5).setCellValue("Span Loss "+dateStr);
			
			if (roadmName.equals("Chennai Facebook")) {
				row.createCell(6).setCellValue("Attenuation ");
				row.createCell(7).setCellValue("Span Loss - Attenuation");
				row.createCell(8).setCellValue("Span Loss Degraded");
				rowcount++;
			
			} else {
				
				row.createCell(6).setCellValue("Span Loss Degraded");
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
					+ "where collection_date='"	+ dateStr + "' and roadm_name='"	+ roadmName + "'";
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
				
				Double aendSpanloss=null;
				if (!result.getString("aend_span_loss").equals("null")) {
					aendSpanloss = Double.valueOf(result.getString("aend_span_loss"));
				}
				
				Double zendSpanloss=null;
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
				} else  {
					dolkm = Double.valueOf(result.getString("design_optical_length_km"));
					bol = Double.valueOf(result.getString("design_bol"));
				}

				String linkName = result.getString("link_name");
				String nodeName = result.getString("aend_node_name");
				String port = result.getString("aend_port");
				
				
				logger.info("preparing excel values for "+linkName);
				
				
				
				
				

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
				if (aendSpanloss!=null) {
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
					//cell.setCellStyle(style1);
				} else {
					cell.setCellValue("NO");
				}
				
				rowcount++;
				
				
				if (roadmName.equals("Chennai Facebook")) {
					dolkm = zEndDol;
					bol = zEndBol;
				}
				
				//linkName = result.getString("link_name");
				nodeName = result.getString("zend_node_name");
				port = result.getString("zend_port");
				//dolkm = result.getString("design_optical_length_km");
				//bol = result.getString("design_bol");
				
				
				
				

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
				if (zendSpanloss!=null) {
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
					//cell.setCellStyle(style1);
				} else {
					cell.setCellValue("NO");
				}
				
				sheet.addMergedRegion(new CellRangeAddress(rowcount-1,rowcount,0,0));
				
				if (!roadmName.equals("Chennai Facebook")) {
				sheet.addMergedRegion(new CellRangeAddress(rowcount-1,rowcount,3,3));
				sheet.addMergedRegion(new CellRangeAddress(rowcount-1,rowcount,4,4));
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
	
	public void generateSpanLossSheetHuawei(XSSFWorkbook wworkbook, String roadmName, String dateStr) {
		try {
			XSSFSheet sheet = wworkbook.createSheet(roadmName);
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
			row.createCell(9).setCellValue("Span Loss Degraded");
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
				
						rowcount++;
			}

			for (int i = 0; i < 11; i++) {
				sheet.autoSizeColumn(i);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	

	public void initPolling() {

		logger.info("initPolling called");
		
		Date currentDate = new Date();
		polled_date = new Timestamp(currentDate.getTime());


	}
	
	
	public void finishInvnetory() {
		
		logger.info("Spanloss report generation called");

		String dateStr=generateReport();
		
		ServiceNowRestClient.postSpanLossReportToServiceNow(dateStr);

		logger.info("Spanloss report generation completed");

	}


	public void finishPolling() {
		logger.info("Spanloss monitoring started");
		logger.info("Spanloss monitoring completed");

	}

}
