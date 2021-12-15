package com.sify.network.assets.ciena;

import java.sql.Timestamp;

import com.sify.network.assets.core.api.bean.InventoryDeviceDetails;

public class CienaSNCInventory extends InventoryDeviceDetails {

	private String sncKey;
	private String aEndDeviceIp="";
	private String aEndNodeName="";
	private String aEndPoint="";
	private String aEndSncId="";
	private String aEndEpState="";
	private String zEndDeviceIp="";
	private String zEndNodeName="";
	private String zEndPoint="";
	private String zEndSncId="";
	private String zEndEpState="";
	private String label="";
	private String sncLineType="";
	private String frequency="";
	private String cktid="";
	private Timestamp date;
	
	public CienaSNCInventory(String ip) {
		super(ip);
	}

	public CienaSNCInventory(String sncKey, String ip, String aEndDeviceIp, String aEndNodeName,
			String aEndPoint, String aEndSncId, String aEndEpState, String zEndDeviceIp, String zEndNodeName,
			String zEndPoint, String zEndSncId, String zEndEpState, String label, String sncLineType, String frequency,
			String cktid,Timestamp date) {
		super(ip);
		this.sncKey = sncKey;
		this.aEndDeviceIp = aEndDeviceIp;
		this.aEndNodeName = aEndNodeName;
		this.aEndPoint = aEndPoint;
		this.aEndSncId = aEndSncId;
		this.aEndEpState = aEndEpState;
		this.zEndDeviceIp = zEndDeviceIp;
		this.zEndNodeName = zEndNodeName;
		this.zEndPoint = zEndPoint;
		this.zEndSncId = zEndSncId;
		this.zEndEpState = zEndEpState;
		this.label = label;
		this.sncLineType = sncLineType;
		this.frequency = frequency;
		this.cktid = cktid;
		this.date=date;
	}

	public String getSncKey() {
		return sncKey;
	}

