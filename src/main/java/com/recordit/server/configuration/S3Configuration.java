package com.recordit.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.recordit.server.environment.S3Properties;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class S3Configuration {

	private final S3Properties s3Properties;

	@Bean
	public AmazonS3 amazonS3() {
		AWSCredentials awsCredentials = new BasicAWSCredentials(
				s3Properties.getCredentials().getAccessKey(),
				s3Properties.getCredentials().getSecretKey()
		);

		return AmazonS3ClientBuilder
				.standard()
				.withRegion(s3Properties.getRegion())
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
				.build();
	}
}
