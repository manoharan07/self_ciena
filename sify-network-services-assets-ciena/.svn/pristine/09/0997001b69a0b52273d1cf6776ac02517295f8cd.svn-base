package com.sify.network.alarms.ciena;

import java.io.IOException;
import java.net.URI;
import java.util.List;
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

public class CienaEventClient extends WebSocketClient {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("cienaalarms");
	
	private DeviceDetails deviceDetails;
	
	private boolean closeNotificationSent=false;
	
	
	final OpticalAlarmSender alertSender=new OpticalAlarmSender();
	SmsEmailClient smsEmailClient = new SmsEmailClient();
	

	public CienaEventClient(URI serverUri, Draft draft) {
		super(serverUri, draft);
	}

	public CienaEventClient(URI serverURI) {
		super(serverURI);
	}
	
	public CienaEventClient(URI serverUri, Draft_6455 draft, Map<String, String> httpHeaders, DeviceDetails deviceDetails) {
		super(serverUri, draft, httpHeaders);
		this.deviceDetails = deviceDetails;
		//alertSender = new AlertSender();
	}
	public CienaEventClient(URI serverUri, Draft_6455 draft, Map<String, String> httpHeaders) {
		super(serverUri, draft, httpHeaders);
	}
	
	public CienaEventClient(URI serverUri, Map<String, String> httpHeaders) {
		super(serverUri, httpHeaders);
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		logger.info("Connection opened with "+deviceDetails.ip);
		smsEmailClient.sendSmsEmailAlerts("Connection opened with "+deviceDetails.ip);
		closeNotificationSent=false;
		//logger.info("handshake : message : " + handshakedata.getHttpStatusMessage() + " : code : " + handshakedata.getHttpStatus() );
	}

