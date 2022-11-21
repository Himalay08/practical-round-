package com.batch.listener;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

import com.batch.modal.StudentCsv;
import com.batch.modal.StudentJson;

@Component
public class SkipListener {

	@OnSkipInRead
	public void skipInRead(Throwable th) {
		if(th instanceof FlatFileParseException) {
			createFile("C:\\Users\\2101946\\Udemy\\spring-batch\\Second Job\\First Chunk Step\\reader\\SkipInRead.txt",
					((FlatFileParseException)th).getInput());
		}
	}
	
	@OnSkipInProcess
	public void skipInProcess(StudentCsv studentCsv,Throwable th) {
		createFile("C:\\Users\\2101946\\Udemy\\spring-batch\\Second Job\\First Chunk Step\\processor\\SkipInProcess.txt",
				studentCsv.toString());
	}
	
	@OnSkipInWrite
	public void skipInWriter(StudentJson studentJson,Throwable th) {
		System.out.println("Writer");
		createFile("C:\\Users\\2101946\\Udemy\\spring-batch\\Second Job\\First Chunk Step\\writer\\SkipInWriter.txt",
				studentJson.toString());
	}
	public void createFile(String filePath,String data) {
		try(FileWriter fileWriter = new FileWriter(new File(filePath),true)){
			fileWriter.write(data+"   "+ new Date()+"   " + "\n");
		}
		catch(Exception e) {
			
		}
	}
}
