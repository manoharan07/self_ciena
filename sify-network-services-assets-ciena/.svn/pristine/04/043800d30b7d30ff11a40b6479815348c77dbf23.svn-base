package com.sify.network.assets.ciena;

import java.io.File;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import com.sify.network.alarms.ciena.KeyToolUtils;

public class ServiceNowRestClient {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("com.sify.network.assets.ciena.ServiceNowRestClient");
	
	//static CienaProperties cienaProperties = new CienaProperties();
	
	private static String serviceNowSslContextType = "SSL";

	public static String postAttachment(ServiceNowRequest request,String tableName, String sysId) {
		String attachmentSysId = null;
		try {

			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(request.getUsername(),
					request.getPassword());
			provider.setCredentials(AuthScope.ANY, credentials);
			CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

			HttpPost post = new HttpPost(request.getTargetUrl());
			String textFileName = request.getFilePath();

			File file = new File(textFileName);
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
			StringBody stringBody1 = new StringBody(tableName,
					ContentType.MULTIPART_FORM_DATA);
			StringBody stringBody2 = new StringBody(sysId,
					ContentType.MULTIPART_FORM_DATA);

			builder.addPart("table_name", stringBody1);
			builder.addPart("table_sys_id", stringBody2);
			builder.addPart("f", fileBody);
			HttpEntity entity = builder.build();

			post.setEntity(entity);
			// Execute HTTP Post Request
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			
			
			String responseBody = client.execute(post, responseHandler);

			logger.info(responseBody);

			JSONObject jsonObj = new JSONObject(responseBody);

			JSONObject resultObj = jsonObj.getJSONObject("result");

			attachmentSysId = resultObj.getString("sys_id");

		} catch (Exception e) {

			e.printStackTrace();

		}

		return attachmentSysId;
	}
	
	public static String postAttachment(ServiceNowRequest request,String tableName, String sysId,org.apache.log4j.Logger logger) {
		String attachmentSysId = null;
		try {

			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(request.getUsername(),
					request.getPassword());
			provider.setCredentials(AuthScope.ANY, credentials);
			CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

			HttpPost post = new HttpPost(request.getTargetUrl());
			String textFileName = request.getFilePath();

			File file = new File(textFileName);
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
			StringBody stringBody1 = new StringBody(tableName,
					ContentType.MULTIPART_FORM_DATA);
			StringBody stringBody2 = new StringBody(sysId,
					ContentType.MULTIPART_FORM_DATA);

			builder.addPart("table_name", stringBody1);
			builder.addPart("table_sys_id", stringBody2);
			builder.addPart("f", fileBody);
			HttpEntity entity = builder.build();

			post.setEntity(entity);
			// Execute HTTP Post Request
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			
			
			String responseBody = client.execute(post, responseHandler);

			logger.info(responseBody);
			
			logger.info("ServiceNow File Upload response : "+responseBody);

			JSONObject jsonObj = new JSONObject(responseBody);

			JSONObject resultObj = jsonObj.getJSONObject("result");

			attachmentSysId = resultObj.getString("sys_id");

		} catch (Exception e) {

			logger.error("Unable to upload file to ServiceNow ",e);
			
			e.printStackTrace();

		}

		return attachmentSysId;
	}


	public static void postFbReportRecord(String dateStr, String attachmentSysId) {
		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("fbdailyreportlog");
		try {

			ServiceNowRequest request = new ServiceNowRequest();
			request.setUsername(CienaProperties.get("servicenow.username"));
			request.setPassword(CienaProperties.get("servicenow.password"));
			request.setTargetUrl(
					CienaProperties.get("ciena.fbdailyreport.servicenow.table.api.url"));
					//"https://sifydev.service-now.com/api/x_sitl_telco_sify/sifyservice/postFaceBookDailyReports");

			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(request.getUsername(),
					request.getPassword());
			provider.setCredentials(AuthScope.ANY, credentials);
			CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

			HttpPost post = new HttpPost(request.getTargetUrl());

			HttpEntity stringEntity = new StringEntity(
					"{\"u_report_date\":\"" + dateStr + "\",\"u_report_file\":\"" + attachmentSysId + "\"}",
					ContentType.APPLICATION_JSON);

			post.setEntity(stringEntity);
			// Execute HTTP Post Request
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = client.execute(post, responseHandler);

			logger.info("Upload response : "+responseBody);
			logger.info(responseBody);

		} catch (Exception e) {
			logger.error("Unable to upload fb report to ServiceNow",e);
			e.printStackTrace();

		}

	}
	
