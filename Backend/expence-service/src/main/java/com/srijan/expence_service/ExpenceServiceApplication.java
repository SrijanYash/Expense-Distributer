package com.srijan.expence_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ExpenceServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenceServiceApplication.class, args);
	}

}
