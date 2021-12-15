package com.sify.network.alarms.ciena;

public class DeviceDetails {

	public String ip;
	String username;
	String password;
	String port;
	String type;

	public DeviceDetails(String ip, String username, String password, Integer port2) {
		this.ip = ip;
		this.username = username;
		this.password = password;
		this.port = String.valueOf(port2);
		this.type = "Cienna";
	}

	public DeviceDetails() {
		this.ip = "";
		this.username = "";
		this.password = "";
		this.port = "";
		this.type = "";
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "DeviceDetails [ip=" + ip + ", username=" + username + ", password=" + password + ", port=" + port
				+ ", type=" + type + "]";
	}
	
	

}
