package com.gto.aws.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Address;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.gto.aws.model.JobRequest;
import com.gto.aws.model.User;

public class JobRequestSender {
	
	@Autowired
	RabbitTemplate jobRequestTemplate;
    /*public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/integration/Rabbit-Sender.xml");//loading beans
        RabbitTemplate  aTemplate = (RabbitTemplate ) context.getBean("workerTemplate");// getting a reference to the sender bean
       
		Collection<Dimension> dimensionsList = new ArrayList<Dimension>();
		Dimension dimensionFilter = new Dimension();
		dimensionFilter.setName("InstanceId");
		dimensionFilter.setValue("i-c30fa5a4");
		dimensionsList.add(dimensionFilter);
		GetMetricStatisticsRequest getMetricStatisticsRequest = new GetMetricStatisticsRequest();
		getMetricStatisticsRequest.setMetricName("CPUUtilization");
		Date startTime=new Date(System.currentTimeMillis()-1000000 * 5);
		Date endTime=new Date(System.currentTimeMillis());
		getMetricStatisticsRequest.setStartTime(startTime);
		getMetricStatisticsRequest.setEndTime(endTime);
		getMetricStatisticsRequest.setNamespace("AWS/EC2");
		Collection<String> statistics = new ArrayList<String>();
		statistics.add("Average");
		getMetricStatisticsRequest.setStatistics(statistics );
		getMetricStatisticsRequest.setPeriod(360);
		getMetricStatisticsRequest.setDimensions(dimensionsList);
		
        for (int i = 0; i < 10; i++){
        	System.out.println("sendind mesage");
        	User user = new User();
        	user.setLoginID("test");
            aTemplate.convertAndSend(getMetricStatisticsRequest, new MessagePostProcessor() {				
				public Message postProcessMessage(Message message) throws AmqpException {
					message.getMessageProperties().setReplyToAddress(new Address("direct://JOB-EXCHANGE/job.response.ec2"));
					return message;
				}
			});
        }
    }*/
	
	public void sendJobRequest(JobRequest request){
		jobRequestTemplate.convertAndSend(request, new MessagePostProcessor() {				
			public Message postProcessMessage(Message message) throws AmqpException {
				message.getMessageProperties().setReplyToAddress(new Address("direct://JOB-EXCHANGE/job.response.ec2"));
				return message;
			}
		});
	}
}