package com.sify.network.assets.ciena;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sify.network.assets.core.api.bean.InventoryDeviceDetails;
 
//WaveServer Client
public class CienaWsClient {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("cienaws");
	
	String opticalInstances[] = { "1-1-OpticalPower" };
	String modemInstances[] = { "1-1-Modem" };
	String oduInstances[] = { "1-1.1-ODU" };
	
	public static HashMap<String,String> hostNamesMap = new HashMap<String,String>();
	
	public static HashMap<String,CienaWsCircuitInventory> circuitMap = new HashMap<String,CienaWsCircuitInventory>();
	
	private CienaProperties cienaProperties = new CienaProperties();
	
	
	

	public static void main(String[] args)  {
	
		CienaWsClient cienaClient  = new CienaWsClient();
		
		//cienaClient.retrievePerformanceDetails();
		//cienaClient.retrievePerformanceHistoryAll();
		cienaClient.retrieveActiveAlarms();
	}


	public Object getJson(HttpsURLConnection conn){

		JSONObject jsonObject = null;
		JSONArray jsonArray = null;
		Object json=null;
		BufferedReader br = null;
		InputStream is1  = null;
		int statusCode = 0;
		try {
			if(conn!=null){
				statusCode = conn.getResponseCode();
				if (statusCode >= 200 && statusCode < 400) {
					is1 = conn.getInputStream();
					InputStreamReader isr = new InputStreamReader(is1);
					br = new BufferedReader(isr);
					String inputLine;
					StringBuilder sb = new StringBuilder();

					while ((inputLine = br.readLine()) != null) {
						sb.append(inputLine);

					}
					
					System.out.println(conn.getURL().toString() + " : \n"+sb);
					logger.info(sb);
					if(sb == null || sb.toString().equals("")){
//						System.out.println("Builder is empty");
						logger.info("Builder is empty");
						return null;
					}
					
							
					String jsonStr = sb.toString();
					
					System.out.println("Json : "+jsonStr);
					
					if (jsonStr.startsWith("[")) {
						jsonArray = new JSONArray(sb.toString());
						json = jsonArray;
					} else {
						jsonObject = new JSONObject(sb.toString());
						json = jsonObject;
					}
					

				}else{
					String returnStr;
					try (BufferedReader buffer = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
						returnStr = buffer.lines().collect(Collectors.joining("\n"));
					}
					logger.error("Error Status code is " + statusCode + " with error " + returnStr + " for url :"
							+ conn.getURL());
//					System.out.println("Error Status code is " + statusCode);
					return null;
				}
			}else{
//				System.out.println("conn is null");
				return null;
			}

		} catch (IOException e) {
			System.out.println(e.getMessage() + " : "+conn.getURL().toString());
			e.printStackTrace(System.out);
			return null;
		} catch (JSONException e) {
			e.printStackTrace(System.out);
			return null;
		} catch (Exception e){
			return null;
		}
		finally{
			if(conn!=null){
				conn.disconnect();
			}
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
			}
			if(is1!=null){
				try {
					is1.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
			}
		}
		
	
		return json;

	}


	public HttpsURLConnection getHttpsConnectionWithoutCertificate(String url,String methodType,Map<String,String> sessionMap){

		HttpsURLConnection conn = null;

		try {
			
			long startTime = System.currentTimeMillis();
			logger.info("API is " + url + " started at " +  startTime);

			
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
	       
	        URL myUrl = new URL(url);
			conn = (HttpsURLConnection)myUrl.openConnection();
			String authStr = cienaProperties.get("ciena.waveserver.username") +":"+cienaProperties.get("ciena.waveserver.password");
			byte[] authEncoded = Base64.getEncoder().encode(authStr.getBytes());

			conn.setRequestMethod(methodType);
			conn.setDoOutput(true);
			conn.setRequestProperty("Authorization", "Basic " + new String(authEncoded)); 
//			conn.setSSLSocketFactory(sslContext.getSocketFactory());
			conn.setRequestProperty("Content-Type", "application/json");
		//	conn.setRequestProperty("X-XSRF-TOKEN", sessionMap.get("token"));
		//	conn.setRequestProperty("Cookie", "-http-session-="+sessionMap.get("cookie"));
		//	conn.setRequestProperty("Cache-Control","no-cache");
			conn.setRequestProperty("Accept", "application/json");
	//		conn.setConnectTimeout(cienaProperties.get("versa.api.read.timeout",20000));
	//		conn.setReadTimeout(cienaProperties.get("versa.api.read.timeout",20000));

		//	conn.setHostnameVerifier(new InvalidCertificateHostVerifier());

			if(conn.getResponseCode() >= 200 && conn.getResponseCode() < 400){
				logger.info("Succeeded API Response Code : "+conn.getResponseCode()+", Response Time (seconds) : " + ((System.currentTimeMillis() - startTime) * 0.001) + ", URL : "+ url );
			}else{
				logger.info("Failed API Response Code : "+conn.getResponseCode()+", Response Time (seconds) : " + ((System.currentTimeMillis() - startTime) * 0.001) + ", URL : "+ url );
				System.out.println(conn.getResponseMessage());
				return null;
			}
			
		} catch(SocketTimeoutException tEx) {
			//logger.error(" for " + url,tEx );
			  tEx.printStackTrace(System.out);
			logger.error("Read timeout happened. Retrying URL : " + url,tEx );			//tEx.printStackTrace(System.out);
		} catch (Exception e){
			logger.error("conn is null for " + url,e );
			
			e.printStackTrace(System.out);
			return null;
		}

		return conn;
	}
	
	
	public Map<String, String> getSessionId(HttpURLConnection conn) {

		Map<String, List<String>> mapValue = conn.getHeaderFields(); 
	//	System.out.println(mapValue.toString());
		logger.info(mapValue.toString());
		String token = "";
		String cookie = "";
		
		String strArray[];
		for (String key : mapValue.keySet()) {
			if (key == null) {
				continue;
			}
			if (key.trim().equalsIgnoreCase("x-xsrf-token")) {
				token = mapValue.get(key).get(0);
			}
			if (key.trim().equalsIgnoreCase("Set-Cookie")) {
				cookie = mapValue.get(key).get(1);
				
			}
			
			
		}

	/*	System.out.println("cookie : " + cookie.split(";")[0].split("=")[1]);
		
		System.out.println("x-xsrf-token : " + token.split(";")[0]);
		*/

		Map<String, String> sessionMap = new HashMap<>();
		sessionMap.put("token", token.split(";")[0]);
		sessionMap.put("cookie", cookie.split(";")[0].split("=")[1]);

		conn.disconnect();
		return sessionMap;
	}


