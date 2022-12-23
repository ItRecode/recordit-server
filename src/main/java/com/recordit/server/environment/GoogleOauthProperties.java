package com.recordit.server.environment;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "oauth.google")
@RequiredArgsConstructor
public class GoogleOauthProperties {

	@NotBlank
	private final String clientId;
	@NotBlank
	private final String clientSecret;
	@NotBlank
	private final String redirectUrl;
	@NotBlank
	private final String tokenRequestUrl;
	@NotBlank
	private final String userInfoRequestUrl;

}
