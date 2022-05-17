package com.teo.usecase3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class Usecase3UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(Usecase3UserApplication.class, args);
	}

}
