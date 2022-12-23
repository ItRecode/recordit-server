package com.recordit.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan
public class RecordItServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecordItServerApplication.class, args);
	}

}
