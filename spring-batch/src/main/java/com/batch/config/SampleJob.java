package com.batch.config;

import java.io.File; 
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.batch.core.Job; 
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.FlatFileParseException;
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
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.codec.xml.Jaxb2XmlDecoder;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.batch.listener.SkipListener;
import com.batch.listener.SkipListenerImpl;
import com.batch.modal.StudentCsv;
import com.batch.modal.StudentJdbc;
import com.batch.modal.StudentJson;
import com.batch.modal.StudentResponse;
import com.batch.modal.StudentXml;
import com.batch.postgres.entity.Student;
import com.batch.processor.FirstItemProcessor;
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
	
	@Autowired
	private FirstItemProcessor firstItemProcessor;

	@Autowired
	private FirstItemReader firstItemReader;
	
	@Autowired
	private FirstItemWriter firstItemWriter;
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private SkipListener skipListener;

	@Autowired
	private SkipListenerImpl skipListenerImpl;

	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;
	
	@Autowired
	@Qualifier("instagramdataSource")
	private DataSource instagramdataSource;
	
	@Autowired
	@Qualifier("postgresdataSource")
	private DataSource postDataSource;
	
	@Autowired
	@Qualifier("postgresEntityManagerFactory")
	private EntityManagerFactory postgresEntityManagerFactory;
	
	@Autowired
	@Qualifier("mysqlEntityManagerFactory")
	private EntityManagerFactory mysqlEntityManagerFactory;
	
	@Autowired
	private JpaTransactionManager jpaTransactionManager;
	
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
//				.<StudentCsv,StudentJson>chunk(3)
				.<Student,com.batch.mysql.entity.Student>chunk(3)
//				.reader(jdbcCursorItemReader())
//				.reader(flatFileItemReader(null))
				.reader(jpaCursorItemReader(null,null))
//				.reader(itemReaderAdapter())
//				.processor(firstItemProcessor)
				.processor(firstItemProcessor)
//				.writer(flatfileItemWriter(null))
//				.writer(firstItemWriter)
//				.writer(jsonFileItemWriter(null
//                 .writer(staxEventItemWriter(null))
//				.writer(jdbcBatchItemWriter())
//				.writer(jdbcBatchItemWriterPrepared())
//				.writer(jsonFileItemWriter(null))
				.writer(jpaItemWriter())
				.faultTolerant()
//				.skip(FlatFileParseException.class)
				.skip(Throwable.class)
//				.skipLimit(Integer.MAX_VALUE)
				.skipLimit(100)
