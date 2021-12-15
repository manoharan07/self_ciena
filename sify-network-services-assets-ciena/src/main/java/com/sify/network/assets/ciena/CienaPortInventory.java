 	package com.sify.network.assets.ciena;


import java.sql.Timestamp;

import com.sify.network.assets.core.api.bean.InventoryDeviceDetails;

public class CienaPortInventory extends InventoryDeviceDetails {

	
	private String nodeName;
	private String nodeIp;
	private String shelfSlotPort;
	private String portType;
	private String portId;
	private String clfi;
	private String label;
	private String sncKey;
	private Timestamp date;
	
	public CienaPortInventory(String ip) {
		super(ip);
	}
	  
	public CienaPortInventory(String ip ,String nodeName,String nodeIp,String shelfSlotPort,String portType,String portId,String clfi,String label,String sncKey,Timestamp date) {
		super(ip);
		
		this.nodeName=nodeName;
		this.nodeIp=nodeIp;
		this.shelfSlotPort=shelfSlotPort;
		this.portType=portType;
		this.portId=portId;
		this.clfi=clfi;
		this.label=label;
		this.sncKey=sncKey;
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


	public String getShelfSlotPort() {
		return shelfSlotPort;
	}


	public void setShelfSlotPort(String shelfSlotPort) {
		this.shelfSlotPort = shelfSlotPort;
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


	public String getClfi() {
		return clfi;
	}


	public void setClfi(String clfi) {
		this.clfi = clfi;
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public String getSncKey() {
		return sncKey;
	}


	public void setSncKey(String sncKey) {
		this.sncKey = sncKey;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "CienaPortInventory [nodeName=" + nodeName + ", nodeIp=" + nodeIp + ", shelfSlotPort=" + shelfSlotPort
				+ ", portType=" + portType + ", portId=" + portId + ", clfi=" + clfi + ", label=" + label + ", sncKey="
				+ sncKey + ", date=" + date + "]";
	}

	@Override
	public String getInsertQuery() {
		
		String key;
		
		key = this.nodeName + "#" + this.portId;
		 
		String s = String.format("insert into ports(port_key,node_name,node_ip,shelf_slot_port,port_type,port_id,clfi,label,snc_key,created_date,updated_date)"
				+ "values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s') on duplicate key update "
				+ "node_name='%s',node_ip='%s',shelf_slot_port='%s',port_type='%s',port_id='%s',clfi='%s',label='%s',snc_key='%s',updated_date='%s'", 
				key,this.nodeName,this.nodeIp,this.shelfSlotPort,this.portType,this.portId,this.clfi,this.label,this.sncKey,date,date,this.nodeName,this.nodeIp,this.shelfSlotPort,this.portType,this.portId,this.clfi,this.label,this.sncKey,date);
		return s;
		}
}