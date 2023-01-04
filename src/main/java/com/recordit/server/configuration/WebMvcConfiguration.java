package com.recordit.server.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.recordit.server.converter.LoginTypeConverter;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

	private final long MAX_AGE_SECS = 3000;

	private final String originPattern;

	public WebMvcConfiguration(
			@Value("${cors.origin}") String originPattern
	) {
		this.originPattern = originPattern;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOriginPatterns(originPattern)
				.allowedMethods("*")
				.allowedHeaders("*")
				.allowCredentials(true)
				.exposedHeaders("Set-Cookie")
				.maxAge(MAX_AGE_SECS);
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new LoginTypeConverter());
	}
}
