package com.contoso.socialapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.contoso.socialapp")
@EnableJpaRepositories(basePackages = "com.contoso.socialapp.repository")
@EntityScan(basePackages = "com.contoso.socialapp.entity")
public class SocialappApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialappApplication.class, args);
	}

}
