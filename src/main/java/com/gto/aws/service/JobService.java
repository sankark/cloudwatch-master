package com.gto.aws.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.basho.riak.client.IRiakObject;
import com.basho.riak.client.RiakException;
import com.basho.riak.client.RiakRetryFailedException;
import com.basho.riak.client.bucket.Bucket;
import com.basho.riak.client.cap.UnresolvedConflictException;
import com.basho.riak.client.convert.ConversionException;
import com.basho.riak.client.query.indexes.BinIndex;
import com.gto.aws.convertor.ConvertorFactory;
import com.gto.aws.dao.DAOFactory;
import com.gto.aws.model.Data;
import com.gto.aws.model.DataPoint;
import com.gto.aws.model.Ec2CpuUtilizationInstance;
import com.gto.aws.model.Ec2CpuUtilizationJob;
import com.gto.aws.model.JobConstants;
import com.gto.aws.model.JobRequest;
import com.gto.aws.model.JobRequestInstance;


public class JobService {
	
	@Autowired
	JobRequestSender jobRequestSender;
	
	public void createJob(Ec2CpuUtilizationJob job) {
		
		DAOFactory.getJobDetailsDao().put(job);
		// TODO Auto-generated method stub

	}
	
	public void executeJob(JobRequestInstance job) {
		
		DAOFactory.getJobRequestDao().put(job);
		JobRequest jobRequest= new JobRequest();
		jobRequest.setJobRequestId(job.getJobRequestId());
		jobRequest.setResponse("PENDING");
		jobRequestSender.sendJobRequest(jobRequest);
		// TODO Auto-generated method stub

	}
	
