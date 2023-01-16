package com.recordit.server.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CacheConfiguration {

	@Bean
	public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
		RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
				.disableCachingNullValues()
				.prefixCacheNameWith("cache:")
				.serializeKeysWith(
						RedisSerializationContext
								.SerializationPair
								.fromSerializer(new StringRedisSerializer())
				)
				.serializeValuesWith(
						RedisSerializationContext
								.SerializationPair
								.fromSerializer(new Jackson2JsonRedisSerializer<>(Object.class))
				);

		return RedisCacheManager.RedisCacheManagerBuilder
				.fromConnectionFactory(redisConnectionFactory)
				.cacheDefaults(configuration)
				.build();
	}
}