	public static void postAmazonReportRecord(String dateStr, String attachmentSysId) {
		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("amazonhealthreportlog");
		try {

			ServiceNowRequest request = new ServiceNowRequest();
			request.setUsername(CienaProperties.get("servicenow.username"));
			request.setPassword(CienaProperties.get("servicenow.password"));
			request.setTargetUrl(
					CienaProperties.get("ciena.amazonhealthreport.servicenow.table.api.url"));
					//"https://sifydev.service-now.com/api/x_sitl_telco_sify/sifyservice/postFaceBookDailyReports");

			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(request.getUsername(),
					request.getPassword());
			provider.setCredentials(AuthScope.ANY, credentials);
			CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

			HttpPost post = new HttpPost(request.getTargetUrl());
			
			DateFormat f1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    Date polledDate2 = f1.parse(dateStr);
		    
		   // String utcStr = 
			
			TimeZone utc = TimeZone.getTimeZone("UTC");
			f1.setTimeZone(utc);
			
			String dateStrUtc = f1.format(polledDate2);
			//logger.info(dateStrUtc);

			HttpEntity stringEntity = new StringEntity(
					"{\"report_date\":\"" + dateStrUtc + "\",\"report_file\":\"" + attachmentSysId + "\"}",
					ContentType.APPLICATION_JSON);

            logger.info("Adding uploaded file to report table (json) : "+"{\"report_date\":\"" + dateStrUtc + "\",\"report_file\":\"" + attachmentSysId + "\"}");
			
			post.setEntity(stringEntity);
			// Execute HTTP Post Request
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = client.execute(post, responseHandler);

			logger.info("Upload response : "+responseBody);
			logger.info(responseBody);

		} catch (Exception e) {
			logger.error("Unable to upload amazon report to ServiceNow",e);
			e.printStackTrace();

		}

	}

	
	public static void postSpanLossReportRecord(String dateStr, String attachmentSysId) {
		
		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("spanlossreportlog");

		try {
			
			ServiceNowRequest request = new ServiceNowRequest();
			request.setUsername(CienaProperties.get("servicenow.username"));
			request.setPassword(CienaProperties.get("servicenow.password"));
			request.setTargetUrl(
					CienaProperties.get("ciena.spanlossreport.servicenow.table.api.url"));
					

			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(request.getUsername(),
					request.getPassword());
			provider.setCredentials(AuthScope.ANY, credentials);
			CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

			HttpPost post = new HttpPost(request.getTargetUrl());

			HttpEntity stringEntity = new StringEntity(
					"{\"u_report_date\":\"" + dateStr + "\",\"u_report_file\":\"" + attachmentSysId + "\"}",
					ContentType.APPLICATION_JSON);

			post.setEntity(stringEntity);
			// Execute HTTP Post Request
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = client.execute(post, responseHandler);

			logger.info("Upload response : "+responseBody);
			logger.info(responseBody);

		} catch (Exception e) {
			logger.error("Unable to upload spanloss report to ServiceNow",e);
			e.printStackTrace();

		}

	}

	public static void postFbReportToServiceNow(String dateStr,String fileNamePrefix) {
		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("fbdailyreportlog");
		
		logger.info("Uploading generated report to ServiceNow");
		try {

			// String dateStr = "2020-07-13";
			
			String filePath = CienaProperties.get("ciena.fbdailyreport.folder");
			
			String fileNameStr = filePath + File.separator + fileNamePrefix + dateStr + ".xlsx";

			File f = new File(fileNameStr);
			
			logger.info("Uploading generated report "+fileNameStr+" to ServiceNow");

			ServiceNowRequest req = new ServiceNowRequest();
			
			req.setUsername(CienaProperties.get("servicenow.username"));
			req.setPassword(CienaProperties.get("servicenow.password"));
			req.setTargetUrl(CienaProperties.get("servicenow.fileattachment.upload.url"));
			
			req.setFilePath(f.getAbsolutePath());
			//String attachmentSysId = postAttachment(req,"x_sitl_telco_sify_sifyopticalreportsfbdaily","fcd86423db751410067cac1bd3961981");
			String tableName = CienaProperties.get("ciena.fbdailyreport.servicenow.tablename");
			String tableSysId = CienaProperties.get("ciena.fbdailyreport.servicenow.tablesysid");
			String attachmentSysId = postAttachment(req,tableName,tableSysId);
			postFbReportRecord(dateStr, attachmentSysId);

		} catch (Exception e) {
			logger.error("Unable to upload fb report to ServiceNow",e);
			
		}
	}
	
	public static void postAmazonReportToServiceNow(String dateStr) {
		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("amazonhealthreportlog");
		
		logger.info("Uploading generated report to ServiceNow");
		try {

			// String dateStr = "2020-07-13";
			
			String dateStrFileName = dateStr.replaceAll(" ", "_").replaceAll(":", "_");
			
			dateStrFileName = dateStrFileName.substring(0,dateStrFileName.length()-3);

			
			String filePath = CienaProperties.get("ciena.amazonhealthreport.folder");
			
			String fileNameStr = filePath + File.separator + "amazon_health_report_" + dateStrFileName + ".xlsx";

			File f = new File(fileNameStr);
			
			logger.info("Uploading generated report "+fileNameStr+" to ServiceNow");

			ServiceNowRequest req = new ServiceNowRequest();
			
			req.setUsername(CienaProperties.get("servicenow.username"));
			req.setPassword(CienaProperties.get("servicenow.password"));
			req.setTargetUrl(CienaProperties.get("servicenow.fileattachment.upload.url"));
			
			req.setFilePath(f.getAbsolutePath());
			//String attachmentSysId = postAttachment(req,"x_sitl_telco_sify_sifyopticalreportsfbdaily","fcd86423db751410067cac1bd3961981");
			String tableName = CienaProperties.get("ciena.amazonhealthreport.servicenow.tablename");
			String tableSysId = CienaProperties.get("ciena.amazonhealthreport.servicenow.tablesysid");
			String attachmentSysId = postAttachment(req,tableName,tableSysId,logger);
			
			if (attachmentSysId != null) {
				postAmazonReportRecord(dateStr, attachmentSysId);
			} else {
				//try again posting record.
				logger.info("File upload to ServiceNow failed. Trying again.");
				
				attachmentSysId = postAttachment(req,tableName,tableSysId,logger);
				if (attachmentSysId != null) {
					postAmazonReportRecord(dateStr, attachmentSysId);
				} else {
					logger.error("File upload to ServiceNow failed two times.");
				}
			}

		} catch (Exception e) {
			logger.error("Unable to upload Amazon report to ServiceNow",e);
			
		}
	}

	
	public static void postSpanLossReportToServiceNow(String dateStr) {
		
		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("spanlossreportlog");
		//logger.info("Running ....");
		try {

			// String dateStr = "2020-07-13";

			String filePath = CienaProperties.get("ciena.spanlossreport.folder");

			String fileNameStr = filePath + File.separator + "span_loss_report_" + dateStr + ".xlsx";

			File f = new File(fileNameStr);
			
			logger.info("Uploading generated report "+fileNameStr+" to ServiceNow");

			ServiceNowRequest req = new ServiceNowRequest();
			/*req.setUsername("bhaskaran.arumugam@sifycorp.com");
			req.setPassword("test123");
			req.setTargetUrl("https://sifydev.service-now.com/api/now/attachment/upload");*/
			
			req.setUsername(CienaProperties.get("servicenow.username"));
			req.setPassword(CienaProperties.get("servicenow.password"));
			req.setTargetUrl(CienaProperties.get("servicenow.fileattachment.upload.url"));
			
			req.setFilePath(f.getAbsolutePath());
			//String attachmentSysId = postAttachment(req,"x_sitl_telco_sify_opticalreports_spanloss_ciena","e5ff1e30db86d410067cac1bd39619ff");
			String tableName = CienaProperties.get("ciena.spanlossreport.servicenow.tablename");
			String tableSysId = CienaProperties.get("ciena.spanlossreport.servicenow.tablesysid");
			String attachmentSysId = postAttachment(req,tableName,tableSysId);
			postSpanLossReportRecord(dateStr, attachmentSysId);

		} catch (Exception e) {
			logger.error("Unable to upload spanloss report to ServiceNow",e);
		}
	}
	
	
	public static void postNetworkAvailabilityReportToServiceNow(String dateStr,String roadmName) {
		
		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("networkavaiabilitylog");
		//logger.info("Running ....");
		try {

			// String dateStr = "2020-07-13";

	//		String filePath = CienaProperties.get("ciena.spanlossreport.folder");

	//		String fileNameStr = filePath + File.separator + "span_loss_report_" + dateStr + ".xlsx";
			
			
			String roadmNameForFile = roadmName.replaceAll(" ", "_");

			String filePath = CienaProperties.get("optical.network.availability.report.folder");
			String fileNameStr = filePath + File.separator + roadmNameForFile + "_NW_Availability_Report_" + dateStr
					+ ".xlsx";


			File f = new File(fileNameStr);
			
			logger.info("Uploading generated report "+fileNameStr+" to ServiceNow");

			ServiceNowRequest req = new ServiceNowRequest();
			/*req.setUsername("bhaskaran.arumugam@sifycorp.com");
			req.setPassword("test123");
			req.setTargetUrl("https://sifydev.service-now.com/api/now/attachment/upload");*/
			
			req.setUsername(CienaProperties.get("servicenow.username"));
			req.setPassword(CienaProperties.get("servicenow.password"));
			req.setTargetUrl(CienaProperties.get("servicenow.fileattachment.upload.url"));
			
			req.setFilePath(f.getAbsolutePath());
			//String attachmentSysId = postAttachment(req,"x_sitl_telco_sify_opticalreports_spanloss_ciena","e5ff1e30db86d410067cac1bd39619ff");
			String tableName = CienaProperties.get("optical.network.availability.report.servicenow.tablename");
			String tableSysId = CienaProperties.get("optical.network.availability.report.servicenow.tablesysid");
			String attachmentSysId = postAttachment(req,tableName,tableSysId);
			postNetworkAvaiablityReportFileAttachment(dateStr, attachmentSysId);

		} catch (Exception e) {
			logger.error("Unable to upload spanloss report to ServiceNow",e);
		}
	}
	
