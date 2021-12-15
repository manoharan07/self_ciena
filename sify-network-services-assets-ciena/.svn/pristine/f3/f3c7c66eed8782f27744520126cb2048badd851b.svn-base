package com.sify.network.alarms.ciena;




import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;







/**
 * @author
 * 
 */
public abstract class GenericQueueProducer  {
	


	public Destination destination;
	public String user;
	public String password;
	public String url;
	public String subject;
	public boolean topic;
	public boolean transacted;
	public boolean persistent;
	public Session session = null;
	public MessageProducer producer = null;
	public Connection connection = null;
	
	
	
	protected void loadProperties(String user, String password, String url,
			String subject, boolean topic, boolean persistent) {
		this.user = user;
		this.password = password;
		this.url = url;
		this.subject = subject;
		this.topic = topic;
		this.persistent = persistent;
	}

	public void connect() {

		try {
			// Create the connection.
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
					user, password, url);
			connection = connectionFactory.createConnection();
			connection.start();
			
			// Create the session
			session = connection.createSession(transacted,
					Session.AUTO_ACKNOWLEDGE);
			if (topic) {
				destination = session.createTopic(subject);
			} else {
				destination = session.createQueue(subject);
			}

			// Create the producer.
			producer = session.createProducer(destination);
			if (persistent) {
				producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			} else {
				producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			}

		} catch (Exception e) {
			//Logger.logError("Exception in Active MQ !", e);
		}
	}


	public void close() {
		try {
			if (null != connection) {
				connection.close();
				if (null != session) {
					session.close();
					if (null != producer) {
						producer.close();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// Logger.logError("Exception in closing the Connection | Session | Producer !",
			// e);
		} finally {
			connection = null;
			session = null;
			producer = null;
		}

	}

	public void cleanup() {
		close();
	}


	public abstract void send(String pk);
}
