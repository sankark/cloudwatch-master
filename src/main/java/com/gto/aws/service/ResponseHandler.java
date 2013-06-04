package com.gto.aws.service;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.gto.aws.dao.DAOFactory;
import com.gto.aws.model.JobRequest;

public class ResponseHandler {

	private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);

	

	public void handleMessage(JobRequest responseInstance) {
		System.out.println("inside response");
       DAOFactory.getJobRequestDao().updateStatus(responseInstance.getJobRequestId(),responseInstance.getResponse());
		

	}

	

}