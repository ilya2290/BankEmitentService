package com.example.BankEmitentService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BankEmitentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankEmitentServiceApplication.class, args);
	}

}

