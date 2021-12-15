package com.sify.network.assets.ciena;


import java.sql.Timestamp;

import com.sify.network.assets.core.api.bean.InventoryDeviceDetails;

public class CienaEquipmentInventory extends InventoryDeviceDetails {

	private static String ip;
	private String nodeName;
	private String nodeIp;
	private String cardId;
	private String cardType;
	private Timestamp date;
	
	public CienaEquipmentInventory(String ip) {
		super(ip);
	}
	public CienaEquipmentInventory(String ip ,String nodeName,String nodeIp,String cardId,String cardType,Timestamp date) {
		super(ip);
		this.nodeName=nodeName;
		this.nodeIp=nodeIp;
		this.cardId=cardId;
		this.cardType=cardType;
		this.date=date;
	}
	
	public static String getIp() {
		return ip;
	}

	public static void setIp(String ip) {
		CienaEquipmentInventory.ip = ip;
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

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "CienaEquipmentInventory [nodeName=" + nodeName + ", nodeIp=" + nodeIp + ", cardId=" + cardId
				+ ", cardType=" + cardType + ", date=" + date + "]";
	}

	@Override
	public String getInsertQuery() {
		String key = this.nodeName +"#"+this.cardId;
		
		String s = String.format("insert into equipments(equipment_key,node_name,node_ip,card_id,card_type,created_date,updated_date)"
				+ "values('%s','%s','%s','%s','%s','%s','%s') on duplicate key update "
				+ "node_name='%s',node_ip='%s',card_id='%s',card_type='%s',updated_date='%s'", 
				key,this.nodeName,this.nodeIp,this.cardId,this.cardType,date,date,this.nodeName,this.nodeIp,this.cardId,this.cardType,date);
		return s;
	}
}
