package com.sify.network.alarms.ciena;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sify.network.assets.ciena.CienaProperties;
import com.sify.network.assets.ciena.ServiceNowRestClient;
import com.sify.network.assets.ciena.SmsEmailClient;

public class CienaWsEventClient extends WebSocketClient {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("cienaalarms");
	
	private DeviceDetails deviceDetails;
	
	String postUrlServiceNowTest = CienaProperties.get("servicenowtest.alarm.post.url.ciena.ws");
	String postUrlServiceNow = CienaProperties.get("servicenow.alarm.post.url.ciena.ws");
	
	
	final OpticalAlarmSender alertSender=new OpticalAlarmSender();
	SmsEmailClient smsEmailClient = new SmsEmailClient();
	

	public CienaWsEventClient(URI serverUri, Draft draft) {
		super(serverUri, draft);
	}

	public CienaWsEventClient(URI serverURI) {
		super(serverURI);
	}
	
	public CienaWsEventClient(URI serverUri, Draft_6455 draft, Map<String, String> httpHeaders, DeviceDetails deviceDetails) {
		super(serverUri, draft, httpHeaders);
		this.deviceDetails = deviceDetails;
		//alertSender = new AlertSender();
	}
	public CienaWsEventClient(URI serverUri, Draft_6455 draft, Map<String, String> httpHeaders) {
		super(serverUri, draft, httpHeaders);
	}
	
	public CienaWsEventClient(URI serverUri, Map<String, String> httpHeaders) {
		super(serverUri, httpHeaders);
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		logger.info("Connection opened with "+deviceDetails.ip);
		smsEmailClient.sendSmsEmailAlerts("Connection opened with "+deviceDetails.ip);
		//logger.info("handshake : message : " + handshakedata.getHttpStatusMessage() + " : code : " + handshakedata.getHttpStatus() );
	}

	@Override
	public void onMessage(String message) {

		logger.info("Received Message from "+this.deviceDetails.getIp()+" : "+message);
		//logger.info(this.deviceDetails.getIp()+" : received : " + message);
		Gson gson = new Gson();

		try {
			CienaWsEvent wsEvent = gson.fromJson(message, CienaWsEvent.class);
			
			Event event = wsEvent.getEvent();
			//System.out.println("Device Details : "+this.deviceDetails);
			if (this.deviceDetails!= null && event !=null) {
			
			event.setDeviceip(this.deviceDetails.getIp());
			
			if (event.getDbChange().getPath().startsWith("/waveserver-alarms/alarms") || 
					event.getDbChange().getPath().startsWith("/ws-alarms/alarms")) {
			
			String alarmJson = event.toJson();
			logger.info("Received Message from "+this.deviceDetails.getIp()+" : "+message);
			//logger.info("Alarm data : "+alarmJson);
			
			
			if (alertSender!=null && CienaProperties.get("optical.activemq.enabled").equals("true")) {
				
				try {
					logger.info("Sending alarm to activemq : "+alarmJson);
					
									
				    alertSender.sendToQueue(deviceDetails.ip,alarmJson);
				} catch(Exception e) {
					logger.warn("Unable to send alarm to activemq",e);
				}
				
			} else {
				//directly send alarm to servicenow skipping queue.
				if (CienaProperties.get("servicenow.alarm.forwarding.ciena.ws").equals("true")) {
					ServiceNowRestClient.postAlarm(alarmJson,postUrlServiceNow);
				}
				
				if (CienaProperties.get("servicenowtest.alarm.forwarding.ciena.ws").equals("true")) {
					ServiceNowRestClient.postAlarmToServiceNowTest(alarmJson,postUrlServiceNowTest);
				}

				
			}
			
			}
			
			}
			
			
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		// The codecodes are documented in class org.java_websocket.framing.CloseFrame
		logger.info("Connection closed with "+deviceDetails.ip);
		logger.info(
				"Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: " + reason);
		smsEmailClient.sendSmsEmailAlerts("Connection closed with "+deviceDetails.ip);
		
	}

	@Override
	public void onError(Exception ex) {
		logger.error("Got error in receiving alarm from "+deviceDetails.ip+", "+ex.getCause()+" : "+ex.getClass(),ex);
		
		//ex.printStackTrace();
		
		if (ex instanceof javax.net.ssl.SSLException) {
			try {
			KeyToolUtils.retrieveCertificateAndStore(deviceDetails.getIp(), Integer.parseInt(deviceDetails.getPort()));
			//this.connectBlocking();
			} catch(Exception e) {
				logger.info("Got exception while retrieving certificate from "+deviceDetails.ip,e);
				//e.printStackTrace(System.out);
			}
			
		}
	}

	/*public static void main(String[] args) throws URISyntaxException {
		CiennaEventClient c = new CiennaEventClient(new URI("wss://100.66.28.116:8443/status?-xsrf-=e8bb6d58c61aace11739412744c4365c")); // more about drafts here:
																				// http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts
		c.connect();
	}*/

}