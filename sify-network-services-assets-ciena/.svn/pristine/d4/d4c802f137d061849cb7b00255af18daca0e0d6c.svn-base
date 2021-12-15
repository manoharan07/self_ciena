package com.sify.network.assets.ciena;


import java.sql.Timestamp;

import com.sify.network.assets.core.api.bean.InventoryDeviceDetails;

public class CienaWsDeviceInventory extends InventoryDeviceDetails {

	private Timestamp date;
	private String ip;
	private String extendedSid;
	private String nodeName;
	private String shelf;
	private String manufacturer;
	private String model;
	private String siteId;
	private String siteName;
	private String groupDescription;
	private String groupName;
	
	
	public CienaWsDeviceInventory(String ip) {
		super(ip);
	}

	public CienaWsDeviceInventory(String ip , String extendedSid,String nodeName,String shelf,String manufacturer,String model,String siteId,String siteName, String location,String subnetName,Timestamp date) {
		super(ip);
		this.date=date;
		this.ip=ip;
		this.extendedSid=extendedSid;
		this.nodeName=nodeName;
		this.shelf=shelf;
		this.manufacturer=manufacturer;
		this.model=model;
		this.siteId=siteId;
		this.siteName=siteName;
		this.groupDescription=location;
		this.groupName=subnetName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getExtendedSid() {
		return extendedSid;
	}


	public void setExtendedSid(String extendedSid) {
		this.extendedSid = extendedSid;
	}


	public String getNodeName() {
		return nodeName;
	}


	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}


	public String getShelf() {
		return shelf;
	}


	public void setShelf(String shelf) {
		this.shelf = shelf;
	}


	public String getManufacturer() {
		return manufacturer;
	}


	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}


	public String getModel() {
		return model;
	}


	public void setModel(String model) {
		this.model = model;
	}


	public String getSiteId() {
		return siteId;
	}


	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}


	public String getSiteName() {
		return siteName;
	}


	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}


	

	public String getGroupDescription() {
		return groupDescription;
	}

	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Timestamp getDate() {
		return date;
	}


	public void setDate(Timestamp date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "CienaDeviceInventory [date=" + date + ", extendedSid=" + extendedSid + ", nodeName=" + nodeName
				+ ", shelf=" + shelf + ", manufacturer=" + manufacturer + ", model=" + model + ", siteId=" + siteId
				+ ", siteName=" + siteName + ", location=" + groupDescription + ", subnetName=" + groupName + "]";
	}


	@Override
	public String getInsertQuery() {
		String s;
		
		String key;
		
		key = this.nodeName + "#" + this.ip;

		s = String.format("insert into ciena.ws_devices(device_key,device_ip,node_name,shelf,manuf,model,site_name,group_description,group_name,created_date,updated_date)"
				+ "values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s') on duplicate key update "
				+ "device_ip='%s',node_name='%s',shelf='%s',manuf='%s',model='%s',site_name='%s',group_description='%s',"
				+ "group_name='%s',updated_date='%s'", 
				key, this.ip,this.nodeName,this.shelf,this.manufacturer,this.model,this.siteName,this.groupDescription,
				this.groupName,date,date,this.ip,this.nodeName,this.shelf,this.manufacturer,this.model,this.siteName,this.groupDescription,
				this.groupName,date);
		
		return s;
	}
}
