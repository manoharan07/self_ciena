package com.sify.network.alarms.ciena;

import java.io.IOException;

import javax.jms.ExceptionListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * @author srini
 * 
 */
public abstract class GenericQueueConsumer extends Thread implements ExceptionListener {
	public String user;
	public String password;
	public String url;
	public String subject;
	public boolean topic;
	public boolean durable;
	public boolean transacted;
	public String clientId;
	public boolean running;
	public Session session;
	public Destination destination;
	public int ackMode = Session.AUTO_ACKNOWLEDGE;
	public String consumerName;
	public long batch = 10; // Default batch size for CLIENT_ACKNOWLEDGEMENT or
							// SESSION_TRANSACTED
	public int messagesReceived = 0;
	public String acknowledgeMode;

	public void run() {

		MessageConsumer consumer = null;
		Connection connection = null;
		ActiveMQConnectionFactory connectionFactory = null;

		try {
			running = true;

			connectionFactory = new ActiveMQConnectionFactory(user, password, url);

			connection = connectionFactory.createConnection();
			if (durable && clientId != null && clientId.length() > 0 && !"null".equals(clientId)) {
				connection.setClientID(clientId);
			}

			connection.setExceptionListener(this);

			connection.start();

			// receciver_ip = ConsumerProps.get("poller_ip");
			session = connection.createSession(transacted, ackMode);

			// Logger.logMessage("activemq session" + session);
			if (topic) {
				destination = session.createTopic(subject);
			} else {
				destination = session.createQueue(subject);
			}

			if (durable && topic) {
				consumer = session.createDurableSubscriber((Topic) destination, consumerName);
			} else {
				consumer = session.createConsumer(destination);
			}

			consumeMessages(connection, session, consumer);

		} catch (Exception e) {
			e.printStackTrace();
			/*
			 * Logger.logMessage("activemq consumer run : exception " + e.getMessage());
			 */
		}
	}

	protected void consumeMessages(Connection connection, Session session, MessageConsumer consumer)
			throws JMSException, IOException {
		boolean doLoop = true;
		try {
			while (doLoop && isRunning()) {
				try {
					Message message = consumer.receive(1000);

					if (message != null) {
						messagesReceived++;
						onMessage(message);
					}

					if (transacted) {
						if ((messagesReceived % batch) == 0) {
							session.commit();
							messagesReceived = 0;
						}
					} else if (ackMode == Session.CLIENT_ACKNOWLEDGE) {
						if ((messagesReceived % batch) == 0) {
							message.acknowledge();
							messagesReceived = 0;
						}
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			consumer.close();
			session.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setAckMode(String ackMode) {
		if ("CLIENT_ACKNOWLEDGE".equals(ackMode)) {
			this.ackMode = Session.CLIENT_ACKNOWLEDGE;
		}
		if ("AUTO_ACKNOWLEDGE".equals(ackMode)) {
			this.ackMode = Session.AUTO_ACKNOWLEDGE;
		}
		if ("DUPS_OK_ACKNOWLEDGE".equals(ackMode)) {
			this.ackMode = Session.DUPS_OK_ACKNOWLEDGE;
		}
		if ("SESSION_TRANSACTED".equals(ackMode)) {
			this.ackMode = Session.SESSION_TRANSACTED;
		}
	}

	public void setClientId(String clientID) {
		this.clientId = clientID;
	}

	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}

	public void setDurable(boolean durable) {
		this.durable = durable;
	}

	public void setPassword(String pwd) {
		this.password = pwd;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setTopic(boolean topic) {
		this.topic = topic;
	}

	public void setQueue(boolean queue) {
		this.topic = !queue;
	}

	public void setTransacted(boolean transacted) {
		this.transacted = transacted;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setBatch(long batch) {
		this.batch = batch;
	}

	public synchronized void onException(JMSException ex) {
		// Logger.logError("[" + this.getName()
		// + "] JMS Exception occured. Shutting down client.");
		running = false;
	}

	synchronized boolean isRunning() {
		return running;
	}

	public abstract void onMessage(Message message);
	

}
