package com.sify.network.assets.ciena;


import java.sql.Timestamp;

import com.sify.network.assets.core.api.bean.InventoryDeviceDetails;

public class CienaNetworkInventory extends InventoryDeviceDetails {

	private Timestamp date;
	private static String ip;
	private String nodeName;
	private String nodeIp;
	private String adjId;
	private String txTag;
	private String rxActual;
	
	public CienaNetworkInventory(String ip) {
		super(ip);
	}

	public CienaNetworkInventory(String ip ,String nodeName,String nodeIp,String adjId,String txTag,String rxActua,Timestamp date) {
		super(ip);
		this.nodeName=nodeName;
		this.nodeIp=nodeIp;
		this.adjId=adjId;
		this.txTag=txTag;
		this.rxActual=rxActual;
		this.date=date;
	}

	public static String getIp() {
		return ip;
	}


	public static void setIp(String ip) {
		CienaNetworkInventory.ip = ip;
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


	public String getAdjId() {
		return adjId;
	}


	public void setAdjId(String adjId) {
		this.adjId = adjId;
	}


	public String getTxTag() {
		return txTag;
	}


	public void setTxTag(String txTag) {
		this.txTag = txTag;
	}


	public String getRxActual() {
		return rxActual;
	}


	public void setRxActual(String rxActual) {
		this.rxActual = rxActual;
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
				+ adjId + ", txTag=" + txTag + ", rxActual=" + rxActual + "]";
	}

	@Override
	public String getInsertQuery() {
		String s;
		
		String key = this.nodeName + "#" + this.adjId;
		
		s = "insert into network(network_key,node_name,node_ip,adj_id,tx_tag,rx_actual,created_date,updated_date) values "
				+ "('"+key+"','"+this.nodeName+"', '"+this.nodeIp+"', '"+this.adjId+"', '"+txTag+"', '"+rxActual +"','"+date+"','"+date+"') "
						+ "ON DUPLICATE KEY UPDATE "
						+ "node_name='"+this.nodeName+"', node_ip='"+this.nodeIp+"', adj_id='"+this.adjId+"', tx_tag='"+this.txTag+"', rx_actual='"+this.rxActual+"',updated_date='"+date+"'";
		return s;
	}
}
