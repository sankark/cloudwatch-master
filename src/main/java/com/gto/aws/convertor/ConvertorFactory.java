package com.gto.aws.convertor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.basho.riak.client.IRiakClient;
import com.basho.riak.client.convert.Converter;
import com.gto.aws.model.Ec2CpuUtilizationInstance;
import com.gto.aws.model.Ec2CpuUtilizationJob;
import com.gto.aws.model.JobConstants;
import com.gto.aws.model.JobRequestInstance;
import com.gto.aws.model.User;


public class ConvertorFactory {
 
	
	public interface IJobConvertor extends IGenericCryoConvertor<Ec2CpuUtilizationJob> {}
	public interface IJobRequestConvertor extends IGenericCryoConvertor<JobRequestInstance> {}
	
	public interface IJobInstanceConvertor extends IGenericCryoConvertor<Ec2CpuUtilizationInstance> {}
		
	
	// The use those interfaces as we declare entity-specific DAOs

	public static class JobConvertor extends GenericCryoConvertor<Ec2CpuUtilizationJob> implements IJobConvertor {
		public JobConvertor(){
			super(JobConstants.JOB_DEFINITION);
		}
	}
	
	public static class JobRequestConvertor extends GenericCryoConvertor<JobRequestInstance> implements IJobRequestConvertor {
		public JobRequestConvertor(){
			super(JobConstants.JOB_REQUESTS);
		}
	}
	
	public static class JobInstanceConvertor extends GenericCryoConvertor<Ec2CpuUtilizationInstance> implements IJobInstanceConvertor {
		public JobInstanceConvertor(){
			super(JobConstants.JOB_INSTANCES);
		}
	}
 
	// Static-only usage pattern
	protected ConvertorFactory() {}
	
	public static Converter<Ec2CpuUtilizationJob> getJobConvertor() {
		return new JobConvertor();
	}
	
	public static Converter<JobRequestInstance> getJobRequestConvertor() {
		return new JobRequestConvertor();
	}

	public static Converter<Ec2CpuUtilizationInstance> getJobInstanceConvertor() {
		return new JobInstanceConvertor();
	}
}