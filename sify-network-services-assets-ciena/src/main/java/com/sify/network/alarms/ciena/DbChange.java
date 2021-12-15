
package com.sify.network.alarms.ciena;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DbChange {

    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
