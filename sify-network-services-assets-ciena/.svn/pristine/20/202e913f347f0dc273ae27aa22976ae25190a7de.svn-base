package com.sify.network.assets.ciena;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONObject;
 

public class CienaMcpClient {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("cienamcp");

	private String ip = "";
	private Integer port = 0;

	private String username = "";
	private String password = "";

	private String loginUrl = "";
	private String logoutUrl = "";
	
	
	private String filterUrl = "";

	//static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CienaMcpClient.class);
	
	private String bearerToken="";

	public CienaMcpClient(String ip, Integer port, String username, String password) {

		this.ip = ip;
		this.port = port;
		this.username = username;
		this.password = password;

		this.loginUrl = String.format("https://%s:%s/tron/api/v1/tokens", this.ip, this.port);
		this.filterUrl = String.format("https://%s:%s/nsa/api/v2_0/alarms/filter", this.ip, this.port);
		
		this.logoutUrl = String.format("https://%s:%s/api/logout", this.ip, this.port);

	}

	public static void main(String[] args) throws IOException {

		/*
		 * System.out.println("application started..."); new HttpConnection("100.66.56.135",
		 * 8443, "013273", "May.9999").getJsons(); String portjson =
		 * jsons.getPortJson(); PortFromJson port = new PortFromJson();
		 * port.fromjson(portjson); System.out.println(jsons);
		 */	
		
		String username = "Ciena";
		String password = "Sify@123";
		
		Calendar startTime=null;
		Calendar endTime = null;
		
		Calendar cal = Calendar.getInstance();
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
		
		Date startDate = startTime.getTime();
		Date endDate = endTime.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH'%3A'mm'%3A'ss");
		String startDateStr = sdf.format(startDate);
		String endDateStr = sdf.format(endDate);
		
		System.out.println("Start Date "+startDateStr);
		System.out.println("End Date "+endDateStr);

		
		CienaMcpClient mcpClient = new CienaMcpClient("bpmcp1.sify.net", 443, username, password);
		
		HttpsURLConnection loginConn = mcpClient.doLogin();
		
		mcpClient.getBearerToken(loginConn);
		//HttpsURLConnection alarmsConn = mcpClient.retrieveAlarms("100.66.74.139", "OPTMON-1-7-8","ESAM-1-7","2021-10-01T00%3A00%3A00.000Z", "2021-10-28T23%3A59%3A59.999Z", bearerToken);
		String alarmsJson = mcpClient.retrieveAlarms("100.66.74.139", "OPTMON-1-7-8","ESAM-1-7",startDateStr, endDateStr);
		
		//System.out.println("token : " + token + " : cookie : " + cookie);
		
		System.out.println("Response json "+alarmsJson);

		
		//conn.doLogout(token, cookie);
		
	}


	/*
	 * private CiennaJsons getJsons() { CiennaJsons jsons = new CiennaJsons();
	 * 
	 * HttpsURLConnection login = doLogin();
	 * 
	 * String xsrfToken = getXsrfToken(login); String cookie = getCookie(login);
	 * 
	 * login.disconnect();
	 * 
	 * HttpsURLConnection equip = createConnection(this.equipmentUrl, xsrfToken,
	 * cookie); jsons.setEquipmentJson(getJson(equip)); equip.disconnect();
	 * 
	 * HttpsURLConnection port = createConnection(this.portUrl, xsrfToken, cookie);
	 * jsons.setPortJson(getJson(port)); port.disconnect();
	 * 
	 * HttpsURLConnection osrp = createConnection(this.osrpUrl, xsrfToken, cookie);
	 * jsons.setOsrp(getJson(osrp)); osrp.disconnect();
	 * 
	 * HttpsURLConnection logout = doLogout(xsrfToken, cookie); logout.disconnect();
	 * 
	 * return jsons; }
	 */
	public HttpsURLConnection createConnection(String url, String xsrfToken, String cookie) {

		HttpsURLConnection conn = null;

		try {
			URL myUrl = new URL(url);
			conn = (HttpsURLConnection) myUrl.openConnection();

			conn.setRequestMethod("GET");

			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("X-XSRF-TOKEN", xsrfToken);
			conn.setRequestProperty("cookie", cookie);

		} catch (MalformedURLException e) {
			System.out.println("getHttpsConn W/O Certificate MalformedURLException:");
			e.printStackTrace();
			return null;
		} catch (ProtocolException e) {
			System.out.println("getHttpsConn W/O Certificate ProtocolException:");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			System.out.println("getHttpsConn W/O Certificate IOException");
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			System.out.println("getHttpsConn W/O Certificate Exception");
			e.printStackTrace();
			return null;
		}
		return conn;
	}

