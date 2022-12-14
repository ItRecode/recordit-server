package com.recordit.server.exception.redis;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.recordit.server.exception.ErrorMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class RedisExceptionHandler {

	@ExceptionHandler(RedisParsingException.class)
	public ResponseEntity<ErrorMessage> handleRedisParsingException(RedisParsingException exception) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ErrorMessage.of(exception, HttpStatus.INTERNAL_SERVER_ERROR));
	}

}
