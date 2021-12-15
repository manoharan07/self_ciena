package com.sify.network.alarms.ciena;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CienaEvent implements Serializable {

	@SerializedName("notification")
	@Expose
	private Notification notification;
	private final static long serialVersionUID = 1519076781394830413L;

	public CienaEvent() {
	}

	/**
	 * 
	 * @param notification
	 */
	public CienaEvent(Notification notification) {
		this.notification = notification;
	}
	
	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

	@Override
	public String toString() {
		return "CiennaEvent [notification=" + notification + "]";
	}
}
