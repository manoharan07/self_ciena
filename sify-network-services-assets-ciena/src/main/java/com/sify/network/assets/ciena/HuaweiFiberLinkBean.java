package com.sify.network.assets.ciena;

import com.sify.network.assets.core.api.bean.PerformanceInputBean;

public class HuaweiFiberLinkBean extends PerformanceInputBean {
	
	public String aEndSrcNodeName;
	public String aEndSrcPort;
	public String aEndSinkNodeName;
	public String aEndSinkPort;

	
	public String zEndSrcNodeName;
	public String zEndSrcPort;
	public String zEndSinkNodeName;
	public String zEndSinkPort;
	
	public String aEndTopologyDeviceId;
	public String aEndTopologyPort;
	public String zEndTopologyDeviceId;
	public String zEndTopologyPort;
	
	
	public String roadmName;
	public String linkName;
	public String designedOpticalLength;
	public String designedBOL;
	public String getaEndSrcNodeName() {
		return aEndSrcNodeName;
	}
	public void setaEndSrcNodeName(String aEndSrcNodeName) {
		this.aEndSrcNodeName = aEndSrcNodeName;
	}
	public String getaEndSrcPort() {
		return aEndSrcPort;
	}
	public void setaEndSrcPort(String aEndSrcPort) {
		this.aEndSrcPort = aEndSrcPort;
	}
	public String getaEndSinkNodeName() {
		return aEndSinkNodeName;
	}
	public void setaEndSinkNodeName(String aEndSinkNodeName) {
		this.aEndSinkNodeName = aEndSinkNodeName;
	}
	public String getaEndSinkPort() {
		return aEndSinkPort;
	}
	public void setaEndSinkPort(String aEndSinkPort) {
		this.aEndSinkPort = aEndSinkPort;
	}
	public String getzEndSrcNodeName() {
		return zEndSrcNodeName;
	}
	public void setzEndSrcNodeName(String zEndSrcNodeName) {
		this.zEndSrcNodeName = zEndSrcNodeName;
	}
	public String getzEndSrcPort() {
		return zEndSrcPort;
	}
	public void setzEndSrcPort(String zEndSrcPort) {
		this.zEndSrcPort = zEndSrcPort;
	}
	public String getzEndSinkNodeName() {
		return zEndSinkNodeName;
	}
	public void setzEndSinkNodeName(String zEndSinkNodeName) {
		this.zEndSinkNodeName = zEndSinkNodeName;
	}
	public String getzEndSinkPort() {
		return zEndSinkPort;
	}
	public void setzEndSinkPort(String zEndSinkPort) {
		this.zEndSinkPort = zEndSinkPort;
	}
	public String getaEndTopologyDeviceId() {
		return aEndTopologyDeviceId;
	}
	public void setaEndTopologyDeviceId(String aEndTopologyDeviceId) {
		this.aEndTopologyDeviceId = aEndTopologyDeviceId;
	}
	public String getaEndTopologyPort() {
		return aEndTopologyPort;
	}
	public void setaEndTopologyPort(String aEndTopologyPort) {
		this.aEndTopologyPort = aEndTopologyPort;
	}
	public String getzEndTopologyDeviceId() {
		return zEndTopologyDeviceId;
	}
	public void setzEndTopologyDeviceId(String zEndTopologyDeviceId) {
		this.zEndTopologyDeviceId = zEndTopologyDeviceId;
	}
	public String getzEndTopologyPort() {
		return zEndTopologyPort;
	}
	public void setzEndTopologyPort(String zEndTopologyPort) {
		this.zEndTopologyPort = zEndTopologyPort;
	}
	public String getRoadmName() {
		return roadmName;
	}
	public void setRoadmName(String roadmName) {
		this.roadmName = roadmName;
	}
	public String getLinkName() {
		return linkName;
	}
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	public String getDesignedOpticalLength() {
		return designedOpticalLength;
	}
	public void setDesignedOpticalLength(String designedOpticalLength) {
		this.designedOpticalLength = designedOpticalLength;
	}
	public String getDesignedBOL() {
		return designedBOL;
	}
	public void setDesignedBOL(String designedBOL) {
		this.designedBOL = designedBOL;
	}
	@Override
	public String toString() {
		return "HuaweiFiberLinkBean [aEndSrcNodeName=" + aEndSrcNodeName + ", aEndSrcPort=" + aEndSrcPort
				+ ", aEndSinkNodeName=" + aEndSinkNodeName + ", aEndSinkPort=" + aEndSinkPort + ", zEndSrcNodeName="
				+ zEndSrcNodeName + ", zEndSrcPort=" + zEndSrcPort + ", zEndSinkNodeName=" + zEndSinkNodeName
				+ ", zEndSinkPort=" + zEndSinkPort + ", aEndTopologyDeviceId=" + aEndTopologyDeviceId
				+ ", aEndTopologyPort=" + aEndTopologyPort + ", zEndTopologyDeviceId=" + zEndTopologyDeviceId
				+ ", zEndTopologyPort=" + zEndTopologyPort + ", roadmName=" + roadmName + ", linkName=" + linkName
				+ ", designedOpticalLength=" + designedOpticalLength + ", designedBOL=" + designedBOL + "]";
	}
	
	
}
