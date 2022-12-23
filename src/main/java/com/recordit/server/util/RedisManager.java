package com.recordit.server.util;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recordit.server.exception.redis.RedisParsingException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisManager {

	private static final String REDIS_PARSING_ERROR_MESSAGE = "Redis에서 Json을 파싱할 때 에러가 발생했습니다.";
	private final StringRedisTemplate stringRedisTemplate;
	private final ObjectMapper objectMapper;

	public void set(@NonNull String key, @NonNull Object value) {
		try {
			stringRedisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(value));
		} catch (JsonProcessingException e) {
			throw new RedisParsingException(REDIS_PARSING_ERROR_MESSAGE);
		}
	}

	public void set(@NonNull String key, @NonNull Object value, long timeout, @NonNull TimeUnit timeUnit) {
		try {
			stringRedisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(value), timeout, timeUnit);
		} catch (JsonProcessingException e) {
			throw new RedisParsingException(REDIS_PARSING_ERROR_MESSAGE);
		}
	}

	public <T> Optional<T> get(@NonNull String key, @NonNull Class<T> clazz) {
		String jsonString = stringRedisTemplate.opsForValue().get(key);
		if (!StringUtils.hasText(jsonString)) {
			return Optional.empty();
		}
		try {
			return Optional.of(objectMapper.readValue(jsonString, clazz));
		} catch (JsonProcessingException e) {
			throw new RedisParsingException(REDIS_PARSING_ERROR_MESSAGE);
		}
	}
}
