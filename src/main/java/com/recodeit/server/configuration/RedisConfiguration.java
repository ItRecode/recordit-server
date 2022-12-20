package com.recodeit.server.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisConfiguration {

	private final String host;
	private final int port;
	private final String password;

	public RedisConfiguration(
		@Value("${spring.redis.host}") String host,
		@Value("${spring.redis.port}") int port,
		@Value("${spring.redis.password}") String password
	) {
		this.host = host;
		this.port = port;
		this.password = password;
	}

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
		configuration.setHostName(host);
		configuration.setPort(port);
		configuration.setPassword(password);
		return new LettuceConnectionFactory(configuration);
	}

}
