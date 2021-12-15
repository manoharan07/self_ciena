
package com.sify.network.alarms.ciena;

import java.io.IOException;
import java.io.Serializable;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification implements Serializable
{

    @SerializedName("atag-seq")
    @Expose
    private String atagSeq="";
    @SerializedName("msg-type")
    @Expose
    private String msgType="";
    @SerializedName("sub-type")
    @Expose
    private String subType="";
    @SerializedName("date")
    @Expose
    private String date="";
    @SerializedName("time")
    @Expose
    private String time="";
    @SerializedName("path")
    @Expose
    private String path="";
    @SerializedName("aid")
    @Expose
    private String aid="";
    @SerializedName("year")
    @Expose
    private int year=0;
    @SerializedName("dgn-type")
    @Expose
    private String dgnType="";
    @SerializedName("mode")
    @Expose
    private String mode="";
    @SerializedName("condtype")
    @Expose
    private String condtype="";
    @SerializedName("condeff")
    @Expose
    private String condeff="";
    @SerializedName("locn")
    @Expose
    private String locn="";
    @SerializedName("dirn")
    @Expose
    private String dirn="";
    @SerializedName("status")
    @Expose
    private String status="";
    @SerializedName("ntfcncde")
    @Expose
    private String ntfcncde="";
    @SerializedName("conddescr")
    @Expose
    private String conddescr="";
    @SerializedName("subnet-name")
    @Expose
    private String subnetName="";
    @SerializedName("card-type")
    @Expose
    private String cardType="";
    @SerializedName("srveff")
    @Expose
    private String serviceAffected="";
    @SerializedName("clfi")
    @Expose
    private String clfi="";
    @SerializedName("wavelength")
    @Expose
    private String wavelength="";
    @SerializedName("deviceip")
    @Expose
    private String deviceip="";
    
    private final static long serialVersionUID = -8516637692041748009L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Notification() {
    }

    /**
     * 
     * @param date
     * @param msgType
     * @param year
     * @param condtype
     * @param condeff
     * @param dgnType
     * @param dirn
     * @param mode
     * @param path
     * @param atagSeq
     * @param subType
     * @param time
     * @param locn
     * @param aid
     */
    public Notification(String atagSeq, String msgType, String subType, String date, String time, String path, String aid, int year, String dgnType, String mode, String condtype, String condeff, String locn, String dirn) {
        super();
        this.atagSeq = atagSeq;
        this.msgType = msgType;
        this.subType = subType;
        this.date = date;
        this.time = time;
        this.path = path;
        this.aid = aid;
        this.year = year;
        this.dgnType = dgnType;
        this.mode = mode;
        this.condtype = condtype;
        this.condeff = condeff;
        this.locn = locn;
        this.dirn = dirn;
    }

    public String getAtagSeq() {
        return atagSeq;
    }

    public void setAtagSeq(String atagSeq) {
        this.atagSeq = atagSeq;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDgnType() {
        return dgnType;
    }

    public void setDgnType(String dgnType) {
        this.dgnType = dgnType;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getCondtype() {
        return condtype;
    }

    public void setCondtype(String condtype) {
        this.condtype = condtype;
    }

    public String getCondeff() {
        return condeff;
    }

    public void setCondeff(String condeff) {
        this.condeff = condeff;
    }

    public String getLocn() {
        return locn;
    }

    public void setLocn(String locn) {
        this.locn = locn;
    }

    public String getDirn() {
        return dirn;
    }

    public void setDirn(String dirn) {
        this.dirn = dirn;
    }
    
    

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNtfcncde() {
		return ntfcncde;
	}

	public void setNtfcncde(String ntfcncde) {
		this.ntfcncde = ntfcncde;
	}

	public String getConddescr() {
		return conddescr;
	}

	public void setConddescr(String conddescr) {
		this.conddescr = conddescr;
	}

	public String getSubnetName() {
		return subnetName;
	}

	public void setSubnetName(String subnetName) {
		this.subnetName = subnetName;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getServiceAffected() {
		return serviceAffected;
	}

	public void setServiceAffected(String serviceAffected) {
		this.serviceAffected = serviceAffected;
	}
	
	

	public String getClfi() {
		return clfi;
	}

	public void setClfi(String clfi) {
		this.clfi = clfi;
	}

	
	public String getWavelength() {
		return wavelength;
	}

	public void setWavelength(String wavelength) {
		this.wavelength = wavelength;
	}

	public String getDeviceip() {
		return deviceip;
	}

	public void setDeviceip(String deviceip) {
		this.deviceip = deviceip;
	}
		

	
	
	@Override
	public String toString() {
		return "Notification [atagSeq=" + atagSeq + ", msgType=" + msgType + ", subType=" + subType + ", date=" + date
				+ ", time=" + time + ", path=" + path + ", aid=" + aid + ", year=" + year + ", dgnType=" + dgnType
				+ ", mode=" + mode + ", condtype=" + condtype + ", condeff=" + condeff + ", locn=" + locn + ", dirn="
				+ dirn + ", status=" + status + ", ntfcncde=" + ntfcncde + ", conddescr=" + conddescr + ", subnetName="
				+ subnetName + ", cardType=" + cardType + ", serviceAffected=" + serviceAffected + ", clfi=" + clfi
				+ ", wavelength=" + wavelength + ", deviceip=" + deviceip + "]";
	}

	public String toJson() throws JsonGenerationException, JsonMappingException, IOException {
		String jsonStr="";
		
		ObjectMapper mapper = new ObjectMapper();
		
		//jsonStr = mapper.defaultPrettyPrintingWriter().writeValueAsString(this);
		jsonStr = mapper.writeValueAsString(this);
		
		return jsonStr;
	}
}
