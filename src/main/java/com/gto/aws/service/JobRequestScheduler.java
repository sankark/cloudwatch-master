package com.gto.aws.service;
import org.quartz.*;
import org.springframework.beans.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class JobRequestScheduler extends QuartzJobBean {
	JobService jobService;
  public JobService getJobService() {
		return jobService;
	}

	public void setJobService(JobService jobService) {
		this.jobService = jobService;
	}

private int timeout;
  
  /**
   * Setter called after the ExampleJob is instantiated
   * with the value from the JobDetailBean (5)
   */ 
  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }

@Override
protected void executeInternal(JobExecutionContext arg0)
		throws JobExecutionException {
	// TODO Auto-generated method stub
	jobService.putDayJobRequests();
}
  
 
}