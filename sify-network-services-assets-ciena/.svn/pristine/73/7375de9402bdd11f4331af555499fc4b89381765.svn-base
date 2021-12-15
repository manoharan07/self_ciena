package com.sify.network.assets.ciena;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class CienaProperties {

	private static Logger logger = Logger.getLogger(CienaProperties.class.getName());

	private static Properties properties;

	public CienaProperties() {
		properties = new Properties();
		loadProperties();
	}

	public void loadProperties() {
		synchronized (properties) {
			String file = "config" + File.separator + "ciena.properties";
			try {
				properties.load(new FileInputStream(file));
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	public static String get(String key) {
		return properties.getProperty(key);
	}

	public String get(String key, String defaultValue) {
		String value = properties.getProperty(key);
		if (value == null || value.isEmpty()) {
			value = defaultValue;
		}
		return value;
	}

	public int get(String key, int defaultValue) {
		try {
			return Integer.parseInt(properties.getProperty(key));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			logger.error(e.getMessage(),e);
			return defaultValue;
		}
	}
}
