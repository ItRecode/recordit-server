package com.recordit.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
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

	@Bean
	public CookieSerializer cookieSerializer() {
		DefaultCookieSerializer serializer = new DefaultCookieSerializer();
		serializer.setUseSecureCookie(true);
		serializer.setSameSite("None");
		return serializer;
	}
}
