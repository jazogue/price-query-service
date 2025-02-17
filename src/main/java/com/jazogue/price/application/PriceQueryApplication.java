package com.jazogue.price.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.jazogue.price")
@EnableJpaRepositories(basePackages = "com.jazogue.price.domain.repository")
@EntityScan(basePackages = "com.jazogue.price.domain.model")
public class PriceQueryApplication {

	public static void main(String[] args) {
		SpringApplication.run(PriceQueryApplication.class, args);
	}

}
