package com.sunny.microservices.learning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class LearningServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningServiceApplication.class, args);
	}

}
