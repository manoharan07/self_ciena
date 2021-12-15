package com.sify.network.assets.ciena;


import java.sql.Timestamp;

import com.sify.network.assets.core.api.bean.InventoryDeviceDetails;

public class CienaDeviceInventory extends InventoryDeviceDetails {

	private Timestamp date;
	private String ip;
	private String extendedSid;
	private String nodeName;
	private String shelf;
	private String manufacturer;
	private String model;
	private String siteId;
	private String siteName;
	private String location;
	private String subnetName;
	
	
	public CienaDeviceInventory(String ip) {
		super(ip);
	}

	public CienaDeviceInventory(String ip , String extendedSid,String nodeName,String shelf,String manufacturer,String model,String siteId,String siteName, String location,String subnetName,Timestamp date) {
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
		this.location=location;
		this.subnetName=subnetName;
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


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public String getSubnetName() {
		return subnetName;
	}


	public void setSubnetName(String subnetName) {
		this.subnetName = subnetName;
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
				+ ", siteName=" + siteName + ", location=" + location + ", subnetName=" + subnetName + "]";
	}


	@Override
	public String getInsertQuery() {
		String s;
		
		String key;
		
		key = this.nodeName + "#" + this.ip;

		s = String.format("insert into devices(device_key,device_ip,extended_sid,node_name,shelf,manuf,model,site_id,site_name,location,subnet_name,created_date,updated_date)"
				+ "values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s') on duplicate key update "
				+ "device_ip='%s',extended_sid='%s',node_name='%s',shelf='%s',manuf='%s',model='%s',site_id='%s',site_name='%s',location='%s',"
				+ "subnet_name='%s',updated_date='%s'", 
				key, this.ip,this.extendedSid,this.nodeName,this.shelf,this.manufacturer,this.model,this.siteId,this.siteName,this.location,
				this.subnetName,date,date,this.ip,this.extendedSid,this.nodeName,this.shelf,this.manufacturer,this.model,this.siteId,this.siteName,this.location,
				this.subnetName,date);
		
		return s;
	}
}