	public HttpsURLConnection doLogin() {

		HttpsURLConnection conn = null;

		try {

			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			URL myUrl = new URL(this.loginUrl);
			conn = (HttpsURLConnection) myUrl.openConnection();

			conn.setRequestMethod("POST");

			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			String params = String.format("username=%s&tenant=%s&password=%s", this.username, "master",this.password);

			System.out.println("login url");
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.writeBytes(params);
			wr.flush();
			wr.close();
			
			System.out.println("do login response : code : " + conn.getResponseCode() + " : message : " + conn.getResponseMessage());

			conn.setHostnameVerifier(new InvalidCertificateHostVerifier());

		} catch (KeyManagementException e) {
			e.printStackTrace();
			return null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (ProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return conn;
	}
	
	
	public HttpsURLConnection createFilter(String bearerToken) {

		HttpsURLConnection conn = null;

		try {

			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			URL myUrl = new URL(this.filterUrl);
			conn = (HttpsURLConnection) myUrl.openConnection();

			conn.setRequestMethod("POST");

			conn.setDoOutput(true);
			conn.setRequestProperty("Authorization", "Bearer "+bearerToken);
			conn.setRequestProperty("Content-Type", "application/json");

			String params = "{\"data\": {\"type\": \"Filter\",\"attributes\": {\"severity\": [\"CRITICAL\", \"MAJOR\", \"MINOR\"]}}}";

			
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.writeBytes(params);
			wr.flush();
			wr.close();
			
			System.out.println("create filter response : code : " + conn.getResponseCode() + " : message : " + conn.getResponseMessage());

			conn.setHostnameVerifier(new InvalidCertificateHostVerifier());

		} catch (KeyManagementException e) {
			e.printStackTrace();
			return null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (ProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return conn;
	}
	
	
	public String retrieveAlarms(String nodeName, String optmonPort, String esamSlot, String startDate, String endDate) {
		
		long startTime = System.currentTimeMillis();
		
		//String alarmUrl = "https://bpmcp1.sify.net/nsa/api/v2_0/alarms/filter/filteredAlarms?filter%5Bseverity%5D%5B%5D=CRITICAL%2CMAJOR%2CMINOR&filter%5BipAddress%5D%5B%5D="+ip+"&filter%5BresourceList%5D="+optmonPort+"%2C"+esamSlot+"&filter%5BrefinedRaisedTimeFrom%5D="+startDate+"&filter%5BrefinedRaisedTimeTo%5D="+endDate+"&sort%5B%5D=last-raise-time%3ADESC&offset=0&pageSize=1000";
		
		String alarmUrl = "https://bpmcp1.sify.net/nsa/api/v2_0/alarms/filter/filteredAlarms?filter%5Bseverity%5D%5B%5D=CRITICAL%2CMAJOR%2CMINOR&filter%5BdeviceName%5D%5B%5D="+nodeName+"&filter%5BresourceList%5D="+optmonPort+"%2C"+esamSlot+"&filter%5BrefinedRaisedTimeFrom%5D="+startDate+"&filter%5BrefinedRaisedTimeTo%5D="+endDate+"&sort%5B%5D=last-raise-time%3ADESC&offset=0&pageSize=1000";

		HttpsURLConnection conn = null;

		try {

			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			URL myUrl = new URL(alarmUrl);
			conn = (HttpsURLConnection) myUrl.openConnection();

			conn.setRequestMethod("GET");

			conn.setDoOutput(true);
			conn.setRequestProperty("Authorization", "Bearer "+bearerToken);
			conn.setRequestProperty("Accept", "application/json");

/*			String params = "{\"data\": {\"type\": \"Filter\",\"attributes\": {\"severity\": [\"CRITICAL\", \"MAJOR\", \"MINOR\"]}}}";

			
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.writeBytes(params);
			wr.flush();
			wr.close();
*/			
			System.out.println("create filter response : code : " + conn.getResponseCode() + " : message : " + conn.getResponseMessage());
			
			if(conn.getResponseCode() >= 200 && conn.getResponseCode() < 400){
				logger.info("Succeeded API Response Code : "+conn.getResponseCode()+", Response Time (seconds) : " + ((System.currentTimeMillis() - startTime) * 0.001) + ", URL : "+ alarmUrl );
			}else{
				logger.info("Failed API Response Code : "+conn.getResponseCode()+", Response Time (seconds) : " + ((System.currentTimeMillis() - startTime) * 0.001) + ", URL : "+ alarmUrl );
				System.out.println(conn.getResponseMessage());
				return null;
			}


			conn.setHostnameVerifier(new InvalidCertificateHostVerifier());

		} catch (KeyManagementException e) {
			e.printStackTrace();
			return null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (ProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return getJson(conn);

		//return conn;
	}



	class InvalidCertificateHostVerifier implements HostnameVerifier {
		public boolean verify(String paramString, SSLSession paramSSLSession) {
			return true;
		}
	}

	public String getJson(HttpsURLConnection conn) {

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
		return json;
	}

	public String getXsrfToken(HttpURLConnection conn) {

		String xsrf = "";

		if (conn.getHeaderFields().containsKey("X-XSRF-TOKEN")) {
			xsrf = conn.getHeaderFields().get("X-XSRF-TOKEN").get(0);
		}
		logger.debug("obtained xsrf token : " + xsrf);
		return xsrf;
	}
	
	public String getBearerToken(HttpsURLConnection conn) {

		String token = "";
		
		String jsonStr = getJson(conn);
		
		JSONObject loginResponseObj = new JSONObject(jsonStr);	
		
		token = loginResponseObj.getString("token");
		
		System.out.println("getToken response :"+jsonStr);
		System.out.println("token received"+token);
		
		bearerToken = token;

		//logger.debug("obtained xsrf token : " + xsrf);
		return token;
	}
	
	public String getFilterChannel(HttpsURLConnection conn) {

		String filterChannel = "";
		
		String jsonStr = getJson(conn);
		
		JSONObject loginResponseObj = new JSONObject(jsonStr);	
		
		filterChannel = loginResponseObj.getJSONObject("data").getJSONObject("attributes").getString("channel");
		
		System.out.println("createFilter response :"+jsonStr);
		System.out.println("filterChannel received"+filterChannel);

		//logger.debug("obtained xsrf token : " + xsrf);
		return filterChannel;
	}



	public String getCookie(HttpURLConnection conn) {

		String cookie = "";

		if (conn.getHeaderFields().containsKey("Set-Cookie")) {
			cookie = conn.getHeaderFields().get("Set-Cookie").get(1);
		}
		logger.debug("obtained cookie : " + cookie);
		return cookie;
	}

	public HttpsURLConnection doLogout(String xsrfToken, String cookie) {

		HttpsURLConnection conn = null;
		try {
			URL myUrl = new URL(this.logoutUrl);

			conn = (HttpsURLConnection) myUrl.openConnection();

			conn.setRequestMethod("POST");
			conn.setDoOutput(false);

			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", "31");
			conn.setRequestProperty("X-XSRF-TOKEN", xsrfToken);
			conn.setRequestProperty("cookie", cookie);
			
			System.out.println("logout : " + conn.getResponseMessage() + " : code : " + conn.getResponseCode());

			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
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
		//	conn.setConnectTimeout(cienaProperties.get("ciena.api.read.timeout",20000));
		//	conn.setReadTimeout(cienaProperties.get("ciena.api.read.timeout",20000));

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
	
	

}




/*class InvalidCertificateHostVerifierCienaMcp implements HostnameVerifier{
	@Override
	public boolean verify(String paramString, SSLSession paramSSLSession) {
		return true;
	}
	
	

}

*/