	public static void postNetworkAvailabilityReportToServiceNow(String dateStr,String roadmName, String fileName) {
		
		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("networkavaiabilitylog");
		//logger.info("Running ....");
		try {


			String filePath = CienaProperties.get("optical.network.availability.report.folder");
			String fileNameStr = filePath + File.separator + fileName;


			File f = new File(fileNameStr);
			
			logger.info("Uploading generated report "+fileNameStr+" to ServiceNow");

			ServiceNowRequest req = new ServiceNowRequest();
			/*req.setUsername("bhaskaran.arumugam@sifycorp.com");
			req.setPassword("test123");
			req.setTargetUrl("https://sifydev.service-now.com/api/now/attachment/upload");*/
			
			req.setUsername(CienaProperties.get("servicenow.username"));
			req.setPassword(CienaProperties.get("servicenow.password"));
			req.setTargetUrl(CienaProperties.get("servicenow.fileattachment.upload.url"));
			
			req.setFilePath(f.getAbsolutePath());
			//String attachmentSysId = postAttachment(req,"x_sitl_telco_sify_opticalreports_spanloss_ciena","e5ff1e30db86d410067cac1bd39619ff");
			String tableName = CienaProperties.get("optical.network.availability.report.servicenow.tablename");
			String tableSysId = CienaProperties.get("optical.network.availability.report.servicenow.tablesysid");
			String attachmentSysId = postAttachment(req,tableName,tableSysId);
			postNetworkAvaiablityReportFileAttachment(dateStr, attachmentSysId);

		} catch (Exception e) {
			logger.error("Unable to upload spanloss report to ServiceNow",e);
		}
	}

	
	public static void postNetworkAvaiablityReportFileAttachment(String dateStr, String attachmentSysId) {
		
		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("networkavaiabilitylog");

		try {
			
			ServiceNowRequest request = new ServiceNowRequest();
			request.setUsername(CienaProperties.get("servicenow.username"));
			request.setPassword(CienaProperties.get("servicenow.password"));
			request.setTargetUrl(
					CienaProperties.get("optical.network.availability.report.servicenow.table.api.url"));
					

			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(request.getUsername(),
					request.getPassword());
			provider.setCredentials(AuthScope.ANY, credentials);
			CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

			HttpPost post = new HttpPost(request.getTargetUrl());

			HttpEntity stringEntity = new StringEntity(
					"{\"u_report_date\":\"" + dateStr + "\",\"u_report_file\":\"" + attachmentSysId + "\"}",
					ContentType.APPLICATION_JSON);

			post.setEntity(stringEntity);
			// Execute HTTP Post Request
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = client.execute(post, responseHandler);

			logger.info("Upload response : "+responseBody);
			logger.info(responseBody);

		} catch (Exception e) {
			logger.error("Unable to upload spanloss report to ServiceNow",e);
			e.printStackTrace();

		}

	}
	
	
	public static void postAlarmHealthReportToServiceNow(String dateStr) {
		
		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("alarmhealthcheckup");
		//logger.info("Running ....");
		try {

			String filePath = CienaProperties.get("optical.alarm.health.checkup.report.folder");
			String fileNameStr = filePath + File.separator + "AlarmHealthCheckupReport_" + dateStr
					+ ".xlsx";


			File f = new File(fileNameStr);
			
			logger.info("Uploading generated report "+fileNameStr+" to ServiceNow");

			ServiceNowRequest req = new ServiceNowRequest();
	
			req.setUsername(CienaProperties.get("servicenow.username"));
			req.setPassword(CienaProperties.get("servicenow.password"));
			req.setTargetUrl(CienaProperties.get("servicenow.fileattachment.upload.url"));
			
			req.setFilePath(f.getAbsolutePath());
			String tableName = CienaProperties.get("optical.alarm.health.checkup.report.servicenow.tablename");
			String tableSysId = CienaProperties.get("optical.alarm.health.checkup.report.servicenow.tablesysid");
			String attachmentSysId = postAttachment(req,tableName,tableSysId);
			postAlarmHealthReportFileAttachment(dateStr, attachmentSysId);

		} catch (Exception e) {
			logger.error("Unable to upload spanloss report to ServiceNow",e);
		}
	}
	
