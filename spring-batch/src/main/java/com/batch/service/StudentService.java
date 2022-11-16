package com.batch.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.batch.modal.StudentResponse;

@Service
public class StudentService {
	List<StudentResponse> list;
	public List<StudentResponse> restCallToGetStudents(){
		RestTemplate restTemplate = new RestTemplate();
		StudentResponse[] studentResponse= restTemplate.getForObject("http://localhost:8082/students", StudentResponse[].class);
		list = new ArrayList<StudentResponse>();
		for(StudentResponse s: studentResponse) {
			list.add(s);
		}
		return list;
	}
	
	public StudentResponse getStudent(Long id,String name) {
		System.out.println("id="+id+" "+"name="+name);
		if(list==null) {
			restCallToGetStudents();
		}
		if(list!=null && !list.isEmpty()) {
			return list.remove(0);
		}
		return null;
	}
}