	public String putDayJobRequests(){
		
		
		
		Bucket jobDefinitionBucket=null;
		Bucket jobRequestBucket=null;
		try {
			jobDefinitionBucket = RiakFactory.getRiakClient().fetchBucket( JobConstants.JOB_DEFINITION).execute();
			jobRequestBucket = RiakFactory.getRiakClient().fetchBucket( JobConstants.JOB_REQUESTS).execute();
		} catch (RiakRetryFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			    
			try {
				List<String> engineers = jobDefinitionBucket.fetchIndex(BinIndex.named("job-type")).withValue(JobConstants.DAILY).execute();
				for (Iterator iterator = engineers.iterator(); iterator
						.hasNext();) {
					String jobId = (String) iterator.next();
					Ec2CpuUtilizationJob job = new Ec2CpuUtilizationJob();
					job.setJobId(jobId);
					job = jobDefinitionBucket.fetch(job).withConverter(ConvertorFactory.getJobConvertor()).execute();
					
					java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMddyyyy");
					java.util.Calendar c = java.util.Calendar.getInstance();
					c.add(Calendar.HOUR, -1);
					JobRequestInstance jobRequest = new JobRequestInstance();
					jobRequest.setDate(sdf.format(c.getTime()));
					jobRequest.setStatus(JobConstants.PENDING);
					jobRequest.setJobRequestId(UUID.randomUUID().toString());
					jobRequest.setJob(job);
					jobRequestBucket.store(jobRequest).withConverter(ConvertorFactory.getJobRequestConvertor()).execute();
					JobRequest request = new JobRequest();
					request.setJobRequestId(jobRequest.getJobRequestId());
					jobRequestSender.sendJobRequest(request);
					return jobRequest.getJobRequestId();
					
				}
			} catch (RiakException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		return null;
	}
	
	public static void main(String[] args) {
		
		 ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/integration/spring-integration-context.xml");//loading beans
		 JobService serv = (JobService) context.getBean("jobService");
	/*	Ec2CpuUtilizationJob job = new Ec2CpuUtilizationJob();
		job.setJobId("test");
		List<String> instances= new ArrayList<String>();
		instances.add("i-c30fa5a4");
		job.setInstances(instances);
		serv.createJob(job );
		*/
		/*JobRequestInstance jobRequestInstance = new JobRequestInstance();
		jobRequestInstance.setJobRequestId(UUID.randomUUID().toString());
		jobRequestInstance.setDate(new Date());
		jobRequestInstance.setJob(DAOFactory.getJobDetailsDao().get("test1"));
		serv.executeJob(jobRequestInstance);*/
		
		//System.out.println(DAOFactory.getJobRequestDao().get("c177a400-81ab-4b00-80b6-3d0a76fe3186").getDate().toString());
		
		Ec2CpuUtilizationJob job = new Ec2CpuUtilizationJob();
		job.setGroupName("test2");
		job.setJobId("test2");
		job.setJobName("test2");
		job.setJobType(JobConstants.DAILY);
		Collection<String> al = new ArrayList<String>();
		al.add("i-c30fa5a4");
		job.setInstances(al);
		Bucket bucket=null;
		try {
			bucket = RiakFactory.getRiakClient().fetchBucket( JobConstants.JOB_DEFINITION).execute();
		} catch (RiakRetryFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			bucket.store(job).withConverter(ConvertorFactory.getJobConvertor()).execute();
		} catch (RiakRetryFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnresolvedConflictException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConversionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		/*Ec2CpuUtilizationInstance instance  = new Ec2CpuUtilizationInstance();
		instance.setJobId("test");
		instance.setDate(new Date());
		instance.setInstanceId("test");
		Collection<Data> perDayData = new ArrayList<Data>();
		Collection<HourlyData> hourlyData = new ArrayList<HourlyData>();
		Data data = new Data();
		HourlyData sample = new HourlyData();
		sample.setTimestamp(new Date());
		hourlyData.add(sample);
		data.setData(hourlyData);
		perDayData.add(data);
		instance.setHourlyData(perDayData);
		DAOFactory.getEc2CpuUtilizationInstanceDao().put(instance);
		Ec2CpuUtilizationInstance res = DAOFactory.getEc2CpuUtilizationInstanceDao().get("test");
		ArrayList<Data> li = new ArrayList<Data>();
		li.addAll(res.getHourlyData());
		
		System.out.println(new ArrayList<HourlyData>(li.get(0).getData()).get(0).getTimestamp());*/
		
		String reqId = serv.putDayJobRequests();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
/*		
		Bucket jobRequestBucket=null;
    	try {
			jobRequestBucket = RiakFactory.getRiakClient().fetchBucket( JobConstants.JOB_REQUESTS).execute();
		} catch (RiakRetryFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	JobRequestInstance req = new JobRequestInstance();
    	req.setJobRequestId("36a5bb0d-1d7d-4c52-b63d-fe9f5d492795");
    	try {
			req = jobRequestBucket.fetch(req).withConverter(ConvertorFactory.getJobRequestConvertor()).execute();
		} catch (UnresolvedConflictException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RiakRetryFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConversionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		System.out.println(req.getJob());*/
		
	Bucket jobInstanceBucket = null;
		try {
			 jobInstanceBucket = RiakFactory.getRiakClient().fetchBucket( JobConstants.JOB_INSTANCES).execute();
		} catch (RiakRetryFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Ec2CpuUtilizationInstance inst = new Ec2CpuUtilizationInstance();
			inst.setJobId(reqId);
			
			 inst = jobInstanceBucket.fetch(inst).withConverter(ConvertorFactory.getJobInstanceConvertor()).execute();
			//System.out.println("^^^^^^^^^^^^^^^^"+inst.getHourlyData().iterator().next().getData().size());
			System.out.println(inst.getInstanceId());
			int i =0;
			for (Iterator iterator = inst.getHourlyData().iterator(); iterator.hasNext();) {
				i++;
				Data data = (Data) iterator.next();
				Collection<DataPoint> t1 = data.getData();
				for (Iterator iterator2 = t1.iterator(); iterator2.hasNext();) {
					DataPoint dataPoint = (DataPoint) iterator2.next();
					//System.out.println("dataPoint"+i);
					//System.out.println("dataPoint time"+dataPoint.getTimestamp());
				}
				System.out.println(new JSONObject(inst).toString());
				
			}
		} catch (RiakRetryFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnresolvedConflictException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConversionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		/*Bucket jobInstanceBucket = null;
		try {
			 jobInstanceBucket = RiakFactory.getRiakClient().fetchBucket( JobConstants.JOB_INSTANCES).execute();
		} catch (RiakRetryFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			try {
				Ec2CpuUtilizationInstance res  = new Ec2CpuUtilizationInstance();
				res.setJobId("1273cd65-0ae9-4a67-b779-e1495d6b8eb0");
				res = (Ec2CpuUtilizationInstance) jobInstanceBucket.fetch(res).withConverter(new Ec2CpuUtilizationInstanceConvertor(JobConstants.JOB_INSTANCES)).execute();
				System.out.println(res.getHourlyData().size());
			} catch (RiakRetryFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//jobInstanceBucket.store(inst).withConverter(new Ec2CpuUtilizationInstanceConvertor(JobConstants.JOB_INSTANCES)).execute();
		} catch (UnresolvedConflictException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConversionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
