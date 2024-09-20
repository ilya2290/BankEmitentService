/**
 * <==================================>
 * Copyright (c) 2024 Ilya Sukhina.*
 * <=================================>
 */

package com.example.BankEmitentService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The main class of the BankEmitentService application.
 **/
 @SpringBootApplication
@EnableScheduling
public class BankEmitentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankEmitentServiceApplication.class, args);
	}

}