	public static void postAlarmHealthReportFileAttachment(String dateStr, String attachmentSysId) {
		
		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("alarmhealthcheckup");

		try {
			
			ServiceNowRequest request = new ServiceNowRequest();
			request.setUsername(CienaProperties.get("servicenow.username"));
			request.setPassword(CienaProperties.get("servicenow.password"));
			request.setTargetUrl(
					CienaProperties.get("optical.alarm.health.checkup.report.servicenow.table.api.url"));
					

			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(request.getUsername(),
					request.getPassword());
			provider.setCredentials(AuthScope.ANY, credentials);
			CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

			HttpPost post = new HttpPost(request.getTargetUrl());

			HttpEntity stringEntity = new StringEntity(
					"{\"u_report_date\":\"" + dateStr + "\",\"u_report_file\":\"" + attachmentSysId + "\"}",
					ContentType.APPLICATION_JSON);

			post.setEntity(stringEntity);
			// Execute HTTP Post Request
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = client.execute(post, responseHandler);

			logger.info("Upload response : "+responseBody);
			logger.info(responseBody);

		} catch (Exception e) {
			logger.error("Unable to upload spanloss report to ServiceNow",e);
			e.printStackTrace();

		}

	}



	
	
	public static void postAlarm(String alarmJson) {
		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("cienaalarms");
		try {
			
			logger.info("Posting alarms to ServiceNow : "+alarmJson);
			logger.info("Posting alarms to ServiceNow : "+alarmJson);


			ServiceNowRequest request = new ServiceNowRequest();
			request.setUsername(CienaProperties.get("servicenow.username"));
			request.setPassword(CienaProperties.get("servicenow.password"));
			request.setTargetUrl(CienaProperties.get("servicenow.alarm.post.url.ciena"));
					

			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(request.getUsername(),
					request.getPassword());
			provider.setCredentials(AuthScope.ANY, credentials);
			CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

			HttpPost post = new HttpPost(request.getTargetUrl());

			HttpEntity stringEntity = new StringEntity(alarmJson,
					ContentType.APPLICATION_JSON);

			post.setEntity(stringEntity);
			// Execute HTTP Post Request
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = client.execute(post, responseHandler);

			logger.info("Alarm post to ServiceNow response : "+responseBody);

		} catch(javax.net.ssl.SSLHandshakeException sslException) {
			
			logger.error("Unable to post Alarm to ServiceNow due to ssl issues, " + alarmJson, sslException);
			sslException.printStackTrace();

			try {
				logger.info("Trying to retrieve new certificate from ServiceNow and store in keystore.");
				
				String url = CienaProperties.get("servicenow.alarm.post.url.ciena");
				
				String site = url.replaceAll("https://", "");
				site = site.substring(0,site.indexOf("/"));
				logger.info("Site "+site);
				
				KeyToolUtils.retrieveCertificateAndStore(site, 443);
				logger.info("Retrieve new certificate from ServiceNow and stored in keystore.");
				logger.info("Retrying to post alarm again after ssl certificate received.");
				postAlarmRetry(alarmJson);
			} catch (Exception e) {
				logger.error("Unable to retrieve ssl certificate from ServiceNow", e);
			}

			
		}	catch (Exception e) {
			logger.error("Unable to post Alarm to ServiceNow, "+alarmJson,e);
			e.printStackTrace();

		}

	}
	
	public static void postAlarmRetry(String alarmJson) {
		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("cienaalarms");
		try {
			
			logger.info("Posting alarms to ServiceNow : "+alarmJson);
			logger.info("Posting alarms to ServiceNow : "+alarmJson);


			ServiceNowRequest request = new ServiceNowRequest();
			request.setUsername(CienaProperties.get("servicenow.username"));
			request.setPassword(CienaProperties.get("servicenow.password"));
			request.setTargetUrl(CienaProperties.get("servicenow.alarm.post.url.ciena"));
					

			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(request.getUsername(),
					request.getPassword());
			provider.setCredentials(AuthScope.ANY, credentials);
			CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

			HttpPost post = new HttpPost(request.getTargetUrl());

			HttpEntity stringEntity = new StringEntity(alarmJson,
					ContentType.APPLICATION_JSON);

			post.setEntity(stringEntity);
			// Execute HTTP Post Request
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = client.execute(post, responseHandler);

			logger.info("Alarm post to ServiceNow response : "+responseBody);

		} catch(javax.net.ssl.SSLHandshakeException sslException) {
			
			logger.error("Unable to post Alarm to ServiceNow due to ssl issues, "+alarmJson,sslException);
			sslException.printStackTrace();

			
		}	catch (Exception e) {
			logger.error("Unable to post Alarm to ServiceNow, "+alarmJson,e);
			e.printStackTrace();

		}

	}
	
