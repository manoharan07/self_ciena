package com.sify.network.assets.ciena;

import java.sql.Timestamp;

import com.sify.network.assets.core.api.bean.InventoryDeviceDetails;

public class CienaWsCircuitInventory extends InventoryDeviceDetails {

	private String circuitKey;
	private String aEndDeviceIp="";
	private String aEndNodeName="";
	private String aEndPoint="";
	private String zEndDeviceIp="";
	private String zEndNodeName="";
	private String zEndPoint="";
	private String circuitName="";
	private Timestamp date;
	
	public CienaWsCircuitInventory(String ip) {
		super(ip);
	}
	
	
	

	public CienaWsCircuitInventory(String sncKey, String ip, String aEndDeviceIp, String aEndNodeName,
			String aEndPoint, String aEndSncId, String aEndEpState, String zEndDeviceIp, String zEndNodeName,
			String zEndPoint, String zEndSncId, String zEndEpState, String label, String sncLineType, String frequency,
			String cktid,Timestamp date) {
		super(ip);
		this.circuitKey = sncKey;
		this.aEndDeviceIp = aEndDeviceIp;
		this.aEndNodeName = aEndNodeName;
		this.aEndPoint = aEndPoint;
		
		this.zEndDeviceIp = zEndDeviceIp;
		this.zEndNodeName = zEndNodeName;
		this.zEndPoint = zEndPoint;
		this.circuitName = label;
		this.date=date;
	}

	public String getSncKey() {
		return circuitKey;
	}

	public void setSncKey(String sncKey) {
		this.circuitKey = sncKey;
	}

	public String getaEndDeviceIp() {
		return aEndDeviceIp;
	}

	public void setaEndDeviceIp(String aEndDeviceIp) {
		this.aEndDeviceIp = aEndDeviceIp;
	}

	public String getaEndNodeName() {
		return aEndNodeName;
	}

	public void setaEndNodeName(String aEndNodeName) {
		this.aEndNodeName = aEndNodeName;
	}

	public String getaEndPoint() {
		return aEndPoint;
	}

	public void setaEndPoint(String aEndPoint) {
		this.aEndPoint = aEndPoint;
	}


	


	public String getCircuitKey() {
		return circuitKey;
	}




	public void setCircuitKey(String circuitKey) {
		this.circuitKey = circuitKey;
	}




	public String getzEndDeviceIp() {
		return zEndDeviceIp;
	}




	public void setzEndDeviceIp(String zEndDeviceIp) {
		this.zEndDeviceIp = zEndDeviceIp;
	}




	public String getzEndNodeName() {
		return zEndNodeName;
	}

	public void setzEndNodeName(String zEndNodeName) {
		this.zEndNodeName = zEndNodeName;
	}

	public String getzEndPoint() {
		return zEndPoint;
	}

	public void setzEndPoint(String zEndPoint) {
		this.zEndPoint = zEndPoint;
	}


	public String getCircuitName() {
		return circuitName;
	}

	public void setCircuitName(String label) {
		this.circuitName = label;
	}


	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}


	


	@Override
	public String toString() {
		return "CienaWsCircuitInventory [circuitKey=" + circuitKey + ", aEndDeviceIp=" + aEndDeviceIp
				+ ", aEndNodeName=" + aEndNodeName + ", aEndPoint=" + aEndPoint + ", zEndDeviceIp=" + zEndDeviceIp
				+ ", zEndNodeName=" + zEndNodeName + ", zEndPoint=" + zEndPoint + ", label=" + circuitName + ", date=" + date
				+ "]";
	}




	@Override
	public String getInsertQuery() {

		String s="";
		
	
		if (aEndDeviceIp.isEmpty()) {
			//skip aend parameters when updating.
			s = String.format("insert into ws_circuits(circuit_key,aend_device_ip,aend_node_name,aend_endpoint,zend_device_ip,zend_node_name,zend_endpoint,circuit_name,created_date,updated_date)"
					+ "values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s') on duplicate key update "
					+ "circuit_key='%s',zend_device_ip='%s',zend_node_name='%s',zend_endpoint='%s',circuit_name='%s',updated_date='%s'", 
					this.circuitKey,this.aEndDeviceIp,this.aEndNodeName,this.aEndPoint,this.zEndDeviceIp,this.zEndNodeName,this.zEndPoint,this.circuitName,date,date,
					this.circuitKey,this.zEndDeviceIp,this.zEndNodeName,this.zEndPoint,this.circuitName,date);
		} else {
			//skip zend parameters when updating.
			s = String.format("insert into ws_circuits(circuit_key,aend_device_ip,aend_node_name,aend_endpoint,zend_device_ip,zend_node_name,zend_endpoint,circuit_name,created_date,updated_date)"
					+ "values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s') on duplicate key update "
					+ "circuit_key='%s',aend_device_ip='%s',aend_node_name='%s',aend_endpoint='%s',circuit_name='%s',updated_date='%s'", 
					this.circuitKey,this.aEndDeviceIp,this.aEndNodeName,this.aEndPoint,this.zEndDeviceIp,this.zEndNodeName,this.zEndPoint,this.circuitName,date,date,
					this.circuitKey,this.aEndDeviceIp,this.aEndNodeName,this.aEndPoint,this.circuitName,date);
		}
				
		return s;
		
		}
}
