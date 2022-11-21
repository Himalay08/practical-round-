package com.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.batch.modal.StudentCsv;
import com.batch.modal.StudentJdbc;
import com.batch.modal.StudentJson;
import com.batch.mysql.entity.Student;

@Component
public class FirstItemProcessor implements ItemProcessor<com.batch.postgres.entity.Student,Student> {

	@Override
	public Student process(com.batch.postgres.entity.Student item) throws Exception {
		Student student = new Student();
		student.setId(item.getId());
		student.setFirstName(item.getFirstName());
		student.setLastName(item.getLastName());
		student.setEmail(item.getEmail());
		student.setDeptId(item.getDeptId());
		student.setIsActive(item.getIsActive()!=null?Boolean.valueOf(item.getIsActive()):false);
		return student;
	}

//	@Override
//	public StudentJson process(StudentCsv item) throws Exception {
////		System.out.println("Inside Item Processor");
//		if(item.getId()==6) {
//			System.out.println("JsonItemProcessor!!");
//			throw new NullPointerException();
//		}
//		StudentJson studentJson = new StudentJson();
//		studentJson.setEmail(item.getEmail());
//		studentJson.setId(item.getId());
//		studentJson.setLastName(item.getLastName());
//		studentJson.setFirstName(item.getFirstName());
//		
//		return studentJson;
//	}

}
