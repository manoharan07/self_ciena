package com.sify.network.assets.ciena;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONArray;
import org.json.JSONObject;
 

public class LeptonClient {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("spanloss");
	
	private CienaProperties cienaProperties = new CienaProperties();

	private String ip = "";
	
	private static String[] regions =  { "Southern","Western","Northern","Eastern" };

	public static void main(String[] args) throws IOException {

		
	
		LeptonClient leptonClient = new LeptonClient();
		
		File csvFile = new File("LeptonFiberLinks.csv");
		
		PrintWriter pw = new PrintWriter(csvFile);
		
		pw.println("lmsid,linktype,networkid,cablename,province,from facility,to facility,otdr length,gislength");
	
		for(String region:regions) {
		//HttpURLConnection alarmsConn = leptonClient.retrieveLinks();
		//HttpsURLConnection alarmsConn = leptonClient.retrieveLinksHttps(region);
		HttpURLConnection alarmsConn = leptonClient.retrieveLinksHttp(region);
		//HttpURLConnection alarmsConn = leptonClient.retrieveLinks("Southern");
		
		//System.out.println("token : " + token + " : cookie : " + cookie);
		
		//System.out.println("Response json "+leptonClient.getJson(alarmsConn));
		
		JSONObject jsonResponse = new JSONObject(leptonClient.getJson(alarmsConn));
		JSONArray jsonLinks = jsonResponse.getJSONArray("Data").getJSONObject(0).getJSONArray("Features");
		for(int i=0;i<jsonLinks.length();i++) {
			JSONObject jsonLink = jsonLinks.getJSONObject(i);
			JSONObject linkProperties = jsonLink.getJSONObject("Properties");
			String lmsId = linkProperties.optString("LmsLinkID");
			String networkId = linkProperties.optString("NetworkID");
			String cableName = linkProperties.optString("CableName");
			String provinceName = linkProperties.optString("ProvinceName");
			String linkType = linkProperties.optString("RingNameCoreOrCollector");
			String linkFromFacility = linkProperties.optString("FromFacilityID");
			String linkToFacility = linkProperties.optString("ToFacilityID");
			String otdrLength = linkProperties.optString("OtdrLengthMtr");
			String gisLength = linkProperties.optString("CableGisLengthMtr");
			
			//if (linkType.equals("CORE")) {
			pw.println(lmsId + "," + linkType+ ","  + networkId+ "," + cableName+ "," + provinceName+ ","+ linkFromFacility+ "," + linkToFacility+ "," + otdrLength + "," + gisLength);
			System.out.println(lmsId + "," + linkType+ ","  + networkId+ "," + cableName+ "," + provinceName+ ","+ linkFromFacility+ "," + linkToFacility+ "," + otdrLength + "," + gisLength);
			//}
			
		}
			
		
		
		}
		
		pw.flush();
		pw.close();

		
		//conn.doLogout(token, cookie);
		
	}


	
	public HttpURLConnection retrieveLinks(String region) {
		
		long startTime = System.currentTimeMillis();
		
		String linkRetrieveUrl = "http://"+ip+"/sfautomationapi/getcable";
		
		System.out.println("url used : "+linkRetrieveUrl);


		HttpURLConnection conn = null;
        try {

			URL myUrl = new URL(linkRetrieveUrl);
			conn =  (HttpURLConnection) myUrl.openConnection();

			conn.setRequestMethod("GET");

			conn.setDoOutput(true);
			conn.setRequestProperty("key", "123");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");

			String params = "{     \"LayerType\": \"Cable\",     \"ElocID\": \"\",     \"Region\": \"Southern\" }";

			
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.writeBytes(params);
			wr.flush();
			wr.close();
			
			System.out.println("getCables response : code : " + conn.getResponseCode() + " : message : " + conn.getResponseMessage());
			
			if(conn.getResponseCode() >= 200 && conn.getResponseCode() < 400){
				logger.info("Succeeded API Response Code : "+conn.getResponseCode()+", Response Time (seconds) : " + ((System.currentTimeMillis() - startTime) * 0.001) + ", URL : "+ linkRetrieveUrl );
			}else{
				logger.info("Failed API Response Code : "+conn.getResponseCode()+", Response Time (seconds) : " + ((System.currentTimeMillis() - startTime) * 0.001) + ", URL : "+ linkRetrieveUrl );
				System.out.println(conn.getResponseMessage());
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return conn;
	}


	
	public String getJson(HttpURLConnection conn) {

		int statusCode = 0;
		String inputLine = "";
		String json = "";
		try {
			if (conn != null) {
				statusCode = conn.getResponseCode();
				System.out.println("statusCode:::" + statusCode);
				if (statusCode == 200 || statusCode == 201) {
					StringBuilder sb = new StringBuilder();
					BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					while ((inputLine = br.readLine()) != null) {
						sb.append(inputLine);
					}
					json = sb.toString();
				} else {
					StringBuilder sb = new StringBuilder();
					BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
					while ((inputLine = br.readLine()) != null) {
						sb.append(inputLine);
					}
					json = sb.toString();
				}
			} else {
				System.out.println("conn is null");
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			return null;
		}
		System.out.println("JSON Response : "+json);
		return json;
	}
	
	
	
	public HttpURLConnection retrieveLinks() {
		

			HttpURLConnection conn = null;
			try {

				String urlParameters = "{     \"data\": { \"LayerType\": \"Cable\", \"ElocID\": \"\", \"Region\": \"Southern\" },   "
						+ "  \"url\": \""+CienaProperties.get("ciena.spanlossreport.lepton.url")+"\",   "
						+ "  \"key\": \""+CienaProperties.get("ciena.spanlossreport.lepton.key")+"\" }";

				byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
				int postDataLength = postData.length;

				// URL myUrl = new
				// URL("http://100.65.181.54/_performance/deviceSSHPush");
				//URL myUrl = new URL("http://100.65.181.54/_performance/leptonAPICalling");
				URL myUrl = new URL(CienaProperties.get("ciena.spanlossreport.lepton.proxy.url"));

				logger.info("Calling proxy API for link retrieval : " + myUrl + " with parameters " + urlParameters);

				conn = (HttpURLConnection) myUrl.openConnection();

				conn.setRequestMethod("POST");
				conn.setDoOutput(true);

				//conn.setConnectTimeout(viptelaProp.get("viptela.api.read.timeout", 20000));
				conn.setRequestProperty("Content-Type", "application/json");
			//	conn.setRequestProperty("token", "375941e0-1c1b-4f46-8944-89757423da6b");
				conn.setRequestProperty("token", CienaProperties.get("ciena.spanlossreport.lepton.proxy.token"));
				// conn.setInstanceFollowRedirects(false);
				//conn.setReadTimeout(viptelaProp.get("viptela.api.read.timeout", 20000));

				conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
				try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
					wr.write(postData);
				} catch (Exception e) {
					logger.error("Unable write credentials to login url connection", e);
					e.printStackTrace();
				}
				
				
				return conn;
			} catch (Exception e) {
				logger.error("doLogin Exception", e);
			}
			return null;
		}

	
	public HttpsURLConnection retrieveLinksHttps(String region) {
		

		HttpsURLConnection conn = null;
		try {
			
			 
			
			long startTime = System.currentTimeMillis();
			System.out.println("Retrieve Links API started for region " +  region);

			
			TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
        };
			 // Install the all-trusting trust manager
	        SSLContext sc = SSLContext.getInstance("SSL");
			//SSLContext sc = SSLContext.getInstance("TLS");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	 
	        // Create all-trusting host name verifier
	        HostnameVerifier allHostsValid = new HostnameVerifier() {
	            public boolean verify(String hostname, SSLSession session) {
	                return true;
	            }
	        };

	        // Install the all-trusting host verifier
	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);


			String urlParameters = "{     \"data\": { \"LayerType\": \"Cable\", \"ElocID\": \"\", \"Region\": \""+region+"\" },   "
					+ "  \"url\": \""+CienaProperties.get("ciena.spanlossreport.lepton.url")+"\",   "
					+ "  \"key\": \""+CienaProperties.get("ciena.spanlossreport.lepton.key")+"\" }";

			byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
			int postDataLength = postData.length;

			// URL myUrl = new
			// URL("http://100.65.181.54/_performance/deviceSSHPush");
			//URL myUrl = new URL("http://100.65.181.54/_performance/leptonAPICalling");
			String proxyUrl = CienaProperties.get("ciena.spanlossreport.lepton.proxy.url");
			URL myUrl = new URL(proxyUrl);

			logger.info("Calling proxy API for link retrieval : " + myUrl + " with parameters " + urlParameters);

			conn = (HttpsURLConnection) myUrl.openConnection();

			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			//conn.setConnectTimeout(viptelaProp.get("viptela.api.read.timeout", 20000));
			conn.setRequestProperty("Content-Type", "application/json");
		//	conn.setRequestProperty("token", "375941e0-1c1b-4f46-8944-89757423da6b");
			conn.setRequestProperty("token", CienaProperties.get("ciena.spanlossreport.lepton.proxy.token"));
			// conn.setInstanceFollowRedirects(false);
			conn.setReadTimeout(30000);

			conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
				wr.write(postData);
			} catch (Exception e) {
				logger.error("Unable write credentials to login url connection", e);
				e.printStackTrace();
			}
			
			
			if(conn.getResponseCode() >= 200 && conn.getResponseCode() < 400){
				System.out.println("Succeeded API Response Code : "+conn.getResponseCode()+", Response Time (seconds) : " + ((System.currentTimeMillis() - startTime) * 0.001) + ", URL : "+ proxyUrl );
			}else{
				logger.info("Failed API Response Code : "+conn.getResponseCode()+", Response Time (seconds) : " + ((System.currentTimeMillis() - startTime) * 0.001) + ", URL : "+ proxyUrl );
				System.out.println(conn.getResponseMessage());
				return null;
			}

			
			
			return conn;
		} catch (Exception e) {
			logger.error("doLogin Exception", e);
		}
		return null;
	}
	
	public HttpURLConnection retrieveLinksHttp(String region) {
		

		HttpURLConnection conn = null;
		try {
			
			 
			
			long startTime = System.currentTimeMillis();
			System.out.println("Retrieve Links API started for region " +  region);

			
			/*
			 * TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
			 * public java.security.cert.X509Certificate[] getAcceptedIssuers() { return
			 * null; } public void checkClientTrusted(X509Certificate[] certs, String
			 * authType) { } public void checkServerTrusted(X509Certificate[] certs, String
			 * authType) { } } }; // Install the all-trusting trust manager SSLContext sc =
			 * SSLContext.getInstance("SSL"); //SSLContext sc =
			 * SSLContext.getInstance("TLS"); sc.init(null, trustAllCerts, new
			 * java.security.SecureRandom());
			 * HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			 * 
			 * // Create all-trusting host name verifier HostnameVerifier allHostsValid =
			 * new HostnameVerifier() { public boolean verify(String hostname, SSLSession
			 * session) { return true; } };
			 * 
			 * // Install the all-trusting host verifier
			 * HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			 */

			String urlParameters = "{     \"data\": { \"LayerType\": \"Cable\", \"ElocID\": \"\", \"Region\": \""+region+"\" },   "
					+ "  \"url\": \""+CienaProperties.get("ciena.spanlossreport.lepton.url")+"\",   "
					+ "  \"key\": \""+CienaProperties.get("ciena.spanlossreport.lepton.key")+"\" }";

			byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
			int postDataLength = postData.length;

			// URL myUrl = new
			// URL("http://100.65.181.54/_performance/deviceSSHPush");
			//URL myUrl = new URL("http://100.65.181.54/_performance/leptonAPICalling");
			String proxyUrl = CienaProperties.get("ciena.spanlossreport.lepton.proxy.url");
			URL myUrl = new URL(proxyUrl);

			logger.info("Calling proxy API for link retrieval : " + myUrl + " with parameters " + urlParameters);

			conn = (HttpURLConnection) myUrl.openConnection();

			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			//conn.setConnectTimeout(viptelaProp.get("viptela.api.read.timeout", 20000));
			conn.setRequestProperty("Content-Type", "application/json");
		//	conn.setRequestProperty("token", "375941e0-1c1b-4f46-8944-89757423da6b");
			conn.setRequestProperty("token", CienaProperties.get("ciena.spanlossreport.lepton.proxy.token"));
			// conn.setInstanceFollowRedirects(false);
			conn.setReadTimeout(30000);

			conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
				wr.write(postData);
			} catch (Exception e) {
				logger.error("Unable write credentials to login url connection", e);
				e.printStackTrace();
			}
			
			
			if(conn.getResponseCode() >= 200 && conn.getResponseCode() < 400){
				System.out.println("Succeeded API Response Code : "+conn.getResponseCode()+", Response Time (seconds) : " + ((System.currentTimeMillis() - startTime) * 0.001) + ", URL : "+ proxyUrl );
			}else{
				logger.info("Failed API Response Code : "+conn.getResponseCode()+", Response Time (seconds) : " + ((System.currentTimeMillis() - startTime) * 0.001) + ", URL : "+ proxyUrl );
				System.out.println(conn.getResponseMessage());
				return null;
			}

			
			
			return conn;
		} catch (Exception e) {
			logger.error("doLogin Exception", e);
		}
		return null;
	}



	
	

}


