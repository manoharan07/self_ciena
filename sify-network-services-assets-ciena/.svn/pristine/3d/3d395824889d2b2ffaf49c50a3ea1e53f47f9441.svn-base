package com.sify.network.alarms.ciena;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class KeyToolUtils {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("cienaalarms");

	static char[] passphrase = "changeit".toCharArray();
	static String keyStoreFile = "KeyStore6500";

	public static synchronized void retrieveCertificateAndStore(String host, int port) throws Exception {

		File file = new File(keyStoreFile);

		KeyStore ks = null;
		if (file.exists()) {
			logger.info("Loading KeyStore " + file + "...");
			InputStream in = new FileInputStream(file);
			ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(in, passphrase);
			in.close();
		} else {
			ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(null, passphrase);
		}

		SSLContext context = SSLContext.getInstance("TLS");
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(ks);
		X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
		SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
		context.init(null, new TrustManager[] { tm }, null);
		SSLSocketFactory factory = context.getSocketFactory();

		logger.info("Opening connection to " + host + ":" + port + "...");
		System.out.println("Opening connection to " + host + ":" + port + "...");
		SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
		socket.setSoTimeout(10000);
		try {
			logger.info("Starting SSL handshake...");
			socket.startHandshake();
			socket.close();
			
			logger.info("No errors, certificate is already trusted");
		} catch (SSLHandshakeException e) {
			logger.error("Got handshake exception while handshaking ",e);
			logger.info("Trying handshake with TLSv1.2 protocol to  "+host, e);
			
			e.printStackTrace(System.out);
			System.out.println("Got Exception in retrieving certificate, trying handshake with TLSv1.2 protocol to  "+host);
			
			
			retrieveCertificateAndStoreV2(host,port);
			
		} catch (SSLException e) {
			logger.info("Got exception while handshaking "+e);
			e.printStackTrace(System.out);
		}  

		X509Certificate[] chain = tm.chain;
		if (chain == null) {
			logger.info("Could not obtain server certificate chain");
			return;
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

//		logger.info("Server sent " + chain.length + " certificate(s):");
		MessageDigest sha1 = MessageDigest.getInstance("SHA1");
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		for (int i = 0; i < chain.length; i++) {
			X509Certificate cert = chain[i];
			logger.info(" " + (i + 1) + " Subject " + cert.getSubjectDN());
			logger.info("   Issuer  " + cert.getIssuerDN());
			sha1.update(cert.getEncoded());
			logger.info("   sha1    " + toHexString(sha1.digest()));
			md5.update(cert.getEncoded());
			logger.info("   md5     " + toHexString(md5.digest()));
			
		}

		X509Certificate cert = chain[0];
		String alias = host;// + "-" + (k + 1);
		ks.setCertificateEntry(alias, cert);

		OutputStream out = new FileOutputStream(keyStoreFile);
		ks.store(out, passphrase);
		out.close();

		
		//logger.info(cert);
		logger.info("Added certificate to keystore " + keyStoreFile + " using alias '" + alias + "'");
		System.out.println("Added certificate to keystore " + keyStoreFile + " using alias '" + alias + "'");
	}
	
	public static synchronized void retrieveCertificateAndStoreV2(String host, int port) throws Exception {

		File file = new File(keyStoreFile);

		KeyStore ks = null;
		if (file.exists()) {
			logger.info("Loading KeyStore " + file + "...");
			InputStream in = new FileInputStream(file);
			ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(in, passphrase);
			in.close();
		} else {
			ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(null, passphrase);
		}

	//	SSLContext context = SSLContext.getInstance("SSL");
		
		//SSLContext context = SSLContext.getInstance("TLS");
		SSLContext context = SSLContext.getInstance("TLSv1.2");
		context.init(null, null, null);
		SSLContext.setDefault(context); 
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(ks);
		X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
		SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
		context.init(null, new TrustManager[] { tm }, null);
		//context.init(null, null, null);
		SSLSocketFactory factory = context.getSocketFactory();

		logger.info("Opening connection to " + host + ":" + port + "...");
		SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
		socket.setSoTimeout(10000);
		try {
			logger.info("Starting SSL handshake...");
			socket.startHandshake();
			socket.close();
			
			logger.info("No errors, certificate is already trusted");
		} catch (SSLException e) {
			logger.info("Got exception while handshaking "+e);
			e.printStackTrace(System.out);
		}

		X509Certificate[] chain = tm.chain;
		if (chain == null) {
			logger.info("Could not obtain server certificate chain");
			return;
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

//		logger.info("Server sent " + chain.length + " certificate(s):");
		MessageDigest sha1 = MessageDigest.getInstance("SHA1");
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		for (int i = 0; i < chain.length; i++) {
			X509Certificate cert = chain[i];
			logger.info(" " + (i + 1) + " Subject " + cert.getSubjectDN());
			logger.info("   Issuer  " + cert.getIssuerDN());
			sha1.update(cert.getEncoded());
			logger.info("   sha1    " + toHexString(sha1.digest()));
			md5.update(cert.getEncoded());
			logger.info("   md5     " + toHexString(md5.digest()));
			
		}

		X509Certificate cert = chain[0];
		String alias = host;// + "-" + (k + 1);
		ks.setCertificateEntry(alias, cert);

		OutputStream out = new FileOutputStream(keyStoreFile);
		ks.store(out, passphrase);
		out.close();

		
		//logger.info(cert);
		logger.info("Added certificate to keystore " + keyStoreFile + " using alias '" + alias + "'");
        System.out.println("Added certificate to keystore " + keyStoreFile + " using alias '" + alias + "'");	
		
	}


	private static final char[] HEXDIGITS = "0123456789abcdef".toCharArray();

	private static String toHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 3);
		for (int b : bytes) {
			b &= 0xff;
			sb.append(HEXDIGITS[b >> 4]);
			sb.append(HEXDIGITS[b & 15]);
			sb.append(' ');
		}
		return sb.toString();
	}

	private static class SavingTrustManager implements X509TrustManager {

		private final X509TrustManager tm;
		private X509Certificate[] chain;

		SavingTrustManager(X509TrustManager tm) {
			this.tm = tm;
		}

		public X509Certificate[] getAcceptedIssuers() {
			throw new UnsupportedOperationException();
		}

		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			throw new UnsupportedOperationException();
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			this.chain = chain;
			tm.checkServerTrusted(chain, authType);
		}
	}
	
	public static void main(String args[]) {
		if (args.length == 2) {
		try {
		KeyToolUtils.retrieveCertificateAndStore(args[0], Integer.parseInt(args[1]));
		} catch(Exception e) {
			e.printStackTrace(System.out);
		}
		} else {
			logger.info("java KeyToolUtils <serverip or address> <port>");
		}
	}

}
