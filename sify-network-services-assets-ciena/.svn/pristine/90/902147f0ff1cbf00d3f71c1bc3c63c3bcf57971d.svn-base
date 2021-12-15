package com.sify.network.assets.ciena;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SmsEmailClient {
	
	//private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("Viptela");
	org.apache.log4j.Logger emailLogger = org.apache.log4j.Logger.getLogger("emaillog");
	org.apache.log4j.Logger smsLogger = org.apache.log4j.Logger.getLogger("smslog");
	
	
	public void sendSmsEmailAlerts(String message) {
		sendSmsAlerts(message);
		sendEmailAlerts(message);
	}

	
	public void sendSmsAlerts(String message) {
		if (CienaProperties.get("sms.enabled").equals("true")) {
			sendSmsToNumbers(message);
		}
	}
	
	public void sendEmailAlerts(String message) {
		if (CienaProperties.get("email.enabled").equals("true")) {
			sendEmail(message);
		}
	}
	
	public String sendEmail(String emailContent) {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		
		String time=dateFormat.format(cal.getTime());

	String to = CienaProperties.get("email.to");

		emailLogger.info("To Email :: " + to);
		if ((to != null) && (!to.equalsIgnoreCase("NA")) && (!to.isEmpty()) && (to.contains("@"))) {
			String emailResponse = "";
			final String username = CienaProperties.get("m_username");
			final String password = CienaProperties.get("m_password");
			String host = CienaProperties.get("host");
			String port = CienaProperties.get("port");
			String from = CienaProperties.get("from");

			Properties emailProps = null;

			emailProps = new Properties();
			emailProps.put("mail.smtp.auth", "true");
			emailProps.put("mail.smtp.starttls.enable", "true");
			emailProps.put("mail.smtp.host", host);
			emailProps.put("mail.smtp.port", port);
			emailProps.put("mail.smtp.connectiontimeout ",
					Integer.parseInt(CienaProperties.get("mail.smtp.connectiontimeout")));
			emailProps.put("mail.smtp.timeout", Integer.parseInt(CienaProperties.get("mail.smtp.timeout")));
			emailProps.put("mail.smtp.writetimeout",
					Integer.parseInt(CienaProperties.get("mail.smtp.writetimeout")));

			String subject = emailContent;
			

			emailLogger.info("Email -- subject : " + subject);

			Session session = Session.getInstance(emailProps, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});

			try {
			Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(from));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

				message.setSubject(subject);
				BodyPart htmlPart = new MimeBodyPart();
				//htmlPart.setContent(writer.toString(), "text/html");
				htmlPart.setContent(emailContent, "text/html");
				htmlPart.setDisposition(BodyPart.INLINE);
				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(htmlPart);
				message.setContent(multipart, "text/html; charset=ISO-8859-1");
				Transport.send(message);

				emailLogger.info("Sent email '" + subject + "' to " + to);
				emailLogger.info("Sent email '" + subject + "' to " + to);

				return emailContent;

			} catch (MessagingException e) {
				StringWriter err = new StringWriter();
				e.printStackTrace(new PrintWriter(err));
				String errStr = err.toString();
				emailLogger.error("MessagingException in EmailConsumer sendEmail: ", e);
				e.printStackTrace(System.out);

				return emailResponse = errStr;

			} catch (Exception e1) {
				e1.printStackTrace(System.out);

				emailLogger.error("Exception in EmailConsumer processTemplate: ", e1);
			}
			System.out.println("Email response " + emailResponse);
			return emailResponse;
		} else {
			emailLogger.error("Vaild email is not configured for restart alerts" );
		}

		emailLogger.info("Sent mail successfully");
		
		return emailContent;
	}
	
	
	public String sendEmailWithAttachment(String dateStr, String emailContent,String fileName,String fileNameWithPath) {
		
		emailLogger.info("Sending report email with the file : "+fileName);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		
		String time=dateFormat.format(cal.getTime());

	String to = CienaProperties.get("ciena.amazonhealthreport.email.to");

		emailLogger.info("To Email :: " + to);
		if ((to != null) && (!to.equalsIgnoreCase("NA")) && (!to.isEmpty()) && (to.contains("@"))) {
			String emailResponse = "";
			final String username = CienaProperties.get("m_username");
			final String password = CienaProperties.get("m_password");
			String host = CienaProperties.get("host");
			String port = CienaProperties.get("port");
			String from = CienaProperties.get("from");

			Properties emailProps = null;

			emailProps = new Properties();
			emailProps.put("mail.smtp.auth", "true");
			emailProps.put("mail.smtp.starttls.enable", "true");
			emailProps.put("mail.smtp.host", host);
			emailProps.put("mail.smtp.port", port);
			emailProps.put("mail.smtp.connectiontimeout ",
					Integer.parseInt(CienaProperties.get("mail.smtp.connectiontimeout")));
			emailProps.put("mail.smtp.timeout", Integer.parseInt(CienaProperties.get("mail.smtp.timeout")));
			emailProps.put("mail.smtp.writetimeout",
					Integer.parseInt(CienaProperties.get("mail.smtp.writetimeout")));

			String subject = emailContent;
			

			emailLogger.info("Email -- subject : " + subject);

			Session session = Session.getInstance(emailProps, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});

			try {
				
				MimeMessage message = new MimeMessage(session);  
			    message.setFrom(new InternetAddress(from));  
			   // message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
			    
			    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			    
			    message.setSubject("BOM53-BOM59 HealthCheckupReport generated on "+dateStr);  
			      
			    //3) create MimeBodyPart object and set your message text     
			    BodyPart messageBodyPart1 = new MimeBodyPart();  
			    messageBodyPart1.setText(emailContent);  
			      
			    //4) create new MimeBodyPart object and set DataHandler object to this object      
			    MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
			  
			   // String filename = "SendAttachment.java";//change accordingly  
			    DataSource source = new FileDataSource(fileNameWithPath);  
			    messageBodyPart2.setDataHandler(new DataHandler(source));  
			    messageBodyPart2.setFileName(fileName);  
			     
			     
			    //5) create Multipart object and add MimeBodyPart objects to this object      
			    Multipart multipart = new MimeMultipart();  
			    multipart.addBodyPart(messageBodyPart1);  
			    multipart.addBodyPart(messageBodyPart2);  
			  
			    //6) set the multiplart object to the message object  
			    message.setContent(multipart );  
			     
			    //7) send message  
			    Transport.send(message);  

				
				emailLogger.info("Sent email '" + subject + "' to " + to);

				return emailContent;

			} catch (MessagingException e) {
				StringWriter err = new StringWriter();
				e.printStackTrace(new PrintWriter(err));
				String errStr = err.toString();
				emailLogger.error("MessagingException in EmailConsumer sendEmail: ", e);
				e.printStackTrace(System.out);

				return emailResponse = errStr;

			} catch (Exception e1) {
				e1.printStackTrace(System.out);

				emailLogger.error("Exception in EmailConsumer processTemplate: ", e1);
			}
			System.out.println("Email response " + emailResponse);
			return emailResponse;
		} else {
			emailLogger.error("Vaild email is not configured for restart alerts" );
		}

		emailLogger.info("Sent mail successfully");
		
		return emailContent;
	}



	
	
	public void sendSmsToNumbers(String message) {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		
		String time=dateFormat.format(cal.getTime());
		
		String multipleNumbers=CienaProperties.get("sms.to");
		String numbers[] = multipleNumbers.split(",");
		for(String number:numbers) {
			String smsResponse = sendSms(number,message);
			smsLogger.info("Sent to "+number+", SMS: '"+message+"', SMS Response:"+smsResponse);
		}
		
		smsLogger.info("Sent SMS successfully : "+message);
	}
	
	public String sendSms(String mnumber, String message) {

		String smsUrl = CienaProperties.get("SmsLinkStatus.URL");
		String smsUsername = CienaProperties.get("sms.username");
		String smsPassword = CienaProperties.get("sms.password");

		String line = "";
		BufferedReader bufReader = null;
		try {
			if (message == null || message.isEmpty()) {
				return "message part is empty";
			}
			// https with outcertificate (keystore)
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					// TODO Auto-generated method stub
					
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					// TODO Auto-generated method stub
					return null;
				}
				
				
			} };
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
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			String requestUrl = smsUrl + "?" + "username=" + URLEncoder.encode(smsUsername, "UTF-8") + "&password="
					+ URLEncoder.encode(smsPassword, "UTF-8") + "&to=" + URLEncoder.encode("91" + mnumber, "UTF-8")
					+ "&text=" + URLEncoder.encode(message, "UTF-8");

			URL url;
			// SmsLinkStatus.URL=http://luna.a2wi.co.in:7501/failsafe/HttpPublishLink?pcode=sify&acode=sify&pin=12883&signature=SIFYXX
			// webServiceURL.append(smsUrl);
			// URL url = new URL(smsUrl);
			// webServiceURL.append(smsUrl);
			// webServiceURL.append("?");
			// webServiceURL.append("pcode=sify&acode=sify&pin=12883&signature=SIFYXX");
			url = new URL(requestUrl);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);

			bufReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			line = bufReader.readLine();

			bufReader.close();

		} catch (Exception ex) {
			line = ex.toString();
			ex.printStackTrace(System.out);
			smsLogger.error("Got error while sending SMS", ex);
			return line;
		}
		return line;
	}

	
	public static void main(String args[]) {
		CienaProperties cienaProps = new CienaProperties();
		SmsEmailClient smsClient = new SmsEmailClient();
		smsClient.sendSmsEmailAlerts("You are you using your phone more than 8 hours");
	}

}