	public void doLogout(String url) {

		HttpURLConnection conn = null;
		try {
			URL myUrl = new URL(url);
			conn = (HttpURLConnection)myUrl.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setConnectTimeout(cienaProperties.get("versa.api.read.timeout",20000));
			conn.setInstanceFollowRedirects(false);
			conn.setReadTimeout(cienaProperties.get("versa.api.read.timeout",20000));
		} catch (Exception e){
			e.printStackTrace();
		}finally{
			conn.disconnect();
		}
	}
	
	public HttpsURLConnection doLoginUrlEncoded(String url) {

		HttpsURLConnection conn = null;
		try {
			
			long startTime = System.currentTimeMillis();
			logger.info("API is " + url + " started at " +  startTime);
			
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
			
			String urlParameters  = "userid="+cienaProperties.get("ciena.api.username")+"&password="+cienaProperties.get("ciena.api.password");
			
			byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
			int postDataLength = postData.length;
			
			URL myUrl = new URL(url);
			conn = (HttpsURLConnection)myUrl.openConnection();

			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setConnectTimeout(cienaProperties.get("ciena.api.read.timeout",20000));
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setInstanceFollowRedirects(false);
			conn.setReadTimeout(cienaProperties.get("ciena.api.read.timeout",20000));
			
			conn.setRequestProperty("Content-Length", Integer.toString(postDataLength ));
			try {
				DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
				wr.write( postData );
			} catch(Exception e) {
				logger.error("Unable write credentials to login url connection",e);
				//e.printStackTrace();
			}
			
			if(conn.getResponseCode() >= 200 && conn.getResponseCode() < 400){
				//System.out.println("Succeeded Login Response Code : "+conn.getResponseCode()+", Response Time (seconds) : " + ((System.currentTimeMillis() - startTime) * 0.001) + ", URL : "+ url );
			}else{
				//System.out.println("Failed Login Response Code : "+conn.getResponseCode()+", Response Time (seconds) : " + ((System.currentTimeMillis() - startTime) * 0.001) + ", URL : "+ url );
				System.out.println(conn.getResponseMessage() + " : " + url);
				return null;
			}
			
			return conn;
		} catch (Exception e){
			//e.printStackTrace();
			System.err.println(e.getMessage()+" : "+url);
			//return null;
		}
		return null;
	}
	

	
	public List<String> getDeviceIpList() {
		
		String COMMA_DELIMITER=",";
        String[] values = null;
        List<String> ipAddresses = new ArrayList<String>();
        try {
            try (BufferedReader br = new BufferedReader(new FileReader("e:\\Sify\\ciena\\CienaDevicesIpList.csv"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    values = line.split(COMMA_DELIMITER);
                    ipAddresses.add(values[5]);
                }
            }
            ipAddresses.remove(0);
            /*
             * String deviceIps = "100.66.28.116,100.66.28.132"; String ipList[] =
             * deviceIps.split(","); return Arrays.asList(ipList);
             */
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //System.out.println("ipAddresses....."+ipAddresses);
        return ipAddresses;
    }

	public void retrievePerformanceDetails() {

		try {

			ArrayList<String> ipList = new ArrayList<String>();
			
			//waveserver1
			ipList.add("100.65.244.99");
			//ipList.add("100.65.244.107");
			
			//waveserver2
			//ipList.add("100.65.244.100");
			//ipList.add("100.65.244.108");
			
			
			

			FileWriter fw = new FileWriter("e:\\Sify\\ciena\\WSParameters.csv");

		//	fw.write("ip,aid,tx_tag,rx_actual");
		//	fw.write("\n");

			for (String ip : ipList) {


				HttpsURLConnection apiConn = getHttpsConnectionWithoutCertificate(
						"https://" + ip + "/restconf/data/waveserver-ptps", "GET", null);

				JSONObject jsonObject = (JSONObject) getJson(apiConn);
				
				JSONArray jsonArray =(JSONArray) ((JSONObject)jsonObject.get("ciena-waveserver-ptp:waveserver-ptps")).get("ptps");
				
				
				
				for(int i=0;i<jsonArray.length();i++) {
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					
					System.out.println(jsonObj.get("ptp-id") );
					
					JSONObject lanesObj = jsonObj.getJSONObject("properties").getJSONObject("lanes");
					
					if (lanesObj.getInt("number-of-lanes")==1) {
					
					JSONObject laneObj = jsonObj.getJSONObject("properties").getJSONObject("lanes").getJSONArray("lane").getJSONObject(0);
					System.out.println("Rx Power "+laneObj.getJSONObject("rx").getJSONObject("power").get("actual"));
					System.out.println("Tx Power "+laneObj.getJSONObject("tx").getJSONObject("power").get("actual"));
					} else {
						
						JSONArray lanesArray = jsonObj.getJSONObject("properties").getJSONObject("lanes").getJSONArray("lane");
						
						String txPort="";
						String txValue= "";
						String rxPort="";
						String rxValue="";
						
						for (int laneIndex=0;laneIndex<lanesArray.length();laneIndex++) {
							JSONObject laneObj = lanesArray.getJSONObject(laneIndex);
							txPort += "L"+laneIndex + "/";
							txValue += laneObj.getJSONObject("tx").getJSONObject("power").get("actual") + "/";
							
							rxPort += "L"+laneIndex + "/";
							rxValue += laneObj.getJSONObject("rx").getJSONObject("power").get("actual") + "/";
							
							//System.out.println("Lane "+laneIndex+", Rx Power "+laneObj.getJSONObject("rx").getJSONObject("power").get("actual"));
							//System.out.println("Lane "+laneIndex+", Tx Power "+laneObj.getJSONObject("tx").getJSONObject("power").get("actual"));
						}
						
						System.out.println("Tx Power "+jsonObj.get("ptp-id")+" "+txPort.substring(0,txPort.length()-1)+" : "+txValue.substring(0,txValue.length()-1));
						System.out.println("Rx Power "+jsonObj.get("ptp-id")+" "+rxPort.substring(0,rxPort.length()-1)+" : "+rxValue.substring(0,rxValue.length()-1));
						
					}
					
							
				}
				
				HttpsURLConnection fecConn = getHttpsConnectionWithoutCertificate(
						"https://" + ip + "/restconf/data/waveserver-pm/modem-performance-instances", "GET", null);

				JSONObject instancesJsonObject = (JSONObject) getJson(fecConn);
				
				JSONArray fecJsonArray = instancesJsonObject.getJSONArray("modem-performance-instances");
				
				for(int fecIndex=0;fecIndex<fecJsonArray.length();fecIndex++) {
					JSONObject fecJsonObject = fecJsonArray.getJSONObject(fecIndex);
					JSONObject attachedInterfaceObj = fecJsonObject.getJSONObject("attached-interface");
					System.out.println("attached interface "+attachedInterfaceObj.get("name"));
					
					JSONObject currentBinJsonObj = fecJsonObject.getJSONObject("current-bin").getJSONObject("statistics");
					System.out.println("pre fec ber "+currentBinJsonObj.getJSONObject("pre-fec-bit-error-rate").getJSONObject("bit-error-rate").getDouble("value"));
					System.out.println("q-factor "+currentBinJsonObj.getJSONObject("q-factor").getJSONObject("q-factor").getDouble("value"));
				}
			}
			
			fw.flush();
			fw.close();

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
	
	public void retrievePerformanceHistoryAll() {
		//retrievePerformanceHistoryOptical();
		//retrievePerformanceHistoryModem();
		retrievePerformanceHistoryOdu();
	}
	
	public void retrievePerformanceHistoryOptical() {


		try {

			ArrayList<String> ipList = new ArrayList<String>();
			
			//waveserver1
			ipList.add("100.65.244.99");
			//ipList.add("100.65.244.107");
			
			//waveserver2
			//ipList.add("100.65.244.100");
			//ipList.add("100.65.244.108");
			
			
			

			FileWriter fw = new FileWriter("e:\\Sify\\ciena\\WSParameters.csv");

			for (String ip : ipList) {


				HttpsURLConnection apiConn = getHttpsConnectionWithoutCertificate(
						"https://" + ip + "/restconf/data/waveserver-pm/optical-power-instances="+opticalInstances[0], "GET", null);

				JSONObject jsonObject = (JSONObject) getJson(apiConn);
				
				JSONObject instanceJsonObject = jsonObject.getJSONArray("optical-power-instances").getJSONObject(0);
				
				String port = instanceJsonObject.getJSONObject("attached-interface").getString("name");
				
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
				
				for(int i=0;i<allBins.length();i++) {
					JSONObject binObj = allBins.getJSONObject(i);
					
					String id = binObj.getJSONObject("id").getString("bin-name");
					String binName = id;
					
					if (id.equals("history")) {
						;
						binName = "History Bin #" +binObj.getInt("bin-number");
					}
					
					System.out.println(port + ", "+binName);
					
					System.out.println(port + ", Start Time : "+binObj.getJSONObject("state").getString("start-date-time"));
					if (binObj.getJSONObject("state").has("cleared-date-time")) {
						System.out.println(port + ", Cleared Time : "+binObj.getJSONObject("state").getString("cleared-date-time"));
					} else {
						System.out.println(port + ", End time : "+binObj.getJSONObject("state").getString("end-date-time"));
					}
					System.out.println(port + ",  : "+binName);
					
					
					JSONArray laneArray = binObj.getJSONObject("statistics").getJSONArray("optical-power");
					
					for(int laneIndex=0;laneIndex<laneArray.length();laneIndex++) {
						JSONObject laneObject = laneArray.getJSONObject(laneIndex);
						int laneNumber = laneObject.getInt("lane-number");
						System.out.println(port + ", Lane : "+laneNumber);
						
						String rxMin = laneObject.getJSONObject("rx-power").getJSONObject("minimum").optString("value");
						String rxMax = laneObject.getJSONObject("rx-power").getJSONObject("maximum").optString("value");
						String rxAvg = laneObject.getJSONObject("rx-power").getJSONObject("average").optString("value");
						
						System.out.println("Rx Power minimum :"+rxMin);
						System.out.println("Rx Power maximum :"+rxMax);
						System.out.println("Rx Power average :"+rxAvg);
						
						String txMin = laneObject.getJSONObject("tx-power").getJSONObject("minimum").optString("value");
						String txMax = laneObject.getJSONObject("tx-power").getJSONObject("maximum").optString("value");
						String txAvg = laneObject.getJSONObject("tx-power").getJSONObject("average").optString("value");
						
						System.out.println("Tx Power minimum :"+txMin);
						System.out.println("Tx Power maximum :"+txMax);
						System.out.println("Tx Power average :"+txAvg);
						
					}
					
					JSONObject laneObject = binObj.getJSONObject("statistics").getJSONObject("channel-power");
					
					String rxMin = laneObject.getJSONObject("rx-power").getJSONObject("minimum").optString("value");
					String rxMax = laneObject.getJSONObject("rx-power").getJSONObject("maximum").optString("value");
					String rxAvg = laneObject.getJSONObject("rx-power").getJSONObject("average").optString("value");
					
					System.out.println("Rx Channel Power minimum :"+rxMin);
					System.out.println("Rx Channel Power maximum :"+rxMax);
					System.out.println("Rx Channel Power average :"+rxAvg);
					
					System.out.println("-------------------------------");	
				}
				
							
			}
			
			fw.flush();
			fw.close();

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	
	}
	
	public void retrievePerformanceHistoryModem() {


		try {

			ArrayList<String> ipList = new ArrayList<String>();
			
			//waveserver1
			ipList.add("100.65.244.99");
			//ipList.add("100.65.244.107");
			
			//waveserver2
			//ipList.add("100.65.244.100");
			//ipList.add("100.65.244.108");
			
			
			

			FileWriter fw = new FileWriter("e:\\Sify\\ciena\\WSParameters.csv");

		//	fw.write("ip,aid,tx_tag,rx_actual");
		//	fw.write("\n");

			for (String ip : ipList) {


				HttpsURLConnection apiConn = getHttpsConnectionWithoutCertificate(
						"https://" + ip + "/restconf/data/waveserver-pm/modem-performance-instances="+modemInstances[0], "GET", null);

				JSONObject jsonObject = (JSONObject) getJson(apiConn);
				
				JSONObject instanceJsonObject = jsonObject.getJSONArray("modem-performance-instances").getJSONObject(0);
				
				String port = instanceJsonObject.getJSONObject("attached-interface").getString("name");
				
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
				
				for(int i=0;i<allBins.length();i++) {
					JSONObject binObj = allBins.getJSONObject(i);
					
					String id = binObj.getJSONObject("id").getString("bin-name");
					String binName = id;
					
					if (id.equals("history")) {
						;
						binName = binName + binObj.getInt("bin-number");
					}
					
					System.out.println(port + ", Bin : "+binName);
					
					System.out.println(port + ", Start Time : "+binObj.getJSONObject("state").getString("start-date-time"));
					if (binObj.getJSONObject("state").has("cleared-date-time")) {
						System.out.println(port + ", Cleared Time : "+binObj.getJSONObject("state").getString("cleared-date-time"));
					} else {
						System.out.println(port + ", End time : "+binObj.getJSONObject("state").getString("end-date-time"));
					}
					System.out.println(port + ",  : "+binName);
				
					JSONObject statsObject = binObj.getJSONObject("statistics");
					
					String preFecBER = statsObject.getJSONObject("pre-fec-bit-error-rate").getJSONObject("bit-error-rate").optString("value");
					String preFecBERMax = statsObject.getJSONObject("pre-fec-bit-error-rate").getJSONObject("maximum").optString("value");
					
					System.out.println("Pre-FEC BER :"+preFecBER);
					System.out.println("Pre-FEC BER maximum :"+preFecBERMax);
					
					String qFactor = statsObject.getJSONObject("q-factor").getJSONObject("q-factor").optString("value");
					String qFactorMin = statsObject.getJSONObject("q-factor").getJSONObject("minimum").optString("value");
					String qFactorMax = statsObject.getJSONObject("q-factor").getJSONObject("maximum").optString("value");
					
					System.out.println("Q-factor :"+qFactor);
					System.out.println("Q-factor Min :"+qFactorMin);
					System.out.println("Q-factor Max :"+qFactorMax);
					
					String fecFrameErrorCount = statsObject.getJSONObject("fec-error").getJSONObject("frame-error-count").optString("value");
					String fecFrameErrorCountSecond = statsObject.getJSONObject("fec-error").getJSONObject("frame-error-count-second").optString("value");
					String fecUncorrectedBlockCount = statsObject.getJSONObject("fec-error").getJSONObject("uncorrected-block-count").optString("value");
					String highCorrectionCountSecond = statsObject.getJSONObject("fec-error").getJSONObject("high-correction-count-seconds").optString("value");
					
					System.out.println("fecFrameErrorCount :"+fecFrameErrorCount);
					System.out.println("fecFrameErrorCountSecond :"+fecFrameErrorCountSecond);
					System.out.println("fecUncorrectedBlockCount :"+fecUncorrectedBlockCount);
					System.out.println("highCorrectionCountSecond :"+highCorrectionCountSecond);
					
					String dgdAvg = statsObject.getJSONObject("dgd").getJSONObject("average").optString("value");
					String dgdMax = statsObject.getJSONObject("dgd").getJSONObject("maximum").optString("value");
					
					System.out.println("dgdAvg :"+dgdAvg);
					System.out.println("dgdMax :"+dgdMax);
					
					String pdlAvg = statsObject.getJSONObject("pdl").getJSONObject("average").optString("value");
					String pdlMax = statsObject.getJSONObject("pdl").getJSONObject("maximum").optString("value");
					
					System.out.println("pdlAvg :"+pdlAvg);
					System.out.println("pdlMax :"+pdlMax);
					
					
					System.out.println("-------------------------------");	
				}
				
							
			}
			
			fw.flush();
			fw.close();

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	
	}
	
	
	public void retrievePerformanceHistoryOdu() {


		try {

			ArrayList<String> ipList = new ArrayList<String>();
			
			//waveserver1
			ipList.add("100.65.244.99");
			//ipList.add("100.65.244.107");
			
			//waveserver2
			//ipList.add("100.65.244.100");
			//ipList.add("100.65.244.108");
			
			
			

			FileWriter fw = new FileWriter("e:\\Sify\\ciena\\WSParameters.csv");

		//	fw.write("ip,aid,tx_tag,rx_actual");
		//	fw.write("\n");

			for (String ip : ipList) {


				HttpsURLConnection apiConn = getHttpsConnectionWithoutCertificate(
						"https://" + ip + "/restconf/data/waveserver-pm/odu-performance-instances="+oduInstances[0], "GET", null);

				JSONObject jsonObject = (JSONObject) getJson(apiConn);
				
				JSONObject instanceJsonObject = jsonObject.getJSONArray("odu-performance-instances").getJSONObject(0);
				
				String port = instanceJsonObject.getJSONObject("attached-interface").getString("name");
				
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
				
				for(int i=0;i<allBins.length();i++) {
					JSONObject binObj = allBins.getJSONObject(i);
					
					String id = binObj.getJSONObject("id").getString("bin-name");
					String binName = id;
					
					if (id.equals("history")) {
						;
						binName = binName + binObj.getInt("bin-number");
					}
					
					System.out.println(port + ", Bin : "+binName);
					
					System.out.println(port + ", Start Time : "+binObj.getJSONObject("state").getString("start-date-time"));
					if (binObj.getJSONObject("state").has("cleared-date-time")) {
						System.out.println(port + ", Cleared Time : "+binObj.getJSONObject("state").getString("cleared-date-time"));
					} else {
						System.out.println(port + ", End time : "+binObj.getJSONObject("state").getString("end-date-time"));
					}
					System.out.println(port + ",  : "+binName);
				
					JSONObject statsObject = binObj.getJSONObject("statistics");
					
					String oduBBE = statsObject.getJSONObject("background-block-errors").optString("value");
					String oduES = statsObject.getJSONObject("errored-seconds").optString("value");
					String oduSES = statsObject.getJSONObject("severely-errored-seconds").optString("value");
					String oduUAS = statsObject.getJSONObject("unavailable-seconds").optString("value");
					
					String oduBBEfarEnd = statsObject.getJSONObject("far-end").getJSONObject("background-block-errors").optString("value");
					String oduESfarEnd = statsObject.getJSONObject("far-end").getJSONObject("errored-seconds").optString("value");
					String oduSESfarEnd = statsObject.getJSONObject("far-end").getJSONObject("severely-errored-seconds").optString("value");
					String oduUASfarEnd = statsObject.getJSONObject("far-end").getJSONObject("unavailable-seconds").optString("value");
					
					System.out.println("oduBBE :"+oduBBE);
					System.out.println("oduES :"+oduES);
					System.out.println("oduSES :"+oduSES);
					System.out.println("oduUAS :"+oduUAS);
					
					System.out.println("oduBBEfarEnd :"+oduBBEfarEnd);
					System.out.println("oduESfarEnd :"+oduESfarEnd);
					System.out.println("oduSESfarEnd :"+oduSESfarEnd);
					System.out.println("oduUASfarEnd :"+oduUASfarEnd);
					
					System.out.println("-------------------------------");	
				}
				
							
			}
			
			fw.flush();
			fw.close();

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	
	}
	
	public void retrieveActiveAlarms() {


		try {

			ArrayList<String> ipList = new ArrayList<String>();
			
			//waveserver1
			ipList.add("100.65.244.99");
			//ipList.add("100.65.244.107");
			
			//waveserver2
			//ipList.add("100.65.244.100");
			//ipList.add("100.65.244.108");
			
			
			

			FileWriter fw = new FileWriter("e:\\Sify\\ciena\\WSParameters.csv");

		//	fw.write("ip,aid,tx_tag,rx_actual");
		//	fw.write("\n");

			for (String ip : ipList) {


				HttpsURLConnection apiConn = getHttpsConnectionWithoutCertificate(
						"https://" + ip + "/restconf/data/waveserver-alarms/active", "GET", null);

				JSONObject jsonObject = (JSONObject) getJson(apiConn);
				
				JSONArray alarmsArray = jsonObject.getJSONArray("active");
				
				
				for(int i=0;i<alarmsArray.length();i++) {
					JSONObject alarmObj = alarmsArray.getJSONObject(i);
					
					System.out.println("alarm-instance-id : "+alarmObj.getInt("alarm-instance-id"));
					System.out.println("acknowledged : "+alarmObj.getBoolean("acknowledged"));
					System.out.println("intermittent : "+alarmObj.getBoolean("intermittent"));
					System.out.println("alarm-table-id : "+alarmObj.getInt("alarm-table-id"));
					System.out.println("severity : "+alarmObj.getString("severity"));
					System.out.println("local-date-time : "+alarmObj.getString("local-date-time"));
					System.out.println("instance : "+alarmObj.getString("instance"));
					System.out.println("description : "+alarmObj.getString("description"));
					System.out.println("site-identifier : "+alarmObj.getInt("site-identifier"));
					System.out.println("group-identifier : "+alarmObj.getInt("group-identifier"));
					System.out.println("member-identifier : "+alarmObj.getInt("member-identifier"));
					System.out.println("-------------------------------");	
				}
						
			}
			
			fw.flush();
			fw.close();

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	
	}
	
	public ArrayList<InventoryDeviceDetails> retrieveInventoryAll(String ip,String restPrefix) {
		System.out.println("Retrieving using "+restPrefix+" with "+ip);
		if (restPrefix.equals("yang-api")) {
			return retrieveInventoryAllYang(ip);
		} else {
			return retrieveInventoryAll(ip);
		}
	}
	
	public ArrayList<InventoryDeviceDetails> retrieveInventoryAll(String ip) {
		
		ArrayList<InventoryDeviceDetails> inventoryList = new ArrayList<InventoryDeviceDetails>();
		

		try {
				
				inventoryList.addAll(retrieveDeviceDetails(ip,null));
				inventoryList.addAll(retrievePortDetails(ip,null));
				inventoryList.addAll(retrievePtpDetails(ip,null));
				inventoryList.addAll(retrieveXcvrDetails(ip,null));
				inventoryList.addAll(retrieveMgmtPortDetails(ip,null));
		
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		
		return inventoryList;
	}
	
	public ArrayList<InventoryDeviceDetails> retrieveInventoryAllYang(String ip) {
		
		ArrayList<InventoryDeviceDetails> inventoryList = new ArrayList<InventoryDeviceDetails>();
		

		try {
				
				inventoryList.addAll(retrieveDeviceDetailsYang(ip,null));
				inventoryList.addAll(retrievePortDetailsYang(ip,null));
//				inventoryList.addAll(retrievePtpDetails(ip,null));
//				inventoryList.addAll(retrieveXcvrDetails(ip,null));
//				inventoryList.addAll(retrieveMgmtPortDetails(ip,null));
		
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		
		return inventoryList;
	}

	
	public ArrayList<CienaWsDeviceInventory> retrieveDeviceDetails(String ip,Map sessionMap) {

		ArrayList<CienaWsDeviceInventory> deviceList = new ArrayList<CienaWsDeviceInventory>();
		try {

			Date currentDate = new Date();
			Timestamp date = new Timestamp(currentDate.getTime());
	
				HttpsURLConnection apiConn = getHttpsConnectionWithoutCertificate(
						"https://" + ip + "/restconf/data/waveserver-system", "GET", sessionMap);

				JSONObject receivedJsonObject = (JSONObject) getJson(apiConn);
				
				JSONObject systemObj = receivedJsonObject.getJSONObject("ciena-waveserver-system:waveserver-system");
				
				JSONObject idObj = systemObj.getJSONObject("id");
				JSONObject siteObj = idObj.getJSONObject("site");
				
				String siteId = siteObj.optString("id");
				String siteName = siteObj.optString("name");
				
				JSONObject groupObj = idObj.getJSONObject("group");
				
				
				String groupName = groupObj.optString("name");
				String groupDescription = groupObj.optString("description");
				
				
				JSONObject hostObj = systemObj.getJSONObject("host-name");
				
				
				String hostName = hostObj.optString("current-host-name");
				
				System.out.println(ip+", "+siteId+", "+siteName+", "+groupName+", "+groupDescription+", "+hostName);
				
				
			CienaWsDeviceInventory device = new CienaWsDeviceInventory(ip);
			
			device.setIp(ip);
			//device.setSiteId(siteid);
			device.setSiteName(siteName);
			device.setGroupDescription(groupDescription);
			device.setGroupName(groupName);
			device.setNodeName(hostName);
			
			device.setDate(date);
			
			System.out.println(ip+hostName+ " before inserting : Hostnames Map : "+hostNamesMap);
			
			synchronized (hostNamesMap) {
			hostNamesMap.put(ip, hostName);
		    }
			
			System.out.println(ip+hostName+ " after inserting : Hostnames Map : "+hostNamesMap);
			
			deviceList.add(device);
			
			System.out.println("Hostnames Map : "+hostNamesMap);
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return deviceList;
	}
	
	public ArrayList<CienaWsDeviceInventory> retrieveDeviceDetailsYang(String ip,Map sessionMap) {

		ArrayList<CienaWsDeviceInventory> deviceList = new ArrayList<CienaWsDeviceInventory>();
		try {

			Date currentDate = new Date();
			Timestamp date = new Timestamp(currentDate.getTime());
	
				HttpsURLConnection apiConn = getHttpsConnectionWithoutCertificate(
						"https://" + ip + "/yang-api/datastore/ws-system", "GET", sessionMap);

				JSONObject receivedJsonObject = (JSONObject) getJson(apiConn);
				
				JSONObject systemObj = receivedJsonObject.getJSONObject("data").getJSONObject("ciena-ws-system:ws-system");
				
				JSONObject idObj = systemObj.getJSONObject("id");
				JSONObject siteObj = idObj.getJSONObject("site");
				
				String siteId = siteObj.optString("id");
				String siteName = siteObj.optString("name");
				
				JSONObject groupObj = idObj.getJSONObject("group");
				
				
				String groupName = groupObj.optString("name");
				String groupDescription = groupObj.optString("description");
				
				
				JSONObject hostObj = systemObj.getJSONObject("host-name");
				
				
				String hostName = hostObj.optString("config-host-name");
				
				System.out.println(ip+", "+siteId+", "+siteName+", "+groupName+", "+groupDescription+", "+hostName);
				
				
			CienaWsDeviceInventory device = new CienaWsDeviceInventory(ip);
			
			device.setIp(ip);
			//device.setSiteId(siteid);
			device.setSiteName(siteName);
			device.setGroupDescription(groupDescription);
			device.setGroupName(groupName);
			device.setNodeName(hostName);
			
			device.setDate(date);
			
			System.out.println(ip+hostName+ " before inserting : Hostnames Map : "+hostNamesMap);
			
			synchronized (hostNamesMap) {
			hostNamesMap.put(ip, hostName);
		    }
			
			System.out.println(ip+hostName+ " after inserting : Hostnames Map : "+hostNamesMap);
			
			deviceList.add(device);
			
			System.out.println("Hostnames Map : "+hostNamesMap);
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return deviceList;
	}



	public ArrayList<InventoryDeviceDetails> retrievePortDetails(String ip,Map sessionMap) {
		//ArrayList<CienaWsPortInventory> portsList = new ArrayList<CienaWsPortInventory>();
		
		ArrayList<InventoryDeviceDetails> portsList = new ArrayList<InventoryDeviceDetails>();
		
		try {
		
		Date currentDate = new Date();
		Timestamp date = new Timestamp(currentDate.getTime());
		
		String nodename = hostNamesMap.get(ip);

		
		HttpsURLConnection apiConn = getHttpsConnectionWithoutCertificate(
				"https://" + ip + "/restconf/data/waveserver-ports", "GET", sessionMap);
		
		JSONObject receivedJsonObject = (JSONObject) getJson(apiConn);
		
		JSONObject jsonPorts = receivedJsonObject.getJSONObject("ciena-waveserver-port:waveserver-ports");	
		
		JSONArray jsonPortsList = jsonPorts.getJSONArray("ports");	
		
		
		for(int i=0;i<jsonPortsList.length();i++) {
			JSONObject jsonObj = jsonPortsList.getJSONObject(i);
			
			String portId= jsonObj.getString("port-id");
			
			JSONObject portIdObj = jsonObj.getJSONObject("id");
			String portName = "Port-"+portIdObj.getString("name");
			String portLabel = portIdObj.getString("label");
			String portType = portIdObj.getString("type");
			String portRate = portIdObj.getString("rate");
			String portSpeed = portIdObj.getString("speed");
			
			CienaWsPortInventory port = new CienaWsPortInventory(ip);
			
			
		//	String nodename = hostNamesMap.get(ip);
			
			port.setNodeIp(ip);
			port.setNodeName(nodename);
			port.setPortType(portType);
			port.setPortId(portId);
			port.setPortName(portName);
			port.setPortLabel(portLabel);
			port.setPortRate(portRate);
			port.setPortSpeed(portSpeed);
			
			port.setDate(date);

			
			portsList.add(port);
			
			if (portType.equals("otn") || portType.equals("OTUCn")) {
			JSONArray channelList = jsonObj.getJSONArray("channels");
			
			for(int j=0;j<channelList.length();j++) {
				jsonObj = channelList.getJSONObject(j);
				
			
			 portId= jsonObj.optString("channel-id");
			
			 portIdObj = jsonObj.getJSONObject("id");
			 portName = "Channel-"+portIdObj.getString("name");
			// portLabel = portIdObj.getString("label");
			 portType = "channel-" + portIdObj.getString("type");
			 portRate = portIdObj.getString("rate");
			 portSpeed = portIdObj.getString("speed");
			
			port = new CienaWsPortInventory(ip);
			
			
			//String nodename = hostNamesMap.get(ip);
			
			port.setNodeIp(ip);
			port.setNodeName(nodename);
			port.setPortType(portType);
			port.setPortId(portId);
			port.setPortName(portName);
			port.setPortLabel(portLabel);
			port.setPortRate(portRate);
			port.setPortSpeed(portSpeed);
			
			port.setDate(date);

			
			portsList.add(port);
			
			}

			} else if ((portType.equals("ethernet"))) {
				
				synchronized(circuitMap) {
				
				String circuitName = portLabel;
				
				String nameTokens[]=portLabel.split("-");
				//System.out.println("PortLabel : "+portLabel);
				
				CienaWsCircuitInventory circuitObj = circuitMap.get(circuitName);
				
				System.out.println("PortLabel : "+portLabel+",CircuitObj in map "+circuitObj);
				if (circuitObj == null) {
					
					circuitObj = new CienaWsCircuitInventory(ip);
				} 
				
				System.out.println("nameToken[0] : "+nameTokens[0]+", nodename : "+nodename);
				
				if (!nameTokens[0].isEmpty() && nodename.startsWith(nameTokens[0])) {
					
					//aend port
					circuitObj.setaEndDeviceIp(ip);
					circuitObj.setaEndNodeName(nodename);
					circuitObj.setaEndPoint(portName);
					circuitObj.setCircuitName(portLabel);
					
				} else {
					//zend port
					
					circuitObj.setzEndDeviceIp(ip);
					circuitObj.setzEndNodeName(nodename);
					circuitObj.setzEndPoint(portName);
					circuitObj.setCircuitName(portLabel);
					
				}
				
				if (!circuitObj.getaEndDeviceIp().isEmpty() && !circuitObj.getzEndDeviceIp().isEmpty()) {
					System.out.println("Storing into map : "+circuitObj.getaEndDeviceIp()+","+circuitObj.getzEndDeviceIp());
					System.out.println("Storing into map circuit : "+circuitObj.getaEndNodeName() + ":" + circuitObj.getaEndPoint() + ":" +circuitObj.getzEndNodeName() + ":" + circuitObj.getzEndPoint());
					String circuitKey = circuitObj.getaEndNodeName() + ":" + circuitObj.getaEndPoint() + ":" +circuitObj.getzEndNodeName() + ":" + circuitObj.getzEndPoint() ;
					circuitObj.setCircuitKey(circuitKey);
					circuitObj.setDate(date);
					
					portsList.add(circuitObj);
				}
				
								
				circuitMap.put(circuitName, circuitObj);
			}
				
			}

		}
		
		} catch(Exception e) {
			logger.error("Unable to retrieve port details ",e);
		}
		
		return portsList;
	}
	
	
	public ArrayList<InventoryDeviceDetails> retrievePortDetailsYang(String ip,Map sessionMap) {
		//ArrayList<CienaWsPortInventory> portsList = new ArrayList<CienaWsPortInventory>();
		
		ArrayList<InventoryDeviceDetails> portsList = new ArrayList<InventoryDeviceDetails>();
		
		try {
		
		Date currentDate = new Date();
		Timestamp date = new Timestamp(currentDate.getTime());
		
		String nodename = hostNamesMap.get(ip);

		
		HttpsURLConnection apiConn = getHttpsConnectionWithoutCertificate(
				"https://" + ip + "/yang-api/datastore/ws-ports", "GET", sessionMap);
		
		JSONObject receivedJsonObject = (JSONObject) getJson(apiConn);
		
		JSONObject jsonPorts = receivedJsonObject.getJSONObject("data").getJSONObject("ciena-ws-port:ws-ports");	
		
		JSONArray jsonPortsList = jsonPorts.getJSONArray("ports");	
		
		
		for(int i=0;i<jsonPortsList.length();i++) {
			JSONObject jsonObj = jsonPortsList.getJSONObject(i);
			
			String portId= jsonObj.optString("port-id");
			
			JSONObject portIdObj = jsonObj.getJSONObject("id");
			String portName = "Port-"+portId;
			String portLabel = portIdObj.getString("description");
			String portType = portIdObj.getString("interface-type");
			String portRate = "";
			String portSpeed = "";
			
			CienaWsPortInventory port = new CienaWsPortInventory(ip);
			
			
		//	String nodename = hostNamesMap.get(ip);
			
			port.setNodeIp(ip);
			port.setNodeName(nodename);
			port.setPortType(portType);
			port.setPortId(portId);
			port.setPortName(portName);
			port.setPortLabel(portLabel);
			port.setPortRate(portRate);
			port.setPortSpeed(portSpeed);
			
			port.setDate(date);

			
			portsList.add(port);
			
			if ((portType.equals("uni"))) {
				
				synchronized(circuitMap) {
				
				String circuitName = portLabel;
				
				String nameTokens[]=portLabel.split("-");
				//System.out.println("PortLabel : "+portLabel);
				
				CienaWsCircuitInventory circuitObj = circuitMap.get(circuitName);
				
				System.out.println("PortLabel : "+portLabel+",CircuitObj in map "+circuitObj);
				if (circuitObj == null) {
					
					circuitObj = new CienaWsCircuitInventory(ip);
				} 
				
				System.out.println("nameToken[0] : "+nameTokens[0]+", nodename : "+nodename);
				
				if (!nameTokens[0].isEmpty() && nodename.startsWith(nameTokens[0])) {
					
					//aend port
					circuitObj.setaEndDeviceIp(ip);
					circuitObj.setaEndNodeName(nodename);
					circuitObj.setaEndPoint(portName);
					circuitObj.setCircuitName(portLabel);
					
				} else {
					//zend port
					
					circuitObj.setzEndDeviceIp(ip);
					circuitObj.setzEndNodeName(nodename);
					circuitObj.setzEndPoint(portName);
					circuitObj.setCircuitName(portLabel);
					
				}
				
				
				
				if (!circuitObj.getaEndDeviceIp().isEmpty() && !circuitObj.getzEndDeviceIp().isEmpty()) {
					System.out.println("Yang Storing into map : "+circuitObj.getaEndDeviceIp()+","+circuitObj.getzEndDeviceIp());
					System.out.println("Yang Storing into map circuit : "+circuitObj.getaEndNodeName() + ":" + circuitObj.getaEndPoint() + ":" +circuitObj.getzEndNodeName() + ":" + circuitObj.getzEndPoint());
					String circuitKey = circuitObj.getaEndNodeName() + ":" + circuitObj.getaEndPoint() + ":" +circuitObj.getzEndNodeName() + ":" + circuitObj.getzEndPoint() ;
					circuitObj.setCircuitKey(circuitKey);
					circuitObj.setDate(date);
					
					portsList.add(circuitObj);
				}
				
				circuitMap.put(circuitName, circuitObj);
			}
				
			}

		}
		
		} catch(Exception e) {
			logger.error("Unable to retrieve port details ",e);
		}
		
		return portsList;
	}

	
	
	public ArrayList<CienaWsPortInventory> retrievePtpDetails(String ip,Map sessionMap) {
		ArrayList<CienaWsPortInventory> portsList = new ArrayList<CienaWsPortInventory>();
		
		try {
		
		Date currentDate = new Date();
		Timestamp date = new Timestamp(currentDate.getTime());
		
		String nodename = hostNamesMap.get(ip);

		
		HttpsURLConnection apiConn = getHttpsConnectionWithoutCertificate(
				"https://" + ip + "/restconf/data/waveserver-ptps", "GET", sessionMap);
		
		JSONObject receivedJsonObject = (JSONObject) getJson(apiConn);
		
		JSONObject jsonPorts = receivedJsonObject.getJSONObject("ciena-waveserver-ptp:waveserver-ptps");	
		
		JSONArray jsonPortsList = jsonPorts.getJSONArray("ptps");	
		
		
		for(int i=0;i<jsonPortsList.length();i++) {
			JSONObject jsonObj = jsonPortsList.getJSONObject(i);
			
			String portId= jsonObj.getString("ptp-id");
			
			JSONObject portIdObj = jsonObj.getJSONObject("id");
			String portName = "PTP-"+portIdObj.getString("name");
			//String portLabel = portIdObj.getString("label");
			String portType = "ptp";
			//String portRate = portIdObj.getString("rate");
			//String portSpeed = portIdObj.getString("speed");
			
			CienaWsPortInventory port = new CienaWsPortInventory(ip);
			
			
		//	String nodename = hostNamesMap.get(ip);
			System.out.println("ip "+ip+" hostname "+nodename);
			
			port.setNodeIp(ip);
			port.setNodeName(nodename);
			port.setPortType(portType);
			port.setPortId(portId);
			port.setPortName(portName);
			
			
			port.setDate(date);

			
			portsList.add(port);


		}
		
		} catch(Exception e) {
			logger.error("Unable to retrieve ptp details ",e);
		}
		
		return portsList;
	}
	
	
	
	public ArrayList<CienaWsPortInventory> retrieveXcvrDetails(String ip,Map sessionMap) {
		ArrayList<CienaWsPortInventory> portsList = new ArrayList<CienaWsPortInventory>();
		
		try {
		
		Date currentDate = new Date();
		Timestamp date = new Timestamp(currentDate.getTime());
		
		String nodename = hostNamesMap.get(ip);

		
		HttpsURLConnection apiConn = getHttpsConnectionWithoutCertificate(
				"https://" + ip + "/restconf/data/waveserver-xcvrs", "GET", sessionMap);
		
		JSONObject receivedJsonObject = (JSONObject) getJson(apiConn);
		
		JSONObject jsonPorts = receivedJsonObject.getJSONObject("ciena-waveserver-xcvr:waveserver-xcvrs");	
		
		JSONArray jsonPortsList = jsonPorts.getJSONArray("xcvrs");	
		
		
		for(int i=0;i<jsonPortsList.length();i++) {
			JSONObject jsonObj = jsonPortsList.getJSONObject(i);
			
			String portId= jsonObj.getString("xcvr-id");
			
			JSONObject portIdObj = jsonObj.getJSONObject("id");
			String portName = "XCVR-"+portIdObj.getString("name");
			//String portLabel = portIdObj.getString("label");
			String portType = "xcvr";
			//String portRate = portIdObj.getString("rate");
			//String portSpeed = portIdObj.getString("speed");
			
			CienaWsPortInventory port = new CienaWsPortInventory(ip);
			
			
		//	String nodename = hostNamesMap.get(ip);
			System.out.println("ip "+ip+" hostname "+nodename);
			
			port.setNodeIp(ip);
			port.setNodeName(nodename);
			port.setPortType(portType);
			port.setPortId(portId);
			port.setPortName(portName);
			
			
			port.setDate(date);

			
			portsList.add(port);


		}
		
		} catch(Exception e) {
			logger.error("Unable to retrieve ptp details ",e);
		}
		
		return portsList;
	}


	public ArrayList<CienaWsPortInventory> retrieveMgmtPortDetails(String ip,Map sessionMap) {
		ArrayList<CienaWsPortInventory> portsList = new ArrayList<CienaWsPortInventory>();
		
		try {
		
		Date currentDate = new Date();
		Timestamp date = new Timestamp(currentDate.getTime());
		
		String nodename = hostNamesMap.get(ip);

		
		HttpsURLConnection apiConn = getHttpsConnectionWithoutCertificate(
				"https://" + ip + "/restconf/data/waveserver-chassis", "GET", sessionMap);
		
		JSONObject receivedJsonObject = (JSONObject) getJson(apiConn);
		
		JSONObject jsonPorts = receivedJsonObject.getJSONObject("ciena-waveserver-chassis:waveserver-chassis");	
		
		JSONArray jsonPortsList = jsonPorts.getJSONArray("management-port");	
		
		
		for(int i=0;i<jsonPortsList.length();i++) {
			JSONObject jsonObj = jsonPortsList.getJSONObject(i);
			
			String portId= jsonObj.optString("index");
			
			JSONObject portIdObj = jsonObj.getJSONObject("id");
			String portName = portIdObj.getString("name");
			//String portLabel = portIdObj.getString("label");
			
			
			JSONObject propertiesObj= jsonObj.getJSONObject("properties");
			
			String portType = "mgmt-"+propertiesObj.getString("type");
			//String portRate = propertiesObj.getString("rate");
			String portSpeed = propertiesObj.optString("speed");
			
			CienaWsPortInventory port = new CienaWsPortInventory(ip);
			
			
		//	String nodename = hostNamesMap.get(ip);
			System.out.println("ip "+ip+" hostname "+nodename);
			
			port.setNodeIp(ip);
			port.setNodeName(nodename);
			port.setPortType(portType);
			port.setPortId(portId);
			port.setPortName(portName);
			port.setPortSpeed(portSpeed);
			
			
			port.setDate(date);

			
			portsList.add(port);


		}
		
		} catch(Exception e) {
			logger.error("Unable to retrieve ILAN port details ",e);
		}
		
		return portsList;
	}

	

	
	
}


class InvalidCertificateHostVerifierCienaWS implements HostnameVerifier{
	@Override
	public boolean verify(String paramString, SSLSession paramSSLSession) {
		return true;
	}
	
	

}

