package com.gto.aws.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.basho.riak.client.convert.RiakIndex;
import com.basho.riak.client.convert.RiakKey;

public class Ec2CpuUtilizationInstance implements Serializable{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
@RiakKey private String jobId;
@RiakIndex(name = "instance") transient private String instanceId;
@RiakIndex(name = "date") transient private String date;
private Collection<Data> hourlyData = new ArrayList<Data>();

public String getJobId() {
	return jobId;
}

public void setJobId(String jobId) {
	this.jobId = jobId;
}

public String getInstanceId() {
	return instanceId;
}

public void setInstanceId(String instanceId) {
	this.instanceId = instanceId;
}

public String getDate() {
	
	return date;
}

public void setDate(String date) {
	this.date = date;
		

	
}



public Collection<Data> getHourlyData() {
	return hourlyData;
}

public void setHourlyData(Collection<Data> hourlyData) {
	this.hourlyData = hourlyData;
}

public Ec2CpuUtilizationInstance()
{

}


}
