package com.recordit.server.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NonNull;

public class CustomObjectMapper {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static <T> T readValue(@NonNull String str, @NonNull Class<T> clazz) {
		try {
			return objectMapper.readValue(str, clazz);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("잘못된 Json값이나 Class Type을 입력했습니다.");
		}
	}
}
