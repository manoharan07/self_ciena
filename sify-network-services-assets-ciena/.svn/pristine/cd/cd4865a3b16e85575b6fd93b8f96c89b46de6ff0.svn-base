package com.sify.network.alarms.ciena;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Alarm {
	private Date createdTime;
	private Date clearedTime;

	public Alarm(String createdTime, String clearedTime) throws ParseException {
		this.setCreatedTime(getDate(createdTime));
		this.setClearedTime(getDate(clearedTime));
	}

	private Date getDate(String date) throws ParseException {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getClearedTime() {
		return clearedTime;
	}

	public void setClearedTime(Date clearedTime) {
		this.clearedTime = clearedTime;
	}

	public static int getIsloationEventCount(List<Alarm> primary, List<Alarm> secondary, List<Alarm> ternary) {
		int sum = getIsloationEvent(primary, secondary);
		int sum1 = getIsloationEvent(secondary, primary);
		return sum+sum1;
	}

	private static int getIsloationEvent(List<Alarm> primary, List<Alarm> secondary) {
		int sum = 0;
		for (Alarm palarm : primary) {
			int count = 0;
			for (Alarm salarm : secondary) {
				if (salarm.getCreatedTime().equals(palarm.getCreatedTime())
						|| (salarm.getCreatedTime().after(palarm.getCreatedTime())
								&& salarm.getCreatedTime().before(palarm.getClearedTime()))) {
					count++;
				}
			}
			if (count > 0) {
				sum = sum + count;
			}
		}
		return sum;
	}

	public static ArrayList<Alarm> getPrimaryAlarms() throws ParseException {
		ArrayList<Alarm> primary = new ArrayList<>();
		primary.add(new Alarm("2020-11-06 09:17:37", "2020-11-06 09:21:46"));
		primary.add(new Alarm("2020-11-06 09:26:23", "2020-11-06 09:26:35"));
		primary.add(new Alarm("2020-11-06 09:26:38", "2020-11-06 09:51:24"));
		primary.add(new Alarm("2020-11-06 09:51:24", "2020-11-06 09:51:34"));
		primary.add(new Alarm("2020-11-06 10:00:44", "2020-11-06 10:18:42"));
		return primary;
	}

	public static ArrayList<Alarm> getSecondaryAlarms() throws ParseException {
		ArrayList<Alarm> primary = new ArrayList<>();
		primary.add(new Alarm("2020-11-06 09:21:42", "2020-11-06 09:49:55"));
		primary.add(new Alarm("2020-11-06 10:00:43", "2020-11-06 10:07:32"));
		primary.add(new Alarm("2020-11-06 10:07:32", "2020-11-06 10:17:42"));
		primary.add(new Alarm("2020-11-06 10:18:19", "2020-11-06 10:22:56"));
		primary.add(new Alarm("2020-11-06 10:00:43", "2020-11-06 10:07:32"));
		return primary;
	}

	public static void main(String[] args) throws ParseException {

		List<Alarm> primary = getPrimaryAlarms();
		List<Alarm> secondary = getSecondaryAlarms();
		System.out.println(getIsloationEventCount(primary,secondary,null));
	}

}
