package com.sify.network.assets.ciena;


import java.sql.Timestamp;

import com.sify.network.assets.core.api.bean.InventoryDeviceDetails;

public class CienaAdjacencyInventory extends InventoryDeviceDetails {

	private Timestamp date;
//	private static String ip;
	private String nodeName;
	private String nodeIp;
	private String adjId;
	private String provFeAddr;
	private String discFeAddr;
	private String portLabel;
	
	
	public CienaAdjacencyInventory(String ip) {
		super(ip);
	}

	public CienaAdjacencyInventory(String ip ,String nodeName,String nodeIp,String adjId,String provFeAddr,String discFeAddr,String portLabel,Timestamp date) {
		super(ip);
		this.nodeName=nodeName;
		this.nodeIp=nodeIp;
		this.adjId=adjId;
		this.provFeAddr=provFeAddr;
		this.discFeAddr=discFeAddr;
		this.portLabel=portLabel;
		this.date=date;
	}

/*	public static String getIp() {
		return ip;
	}


	public static void setIp(String ip) {
		CienaAdjacencyInventory.ip = ip;
	}
*/

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


	public String getAdjId() {
		return adjId;
	}


	public void setAdjId(String adjId) {
		this.adjId = adjId;
	}



	public String getProvFeAddr() {
		return provFeAddr;
	}

	public void setProvFeAddr(String provFeAddr) {
		this.provFeAddr = provFeAddr;
	}

	public String getDiscFeAddr() {
		return discFeAddr;
	}

	public void setDiscFeAddr(String discFeAddr) {
		this.discFeAddr = discFeAddr;
	}

	public String getPortLabel() {
		return portLabel;
	}

	public void setPortLabel(String portLabel) {
		this.portLabel = portLabel;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "CienaNetworkInventory [date=" + date + ", nodeName=" + nodeName + ", nodeIp=" + nodeIp + ", adjId="
				+ adjId + ", provFeAddr=" + provFeAddr + ", discFeAddr=" + discFeAddr + ", portLabel=" + portLabel +"]";
	}

	@Override
	public String getInsertQuery() {
		String s;
		
		String key = this.nodeName + "#" + this.adjId;
		
		s = "insert into adjacencies(adjacency_key,node_name,node_ip,adj_id,prov_fe_addr,disc_fe_addr,port_label,created_date,updated_date) values "
				+ "('"+key+"','"+this.nodeName+"', '"+this.nodeIp+"', '"+this.adjId+"', '"+this.provFeAddr+"', '"+this.discFeAddr +"', '"+this.portLabel  +"','"+date+"','"+date+"') "
						+ "ON DUPLICATE KEY UPDATE "
						+ "node_name='"+this.nodeName+"', node_ip='"+this.nodeIp+"', adj_id='"+this.adjId+"', prov_fe_addr='"+this.provFeAddr+"', disc_fe_addr='"+this.discFeAddr+"', port_label='"+this.portLabel+"',updated_date='"+date+"'";
		
		System.out.println("adjacency query : "+s);
		
		
		return s;
	}
}
