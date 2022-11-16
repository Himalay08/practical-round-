//package com.batch.controller;
//
//
//import java.util.List;
//
//import org.springframework.batch.core.launch.JobOperator;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.batch.request.JobParamsRequest;
//import com.batch.service.JobService;
//
//@RestController
//@RequestMapping("/api/job")
//public class JobController {
//
//	@Autowired
//	private JobService jobService;
//	
//	@Autowired
//	private JobOperator jobOperator;
//	
//	@GetMapping("/start/{jobName}")
//	public String startJob(@PathVariable("jobName") String jobName,@RequestBody List<JobParamsRequest> list) throws Exception{
//		this.jobService.startJob(jobName,list);
//		return "Job Started";
//	}
//	
//	@GetMapping("/stop/{jobExecutionId}")
//	public String stopJob(@PathVariable("jobExecutionId") long jobExecutionId) {
//		try {
//			jobOperator.stop(jobExecutionId);
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//		return "Job Stopped...";
//	}
//}
