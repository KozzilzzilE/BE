package com.pocketco;

import com.pocketco.global.config.judge0.Judge0Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties(Judge0Properties.class)
public class PocketcoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PocketcoApplication.class, args);
	}

}