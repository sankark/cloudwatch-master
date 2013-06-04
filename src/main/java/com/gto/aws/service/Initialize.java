package com.gto.aws.service;

import org.springframework.beans.factory.InitializingBean;

import com.gto.aws.dao.DAOFactory;

public class Initialize implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
		DAOFactory.getEc2CpuUtilizationInstanceDao().clean();
		DAOFactory.getJobRequestDao().clean();

	}

}
