package com.teo.usecase3.batch.vendor;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.teo.usecase3.entity.Vendor;

@Configuration
public class VendorKeeperJob extends JobExecutionListenerSupport {
	
	private static final Logger logger = LoggerFactory.getLogger(VendorKeeperJob.class);
		
	@Bean
	@JobScope
	public ReaderVendor vendorReader(@Value("#{jobParameters['fileName']}") String fileName) {
		return new ReaderVendor(new ClassPathResource(fileName));
	}
	
	@Bean(name = "vendorJob")
	public Job vendorKeeperJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, 
			@Qualifier("writerVendor") ItemWriter<Vendor> writer) {
		Step step = stepBuilderFactory.get("step-1")
				.<Vendor, Vendor> chunk(2)
				.reader(vendorReader("will be injected"))
				.writer(writer)
				.build();
		
		return jobBuilderFactory.get("vendor-job")
				.incrementer(new RunIdIncrementer())
				.listener(this)
				.start(step)
				.build();
	}
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			logger.info("All Vendors added successfully!");
		}
	}
	
}
