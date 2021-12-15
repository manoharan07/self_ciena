package com.sify.network.alarms.ciena;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Base64;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.sify.network.assets.ciena.CienaProperties;

public class CienaWsHttpConnection {

	private String ip = "";
	private Integer port = 0;

	private String username = "";
	private String password = "";

	private String loginUrl = "";
	private String logoutUrl = "";

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("cienaalarms");

	public CienaWsHttpConnection(String ip, Integer port, String username, String password) {

		this.ip = ip;
		this.port = port;
		this.username = username;
		this.password = password;

		this.loginUrl = String.format("https://%s/php/js/jquery_1.5.2.js", this.ip);
		this.logoutUrl = String.format("https://%s/php/js/jquery_1.5.2.js", this.ip);

	}

	/*public static void main(String[] args) throws IOException {

		
		 * logger.info("application started..."); new HttpConnection("100.66.56.135",
		 * 8443, "013273", "May.9999").getJsons(); String portjson =
		 * jsons.getPortJson(); PortFromJson port = new PortFromJson();
		 * port.fromjson(portjson); logger.info(jsons);
		 	
		
		String username = "txsn";
		String password = "Txnoc@321";
		HttpConnection conn = new HttpConnection("100.66.28.116", 8443, username, password);
		
		HttpURLConnection urlConn = conn.doLogin();
		
		String token = conn.getXsrfToken(urlConn);
		String cookie = conn.getCookie(urlConn);
		
		logger.info("token : " + token + " : cookie : " + cookie);
		
		conn.doLogout(token, cookie);
		
	}*/


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
			logger.info("getHttpsConn W/O Certificate MalformedURLException:");
			e.printStackTrace();
			return null;
		} catch (ProtocolException e) {
			logger.info("getHttpsConn W/O Certificate ProtocolException:");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			logger.info("getHttpsConn W/O Certificate IOException");
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			logger.info("getHttpsConn W/O Certificate Exception");
			e.printStackTrace();
			return null;
		}
		return conn;
	}

	public HttpsURLConnection doLogin() {

		HttpsURLConnection conn = null;

		try {
			
			long startTime = System.currentTimeMillis();
			logger.info("API is " + this.loginUrl + " started at " +  startTime);

			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };
			//SSLContext sc = SSLContext.getInstance("SSL");
			SSLContext sc=null;
			if (ip.equals("100.65.244.101") || ip.equals("100.65.244.109")) {
				sc = SSLContext.getInstance("TLSv1.2");
			} else {
				sc = SSLContext.getInstance("SSL");
			}
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

		

			String authStr = CienaProperties.get("ciena.waveserver.username") +":"+CienaProperties.get("ciena.waveserver.password");
			byte[] authEncoded = Base64.getEncoder().encode(authStr.getBytes());

			conn.setRequestMethod("GET");
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
				logger.info("Succeeded API Response Code : "+conn.getResponseCode()+", Response Time (seconds) : " + ((System.currentTimeMillis() - startTime) * 0.001) + ", URL : "+ this.loginUrl );
			}else{
				logger.info("Failed API Response Code : "+conn.getResponseCode()+", Response Time (seconds) : " + ((System.currentTimeMillis() - startTime) * 0.001) + ", URL : "+ this.loginUrl );
				System.out.println(conn.getResponseMessage());
				return null;
			}
			
			logger.info("do login response : code : " + conn.getResponseCode() + " : message : " + conn.getResponseMessage());

			conn.setHostnameVerifier(new InvalidCertificateHostVerifier());

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Unable to login with "+ip+", got exception "+e.getMessage()+":"+e.getCause());
			try {
				KeyToolUtils.retrieveCertificateAndStore(ip, port);
				//this.connectBlocking();
				} catch(Exception ex) {
					logger.info("Got exception while retrieving certificate from "+ip,ex);
					//e.printStackTrace(System.out);
				}
			return null;
		}

		return conn;
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
				logger.info("statusCode:::" + statusCode);
				if (statusCode == 200) {
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
				logger.info("conn is null");
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

	public String getCookie(HttpURLConnection conn) {

		String cookie = "";

		if (conn.getHeaderFields().containsKey("Set-Cookie")) {
			cookie = conn.getHeaderFields().get("Set-Cookie").get(0);
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
			
			logger.info("logout : " + conn.getResponseMessage() + " : code : " + conn.getResponseCode());

			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}