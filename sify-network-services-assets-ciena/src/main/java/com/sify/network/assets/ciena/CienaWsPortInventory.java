 	package com.sify.network.assets.ciena;


import java.sql.Timestamp;

import com.sify.network.assets.core.api.bean.InventoryDeviceDetails;

public class CienaWsPortInventory extends InventoryDeviceDetails {

	
	private String nodeName;
	private String nodeIp;
	private String portType;
	private String portId;
	private String portName;
	private String portLabel="";
    private String portRate="";
    private String portSpeed="";
    
	private Timestamp date;
	
	public CienaWsPortInventory(String ip) {
		super(ip);
	}
	  
	public CienaWsPortInventory(String ip ,String nodeName,String nodeIp,String portType,String portId,String portName,String portLabel,String portRate,String portSpeed,Timestamp date) {
		super(ip);
		
		this.nodeName=nodeName;
		this.nodeIp=nodeIp;
		this.portType=portType;
		this.portId=portId;
		this.portName=portName;
		this.portLabel=portLabel;
		this.portRate = portRate;
		this.portSpeed = portSpeed;
		this.date=date;
	}
	



	public String getNodeName() {
		return nodeName;
	}


	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}


	public String getNodeIp() {
		return nodeIp;
	}


	public void setNodeIp(String nodeIp) {
		this.nodeIp = nodeIp;
	}



	public String getPortType() {
		return portType;
	}


	public void setPortType(String portType) {
		this.portType = portType;
	}


	public String getPortId() {
		return portId;
	}


	public void setPortId(String portId) {
		this.portId = portId;
	}



	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public String getPortLabel() {
		return portLabel;
	}

	public void setPortLabel(String portLabel) {
		this.portLabel = portLabel;
	}

	public String getPortRate() {
		return portRate;
	}

	public void setPortRate(String portRate) {
		this.portRate = portRate;
	}

	public String getPortSpeed() {
		return portSpeed;
	}

	public void setPortSpeed(String portSpeed) {
		this.portSpeed = portSpeed;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	

	@Override
	public String toString() {
		return "CienaWsPortInventory [nodeName=" + nodeName + ", nodeIp=" + nodeIp + ", portType=" + portType
				+ ", portId=" + portId + ", portName=" + portName + ", portLabel=" + portLabel + ", portRate="
				+ portRate + ", portSpeed=" + portSpeed + ", date=" + date + "]";
	}

	@Override
	public String getInsertQuery() {
		
		String key;
		
		key = this.nodeName + "#" + this.portName;
		 
		String s = String.format("insert into ciena.ws_ports(port_key,node_name,node_ip,port_type,port_id,port_name,port_label,port_rate,port_speed,created_date,updated_date)"
				+ "values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s') on duplicate key update "
				+ "node_name='%s',node_ip='%s',port_type='%s',port_id='%s',port_name='%s',port_label='%s',port_rate='%s',port_speed='%s',updated_date='%s'", 
				key,this.nodeName,this.nodeIp,this.portType,this.portId,this.portName,this.portLabel,this.portRate,this.portSpeed,date,date,
				this.nodeName,this.nodeIp,this.portType,this.portId,this.portName,this.portLabel,this.portRate,this.portSpeed,date);
		return s;
		}
}