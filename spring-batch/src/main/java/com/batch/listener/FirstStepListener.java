//package com.batch.listener;
//
//import org.springframework.batch.core.ExitStatus;
//import org.springframework.batch.core.StepExecution;
//import org.springframework.batch.core.StepExecutionListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class FirstStepListener implements StepExecutionListener {
//
//	@Override
//	public void beforeStep(StepExecution stepExecution) {
//		System.out.println("Before Step:"+stepExecution.getStepName());
//		System.out.println("Job Params:"+stepExecution.getJobExecution().getJobParameters());
//		System.out.println("Step Exec Context:"+stepExecution.getExecutionContext());
//		stepExecution.getExecutionContext().put("Hi","Bye");
//	
//		
//	}
//
//	@Override
//	public ExitStatus afterStep(StepExecution stepExecution) {
//		System.out.println("After Step:"+stepExecution.getStepName());
//		System.out.println("Job Params:"+stepExecution.getJobExecution().getJobParameters());
//		System.out.println("Step Exec Context:"+stepExecution.getExecutionContext());
//	    
//		return null;
//	}
//
//}
