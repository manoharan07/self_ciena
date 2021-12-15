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
 

public class CienaClient {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("ciena");

	private CienaProperties cienaProperties = new CienaProperties();
	
	public static HashMap<String,String> hostNamesMap = new HashMap<String,String>();
	static String previousSubnet="";
	
	//snc-ep-state
	public static String SNC_EP_STATE_ORIGIN="ORIG_WORKING";
	public static String SNC_EP_STATE_TERMINATION="TERM_WORKING_CONNECTED";
	
	public static HashMap<String,String> networkAdjacencySncMap = new HashMap<String,String>();
	public static HashMap<String,String> portAdjMap = new HashMap<String,String>();
	public static HashMap<String,String> opsCardMap = new HashMap<String,String>();
	
	

	public static void main(String[] args)  {
	/*	String aid="ETH10G-1-9-4";
		
		String shelfslotport= aid.substring(aid.indexOf("-"));
		
		System.out.println(shelfslotport);*/
		
		CienaClient cienaClient  = new CienaClient();
	
	//	cienaClient.retrieveInventoryAll();
		
	//	System.out.println(adjPortMap);
	//	System.out.println(adjSncMap);
	//	cienaClient.retrieveDeviceDetails();
		//cienaClient.retrieveSNCDetails();
		//cienaClient.retrieveNetworkDetails();
		//cienaClient.retrieveEquipmentDetails();
	//	cienaClient.retrievePortDetails();
		
		cienaClient.retrievePerformanceDetails();
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
	/*		String authStr = versaProperties.get("versa.url.username") +":"+versaProperties.get("versa.url.password");
			byte[] authEncoded = Base64.getEncoder().encode(authStr.getBytes());
*/
			conn.setRequestMethod(methodType);
			conn.setDoOutput(true);
	//		conn.setRequestProperty("Authorization", "Basic " + new String(authEncoded)); 
//			conn.setSSLSocketFactory(sslContext.getSocketFactory());
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("X-XSRF-TOKEN", sessionMap.get("token"));
			conn.setRequestProperty("Cookie", "-http-session-="+sessionMap.get("cookie"));
		//	conn.setRequestProperty("Cache-Control","no-cache");
			conn.setRequestProperty("Accept", "application/json");
			conn.setConnectTimeout(cienaProperties.get("ciena.api.read.timeout",20000));
			conn.setReadTimeout(cienaProperties.get("ciena.api.read.timeout",20000));

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
	//	logger.info(mapValue.toString());
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
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setInstanceFollowRedirects(false);
			conn.setReadTimeout(cienaProperties.get("ciena.api.read.timeout",10000));
			conn.setConnectTimeout(cienaProperties.get("ciena.api.read.timeout",10000));
			
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
				logger.info("Succeeded Login Response Code : "+conn.getResponseCode()+", Response Time (seconds) : " + ((System.currentTimeMillis() - startTime) * 0.001) + ", URL : "+ url );
			}else{
				//System.out.println("Failed Login Response Code : "+conn.getResponseCode()+", Response Time (seconds) : " + ((System.currentTimeMillis() - startTime) * 0.001) + ", URL : "+ url );
				System.out.println(conn.getResponseMessage() + " : " + url);
				logger.error(conn.getResponseMessage() + " : " + url);
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

	
	public void retrieveHosts(String ip, Map sessionMap) {

		try {

					
				HttpsURLConnection apiConn = getHttpsConnectionWithoutCertificate(
						"https://" + ip + ":8443/api/v1/datastore/comms/nodes", "GET", sessionMap);

				JSONArray jsonArray = (JSONArray) getJson(apiConn);
				
				for(int i=0;i<jsonArray.length();i++) {

				JSONObject jsonObjSid = jsonArray.getJSONObject(i);

				String nodeName = jsonObjSid.optString("tid");
				String shelfIp = jsonObjSid.optString("ip-addr");

				hostNamesMap.put(shelfIp, nodeName);
				
				System.out.println(ip + "," + nodeName);
		
			}
			
		//	fw.flush();
		//	fw.close();

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
	
	
	
	
	public ArrayList<InventoryDeviceDetails> retrieveInventoryAll(String ip) {
		
		ArrayList<InventoryDeviceDetails> inventoryList = new ArrayList<InventoryDeviceDetails>();
		

		try {
			
				HttpsURLConnection loginConn = doLoginUrlEncoded("https://" + ip + ":8443/api/login");
				
				if (loginConn==null) {
					System.out.println("Unable to connect with "+ip+", continuing with next ip");
					//continue;
					return inventoryList;
				}

				Map<String, String> sessionMap = getSessionId(loginConn);
				
				
				
				
				inventoryList.addAll(retrieveDeviceDetails(ip,sessionMap));
				inventoryList.addAll(retrieveEquipmentDetails(ip,sessionMap));
				inventoryList.addAll(retrieveNetworkDetails(ip,sessionMap));
				inventoryList.addAll(retrieveAdjacencyDetails(ip,sessionMap));
				inventoryList.addAll(retrieveSNCDetails(ip,sessionMap));
				inventoryList.addAll(retrievePortDetails(ip,sessionMap));
				inventoryList.addAll(retrieveOptMonPortDetails(ip,sessionMap));
				inventoryList.addAll(retrieveAmpPortDetails(ip,sessionMap));
				inventoryList.addAll(retrieveOscPortDetails(ip,sessionMap));
				
				
				
		
		
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		
		return inventoryList;
	}
	

	
	
	public ArrayList<CienaDeviceInventory> retrieveDeviceDetails(String ip,Map sessionMap) {

		ArrayList<CienaDeviceInventory> deviceList = new ArrayList<CienaDeviceInventory>();
		try {

			Date currentDate = new Date();
			Timestamp date = new Timestamp(currentDate.getTime());
	
				HttpsURLConnection apiConn = getHttpsConnectionWithoutCertificate(
						"https://" + ip + ":8443/api/v1/datastore/system", "GET", sessionMap);

				JSONObject jsonObj = (JSONObject) getJson(apiConn);

				JSONObject jsonObjSid = jsonObj.getJSONObject("extsid");

				String sid = jsonObjSid.optString("extended-sid");
				String shelf = jsonObjSid.optString("shelf");

				JSONObject jsonObjNe = jsonObj.getJSONObject("netype");

				String manuf = jsonObjNe.optString("manuf");
				String model = jsonObjNe.optString("model");
				
				HttpsURLConnection shelfConn = getHttpsConnectionWithoutCertificate(
						"https://" + ip + ":8443/api/v1/datastore/shelves", "GET", sessionMap);

				JSONObject shelfObjList = (JSONObject) getJson(shelfConn);
				
				JSONObject shelfObj = shelfObjList.getJSONArray("shelf").getJSONObject(0);
				
				String siteid = shelfObj.optString("site-id");
				String sitename = shelfObj.optString("site-name");
				String location = shelfObj.optString("location");
				String subnetname = shelfObj.optString("subnet-name");
				
				/*	if (!previousSubnet.equals(subnetname)) {
				System.out.println("############subnet : "+subnetname);
				previousSubnet = subnetname;
				retrieveHosts(ip,sessionMap);
			}*/
			
			String nodeName = hostNamesMap.get(ip);
		
			if (nodeName == null) {
				retrieveHosts(ip,sessionMap);
				nodeName = hostNamesMap.get(ip);
			}
			
			CienaDeviceInventory device = new CienaDeviceInventory(ip);
			
			device.setIp(ip);
			device.setExtendedSid(sid);
			device.setShelf(shelf);
			device.setManufacturer(manuf);
			device.setModel(model);
			device.setSiteId(siteid);
			device.setSiteName(sitename);
			device.setLocation(location);
			device.setSubnetName(subnetname);
			device.setNodeName(nodeName);
			
			device.setDate(date);
			
			deviceList.add(device);
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return deviceList;
	}
	
	
	public ArrayList<CienaNetworkInventory> retrieveNetworkDetails(String ip,Map sessionMap) {

		ArrayList<CienaNetworkInventory> networkList = new ArrayList<CienaNetworkInventory>();
		try {

			Date currentDate = new Date();
			Timestamp date = new Timestamp(currentDate.getTime());
		
				HttpsURLConnection apiConn = getHttpsConnectionWithoutCertificate(
						"https://" + ip + ":8443/api/v1/datastore/network", "GET", sessionMap);

				JSONObject jsonObject = (JSONObject) getJson(apiConn);
				
				JSONObject adjObject = jsonObject.getJSONObject("adjacency");
				
				JSONArray adjArray = adjObject.getJSONArray("all");
				
				for(int i=0;i<adjArray.length();i++){

							
				JSONObject shelfObj = adjArray.getJSONObject(i);
				
				String aid = shelfObj.optString("shelf");
				String txTag = shelfObj.optString("tx-tag");
				String rxActual = shelfObj.optString("rx-actual");
				String hostName = hostNamesMap.get(ip);
				
				//adjPortMap.put(hostName+":"+aid, rxActual );
				
				if (!rxActual.isEmpty()) {
				
				if (!portAdjMap.containsKey(rxActual)) {
					portAdjMap.put( rxActual, hostName+":"+aid );  // might need to rework
					System.out.println("@@@@@@@@@@@ adjacency : "+aid +", "+rxActual+ " is mapped to "+portAdjMap.get(rxActual));
				} else {
					System.out.println("############ adjacency : "+aid +", "+rxActual+ " is already mapped to "+portAdjMap.get(rxActual));
				}
				
				}
				System.out.println(hostName + "," +ip + "," + aid + "," + txTag + "," + rxActual );
				
				CienaNetworkInventory network = new CienaNetworkInventory(ip);
				
				network.setNodeIp(ip);
				network.setAdjId(aid);
				network.setTxTag(txTag);
				network.setRxActual(rxActual);
				network.setNodeName(hostName);
				
				network.setDate(date);
				
				networkList.add(network);
			}

			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return networkList;
	}
	
	
	public ArrayList<CienaAdjacencyInventory> retrieveAdjacencyDetails(String ip,Map sessionMap) {

		ArrayList<CienaAdjacencyInventory> networkList = new ArrayList<CienaAdjacencyInventory>();
		try {

			Date currentDate = new Date();
			Timestamp date = new Timestamp(currentDate.getTime());
		
				HttpsURLConnection apiConn = getHttpsConnectionWithoutCertificate(
						"https://" + ip + ":8443/api/v1/datastore/adjacencies/adjacency/ALL", "GET", sessionMap);

				//JSONObject jsonObject = (JSONObject) getJson(apiConn);
				
				//JSONObject adjObject = jsonObject.getJSONObject("adjacency");
				
				JSONArray adjArray = (JSONArray) getJson(apiConn);
				
				for(int i=0;i<adjArray.length();i++){

							
				JSONObject shelfObj = adjArray.getJSONObject(i);
				
				String aid = shelfObj.optString("aid");
				String provFeAddr = shelfObj.optString("prov-fe-addr");
				String discFeAddr = shelfObj.optString("disc-fe-addr");
				String portLabel = shelfObj.optString("port-label");
				
				String hostName = hostNamesMap.get(ip);
				
				//adjPortMap.put(hostName+":"+aid, rxActual );
				
				if (portLabel.startsWith("OPS") && !portLabel.contains("Common")) {
				
					String key = hostName + ":" + portLabel.substring(0,4) +":"+ aid;
				 
					opsCardMap.put(key, "");
					System.out.println("*****OPS********** "+hostName + "," +ip + "," + aid + "," + provFeAddr + "," + portLabel );
					
				
				}
				
				
				CienaAdjacencyInventory adjacency = new CienaAdjacencyInventory(ip);
				
				adjacency.setNodeIp(ip);
				adjacency.setAdjId(aid);
				adjacency.setProvFeAddr(provFeAddr);
				adjacency.setDiscFeAddr(discFeAddr);
				adjacency.setPortLabel(portLabel);
				
				
				adjacency.setNodeName(hostName);
				
				adjacency.setDate(date);
				
				networkList.add(adjacency);
			}
				
				System.out.println("Before assigning values "+opsCardMap);
				
				for(int i=0;i<adjArray.length();i++){

					
					JSONObject shelfObj = adjArray.getJSONObject(i);
					
					String aid = shelfObj.optString("aid");
					String provFeAddr = shelfObj.optString("prov-fe-addr");
					String portLabel = shelfObj.optString("port-label");
					
					String hostName = hostNamesMap.get(ip);
					
					//adjPortMap.put(hostName+":"+aid, rxActual );
					
					if (portLabel.startsWith("OPS") && portLabel.contains("Common Out")) {
					
						for(String opsPort:opsCardMap.keySet()) {
							String keyStart = opsPort.substring(0, opsPort.indexOf("OPS")+4);
							System.out.println("OPS keystart for checking----------- "+keyStart);
							
							if (keyStart.equals(hostName+":"+portLabel.substring(0, 4)))
									{
										opsCardMap.put(opsPort, provFeAddr);
									}
						}
					 
						
						
					
					}
				
				}
				
			System.out.println("After assigning values "+opsCardMap);


			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return networkList;
	}

	
	public ArrayList<CienaSNCInventory> retrieveSNCDetails(String ip,Map sessionMap) {
		long currentTime = System.currentTimeMillis();
		
		ArrayList<CienaSNCInventory> sncList = new ArrayList<CienaSNCInventory>();
		try {
			
			Date currentDate = new Date();
			Timestamp date = new Timestamp(currentDate.getTime());

					HttpsURLConnection apiConn = getHttpsConnectionWithoutCertificate(
							"https://" + ip + ":8443/api/v1/datastore/osrp/snc/ALL", "GET", sessionMap);
					
					
					Object jsonArr = getJson(apiConn);

					// logger.info("object result....." + jsonArr.toString());

					if (jsonArr instanceof JSONArray) {
						for (int iter = 0; iter < ((JSONArray) jsonArr).length(); iter++) {

							String aid = "";
							String label = "";
							String localNode = "";
							String localEndPoint = "";
							String orgDropSideState = "";
							String orgNetSideState = "";
							String peerOrigin = "";
							String peerSnc = "";
							String protType = "";
							String remoteEndPt = "";
							String remoteNode = "";
							String wvlGrid = "";
							String sncLineType = "";
							String terminDropProtState = "";
							String terminNetProtState = "";
							String type = "";
							Double freq = 0.0;
							String dtlUuid = "";
							String pst = "";
							String cktId = "";
							String priority = "";
							String sncEpState = "";
							
							localNode = hostNamesMap.get(ip);
							
							String sncKey ="";

							try {
								//JSONObject sncDetJsonObject = ((JSONObject) jsonArr).get(iter);
								//Gson gson = new Gson();
								//String json = gson.toJson(sncDetJsonObject);
								//JSONObject resultJSONObject = new JSONObject(json);
								
								JSONObject mapJSONObject = ((JSONArray) jsonArr).getJSONObject(iter);

								/*Object mapObject = resultJSONObject.get("map");
								System.out.println("mapObject.toString()......" + mapObject.toString());
								JSONObject mapJSONObject = new JSONObject(mapObject.toString());
								// System.out.println("aid value...." + mapJSONObject.get("aid"));
*/
								if (mapJSONObject.has("aid"))
									aid = (String) mapJSONObject.get("aid");
								if (mapJSONObject.has("label"))
									label = (String) mapJSONObject.get("label");
								if (mapJSONObject.has("local-end-point"))
									localEndPoint = (String) mapJSONObject.get("local-end-point");
								if (mapJSONObject.has("origin-drop-side-prot-state"))
									orgDropSideState = (String) mapJSONObject.get("origin-drop-side-prot-state");
								if (mapJSONObject.has("origin-net-side-prot-state"))
									orgNetSideState = (String) mapJSONObject.get("origin-net-side-prot-state");
								if (mapJSONObject.has("peer-origin"))
									peerOrigin = (String) mapJSONObject.get("peer-origin");
								if (mapJSONObject.has("peer-snc"))
									peerSnc = (String) mapJSONObject.get("peer-snc");
								if (mapJSONObject.has("prot-type"))
									protType = (String) mapJSONObject.get("prot-type");
								if (mapJSONObject.has("remote-end-point"))
									remoteEndPt = (String) mapJSONObject.get("remote-end-point");
								if (mapJSONObject.has("remote-node"))
									remoteNode = (String) mapJSONObject.get("remote-node");
								if (mapJSONObject.has("wvl-grid"))
									wvlGrid = (String) mapJSONObject.get("wvl-grid");
								if (mapJSONObject.has("snc-line-type"))
									sncLineType = (String) mapJSONObject.get("snc-line-type");
								if (mapJSONObject.has("termin-drop-side-prot-state"))
									terminDropProtState = (String) mapJSONObject.get("termin-drop-side-prot-state");
								if (mapJSONObject.has("termin-net-side-prot-state"))
									terminNetProtState = (String) mapJSONObject.get("termin-net-side-prot-state");
								if (mapJSONObject.has("type"))
									type = (String) mapJSONObject.get("type");
								if (mapJSONObject.has("frequency"))
									freq = mapJSONObject.getDouble("frequency");
								if (mapJSONObject.has("dtl-uuid"))
									dtlUuid = (String) mapJSONObject.get("dtl-uuid");
								if (mapJSONObject.has("pst"))
									pst = (String) mapJSONObject.get("pst");
								if (mapJSONObject.has("cktid"))
									cktId = (String) mapJSONObject.get("cktid");
								if (mapJSONObject.has("priority"))
									priority = (String) mapJSONObject.get("priority");
								if (mapJSONObject.has("snc-ep-state"))
									sncEpState = (String) mapJSONObject.get("snc-ep-state");
								
								CienaSNCInventory snc = new CienaSNCInventory(ip);
								
								if (sncEpState.equals(SNC_EP_STATE_ORIGIN)) {
									sncKey=localNode+":"+localEndPoint+":"+remoteNode+":"+remoteEndPt;
									
									snc.setSncKey(sncKey);
									
									snc.setaEndDeviceIp(ip);
									snc.setaEndNodeName(localNode);
									snc.setaEndPoint(localEndPoint);
									snc.setaEndSncId(aid);
									snc.setaEndEpState(sncEpState);
									
									snc.setzEndNodeName(remoteNode);
									snc.setzEndPoint(remoteEndPt);
									
								} else if (sncEpState.equals(SNC_EP_STATE_TERMINATION)) {
									sncKey=remoteNode+":"+remoteEndPt +":"+ localNode+":"+localEndPoint;
									
									snc.setSncKey(sncKey);
									
									snc.setzEndDeviceIp(ip);
									snc.setzEndNodeName(localNode);
									snc.setzEndPoint(localEndPoint);
									snc.setzEndSncId(aid);
									snc.setzEndEpState(sncEpState);
									
									snc.setaEndNodeName(remoteNode);
									snc.setaEndPoint(remoteEndPt);
								}
								
								snc.setLabel(label);
								snc.setCktid(cktId);
								snc.setFrequency(String.valueOf(freq));
								snc.setSncLineType(sncLineType);
								
								snc.setDate(date);
								
								networkAdjacencySncMap.put(localNode+":"+localEndPoint, sncKey);
								
								
								
								sncList.add(snc);

								
							} catch (Exception e) {
								
								e.printStackTrace();
							}
							// break;
						}
					}
				} catch (Exception e) {
					logger.error("Exception occured for the ip while retrieving snc details:" + ip);
					e.printStackTrace();
				}
		
		return sncList;
			
	}

	
	
	public ArrayList<CienaEquipmentInventory> retrieveEquipmentDetails(String ip,Map sessionMap) {

		ArrayList<CienaEquipmentInventory> equipmentList = new ArrayList<CienaEquipmentInventory>();
		try {

			Date currentDate = new Date();
			Timestamp date = new Timestamp(currentDate.getTime());
		
				HttpsURLConnection apiConn = getHttpsConnectionWithoutCertificate(
						"https://" + ip + ":8443/api/v1/datastore/equipment/inventory/ALL", "GET", sessionMap);

				JSONArray equipArray = (JSONArray) getJson(apiConn);

				for(int i=0;i<equipArray.length();i++){

							
				JSONObject shelfObj = equipArray.getJSONObject(i);
				
				String aid = shelfObj.optString("aid");
				String ctype = shelfObj.optString("ctype");
				String ctypeStr = ctype.replace(",", " | ");
				
				String nodeName = hostNamesMap.get(ip);
				
				if (nodeName == null) {
					retrieveHosts(ip,sessionMap);
					nodeName = hostNamesMap.get(ip);
				}
				
				CienaEquipmentInventory equipment = new CienaEquipmentInventory(ip);
				equipment.setNodeIp(ip);
				equipment.setNodeName(nodeName);
				equipment.setCardId(aid);
				equipment.setCardType(ctypeStr);
				
				equipment.setDate(date);
				
				equipmentList.add(equipment);
				}


		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return equipmentList;
	}
	
	
	
	public ArrayList<CienaPortInventory> retrievePortDetails(String ip,Map sessionMap) {
		
		
		long portStartTime = System.currentTimeMillis();
		
		ArrayList<CienaPortInventory> portsList = new ArrayList<CienaPortInventory>();
	
		try {
			
			Date currentDate = new Date();
			Timestamp date = new Timestamp(currentDate.getTime());

		//	List<String> ipList = getDeviceIpList();

		
			String aid = "";
			String clfi = "";
			String txWvlngthProv = "";
			String oduTxTti = "";
			String otuTxTti = "";
			String otuRxExpTti = "";
			String oduRxExpTti = "";
			String ochTxActHighPower="";
			String txWvlngthMin="";
			String label="";
			String ochTxMaxPower="";
			String ochRxActLowPower="";
			String rxActLnPwr="";
			String pst="";
			String ochRxActHighPower="";
			String ochTxMinPower="";
			String ochTxActLowPower="";
			String ochRxMaxPower="";
			String ochRxMinPower="";
			String txWvlngthSpacing="";
			String txActLnPwr="";
			String condType="";
			String txWvlngthMax="";
//			String portType = "";

		
				try {
						

					// https://100.66.28.116:8443/api/v1/datastore/port
					HttpsURLConnection apiConn = getHttpsConnectionWithoutCertificate(
							"https://" + ip + ":8443/api/v1/datastore/port", "GET", sessionMap);

					Object jsonArr = getJson(apiConn);
					// logger.info("object result....." + jsonArr.toString());
					
					retrieveSpecificPortDetails(jsonArr, portsList, "eth10g",  ip, aid, clfi, txWvlngthProv, oduTxTti, otuTxTti,
							otuRxExpTti, oduRxExpTti,ochTxActHighPower,txWvlngthMin,label,ochTxMaxPower,ochRxActLowPower,rxActLnPwr,pst,ochRxActHighPower,ochTxMinPower,ochTxActLowPower,ochRxMaxPower,ochRxMinPower,txWvlngthSpacing,txActLnPwr,condType,txWvlngthMax);
					retrieveSpecificPortDetails(jsonArr, portsList, "otm2",  ip, aid, clfi, txWvlngthProv, oduTxTti, otuTxTti,
							otuRxExpTti, oduRxExpTti,ochTxActHighPower,txWvlngthMin,label,ochTxMaxPower,ochRxActLowPower,rxActLnPwr,pst,ochRxActHighPower,ochTxMinPower,ochTxActLowPower,ochRxMaxPower,ochRxMinPower,txWvlngthSpacing,txActLnPwr,condType,txWvlngthMax);
					retrieveSpecificPortDetails(jsonArr, portsList, "otm", ip, aid, clfi, txWvlngthProv, oduTxTti, otuTxTti,
							otuRxExpTti, oduRxExpTti,ochTxActHighPower,txWvlngthMin,label,ochTxMaxPower,ochRxActLowPower,rxActLnPwr,pst,ochRxActHighPower,ochTxMinPower,ochTxActLowPower,ochRxMaxPower,ochRxMinPower,txWvlngthSpacing,txActLnPwr,condType,txWvlngthMax);
					retrieveSpecificPortDetails(jsonArr, portsList, "flex", ip, aid, clfi, txWvlngthProv, oduTxTti, otuTxTti,
							otuRxExpTti, oduRxExpTti,ochTxActHighPower,txWvlngthMin,label,ochTxMaxPower,ochRxActLowPower,rxActLnPwr,pst,ochRxActHighPower,ochTxMinPower,ochTxActLowPower,ochRxMaxPower,ochRxMinPower,txWvlngthSpacing,txActLnPwr,condType,txWvlngthMax);
					retrieveSpecificPortDetails(jsonArr, portsList, "ptp",  ip, aid, clfi, txWvlngthProv, oduTxTti, otuTxTti,
							otuRxExpTti, oduRxExpTti,ochTxActHighPower,txWvlngthMin,label,ochTxMaxPower,ochRxActLowPower,rxActLnPwr,pst,ochRxActHighPower,ochTxMinPower,ochTxActLowPower,ochRxMaxPower,ochRxMinPower,txWvlngthSpacing,txActLnPwr,condType,txWvlngthMax);

				} catch (Exception e) {
					logger.error("Exception occured for the ip while retrieving port details:" + ip);
					e.printStackTrace();
				}
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} 
		logger.info("finished executing port ......" + (System.currentTimeMillis() - portStartTime));
		System.out.println("finished executing port ......" + (System.currentTimeMillis() - portStartTime));
		
		return portsList;
	}

	public void retrieveSpecificPortDetails(Object jsonArr, ArrayList<CienaPortInventory> portsList,String portType,  String ip, String aid,
			String clfi, String txWvlngthProv, String oduTxTti, String otuTxTti, String otuRxExpTti,
			String oduRxExpTti,String ochTxActHighPower,String txWvlngthMin,String label,String ochTxMaxPower,String ochRxActLowPower,String rxActLnPwr,String pst,String ochRxActHighPower,String ochTxMinPower,String ochTxActLowPower,String ochRxMaxPower,String ochRxMinPower,String txWvlngthSpacing,String txActLnPwr,String condType,String txWvlngthMax) {
		try {
			
			Date currentDate = new Date();
			Timestamp date = new Timestamp(currentDate.getTime());
			
			if (jsonArr instanceof JSONObject) {
				JSONArray portData = ((JSONObject) jsonArr).getJSONArray(portType);
				for (int portIter = 0; portIter < portData.length(); portIter++) {
					
					CienaPortInventory port = new CienaPortInventory(ip);

					try {

						JSONObject portIterJsonObject = portData.getJSONObject(portIter);
						//logger.info("portIterJsonObject ....." + portIterJsonObject.toString());

						if (portType.equalsIgnoreCase("eth10g")) {
						aid = (String) portIterJsonObject.optString("eth10g");
						clfi = (String) portIterJsonObject.optString("clfi");
						txWvlngthProv = (String) portIterJsonObject.optString("tx-wvlngth-prov");
						}

						
						if (portType.equalsIgnoreCase("otm2")) {
							aid = (String) portIterJsonObject.optString("otm2aid");
							// portType = portType;
							oduTxTti = (String) portIterJsonObject.optString("odu-tx-tti");
						}

						if (portType.equalsIgnoreCase("otm")) {
							aid = (String) portIterJsonObject.optString("otmaid");
							oduTxTti = (String) portIterJsonObject.optString("odu-tx-tti");
							oduRxExpTti = (String) portIterJsonObject.optString("odu-rx-exp-tti");
							otuTxTti = (String) portIterJsonObject.optString("otu-tx-tti");
							otuRxExpTti = (String) portIterJsonObject.optString("otu-rx-exp-tti");
						}

						if (portType.equalsIgnoreCase("flex")) {
							aid = (String) portIterJsonObject.optString("flex");
							clfi = (String) portIterJsonObject.optString("clfi");
						}
						
						if (portType.equalsIgnoreCase("ptp")) {
							logger.info("portIterJsonObject ....." + portIterJsonObject.toString() + " for the ip "+ip);
							aid = portIterJsonObject.optString("ptp");
							ochTxActHighPower = portIterJsonObject.optString("och-tx-act-high-power");
							txWvlngthMin = portIterJsonObject.optString("tx-wvlngth-min");
							label = portIterJsonObject.optString("label");
							clfi = label;
							ochTxMaxPower = portIterJsonObject.optString("och-tx-max-power");
							ochRxActLowPower = portIterJsonObject.optString("och-rx-act-low-power");
							rxActLnPwr = portIterJsonObject.optString("rx-act-ln-pwr").toString();
							pst = portIterJsonObject.optString("pst");
							ochRxActHighPower = portIterJsonObject.optString("och-rx-act-high-power");
							ochTxMinPower = portIterJsonObject.optString("och-tx-min-power");
							ochTxActLowPower = portIterJsonObject.optString("och-tx-act-low-power");
							ochRxMaxPower = portIterJsonObject.optString("och-rx-max-power");
							ochRxMinPower = portIterJsonObject.optString("och-rx-min-power");
							txWvlngthSpacing = portIterJsonObject.optString("tx-wvlngth-spacing");
							txActLnPwr = portIterJsonObject.optString("tx-act-ln-pwr").toString();
							condType = portIterJsonObject.optString("cond-type");
							txWvlngthMax = portIterJsonObject.optString("tx-wvlngth-max");
							txWvlngthProv = portIterJsonObject.optString("tx-wvlngth-prov");
						}
						
						String shelfslotport= aid.substring(aid.indexOf("-")+1);
						
				//		System.out.println("##########AID : "+aid+", port : "+shelfslotport)
						
						
						String nodename = hostNamesMap.get(ip);
						
						String portkey = nodename+"-"+shelfslotport;
						
						String nodeAdj = portAdjMap.get(nodename+"-"+shelfslotport);
						
						String sncKey = "";
						
						if (nodeAdj != null)
						
						//String nodeAdj = adjPortMap.get()
						{
							sncKey = networkAdjacencySncMap.get(nodeAdj);
							
							if (sncKey!=null) {
								System.out.println("snckey found in direct map without OPS, portmapkey : "+nodename+"-"+shelfslotport + ", nodeadjacency : "+nodeAdj + ", sncKey : "+sncKey);
							} else {
								
								if (opsCardMap.containsValue(portkey)) {
									
									System.out.println("$$$$$$$$$$$$$$$$ portkey : "+portkey);
									System.out.println("$$$$$$$$$$$$$$$$ opsCardMap : "+opsCardMap);
									
									System.out.println("OPS Card contains the value "+portkey);
									
									for(String key:opsCardMap.keySet()) {
										if(opsCardMap.get(key).equals(portkey)) {
											//portkey matches adjacency.
											System.out.println(portkey + "matched with "+key);
											
											String keyOpsAdj = nodename+"-"+key.substring(key.indexOf("ADJ")+4, key.length());
											
											System.out.println("@@@@@@@@ key to find node adjacency (made from OpsAdj) : "+keyOpsAdj);
											
											String nodeAdjOps = portAdjMap.get(keyOpsAdj);
											
											System.out.println("@@@@@@@@ node adjacency found : "+nodeAdjOps);
											
											if (nodeAdjOps != null) {
												sncKey = networkAdjacencySncMap.get(nodeAdjOps);
												
												
												
												System.out.println("####### OPS SNC ######## portmapkey : "+nodename+"-"+shelfslotport + ", nodeadjacency : "+nodeAdjOps + ", sncKey : "+sncKey);
											}
										}
									}
								}
								
							}
							
							
						
							
						//System.out.println("portmapkey "+nodename+"-"+shelfslotport + ", nodeadjacency "+nodeAdj);
						} else {
							
							
							if (opsCardMap.containsValue(portkey)) {
								
								System.out.println("$$$$$$$$$$$$$$$$ portkey : "+portkey);
								System.out.println("$$$$$$$$$$$$$$$$ opsCardMap : "+opsCardMap);
								
								System.out.println("OPS Card contains the value "+portkey);
								
								for(String key:opsCardMap.keySet()) {
									if(opsCardMap.get(key).equals(portkey)) {
										//portkey matches adjacency.
										System.out.println(portkey + "matched with "+key);
										
										String keyOpsAdj = nodename+"-"+key.substring(key.indexOf("ADJ")+4, key.length());
										
										System.out.println("@@@@@@@@22keyOpsAdj : "+keyOpsAdj);
										
										String nodeAdjOps = portAdjMap.get(keyOpsAdj);
										
										if (nodeAdjOps != null) {
											sncKey = networkAdjacencySncMap.get(nodeAdj);
											
											
											
											System.out.println("####### OPS SNC ######## portmapkey : "+nodename+"-"+shelfslotport + ", nodeadjacency : "+nodeAdj + ", sncKey : "+sncKey);
										}
									}
								}
							}
						}
						
						port.setNodeIp(ip);
						port.setNodeName(nodename);
						port.setPortId(aid);
						port.setShelfSlotPort(shelfslotport);
						port.setPortType(portType);
						port.setClfi(clfi);
						port.setLabel(label);
						port.setSncKey(sncKey);
						
						port.setDate(date);
						
						portsList.add(port);
					

					} catch (Exception e) {
						e.printStackTrace();

					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
	}
	
	
	public ArrayList<CienaPortInventory> retrieveOptMonPortDetails(String ip,Map sessionMap) {
		ArrayList<CienaPortInventory> portsList = new ArrayList<CienaPortInventory>();
		
		try {
		
		Date currentDate = new Date();
		Timestamp date = new Timestamp(currentDate.getTime());
		
		String nodename = hostNamesMap.get(ip);

		
		HttpsURLConnection apiConnOptMon = getHttpsConnectionWithoutCertificate(
				"https://" + ip + ":8443/api/v1/datastore/optmons", "GET", sessionMap);
		
		JSONObject jsonObjectOptMon = (JSONObject) getJson(apiConnOptMon);

		JSONArray jsonArrayOptmon = jsonObjectOptMon.getJSONArray("optmon");
						
		

		
		
		for(int i=0;i<jsonArrayOptmon.length();i++) {
			JSONObject jsonObj = jsonArrayOptmon.getJSONObject(i);
			
			String aid = jsonObj.getString("optmon");
			String shelfslotport= aid.substring(aid.indexOf("-")+1);
			
			CienaPortInventory port = new CienaPortInventory(ip);
			
			port.setNodeIp(ip);
			port.setNodeName(nodename);
			port.setPortId(aid);
			port.setShelfSlotPort(shelfslotport);
			port.setPortType("optmon");
			port.setDate(date);
			
			port.setClfi("");
			port.setLabel("");
			port.setSncKey("");

			
			portsList.add(port);


		}
		
		} catch(Exception e) {
			logger.error("Unable to retrieve optmon port details ",e);
		}
		
		return portsList;
	}
	
	public ArrayList<CienaPortInventory> retrieveAmpPortDetails(String ip,Map sessionMap) {
		ArrayList<CienaPortInventory> portsList = new ArrayList<CienaPortInventory>();
		
		try {
		
		Date currentDate = new Date();
		Timestamp date = new Timestamp(currentDate.getTime());
		
		String nodename = hostNamesMap.get(ip);

		
		HttpsURLConnection apiConnOptMon = getHttpsConnectionWithoutCertificate(
				"https://" + ip + ":8443/api/v1/datastore/amps", "GET", sessionMap);
		
		JSONObject jsonObjectOptMon = (JSONObject) getJson(apiConnOptMon);

		JSONArray jsonArrayOptmon = jsonObjectOptMon.getJSONArray("amp");
						
		

		
		
		for(int i=0;i<jsonArrayOptmon.length();i++) {
			JSONObject jsonObj = jsonArrayOptmon.getJSONObject(i);
			
			String aid = jsonObj.getString("amp");
			String shelfslotport= aid.substring(aid.indexOf("-")+1);
			
			CienaPortInventory port = new CienaPortInventory(ip);
			
			port.setNodeIp(ip);
			port.setNodeName(nodename);
			port.setPortId(aid);
			port.setShelfSlotPort(shelfslotport);
			port.setPortType("amp");
			port.setDate(date);
			
			port.setClfi("");
			port.setLabel("");
			port.setSncKey("");

			
			portsList.add(port);


		}
		
		} catch(Exception e) {
			logger.error("Unable to retrieve optmon port details ",e);
		}
		
		return portsList;
	}
	
	
	public ArrayList<CienaPortInventory> retrieveOscPortDetails(String ip,Map sessionMap) {
		ArrayList<CienaPortInventory> portsList = new ArrayList<CienaPortInventory>();
		
		try {
		
		Date currentDate = new Date();
		Timestamp date = new Timestamp(currentDate.getTime());
		
		String nodename = hostNamesMap.get(ip);

		
		HttpsURLConnection apiConnOptMon = getHttpsConnectionWithoutCertificate(
				"https://" + ip + ":8443/api/v1/datastore/oscs", "GET", sessionMap);
		
		JSONObject jsonObjectOptMon = (JSONObject) getJson(apiConnOptMon);

		JSONArray jsonArrayOptmon = jsonObjectOptMon.getJSONArray("osc");
						
		

		
		
		for(int i=0;i<jsonArrayOptmon.length();i++) {
			JSONObject jsonObj = jsonArrayOptmon.getJSONObject(i);
			
			String aid = jsonObj.getString("osc");
			String shelfslotport= aid.substring(aid.indexOf("-")+1);
			
			CienaPortInventory port = new CienaPortInventory(ip);
			
			port.setNodeIp(ip);
			port.setNodeName(nodename);
			port.setPortId(aid);
			port.setShelfSlotPort(shelfslotport);
			port.setPortType("osc");
			port.setDate(date);
			
			port.setClfi("");
			port.setLabel("");
			port.setSncKey("");

			
			portsList.add(port);


		}
		
		} catch(Exception e) {
			logger.error("Unable to retrieve optmon port details ",e);
		}
		
		return portsList;
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
	
	public void printMap() {
		System.out.println("OPS Map"+opsCardMap);
	}
	
	public void retrievePerformanceDetailsAll(String ip) {
		
	}
	
	public void retrievePerformanceDetails() {

		try {

			ArrayList<String> ipList = new ArrayList<String>();
			ipList.add("100.65.244.98");
			ipList.add("100.65.244.106");

			FileWriter fw = new FileWriter("e:\\Sify\\ciena\\6500Parameters.csv");

		//	fw.write("ip,aid,tx_tag,rx_actual");
		//	fw.write("\n");

			for (String ip : ipList) {

				HttpsURLConnection loginConn = doLoginUrlEncoded("https://" + ip + ":8443/api/login");
				
				if (loginConn==null) {
					System.out.println("Unable to connect with "+ip+", continuing with next ip");
					//fw.write(ip + "," + aid + "," + tx_tag + "," + rx_actual);
					fw.write(ip + "," +  ","  +  "," );
					System.out.println(ip + "," +  "," +  "," );
					fw.write("\n");
					continue;
				}

				Map<String, String> sessionMap = getSessionId(loginConn);

				HttpsURLConnection apiConn = getHttpsConnectionWithoutCertificate(
						"https://" + ip + ":8443/api/v1/datastore/amps/amp", "GET", sessionMap);

				JSONArray jsonArray = (JSONArray) getJson(apiConn);
				
				for(int i=0;i<jsonArray.length();i++) {
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					
					System.out.println(jsonObj.get("amp") +" : "+"Gain : "+jsonObj.get("gain"));
					
					if(jsonObj.get("amp").equals("AMP-1-4-6")) {
						System.out.println("#########"+jsonObj.get("amp") +" : "+"Gain : "+jsonObj.get("gain"));
						
					}
					
					if(jsonObj.get("amp").equals("AMP-1-4-8")) {
						System.out.println("#########"+jsonObj.get("amp") +" : "+"Gain : "+jsonObj.get("gain"));
						
					}
				}
				
				HttpsURLConnection apiConnCounts = getHttpsConnectionWithoutCertificate(
						"https://" + ip + ":8443/api/v1/datastore/pm/amp", "GET", sessionMap);
				
				JSONObject jsonObjectCounts = (JSONObject) getJson(apiConnCounts);

				JSONArray jsonArrayCounts = jsonObjectCounts.getJSONArray("counts");
				
				for(int i=0;i<jsonArrayCounts.length();i++) {
					JSONObject jsonObj = jsonArrayCounts.getJSONObject(i);
					
					//if (((JSONArray)jsonObj.get("montype")).get(0).equals("OPOUT-OTS") && jsonObj.get("ampaid").equals("AMP-1-4-6"))
					if (((JSONArray)jsonObj.get("montype")).get(0).equals("OPOUT-OTS") )
					{
						System.out.println(jsonObj.get("ampaid") +" : "+"Tx Power : " + jsonObj.get("monval"));
					}
					
					//if (((JSONArray)jsonObj.get("montype")).get(0).equals("OPIN-OTS") && jsonObj.get("ampaid").equals("AMP-1-4-8"))
					if (((JSONArray)jsonObj.get("montype")).get(0).equals("OPIN-OTS") )
					{
						System.out.println(jsonObj.get("ampaid") +" : "+"Rx Power : " + jsonObj.get("monval"));
					}
					
					if (((JSONArray)jsonObj.get("montype")).get(0).equals("ORL-OTS") && jsonObj.get("ampaid").equals("AMP-1-4-6"))
					{
						System.out.println(jsonObj.get("ampaid") +" : "+ jsonObj.get("montype") +" : "+ jsonObj.get("monval"));
					}
					
					if (((JSONArray)jsonObj.get("montype")).get(0).equals("ORL-OTS") && jsonObj.get("ampaid").equals("AMP-1-4-8"))
					{
						System.out.println(jsonObj.get("ampaid") +" : "+ jsonObj.get("montype") +" : "+ jsonObj.get("monval"));
					}
				}
				
				
				HttpsURLConnection apiConnOscs = getHttpsConnectionWithoutCertificate(
						"https://" + ip + ":8443/api/v1/datastore/oscs", "GET", sessionMap);
				
				JSONObject jsonObjectOscs = (JSONObject) getJson(apiConnOscs);

				JSONArray jsonArrayOscs = jsonObjectOscs.getJSONArray("osc");
				
				for(int i=0;i<jsonArrayOscs.length();i++) {
					JSONObject jsonObj = jsonArrayOscs.getJSONObject(i);
					
					System.out.println(jsonObj.get("osc")+", spanloss : "+jsonObj.get("span-loss"));
					
				}
				
				HttpsURLConnection apiConnOptMon = getHttpsConnectionWithoutCertificate(
						"https://" + ip + ":8443/api/v1/datastore/optmons", "GET", sessionMap);
				
				JSONObject jsonObjectOptMon = (JSONObject) getJson(apiConnOptMon);

				JSONArray jsonArrayOptmon = jsonObjectOptMon.getJSONArray("optmon");
				
				for(int i=0;i<jsonArrayOptmon.length();i++) {
					JSONObject jsonObj = jsonArrayOptmon.getJSONObject(i);
					
					if (jsonObj.get("optmon").equals("OPTMON-1-5-13")) {
					
					System.out.println("OPTMON-1-5-13 working status : "+((JSONArray)jsonObj.get("sst")).get(0));
					}
					
					if (jsonObj.get("optmon").equals("OPTMON-1-5-15")) {
						
						System.out.println("OPTMON-1-5-15 working status : "+((JSONArray)jsonObj.get("sst")).get(0));
					}
						
					
				}
				
				

			}
			
			fw.flush();
			fw.close();

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	
	
}


class InvalidCertificateHostVerifierCiena implements HostnameVerifier{
	@Override
	public boolean verify(String paramString, SSLSession paramSSLSession) {
		return true;
	}
	
	

}

