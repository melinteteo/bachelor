package com.teo.usecase3;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EnableBatchProcessing(modular=true)
public class Usecase3VendorApplication {

	public static void main(String[] args) {
		SpringApplication.run(Usecase3VendorApplication.class, args);
	}

}
