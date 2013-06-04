package com.gto.aws.model;

import java.io.Serializable;
import java.util.Date;

import com.basho.riak.client.convert.RiakIndex;
import com.basho.riak.client.convert.RiakKey;

public class JobRequestInstance implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
@RiakKey String jobRequestId;
public String getJobRequestId() {
	return jobRequestId;
}
public void setJobRequestId(String jobRequestId) {
	this.jobRequestId = jobRequestId;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public String getDate() {
	return dateentry;
}
public void setDate(String date) {
	this.dateentry =    date;
}
public Ec2CpuUtilizationJob getJob() {
	return job;
}
public void setJob(Ec2CpuUtilizationJob job) {
	this.job = job;
}

public JobRequestInstance()
{
	
}
@RiakIndex(name="status") transient private String status;
@RiakIndex(name="dateentry") transient private String dateentry;
Ec2CpuUtilizationJob job;
}
