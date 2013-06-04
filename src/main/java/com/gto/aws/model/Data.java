package com.gto.aws.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;


public class Data implements Serializable{
	/**
	 * 
	 */

	
	String startTime;
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Collection<DataPoint> getData() {
		return data;
	}
	public void setData(Collection<DataPoint> data) {
		this.data = data;
	}
	String endTime;
	Collection<DataPoint> data = new ArrayList<DataPoint>();


}