	public static void postAlarm(String alarmJson,String postUrl) {
		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("cienaalarms");
		try {
			
			logger.info("Posting alarms to ServiceNow : "+alarmJson);
			logger.info("Posting alarms to ServiceNow : "+alarmJson);


			ServiceNowRequest request = new ServiceNowRequest();
			request.setUsername(CienaProperties.get("servicenow.username"));
			request.setPassword(CienaProperties.get("servicenow.password"));
			request.setTargetUrl(postUrl);
					
			
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };
			SSLContext sslContext = SSLContext.getInstance(serviceNowSslContextType);
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(request.getUsername(),
					request.getPassword());
			provider.setCredentials(AuthScope.ANY, credentials);
			//CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

			CloseableHttpClient client = HttpClientBuilder.create().setSSLContext(sslContext).setDefaultCredentialsProvider(provider).build();
			
			HttpPost post = new HttpPost(request.getTargetUrl());

			HttpEntity stringEntity = new StringEntity(alarmJson,
					ContentType.APPLICATION_JSON);

			post.setEntity(stringEntity);
			// Execute HTTP Post Request
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = client.execute(post, responseHandler);

			logger.info("Alarm post to ServiceNow response : "+responseBody);

		} catch(javax.net.ssl.SSLHandshakeException sslException) {
			
			logger.error("Unable to post Alarm to ServiceNow due to ssl issues, " + alarmJson, sslException);
			sslException.printStackTrace(System.out);

			try {
				logger.info("Trying to retrieve new certificate from ServiceNow and store in keystore.");
				System.out.println("Trying to retrieve new certificate from ServiceNow and store in keystore.");
				
				String url = CienaProperties.get("servicenow.alarm.post.url.ciena");
				
				String site = url.replaceAll("https://", "");
				site = site.substring(0,site.indexOf("/"));
				logger.info("Site "+site);
				System.out.println("Site "+site);
				
				KeyToolUtils.retrieveCertificateAndStore(site, 443);
				logger.info("Retrieve new certificate from ServiceNow and stored in keystore.");
				System.out.println("Retrieve new certificate from ServiceNow and stored in keystore.");
				
				logger.info("Retrying to post alarm again after ssl certificate received.");
				postAlarmRetry(alarmJson,postUrl);
			} catch (Exception e) {
				logger.error("Unable to retrieve ssl certificate from ServiceNow", e);
				e.printStackTrace(System.out);
			}

			
		}	catch (Exception e) {
			logger.error("Unable to post Alarm to ServiceNow, "+alarmJson,e);
			e.printStackTrace();

		}

	}
	
	public static void postAlarmRetry(String alarmJson,String postUrl) {
		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("cienaalarms");
		try {
			
			logger.info("Posting alarms to ServiceNow : "+alarmJson);
			//logger.info("Posting alarms to ServiceNow : "+alarmJson);


			ServiceNowRequest request = new ServiceNowRequest();
			request.setUsername(CienaProperties.get("servicenow.username"));
			request.setPassword(CienaProperties.get("servicenow.password"));
			request.setTargetUrl(postUrl);
			
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };
			SSLContext sslContext = SSLContext.getInstance(serviceNowSslContextType);
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

					

			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(request.getUsername(),
					request.getPassword());
			provider.setCredentials(AuthScope.ANY, credentials);
			//CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

			CloseableHttpClient client = HttpClientBuilder.create().setSSLContext(sslContext).setDefaultCredentialsProvider(provider).build();
			HttpPost post = new HttpPost(request.getTargetUrl());

			HttpEntity stringEntity = new StringEntity(alarmJson,
					ContentType.APPLICATION_JSON);

			post.setEntity(stringEntity);
			// Execute HTTP Post Request
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = client.execute(post, responseHandler);

			logger.info("Alarm post to ServiceNow response : "+responseBody);

		} catch(javax.net.ssl.SSLHandshakeException sslException) {
			
			logger.error("Unable to post Alarm to ServiceNow due to ssl issues, "+alarmJson,sslException);
			sslException.printStackTrace();

			
		}	catch (Exception e) {
			logger.error("Unable to post Alarm to ServiceNow, "+alarmJson,e);
			e.printStackTrace();

		}

	}

	
	
	public static void postAlarmToServiceNowTest(String alarmJson) {
		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("cienawsalarms");
		try {
			
			logger.info("Posting alarms to ServiceNowTest : "+alarmJson);
			//logger.info("Posting alarms to ServiceNowTest : "+alarmJson);


			ServiceNowRequest request = new ServiceNowRequest();
			request.setUsername(CienaProperties.get("servicenowtest.username"));
			request.setPassword(CienaProperties.get("servicenowtest.password"));
			request.setTargetUrl(CienaProperties.get("servicenowtest.alarm.post.url.ciena"));
			//request.setTargetUrl(postUrl);
					

			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(request.getUsername(),
					request.getPassword());
			provider.setCredentials(AuthScope.ANY, credentials);
			CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

			HttpPost post = new HttpPost(request.getTargetUrl());

			HttpEntity stringEntity = new StringEntity(alarmJson,
					ContentType.APPLICATION_JSON);

			post.setEntity(stringEntity);
			// Execute HTTP Post Request
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = client.execute(post, responseHandler);

			logger.info("Alarm post to ServiceNowTest response : "+responseBody);

		} catch(javax.net.ssl.SSLHandshakeException sslException) {
			
			logger.error("Unable to post Alarm to ServiceNowTest due to ssl issues, " + alarmJson, sslException);
			sslException.printStackTrace();

			try {
				logger.info("Trying to retrieve new certificate from ServiceNowTest and store in keystore.");
				
				String url = CienaProperties.get("servicenowtest.alarm.post.url.ciena");
				
				String site = url.replaceAll("https://", "");
				site = site.substring(0,site.indexOf("/"));
				logger.info("Site "+site);
				
				KeyToolUtils.retrieveCertificateAndStore(site, 443);
				logger.info("Retrieve new certificate from ServiceNowTest and stored in keystore.");
				logger.info("Retrying to post alarm again to ServiceNowTest after ssl certificate received.");
				postAlarmToServiceNowTestRetry(alarmJson);
			} catch (Exception e) {
				logger.error("Unable to retrieve ssl certificate from ServiceNow", e);
			}

			
		}	catch (Exception e) {
			logger.error("Unable to post Alarm to ServiceNowTest, "+alarmJson,e);
			e.printStackTrace();

		}

	}

	
	
	public static void postAlarmToServiceNowTest(String alarmJson,String postUrl) {
		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("cienawsalarms");
		try {
			
			logger.info("Posting alarms to ServiceNowTest : "+alarmJson);
			//logger.info("Posting alarms to ServiceNowTest : "+alarmJson);


			ServiceNowRequest request = new ServiceNowRequest();
			request.setUsername(CienaProperties.get("servicenowtest.username"));
			request.setPassword(CienaProperties.get("servicenowtest.password"));
			//request.setTargetUrl(CienaProperties.get("servicenowtest.alarm.post.url.ciena"));
			request.setTargetUrl(postUrl);
			
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };
			SSLContext sslContext = SSLContext.getInstance(serviceNowSslContextType);
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

					

			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(request.getUsername(),
					request.getPassword());
			provider.setCredentials(AuthScope.ANY, credentials);
		//	CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

			CloseableHttpClient client = HttpClientBuilder.create().setSSLContext(sslContext).setDefaultCredentialsProvider(provider).build();
			HttpPost post = new HttpPost(request.getTargetUrl());

			HttpEntity stringEntity = new StringEntity(alarmJson,
					ContentType.APPLICATION_JSON);

			post.setEntity(stringEntity);
			// Execute HTTP Post Request
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = client.execute(post, responseHandler);

			logger.info("Alarm post to ServiceNowTest response : "+responseBody);

		} catch(javax.net.ssl.SSLHandshakeException sslException) {
			
			logger.error("Unable to post Alarm to ServiceNowTest due to ssl issues, " + alarmJson, sslException);
			sslException.printStackTrace();

			try {
				logger.info("Trying to retrieve new certificate from ServiceNowTest and store in keystore.");
				
				String url = CienaProperties.get("servicenowtest.alarm.post.url.ciena");
				
				String site = url.replaceAll("https://", "");
				site = site.substring(0,site.indexOf("/"));
				logger.info("Site "+site);
				
				KeyToolUtils.retrieveCertificateAndStore(site, 443);
				logger.info("Retrieve new certificate from ServiceNowTest and stored in keystore.");
				logger.info("Retrying to post alarm again to ServiceNowTest after ssl certificate received.");
				postAlarmToServiceNowTestRetry(alarmJson,postUrl);
			} catch (Exception e) {
				logger.error("Unable to retrieve ssl certificate from ServiceNow", e);
			}

			
		}	catch (Exception e) {
			logger.error("Unable to post Alarm to ServiceNowTest, "+alarmJson,e);
			e.printStackTrace();

		}

	}
	
	public static void postAlarmToServiceNowTestRetry(String alarmJson) {
		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("cienaalarms");
		try {
			
			logger.info("Posting alarms to ServiceNowTest : "+alarmJson);
			//logger.info("Posting alarms to ServiceNowTest : "+alarmJson);


			ServiceNowRequest request = new ServiceNowRequest();
			request.setUsername(CienaProperties.get("servicenowtest.username"));
			request.setPassword(CienaProperties.get("servicenowtest.password"));
			request.setTargetUrl(CienaProperties.get("servicenowtest.alarm.post.url.ciena"));
					

			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(request.getUsername(),
					request.getPassword());
			provider.setCredentials(AuthScope.ANY, credentials);
			CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

			HttpPost post = new HttpPost(request.getTargetUrl());

			HttpEntity stringEntity = new StringEntity(alarmJson,
					ContentType.APPLICATION_JSON);

			post.setEntity(stringEntity);
			// Execute HTTP Post Request
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = client.execute(post, responseHandler);

			logger.info("Alarm post to ServiceNow response : "+responseBody);

		} catch(javax.net.ssl.SSLHandshakeException sslException) {
			
			logger.error("Unable to post Alarm to ServiceNow due to ssl issues, "+alarmJson,sslException);
			sslException.printStackTrace();

			
		}	catch (Exception e) {
			logger.error("Unable to post Alarm to ServiceNow, "+alarmJson,e);
			e.printStackTrace();

		}

	}

	public static void postAlarmToServiceNowTestRetry(String alarmJson,String postUrl) {
		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("cienaalarms");
		try {
			
			logger.info("Posting alarms to ServiceNowTest : "+alarmJson);
			//logger.info("Posting alarms to ServiceNowTest : "+alarmJson);


			ServiceNowRequest request = new ServiceNowRequest();
			request.setUsername(CienaProperties.get("servicenowtest.username"));
			request.setPassword(CienaProperties.get("servicenowtest.password"));
			//request.setTargetUrl(CienaProperties.get("servicenowtest.alarm.post.url.ciena"));
			request.setTargetUrl(postUrl);
			
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };
			SSLContext sslContext = SSLContext.getInstance(serviceNowSslContextType);
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

					

			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(request.getUsername(),
					request.getPassword());
			provider.setCredentials(AuthScope.ANY, credentials);
			//CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
			
			CloseableHttpClient client = HttpClientBuilder.create().setSSLContext(sslContext).setDefaultCredentialsProvider(provider).build();

			HttpPost post = new HttpPost(request.getTargetUrl());

			HttpEntity stringEntity = new StringEntity(alarmJson,
					ContentType.APPLICATION_JSON);

			post.setEntity(stringEntity);
			// Execute HTTP Post Request
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = client.execute(post, responseHandler);

			logger.info("Alarm post to ServiceNow response : "+responseBody);

		} catch(javax.net.ssl.SSLHandshakeException sslException) {
			
			logger.error("Unable to post Alarm to ServiceNow due to ssl issues, "+alarmJson,sslException);
			sslException.printStackTrace();

			
		}	catch (Exception e) {
			logger.error("Unable to post Alarm to ServiceNow, "+alarmJson,e);
			e.printStackTrace();

		}

	}

	
	
	public static String getCasesFromServiceNow(String customer,String startDate, String endDate) {
		String responseJson=null;
		String queryJson="{\"startDate\":\""+startDate+"\",\"endDate\":\""+endDate+"\",\"customer\":\""+customer+"\"}";
		String casesUrl = CienaProperties.get("servicenow.cases.retrieve.url");
		responseJson = getPostResponseFromServiceNow(casesUrl,queryJson);
		return responseJson;
	}


	public static String getIncidentsFromServiceNow(String taskGroup,String startDate, String endDate) {
		String responseJson=null;
		String queryJson="{\"startDate\":\""+startDate+"\",\"endDate\":\""+endDate+"\",\"taskGroup\":\""+taskGroup+"\"}";
		String incidentUrl = CienaProperties.get("servicenow.incidents.retrieve.url");
		responseJson = getPostResponseFromServiceNow(incidentUrl,queryJson);
		return responseJson;
	}
	
	public static String getFiberLinksFromServiceNow() {
		String responseJson=null;
		String queryJson="{\"linkType\":\"CORE\"}";
		String linksUrl = CienaProperties.get("servicenow.fiberlinks.retrieve.url");
		responseJson = getPostResponseFromServiceNow(linksUrl,queryJson);
		return responseJson;
	}


	
	public static String getCiena6500AlarmsForAvailability(String ip,String optmonPort, String esamSlot, String startDate, String endDate) {
		String responseJson=null;
		String queryJson="{\"startDate\":\""+startDate+"\",\"endDate\":\""+endDate+"\",\"ipAddress\":\""+ip+"\",\"optmonport\":\""+optmonPort+"\",\"esamslot\":\""+esamSlot+"\"}";
		
		responseJson = getCiena6500Alarms(ip,startDate, endDate,queryJson);
		return responseJson;
	}
	
	public static String getCiena6500AlarmsHistory(String ip,String startDate, String endDate) {
		String responseJson=null;
		String queryJson="{\"startDate\":\""+startDate+"\",\"endDate\":\""+endDate+"\",\"ipAddress\":\""+ip+"\"}";
		
		responseJson = getCiena6500Alarms(ip,startDate, endDate,queryJson);
		return responseJson;
	}
	
	public static String getCiena6500Alarms(String ip,String startDate, String endDate,String queryJson) {
		
		String responseJson=null;
		//String queryJson="{\"startDate\":\""+startDate+"\",\"endDate\":\""+endDate+"\",\"ipAddress\":\""+ip+"\"}";
		
		try {
			
			logger.info("QueryJSON to retrive alarms from ServiceNow : "+queryJson);


			ServiceNowRequest request = new ServiceNowRequest();
			request.setUsername(CienaProperties.get("servicenow.username"));
			request.setPassword(CienaProperties.get("servicenow.password"));
			request.setTargetUrl(CienaProperties.get("servicenow.alarm.retrieve.url.ciena"));
					

			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(request.getUsername(),
					request.getPassword());
			provider.setCredentials(AuthScope.ANY, credentials);
			CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

			HttpPost post = new HttpPost(request.getTargetUrl());

			HttpEntity stringEntity = new StringEntity(queryJson,
					ContentType.APPLICATION_JSON);

			post.setEntity(stringEntity);
			// Execute HTTP Post Request
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			responseJson = client.execute(post, responseHandler);

			logger.info("Alarm post to ServiceNow response : "+responseJson);

		} catch (Exception e) {

			e.printStackTrace();

		}
		
		return responseJson;

	}
	
	public static String getHuaweiAlarms(String elementId, String port, String startDate, String endDate) {
		
		String responseJson=null;
		String queryJson="{\"startDate\":\""+startDate+"\",\"endDate\":\""+endDate+"\",\"elementId\":\""+elementId+"\",\"port\":\""+port+"\"}";
		
		try {
			
			logger.info("QueryJSON to retrive alarms from ServiceNow : "+queryJson);


			ServiceNowRequest request = new ServiceNowRequest();
			request.setUsername(CienaProperties.get("servicenow.username"));
			request.setPassword(CienaProperties.get("servicenow.password"));
			request.setTargetUrl(CienaProperties.get("servicenow.alarm.retrieve.url.huawei"));
					

			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(request.getUsername(),
					request.getPassword());
			provider.setCredentials(AuthScope.ANY, credentials);
			CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

			HttpPost post = new HttpPost(request.getTargetUrl());

			HttpEntity stringEntity = new StringEntity(queryJson,
					ContentType.APPLICATION_JSON);

			post.setEntity(stringEntity);
			// Execute HTTP Post Request
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			responseJson = client.execute(post, responseHandler);

			logger.info("Alarm post to ServiceNow response : "+responseJson);

		} catch (Exception e) {

			e.printStackTrace();

		}
		
		return responseJson;

	}
	
	
