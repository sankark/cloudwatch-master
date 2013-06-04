package com.gto.aws.model;

import java.io.Serializable;
import java.util.Collection;

import com.basho.riak.client.convert.RiakIndex;
import com.basho.riak.client.convert.RiakKey;

public class Ec2CpuUtilizationJob implements Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
@RiakKey String jobId;
public String getJobId() {
	return jobId;
}
public void setJobId(String jobId) {
	this.jobId = jobId;
}
@RiakIndex(name = "job-name") transient private String jobName;
@RiakIndex(name = "job-type") transient private String jobType;
@RiakIndex(name = "group-name") transient private String groupName;
Collection<String> instances ;

public Ec2CpuUtilizationJob()
{
	jobName = "Ec2CpuUtilizationJob";
	jobType = JobConstants.DAILY;
}
public String getJobName() {
	return jobName;
}
public void setJobName(String jobName) {
	this.jobName = jobName;
}
public String getJobType() {
	return jobType;
}
public void setJobType(String jobType) {
	this.jobType = jobType;
}
public String getGroupName() {
	return groupName;
}
public void setGroupName(String groupName) {
	this.groupName = groupName;
}
public Collection<String> getInstances() {
	return instances;
}
public void setInstances(Collection<String> instances) {
	this.instances = instances;
}

}
