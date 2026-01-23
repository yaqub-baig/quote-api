package com.autoadda.apis.quote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@EnableDiscoveryClient
@EnableScheduling
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class QuoteServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run( QuoteServiceApplication.class, args);
	}
}