public static String getPostResponseFromServiceNow(String url,String queryJson) {
		
		String responseJson=null;
		//String queryJson="{\"startDate\":\""+startDate+"\",\"endDate\":\""+endDate+"\",\"ipAddress\":\""+ip+"\"}";
		
		try {
			
			logger.info("URL and QueryJSON : "+url+","+queryJson);


			ServiceNowRequest request = new ServiceNowRequest();
			request.setUsername(CienaProperties.get("servicenow.username"));
			request.setPassword(CienaProperties.get("servicenow.password"));
			request.setTargetUrl(url);
					

			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(request.getUsername(),
					request.getPassword());
			provider.setCredentials(AuthScope.ANY, credentials);
			CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

			HttpPost post = new HttpPost(request.getTargetUrl());

			HttpEntity stringEntity = new StringEntity(queryJson,
					ContentType.APPLICATION_JSON);

			post.setEntity(stringEntity);
			// Execute HTTP Post Request
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			responseJson = client.execute(post, responseHandler);

			logger.info(url+ " response : "+responseJson);

		} catch (Exception e) {
			
			logger.error("Unable to retrieve data from ServiceNow for URL : "+url,e);

			e.printStackTrace();

		}
		
		return responseJson;

	}


	public static void syncAlarms(String nodeIp, String alarmsListJson) {
		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("alarmssync");
		try {

			logger.info("Syncing alarms to ServiceNow for "+nodeIp+" : " + alarmsListJson);
			logger.info("Syncing alarms to ServiceNow : " + alarmsListJson);

			ServiceNowRequest request = new ServiceNowRequest();
			request.setUsername(CienaProperties.get("servicenow.username"));
			request.setPassword(CienaProperties.get("servicenow.password"));
			request.setTargetUrl(CienaProperties.get("servicenow.alarm.sync.url.ciena"));

			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(request.getUsername(),
					request.getPassword());
			provider.setCredentials(AuthScope.ANY, credentials);
			CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

			HttpPost post = new HttpPost(request.getTargetUrl());

			HttpEntity stringEntity = new StringEntity(alarmsListJson, ContentType.APPLICATION_JSON);

			post.setEntity(stringEntity);
			// Execute HTTP Post Request
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = client.execute(post, responseHandler);

			logger.info("Sync alarms response for "+nodeIp+" : " + responseBody);

		} catch (javax.net.ssl.SSLHandshakeException sslException) {

			logger.error("Unable to sync alarms to ServiceNow due to ssl issues, " + alarmsListJson, sslException);

		} catch (Exception e) {
			logger.error("Unable to sync alarms to ServiceNow, " + alarmsListJson, e);
			e.printStackTrace();

		}

	}
	
	
	
	
	
	public static void main(String args[]){
		
		CienaProperties cienaProperties = new CienaProperties();
		
		if (args.length > 0) {
			System.out.println("Passed ServiceNow SSLContext : "+args[0]);
			serviceNowSslContextType = args[0];
		} 
		
		System.out.println("ServiceNow SSLContext : "+serviceNowSslContextType);
		
		String postUrlServiceNowTest = CienaProperties.get("servicenowtest.alarm.post.url.ciena.ws");
		//String postUrlServiceNow = CienaProperties.get("servicenow.alarm.post.url.ciena.ws");
		
		String currentTimeStamp = "" + (System.currentTimeMillis() / 1000);

		String alarmJson = "{\"version\":1,\"seqNumber\":2000,\"timestamp\":"+currentTimeStamp+",\"dbChange\":{\"action\":\"create\",\"path\":\"/waveserver-alarms/alarms/active/241/\",\"data\":{\"active\":{\"alarmInstanceId\":241,\"acknowledged\":false,\"alarmTableId\":131,\"severity\":\"major\",\"localDateTime\":\"Mon Aug 30 03:11:11 2021\",\"instance\":\"Chassis\",\"description\":\"External License Server Comms Failed\",\"siteIdentifier\":9,\"groupIdentifier\":1,\"memberIdentifier\":0}}},\"deviceip\":\"100.67.78.197\"}";
		postAlarm(alarmJson,postUrlServiceNowTest);
	}


	public static String createSpanlossTicket(CienaFiberLinkBean link, String ticketJson) {
		
		String responseBody="";

		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("spanloss");
		try {

			logger.info("Creating ticket for "+link.getLmsId()+" : " + ticketJson);
			

			ServiceNowRequest request = new ServiceNowRequest();
			request.setUsername(CienaProperties.get("servicenow.username"));
			request.setPassword(CienaProperties.get("servicenow.password"));
			request.setTargetUrl(CienaProperties.get("servicenow.fiberlinks.ticket.url"));

			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(request.getUsername(),
					request.getPassword());
			provider.setCredentials(AuthScope.ANY, credentials);
			CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

			HttpPost post = new HttpPost(request.getTargetUrl());

			HttpEntity stringEntity = new StringEntity(ticketJson, ContentType.APPLICATION_JSON);

			post.setEntity(stringEntity);
			// Execute HTTP Post Request
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			responseBody = client.execute(post, responseHandler);

			logger.info("Create ticket response "+link.getLmsId()+" : " + responseBody);
			System.out.println("Create ticket response "+link.getLmsId()+" : " + responseBody);
			
			return responseBody;

		} catch (javax.net.ssl.SSLHandshakeException sslException) {

			logger.error("Unable to create ticket to ServiceNow due to ssl issues, " + ticketJson, sslException);

		} catch (Exception e) {
			logger.error("Unable to create ticket in ServiceNow for " + link.getLmsId() + ", "+ ticketJson, e);
			e.printStackTrace();

		}
	
		return responseBody;
	}
	
	public static String postSpanLossToServiceNow(CienaFiberLinkBean link, String spanLossJson) {
		
		String responseBody="";

		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("spanloss");
		try {

			logger.info("Posting spanloss details to servicenow report table for "+link.getLmsId()+" : " + spanLossJson);
			

			ServiceNowRequest request = new ServiceNowRequest();
			request.setUsername(CienaProperties.get("servicenow.username"));
			request.setPassword(CienaProperties.get("servicenow.password"));
			request.setTargetUrl(CienaProperties.get("servicenow.fiberlinks.spanloss.report.url"));

			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(request.getUsername(),
					request.getPassword());
			provider.setCredentials(AuthScope.ANY, credentials);
			CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

			HttpPost post = new HttpPost(request.getTargetUrl());

			HttpEntity stringEntity = new StringEntity(spanLossJson, ContentType.APPLICATION_JSON);

			post.setEntity(stringEntity);
			// Execute HTTP Post Request
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			responseBody = client.execute(post, responseHandler);

			logger.info("post spanloss to servicenow response "+link.getLmsId()+" : " + responseBody);
			System.out.println("post spanloss to servicenow response "+link.getLmsId()+" : " + responseBody);
			
			return responseBody;

		} catch (javax.net.ssl.SSLHandshakeException sslException) {

			logger.error("Unable to post spanloss to ServiceNow report table due to ssl issues, " + spanLossJson, sslException);

		} catch (Exception e) {
			logger.error("Unable to post spanloss to ServiceNow report table for " + link.getLmsId() + ", "+ spanLossJson, e);
			e.printStackTrace();

		}

		return responseBody;
	}

	

}
