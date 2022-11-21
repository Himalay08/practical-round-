package com.student;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController()
@RequestMapping("/students")
public class StudentBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentBatchApplication.class, args);
	}
	
	@GetMapping
	public List<StudentResponse> getStudents(){
		List<StudentResponse> list = new ArrayList<StudentResponse>();
		list.add(new StudentResponse(1L, "Krishana", "Koladiya", "krishna@gmail.com"));
		list.add(new StudentResponse(2L, "Ritika", "Viradiya", "ritika@gmail.com"));
		list.add(new StudentResponse(3L, "Sweta", "Vaja", "sweta@gmail.com"));
		list.add(new StudentResponse(4L, "Radhika", "Vaja", "radhika@gmail.com"));
		list.add(new StudentResponse(5L, "Avnik", "Chuahan", "avnik@gmail.com"));
		list.add(new StudentResponse(6L, "Krishana", "Koladiya", "krishna@gmail.com"));
		list.add(new StudentResponse(7L, "Krishana", "Koladiya", "krishna@gmail.com"));
		list.add(new StudentResponse(8L, "Krishana", "Koladiya", "krishna@gmail.com"));
		list.add(new StudentResponse(9L, "Krishana", "Koladiya", "krishna@gmail.com"));
		list.add(new StudentResponse(10L, "Krishana", "Koladiya", "krishna@gmail.com"));
		return list;
	}
	
	@PostMapping("/createStudent")
	public StudentResponse addStudent(@RequestBody StudentRequest s) {
		System.out.println("Student id: "+s.getId());
		return new StudentResponse(s.getId(),s.getFirstName(),s.getLastName(),s.getEmail());
	}

}