	public void setSncKey(String sncKey) {
		this.sncKey = sncKey;
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

	public String getaEndSncId() {
		return aEndSncId;
	}

	public void setaEndSncId(String aEndSncId) {
		this.aEndSncId = aEndSncId;
	}

	public String getaEndEpState() {
		return aEndEpState;
	}

	public void setaEndEpState(String aEndEpState) {
		this.aEndEpState = aEndEpState;
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

	public String getzEndSncId() {
		return zEndSncId;
	}

	public void setzEndSncId(String zEndSncId) {
		this.zEndSncId = zEndSncId;
	}

	public String getzEndEpState() {
		return zEndEpState;
	}

	public void setzEndEpState(String zEndEpState) {
		this.zEndEpState = zEndEpState;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getSncLineType() {
		return sncLineType;
	}

	public void setSncLineType(String sncLineType) {
		this.sncLineType = sncLineType;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getCktid() {
		return cktid;
	}

	public void setCktid(String cktid) {
		this.cktid = cktid;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "CienaSNCInventoryDetails [sncKey=" + sncKey + ", aEndDeviceIp=" + aEndDeviceIp + ", aEndNodeName="
				+ aEndNodeName + ", aEndPoint=" + aEndPoint + ", aEndSncId=" + aEndSncId + ", aEndEpState="
				+ aEndEpState + ", zEndDeviceIp=" + zEndDeviceIp + ", zEndNodeName=" + zEndNodeName + ", zEndPoint="
				+ zEndPoint + ", zEndSncId=" + zEndSncId + ", zEndEpState=" + zEndEpState + ", label=" + label
				+ ", sncLineType=" + sncLineType + ", frequency=" + frequency + ", cktid=" + cktid + ", date=" + date
				+ "]";
	}

	@Override
	public String getInsertQuery() {

		String s="";
		
		if (!sncLineType.isEmpty()) {
		
		if (aEndDeviceIp.isEmpty()) {
			//skip aend parameters when updating.
			s = String.format("insert into sncs(snc_key,aend_device_ip,aend_node_name,aend_endpoint,aend_snc_id,aend_ep_state,zend_device_ip,zend_node_name,zend_endpoint,zend_snc_id,zend_ep_state,label,snc_line_type,frequency,ckt_id,created_date,updated_date)"
					+ "values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s') on duplicate key update "
					+ "snc_key='%s',zend_device_ip='%s',zend_node_name='%s',zend_endpoint='%s',zend_snc_id='%s',zend_ep_state='%s',label='%s',snc_line_type='%s',frequency='%s',ckt_id='%s',updated_date='%s'", 
					this.sncKey,this.aEndDeviceIp,this.aEndNodeName,this.aEndPoint,this.aEndSncId,this.aEndEpState,this.zEndDeviceIp,this.zEndNodeName,this.zEndPoint,this.zEndSncId,this.zEndEpState,this.label,this.sncLineType,this.frequency,this.cktid,date,date,
					this.sncKey,this.zEndDeviceIp,this.zEndNodeName,this.zEndPoint,this.zEndSncId,this.zEndEpState,this.label,this.sncLineType,this.frequency,this.cktid,date);
		} else {
			//skip zend parameters when updating.
			s = String.format("insert into sncs(snc_key,aend_device_ip,aend_node_name,aend_endpoint,aend_snc_id,aend_ep_state,zend_device_ip,zend_node_name,zend_endpoint,zend_snc_id,zend_ep_state,label,snc_line_type,frequency,ckt_id,created_date,updated_date)"
					+ "values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s') on duplicate key update "
					+ "snc_key='%s',aend_device_ip='%s',aend_node_name='%s',aend_endpoint='%s',aend_snc_id='%s',aend_ep_state='%s',label='%s',snc_line_type='%s',frequency='%s',ckt_id='%s',updated_date='%s'", 
					this.sncKey,this.aEndDeviceIp,this.aEndNodeName,this.aEndPoint,this.aEndSncId,this.aEndEpState,this.zEndDeviceIp,this.zEndNodeName,this.zEndPoint,this.zEndSncId,this.zEndEpState,this.label,this.sncLineType,this.frequency,this.cktid,date,date,
					this.sncKey,this.aEndDeviceIp,this.aEndNodeName,this.aEndPoint,this.aEndSncId,this.aEndEpState,this.label,this.sncLineType,this.frequency,this.cktid,date);
		}
		} else {
			

			if (aEndDeviceIp.isEmpty()) {
				//skip aend parameters when updating.
				s = String.format("insert into sncs(snc_key,aend_device_ip,aend_node_name,aend_endpoint,aend_snc_id,aend_ep_state,zend_device_ip,zend_node_name,zend_endpoint,zend_snc_id,zend_ep_state,label,snc_line_type,frequency,ckt_id,created_date,updated_date)"
						+ "values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s') on duplicate key update "
						+ "snc_key='%s',zend_device_ip='%s',zend_node_name='%s',zend_endpoint='%s',zend_snc_id='%s',zend_ep_state='%s',label='%s',frequency='%s',ckt_id='%s',updated_date='%s'", 
						this.sncKey,this.aEndDeviceIp,this.aEndNodeName,this.aEndPoint,this.aEndSncId,this.aEndEpState,this.zEndDeviceIp,this.zEndNodeName,this.zEndPoint,this.zEndSncId,this.zEndEpState,this.label,this.sncLineType,this.frequency,this.cktid,date,date,
						this.sncKey,this.zEndDeviceIp,this.zEndNodeName,this.zEndPoint,this.zEndSncId,this.zEndEpState,this.label,this.frequency,this.cktid,date);
			} else {
				//skip zend parameters when updating.
				s = String.format("insert into sncs(snc_key,aend_device_ip,aend_node_name,aend_endpoint,aend_snc_id,aend_ep_state,zend_device_ip,zend_node_name,zend_endpoint,zend_snc_id,zend_ep_state,label,snc_line_type,frequency,ckt_id,created_date,updated_date)"
						+ "values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s') on duplicate key update "
						+ "snc_key='%s',aend_device_ip='%s',aend_node_name='%s',aend_endpoint='%s',aend_snc_id='%s',aend_ep_state='%s',label='%s',frequency='%s',ckt_id='%s',updated_date='%s'", 
						this.sncKey,this.aEndDeviceIp,this.aEndNodeName,this.aEndPoint,this.aEndSncId,this.aEndEpState,this.zEndDeviceIp,this.zEndNodeName,this.zEndPoint,this.zEndSncId,this.zEndEpState,this.label,this.sncLineType,this.frequency,this.cktid,date,date,
						this.sncKey,this.aEndDeviceIp,this.aEndNodeName,this.aEndPoint,this.aEndSncId,this.aEndEpState,this.label,this.frequency,this.cktid,date);
			}
			
		}
		
	/*	s = String.format("insert into sncs(snc_key,aend_device_ip,aend_node_name,aend_endpoint,aend_snc_id,aend_ep_state,zend_device_ip,zend_node_name,zend_endpoint,zend_snc_id,zend_ep_state,label,snc_line_type,frequency,ckt_id,created_date,updated_date)"
				+ "values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s') on duplicate key update "
				+ "snc_key='%s',aend_device_ip='%s',aend_node_name='%s',aend_endpoint='%s',aend_snc_id='%s',aend_ep_state='%s',zend_device_ip='%s',zend_node_name='%s',zend_endpoint='%s',zend_snc_id='%s',zend_ep_state='%s',label='%s',snc_line_type='%s',frequency='%s',ckt_id='%s',updated_date='%s'", 
				this.sncKey,this.aEndDeviceIp,this.aEndNodeName,this.aEndPoint,this.aEndSncId,this.aEndEpState,this.zEndDeviceIp,this.zEndNodeName,this.zEndPoint,this.zEndSncId,this.zEndEpState,this.label,this.sncLineType,this.frequency,this.cktid,date,date,this.sncKey,this.aEndDeviceIp,this.aEndNodeName,this.aEndPoint,this.aEndSncId,this.aEndEpState,this.zEndDeviceIp,this.zEndNodeName,this.zEndPoint,this.zEndSncId,this.zEndEpState,this.label,this.sncLineType,this.frequency,this.cktid,date);*/
		return s;
		}
}
