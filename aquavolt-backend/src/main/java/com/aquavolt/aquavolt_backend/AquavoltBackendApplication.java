package com.aquavolt.aquavolt_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AquavoltBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AquavoltBackendApplication.class, args);
	}

}