//				.skipPolicy(new AlwaysSkipItemSkipPolicy())
//				.listener(skipListener)
				.retryLimit(3)
				.retry(Throwable.class)
				.listener(skipListenerImpl)
				.transactionManager(jpaTransactionManager)
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
	
	public JdbcCursorItemReader<StudentCsv> jdbcCursorItemReader(){
		JdbcCursorItemReader<StudentCsv> jdbcCursorItemReader = new JdbcCursorItemReader<StudentCsv>();
		jdbcCursorItemReader.setDataSource(instagramdataSource);
		jdbcCursorItemReader.setSql("select * from students");
		jdbcCursorItemReader.setRowMapper(new BeanPropertyRowMapper<StudentCsv>() {
			{
				setMappedClass(StudentCsv.class);
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
	
	public ItemWriterAdapter<StudentCsv> itemWriterAdapter(){
		ItemWriterAdapter<StudentCsv> itemWriterAdapter = new ItemWriterAdapter<StudentCsv>();
		itemWriterAdapter.setTargetObject(studentService);
		itemWriterAdapter.setTargetMethod("restCallToCreateStudent");
//		itemWriterAdapter.setArguments(new Object[] {1L,"test"});
	return itemWriterAdapter;
}
	
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
	public JsonFileItemWriter<StudentJson> jsonFileItemWriter(@Value("#{jobParameters['outputfile']}") FileSystemResource fileSystemResource){
		JsonFileItemWriter<StudentJson> jsonFileItemWriter = new JsonFileItemWriter<StudentJson>(fileSystemResource,new JacksonJsonObjectMarshaller<StudentJson>()) {
			@Override
			public String doWrite(List<? extends StudentJson> items) {
				items.stream().forEach((item)->{
					if(item.getId()==4) {
						System.out.println("JsonFileWriter!!");
						throw new NullPointerException();
					}
				});
				return super.doWrite(items);
			}
		};

	    	
		return jsonFileItemWriter;
	}
	
	@Bean
	@StepScope
	public StaxEventItemWriter<StudentJdbc> staxEventItemWriter(@Value("#{jobParameters['outputfile']}") FileSystemResource fileSystemResource){
		StaxEventItemWriter<StudentJdbc> staxEventItemWriter = new StaxEventItemWriter<StudentJdbc>();
		staxEventItemWriter.setResource(fileSystemResource);
		staxEventItemWriter.setRootTagName("students");
		staxEventItemWriter.setMarshaller(new Jaxb2Marshaller() {
			{
				setClassesToBeBound(StudentJdbc.class);
			}
		});
		return staxEventItemWriter;
	}
	
	@Bean
	public JdbcBatchItemWriter<StudentCsv> jdbcBatchItemWriter(){
		JdbcBatchItemWriter<StudentCsv> jdbcBatchItemWriter = new JdbcBatchItemWriter<StudentCsv>();
		jdbcBatchItemWriter.setDataSource(instagramdataSource);
		jdbcBatchItemWriter.setSql("insert into students(id,firstName,lastName,email)"+"values(:id,:firstName,:lastName,:email)");
		jdbcBatchItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<StudentCsv>());
	    return jdbcBatchItemWriter;
	}
	
	@Bean
	public JdbcBatchItemWriter<StudentCsv> jdbcBatchItemWriterPrepared(){
		JdbcBatchItemWriter<StudentCsv> jdbcBatchItemWriter = new JdbcBatchItemWriter<StudentCsv>();
		jdbcBatchItemWriter.setDataSource(instagramdataSource);
		jdbcBatchItemWriter.setSql("insert into students(id,firstName,lastName,email)"+"values(?,?,?,?)");
		jdbcBatchItemWriter.setItemPreparedStatementSetter(new ItemPreparedStatementSetter<StudentCsv>() {
			
			@Override
			public void setValues(StudentCsv item, PreparedStatement ps) throws SQLException {
				ps.setLong(1, item.getId());
				ps.setString(2,item.getFirstName());
				ps.setString(3,item.getLastName());
				ps.setString(4,item.getEmail());
				
			}
		});
	    return jdbcBatchItemWriter;
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
	@Bean
	@StepScope
	public JpaCursorItemReader<Student> jpaCursorItemReader(
			@Value("#{jobParameters['currentItemCount']}") Integer currentItemCount,
			@Value("#{jobParameters['maxItemCount']}") Integer maxItemCount
			){
		JpaCursorItemReader<Student> jpaCursorItemReader = new JpaCursorItemReader<Student>();
		jpaCursorItemReader.setEntityManagerFactory(postgresEntityManagerFactory);
		jpaCursorItemReader.setQueryString("from Student");
		jpaCursorItemReader.setCurrentItemCount(currentItemCount);
		jpaCursorItemReader.setMaxItemCount(maxItemCount);
		return jpaCursorItemReader;
	}
	
	public JpaItemWriter<com.batch.mysql.entity.Student> jpaItemWriter(){
		JpaItemWriter<com.batch.mysql.entity.Student> jpaItemWriter = new JpaItemWriter<com.batch.mysql.entity.Student>();
		jpaItemWriter.setEntityManagerFactory(mysqlEntityManagerFactory);
		return jpaItemWriter;
	}
}
