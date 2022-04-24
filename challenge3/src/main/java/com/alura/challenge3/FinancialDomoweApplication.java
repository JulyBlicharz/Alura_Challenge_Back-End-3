package com.alura.challenge3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class FinancialDomoweApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinancialDomoweApplication.class, args);
	}

}
