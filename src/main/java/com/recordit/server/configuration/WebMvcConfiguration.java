package com.recordit.server.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.recordit.server.converter.LoginTypeConverter;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

	private final long MAX_AGE_SECS = 3000;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOriginPatterns("*")
				.allowedMethods("*")
				.allowedHeaders("*")
				.allowCredentials(true)
				.maxAge(MAX_AGE_SECS);
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new LoginTypeConverter());
	}
}
