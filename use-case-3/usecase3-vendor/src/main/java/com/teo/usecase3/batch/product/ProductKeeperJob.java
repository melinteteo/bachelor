package com.teo.usecase3.batch.product;

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

import com.teo.usecase3.entity.Product;

@Configuration
public class ProductKeeperJob extends JobExecutionListenerSupport {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductKeeperJob.class);
	
	@Bean
	@JobScope
	public ReaderProduct productReader(@Value("#{jobParameters['fileName']}") String fileName) {
		return new ReaderProduct(new ClassPathResource(fileName));
	}
	
	@Bean(name = "productJob")
	public Job vendorKeeperJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, 
			ProductProcessor processor, @Qualifier("writerProduct") ItemWriter<Product> writer) {
		Step step = stepBuilderFactory.get("step-1")
				.<Product, Product> chunk(2)
				.reader(productReader("will be injected"))
				.processor(processor)
				.writer(writer)
				.build();
		
		return jobBuilderFactory.get("product-job")
				.incrementer(new RunIdIncrementer())
				.listener(this)
				.start(step)
				.build();
	}
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			logger.info("All Products added successfully!");
		}
	}
}
