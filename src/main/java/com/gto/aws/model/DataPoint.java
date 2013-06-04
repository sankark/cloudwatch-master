package com.gto.aws.model;

import java.io.Serializable;
import java.util.Date;

import com.amazonaws.services.cloudwatch.model.Datapoint;

public class DataPoint implements Serializable {
    private Date timestamp;
    private Double sampleCount;
    private Double average;
    private Double sum;
    private Double minimum;
    private Double maximum;
    private String unit;
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public Double getSampleCount() {
		return sampleCount;
	}
	public void setSampleCount(Double sampleCount) {
		this.sampleCount = sampleCount;
	}
	public Double getAverage() {
		return average;
	}
	public void setAverage(Double average) {
		this.average = average;
	}
	public Double getSum() {
		return sum;
	}
	public void setSum(Double sum) {
		this.sum = sum;
	}
	public Double getMinimum() {
		return minimum;
	}
	public void setMinimum(Double minimum) {
		this.minimum = minimum;
	}
	public Double getMaximum() {
		return maximum;
	}
	public void setMaximum(Double maximum) {
		this.maximum = maximum;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public DataPoint(Datapoint datapoint){
		
		this.average = datapoint.getAverage();
		this.maximum = datapoint.getMaximum();
		this.minimum = datapoint.getMinimum();
		this.sampleCount = datapoint.getSampleCount();
		this.sum = datapoint.getSum();
		this.timestamp = datapoint.getTimestamp();
		this.unit = datapoint.getUnit();
	}
	

public DataPoint(){
	
}
}