	@Override
	public void onMessage(String message) {
		
		String alarmJson="";

		//logger.info(this.deviceDetails.getIp()+" : received : " + message);
		Gson gson = new Gson();

		try {
			
			logger.info("Received Message from "+this.deviceDetails.getIp()+" : "+message);
			System.out.println("Received Message from "+this.deviceDetails.getIp()+" : "+message);
			
			CienaEvent event = gson.fromJson(message, CienaEvent.class);
			
			Notification notification = event.getNotification();
			if (notification == null) {
				logger.info("Alarm are received in different format, ignoring : "+message );
				
			/*	CienaEventAO aoEvents = gson.fromJson(message, CienaEventAO.class);
				
				List<Ao> aoList = aoEvents.getEvents().getAo();
				
				for (int i=0;i<aoList.size();i++) {
					Ao aoEvent  = aoList.get(i);
					if (aoEvent.getMsgType().equals("ALM")) {
						Notification alarm = new Notification();
						alarm.setAtagSeq(String.valueOf(aoEvent.getAtagSeq().get(0)));
					System.out.println(aoEvent.getAtagSeq()+","+aoEvent.getAoMessage());
					String aoMessage = aoEvent.getAoMessage();
					String msgTokens[] = aoMessage.split(",");
					for (String token:msgTokens) {
						System.out.println(token);
					}
					String aidTokens[]= msgTokens[0].split(":");
					alarm.setAid(aidTokens[0]);
					alarm.setNtfcncde(aidTokens[1]);
					alarm.setCondtype(msgTokens[1]);
					alarm.setDate(msgTokens[3]);
					alarm.setTime(msgTokens[4]);
					alarm.setLocn(msgTokens[5]);
					
					String condDescTokens[] = msgTokens[6].split(":");
					alarm.setDirn(condDescTokens[0]);
					alarm.setConddescr(condDescTokens[1]);
					
					String yearTokens[] = msgTokens[7].split(":");
					alarm.setYear(Integer.parseInt(yearTokens[2].split("=")[1]));
					
					alarm.setServiceAffected(msgTokens[2]);
					
					alarm.setDeviceip(this.deviceDetails.getIp());
					
					System.out.println("Notification created "+alarm);
					
					sendAlarm(alarm);
					
					}
				}
				*/
				
			} else {
			
			notification.setDeviceip(this.deviceDetails.getIp());
			
			if (notification.getMsgType().equals("ALM")) {
			
			alarmJson = notification.toJson();
			
			sendAlarm(notification);
			
			}
			
			}
			
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//logger.info("Alarm data : "+alarmJson);
			
			
	/*	if (!alarmJson.isEmpty()) {
			if (alertSender!=null && CienaProperties.get("optical.activemq.enabled").equals("true")) {
				
				try {
					logger.info("Sending alarm to Queue : "+alarmJson);
					
									
				    alertSender.sendToQueue(deviceDetails.ip,alarmJson);
				} catch(Exception e) {
					logger.warn("Unable to send alarm to activemq",e);
				}
				
			} else {
				//directly send alarm to servicenow skipping queue.
				if (CienaProperties.get("servicenow.alarm.forwarding.ciena").equals("true")) {
					ServiceNowRestClient.postAlarm(alarmJson);
				}
				
				if (CienaProperties.get("servicenowtest.alarm.forwarding.ciena").equals("true")) {
					ServiceNowRestClient.postAlarmToServiceNowTest(alarmJson);
				}

				
			}
			
			
		}*/
			
		
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		// The codecodes are documented in class org.java_websocket.framing.CloseFrame
		logger.info("Connection closed with "+deviceDetails.ip);
		logger.info(
				"Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: " + reason);
		
		if (!closeNotificationSent) {
		smsEmailClient.sendSmsEmailAlerts("Connection closed with "+deviceDetails.ip+" by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: " + reason);
		closeNotificationSent=true;
		}
		
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
	
	public void setDeviceDetails(DeviceDetails device) {
		this.deviceDetails = device;
	}
	
	
	private void sendAlarm(Notification notification) {
		
		try {
		
		
		String alarmJson = notification.toJson();
		
		
		if (!alarmJson.isEmpty()) {
			if (alertSender!=null && CienaProperties.get("optical.activemq.enabled").equals("true")) {
				
				try {
					logger.info("Sending alarm to Queue : "+alarmJson);
					
									
				    alertSender.sendToQueue(deviceDetails.ip,alarmJson);
				} catch(Exception e) {
					logger.warn("Unable to send alarm to activemq",e);
				}
				
			} else {
				//directly send alarm to servicenow skipping queue.
				if (CienaProperties.get("servicenow.alarm.forwarding.ciena").equals("true")) {
					ServiceNowRestClient.postAlarm(alarmJson);
				}
				
				if (CienaProperties.get("servicenowtest.alarm.forwarding.ciena").equals("true")) {
					ServiceNowRestClient.postAlarmToServiceNowTest(alarmJson);
				}

				
			}
			
			
		}
		
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unable send notificaiton to ServiceNow : "+notification);
		}
		
		
	}

	/*public static void main(String[] args) throws URISyntaxException {
		CiennaEventClient c = new CiennaEventClient(new URI("wss://100.66.28.116:8443/status?-xsrf-=e8bb6d58c61aace11739412744c4365c")); // more about drafts here:
																				// http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts
		c.connect();
	}*/
	
	public static void main(String args[]) {
		try {
			DeviceDetails device = new DeviceDetails();
			device.setIp("100.66.28.125");
		CienaEventClient client = new CienaEventClient(new URI(String.format("wss://%s/recv", "100.66.28.125", 8443)));
		client.setDeviceDetails(device);
		client.onMessage("{   \"events\": {     \"ao\": [       {         \"msg-type\": \"EVT\",         \"subtype\": \"COM\",         \"ao_message\": \"SHELF-1:BKUPC,TC,10-01,19-52-24,NEND,NA:Database Save Completed,NONE:0100000000-0000-0196:YEAR=2020,MODE=NONE\",         \"atag-seq\": [           215642         ]       },                  {         \"msg-type\": \"ACCESS\",         \"ao_message\": \"DATE=21-03-15,TIME=13-45-42:SHELF-1:txauto,TANGPF-MCD-O-I657-02:STATUS=COMPLD,EVENTTYPE=LOGOUT,USERTYPE=NETWORK,PORTTYPE=TCP,INTERFACE=HTTP,SESSION=2177\",         \"atag-seq\": [           282494         ]       },       {         \"msg-type\": \"ALM\",         \"subtype\": \"AMP\",         \"ao_message\": \"AMP-1-1-8:CL,STC_OTS,SA,11-26,19-50-11,NEND,RCV:Shutoff Threshold Crossed,NONE:0100000962-0008-0540:YEAR=2020,MODE=NONE\",         \"atag-seq\": [           232339         ]       },       {         \"msg-type\": \"ALM\",         \"subtype\": \"OSC\",         \"ao_message\": \"OSC-1-15-1:CL,OSC_OSPF_FAIL,NSA,11-26,19-50-20,NEND,RCV:OSC OSPF Adjacency Loss,NONE:0100000961-6355-0975:YEAR=2020,MODE=NONE\",         \"atag-seq\": [           232351         ]       },       {         \"msg-type\": \"ACCESS\",         \"ao_message\": \"DATE=21-03-15,TIME=13-45-44:SHELF-1:txauto,TANGPF-MCD-O-I657-02:STATUS=COMPLD,EVENTTYPE=LOGOUT,USERTYPE=NETWORK,PORTTYPE=TCP,INTERFACE=HTTP,SESSION=2178\",         \"atag-seq\": [           282495         ]       },       {         \"msg-type\": \"ACCESS\",         \"ao_message\": \"DATE=21-03-15,TIME=13-45-46:SHELF-1:txauto,TANGPF-MCD-O-I657-02:STATUS=COMPLD,EVENTTYPE=LOGOUT,USERTYPE=NETWORK,PORTTYPE=TCP,INTERFACE=HTTP,SESSION=2179\",         \"atag-seq\": [           282496         ]       },       {         \"msg-type\": \"ACCESS\",         \"ao_message\": \"DATE=21-03-15,TIME=13-49-43:SHELF-1:txsn,TANGPF-MCD-O-I657-02:STATUS=COMPLD,EVENTTYPE=LOGIN,USERTYPE=NETWORK,PORTTYPE=TCP,INTERFACE=HTTP,SESSION=2181\",         \"atag-seq\": [           282497         ]       }     ]   } } ");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}