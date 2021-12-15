package com.sify.network.alarms.ciena;


import java.util.HashMap;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import com.sify.network.assets.ciena.CienaProperties;


/**
 * @author
 * 
 */
public class OpticalAlarmProducer extends GenericQueueProducer {
	
	String deviceIP="";
	
	//private  static Logger log = Logger.getLogger(AlertProducer.class); 
	private static OpticalAlarmProducer alarmProducer=null;
	
	private static HashMap<String,OpticalAlarmProducer>  producerMap = new HashMap<String,OpticalAlarmProducer>();
	
	public static OpticalAlarmProducer getInstance() {
		
		if (alarmProducer==null) {
			alarmProducer=new OpticalAlarmProducer(); 
		}
		return alarmProducer;
	}
	
	public static OpticalAlarmProducer getInstance(String deviceIP) {
		
		OpticalAlarmProducer alarmProducer = producerMap.get(deviceIP);
		
		if (alarmProducer==null) {
			alarmProducer=new OpticalAlarmProducer(deviceIP); 
			producerMap.put(deviceIP, alarmProducer);
		} 
		
		
		
		return alarmProducer;
	}

	private OpticalAlarmProducer() {

		String user = CienaProperties.get("optical.activemq.user");
		String password = CienaProperties.get("optical.activemq.password");
		String url = CienaProperties.get("optical.activemq.url");
		String subject = CienaProperties.get("optical.activemq.subject");
		String topicStr =  CienaProperties.get("optical.activemq.topic");
		String persistentStr = CienaProperties.get("optical.activemq.persistent");
		
		
		
		boolean topic=false;
		boolean persistent=true;
		if (topicStr!=null && topicStr.equals("true")) {
			topic=true;
		} 
		
		if (persistentStr!=null && persistentStr.equals("true")) {
			topic=true;
		}

		loadProperties(user, password, url, subject, topic, persistent);
	}
	
	private OpticalAlarmProducer(String deviceIP) {
		this.deviceIP = deviceIP;


		String user = CienaProperties.get("optical.activemq.user");
		String password = CienaProperties.get("optical.activemq.password");
		String url = CienaProperties.get("optical.activemq.url");
		String subject = CienaProperties.get("optical.activemq.subject")+ "-" +deviceIP;
		String topicStr =  CienaProperties.get("optical.activemq.topic");
		String persistentStr = CienaProperties.get("optical.activemq.persistent");
		
		
		
		boolean topic=false;
		boolean persistent=true;
		if (topicStr!=null && topicStr.equals("true")) {
			topic=true;
		} 
		
		if (persistentStr!=null && persistentStr.equals("true")) {
			topic=true;
		}

		loadProperties(user, password, url, subject, topic, persistent);
	}


	public synchronized void send(String link) {

		try {
			//ObjectMessage objMsg = session.createObjectMessage(link);
			
			TextMessage textMsg = session.createTextMessage(link);
			
			
			producer.send(textMsg);
			System.out.println("Send trap Data to "+deviceIP+"-Q : " + textMsg);
			if (transacted) {
				session.commit();
			}

			//log.info("Send Data to queue: "+link);

		} catch (JMSException je) {
			System.out.println(je);
			je.printStackTrace();
			
			//Logger.logException(je, "Exception in sending the data to Queue !");
		}
	}

	

	
}
