package com.fast.projectboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class FastProjectBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(FastProjectBoardApplication.class, args);
	}

}
