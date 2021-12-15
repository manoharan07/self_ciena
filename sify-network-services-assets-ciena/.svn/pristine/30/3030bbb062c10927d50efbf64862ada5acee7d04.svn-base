package com.sify.network.alarms.ciena;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.extensions.IExtension;
import org.java_websocket.protocols.IProtocol;
import org.java_websocket.protocols.Protocol;

import com.sify.network.assets.ciena.CienaProperties;

public class CienaWsEventReceiverThread implements Runnable {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("cienaalarms");

	private String deviceIP;
	private String userName;
	private String password;
	private Integer port;

	public CienaWsEventReceiverThread(String deviceIP, String userName, String password, Integer port) {
		this.deviceIP = deviceIP;
		this.userName = userName;
		this.password = password;
		this.port = port;
	}

	public void run() {

		while (true) {

			try {
				logger.info("Trying to connect with " + this.deviceIP + " for getting alarms");

				CienaWsHttpConnection conn = new CienaWsHttpConnection(this.deviceIP, this.port, this.userName, this.password);
				logger.info("connection created with " + deviceIP + " : " + conn);

				HttpURLConnection urlConn = conn.doLogin();

				if (urlConn != null) {
					logger.info("login conn for " + this.deviceIP + " : " + urlConn);

					//String token = conn.getXsrfToken(urlConn);
					String cookie = conn.getCookie(urlConn);

					logger.info(
							"device IP : " + this.deviceIP + " : cookie : " + cookie);

					//String tokenKey = "-xsrf-";
					String uri = String.format("wss://%s/recv", this.deviceIP, this.port);
					logger.info("uri : " + uri);

					// This draft allows the specific Sec-WebSocket-Protocol and
					// also
					// provides a
					// fallback, if the other endpoint does not accept the
					// specific
					// Sec-WebSocket-Protocol
					ArrayList<IProtocol> protocols = new ArrayList<IProtocol>();
					protocols.add(new Protocol("ocpp2.0"));
					//protocols.add(new Protocol("6500-ao-json"));
					
					protocols.add(new Protocol("waveserver-autonomous-message"));
					Draft_6455 draft = new Draft_6455(Collections.<IExtension>emptyList(), protocols);

					try {
						CienaWsEventClient c = tryConnect(uri, cookie, draft);
						// if tryConnect returned, exception might have occurred
						// due to
						// certificate.
						// Try again as server certficate should have been added
						// in the
						// catch block of ssl exception.

						// logger.info("TryConnect again with " +
						// this.deviceIP);
						// c = tryConnect(uri, cookie, draft);

						boolean check = true;
						while (check) {
							Thread.sleep(5000);
							if (c.getReadyState() == ReadyState.CLOSING || c.getReadyState() == ReadyState.CLOSED)
								check = false;
						}
						// c.connect();
						logger.info("connection closed with "+deviceIP+", ready state : " + c.getReadyState());
						//logger.info("connection lost timeout : " + c.getConnectionLostTimeout());

					} catch (URISyntaxException e) {
						// e.printStackTrace();
						logger.error("Got error while retrieving alarm from " + this.deviceIP, e);
					} catch (InterruptedException e) {
						// e.printStackTrace();
						logger.error("Got error while retrieving alarm from " + this.deviceIP, e);
					} catch (Exception e) {
						logger.info("Got error with " + deviceIP + " : " + e.getMessage() + " : " + e.getCause());
					}

					// logger.info("token : " + token + " : cookie : " +
					// cookie);
					logger.info("logging out " + deviceIP);
					//conn.doLogout(token, cookie);

				} 

					logger.info("Will try connecting again with " + this.deviceIP + " after 5 seconds");
					Thread.sleep(5000); // wait for 10 minutes and try again.
				

			} catch (Exception e) {
				// e.printStackTrace();
				logger.error("Unable to retrieve alarms from " + this.deviceIP, e);

			}

		}
	}

	private CienaWsEventClient tryConnect(String uri, String cookie, Draft_6455 draft) throws Exception {
		Map<String, String> httpHeaders = new HashMap<String, String>();
		
		
		String authStr = CienaProperties.get("ciena.waveserver.username") +":"+CienaProperties.get("ciena.waveserver.password");
		byte[] authEncoded = Base64.getEncoder().encode(authStr.getBytes());

		
		httpHeaders.put("Authorization", "Basic " + new String(authEncoded)); 

		
		//httpHeaders.put("Cookie", cookie);
		CienaWsEventClient c = new CienaWsEventClient(new URI(uri), draft, httpHeaders,
				new DeviceDetails(this.deviceIP, this.userName, this.password, this.port));
		// logger.info("httpheaders : " + httpHeaders);
		// Returns '0 = CONNECTING', '1 = OPEN', '2 = CLOSING' or '3 = CLOSED'
		logger.info("Connection opened with " + this.deviceIP + ", ready state : " + c.getReadyState());
		// logger.info("Connection opened with " + this.deviceIP + ", ready
		// state : " + c.getReadyState());
		// We expect no successful connection
		c.connectBlocking();

		/*
		 * boolean check = true; while (check) { Thread.sleep(5000); if
		 * (c.getReadyState() == ReadyState.CLOSING || c.getReadyState() ==
		 * ReadyState.CLOSED) check = false; } // c.connect();
		 * logger.info("ready state : " + c.getReadyState());
		 * logger.info("connection lost timeout : " +
		 * c.getConnectionLostTimeout());
		 */
		return c;
	}
}
