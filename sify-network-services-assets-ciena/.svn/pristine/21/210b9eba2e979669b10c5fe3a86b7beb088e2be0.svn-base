package com.sify.network.alarms.ciena;

import javax.jms.Message;

import org.apache.activemq.command.ActiveMQTextMessage;

import com.google.gson.Gson;
import com.sify.network.assets.ciena.CienaProperties;
//import com.sify.uac.bean.Logger;
import com.sify.network.assets.ciena.ServiceNowRestClient;

public class OpticalAlarmConsumer extends GenericQueueConsumer {
	
	String deviceIP="";

	public OpticalAlarmConsumer() {

/*		user = CienaProperties.get("alert.common.consumer.username", ActiveMQConnection.DEFAULT_USER);
		password = CienaProperties.get("alert.common.consumer.password", ActiveMQConnection.DEFAULT_PASSWORD);
		url = CienaProperties.get("alert.common.consumer.url", ActiveMQConnection.DEFAULT_BROKER_URL);
		subject = CienaProperties.get("alert.common.consumer.subject", "RAW.DEFAULT");
		consumerName = CienaProperties.get("uac.alert.consumer.name", "RAW.DEFAULT.NAME");
		acknowledgeMode = CienaProperties.get("alert.common.consumer.acknowledge_mode", "AUTO_ACKNOWLEDGE");
		topic = CienaProperties.get("alert.common.consumer.topic", false);
		durable = CienaProperties.get("alert.common.consumer.durable", true);
*/		
		
/*		user = CienaProperties.get("alert.common.consumer.username");
		password = CienaProperties.get("alert.common.consumer.password");
		url = CienaProperties.get("alert.common.consumer.url");
		subject = CienaProperties.get("alert.common.consumer.subject");
		consumerName = CienaProperties.get("uac.alert.consumer.name");
		acknowledgeMode = CienaProperties.get("alert.common.consumer.acknowledge_mode");
	//	topic = CienaProperties.get("alert.common.consumer.topic");
	//	durable = CienaProperties.get("alert.common.consumer.durable");
		
	*/	
		user = CienaProperties.get("optical.activemq.username");
		password = CienaProperties.get("optical.activemq.password");
		url = CienaProperties.get("optical.activemq.url");
		subject = CienaProperties.get("optical.activemq.subject");
		consumerName = CienaProperties.get("uac.alert.consumer.name");
		acknowledgeMode = CienaProperties.get("optical.activemq.acknowledge_mode");
	//	topic = CienaProperties.get("optical.activemq.topic");
	//	durable = CienaProperties.get("optical.activemq.durable");

		
		setAckMode(acknowledgeMode);
		System.out.println("uac.alert-common-consumer thread started");



	}
	
	public OpticalAlarmConsumer(String deviceIP) {

/*		user = CienaProperties.get("alert.common.consumer.username", ActiveMQConnection.DEFAULT_USER);
		password = CienaProperties.get("alert.common.consumer.password", ActiveMQConnection.DEFAULT_PASSWORD);
		url = CienaProperties.get("alert.common.consumer.url", ActiveMQConnection.DEFAULT_BROKER_URL);
		subject = CienaProperties.get("alert.common.consumer.subject", "RAW.DEFAULT");
		consumerName = CienaProperties.get("uac.alert.consumer.name", "RAW.DEFAULT.NAME");
		acknowledgeMode = CienaProperties.get("alert.common.consumer.acknowledge_mode", "AUTO_ACKNOWLEDGE");
		topic = CienaProperties.get("alert.common.consumer.topic", false);
		durable = CienaProperties.get("alert.common.consumer.durable", true);
*/		
		
/*		user = CienaProperties.get("alert.common.consumer.username");
		password = CienaProperties.get("alert.common.consumer.password");
		url = CienaProperties.get("alert.common.consumer.url");
		subject = CienaProperties.get("alert.common.consumer.subject");
		consumerName = CienaProperties.get("uac.alert.consumer.name");
		acknowledgeMode = CienaProperties.get("alert.common.consumer.acknowledge_mode");
	//	topic = CienaProperties.get("alert.common.consumer.topic");
	//	durable = CienaProperties.get("alert.common.consumer.durable");
		
	*/	
		user = CienaProperties.get("optical.activemq.username");
		password = CienaProperties.get("optical.activemq.password");
		url = CienaProperties.get("optical.activemq.url");
		subject = CienaProperties.get("optical.activemq.subject") + "-" + deviceIP;
		consumerName = CienaProperties.get("uac.alert.consumer.name");
		acknowledgeMode = CienaProperties.get("optical.activemq.acknowledge_mode");
	//	topic = CienaProperties.get("optical.activemq.topic");
	//	durable = CienaProperties.get("optical.activemq.durable");

		
		setAckMode(acknowledgeMode);
		System.out.println("uac.alert-common-consumer thread started");
		
		this.deviceIP = deviceIP;



	}


	@Override
	public void onMessage(Message message) {
		try {
			//System.out.println("AlertConsumer :: onMessage");
			if (message instanceof ActiveMQTextMessage) {
				ActiveMQTextMessage objMsg = (ActiveMQTextMessage) message;
				String jsonData = objMsg.getText();
				 System.out.println("consumed alarm from  "+this.deviceIP + " : "	+ jsonData);
				 
				if (CienaProperties.get("servicenow.alarm.forwarding.ciena").equals("true")) {
					ServiceNowRestClient.postAlarm(jsonData);
				}
					
				if (CienaProperties.get("servicenowtest.alarm.forwarding.ciena").equals("true")) {
					ServiceNowRestClient.postAlarmToServiceNowTest(jsonData);
				}

				 
				/*System.out.println("Alert Common consumer on text message");
				Gson gson = new Gson();
				//Alert alert = gson.fromJson(jsonData, Alert.class);

				try {
					System.out.println("Alert Common consumer Alert IP: " + message);
					//pushAlertToQ(alert.getAlert());
				} catch (Exception e) {
					e.printStackTrace();
					//Logger.logException(e, "Exception in UACAlertConsumer: ");
				}
*/			
				 } else {
				System.out.println("Error : ActiveMQTextMessage is not received");
			}
		} catch (Exception e) {
			e.printStackTrace();
			//Logger.logException(e, "Exception in UACAlertConsumer: ");
		}
	}


}
