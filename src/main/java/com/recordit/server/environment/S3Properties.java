package com.recordit.server.environment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "s3")
@RequiredArgsConstructor
public class S3Properties {

	@NotNull
	private final Credentials credentials;

	@NotBlank
	private final String bucket;

	@NotBlank
	private final String directory;

	@NotBlank
	private final String region;

	@Getter
	@Validated
	@RequiredArgsConstructor
	public static final class Credentials {
		@NotBlank
		private final String accessKey;

		@NotBlank
		private final String secretKey;
	}
}
