package com.srijan.user_group_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UserGroupServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserGroupServiceApplication.class, args);
	}

}
