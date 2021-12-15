
package com.sify.network.alarms.ciena;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Event {

    @SerializedName("version")
    @Expose
    private Integer version;
    @SerializedName("seq-number")
    @Expose
    private Integer seqNumber;
    @SerializedName("timestamp")
    @Expose
    private Integer timestamp;
    @SerializedName("db-change")
    @Expose
    private DbChange dbChange;
    
    @SerializedName("deviceip")
    @Expose
    private String deviceip="";

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(Integer seqNumber) {
        this.seqNumber = seqNumber;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public DbChange getDbChange() {
        return dbChange;
    }

    public void setDbChange(DbChange dbChange) {
        this.dbChange = dbChange;
    }

	public String getDeviceip() {
		return deviceip;
	}

	public void setDeviceip(String deviceip) {
		this.deviceip = deviceip;
	}
    
	public String toJson() throws JsonGenerationException, JsonMappingException, IOException {
		String jsonStr="";
		
		ObjectMapper mapper = new ObjectMapper();
		
		//jsonStr = mapper.defaultPrettyPrintingWriter().writeValueAsString(this);
		jsonStr = mapper.writeValueAsString(this);
		
		return jsonStr;
	}

}
