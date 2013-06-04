package com.gto.aws.model;

import com.gto.aws.dao.GenericDAO;
import com.gto.aws.dao.DAOFactory.IJobDetailsDAO;

public class JobConstants {

	public static class JobDetailsDAO extends GenericDAO<Ec2CpuUtilizationJob> implements IJobDetailsDAO {
		private static final String JOB_DETAILS = "job_details";
	
		public JobDetailsDAO(){
			super(JOB_DETAILS);
		}
	}
	public static final String DAILY = "daily";
	public static final String MONTHLY = "monthly";
	public static final String TEST_USER = "test_user";
	public static final String JOB_REQUESTS = "job_requests2";
	public static final String DATA_POINTS = "data_points";
	public static final String HOURLY_DATA = "hourly_data";
	public static final String JOB_INSTANCES = "job_instances";
	public static final String JOB_DEFINITION = "job_details";
	public static final String PENDING = "PENDING";

}
