package com.srijan.api_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiServiceApplication.class, args);
	}
		
	//making bean for custom route locator to route requests to respective services
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
			.route("user-service", r -> r.path("/user/**")
				.uri("lb://USER-SERVICE"))
			.route("group-service", r -> r.path("/group/**")
				.uri("lb://GROUP-SERVICE"))
			.route("expense-service", r -> r.path("/expence/**")
				.uri("lb://EXPENCE-SERVICE"))
			.route("user-group-service", r -> r.path("/userGroup/**")
				.uri("lb://USER-GROUP-SERVICE"))
			.route("user-group-service-alt", r -> r.path("/user-group/**")
				.uri("lb://USER-GROUP-SERVICE"))
			.build();
	}
}
