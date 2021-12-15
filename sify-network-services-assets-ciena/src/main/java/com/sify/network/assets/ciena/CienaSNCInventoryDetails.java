package com.sify.network.assets.ciena;


import com.sify.network.assets.core.api.bean.InventoryDeviceDetails;

public class CienaSNCInventoryDetails extends InventoryDeviceDetails {
	
	 private String sncKey;
	 private String ip;
	 private String aid;
	 private String label;
	 private String localEndPoint;
	 private String originDropSideProtState;
	 private String originNetSideProtState;
	 private String peerOrigin;
	 private String peerSnc;
	 private String protType;
	 private String remoteEndPoint;
	 private String remoteNode;
	 private String wvlGrid;
	 private String sncLineType;
	 private String terminDropSideProtState;
	 private String terminNetSideProtState;
	 private String type;
	 private String frequency;
	 private String dtlUuid;
	 private String pst;
	 private String cktid;
	 private String priority;
	 private String sncEpState;

	
	public CienaSNCInventoryDetails(String sncKey, String ip, String aid, String label,
			String localEndPoint, String originDropSideProtState,String originNetSideProtState,String peerOrigin,String peerSnc,String protType,String remoteEndPoint,String remoteNode,String wvlGrid,String sncLineType,String terminDropSideProtState,String terminNetSideProtState,String type,String frequency,String dtlUuid,String pst,String cktid,String priority,String sncEpState) {
		super(ip);
		this.sncKey = sncKey;
		this.aid = aid;
		this.label = label;
		this.localEndPoint = localEndPoint;
		this.originDropSideProtState = originDropSideProtState;
		this.originNetSideProtState = originNetSideProtState;
		this.peerOrigin = peerOrigin;
		this.peerSnc = peerSnc;
		this.protType = protType;
		this.remoteEndPoint = remoteEndPoint;
		this.remoteNode = remoteNode;
		this.wvlGrid = wvlGrid;
		this.sncLineType = sncLineType;
		this.terminDropSideProtState  =  terminDropSideProtState;
		this.terminNetSideProtState  =  terminNetSideProtState;
		this.type  =  type;
		this.frequency  =  frequency;
		this.dtlUuid  =  dtlUuid;
		this.pst  =  pst;
		this.cktid  =  cktid;
		this.priority  =  priority;
		this.sncEpState  =  sncEpState;
	}

	public String getSncKey() {
		return sncKey;
	}


	public void setSncKey(String sncKey) {
		this.sncKey = sncKey;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public String getAid() {
		return aid;
	}


	public void setAid(String aid) {
		this.aid = aid;
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public String getLocalEndPoint() {
		return localEndPoint;
	}


	public void setLocalEndPoint(String localEndPoint) {
		this.localEndPoint = localEndPoint;
	}


	public String getOriginDropSideProtState() {
		return originDropSideProtState;
	}


	public void setOriginDropSideProtState(String originDropSideProtState) {
		this.originDropSideProtState = originDropSideProtState;
	}


	public String getOriginNetSideProtState() {
		return originNetSideProtState;
	}


	public void setOriginNetSideProtState(String originNetSideProtState) {
		this.originNetSideProtState = originNetSideProtState;
	}


	public String getPeerOrigin() {
		return peerOrigin;
	}


	public void setPeerOrigin(String peerOrigin) {
		this.peerOrigin = peerOrigin;
	}


	public String getPeerSnc() {
		return peerSnc;
	}


	public void setPeerSnc(String peerSnc) {
		this.peerSnc = peerSnc;
	}


	public String getProtType() {
		return protType;
	}


	public void setProtType(String protType) {
		this.protType = protType;
	}


	public String getRemoteEndPoint() {
		return remoteEndPoint;
	}


	public void setRemoteEndPoint(String remoteEndPoint) {
		this.remoteEndPoint = remoteEndPoint;
	}


	public String getRemoteNode() {
		return remoteNode;
	}


	public void setRemoteNode(String remoteNode) {
		this.remoteNode = remoteNode;
	}


	public String getWvlGrid() {
		return wvlGrid;
	}


	public void setWvlGrid(String wvlGrid) {
		this.wvlGrid = wvlGrid;
	}


	public String getSncLineType() {
		return sncLineType;
	}


	public void setSncLineType(String sncLineType) {
		this.sncLineType = sncLineType;
	}


	public String getTerminDropSideProtState() {
		return terminDropSideProtState;
	}


	public void setTerminDropSideProtState(String terminDropSideProtState) {
		this.terminDropSideProtState = terminDropSideProtState;
	}


	public String getTerminNetSideProtState() {
		return terminNetSideProtState;
	}


	public void setTerminNetSideProtState(String terminNetSideProtState) {
		this.terminNetSideProtState = terminNetSideProtState;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getFrequency() {
		return frequency;
	}


	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}


	public String getDtlUuid() {
		return dtlUuid;
	}


	public void setDtlUuid(String dtlUuid) {
		this.dtlUuid = dtlUuid;
	}


	public String getPst() {
		return pst;
	}


	public void setPst(String pst) {
		this.pst = pst;
	}


	public String getCktid() {
		return cktid;
	}


	public void setCktid(String cktid) {
		this.cktid = cktid;
	}


	public String getPriority() {
		return priority;
	}


	public void setPriority(String priority) {
		this.priority = priority;
	}


	public String getSncEpState() {
		return sncEpState;
	}


	public void setSncEpState(String sncEpState) {
		this.sncEpState = sncEpState;
	}


	@Override
	public String toString() {
		return "CienaSNCInventoryDetails [sncKey=" + sncKey + ", ip=" + ip + ", aid=" + aid + ", label=" + label
				+ ", localEndPoint=" + localEndPoint + ", originDropSideProtState=" + originDropSideProtState
				+ ", originNetSideProtState=" + originNetSideProtState + ", peerOrigin=" + peerOrigin + ", peerSnc="
				+ peerSnc + ", protType=" + protType + ", remoteEndPoint=" + remoteEndPoint + ", remoteNode="
				+ remoteNode + ", wvlGrid=" + wvlGrid + ", sncLineType=" + sncLineType + ", terminDropSideProtState="
				+ terminDropSideProtState + ", terminNetSideProtState=" + terminNetSideProtState + ", type=" + type
				+ ", frequency=" + frequency + ", dtlUuid=" + dtlUuid + ", pst=" + pst + ", cktid=" + cktid
				+ ", priority=" + priority + ", sncEpState=" + sncEpState + "]";
	}

	@Override
	public String getInsertQuery() {
		// TODO Auto-generated method stub
		return null;
	}
}
