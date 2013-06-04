package com.gto.aws.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.handlers.RequestHandler;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsyncClient;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.amazonaws.services.cloudwatch.model.ListMetricsRequest;
import com.basho.riak.client.RiakRetryFailedException;
import com.basho.riak.client.bucket.Bucket;
import com.basho.riak.client.cap.UnresolvedConflictException;
import com.basho.riak.client.convert.ConversionException;
import com.gto.aws.convertor.ConvertorFactory;
import com.gto.aws.model.Data;
import com.gto.aws.model.DataPoint;
import com.gto.aws.model.Ec2CpuUtilizationInstance;
import com.gto.aws.model.Ec2CpuUtilizationJob;
import com.gto.aws.model.HourlyData;
import com.gto.aws.model.JobConstants;
import com.gto.aws.model.JobRequestInstance;

public class CloudWatchService {
	@Autowired
	private AmazonCloudWatchAsyncClient cloudWatchClient;
	
	public AmazonCloudWatchAsyncClient getCloudWatchClient() {
		return cloudWatchClient;
	}

	public void setCloudWatchClient(AmazonCloudWatchAsyncClient cloudWatchClient) {
		this.cloudWatchClient = cloudWatchClient;
	}

	public void processJob(JobRequestInstance req){
		 System.out.println("inside process");
		Ec2CpuUtilizationJob job = req.getJob();
		String date = req.getDate();
		Collection<String> instances = job.getInstances();
		for (Iterator iterator = instances.iterator(); iterator.hasNext();) {
			String instance = (String) iterator.next();
			listMetrics(instance,date,req.getJobRequestId());
		}
		
		
	}
	public void listMetrics(String instance,String date,String jobId) {
		
		Ec2CpuUtilizationInstance inst = new Ec2CpuUtilizationInstance();
		inst.setJobId(jobId);
		inst.setDate(date);
		inst.setInstanceId(instance);
	
    	RequestHandler requestHandler = null;
		ListMetricsRequest listMetricsRequest=new ListMetricsRequest();
		Collection<Dimension> dimensionsList = new ArrayList<Dimension>();
		Dimension dimensionFilter = new Dimension();
		dimensionFilter.setName("InstanceId");
		dimensionFilter.setValue(instance);
		dimensionsList.add(dimensionFilter);
		GetMetricStatisticsRequest getMetricStatisticsRequest = new GetMetricStatisticsRequest();
		getMetricStatisticsRequest.setMetricName("CPUUtilization");
		
		
		// Start date
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMddyyyy");
		java.util.Calendar c = java.util.Calendar.getInstance();
		try {
			c.setTime(sdf.parse(date));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(int i=0;i<23;i++){
		Date startTime=c.getTime();
		c.add(Calendar.HOUR, 1);
		Date endTime=c.getTime();
		getMetricStatisticsRequest.setStartTime(startTime);
		getMetricStatisticsRequest.setEndTime(endTime);
		getMetricStatisticsRequest.setNamespace("AWS/EC2");
		Collection<String> statistics = new ArrayList<String>();
		statistics.add("Average");
		statistics.add("SampleCount");
		statistics.add("Minimum");
		statistics.add("Maximum");
		statistics.add("Sum");
	
		getMetricStatisticsRequest.setStatistics(statistics );
		getMetricStatisticsRequest.setPeriod(360);
		getMetricStatisticsRequest.setDimensions(dimensionsList);
		GetMetricStatisticsResult result = cloudWatchClient.getMetricStatistics(getMetricStatisticsRequest );
		Data data = new Data();
				for (Iterator<Datapoint> iterator =  result.getDatapoints().iterator(); iterator.hasNext();) {
				Datapoint datapoint =  iterator.next();
				data.getData().add(new DataPoint(datapoint));
			//System.out.println(datapoint.getAverage());
			
		}
			
				java.util.Calendar c2 = java.util.Calendar.getInstance();
				c2.setTime(startTime);
				data.setStartTime(String.valueOf(c2.get(Calendar.HOUR_OF_DAY)));
				c2.setTime(endTime);
				data.setEndTime(String.valueOf(c2.get(Calendar.HOUR_OF_DAY)));
			inst.getHourlyData().add(data);
			
		//	System.out.println("sdffsdf " +inst.getHourlyData().iterator().next().getData().size());
		}	
	/*int i =0;
		for (Iterator iterator = inst.getHourlyData().iterator(); iterator.hasNext();) {
			i++;
			Data data = (Data) iterator.next();
			Collection<DataPoint> t1 = data.getData();
			for (Iterator iterator2 = t1.iterator(); iterator2.hasNext();) {
				DataPoint dataPoint = (DataPoint) iterator2.next();
				System.out.println("dataPoint"+i);
				System.out.println("dataPoint time"+dataPoint.getTimestamp());
			}
			System.out.println();
			
		}*/
		//System.out.println("inserting size 8***"+inst.getHourlyData().iterator().next().getData().size());
		Bucket jobInstanceBucket = null;
		try {
			 jobInstanceBucket = RiakFactory.getRiakClient().fetchBucket( JobConstants.JOB_INSTANCES).execute();
		} catch (RiakRetryFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			jobInstanceBucket.store(inst).withConverter(ConvertorFactory.getJobInstanceConvertor()).execute();
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
		
	}

	private Date getPreviosDayStartTime() {
		// TODO Auto-generated method stub
		return null;
	}
}
