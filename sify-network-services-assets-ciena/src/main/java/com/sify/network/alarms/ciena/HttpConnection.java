package com.sify.network.alarms.ciena;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpConnection {

	private String ip = "";
	private Integer port = 0;

	private String username = "";
	private String password = "";

	private String loginUrl = "";
	private String logoutUrl = "";

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("cienaalarms");

	public HttpConnection(String ip, Integer port, String username, String password) {

		this.ip = ip;
		this.port = port;
		this.username = username;
		this.password = password;

		this.loginUrl = String.format("https://%s:%s/api/login", this.ip, this.port);
		this.logoutUrl = String.format("https://%s:%s/api/logout", this.ip, this.port);

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

			String params = String.format("userid=%s&password=%s", this.username, this.password);

			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.writeBytes(params);
			wr.flush();
			wr.close();
			
			logger.info("do login response : code : " + conn.getResponseCode() + " : message : " + conn.getResponseMessage());

			conn.setHostnameVerifier(new InvalidCertificateHostVerifier());

		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("Unable to login with "+ip+", got exception "+e.getMessage()+":"+e.getCause());
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
			
			logger.info("logout : " + conn.getResponseMessage() + " : code : " + conn.getResponseCode());

			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}