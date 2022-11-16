package com.batch.config;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.batch.core.Job; 
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.codec.xml.Jaxb2XmlDecoder;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.batch.modal.StudentCsv;
import com.batch.modal.StudentJdbc;
import com.batch.modal.StudentJson;
import com.batch.modal.StudentResponse;
import com.batch.modal.StudentXml;
//import com.batch.listener.FirstJobListener;
//import com.batch.listener.FirstStepListener;
//import com.batch.processor.FirstItemProcessor;
import com.batch.reader.FirstItemReader;
import com.batch.service.StudentService;
import com.batch.service.TaskService;
import com.batch.writer.FirstItemWriter;
@Configuration
public class SampleJob {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private TaskService taskService;
	
//	@Autowired
//	private FirstJobListener firstJobListener;
//	
//	
//	@Autowired
//	private FirstStepListener firstStepListener;
	
//	@Autowired
//	private FirstItemProcessor firstItemProcessor;

	@Autowired
	private FirstItemReader firstItemReader;
	
	@Autowired
	private FirstItemWriter firstItemWriter;
	
	@Autowired
	private StudentService studentService;

	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean
	@ConfigurationProperties(prefix = "spring.instagramdatasource")
	public DataSource instagramdataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean
	public Job firstJob() {
	   return jobBuilderFactory.get("First Job")
			   .incrementer(new RunIdIncrementer())
	   .start(firstStep())
	   .next(secondStep())
//	   .listener(firstJobListener)
	   .build();
	}
	
	private Step firstStep() {
	   return stepBuilderFactory.get("First Step")
	   .tasklet(firstTask())
//	   .listener(firstStepListener)
	   .build();
	}
	
	private Tasklet firstTask() {
		return new Tasklet() {
		
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println("this is first tasklet step!");
				return RepeatStatus.FINISHED;
			}
		};
	}
	
	private Step secondStep() {
		   return stepBuilderFactory.get("Second Step")
		   .tasklet(taskService)
		   .build();
		}
		
	
	@Bean
	public Job secondJob() {
		return jobBuilderFactory.get("Second Job")
				.incrementer(new RunIdIncrementer())
				 .start(firstChunkStep())
				.build();
	}
	
	private Step firstChunkStep() {
		return stepBuilderFactory.get("First Chunk Step")
				.<StudentJdbc,StudentJdbc>chunk(1)
				.reader(jdbcCursorItemReader())
//				.reader(itemReaderAdapter())
//				.processor(firstItemProcessor)
//				.writer(flatfileItemWriter(null))
//				.writer(firstItemWriter)
				.writer(jsonFileItemWriter(null))
				.build();
	}
	
	@Bean
	@StepScope
	public FlatFileItemReader<StudentCsv> flatFileItemReader(@Value("#{jobParameters['inputfile']}") FileSystemResource fileSystemResource){
		FlatFileItemReader<StudentCsv> flatFileItemReader =new FlatFileItemReader<StudentCsv>();
		flatFileItemReader.setResource(fileSystemResource);
		flatFileItemReader.setLineMapper(new DefaultLineMapper<StudentCsv>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames("ID", "First Name", "Last Name", "Email");
				      	
					}
				});
				
				setFieldSetMapper(new BeanWrapperFieldSetMapper<StudentCsv>() {
					{
						setTargetType(StudentCsv.class);
					}
				});
			}
		});
		flatFileItemReader.setLinesToSkip(1);
		return flatFileItemReader;
	}
	
	@Bean
	@StepScope
	public JsonItemReader<StudentJson> jsonItemReader(@Value("#{jobParameters['inputfile']}") FileSystemResource fileSystemResource){
	   JsonItemReader<StudentJson> jsonItemReader = new JsonItemReader<StudentJson>();
	   jsonItemReader.setResource(fileSystemResource);
	   jsonItemReader.setJsonObjectReader(new JacksonJsonObjectReader<StudentJson>(StudentJson.class));
	   return jsonItemReader;
	}
	
	@Bean
	@StepScope
	public StaxEventItemReader<StudentXml> staxEventItemReader(@Value("#{jobParameters['inputfile']}") FileSystemResource fileSystemResource){
	  StaxEventItemReader<StudentXml> staxEventItemReader =new StaxEventItemReader<StudentXml>();
	  staxEventItemReader.setResource(fileSystemResource);
	  staxEventItemReader.setFragmentRootElementName("student");
	  staxEventItemReader.setUnmarshaller(new Jaxb2Marshaller() {
		  {
			  setClassesToBeBound(StudentXml.class);
		  }
	  });
	  return staxEventItemReader;
	}
	
	public JdbcCursorItemReader<StudentJdbc> jdbcCursorItemReader(){
		JdbcCursorItemReader<StudentJdbc> jdbcCursorItemReader = new JdbcCursorItemReader<StudentJdbc>();
		jdbcCursorItemReader.setDataSource(instagramdataSource());
		jdbcCursorItemReader.setSql("select * from students");
		jdbcCursorItemReader.setRowMapper(new BeanPropertyRowMapper<StudentJdbc>() {
			{
				setMappedClass(StudentJdbc.class);
			}
		});
		return jdbcCursorItemReader;
	}
	
	
//	public ItemReaderAdapter<StudentResponse> itemReaderAdapter(){
//		ItemReaderAdapter<StudentResponse> itemReaderAdapter = new ItemReaderAdapter<StudentResponse>();
//		itemReaderAdapter.setTargetObject(studentService);
//		itemReaderAdapter.setTargetMethod("getStudent");
//		itemReaderAdapter.setArguments(new Object[] {1L,"test"});
//		return itemReaderAdapter;
//	}
	
	@Bean
	@StepScope
	public FlatFileItemWriter<StudentJdbc> flatfileItemWriter(@Value("#{jobParameters['outputfile']}") FileSystemResource fileSystemResource){
		FlatFileItemWriter<StudentJdbc> flatFileItemWriter = new FlatFileItemWriter<StudentJdbc>();
		flatFileItemWriter.setResource(fileSystemResource);
		flatFileItemWriter.setHeaderCallback(new FlatFileHeaderCallback() {
			
			@Override
			public void writeHeader(Writer writer) throws IOException {
				writer.write("Id,First Name,Last Name,Email");
				
			}
		});
		flatFileItemWriter.setLineAggregator(new DelimitedLineAggregator<StudentJdbc>() {
			{
//				setDelimiter("|");
				setFieldExtractor(new BeanWrapperFieldExtractor<StudentJdbc>() {
					{
						setNames(new String[] {"id","firstName","lastName","email"});
					}
				});
			}
		});
		
		flatFileItemWriter.setFooterCallback(new FlatFileFooterCallback() {
			
			@Override
			public void writeFooter(Writer writer) throws IOException {
				writer.write("Created @ "+new Date());
				
			}
		});
		return flatFileItemWriter;
	}
	
	@Bean
	@StepScope
	public JsonFileItemWriter<StudentJdbc> jsonFileItemWriter(@Value("#{jobParameters['outputfile']}") FileSystemResource fileSystemResource){
		JsonFileItemWriter<StudentJdbc> jsonFileItemWriter = new JsonFileItemWriter<StudentJdbc>(fileSystemResource,new JacksonJsonObjectMarshaller<StudentJdbc>());
	    	
		return jsonFileItemWriter;
	}
	
//		private Tasklet secondTask() {
//			return new Tasklet() {
//			
//				@Override
//				public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//					System.out.println("this is second tasklet step!");
//					return RepeatStatus.FINISHED;
//				}
//			};
//		}
}
