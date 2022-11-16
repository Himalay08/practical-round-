//package com.batch.service;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobParameter;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//import com.batch.request.JobParamsRequest;
//
//@Service
//public class JobService {
//	
//	@Autowired
//	private JobLauncher jobLauncher;
//	
//	@Qualifier("firstJob")
//	@Autowired
//	private Job firstJob;
//	
//	@Qualifier("secondJob")
//	@Autowired
//	private Job secondJob;
//
//	@Async
//	public void startJob(String jobName,List<JobParamsRequest> list) {
//
//		Map<String,JobParameter> params = new HashMap<>();
//		params.put("currentTime",new JobParameter(System.currentTimeMillis()));
//		
//		
//		list.stream().forEach(l->{
//			params.put(l.getParamKey(),new JobParameter(l.getParamValue()));
//		});
//		JobParameters jobParameters =new JobParameters(params);
//		
//		try {			
//			if(jobName.equals("First Job")) {
//				jobLauncher.run(firstJob, jobParameters);
//			}
//			else if(jobName.equals("Second Job")) {
//				jobLauncher.run(secondJob, jobParameters);	
//			}
//		}
//		catch(Exception  e) {
//			System.out.println(e.getMessage());
//		}
//		
//	}
//}
