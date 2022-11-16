package com.batch.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.batch.modal.StudentCsv;
import com.batch.modal.StudentJdbc;
import com.batch.modal.StudentJson;
import com.batch.modal.StudentResponse;
import com.batch.modal.StudentXml;

@Component
public class FirstItemWriter implements ItemWriter<StudentJdbc> {

	@Override
	public void write(List<? extends StudentJdbc> items) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Inside Item Writer");
		items.stream().forEach(System.out::println);
	}

}
